<template>
  <DashboardLayout>
    <template #header>
      <div class="header-welcome">
        <h2>{{ $t('dashboard.greeting') }}</h2>
        <h1>{{ $t('dashboard.title') }}</h1>
      </div>
    </template>

    <section class="content-cards">
      <div class="card">
        <h3>{{ $t('dashboard.fillSheet') }}</h3>
        <p>{{ $t('dashboard.subject') }}</p>
        <p class="subtitle">{{ $t('dashboard.semester') }}</p>
        <button class="btn-primary">{{ $t('dashboard.complete') }}</button>
      </div>
      <div class="card card-notifications">
        <div class="icon-success">
          <span>✔</span>
        </div>
        <h3>{{ $t('dashboard.notifications') }}</h3>
        <p>{{ $t('dashboard.noNotifications') }}</p>
      </div>
    </section>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';

export default {
  name: 'DashboardView',
  components: {
    DashboardLayout
  }
}
</script>

<style scoped>
.header-welcome h1 {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 2rem;
  font-weight: 700;
  color: var(--color-text-header, #333);
  margin: 0.25rem 0 0 0;
}

.header-welcome h2 {
  font-size: 1.25rem;
  font-weight: 500;
  color: var(--color-text-body, #555);
  margin: 0;
}

.content-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.card {
  background: var(--color-card-bg, #ffffff);
  padding: 2rem;
  border-radius: 20px;
  box-shadow: var(--shadow, 0 10px 30px rgba(0, 0, 0, 0.07));
  border: 1px solid var(--color-border, #eef);
}

.card h3 {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--color-text-header, #333);
  margin: 0 0 0.5rem 0;
}

.card p {
  font-size: 1rem;
  color: var(--color-text-body, #555);
  margin: 0.25rem 0;
}

.card p.subtitle {
  font-size: 0.9rem;
  color: #888;
  margin-bottom: 1.5rem;
}

.card-notifications {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.icon-success {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: #28a745;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
}

.btn-primary {
  width: 100%;
  padding: 14px;
  border: none;
  border-radius: 10px;
  background-color: var(--color-primary, #C00000);
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  margin-top: 1rem;
  font-family: var(--font-primary, 'Poppins', sans-serif);
}

.btn-primary:hover {
  background-color: var(--color-primary-dark, #a00000);
  box-shadow: 0 5px 15px rgba(192, 0, 0, 0.3);
}
</style>