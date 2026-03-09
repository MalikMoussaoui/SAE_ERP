<template>
  <DashboardLayout>
    <template #header>
      <h1 class="page-title">{{ $t('nav.mcccList') }}</h1>
    </template>

    <div class="page-surface list-surface">
      <div class="list-toolbar">
        <input
          ref="fileInput"
          type="file"
          accept=".csv,.xlsx,.xls"
          class="hidden-file-input"
          @change="onImportFileChange"
        />
        <button type="button" class="action-button ghost" :disabled="isImporting" @click="triggerImport">
          {{ isImporting ? 'Import en cours...' : 'Importer des MCCC (Excel/CSV)' }}
        </button>
      </div>

      <div v-if="isLoading" class="empty-state">
        Chargement des MCCC...
      </div>
      <div v-else-if="errorMessage" class="empty-state error-state">
        {{ errorMessage }}
      </div>
      <div v-else-if="!mcccEntries.length" class="empty-state">
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
import axios from 'axios';
import * as XLSX from 'xlsx';

export default {
  name: 'ListeMccc',
  components: { DashboardLayout },
  data() {
    return {
      mcccEntries: [],
      isLoading: false,
      errorMessage: '',
      isImporting: false
    };
  },
  created() {
    this.loadEntries();
  },
  methods: {
    async loadEntries() {
      this.isLoading = true;
      this.errorMessage = '';
      try {
        const response = await axios.get('/mccc');
        const payload = response?.data;
        const rawEntries = Array.isArray(payload)
          ? payload
          : (Array.isArray(payload?.content) ? payload.content : (Array.isArray(payload?.items) ? payload.items : []));

        const entries = rawEntries.map((entry) => {
          const savedAt = entry.savedAt || entry.saved_at || entry.form?.savedAt || '';
          return {
            ...entry,
            departement: entry.departement || entry.department || entry.form?.departement || '',
            ue: entry.ue || entry.form?.ue || entry.form?.titre || entry.form?.code || 'MCCC sans libellé',
            savedAt
          };
        });

        this.mcccEntries = entries.sort((a, b) => {
          const aTime = a.savedAt ? new Date(a.savedAt).getTime() : 0;
          const bTime = b.savedAt ? new Date(b.savedAt).getTime() : 0;
          return bTime - aTime;
        });
      } catch (e) {
        console.error("Error loading MCCC list", e);
        this.errorMessage = "Impossible de charger les fiches MCCC pour le moment.";
      } finally {
        this.isLoading = false;
      }
    },
    triggerImport() {
      if (this.isImporting) return;
      this.$refs.fileInput?.click();
    },
    getCell(row, ...keys) {
      for (const key of keys) {
        if (row[key] !== undefined && row[key] !== null && String(row[key]).trim() !== '') {
          return row[key];
        }
      }
      return '';
    },
    safeJsonParse(value, fallback) {
      if (!value || typeof value !== 'string') return fallback;
      try {
        return JSON.parse(value);
      } catch (_) {
        return fallback;
      }
    },
    toPayloadFromRow(rawRow) {
      const row = rawRow || {};
      const department = String(this.getCell(row, 'departement', 'department', 'Département', 'Department')).trim();
      const ue = String(this.getCell(row, 'ue', 'UE')).trim();
      const years = String(this.getCell(row, 'annee', 'years', 'Année', 'Years')).trim();
      const semester = String(this.getCell(row, 'semestre', 'semester', 'Semestre', 'Semester')).trim();

      const formFromJson = this.safeJsonParse(
        String(this.getCell(row, 'form_json', 'formJson', 'form')).trim(),
        null
      );
      const rowsFromJson = this.safeJsonParse(
        String(this.getCell(row, 'ressources_rows_json', 'ressourcesRowsJson', 'ressourcesRows')).trim(),
        null
      );

      const form = formFromJson || {
        departement: department,
        ue,
        annee: years,
        semestre: semester,
        code: String(this.getCell(row, 'code', 'ressource', 'Code')).trim(),
        typeEvaluation: String(this.getCell(row, 'typeEvaluation', 'type_evaluation')).trim(),
        coeffSae: this.getCell(row, 'coeffSae', 'coeff_sae'),
        coeffRessource: this.getCell(row, 'coeffRessource', 'coeff_ressource'),
        coeffTotal: this.getCell(row, 'coeffTotal', 'coeff_total'),
        regleValidation: String(this.getCell(row, 'regleValidation', 'regle_validation')).trim(),
        rattrapage: String(this.getCell(row, 'rattrapage')).trim(),
        compensation: String(this.getCell(row, 'compensation')).trim(),
        responsable: String(this.getCell(row, 'responsable', 'responsablePedagogique')).trim(),
        objectif: String(this.getCell(row, 'objectif')).trim()
      };

      return {
        department,
        ue,
        years,
        semester,
        form,
        ressourcesRows: Array.isArray(rowsFromJson) ? rowsFromJson : []
      };
    },
    validatePayload(payload, rowNumber) {
      if (!payload.department || !payload.ue || !payload.years || !payload.semester) {
        throw new Error(`Ligne ${rowNumber}: colonnes obligatoires manquantes (departement/ue/annee/semestre).`);
      }
    },
    async onImportFileChange(event) {
      const file = event.target.files?.[0];
      if (!file) return;

      this.isImporting = true;
      this.errorMessage = '';

      try {
        const buffer = await file.arrayBuffer();
        const workbook = XLSX.read(buffer, { type: 'array' });
        const firstSheetName = workbook.SheetNames[0];
        const firstSheet = workbook.Sheets[firstSheetName];
        const rows = XLSX.utils.sheet_to_json(firstSheet, { defval: '' });

        if (!rows.length) {
          throw new Error('Le fichier est vide.');
        }

        let imported = 0;
        const errors = [];

        for (let i = 0; i < rows.length; i += 1) {
          const rowNumber = i + 2;
          try {
            const payload = this.toPayloadFromRow(rows[i]);
            this.validatePayload(payload, rowNumber);
            await axios.post('/mccc', payload);
            imported += 1;
          } catch (e) {
            errors.push(e?.message || `Ligne ${rowNumber}: erreur inconnue`);
          }
        }

        await this.loadEntries();

        const summary = `Import terminé: ${imported}/${rows.length} lignes importées.`;
        if (errors.length) {
          alert(`${summary}\n\nErreurs:\n- ${errors.slice(0, 10).join('\n- ')}`);
        } else {
          alert(summary);
        }
      } catch (e) {
        console.error('Error importing MCCC file', e);
        this.errorMessage = e?.message || "Erreur pendant l'import du fichier MCCC.";
      } finally {
        this.isImporting = false;
        if (this.$refs.fileInput) this.$refs.fileInput.value = '';
      }
    },
    async deleteEntry(entryId) {
      const entry = this.mcccEntries.find(item => String(item.id) === String(entryId));
      const label = entry?.ue ? ` "${entry.ue}"` : '';
      const confirmed = window.confirm(`Supprimer la fiche MCCC${label} ?`);
      if (!confirmed) return;

      try {
        await axios.delete(`/mccc/${entryId}`);
        this.loadEntries();
      } catch (e) {
        console.error("Error deleting entry", e);
        alert("Erreur lors de la suppression");
      }
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

.list-toolbar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 14px;
}

.hidden-file-input {
  display: none;
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

.error-state {
  border-style: solid;
  border-color: #ef4444;
  color: #b91c1c;
  background: #fef2f2;
}
</style>
