<template>
  <DashboardLayout>
    <template #header>
      <div class="header-welcome">
        <h1 class="page-title">{{ $t('roleManagement.title') }}</h1>
      </div>
    </template>

    <div class="page-surface">
      <div class="toolbar">
        <div class="search-wrapper">
        <span class="search-icon">🔍</span>
        <input type="text" :placeholder="$t('roleManagement.searchPlaceholder')" class="search-input" v-model="searchQuery" />
      </div>

      <div class="filters-wrapper">
        <select class="filter-select" v-model="selectedPoste">
          <option value="">{{ $t('roleManagement.filterByPosition') }}</option>
          <option value="Professeur">{{ $t('roleManagement.professor') }}</option>
          <option value="Vacataire">{{ $t('roleManagement.contractor') }}</option>
        </select>
        <select class="filter-select" v-model="selectedDept">
          <option value="">{{ $t('roleManagement.filterByDept') }}</option>
          <option value="INFO">INFO</option>
          <option value="GEA">GEA</option>
          <option value="TC">TC</option>
        </select>
      </div>
    </div>

    <div class="table-card">
      <div class="table-scroll">
        <table>
          <thead>
            <tr>
              <th>{{ $t('roleManagement.table.name') }}</th>
              <th>{{ $t('roleManagement.table.departments') }}</th>
              <th>{{ $t('roleManagement.table.position') }}</th>
              <th>{{ $t('roleManagement.table.action') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(user, index) in filteredUsers" :key="index">
              <td>{{ user.name }}</td>
              <td>{{ user.dept }}</td>
              <td>{{ user.poste === 'Professeur' ? $t('roleManagement.professor') : $t('roleManagement.contractor') }}</td>
              <td class="actions">
                <a href="#" class="action-link">{{ $t('roleManagement.table.edit') }}</a>
                <a href="#" class="action-link">{{ $t('roleManagement.table.delete') }}</a>
              </td>
            </tr>
            <tr v-if="filteredUsers.length === 0">
              <td colspan="4" style="text-align:center; padding: 20px; color: #888;">
                {{ $t('roleManagement.table.noUserFound') }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="footer-actions">
      <button class="btn-add" @click="$router.push('/user-management/add')">{{ $t('roleManagement.addUser') }}</button>
    </div>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';

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
      users: [
        { name: 'Jean Bon', dept: 'INFO', poste: 'Vacataire' },
        { name: 'Ella Tion', dept: 'MP', poste: 'Professeur' },
        { name: 'Sarah Croche', dept: 'GEA', poste: 'Professeur' },
        { name: 'Marc Assin', dept: 'TC', poste: 'Professeur' },
        { name: 'Luc Etincelle', dept: 'TC', poste: 'Professeur' },
        { name: 'Pat Hibulaire', dept: 'GEA', poste: 'Vacataire' },
        { name: 'Guy Tarre', dept: 'INFO', poste: 'Vacataire' },
        { name: 'Emma Thyste', dept: 'GMP', poste: 'Professeur' },
        { name: 'Jacques Pot', dept: 'MMI', poste: 'Professeur' },
        { name: 'Alain Térieur', dept: 'INFO', poste: 'Professeur' },
        { name: 'Alex Terieur', dept: 'INFO', poste: 'Vacataire' }, 
        { name: 'Sarah Vigote', dept: 'GEA', poste: 'Professeur' },
      ]
    };
  },
  computed: {
    filteredUsers() {
      return this.users.filter(user => {
        const searchLower = this.searchQuery.toLowerCase();
        const matchesSearch = 
          user.name.toLowerCase().includes(searchLower) ||
          user.dept.toLowerCase().includes(searchLower) ||
          user.poste.toLowerCase().includes(searchLower);
        const matchesPoste = this.selectedPoste === '' || user.poste === this.selectedPoste;
        const matchesDept = this.selectedDept === '' || user.dept === this.selectedDept;
        return matchesSearch && matchesPoste && matchesDept;
      });
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

/* --- Bouton Ajouter --- */
.footer-actions { display: flex; justify-content: center; padding-bottom: 2rem; }
.btn-add { background-color: var(--color-primary, #C00000); color: white; border: none; padding: 12px 30px; border-radius: 25px; font-size: 1.1rem; font-family: var(--font-primary, 'Poppins', sans-serif); font-weight: 600; cursor: pointer; box-shadow: 0 4px 10px rgba(192, 0, 0, 0.2); transition: background 0.3s; }
.btn-add:hover { background-color: var(--color-primary-dark, #a00000); }

.table-scroll::-webkit-scrollbar { width: 8px; }
.table-scroll::-webkit-scrollbar-track { background: var(--color-sidebar-bg, #f1f1f1); }
.table-scroll::-webkit-scrollbar-thumb { background: var(--color-text-muted, #ccc); border-radius: 4px; }
.table-scroll::-webkit-scrollbar-thumb:hover { background: var(--color-text-body, #aaa); }
</style>
