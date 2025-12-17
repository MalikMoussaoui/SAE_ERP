<template>
  <div class="page-container">
    <img src="@/assets/logo_iut.png" alt="Logo Université de Limoges" class="main-logo" />

    <div class="login-card">

      <div v-if="isLoading" class="loading-overlay">
        <div class="spinner"></div>
      </div>
      <img src="@/assets/GestIUT_logo.png" alt="Logo GestIUT" class="app-title-logo" />

      <h2>Veuillez vous authentifier</h2>

      <form @submit.prevent="handleLogin">

        <div class="input-group">
          <img src="@/assets/Bonhomme.png" alt="Utilisateur" class="icon-img" />
          <input v-model="username" type="text" placeholder="Nom d'utilisateur (email)" required />
        </div>

        <div class="input-group">
          <img src="@/assets/Cadenas.png" alt="Mot de passe" class="icon-img" />
          <input v-model="password" type="password" placeholder="Mot de passe" required />
        </div>

        <button type="submit">Se Connecter</button>
      </form>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

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
      this.isLoading = true;

      try {

        const response = await axios.post('/api/auth/login', {
          email: this.username,
          password: this.password
        });

        console.log('Connexion réussie', response.data);

        if (response.data.token) {
          localStorage.setItem('user-token', response.data.token);
        }

        this.$router.push('/dashboard');

      } catch (error) {
        console.error("Erreur de connexion", error);

        if (error.response && (error.response.status === 403 || error.response.status === 401)) {
          this.errorMessage = 'Identifiants incorrects.';
        } else if (error.code === 'ERR_NETWORK') {
           this.errorMessage = 'Impossible de joindre le serveur.';
        } else {
          this.errorMessage = 'Une erreur est survenue lors de la connexion.';
        }
      } finally {
        this.isLoading = false;
      }
    }
  }
};
</script>

<style scoped>
/* @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@600;700&family=Montserrat:wght@500&display=swap'); */

.page-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #f8f9fa;
  font-family: 'Montserrat', sans-serif;
  padding: 2rem 0;
  box-sizing: border-box;
}

.main-logo {
  width: 235px;
  margin-bottom: 2rem;
}

.login-card {
  background: #ffffff;
  padding: 2.5rem 3rem;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.07);
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
  color: #333;
  margin-bottom: 2rem;
  font-family: 'Poppins', sans-serif;
  margin-top: 1.5rem;
  opacity: 60%;
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
  border-bottom: 2px solid #ddd;
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
  border-bottom-color: #C00000;
}

/* Style pour vos images d'icônes */
.input-group .icon-img {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  width: 20px;
  height: 20px;
  opacity: 1;
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
  animation: spin 2.4s linear infinite; /* L'animation de rotation */
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>