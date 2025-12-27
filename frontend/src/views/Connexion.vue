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

        <button type="submit">{{ $t('login.connect') }}</button>
      </form>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginScreen',
  data() {
    return {
      username: '',
      password: '',
      errorMessage: '',
      isLoading: false
    }; 
  },
  
  methods: {
    async handleLogin() {
      this.errorMessage = ''; 
      this.isLoading = true; // On active le spinner

      try {
        // --- SIMULATION (puisqu'il n'y a pas de back-end) ---
        
        if (this.username === 'admin' && this.password === 'password') {
          // Connexion réussie : pas de délai pour une meilleure expérience utilisateur.
          localStorage.setItem('user-token', 'fake-token-for-simulation');
          this.$router.push('/dashboard');

        } else {
          // On simule une attente seulement en cas d'erreur
          // pour afficher le spinner et décourager les tentatives rapides.
          await new Promise(resolve => setTimeout(resolve, 500));
          throw new Error(this.$t('login.error'));
        }

      } catch (error) {
        this.errorMessage = this.$t('login.error');
        this.isLoading = false;
      }
    }
  }
};
</script>

<style scoped>
.page-container {
  /* --- VARIABLES DE THEME (Mode Clair par défaut) --- */
  --bg-color: #f8f9fa;
  --card-bg: #ffffff;
  --text-main: #333;
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
  color: var(--text-main);
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
  position: relative; /* <-- NÉCESSAIRE pour l'overlay */
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
  color: var(--text-muted);
  margin-bottom: 2rem;
  font-family: 'Poppins', sans-serif;
  margin-top: 1.5rem;
  /* opacity géré par la variable de couleur */
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
  color: var(--text-main);
  font-size: 1rem;
  transition: border-color 0.3s;
  background-color: transparent;
  box-sizing: border-box;
  font-family: 'Montserrat', sans-serif;
}

/* Corrige le fond jaune de l'autocomplétion Chrome */
.input-group input:-webkit-autofill,
.input-group input:-webkit-autofill:hover,
.input-group input:-webkit-autofill:focus,
.input-group input:-webkit-autofill:active {
  -webkit-box-shadow: 0 0 0 30px white inset !important;
  box-shadow: 0 0 0 30px white inset !important;
  -webkit-text-fill-color: #333 !important;
}

.input-group input:focus {
  outline: none;
  border-bottom-color: var(--input-focus);
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
  background-color: #C00000;
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  margin-top: 1rem;
  font-family: 'Poppins', sans-serif;
}

button:hover {
  background-color: #a00000;
  box-shadow: 0 5px 15px rgba(192, 0, 0, 0.3);
}

button:focus {
  outline: none;
}

.error-message {
  color: #C00000;
  margin-top: 1.5rem;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  font-weight: 500;
}


/* --- STYLES DU SPINNER AJOUTÉS --- */

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: var(--overlay-bg);
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 20px; /* Doit être le même que .login-card */
  z-index: 10;
}

.spinner {
  width: 100px;
  height: 100px;
  border: 5px solid var(--spinner-border);
  border-top: 5px solid #C00000; /* Le bout rouge */
  border-radius: 50%;
  animation: spin 2.4s linear infinite; /* L'animation de rotation */
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* --- DEFINITION DU MODE SOMBRE --- */
/* Appliqué si préférence système OU classe .dark sur body/html */

@media (prefers-color-scheme: dark) {
  .page-container {
    --bg-color: #121212;
    --card-bg: #1e1e1e;
    --text-main: #f5f5f5;
    --text-muted: rgba(245, 245, 245, 0.9);
    --input-border: #555;
    --shadow-color: rgba(0, 0, 0, 0.5);
    --icon-filter: brightness(0) invert(1);
    --overlay-bg: rgba(30, 30, 30, 0.9);
    --spinner-border: #444;
  }
  .input-group input:-webkit-autofill,
  .input-group input:-webkit-autofill:hover,
  .input-group input:-webkit-autofill:focus,
  .input-group input:-webkit-autofill:active {
    -webkit-box-shadow: 0 0 0 30px #1e1e1e inset !important;
    box-shadow: 0 0 0 30px #1e1e1e inset !important;
    -webkit-text-fill-color: #f5f5f5 !important;
  }
}

/* Support manuel via classe .dark (ex: toggle dans l'app) */
:global(.dark) .page-container {
  --bg-color: #121212;
  --card-bg: #1e1e1e;
  --text-main: #f5f5f5;
  --text-muted: rgba(245, 245, 245, 0.9);
  --input-border: #555;
  --shadow-color: rgba(0, 0, 0, 0.5);
  --icon-filter: brightness(0) invert(1);
  --overlay-bg: rgba(30, 30, 30, 0.9);
  --spinner-border: #444;
}

:global(.dark) .input-group input:-webkit-autofill,
:global(.dark) .input-group input:-webkit-autofill:hover,
:global(.dark) .input-group input:-webkit-autofill:focus,
:global(.dark) .input-group input:-webkit-autofill:active {
  -webkit-box-shadow: 0 0 0 30px #1e1e1e inset !important;
  box-shadow: 0 0 0 30px #1e1e1e inset !important;
  -webkit-text-fill-color: #f5f5f5 !important;
}
</style>