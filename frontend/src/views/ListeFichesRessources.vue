<template>
  <DashboardLayout>
    <template #header>
      <h1 class="page-title">{{ $t('nav.resourceSheetsList') || 'Liste des Fiches Ressources' }}</h1>
    </template>

    <div class="page-surface list-surface">

      <div class="surface-header-actions">
        <router-link to="/fiche-ressource" class="btn-add">
          + Nouvelle Fiche
        </router-link>
      </div>

      <div v-if="loading" class="loading-state">
        Chargement...
      </div>

      <div v-else-if="!sheets.length" class="empty-state">
        Aucune fiche ressource trouvée.
      </div>

      <div v-else class="mccc-cards">
        <div v-for="sheet in sheets" :key="sheet.id" class="mccc-card">

          <div class="card-header">
            <h3 class="ue-title">{{ sheet.title || 'Sans titre' }}</h3>
            <span class="chip">{{ sheet.departmentName }}</span>
          </div>

          <div class="card-meta">
            <span class="meta-label">Dernière modification :</span>
            <span class="meta-value">{{ formatDate(sheet.updatedAt || sheet.createdAt) }}</span>
          </div>

          <div class="card-actions">
            <router-link
                class="action-button ghost"
                :to="{ name: 'fiche-ressource', query: { id: sheet.id, mode: 'view' } }"
            >
              Consulter
            </router-link>

            <router-link
                class="action-button primary"
                :to="{ name: 'fiche-ressource', query: { id: sheet.id, mode: 'edit' } }"
            >
              Modifier
            </router-link>

            <button
                type="button"
                class="action-button danger"
                @click="deleteSheet(sheet.id)"
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
import axios from 'axios';

export default {
  name: 'ListeFichesRessources',
  components: { DashboardLayout },
  data() {
    return {
      sheets: [],
      loading: true
    };
  },
  created() {
    this.loadEntries();
  },
  methods: {
    async loadEntries() {
      this.loading = true;
      try {
        const response = await axios.get('/resource-sheets');
        this.sheets = response.data.reverse();
      } catch (error) {
        console.error("Erreur chargement:", error);
      } finally {
        this.loading = false;
      }
    },
    async deleteSheet(id) {
      if (!confirm("Voulez-vous vraiment supprimer cette fiche ?")) return;
      try {
        await axios.delete(`/resource-sheets/${id}`);
        this.sheets = this.sheets.filter(s => s.id !== id);
      } catch (error) {
        console.error("Erreur suppression:", error);
        alert("Impossible de supprimer cette fiche.");
      }
    },
    formatDate(isoString) {
      if (!isoString) return 'Date inconnue';
      const date = new Date(isoString);
      return date.toLocaleString('fr-FR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
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
  color: var(--color-text-header, #222);
}

.page-surface {
  background: var(--color-card-bg, #fff);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 18px;
  padding: 20px;
  box-shadow: var(--shadow, 0 4px 10px rgba(0,0,0,0.05));
}

/* --- NOUVEAU STYLE POUR LE CONTENEUR DU BOUTON --- */
.surface-header-actions {
  display: flex;
  justify-content: flex-end; /* Aligne le bouton à droite */
  margin-bottom: 20px; /* Espace avant la liste */
}

.btn-add {
  background-color: var(--color-primary, #c00000);
  color: white;
  padding: 10px 20px;
  border-radius: 10px;
  text-decoration: none;
  font-weight: 600;
  font-size: 0.95rem;
  box-shadow: 0 4px 6px rgba(192, 0, 0, 0.2);
  transition: transform 0.2s, background-color 0.2s;
}

.btn-add:hover {
  background-color: var(--color-primary-dark, #a00000);
  transform: translateY(-2px);
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
  gap: 12px;
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
  font-weight: 700;
}

.chip {
  padding: 6px 10px;
  border-radius: 999px;
  background: var(--color-sidebar-bg, #f3f3f3);
  color: var(--color-text-body, #555);
  font-weight: 600;
  font-size: 0.85rem;
  white-space: nowrap;
}

.card-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.95rem;
  color: var(--color-text-body, #555);
}

.meta-label {
  color: var(--color-text-muted, #777);
  font-weight: 600;
}

/* --- CORRECTION DARK MODE --- */
.meta-value {
  font-weight: 600;
  /* Utilise la variable CSS globale au lieu d'une couleur fixe */
  color: var(--color-text-header, #333);
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
  cursor: pointer;
  border: none;
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

.empty-state, .loading-state {
  border: 1px dashed var(--color-border, #ddd);
  border-radius: 12px;
  padding: 30px;
  color: var(--color-text-muted, #666);
  text-align: center;
  font-style: italic;
}
</style>