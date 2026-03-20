<template>
  <DashboardLayout>
    <CustomModal
      v-if="modal.show"
      :title="modal.title"
      :message="modal.message"
      :type="modal.type"
      :showCancel="modal.showCancel"
      :confirmLabel="modal.confirmLabel"
      :cancelLabel="modal.cancelLabel"
      @close="closeModal"
      @confirm="handleModalConfirm"
    />
    <template #header>
      <h1 class="page-title">{{ $t('nav.resourceSheetsList') || 'Liste des Fiches Ressources' }}</h1>
    </template>

    <div class="page-surface list-surface">

      <div class="surface-header-actions">
        <router-link to="/fiche-ressource" class="btn-add">
          {{ $t('common.newSheet') }}
        </router-link>
      </div>

      <div v-if="loading" class="loading-state">
        {{ $t('common.loading') }}
      </div>

      <div v-else-if="!sheets.length" class="empty-state">
        {{ $t('common.noSheetFound') }}
      </div>

      <div v-else class="mccc-cards">
        <div v-for="sheet in sheets" :key="sheet.id" class="mccc-card">

          <div class="card-header">
            <h3 class="ue-title">
              {{ sheet.code || $t('common.noCode') }} - {{ sheet.ue || $t('common.noUE') }}
            </h3>
            <div class="header-tags">
              <span v-if="sheet.validated" class="badge-validated">{{ $t('common.validated') }}</span>
              <span class="chip">{{ sheet.departement || sheet.departmentName }}</span>
            </div>
          </div>

          <div class="card-meta">
            <span class="meta-label">{{ $t('common.lastModified') }}</span>
            <span class="meta-value">{{ formatDate(sheet.updatedAt || sheet.updated_at || sheet.createdAt || sheet.created_at) }}</span>
          </div>

          <div class="card-actions">
            <button
                type="button"
                class="action-button success"
                v-if="canValidate(sheet)"
                @click="validateSheet(sheet.id)"
            >
              {{ $t('common.validate') }}
            </button>

            <router-link
                class="action-button ghost"
                :to="{ name: 'fiche-ressource', query: { id: sheet.id, mode: 'view' } }"
            >
              {{ $t('common.view') }}
            </router-link>

            <router-link
                class="action-button primary"
                v-if="canEditList(sheet)"
                :to="{ name: 'fiche-ressource', query: { id: sheet.id, mode: 'edit' } }"
            >
              {{ $t('common.modify') }}
            </router-link>

            <button
                type="button"
                class="action-button danger"
                v-if="canDeleteList(sheet)"
                @click="deleteSheet(sheet.id)"
            >
              {{ $t('common.delete') }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';
import CustomModal from '@/components/CustomModal.vue';
import axios from 'axios';

export default {
  name: 'ListeFichesRessources',
  components: { DashboardLayout, CustomModal },
  data() {
    return {
      sheets: [],
      loading: true,
      modal: {
        show: false,
        title: '',
        message: '',
        type: 'info',
        showCancel: false,
        confirmLabel: 'OK',
        cancelLabel: 'Annuler',
        action: null
      },
      sheetToDelete: null,
      sheetToValidate: null,
      userRole: localStorage.getItem('userRole')
    };
  },
  created() {
    this.loadEntries();
  },
  methods: {
    async loadEntries() {
      this.loading = true;
      try {
        // Note : Vérifie si ton backend utilise /api/resource-sheets ou /resource-sheets
        const response = await axios.get('/resource-sheets');
        this.sheets = response.data.reverse();
      } catch (error) {
        console.error("Erreur chargement:", error);
      } finally {
        this.loading = false;
      }
    },
    deleteSheet(id) {
      this.sheetToDelete = id;
      this.modal = {
        show: true,
        title: this.$t('common.confirmDeleteTitle'),
        message: this.$t('common.confirmDeleteMsg'),
        type: 'warning',
        showCancel: true,
        confirmLabel: this.$t('common.delete'),
        action: 'delete'
      };
    },
    closeModal() {
      this.modal.show = false;
    },
    handleModalConfirm() {
      if (this.modal.action === 'delete') {
        this.confirmDelete();
      } else if (this.modal.action === 'validate') {
        this.confirmValidate();
      } else {
        this.closeModal();
      }
    },
    validateSheet(id) {
      this.sheetToValidate = id;
      this.modal = {
        show: true,
        title: this.$t('common.confirmValidation'),
        message: this.$t('common.confirmValidationMsg'),
        type: 'info',
        showCancel: true,
        confirmLabel: this.$t('common.validate'),
        action: 'validate'
      };
    },
    async confirmValidate() {
      this.closeModal();
      if (!this.sheetToValidate) return;

      try {
        const response = await axios.post(`/resource-sheets/${this.sheetToValidate}/validate`);
        const index = this.sheets.findIndex(s => s.id === this.sheetToValidate);
        if (index !== -1) {
          this.sheets[index].validated = true;
          this.sheets[index] = { ...this.sheets[index], validated: true }; // trigger reactivity
        }
      } catch (error) {
        console.error("Erreur validation:", error);
        this.modal = {
          show: true,
          title: this.$t('common.error.generic'),
          message: this.$t('common.error.validationFailed'),
          type: 'error',
          showCancel: false,
          confirmLabel: 'OK',
          action: null
        };
      } finally {
        this.sheetToValidate = null;
      }
    },
    async confirmDelete() {
      this.closeModal();
      if (!this.sheetToDelete) return;

      try {
        await axios.delete(`/resource-sheets/${this.sheetToDelete}`);
        this.sheets = this.sheets.filter(s => s.id !== this.sheetToDelete);
      } catch (error) {
        console.error("Erreur suppression:", error);
        this.modal = {
          show: true,
          title: this.$t('common.error.generic'),
          message: this.$t('common.error.deleteFailed'),
          type: 'error',
          showCancel: false,
          confirmLabel: 'OK'
        };
      } finally {
        this.sheetToDelete = null;
      }
    },
    // LOGIQUE DE DATE CORRIGÉE
    formatDate(isoString) {
      if (!isoString) return 'Date inconnue';
      const date = new Date(isoString);
      // Vérifie si la date est valide
      if (isNaN(date.getTime())) return 'Date inconnue';

      return date.toLocaleString('fr-FR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    },
    canValidate(sheet) {
      if (sheet.validated) return false;
      return this.userRole === 'ADMINISTRATEUR' || this.userRole === 'RH';
    },
    canEditList(sheet) {
      if (sheet.validated && this.userRole !== 'ADMINISTRATEUR') return false;
      return true;
    },
    canDeleteList(sheet) {
      if (sheet.validated && this.userRole !== 'ADMINISTRATEUR') return false;
      const email = localStorage.getItem('userEmail') || '';
      return this.userRole === 'ADMINISTRATEUR' || (sheet.createdBy && sheet.createdBy === email);
    }
  }
};
</script>

<style scoped>
/* Le style reste inchangé comme demandé */
.page-title {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 2.2rem;
  font-weight: 700;
  margin: 0;
  color: var(--color-text-header, #222);
}

.page-surface {
  background: var(--color-card-bg, #fff);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 18px;
  padding: 20px;
  box-shadow: var(--shadow, 0 4px 10px rgba(0,0,0,0.05));
}

.surface-header-actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.btn-add {
  background-color: var(--color-primary, #c00000);
  color: white;
  padding: 10px 20px;
  border-radius: 10px;
  text-decoration: none;
  font-weight: 600;
  font-size: 0.95rem;
  box-shadow: 0 4px 6px rgba(192, 0, 0, 0.2);
  transition: transform 0.2s, background-color 0.2s;
}

.btn-add:hover {
  background-color: var(--color-primary-dark, #a00000);
  transform: translateY(-2px);
}

.mccc-cards {
  display: grid;
  grid-template-columns: 1fr;
  gap: 14px;
}

.mccc-card {
  border: 1px solid var(--color-border, #ddd);
  border-radius: 14px;
  padding: 14px 16px;
  background: var(--color-card-bg, #fff);
  box-shadow: 0 8px 16px rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.ue-title {
  margin: 0;
  font-size: 1.1rem;
  color: var(--color-text-header, #333);
  font-weight: 700;
}

.header-tags {
  display: flex;
  align-items: center;
  gap: 8px;
}

.badge-validated {
  padding: 6px 10px;
  border-radius: 999px;
  background: #e6f4ea;
  color: #1e8e3e;
  font-weight: 700;
  font-size: 0.85rem;
  white-space: nowrap;
}

.chip {
  padding: 6px 10px;
  border-radius: 999px;
  background: var(--color-sidebar-bg, #f3f3f3);
  color: var(--color-text-body, #555);
  font-weight: 600;
  font-size: 0.85rem;
  white-space: nowrap;
}

.card-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.95rem;
  color: var(--color-text-body, #555);
}

.meta-label {
  color: var(--color-text-muted, #777);
  font-weight: 600;
}

.meta-value {
  font-weight: 600;
  color: var(--color-text-header, #333);
}

.card-actions {
  display: flex;
  gap: 8px;
  margin-top: 4px;
}

.action-button {
  text-decoration: none;
  padding: 8px 12px;
  border-radius: 10px;
  font-weight: 700;
  font-size: 0.92rem;
  text-align: center;
  transition: transform 0.15s ease, box-shadow 0.15s ease, background 0.15s ease;
  cursor: pointer;
  border: none;
}

.action-button.primary {
  background: var(--color-primary, #c00000);
  color: #fff;
  box-shadow: 0 6px 12px rgba(192, 0, 0, 0.18);
}

.action-button.ghost {
  border: 1px solid var(--color-border, #ddd);
  background: var(--color-card-bg, #fff);
  color: var(--color-text-body, #444);
}

.action-button:hover {
  transform: translateY(-1px);
}

.action-button.primary:hover {
  background: var(--color-primary-dark, #a00000);
}

.action-button.success {
  background: #1e8e3e;
  color: white;
  border: 1px solid #1e8e3e;
}

.action-button.success:hover {
  background: #146c2e;
  transform: translateY(-1px);
}

.action-button.danger {
  background: #fff;
  color: #b00020;
  border: 1px solid #b00020;
}

.action-button.danger:hover {
  background: rgba(176, 0, 32, 0.08);
}

.action-button.ghost:hover {
  background: var(--color-sidebar-bg, #f5f5f5);
}

.empty-state, .loading-state {
  border: 1px dashed var(--color-border, #ddd);
  border-radius: 12px;
  padding: 30px;
  color: var(--color-text-muted, #666);
  text-align: center;
  font-style: italic;
}
</style>