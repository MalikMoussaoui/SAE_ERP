<template>
  <DashboardLayout>
    <template #header>
      <h1 class="page-title">{{ $t('nav.mcccList') }}</h1>
    </template>

    <div class="page-surface list-surface">
      <div v-if="!mcccEntries.length" class="empty-state">
        {{ $t('mcccList.empty') }}
      </div>
      <div v-else class="mccc-cards">
        <div v-for="entry in mcccEntries" :key="entry.id" class="mccc-card">
          <div class="card-header">
            <h3 class="ue-title">{{ entry.ue }}</h3>
            <span class="chip">{{ entry.departement }}</span>
          </div>
          <div class="card-meta">
            <span class="meta-label">{{ $t('mcccList.savedAt') }} :</span>
            <span class="meta-value">{{ formatDate(entry.savedAt) }}</span>
          </div>
          <div class="card-actions">
            <router-link
              class="action-button ghost"
              :to="{ name: 'mccc', query: { id: entry.id, mode: 'view' } }"
            >
              Consulter
            </router-link>
            <router-link
              class="action-button primary"
              :to="{ name: 'mccc', query: { id: entry.id, mode: 'edit' } }"
            >
              Modifier
            </router-link>
            <button
              type="button"
              class="action-button danger"
              @click="deleteEntry(entry.id)"
            >
              Supprimer
            </button>
          </div>
        </div>
      </div>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';

export default {
  name: 'ListeMccc',
  components: { DashboardLayout },
  data() {
    return {
      mcccEntries: []
    };
  },
  created() {
    this.loadEntries();
  },
  methods: {
    loadEntries() {
      const stored = JSON.parse(localStorage.getItem('mcccList') || '[]');
      this.mcccEntries = stored.slice().reverse();
    },
    deleteEntry(entryId) {
      const entry = this.mcccEntries.find(item => String(item.id) === String(entryId));
      const label = entry?.ue ? ` "${entry.ue}"` : '';
      const confirmed = window.confirm(`Supprimer la fiche MCCC${label} ?`);
      if (!confirmed) return;

      const stored = JSON.parse(localStorage.getItem('mcccList') || '[]');
      const updated = stored.filter(item => String(item.id) !== String(entryId));
      localStorage.setItem('mcccList', JSON.stringify(updated));
      this.loadEntries();
    },
    formatDate(isoString) {
      if (!isoString) return '';
      const date = new Date(isoString);
      return date.toLocaleString();
    }
  }
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

.mccc-cards {
  display: grid;
  grid-template-columns: 1fr;
  gap: 14px;
}

.mccc-card {
  border: 1px solid var(--color-border, #ddd);
  border-radius: 14px;
  padding: 14px 16px;
  background: var(--color-card-bg, #fff);
  box-shadow: 0 8px 16px rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.ue-title {
  margin: 0;
  font-size: 1.1rem;
  color: var(--color-text-header, #333);
}

.chip {
  padding: 6px 10px;
  border-radius: 999px;
  background: var(--color-sidebar-bg, #f3f3f3);
  color: var(--color-text-body, #555);
  font-weight: 600;
  font-size: 0.9rem;
  white-space: nowrap;
}

.card-meta {
  display: flex;
  gap: 8px;
  font-size: 0.95rem;
  color: var(--color-text-body, #555);
}

.meta-label {
  color: var(--color-text-muted, #777);
  font-weight: 600;
}

.meta-value {
  font-weight: 600;
}

.card-actions {
  display: flex;
  gap: 8px;
  margin-top: 4px;
}

.action-button {
  text-decoration: none;
  padding: 8px 12px;
  border-radius: 10px;
  font-weight: 700;
  font-size: 0.92rem;
  text-align: center;
  transition: transform 0.15s ease, box-shadow 0.15s ease, background 0.15s ease;
}

.action-button.primary {
  background: var(--color-primary, #c00000);
  color: #fff;
  box-shadow: 0 6px 12px rgba(192, 0, 0, 0.18);
}

.action-button.ghost {
  border: 1px solid var(--color-border, #ddd);
  background: var(--color-card-bg, #fff);
  color: var(--color-text-body, #444);
}

.action-button:hover {
  transform: translateY(-1px);
}

.action-button.primary:hover {
  background: var(--color-primary-dark, #a00000);
}

.action-button.danger {
  background: #fff;
  color: #b00020;
  border: 1px solid #b00020;
}

.action-button.danger:hover {
  background: rgba(176, 0, 32, 0.08);
}

.action-button.ghost:hover {
  background: var(--color-sidebar-bg, #f5f5f5);
}

.empty-state {
  border: 1px dashed var(--color-border, #ddd);
  border-radius: 12px;
  padding: 18px;
  color: var(--color-text-muted, #666);
  text-align: center;
}
</style>
