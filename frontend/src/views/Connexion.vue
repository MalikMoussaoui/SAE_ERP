<template>
  <div class="page-container">
    <img src="@/assets/logo_iut.png" alt="Logo Université de Limoges" class="main-logo" />

    <div class="login-card">
      <div v-if="isLoading" class="loading-overlay">
        <div class="spinner"></div>
      </div>

      <img src="@/assets/GestIUT_logo.png" alt="Logo GestIUT" class="app-title-logo" />

      <h2>{{ $t('login.title') }}</h2>

      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <img src="@/assets/Bonhomme.png" alt="Utilisateur" class="icon-img" />
          <input v-model="username" type="text" :placeholder="$t('login.usernamePlaceholder')" required />
        </div>

        <div class="input-group">
          <img src="@/assets/Cadenas.png" alt="Mot de passe" class="icon-img" />
          <input v-model="password" type="password" :placeholder="$t('login.passwordPlaceholder')" required />
        </div>

        <button type="submit" :disabled="isLoading">
          {{ isLoading ? $t('common.connecting') : $t('login.connect') }}
        </button>
      </form>

      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script>
// 1. IMPORT D'AXIOS
import axios from 'axios';
import logoG from '@/assets/Logo_G.png';

export default {
  name: 'LoginScreen',
  data() {
    return {
      username: '', // Utilisé comme email pour le backend
      password: '',
      errorMessage: '',
      isLoading: false
    };
  },
  methods: {
    async handleLogin() {
      this.errorMessage = '';
      this.isLoading = true;

      try {
        const response = await axios.post('http://164.81.120.78:8080/api/auth/login', {
          email: this.username,
          password: this.password
        });

        const token = response.data.token;
        localStorage.setItem('user-token', token);

        // Redirection vers le dashboard
        this.$router.push('/dashboard');

      } catch (error) {
        // 4. GESTION DES ERREURS
        console.error("Erreur de connexion:", error);

        if (error.response) {
          // Erreur venant du serveur
          this.errorMessage = this.$t('login.error');
        } else if (error.request) {
          // Le serveur n'a pas répondu
          this.errorMessage = this.$t('common.error.serverUnreachable');
        } else {
          this.errorMessage = this.$t('common.error.generic');
        }
      } finally {
        this.isLoading = false;
      }
    }
  },
  created() {
    const theme = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-theme', theme);
    document.title = "Gest'IUT";
  },
  mounted() {
    const theme = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-theme', theme);
  }
};
</script>

<style scoped>
.page-container {
  /* --- VARIABLES DE THEME (Mode Clair par défaut) --- */
  --bg-color: #f8f9fa;
  --card-bg: #ffffff;
  --text-main: #333;
  --color-primary: #C00000;
  --color-primary-dark: #a00000;
  --color-primary-rgb: 192, 0, 0;
  --text-muted: rgba(51, 51, 51, 0.6); /* Remplace color #333 + opacity 60% */
  --input-border: #ddd;
  --input-focus: #C00000;
  --shadow-color: rgba(0, 0, 0, 0.07);
  --icon-filter: none;
  --overlay-bg: rgba(255, 255, 255, 0.9);
  --spinner-border: #f3f3f3;

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: var(--bg-color);
  font-family: 'Montserrat', sans-serif;
  padding: 2rem 0;
  box-sizing: border-box;
}

.main-logo {
  width: 235px;
  margin-bottom: 2rem;
}

.login-card {
  background: var(--card-bg);
  padding: 2.5rem 3rem;
  border-radius: 20px;
  box-shadow: 0 10px 30px var(--shadow-color);
  text-align: center;
  width: 100%;
  max-width: 420px;
  box-sizing: border-box;
  position: relative;
}

.app-title-logo {
  max-width: 310px; 
  width: 100%;
  height: auto;
  margin-bottom: -1rem;
  margin-top: -0.75rem;
}


h2 {
  font-size: 1.3rem;
  font-weight: 600;
  color: var(--text-main);
  margin-bottom: 2rem;
  font-family: 'Poppins', sans-serif;
  margin-top: 1.5rem;
  opacity: 0.6;
}

.input-group {
  position: relative;
  margin-bottom: 2rem;
  text-align: left;
}

.input-group input {
  width: 100%;
  padding: 12px 12px 12px 40px; /* Espace à gauche pour l'icône */
  border: none;
  border-bottom: 2px solid var(--input-border);
  font-size: 1rem;
  transition: border-color 0.3s;
  background-color: transparent;
  box-sizing: border-box;
  font-family: 'Montserrat', sans-serif;
  color: var(--text-main);
}

/* Corrige le fond jaune de l'autocomplétion Chrome */
.input-group input:-webkit-autofill,
.input-group input:-webkit-autofill:hover,
.input-group input:-webkit-autofill:focus,
.input-group input:-webkit-autofill:active {
  -webkit-box-shadow: 0 0 0 30px var(--card-bg, white) inset !important;
  box-shadow: 0 0 0 30px var(--card-bg, white) inset !important;
  -webkit-text-fill-color: var(--text-main, #333) !important;
}

.input-group input:focus {
  outline: none;
  border-bottom-color: var(--color-primary);
}

.input-group .icon-img {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  width: 20px;
  height: 20px;
  filter: var(--icon-filter);
}

button {
  width: 100%;
  padding: 14px;
  border: none;
  border-radius: 10px;
  background-color: var(--color-primary);
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  margin-top: 1rem;
  font-family: 'Poppins', sans-serif;
}

button:hover {
  background-color: var(--color-primary-dark);
  box-shadow: 0 5px 15px rgba(var(--color-primary-rgb, 192, 0, 0), 0.3);
}

button:focus {
  outline: none;
}

.error-message {
  color: var(--color-primary);
  margin-top: 1.5rem;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  font-weight: 500;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.9); /* Fond blanc semi-transparent */
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 20px; /* Doit être le même que .login-card */
  z-index: 10;
}

.spinner {
  width: 100px;
  height: 100px;
  border: 5px solid #f3f3f3; /* Le cercle gris clair */
  border-top: 5px solid #C00000; /* Le bout rouge */
  border-radius: 50%;
  animation: spin 2.4s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

</style>

<style>
/* --- DEFINITION DU MODE SOMBRE (GLOBAL) --- */
/* Sorti du scoped pour garantir la priorité sur html[data-theme="dark"] */

html[data-theme="dark"] .page-container {
  --bg-color: #121212;
  --card-bg: #1e1e1e;
  --text-main: #f5f5f5;
  --text-muted: rgba(245, 245, 245, 0.9);
  --input-border: #555;
  --shadow-color: rgba(0, 0, 0, 0.5);
  --icon-filter: brightness(0) invert(1);
  --overlay-bg: rgba(30, 30, 30, 0.9);
  --spinner-border: #444;
  --color-primary: #C00000; /* Rouge standard conservé */
  --color-primary-dark: #a00000;
}

html[data-theme="dark"] .input-group input:-webkit-autofill,
html[data-theme="dark"] .input-group input:-webkit-autofill:hover,
html[data-theme="dark"] .input-group input:-webkit-autofill:focus,
html[data-theme="dark"] .input-group input:-webkit-autofill:active {
  -webkit-box-shadow: 0 0 0 30px #1e1e1e inset !important;
  box-shadow: 0 0 0 30px #1e1e1e inset !important;
  -webkit-text-fill-color: #f5f5f5 !important;
}
</style>