<template>
  <DashboardLayout>
    <template #header>
      <div class="header-welcome">
        <h1 class="page-title">{{ $t('roleManagement.title') }}</h1>
      </div>
    </template>

    <div class="page-surface">
      <div class="toolbar">
        <div class="toolbar-left">
          <button @click="showCreateForm = !showCreateForm" class="btn btn-primary">{{ $t('roleManagement.newUser') }}</button>
        </div>
        <div class="search-wrapper">
        <input type="text" :placeholder="$t('roleManagement.searchPlaceholder')" class="search-input" v-model="searchQuery" />
      </div>

      <div class="filters-wrapper">
        <select class="filter-select" v-model="selectedPoste">
          <option value="">{{ $t('roleManagement.allPositions') }}</option>
          <option value="TEACHER">{{ $t('roleManagement.professor') }}</option>
          <option value="VACATAIRE">{{ $t('roleManagement.contractor') }}</option>
          <option value="RESPONSABLE_PEDAGOGIQUE">{{ $t('roleManagement.responsablePedagogique') }}</option>
          <option value="ADMINISTRATEUR">{{ $t('roleManagement.administrator') }}</option>
          <option value="RH">{{ $t('roleManagement.rh') }}</option>
        </select>
        <select class="filter-select" v-model="selectedDept">
          <option value="">{{ $t('roleManagement.filterByDept') }}</option>
          <option value="INFO">INFO</option>
          <option value="GEA">GEA</option>
          <option value="TC">TC</option>
        </select>
      </div>
    </div>

    <!-- Create User Form (Admin only, visible when toggled) -->
    <div v-if="showCreateForm" class="create-user-form">
      <h3>{{ $t('roleManagement.addUserTitle') }}</h3>
      <div class="form-row">
        <input type="text" v-model="newUser.username" :placeholder="$t('roleManagement.usernamePlaceholder')" class="form-input" />
        <input type="email" v-model="newUser.email" :placeholder="$t('roleManagement.emailPlaceholder')" class="form-input" />
      </div>
      <div class="form-row">
        <input type="password" v-model="newUser.password" :placeholder="$t('roleManagement.passwordPlaceholder')" class="form-input" />
        <select v-model="newUser.departmentName" class="filter-select">
          <option value="">{{ $t('roleManagement.selectDepartment') }}</option>
          <option value="INFO">INFO</option>
          <option value="GEA">GEA</option>
          <option value="TC">TC</option>
          <option value="MMI">MMI</option>
          <option value="GMP">GMP</option>
        </select>
        <select v-model="newUser.roles" class="filter-select">
          <option value="">{{ $t('roleManagement.selectRole') }}</option>
          <option value="TEACHER">{{ $t('roleManagement.professor') }}</option>
          <option value="VACATAIRE">{{ $t('roleManagement.contractor') }}</option>
          <option value="RESPONSABLE_PEDAGOGIQUE">{{ $t('roleManagement.responsablePedagogique') }}</option>
          <option value="ADMINISTRATEUR">{{ $t('roleManagement.administrator') }}</option>
        </select>
      </div>
      <div class="form-actions">
        <button @click="createUser" class="btn btn-primary" :disabled="!isFormValid">{{ $t('roleManagement.createUser') }}</button>
        <button @click="showCreateForm = false" class="btn btn-secondary">{{ $t('roleManagement.cancel') }}</button>
      </div>
    </div>

    <div class="table-card">
      <div class="table-scroll">
        <table>
          <thead>
            <tr>
              <th>{{ $t('roleManagement.table.name') }}</th>
              <th>Email</th>
              <th>{{ $t('roleManagement.table.departments') }}</th>
              <th>{{ $t('roleManagement.table.position') }}</th>
              <th>{{ $t('roleManagement.table.action') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(user, index) in filteredUsers" :key="index">
              <td>{{ user.displayName || 'N/A' }}</td>
              <td>{{ user.email || 'N/A' }}</td>
              <td>{{ user.departement || 'N/A' }}</td>
              <td>{{ getPosteDisplay(user) }}</td>
              <td class="actions">
                <a href="#" class="action-link" @click.prevent="deleteUser(user.id)">{{ $t('roleManagement.table.delete') }}</a>
              </td>
            </tr>
            <tr v-if="filteredUsers.length === 0">
              <td colspan="5" style="text-align:center; padding: 20px; color: #888;">
                {{ $t('roleManagement.table.noUserFound') }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';
import axios from 'axios';

export default {
  name: 'GestionUtilisateurs',
  components: {
    DashboardLayout
  },
  data() {
    return {
      searchQuery: '',
      selectedPoste: '',
      selectedDept: '',
      users: [],
      showCreateForm: false,
      newUser: {
        username: '',
        email: '',
        password: '',
        departmentName: '',
        roles: ''
      }
    };
  },
  async mounted() {
    await this.fetchUsers();
  },
  computed: {
    filteredUsers() {
      return this.users.filter(user => {
        const searchLower = this.searchQuery.toLowerCase();
        
        // Handle potentially null/undefined values safely
        const name = (user.displayName || '').toLowerCase();
        const dept = (user.departement || '').toLowerCase();
        const poste = (user.role || '').toLowerCase();

        const matchesSearch = 
          name.includes(searchLower) ||
          dept.includes(searchLower) ||
          poste.includes(searchLower);
          
        const matchesPoste = this.selectedPoste === '' || (user.role && user.role === this.selectedPoste);
        const matchesDept = this.selectedDept === '' || dept === this.selectedDept.toLowerCase();
        return matchesSearch && matchesPoste && matchesDept;
      });
    },
    isFormValid() {
      return this.newUser.username && this.newUser.email && this.newUser.password && this.newUser.departmentName && this.newUser.roles;
    }
  },
  methods: {
    getPosteDisplay(user) {
      const p = user.role || '';
      if (p.includes('Professeur') || p.includes('TEACHER')) return this.$t('roleManagement.professor') || 'Professeur';
      if (p.includes('Vacataire') || p.includes('VACATAIRE')) return this.$t('roleManagement.contractor') || 'Vacataire';
      if (p.includes('RESPONSABLE_PEDAGOGIQUE')) return 'Responsable Peda.';
      if (p.includes('ADMINISTRATEUR')) return 'Admin';
      if (p.includes('RH')) return 'RH';
      if (p.includes('REFERENT')) return 'Référent';
      return p;
    },
    async fetchUsers() {
      const token = localStorage.getItem('user-token');
      if (!token) return;
      try {
        const response = await axios.get('/app-users', {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.users = response.data;
      } catch (error) {
        console.error("Erreur chargement utilisateurs:", error);
      }
    },
    async createUser() {
      if (!this.isFormValid) return;
      const token = localStorage.getItem('user-token');
      
      const payload = {
        email: this.newUser.email,
        displayName: this.newUser.username,
        passwordHash: this.newUser.password,
        departement: this.newUser.departmentName,
        role: this.newUser.roles,
        status: 'ACTIVE'
      };

      try {
        await axios.post('/app-users', payload, {
          headers: { Authorization: `Bearer ${token}` }
        });
        alert('Utilisateur créé avec succès');
        this.showCreateForm = false;
        this.newUser = { username: '', email: '', password: '', departmentName: '', roles: '' };
        await this.fetchUsers();
      } catch (e) {
        console.error("Erreur lors de la création :", e);
        alert('Erreur lors de la création de l\'utilisateur.');
      }
    },
    async deleteUser(id) {
      if (!id || !confirm("Etes-vous sûr de vouloir supprimer cet utilisateur ?")) return;
      const token = localStorage.getItem('user-token');
      try {
        await axios.delete(`/app-users/${id}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        await this.fetchUsers();
      } catch (e) {
        console.error("Erreur lors de la suppression :", e);
      }
    }
  }
}
</script>

<style scoped>
.page-surface {
  background: var(--color-card-bg, white);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 20px;
  padding: 20px;
  box-shadow: var(--shadow, 0 4px 6px rgba(0,0,0,0.05));
}

.page-title {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--color-text-header, #222);
  margin: 0;
}

/* --- Barre d'outils --- */
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; }
.search-wrapper { position: relative; width: 350px; }
.search-icon { position: absolute; left: 15px; top: 50%; transform: translateY(-50%); color: var(--color-text-muted, #aaa); font-size: 0.9rem; }
.search-input { width: 100%; padding: 10px 15px 10px 40px; border-radius: 25px; border: 1px solid var(--color-border, #ccc); background-color: var(--color-card-bg, white); outline: none; font-family: var(--font-secondary); font-size: 0.9rem; color: var(--color-text-body, #555); }
.search-input::placeholder { color: var(--color-text-muted, #bbb); }
.filters-wrapper { display: flex; gap: 10px; }
.filter-select { padding: 8px 12px; border: 1px solid var(--color-border, #ccc); border-radius: 5px; background-color: var(--color-card-bg, white); color: var(--color-text-body, #555); font-family: var(--font-secondary, 'Montserrat', sans-serif); cursor: pointer; outline: none; min-width: 180px; }

/* --- Carte Tableau --- */
.table-card { background: var(--color-card-bg, white); border: 1px solid var(--color-border, #ddd); border-radius: 20px; padding: 20px; box-shadow: var(--shadow, 0 4px 6px rgba(0,0,0,0.05)); overflow: hidden; margin-bottom: 2rem; }
.table-scroll { max-height: 450px; overflow-y: auto; }
table { width: 100%; border-collapse: collapse; font-size: 0.95rem; }
th { background-color: var(--color-sidebar-bg, #f1f1f1); color: var(--color-text-header, #333); font-weight: 600; text-align: left; padding: 15px 20px; position: sticky; top: 0; z-index: 1; }
td { padding: 12px 20px; border-bottom: 1px solid var(--color-border, #eee); color: var(--color-text-body, #444); }
tr:nth-child(even) { background-color: rgba(0,0,0,0.02); }
html[data-theme="dark"] tr:nth-child(even) { background-color: rgba(255,255,255,0.02); }
tr:hover { background-color: var(--color-hover-bg, #fdfdfd); }
.actions { text-align: right; }
.action-link { color: var(--color-primary, #C00000); text-decoration: none; margin-left: 10px; font-size: 0.9rem; font-weight: 500; }
.action-link:hover { text-decoration: underline; }

.table-scroll::-webkit-scrollbar { width: 8px; }
.table-scroll::-webkit-scrollbar-track { background: var(--color-sidebar-bg, #f1f1f1); }
.table-scroll::-webkit-scrollbar-thumb { background: var(--color-text-muted, #ccc); border-radius: 4px; }
.table-scroll::-webkit-scrollbar-thumb:hover { background: var(--color-text-body, #aaa); }

.create-user-form { margin-bottom: 20px; padding: 20px; border: 1px solid var(--color-border); border-radius: 12px; background: var(--color-sidebar-bg); }
.create-user-form h3 { margin-top: 0; color: var(--color-primary); }
.form-row { display: flex; gap: 15px; margin-bottom: 15px; }
.form-input { flex: 1; padding: 10px; border: 1px solid var(--color-border); border-radius: 8px; font-family: inherit; }
.form-actions { display: flex; gap: 10px; justify-content: flex-end; }
.btn { padding: 8px 16px; border-radius: 8px; font-weight: 600; cursor: pointer; border: none; }
.btn-primary { background-color: var(--color-primary); color: white; }
.btn-primary:disabled { opacity: 0.5; cursor: not-allowed; }
.btn-secondary { background-color: transparent; border: 1px solid var(--color-border); color: var(--color-text-body); }
</style>
