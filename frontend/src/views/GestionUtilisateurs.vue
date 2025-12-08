<template>
  <div class="dashboard-layout" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">

    <nav class="sidebar">
      <div class="sidebar-header">
        <img src="@/assets/Logo_G.png" alt="Logo GestIUT" class="sidebar-logo" />
        <h3 class="sidebar-title">IUT Gestion</h3>
        <button @click="toggleSidebar" class="toggle-button">«</button>
      </div>

      <ul class="nav-links">
        <li>
          <a href="#" @click.prevent="$router.push('/dashboard')">
            <img src="@/assets/TableauDeBord.png" class="nav-icon" />
            <span class="nav-text">Tableau de bord</span>
          </a>
        </li>
        
        <li>
          <a href="#">
            <img src="@/assets/FicheRessource.png" class="nav-icon" />
            <span class="nav-text">Fiches ressources</span>
          </a>
        </li>
        <li>
          <a href="#">
            <img src="@/assets/MCC.png" class="nav-icon" />
            <span class="nav-text">MCCC</span>
          </a>
        </li>
        <li>
          <a href="#">
            <img src="@/assets/TAC.png" class="nav-icon" />
            <span class="nav-text">TAC</span>
          </a>
        </li>
        <li>
          <a href="#">
            <img src="@/assets/EnseignantVacataire.png" class="nav-icon" />
            <span class="nav-text">Enseignants & Vacataires</span>
          </a>
        </li>

        <li>
          <a href="#" class="active">
            <img src="@/assets/GestionRole.png" class="nav-icon" />
            <span class="nav-text">Gestion des rôles</span>
          </a>
        </li>
        
        <li>
          <a href="#">
            <img src="@/assets/parametre.png" class="nav-icon" />
            <span class="nav-text">Paramètres</span>
          </a>
        </li>
      </ul>
    </nav>

    <main class="main-content">
      <header class="header">
        <div class="header-welcome">
          <h1 class="page-title">Gestionnaire</h1>
        </div>
        <div class="user-info">
          <span>
            <img src="@/assets/Bonhomme.png" alt="Admin" class="user-icon" />
            Administrateur
          </span>
          <a href="#" @click.prevent="logout">Déconnexion</a>
        </div>
      </header>

      <div class="toolbar">
        <div class="search-wrapper">
          <span class="search-icon">🔍</span>
          <input type="text" placeholder="Recherchez par prénom, nom, etc" class="search-input" v-model="searchQuery" />
        </div>

        <div class="filters-wrapper">
          <select class="filter-select" v-model="selectedPoste">
            <option value="">Filtrer par poste</option>
            <option value="Professeur">Professeur</option>
            <option value="Vacataire">Vacataire</option>
          </select>
          <select class="filter-select" v-model="selectedDept">
            <option value="">Filtrer par département</option>
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
                <th>Prénom/Nom</th>
                <th>Départements</th>
                <th>Poste</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(user, index) in filteredUsers" :key="index">
                <td>{{ user.name }}</td>
                <td>{{ user.dept }}</td>
                <td>{{ user.poste }}</td>
                <td class="actions">
                  <a href="#" class="action-link">[Modifier]</a>
                  <a href="#" class="action-link">[Supprimer]</a>
                </td>
              </tr>
              <tr v-if="filteredUsers.length === 0">
                <td colspan="4" style="text-align:center; padding: 20px; color: #888;">
                  Aucun utilisateur trouvé.
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="footer-actions">
        <button class="btn-add" @click="$router.push('/gestion-utilisateurs/ajouter')">Ajouter un utilisateur</button>
      </div>

    </main>
  </div>
</template>

<script>
export default {
  name: 'GestionUtilisateurs',
  data() {
    return {
      isSidebarCollapsed: false,
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
  },
  methods: {
    toggleSidebar() {
      this.isSidebarCollapsed = !this.isSidebarCollapsed;
    },
    logout() {
      localStorage.removeItem('user-token');
      this.$router.push('/connexion');
    }
  }
}
</script>

<style scoped>
/* --- Variables CSS --- */
.dashboard-layout {
  --font-primary: 'Poppins', sans-serif;
  --font-secondary: 'Montserrat', sans-serif;
  --color-primary: #C00000;
  --color-primary-dark: #a00000;
  --color-grey-light: #f8f9fa;
  --color-grey-dark: #333;
  --color-text: #555;
  --color-border: #eee;
  --shadow: 0 4px 6px rgba(0,0,0,0.05);
  --sidebar-width-open: 260px;
  --sidebar-width-closed: 80px;
  --sidebar-transition: 0.3s ease;

  display: flex;
  min-height: 100vh;
  background-color: #fcfcfc;
  font-family: var(--font-secondary);
  overflow-x: hidden;
}

/* --- Styles Sidebar --- */
.sidebar { width: var(--sidebar-width-open); flex-shrink: 0; background: var(--color-grey-light); border-right: 1px solid var(--color-border); padding: 1.5rem; position: relative; transition: width var(--sidebar-transition), padding var(--sidebar-transition); }
.sidebar-header { display: flex; align-items: center; gap: 15px; padding-bottom: 1rem; margin-bottom: 1rem; border-bottom: 1px solid var(--color-border); position: relative; }
.sidebar-logo { height: 45px; width: auto; flex-shrink: 0; }
.sidebar-title { margin: 0; font-family: var(--font-primary); font-size: 1.4rem; font-weight: 700; color: #333; white-space: nowrap; overflow: hidden; }
.toggle-button { position: absolute; top: 50%; right: -25px; transform: translateY(-50%); background: white; border: 1px solid var(--color-border); border-left: none; border-radius: 0 50% 50% 0; width: 25px; height: 40px; cursor: pointer; z-index: 10; }
.nav-links { list-style: none; padding: 0; margin: 0; }
.nav-links li { margin-bottom: 0.5rem; }
.nav-links a { display: flex; align-items: center; padding: 0.75rem 1rem; text-decoration: none; color: var(--color-text); font-weight: 500; border-radius: 8px; transition: 0.2s; white-space: nowrap; overflow: hidden; }
.nav-icon { width: 24px; height: 24px; object-fit: contain; margin-right: 12px; flex-shrink: 0; }
.nav-links a:hover { background-color: #e9ecef; }
.nav-links a.active { background-color: #e6f0ff; color: #0056b3; font-weight: 600; }

.dashboard-layout.sidebar-collapsed .sidebar { width: var(--sidebar-width-closed); padding-left: 1rem; padding-right: 1rem; }
.dashboard-layout.sidebar-collapsed .sidebar-title, .dashboard-layout.sidebar-collapsed .nav-text { opacity: 0; width: 0; }
.dashboard-layout.sidebar-collapsed .nav-links a { justify-content: center; padding-left: 0.5rem; padding-right: 0.5rem; }
.dashboard-layout.sidebar-collapsed .nav-icon { margin-right: 0; }
.dashboard-layout.sidebar-collapsed .toggle-button { transform: translateY(-50%) rotate(180deg); right: -25px; border-left: 1px solid var(--color-border); }

/* --- Main Content --- */
.main-content {
  flex-grow: 1;
  padding: 2rem 3rem;
  width: 100%;
}

.header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; }
.page-title { font-family: var(--font-primary); font-size: 2.5rem; font-weight: 700; color: #222; margin: 0; }
.user-info { display: flex; align-items: center; font-size: 0.9rem; color: var(--color-text); gap: 1.5rem; }
.user-info span { display: flex; align-items: center; gap: 8px; }
.user-icon { width: 20px; height: 20px; object-fit: contain; }
.user-info a { color: var(--color-primary); text-decoration: none; font-weight: 600; }

/* --- Barre d'outils --- */
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; }
.search-wrapper { position: relative; width: 350px; }
.search-icon { position: absolute; left: 15px; top: 50%; transform: translateY(-50%); color: #aaa; font-size: 0.9rem; }
.search-input { width: 100%; padding: 10px 15px 10px 40px; border-radius: 25px; border: 1px solid #ccc; outline: none; font-family: var(--font-secondary); font-size: 0.9rem; color: #555; }
.search-input::placeholder { color: #bbb; }
.filters-wrapper { display: flex; gap: 10px; }
.filter-select { padding: 8px 12px; border: 1px solid #ccc; border-radius: 5px; background-color: white; color: #555; font-family: var(--font-secondary); cursor: pointer; outline: none; min-width: 180px; }

/* --- Carte Tableau --- */
.table-card { background: white; border: 1px solid #ddd; border-radius: 12px; padding: 0; box-shadow: var(--shadow); overflow: hidden; margin-bottom: 2rem; }
.table-scroll { max-height: 450px; overflow-y: auto; }
table { width: 100%; border-collapse: collapse; font-size: 0.95rem; }
th { background-color: #f1f1f1; color: #333; font-weight: 600; text-align: left; padding: 15px 20px; position: sticky; top: 0; z-index: 1; }
td { padding: 12px 20px; border-bottom: 1px solid #eee; color: #444; }
tr:nth-child(even) { background-color: #fafafa; }
tr:hover { background-color: #fdfdfd; }
.actions { text-align: right; }
.action-link { color: var(--color-primary); text-decoration: none; margin-left: 10px; font-size: 0.9rem; font-weight: 500; }
.action-link:hover { text-decoration: underline; }

/* --- Bouton Ajouter --- */
.footer-actions { display: flex; justify-content: center; padding-bottom: 2rem; }
.btn-add { background-color: var(--color-primary); color: white; border: none; padding: 12px 30px; border-radius: 25px; font-size: 1.1rem; font-family: var(--font-primary); font-weight: 600; cursor: pointer; box-shadow: 0 4px 10px rgba(192, 0, 0, 0.2); transition: background 0.3s; }
.btn-add:hover { background-color: var(--color-primary-dark); }

.table-scroll::-webkit-scrollbar { width: 8px; }
.table-scroll::-webkit-scrollbar-track { background: #f1f1f1; }
.table-scroll::-webkit-scrollbar-thumb { background: #ccc; border-radius: 4px; }
.table-scroll::-webkit-scrollbar-thumb:hover { background: #aaa; }
</style>