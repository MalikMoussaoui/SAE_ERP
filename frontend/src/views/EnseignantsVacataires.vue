<template>
  <DashboardLayout>
    <template #header>
      <div class="header-welcome">
        <h1 class="page-title">Liste</h1>
      </div>
    </template>

    <div class="page-surface">
      <div class="toolbar">
        <div class="search-wrapper">
          <svg class="search-icon" viewBox="0 0 20 20" fill="none" stroke="currentColor">
            <circle cx="9" cy="9" r="6"></circle>
            <line x1="13.5" y1="13.5" x2="18" y2="18"></line>
          </svg>
          <input
            type="text"
            placeholder="Recherchez: par prénom, nom, etc"
            class="search-input"
            v-model="searchQuery"
          />
        </div>

        <div class="filters-wrapper">
          <select class="filter-select" v-model="selectedPoste">
            <option value="">Filtrer par poste</option>
            <option value="Professeur">Professeur</option>
            <option value="Vacataire">Vacataire</option>
          </select>
          <select class="filter-select" v-model="selectedDept">
            <option value="">Filtrer par departement</option>
            <option value="INFO">INFO</option>
            <option value="MP">MP</option>
            <option value="GEA">GEA</option>
            <option value="TC">TC</option>
            <option value="GMP">GMP</option>
            <option value="MMI">MMI</option>
          </select>
        </div>
      </div>

      <div class="table-card">
        <div class="table-scroll">
          <table>
            <thead>
              <tr>
                <th>Prénom/Nom</th>
                <th>Départements</th>
                <th>Poste</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(person, index) in filteredEnseignants" :key="index">
                <td>{{ person.name }}</td>
                <td>{{ person.dept }}</td>
                <td>{{ person.poste }}</td>
              </tr>
              <tr v-if="filteredEnseignants.length === 0">
                <td colspan="3" class="empty-state">Aucun résultat trouvé</td>
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

export default {
  name: 'EnseignantsVacatairesView',
  components: {
    DashboardLayout
  },
  data() {
    return {
      searchQuery: '',
      selectedPoste: '',
      selectedDept: '',
      enseignants: [
        { name: 'Jean Bon', dept: 'INFO', poste: 'Vacataire' },
        { name: 'Marc Pont', dept: 'MP', poste: 'Professeur' },
        { name: 'Sarah Croche', dept: 'GEA', poste: 'Professeur' },
        { name: 'Marc Assin', dept: 'TC', poste: 'Professeur' },
        { name: 'Luc Etincelle', dept: 'TC', poste: 'Professeur' },
        { name: 'Pat Hibulaire', dept: 'GEA', poste: 'Vacataire' },
        { name: 'Guy Tarre', dept: 'INFO', poste: 'Vacataire' },
        { name: 'Emma Thyste', dept: 'GMP', poste: 'Professeur' },
        { name: 'Jacques Pot', dept: 'MMI', poste: 'Professeur' },
        { name: 'Alain Térieur', dept: 'INFO', poste: 'Professeur' }
      ]
    };
  },
  computed: {
    filteredEnseignants() {
      const searchLower = this.searchQuery.toLowerCase();
      return this.enseignants.filter((person) => {
        const matchesSearch =
          person.name.toLowerCase().includes(searchLower) ||
          person.dept.toLowerCase().includes(searchLower) ||
          person.poste.toLowerCase().includes(searchLower);
        const matchesPoste = this.selectedPoste === '' || person.poste === this.selectedPoste;
        const matchesDept = this.selectedDept === '' || person.dept === this.selectedDept;
        return matchesSearch && matchesPoste && matchesDept;
      });
    }
  }
};
</script>

<style scoped>
.page-title {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--color-text-header, #222);
  margin: 0;
}

.page-surface {
  background: var(--color-card-bg, white);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 20px;
  padding: 20px;
  box-shadow: var(--shadow, 0 4px 6px rgba(0,0,0,0.05));
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.search-wrapper {
  position: relative;
  width: 380px;
}

.search-icon {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-text-muted, #aaa);
  width: 18px;
  height: 18px;
  stroke-width: 2;
}

.search-input {
  width: 100%;
  padding: 10px 15px 10px 42px;
  border-radius: 999px;
  border: 1px solid var(--color-border, #ccc);
  background-color: var(--color-card-bg, white);
  outline: none;
  font-family: var(--font-secondary, 'Montserrat', sans-serif);
  font-size: 0.95rem;
  color: var(--color-text-body, #555);
}

.search-input::placeholder {
  color: var(--color-text-muted, #bbb);
}

.filters-wrapper {
  display: flex;
  gap: 10px;
}

.filter-select {
  padding: 10px 12px;
  border: 1px solid var(--color-border, #ccc);
  border-radius: 5px;
  background-color: var(--color-card-bg, white);
  color: var(--color-text-body, #555);
  font-family: var(--font-secondary, 'Montserrat', sans-serif);
  cursor: pointer;
  outline: none;
  min-width: 180px;
}

.table-card {
  background: var(--color-card-bg, white);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 20px;
  padding: 15px;
  box-shadow: var(--shadow, 0 4px 6px rgba(0,0,0,0.05));
  overflow: hidden;
}

.table-scroll {
  max-height: 460px;
  overflow: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.95rem;
}

th {
  background-color: var(--color-sidebar-bg, #f1f1f1);
  color: var(--color-text-header, #333);
  font-weight: 600;
  text-align: left;
  padding: 12px 18px;
  position: sticky;
  top: 0;
  z-index: 1;
}

td {
  padding: 12px 18px;
  border-bottom: 1px solid var(--color-border, #eee);
  color: var(--color-text-body, #444);
}

tr:nth-child(even) {
  background-color: rgba(0,0,0,0.02);
}

html[data-theme="dark"] tr:nth-child(even) {
  background-color: rgba(255,255,255,0.02);
}

tr:hover {
  background-color: var(--color-hover-bg, #fdfdfd);
}

.empty-state {
  text-align: center;
  padding: 20px;
  color: var(--color-text-muted, #888);
}

.table-scroll::-webkit-scrollbar {
  width: 10px;
}

.table-scroll::-webkit-scrollbar-track {
  background: var(--color-sidebar-bg, #f1f1f1);
  border-radius: 10px;
}

.table-scroll::-webkit-scrollbar-thumb {
  background: var(--color-text-muted, #ccc);
  border-radius: 10px;
}

.table-scroll::-webkit-scrollbar-thumb:hover {
  background: var(--color-text-body, #aaa);
}
</style>
