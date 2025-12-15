<template>
  <DashboardLayout>
    <template #header>
      <h1 class="page-title">{{ $t('addUser.headerTitle') }}</h1>
    </template>

    <div class="page-surface">
      <section class="form-section">
        <div class="form-card">
          <h2>{{ $t('addUser.formTitle') }}</h2>
          <form @submit.prevent="submitForm">
            <div class="form-row">
              <div class="form-group">
                <label for="firstName">{{ $t('addUser.firstName') }}</label>
                <input id="firstName" v-model="form.firstName" type="text" :placeholder="$t('addUser.firstNamePlaceholder')" />
              </div>
              <div class="form-group">
                <label for="position">{{ $t('addUser.position') }}</label>
                <input id="position" v-model="form.position" type="text" :placeholder="$t('addUser.positionPlaceholder')" />
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label for="lastName">{{ $t('addUser.lastName') }}</label>
                <input id="lastName" v-model="form.lastName" type="text" :placeholder="$t('addUser.lastNamePlaceholder')" />
              </div>
              <div class="form-group">
                <label for="department">{{ $t('addUser.department') }}</label>
                <input id="department" v-model="form.department" type="text" :placeholder="$t('addUser.departmentPlaceholder')" />
              </div>
            </div>
            <div class="form-actions">
              <button type="submit" class="btn-primary">{{ $t('addUser.validate') }}</button>
            </div>
          </form>
        </div>
        <div class="back-action">
          <button class="btn-secondary" @click="$router.push('/user-management')">{{ $t('addUser.back') }}</button>
        </div>
      </section>
    </div>
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
        firstName: '',
        lastName: '',
        position: '',
        department: ''
      }
    };
  },
  methods: {
    submitForm() {
      console.log('User submitted:', this.form);
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

.page-surface {
  background: var(--color-card-bg, white);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 20px;
  padding: 20px;
  box-shadow: var(--shadow, 0 4px 6px rgba(0,0,0,0.05));
}

.form-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
  width: 100%;
  padding: 0 2.75rem;
  box-sizing: border-box;
}

.form-card {
  width: 100%;
  max-width: 920px;
  background: var(--color-card-bg, white);
  border: 1px solid var(--color-border, #d7e0ff);
  border-radius: 20px;
  box-shadow: 0 18px 40px rgba(0, 0, 0, 0.06);
  padding: 20px;
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
  .form-section {
    padding: 0 1.25rem;
  }
  .form-card {
    padding: 1.75rem 1.5rem;
  }
}
</style>
