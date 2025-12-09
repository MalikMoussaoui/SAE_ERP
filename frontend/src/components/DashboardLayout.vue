<template>
  <div class="dashboard-layout" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
    <nav class="sidebar">
      <div class="sidebar-header">
        <img src="@/assets/Logo_G.png" alt="Logo GestIUT" class="sidebar-logo" />
        <h3 class="sidebar-title">IUT Gestion</h3>
        <button @click="toggleSidebar" class="toggle-button" aria-label="Toggle sidebar">«</button>
      </div>

      <ul class="nav-links">
        <li v-for="link in navLinks" :key="link.path">
          <router-link :to="link.path" :class="{ active: $route.path === link.path }">
            <img :src="link.icon" :alt="link.text" class="nav-icon" />
            <span class="nav-text">{{ link.text }}</span>
          </router-link>
        </li>
      </ul>
    </nav>

    <main class="main-content">
      <header class="header">
        <slot name="header"></slot>
        
        <div class="user-info">
          <span>
            <img src="@/assets/Bonhomme.png" alt="Admin" class="user-icon" />
            {{ $t('header.admin') }}
          </span>
          <a href="#" @click.prevent="logout">{{ $t('header.logout') }}</a>
        </div>
      </header>

      <slot></slot>
    </main>
  </div>
</template>

<script>
// Importation des icônes pour une gestion centralisée
import iconDashboard from '@/assets/TableauDeBord.png';
import iconFiche from '@/assets/FicheRessource.png';
import iconMcc from '@/assets/MCC.png';
import iconTac from '@/assets/TAC.png';
import iconEnseignant from '@/assets/EnseignantVacataire.png';
import iconRole from '@/assets/GestionRole.png';
import iconParametre from '@/assets/parametre.png';

export default {
  name: 'DashboardLayout',
  data() {
    return {
      isSidebarCollapsed: localStorage.getItem('sidebarCollapsed') === 'true'
    };
  },
  computed: {
    navLinks() {
      return [
        { path: '/dashboard', text: this.$t('nav.dashboard'), icon: iconDashboard },
        { path: '#', text: this.$t('nav.resourceSheets'), icon: iconFiche }, // Chemin temporaire
        { path: '#', text: this.$t('nav.mccc'), icon: iconMcc }, // Chemin temporaire
        { path: '#', text: this.$t('nav.tac'), icon: iconTac }, // Chemin temporaire
        { path: '#', text: this.$t('nav.teachers'), icon: iconEnseignant }, // Chemin temporaire
        { path: '/gestion-utilisateurs', text: this.$t('nav.roleManagement'), icon: iconRole },
        { path: '/parametres', text: this.$t('nav.settings'), icon: iconParametre },
      ]
    }
  },
  methods: {
    toggleSidebar() {
      this.isSidebarCollapsed = !this.isSidebarCollapsed;
      localStorage.setItem('sidebarCollapsed', this.isSidebarCollapsed);
    },
    logout() {
      localStorage.removeItem('user-token');
      this.$router.push('/connexion');
    }
  }
}
</script>

<style scoped>
.dashboard-layout {
  /* --- Variables de Thème --- */
  --font-primary: 'Poppins', sans-serif;
  --font-secondary: 'Montserrat', sans-serif;
  
  /* Thème Clair (Défaut) */
  --color-primary: #C00000;
  --color-primary-dark: #a00000;
  --color-bg: #fcfcfc;
  --color-sidebar-bg: #f8f9fa;
  --color-card-bg: #ffffff;
  --color-text-header: #333;
  --color-text-body: #555;
  --color-text-muted: #888;
  --color-border: #eee;
  --color-hover-bg: #e9ecef;
  --color-active-bg: #e6f0ff;
  --color-active-text: #0056b3;
  --shadow: 0 4px 12px rgba(0, 0, 0, 0.05);

  /* --- Dimensions & Transitions --- */
  --sidebar-width-open: 280px;
  --sidebar-width-closed: 90px;
  --sidebar-transition-duration: 0.3s;
  --sidebar-transition-timing: cubic-bezier(0.65, 0, 0.35, 1);

  display: flex;
  min-height: 100vh;
  background-color: var(--color-bg);
  font-family: var(--font-secondary);
  overflow-x: hidden;
  color: var(--color-text-body);
  transition: background-color 0.3s;
}

html[data-theme="dark"] .dashboard-layout {
  /* Thème Sombre (Overrides) */
  --color-bg: #1a1a1a;
  --color-sidebar-bg: #252525;
  --color-card-bg: #2c2c2c;
  --color-text-header: #f0f0f0;
  --color-text-body: #d0d0d0;
  --color-text-muted: #777;
  --color-border: #3a3a3a;
  --color-hover-bg: #3a3a3e;
  --color-active-bg: #0056b3;
  --color-active-text: #ffffff;
  --shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

.sidebar {
  width: var(--sidebar-width-open);
  flex-shrink: 0;
  background-color: var(--color-sidebar-bg);
  border-right: 1px solid var(--color-border);
  box-sizing: border-box;
  padding: 1.5rem 1.5rem 1.5rem 1rem;
  position: relative;
  transition: width var(--sidebar-transition-duration) var(--sidebar-transition-timing),
              padding var(--sidebar-transition-duration) var(--sidebar-transition-timing),
              background-color 0.3s, border-color 0.3s;
}

.sidebar-header {
  display: flex;
  align-items: center;
  gap: 15px;
  padding-bottom: 1rem;
  margin-bottom: 1rem;
  border-bottom: 1px solid var(--color-border);
  position: relative;
}

.sidebar-logo {
  height: 45px;
  width: auto;
  flex-shrink: 0;
}

.sidebar-title {
  margin: 0;
  font-family: var(--font-primary);
  font-size: 1.4rem;
  font-weight: 700;
  color: var(--color-text-header);
  white-space: nowrap;
  overflow: hidden;
  transition: opacity var(--sidebar-transition-duration) var(--sidebar-transition-timing),
              width var(--sidebar-transition-duration) var(--sidebar-transition-timing);
}

.toggle-button {
  position: absolute;
  top: 50%;
  right: -25px;
  transform: translateY(-50%);
  background-color: var(--color-card-bg);
  border: 1px solid var(--color-border);
  border-left: none;
  border-radius: 0 50% 50% 0;
  width: 25px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 1.2rem;
  color: var(--color-text-body);
  z-index: 10;
  transition: transform var(--sidebar-transition-duration) var(--sidebar-transition-timing), background-color 0.3s, border-color 0.3s;
}
.toggle-button:hover {
  background-color: var(--color-hover-bg);
}

.nav-links { list-style: none; padding: 0; margin: 0; }
.nav-links li { margin-bottom: 0.5rem; }

.nav-links a {
  display: flex;
  align-items: center;
  padding: 0.65rem 1rem;
  text-decoration: none;
  color: var(--color-text-body);
  font-weight: 500;
  border-radius: 8px;
  transition: background-color 0.2s ease, color 0.2s ease;
  white-space: nowrap;
  overflow: hidden;
}

.nav-icon {
  width: 26px;
  height: 26px;
  object-fit: contain;
  margin-right: 10px;
  flex-shrink: 0;
  transition: margin-right var(--sidebar-transition-duration) var(--sidebar-transition-timing);
}

.nav-text {
  transition: opacity var(--sidebar-transition-duration) var(--sidebar-transition-timing),
              width var(--sidebar-transition-duration) var(--sidebar-transition-timing);
}

.nav-links a:hover { background-color: var(--color-hover-bg); }
.nav-links a.active { background-color: var(--color-active-bg); color: var(--color-active-text); font-weight: 600; }

.dashboard-layout.sidebar-collapsed .sidebar { width: var(--sidebar-width-closed); padding-left: 1rem; padding-right: 1rem; }
.dashboard-layout.sidebar-collapsed .sidebar-title,
.dashboard-layout.sidebar-collapsed .nav-text { opacity: 0; width: 0; }
.dashboard-layout.sidebar-collapsed .toggle-button { transform: translateY(-50%) rotate(180deg); border-left: 1px solid var(--color-border); }
.dashboard-layout.sidebar-collapsed .nav-links a {
  padding-left: 1rem;
  padding-right: 1rem;
}
.dashboard-layout.sidebar-collapsed .nav-icon { margin-right: 0; }

.main-content {
  flex-grow: 1;
  padding: 2rem 3rem;
  transition: background-color 0.3s;
  width: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  border-bottom: 1px solid var(--color-border);
  padding-bottom: 1.5rem;
  margin-bottom: 2rem;
}

.user-info {
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  color: var(--color-text-body);
  gap: 1.5rem;
}

.user-info span { display: flex; align-items: center; gap: 8px; }
.user-icon { width: 20px; height: 20px; object-fit: contain; }
.user-info a { color: var(--color-primary); text-decoration: none; font-weight: 600; }
.user-info a:hover { text-decoration: underline; }
</style>