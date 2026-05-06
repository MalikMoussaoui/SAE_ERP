<template>
  <DashboardLayout>
    <template #header>
      <div class="header-welcome">
        <h1 class="page-title">TAC</h1>
      </div>
    </template>

    <div class="page-surface">
      <div class="coming-soon">
        <h2>En cours de développement</h2>
        <p>Cette section sera disponible prochainement.</p>
      </div>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';

export default {
  name: 'TacView',
  components: { DashboardLayout }
};
</script>

<style scoped>
.page-title {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 2.4rem;
  font-weight: 700;
  color: var(--color-text-header);
  margin: 0;
}

.page-surface {
  background: var(--color-card-bg);
  border: 1px solid var(--color-border);
  border-radius: 20px;
  padding: 2rem;
  box-shadow: var(--shadow);
  min-height: 50vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.coming-soon {
  text-align: center;
  color: var(--color-text-body);
}

.coming-soon h2 {
  margin: 0 0 0.5rem 0;
  font-size: 1.8rem;
  color: var(--color-text-header);
}

.coming-soon p {
  margin: 0;
  font-size: 1.05rem;
  color: var(--color-text-muted);
}
</style>
