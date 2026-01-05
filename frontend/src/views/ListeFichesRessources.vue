<template>
  <DashboardLayout>
    <template #header>
      <h1 class="page-title">{{ $t('nav.resourceSheetsList') }}</h1>
    </template>

    <div class="page-surface list-surface">
      <div class="placeholder-card"></div>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';

export default {
  name: 'ListeFichesRessources',
  components: { DashboardLayout }
};
</script>

<style scoped>
.page-title {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 2.2rem;
  font-weight: 700;
  margin: 0;
}

.page-surface {
  background: var(--color-card-bg, #fff);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 18px;
  padding: 20px;
  box-shadow: var(--shadow, 0 4px 10px rgba(0,0,0,0.05));
}

.placeholder-card {
  border: 1px dashed var(--color-border, #ddd);
  border-radius: 12px;
  padding: 18px;
  color: var(--color-text-muted, #666);
}
</style>
