<template>
  <DashboardLayout>
    <template #header>
      <h1 class="page-title">Gestionnaire</h1>
    </template>

    <section class="form-section">
      <div class="form-card">
        <h2>Ajouter/Modifier un utilisateur</h2>
        <form @submit.prevent="submitForm">
          <div class="form-row">
            <div class="form-group">
              <label for="prenom">Prenom</label>
              <input id="prenom" v-model="form.prenom" type="text" placeholder="Entrer le prenom" />
            </div>
            <div class="form-group">
              <label for="poste">Poste</label>
              <input id="poste" v-model="form.poste" type="text" placeholder="Entrer le poste" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="nom">Nom</label>
              <input id="nom" v-model="form.nom" type="text" placeholder="Entrer le nom" />
            </div>
            <div class="form-group">
              <label for="departement">Departements</label>
              <input id="departement" v-model="form.departement" type="text" placeholder="Entrer le departement" />
            </div>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn-primary">Valider</button>
          </div>
        </form>
      </div>
      <div class="back-action">
        <button class="btn-secondary" @click="$router.push('/gestion-utilisateurs')">Retour</button>
      </div>
    </section>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';

export default {
  name: 'AjoutUtilisateur',
  components: { DashboardLayout },
  data() {
    return {
      form: {
        prenom: '',
        nom: '',
        poste: '',
        departement: ''
      }
    };
  },
  methods: {
    submitForm() {
      console.log('Utilisateur soumis :', this.form);
    }
  }
}
</script>

<style scoped>
.page-title {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 2.4rem;
  font-weight: 700;
  color: var(--color-text-header, #222);
  margin: 0;
}

.form-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
}

.form-card {
  width: 100%;
  max-width: 920px;
  background: var(--color-card-bg, white);
  border: 1px solid var(--color-border, #d7e0ff);
  border-radius: 16px;
  box-shadow: 0 18px 40px rgba(0, 0, 0, 0.06);
  padding: 2.25rem 2.5rem;
}

.form-card h2 {
  margin: 0 0 1.5rem 0;
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 1.6rem;
  color: var(--color-text-header, #222);
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 1rem 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

.form-group label {
  font-weight: 700;
  color: var(--color-text-header, #333);
  font-size: 1rem;
}

.form-group input {
  padding: 12px 14px;
  border: 1px solid #ccd5ff;
  border-radius: 10px;
  font-family: var(--font-secondary, 'Montserrat', sans-serif);
  font-size: 0.98rem;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.form-group input:focus {
  border-color: var(--color-primary, #C00000);
  box-shadow: 0 0 0 3px rgba(192, 0, 0, 0.12);
}

.form-actions {
  display: flex;
  justify-content: center;
  margin-top: 1.8rem;
}

.btn-primary,
.btn-secondary {
  background-color: var(--color-primary, #C00000);
  color: white;
  border: none;
  padding: 12px 34px;
  border-radius: 26px;
  font-size: 1.05rem;
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-weight: 700;
  cursor: pointer;
  box-shadow: 0 12px 22px rgba(192, 0, 0, 0.28);
  transition: background 0.2s, transform 0.1s;
}

.btn-primary:hover,
.btn-secondary:hover {
  background-color: var(--color-primary-dark, #a00000);
}

.btn-primary:active,
.btn-secondary:active {
  transform: translateY(1px);
}

.back-action {
  display: flex;
  justify-content: center;
  width: 100%;
  margin-top: 0.5rem;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
  .form-card {
    padding: 1.75rem 1.5rem;
  }
}
</style>
