<template>
  <DashboardLayout>
    <template #header>
      <h1 class="page-title">{{ $t('parametrage.title') }}</h1>
    </template>

    <div class="page-surface">

      <!-- ===== SECTION DÉPARTEMENTS ===== -->
      <section class="section">
        <div class="section-header">
          <h2 class="section-title">{{ $t('parametrage.departmentsTitle') }}</h2>
          <button class="btn btn-primary" @click="showDeptForm = !showDeptForm">
            + {{ $t('parametrage.addDepartment') }}
          </button>
        </div>

        <!-- Formulaire ajout département -->
        <div v-if="showDeptForm" class="inline-form">
          <input
            v-model="newDeptLabel"
            class="form-input"
            :placeholder="$t('parametrage.deptLabelPlaceholder')"
            @keyup.enter="createDepartment"
          />
          <button class="btn btn-primary" :disabled="!newDeptLabel.trim()" @click="createDepartment">
            {{ $t('parametrage.save') }}
          </button>
          <button class="btn btn-secondary" @click="showDeptForm = false; newDeptLabel = ''">
            {{ $t('parametrage.cancel') }}
          </button>
          <span v-if="deptError" class="error-text">{{ deptError }}</span>
        </div>

        <div v-if="loadingDepts" class="empty-state">{{ $t('common.loading') }}</div>
        <div v-else-if="!departments.length" class="empty-state">{{ $t('parametrage.noDepts') }}</div>
        <table v-else class="data-table">
          <thead>
            <tr>
              <th>{{ $t('parametrage.deptLabel') }}</th>
              <th class="col-actions">{{ $t('parametrage.actions') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="dept in departments" :key="dept.id">
              <td>{{ dept.label }}</td>
              <td class="col-actions">
                <button class="action-button danger" @click="deleteDepartment(dept)">
                  {{ $t('parametrage.delete') }}
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </section>

      <!-- ===== SECTION UEs ===== -->
      <section class="section">
        <div class="section-header">
          <h2 class="section-title">{{ $t('parametrage.uesTitle') }}</h2>
          <button class="btn btn-primary" @click="openAddUeForm">
            + {{ $t('parametrage.addUe') }}
          </button>
        </div>

        <!-- Filtre par département -->
        <div class="filters-bar">
          <label class="filter-label">{{ $t('parametrage.filterByDept') }}</label>
          <select v-model="ueFilterDeptId" class="filter-select" @change="loadUes">
            <option value="">{{ $t('parametrage.allDepts') }}</option>
            <option v-for="dept in departments" :key="dept.id" :value="dept.id">{{ dept.label }}</option>
          </select>
        </div>

        <!-- Formulaire ajout/édition UE -->
        <div v-if="showUeForm" class="inline-form ue-form">
          <select v-model="ueForm.departmentId" class="form-input">
            <option value="">{{ $t('parametrage.selectDept') }}</option>
            <option v-for="dept in departments" :key="dept.id" :value="dept.id">{{ dept.label }}</option>
          </select>
          <input v-model="ueForm.title" class="form-input" :placeholder="$t('parametrage.ueTitlePlaceholder')" />
          <select v-model.number="ueForm.semester" class="form-input semester-select">
            <option value="0" disabled>{{ $t('parametrage.selectSemester') }}</option>
            <option v-for="s in [1,2,3,4,5,6]" :key="s" :value="s">S{{ s }}</option>
          </select>
          <input v-model="ueForm.objectives" class="form-input" :placeholder="$t('parametrage.ueObjectivesPlaceholder')" />
          <button class="btn btn-primary" :disabled="!isUeFormValid" @click="saveUe">
            {{ $t('parametrage.save') }}
          </button>
          <button class="btn btn-secondary" @click="cancelUeForm">
            {{ $t('parametrage.cancel') }}
          </button>
          <span v-if="ueError" class="error-text">{{ ueError }}</span>
        </div>

        <div v-if="loadingUes" class="empty-state">{{ $t('common.loading') }}</div>
        <div v-else-if="!filteredUes.length" class="empty-state">{{ $t('parametrage.noUes') }}</div>
        <table v-else class="data-table">
          <thead>
            <tr>
              <th>{{ $t('parametrage.ueTitle') }}</th>
              <th>{{ $t('parametrage.semester') }}</th>
              <th>{{ $t('parametrage.department') }}</th>
              <th>{{ $t('parametrage.objectives') }}</th>
              <th class="col-actions">{{ $t('parametrage.actions') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="ue in filteredUes" :key="ue.id">
              <td>{{ ue.title }}</td>
              <td>S{{ ue.semester }}</td>
              <td>{{ ue.department ? ue.department.label : '-' }}</td>
              <td class="objectives-cell">{{ ue.objectives || '-' }}</td>
              <td class="col-actions">
                <button class="action-button primary" @click="openEditUeForm(ue)">
                  {{ $t('parametrage.edit') }}
                </button>
                <button class="action-button danger" @click="deleteUe(ue)">
                  {{ $t('parametrage.delete') }}
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </section>

    </div>

    <!-- Modal confirmation suppression -->
    <div v-if="confirmModal.show" class="modal-overlay" @click.self="confirmModal.show = false">
      <div class="modal-box">
        <h3 class="modal-title">{{ confirmModal.title }}</h3>
        <p class="modal-message">{{ confirmModal.message }}</p>
        <div class="modal-actions">
          <button class="btn btn-danger" @click="confirmModal.onConfirm">{{ $t('parametrage.delete') }}</button>
          <button class="btn btn-secondary" @click="confirmModal.show = false">{{ $t('parametrage.cancel') }}</button>
        </div>
      </div>
    </div>

  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';
import axios from 'axios';

export default {
  name: 'ParametrageFormation',
  components: { DashboardLayout },
  data() {
    return {
      departments: [],
      loadingDepts: false,
      showDeptForm: false,
      newDeptLabel: '',
      deptError: '',

      allUes: [],
      loadingUes: false,
      ueFilterDeptId: '',
      showUeForm: false,
      editingUeId: null,
      ueForm: { departmentId: '', title: '', semester: 0, objectives: '' },
      ueError: '',

      confirmModal: { show: false, title: '', message: '', onConfirm: null }
    };
  },
  computed: {
    filteredUes() {
      if (!this.ueFilterDeptId) return this.allUes;
      return this.allUes.filter(ue => ue.department && ue.department.id === this.ueFilterDeptId);
    },
    isUeFormValid() {
      return this.ueForm.departmentId && this.ueForm.title.trim() && this.ueForm.semester > 0;
    }
  },
  created() {
    this.loadDepartments();
    this.loadUes();
  },
  methods: {
    async loadDepartments() {
      this.loadingDepts = true;
      try {
        const res = await axios.get('/departments');
        this.departments = res.data;
      } catch (e) {
        console.error('Erreur chargement départements', e);
      } finally {
        this.loadingDepts = false;
      }
    },
    async createDepartment() {
      this.deptError = '';
      if (!this.newDeptLabel.trim()) return;
      try {
        const res = await axios.post('/departments', { label: this.newDeptLabel.trim() });
        this.departments.push(res.data);
        this.newDeptLabel = '';
        this.showDeptForm = false;
      } catch (e) {
        this.deptError = e.response?.data || this.$t('parametrage.errorSave');
      }
    },
    deleteDepartment(dept) {
      this.confirmModal = {
        show: true,
        title: this.$t('parametrage.confirmDeleteTitle'),
        message: this.$t('parametrage.confirmDeleteDept', { label: dept.label }),
        onConfirm: async () => {
          this.confirmModal.show = false;
          try {
            await axios.delete(`/departments/${dept.id}`);
            this.departments = this.departments.filter(d => d.id !== dept.id);
            this.allUes = this.allUes.filter(u => !u.department || u.department.id !== dept.id);
          } catch (e) {
            alert(e.response?.data || this.$t('parametrage.errorDelete'));
          }
        }
      };
    },

    async loadUes() {
      this.loadingUes = true;
      try {
        const res = await axios.get('/ues');
        this.allUes = res.data;
      } catch (e) {
        console.error('Erreur chargement UEs', e);
      } finally {
        this.loadingUes = false;
      }
    },
    openAddUeForm() {
      this.editingUeId = null;
      this.ueForm = { departmentId: this.ueFilterDeptId || '', title: '', semester: 0, objectives: '' };
      this.ueError = '';
      this.showUeForm = true;
    },
    openEditUeForm(ue) {
      this.editingUeId = ue.id;
      this.ueForm = {
        departmentId: ue.department ? ue.department.id : '',
        title: ue.title,
        semester: ue.semester,
        objectives: ue.objectives || ''
      };
      this.ueError = '';
      this.showUeForm = true;
    },
    cancelUeForm() {
      this.showUeForm = false;
      this.editingUeId = null;
      this.ueError = '';
    },
    async saveUe() {
      this.ueError = '';
      const payload = {
        title: this.ueForm.title.trim(),
        semester: this.ueForm.semester,
        objectives: this.ueForm.objectives.trim(),
        departmentId: this.ueForm.departmentId
      };
      try {
        if (this.editingUeId) {
          const res = await axios.put(`/ues/${this.editingUeId}`, payload);
          const idx = this.allUes.findIndex(u => u.id === this.editingUeId);
          if (idx !== -1) this.allUes[idx] = res.data;
        } else {
          const res = await axios.post('/ues', payload);
          this.allUes.push(res.data);
        }
        this.cancelUeForm();
      } catch (e) {
        this.ueError = e.response?.data || this.$t('parametrage.errorSave');
      }
    },
    deleteUe(ue) {
      this.confirmModal = {
        show: true,
        title: this.$t('parametrage.confirmDeleteTitle'),
        message: this.$t('parametrage.confirmDeleteUe', { title: ue.title }),
        onConfirm: async () => {
          this.confirmModal.show = false;
          try {
            await axios.delete(`/ues/${ue.id}`);
            this.allUes = this.allUes.filter(u => u.id !== ue.id);
          } catch (e) {
            alert(e.response?.data || this.$t('parametrage.errorDelete'));
          }
        }
      };
    }
  }
};
</script>

<style scoped>
.page-surface {
  background: var(--color-card-bg, #fff);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 18px;
  padding: 28px;
  box-shadow: var(--shadow, 0 4px 10px rgba(0,0,0,0.05));
  display: flex;
  flex-direction: column;
  gap: 40px;
}

.section { display: flex; flex-direction: column; gap: 16px; }

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.section-title {
  font-size: 1.2rem;
  font-weight: 700;
  color: var(--color-text-header, #222);
  margin: 0;
}

.filters-bar {
  display: flex;
  align-items: center;
  gap: 12px;
}

.filter-label {
  font-weight: 600;
  font-size: 0.9rem;
  color: var(--color-text-muted, #666);
}

.filter-select {
  padding: 8px 12px;
  border: 1px solid var(--color-border, #ddd);
  border-radius: 8px;
  font-size: 0.9rem;
  background: var(--color-card-bg, #fff);
  color: var(--color-text-body, #333);
  min-width: 180px;
}

.inline-form {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
  padding: 14px 16px;
  background: var(--color-sidebar-bg, #f8f8f8);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 12px;
}

.ue-form { flex-wrap: wrap; }

.form-input {
  padding: 8px 12px;
  border: 1px solid var(--color-border, #ddd);
  border-radius: 8px;
  font-size: 0.9rem;
  background: var(--color-card-bg, #fff);
  color: var(--color-text-body, #333);
  flex: 1;
  min-width: 150px;
}

.semester-select { flex: 0 0 90px; min-width: 90px; }

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.92rem;
}

.data-table th {
  text-align: left;
  padding: 10px 14px;
  border-bottom: 2px solid var(--color-border, #ddd);
  font-weight: 700;
  color: var(--color-text-muted, #666);
  font-size: 0.85rem;
  text-transform: uppercase;
  letter-spacing: 0.3px;
}

.data-table td {
  padding: 10px 14px;
  border-bottom: 1px solid var(--color-border, #eee);
  color: var(--color-text-body, #333);
  vertical-align: middle;
}

.data-table tbody tr:hover { background: var(--color-sidebar-bg, #f9f9f9); }

.col-actions { width: 1%; white-space: nowrap; text-align: right; }

.objectives-cell {
  max-width: 260px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.action-button {
  padding: 6px 12px;
  border-radius: 8px;
  font-weight: 600;
  font-size: 0.85rem;
  border: none;
  cursor: pointer;
  margin-left: 6px;
  transition: background 0.15s;
}

.action-button.primary {
  background: var(--color-primary, #c00000);
  color: #fff;
}
.action-button.primary:hover { background: var(--color-primary-dark, #a00000); }

.action-button.danger {
  background: #fff;
  color: #b00020;
  border: 1px solid #b00020;
}
.action-button.danger:hover { background: rgba(176,0,32,0.08); }

.btn {
  padding: 8px 18px;
  border-radius: 10px;
  font-weight: 600;
  font-size: 0.92rem;
  border: none;
  cursor: pointer;
  transition: background 0.15s;
}
.btn:disabled { opacity: 0.5; cursor: not-allowed; }
.btn.btn-primary { background: var(--color-primary, #c00000); color: #fff; }
.btn.btn-primary:hover:not(:disabled) { background: var(--color-primary-dark, #a00000); }
.btn.btn-secondary { background: var(--color-sidebar-bg, #eee); color: var(--color-text-body, #333); border: 1px solid var(--color-border, #ccc); }
.btn.btn-secondary:hover { background: #e0e0e0; }
.btn.btn-danger { background: #b00020; color: #fff; }
.btn.btn-danger:hover { background: #8a0018; }

.empty-state {
  border: 1px dashed var(--color-border, #ddd);
  border-radius: 10px;
  padding: 24px;
  text-align: center;
  color: var(--color-text-muted, #888);
  font-style: italic;
}

.error-text { color: #b00020; font-size: 0.85rem; }

.page-title {
  font-size: 2.2rem;
  font-weight: 700;
  margin: 0;
  color: var(--color-text-header, #222);
}

/* Modal */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-box {
  background: var(--color-card-bg, #fff);
  border-radius: 16px;
  padding: 28px 32px;
  max-width: 420px;
  width: 90%;
  box-shadow: 0 12px 40px rgba(0,0,0,0.2);
}
.modal-title { margin: 0 0 10px; font-size: 1.1rem; font-weight: 700; color: var(--color-text-header, #222); }
.modal-message { margin: 0 0 20px; color: var(--color-text-body, #444); font-size: 0.95rem; }
.modal-actions { display: flex; gap: 10px; justify-content: flex-end; }
</style>
