<template>
  <div class="dashboard-layout" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
    <nav class="sidebar">
      <div class="sidebar-header">
        <img src="@/assets/Logo_G.png" alt="Logo GestIUT" class="sidebar-logo" />
        <h3 class="sidebar-title">Gest'IUT</h3>
        <button @click="toggleSidebar" class="toggle-button" aria-label="Toggle sidebar">
          <svg viewBox="0 0 24 24" width="20" height="20" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="15 18 9 12 15 6"></polyline>
          </svg>
        </button>
      </div>

      <ul class="nav-links">
        <li v-for="item in navItems" :key="item.key || item.path">
          <template v-if="item.type === 'group'">
            <button
              type="button"
              class="nav-group"
              :class="{ open: isGroupOpen(item.key), active: isGroupActive(item) }"
              @click="toggleGroup(item.key)"
            >
              <img :src="item.icon" :alt="item.text" class="nav-icon" />
              <span class="nav-text">{{ item.text }}</span>
              <span class="nav-caret" aria-hidden="true">
                <svg viewBox="0 0 20 20" focusable="false" aria-hidden="true">
                  <path d="M7 4l7 6-7 6V4z" />
                </svg>
              </span>
            </button>
            <ul v-if="isGroupOpen(item.key)" class="nav-sublist">
              <li v-for="child in item.children" :key="child.path">
                <router-link :to="child.path" :class="{ active: isActive(child.path) }">
                  <span class="nav-text">{{ child.text }}</span>
                </router-link>
              </li>
            </ul>
          </template>
          <template v-else>
            <router-link :to="item.path" :class="{ active: isActive(item.path) }">
              <img :src="item.icon" :alt="item.text" class="nav-icon" />
              <span class="nav-text">{{ item.text }}</span>
            </router-link>
          </template>
        </li>
      </ul>

      <div class="sidebar-footer">
        <router-link to="/aide" :class="{ active: isActive('/aide') }">
          <svg class="nav-icon svg-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10"></circle><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"></path><line x1="12" y1="17" x2="12.01" y2="17"></line>
          </svg>
          <span class="nav-text">{{ $t('nav.help') }}</span>
        </router-link>
      </div>
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
import iconDashboard from '@/assets/TableauDeBord.png';
import iconFiche from '@/assets/FicheRessource.png';
import iconMcc from '@/assets/MCC.png';
import iconTac from '@/assets/TAC.png';
import iconEnseignant from '@/assets/EnseignantVacataire.png';
import iconRole from '@/assets/GestionRole.png';
import iconParametre from '@/assets/parametre.png';
import logoG from '@/assets/Logo_G.png';

export default {
  name: 'DashboardLayout',
  data() {
    return {
      isSidebarCollapsed: localStorage.getItem('sidebarCollapsed') === 'true',
      openGroups: {
        resourceSheets: false,
        mccc: false
      }
    };
  },
  computed: {
    navItems() {
      return [
        { type: 'link', path: '/dashboard', text: this.$t('nav.dashboard'), icon: iconDashboard },
        {
          type: 'group',
          key: 'resourceSheets',
          text: this.$t('nav.resourceSheets'),
          icon: iconFiche,
          children: [
            { path: '/fiche-ressource', text: this.$t('nav.resourceSheets') },
            { path: '/liste-fiches-ressources', text: this.$t('nav.resourceSheetsList') }
          ]
        },
        {
          type: 'group',
          key: 'mccc',
          text: this.$t('nav.mccc'),
          icon: iconMcc,
          children: [
            { path: '/mccc', text: this.$t('nav.mccc') },
            { path: '/liste-mccc', text: this.$t('nav.mcccList') }
          ]
        },
        { type: 'link', path: '/tac', text: this.$t('nav.tac'), icon: iconTac },
        { type: 'link', path: '/enseignants', text: this.$t('nav.teachers'), icon: iconEnseignant },
        { type: 'link', path: '/user-management', text: this.$t('nav.roleManagement'), icon: iconRole },
        { type: 'link', path: '/settings', text: this.$t('nav.settings'), icon: iconParametre },
      ]
    }
  },
  created() {
    const theme = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-theme', theme);

    document.title = "Gest'IUT";
    let link = document.querySelector("link[rel~='icon']");
    if (!link) {
      link = document.createElement('link');
      link.rel = 'icon';
      document.head.appendChild(link);
    }
    link.href = logoG;
  },
  watch: {
    '$route.path': {
      immediate: true,
      handler() {
        this.syncOpenGroups();
      }
    }
  },
  methods: {
    isActive(path) {
      if (path === '#') return false;
      return this.$route.path === path || this.$route.path.startsWith(path + '/');
    },
    isGroupActive(item) {
      return item.children.some(child => this.isActive(child.path));
    },
    isGroupOpen(key) {
      return this.openGroups[key];
    },
    toggleGroup(key) {
      this.openGroups[key] = !this.openGroups[key];
    },
    syncOpenGroups() {
      this.navItems
        .filter(item => item.type === 'group')
        .forEach(item => {
          if (this.isGroupActive(item)) {
            this.openGroups[item.key] = true;
          }
        });
    },
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

<style>
/* Définition des variables de thème globales pour toute l'application */
:root {
  /* Thème Clair (Défaut) */
  --color-primary: #C00000;
  --color-primary-rgb: 192, 0, 0;
  --color-primary-dark: #a00000;
  --color-bg: #fcfcfc;
  --color-sidebar-bg: #f8f9fa;
  --color-card-bg: #ffffff;
  --color-text-header: #333;
  --color-text-body: #555;
  --color-text-muted: #888;
  --color-border: #eee;
  --color-overlay-bg: rgba(255, 255, 255, 0.9);
  --color-hover-bg: #e9ecef;
  --color-active-bg: #e6f0ff;
  --color-active-text: #0056b3;
  --shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

html[data-theme="dark"] {
  /* Thème Sombre (Overrides) */
  --color-primary: #C00000;
  --color-primary-rgb: 192, 0, 0;
  --color-bg: #1a1a1a;
  --color-sidebar-bg: #252525;
  --color-card-bg: #2c2c2c;
  --color-text-header: #f5f5f5;
  --color-text-body: #d0d0d0;
  --color-text-muted: #777;
  --color-border: #3a3a3a;
  --color-overlay-bg: rgba(26, 26, 26, 0.9);
  --color-hover-bg: #3a3a3e;
  --color-active-bg: #0056b3;
  --color-active-text: #ffffff;
  --shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}
</style>

<style scoped>
.dashboard-layout {
  --font-primary: 'Poppins', sans-serif;
  --font-secondary: 'Montserrat', sans-serif;
  --sidebar-width-open: 280px;
  --sidebar-width-closed: 90px;
  --sidebar-transition-duration: 0.3s;
  --sidebar-transition-timing: cubic-bezier(0.65, 0, 0.35, 1);

  display: flex;
  height: 100vh;
  background-color: var(--color-bg);
  font-family: var(--font-secondary);
  overflow: hidden;
  color: var(--color-text-body);
  transition: background-color 0.3s;
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
  display: flex;
  flex-direction: column;
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
  right: -40px;
  transform: translateY(-50%);
  background-color: var(--color-card-bg);
  border: 1px solid var(--color-border);
  border-radius: 50%;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: var(--color-text-muted);
  z-index: 10;
  transition: transform var(--sidebar-transition-duration) var(--sidebar-transition-timing), 
              background-color 0.3s, 
              border-color 0.3s,
              color 0.3s,
              box-shadow 0.3s;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}
.toggle-button:hover {
  background-color: var(--color-primary);
  border-color: var(--color-primary);
  color: white;
  box-shadow: 0 4px 8px rgba(var(--color-primary-rgb), 0.3);
}

.nav-links { 
  list-style: none; 
  padding: 0; 
  margin: 0; 
  flex: 1; /* Prend l'espace disponible pour pousser le footer en bas */
  overflow-y: auto; /* Permet le scroll si le menu est trop grand */
  scrollbar-width: thin; /* Firefox: barre fine */
  scrollbar-color: var(--color-border) transparent;
}
.nav-links::-webkit-scrollbar { 
  width: 4px; /* Largeur pour Webkit */
}
.nav-links::-webkit-scrollbar-thumb {
  background-color: var(--color-border);
  border-radius: 4px;
}
.nav-links li { margin-bottom: 0.5rem; }

.nav-links a,
.nav-group,
.sidebar-footer a {
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

.nav-group {
  width: 100%;
  background: none;
  border: none;
  cursor: pointer;
  text-align: left;
  font-family: inherit;
  font-size: inherit;
}

.nav-caret {
  margin-left: auto;
  transition: transform 0.2s ease;
}

.nav-caret svg {
  width: 14px;
  height: 14px;
  fill: currentColor;
  display: block;
}

.nav-group.open .nav-caret {
  transform: rotate(90deg);
}

.nav-sublist {
  list-style: none;
  padding: 0;
  margin: 0.25rem 0 0.75rem;
}

.nav-sublist a {
  padding: 0.5rem 1rem 0.5rem 2.75rem;
  font-size: 0.95rem;
}

.nav-icon,
.svg-icon {
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

.nav-links a:hover, .nav-group:hover, .sidebar-footer a:hover { background-color: var(--color-hover-bg); }
.nav-links a.active, .nav-group.active, .sidebar-footer a.active { background-color: var(--color-active-bg); color: var(--color-active-text); font-weight: 600; }

.sidebar-footer {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid var(--color-border);
}
.svg-icon { stroke: currentColor; }

.dashboard-layout.sidebar-collapsed .sidebar { width: var(--sidebar-width-closed); padding-left: 1rem; padding-right: 1rem; }
.dashboard-layout.sidebar-collapsed .sidebar-title,
.dashboard-layout.sidebar-collapsed .nav-text { opacity: 0; width: 0; }
.dashboard-layout.sidebar-collapsed .toggle-button { transform: translateY(-50%) rotate(180deg); right: -32px; }
.dashboard-layout.sidebar-collapsed .nav-links a,
.dashboard-layout.sidebar-collapsed .nav-group,
.dashboard-layout.sidebar-collapsed .sidebar-footer a {
  padding-left: 1rem;
  padding-right: 1rem;
}
.dashboard-layout.sidebar-collapsed .nav-icon { margin-right: 0; }
.dashboard-layout.sidebar-collapsed .nav-caret,
.dashboard-layout.sidebar-collapsed .nav-sublist { display: none; }

.main-content {
  flex-grow: 1;
  padding: 2rem 3rem;
  transition: background-color 0.3s;
  overflow-y: auto;

  /* Masquer la barre de défilement tout en gardant la fonctionnalité */
  -ms-overflow-style: none;  /* IE et Edge */
  scrollbar-width: none;  /* Firefox */
}

/* Masquer la barre de défilement pour Webkit (Chrome, Safari) */
.main-content::-webkit-scrollbar {
    display: none;
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
