<template>
  <DashboardLayout>
    <template #header>
      <h1 class="page-title">{{ $t('nav.resourceSheets') }}</h1>
    </template>

    <div class="page-surface resource-surface">
      <!-- Indicateur d'étapes (Wizard) -->
      <div v-if="!showAllSteps" class="step-indicator">
        <div class="step" :class="{ active: currentStep >= 1, completed: currentStep > 1 }">
          <div class="step-number">1</div>
          <div class="step-label">{{ $t('resourceSheet.step1_label') }}</div>
        </div>
        <div class="step-connector"></div>
        <div class="step" :class="{ active: currentStep >= 2, completed: currentStep > 2 }">
          <div class="step-number">2</div>
          <div class="step-label">{{ $t('resourceSheet.step2_label') }}</div>
        </div>
        <div class="step-connector"></div>
        <div class="step" :class="{ active: currentStep >= 3, completed: currentStep > 3 }">
          <div class="step-number">3</div>
          <div class="step-label">{{ $t('resourceSheet.step3_label') }}</div>
        </div>
      </div>

      <!-- ÉTAPE 1 : Identification -->
      <section v-if="showAllSteps || currentStep === 1" class="cards-grid top-grid">
        <div class="info-card">
          <h3>{{ $t('mccc.resourcesAndSAE') }}</h3>
          <div class="card-fields">
            <label class="field">
              <span>{{ $t('resourceSheet.resourceCode') }}</span>
              <input v-model="form.code" class="pill-select pill-input" placeholder="R1.XX" :disabled="isReadOnly" />
            </label>
            <label class="field">
              <span>{{ $t('resourceSheet.semester') }}</span>
              <select v-model="form.semestre" class="pill-select" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.select') }}</option>
                <option v-for="option in semestres" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field full">
              <span>{{ $t('resourceSheet.ue') }}</span>
              <select v-model="form.ue" class="pill-select" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.selectUE') }}</option>
                <option v-for="option in availableUes" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
          </div>
        </div>

        <div class="info-card">
          <h3>{{ $t('resourceSheet.generalInfo') }}</h3>
          <div class="card-fields">
            <label class="field full">
              <span>{{ $t('mccc.department') }}</span>
              <select v-model="form.departement" class="pill-select" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.chooseDepartment') }}</option>
                <option v-for="option in departements" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field full">
              <span>{{ $t('resourceSheet.resourceTitle') }}</span>
              <input v-model="form.titre" class="pill-select pill-input" :placeholder="$t('mccc.select')" :disabled="isReadOnly" />
            </label>
          </div>
        </div>
      </section>

      <!-- ÉTAPE 2 : Volume Horaire & Description -->
      <section v-if="showAllSteps || currentStep === 2" class="cards-grid mid-grid">
        <div class="info-card">
          <h3>{{ $t('resourceSheet.hours') }}</h3>
          <div class="card-fields hours-grid">
            <label class="field">
              <span>{{ $t('resourceSheet.hCM') }}</span>
              <input v-model="form.hCM" type="number" min="0" step="0.5" class="pill-select" :disabled="isReadOnly" />
            </label>
            <label class="field">
              <span>{{ $t('resourceSheet.hTD') }}</span>
              <input v-model="form.hTD" type="number" min="0" step="0.5" class="pill-select" :disabled="isReadOnly" />
            </label>
            <label class="field">
              <span>{{ $t('resourceSheet.hTP') }}</span>
              <input v-model="form.hTP" type="number" min="0" step="0.5" class="pill-select" :disabled="isReadOnly" />
            </label>
          </div>
        </div>

        <div class="info-card full-width">
          <h3>{{ $t('resourceSheet.description') }}</h3>
          <div class="card-fields single-col">
            <textarea 
              v-model="form.description" 
              class="info-textarea main-desc" 
              rows="5" 
              :placeholder="$t('resourceSheet.descriptionPlaceholder')"
              :disabled="isReadOnly"
            ></textarea>
          </div>
        </div>
      </section>

      <!-- ÉTAPE 3 : Tableau des Séquences -->
      <section v-if="showAllSteps || currentStep === 3" class="table-section">
        <div class="table-card">
          <div class="table-scroll">
            <table>
              <thead>
                <tr>
                  <th>{{ $t('resourceSheet.table.sequence') }}</th>
                  <th>{{ $t('resourceSheet.table.type') }}</th>
                  <th>{{ $t('resourceSheet.table.duration') }}</th>
                  <th>{{ $t('resourceSheet.table.details') }}</th>
                  <th class="action-header">{{ $t('resourceSheet.table.action') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="row in sequencesRows" :key="row.id">
                  <td>
                    <input class="table-input" v-model="row.label" :placeholder="$t('mccc.select')" :disabled="isReadOnly" />
                  </td>
                  <td>
                    <select v-model="row.type" class="table-input" :disabled="isReadOnly">
                      <option value="CM">CM</option>
                      <option value="TD">TD</option>
                      <option value="TP">TP</option>
                      <option value="Autre">Autre</option>
                    </select>
                  </td>
                  <td>
                    <input class="table-input" v-model="row.duration" type="number" min="0" step="0.5" @input="validatePositive(row, 'duration')" :disabled="isReadOnly" />
                  </td>
                  <td class="info-cell">
                    <div v-if="row.showDetails" class="info-editor">
                      <textarea
                        v-model="row.notes"
                        rows="2"
                        class="info-textarea"
                        :placeholder="$t('mccc.infoPlaceholder')"
                        :disabled="isReadOnly"
                      ></textarea>
                      <div class="info-actions">
                        <button type="button" class="btn-finish" @click="row.showDetails = false" :disabled="isReadOnly">{{ $t('mccc.finish') }}</button>
                      </div>
                    </div>
                    <div v-else class="info-collapsed">
                      <div class="info-preview" :class="{ 'has-content': row.notes }">{{ row.notes || $t('mccc.noInfo') }}</div>
                      <button type="button" class="btn-add" @click="row.showDetails = true" :disabled="isReadOnly">
                        {{ row.notes ? $t('mccc.edit') : $t('mccc.addInfo') }}
                      </button>
                    </div>
                  </td>
                  <td class="action-cell">
                    <button v-if="sequencesRows.length > 1" @click="deleteRow(row.id)" class="btn-delete" :title="$t('mccc.deleteRow')" :disabled="isReadOnly">
                      &times;
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="table-footer">
            <button @click="addRow" class="btn-add-row" :disabled="isReadOnly">
              + {{ $t('resourceSheet.addSequence') }}
            </button>
            <span v-if="errorMessage" class="error-text">{{ errorMessage }}</span>
            <div class="final-totals single">
              <span class="final-label">{{ $t('mccc.totalHours') }}</span>
              <span class="single-total">{{ totalSequenceHours }}</span>
            </div>
          </div>
        </div>
      </section>

      <!-- Navigation -->
      <div v-if="!showAllSteps" class="step-navigation">
        <button v-if="currentStep > 1" @click="prevStep" class="btn btn-secondary">
          {{ $t('mccc.back') }}
        </button>
        <button v-if="currentStep < 3" @click="nextStep" class="btn btn-primary">
          {{ $t('mccc.continue') }}
        </button>
        <button v-if="currentStep === 3" class="btn btn-primary" type="button" @click="saveResource" :disabled="isReadOnly">
          {{ $t('mccc.save') }}
        </button>
      </div>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';

export default {
  name: 'FicheRessourceView',
  components: { DashboardLayout },
  data() {
    return {
      currentStep: 1,
      form: {
        departement: '',
        titre: '',
        code: '',
        semestre: '',
        ue: '',
        hCM: 0,
        hTD: 0,
        hTP: 0,
        description: ''
      },
      editingId: null,
      isReadOnly: false,
      departements: [
        'BUT Informatique',
        'BUT GEA - Gestion des Entreprises et des Administrations',
        'BUT TC - Techniques de Commercialisation',
        'BUT Mesures Physiques (MP)',
        'BUT Genie Mecanique et Productique (GMP)',
        'BUT GEII - Genie Electrique et Informatique Industrielle',
        'BUT Genie Civil - Construction Durable',
        'BUT Genie Biologique (GB)',
        "BUT MMI - Metiers du Multimedia et de l'Internet",
        'BUT GIM - Genie Industriel et Maintenance',
        'BUT HSE - Hygiene, Securite, Environnement',
        'BUT Carrieres Sociales'
      ],
      semestres: ['S1', 'S2', 'S3', 'S4', 'S5', 'S6'],
      uesByDepartement: {
        'BUT Informatique': [
          'UE Realiser des applications',
          'UE Optimiser des applications',
          'UE Administrer des systemes informatiques communicants',
          "UE Gerer des donnees de l'information",
          'UE Conduire un projet',
          'UE Collaborer dans un environnement professionnel'
        ],
        // ... (Autres départements identiques à Mccc.vue, abrégé pour l'exemple)
      },
      availableUes: [],
      sequencesRows: [
        { id: 1, label: '', type: 'CM', duration: 0, notes: '', showDetails: false }
      ],
      nextRowId: 2,
      errorMessage: '',
      errorTimeout: null,
      mcccEntries: null,
      lastAutoFillKey: null,
      isAutoFillLoading: false
    };
  },
  created() {
    if (this.form.departement) {
      this.availableUes = this.uesByDepartement[this.form.departement] || [];
    }
    this.loadFromRoute();
  },
  watch: {
    'form.departement'(newDepartement) {
      this.availableUes = this.uesByDepartement[newDepartement] || [];
      this.form.ue = '';
    },
    '$route.query.id'() {
      this.loadFromRoute();
    },
    '$route.query.mode'() {
      this.isReadOnly = this.$route.query.mode === 'view';
    },
    'form.departement'() {
      this.maybeAutoFillFromMccc();
    },
    'form.code'() {
      this.maybeAutoFillFromMccc();
    },
    'form.semestre'() {
      this.maybeAutoFillFromMccc();
    },
    'form.ue'() {
      this.maybeAutoFillFromMccc();
    }
  },
  computed: {
    showAllSteps() {
      return this.isReadOnly && this.$route.query.mode === 'view';
    },
    totalSequenceHours() {
      return this.sequencesRows.reduce((acc, row) => acc + (Number(row.duration) || 0), 0);
    }
  },
  methods: {
    normalizeValue(value) {
      if (value === null || value === undefined) return '';
      return String(value).trim().toLowerCase();
    },
    toNumber(value) {
      if (value === null || value === undefined) return 0;
      const normalized = String(value).replace(',', '.');
      const parsed = Number(normalized);
      return Number.isFinite(parsed) ? parsed : 0;
    },
    async fetchMcccEntries() {
      if (this.mcccEntries) return this.mcccEntries;

      const response = await axios.get('/api/mccc');
      this.mcccEntries = response.data.map((entry) => {
        const form = entry.form || {};
        return {
          ...entry,
          departement: entry.departement || entry.department || form.departement || '',
          ue: entry.ue || form.ue || '',
          semestre: entry.semestre || entry.semester || form.semestre || '',
          code: form.code || form.ressource || '',
          savedAt: entry.savedAt || entry.saved_at || form.savedAt || ''
        };
      });

      return this.mcccEntries;
    },
    buildAutoFillKey() {
      return [
        this.normalizeValue(this.form.departement),
        this.normalizeValue(this.form.code),
        this.normalizeValue(this.form.semestre),
        this.normalizeValue(this.form.ue)
      ].join('|');
    },
    isDefaultSequences() {
      return this.sequencesRows.length === 1
        && !this.sequencesRows[0].label
        && this.toNumber(this.sequencesRows[0].duration) === 0
        && !this.sequencesRows[0].notes;
    },
    async maybeAutoFillFromMccc() {
      if (this.isReadOnly || this.isAutoFillLoading) return;
      if (!this.form.departement || !this.form.code || !this.form.semestre || !this.form.ue) return;

      const key = this.buildAutoFillKey();
      if (this.lastAutoFillKey === key) return;

      this.isAutoFillLoading = true;
      try {
        const entries = await this.fetchMcccEntries();
        const normalizedCode = this.normalizeValue(this.form.code);
        const normalizedDepartement = this.normalizeValue(this.form.departement);
        const normalizedSemestre = this.normalizeValue(this.form.semestre);
        const normalizedUe = this.normalizeValue(this.form.ue);

        const matches = entries.filter((entry) => {
          if (this.normalizeValue(entry.departement) !== normalizedDepartement) return false;
          if (this.normalizeValue(entry.semestre) !== normalizedSemestre) return false;
          if (this.normalizeValue(entry.ue) !== normalizedUe) return false;
          const entryCode = this.normalizeValue(entry.code);
          return entryCode ? entryCode === normalizedCode : this.normalizeValue(entry.form?.ressource) === normalizedCode;
        });

        if (!matches.length) {
          this.lastAutoFillKey = key;
          return;
        }

        const sorted = matches
          .map(entry => ({ entry, savedAt: entry.savedAt ? new Date(entry.savedAt).getTime() : 0 }))
          .sort((a, b) => b.savedAt - a.savedAt);
        this.applyMcccToResource(sorted[0].entry);
        this.lastAutoFillKey = key;
      } catch (error) {
        console.error('Error loading MCCC for auto-fill', error);
      } finally {
        this.isAutoFillLoading = false;
      }
    },
    applyMcccToResource(mccc) {
      const form = mccc.form || {};
      const rows = Array.isArray(mccc.ressourcesRows) ? mccc.ressourcesRows : [];

      if (!this.form.titre) {
        this.form.titre = form.ressource || form.titre || mccc.ue || '';
      }

      if (!this.form.departement) {
        this.form.departement = mccc.departement || '';
      }

      if (!this.form.semestre) {
        this.form.semestre = mccc.semestre || '';
      }

      if (!this.form.ue) {
        this.form.ue = mccc.ue || '';
      }

      if (this.toNumber(this.form.hCM) === 0) {
        this.form.hCM = rows.reduce((acc, row) => acc + this.toNumber(row.hCM), 0);
      }
      if (this.toNumber(this.form.hTD) === 0) {
        this.form.hTD = rows.reduce((acc, row) => acc + this.toNumber(row.hTD), 0);
      }
      if (this.toNumber(this.form.hTP) === 0) {
        this.form.hTP = rows.reduce((acc, row) => acc + this.toNumber(row.hTP), 0);
      }

      if (!this.form.description) {
        const notes = rows
          .map(row => row.notes)
          .filter(Boolean)
          .join('\n');
        if (notes) {
          this.form.description = notes;
        }
      }

      if (this.isDefaultSequences() && rows.length) {
        this.sequencesRows = rows.map((row, index) => {
          const hcm = this.toNumber(row.hCM);
          const htd = this.toNumber(row.hTD);
          const htp = this.toNumber(row.hTP);
          let type = 'Autre';
          if (hcm > 0 && htd === 0 && htp === 0) type = 'CM';
          if (htd > 0 && hcm === 0 && htp === 0) type = 'TD';
          if (htp > 0 && hcm === 0 && htd === 0) type = 'TP';

          const duration = hcm + htd + htp
            + this.toNumber(row.hDSCM)
            + this.toNumber(row.hDSTP);

          return {
            id: index + 1,
            label: row.label || `Ressource ${index + 1}`,
            type,
            duration,
            notes: row.notes || '',
            showDetails: false
          };
        });
        this.nextRowId = this.sequencesRows.length + 1;
      }
    },
    loadFromRoute() {
      const { id, mode } = this.$route.query;
      this.isReadOnly = mode === 'view';

      if (!id) {
        this.editingId = null;
        return;
      }

      const stored = JSON.parse(localStorage.getItem('resourceList') || '[]');
      const entry = stored.find(item => String(item.id) === String(id));

      if (!entry) return;

      this.currentStep = 1;
      this.editingId = entry.id;
      this.form = { ...this.form, ...(entry.form || {}) };
      this.availableUes = this.uesByDepartement[this.form.departement] || [];

      const loadedRows = (entry.sequencesRows || []).map(row => ({ ...row, showDetails: false }));
      this.sequencesRows = loadedRows.length ? loadedRows : [{ id: 1, label: '', type: 'CM', duration: 0, notes: '', showDetails: false }];
      const maxId = this.sequencesRows.reduce((max, row) => Math.max(max, Number(row.id) || 0), 0);
      this.nextRowId = maxId + 1;
    },
    nextStep() {
      if (this.currentStep < 3) {
        this.currentStep++;
      }
    },
    prevStep() {
      if (this.currentStep > 1) {
        this.currentStep--;
      }
    },
    addRow() {
      this.sequencesRows.push({
        id: this.nextRowId++,
        label: '',
        type: 'CM',
        duration: 0,
        notes: '',
        showDetails: false
      });
    },
    deleteRow(rowId) {
      if (this.sequencesRows.length <= 1) return;
      this.sequencesRows = this.sequencesRows.filter(row => row.id !== rowId);
    },
    validatePositive(row, field) {
      if (row[field] < 0) {
        row[field] = 0;
        this.errorMessage = this.$t('common.error.negativeValue');
        if (this.errorTimeout) clearTimeout(this.errorTimeout);
        this.errorTimeout = setTimeout(() => {
          this.errorMessage = '';
        }, 3000);
      }
    },
    async saveResource() {
      // 1. Préparation des données
      const payload = {
        titre: this.form.titre,
        departement: this.form.departement,
        description: this.form.description,
        hCM: Number(this.form.hCM) || 0,
        hTD: Number(this.form.hTD) || 0,
        hTP: Number(this.form.hTP) || 0,
        ue: this.form.ue,
        semestre: this.form.semestre
      };

      try {
        // 2. Vérification : Édition ou Création ?
        // On regarde si on est en mode 'edit' ET qu'on a bien un ID dans l'URL
        const id = this.$route.query.id;
        const mode = this.$route.query.mode;

        if (mode === 'edit' && id) {
          // --- MODE MODIFICATION (PUT) ---
          console.log("Mise à jour de la fiche n°", id);
          await axios.put(`/resource-sheets/${id}`, payload);
          alert("Fiche modifiée avec succès !");
        } else {
          // --- MODE CRÉATION (POST) ---
          console.log("Création d'une nouvelle fiche");
          await axios.post('/resource-sheets', payload);
          alert("Fiche créée avec succès !");
        }

        // 3. Retour à la liste
        this.$router.push({ name: 'liste-fiches-ressources' });

      } catch (error) {
        console.error("Erreur lors de la sauvegarde :", error);
        alert("Erreur technique. Vérifie la console.");
      }
    }
  }
};
</script>

<style scoped>
/* Styles copiés et adaptés de Mccc.vue pour conserver le même design */
.page-title {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 2.4rem;
  font-weight: 700;
  color: var(--color-text-header, #222);
  margin: 0;
}

.page-surface {
  background: var(--color-sidebar-bg, #f7f8ff);
  border: 1px solid var(--color-border);
  border-radius: 20px;
  padding: 22px;
  box-shadow: var(--shadow, 0 4px 6px rgba(0,0,0,0.05));
}

.resource-surface {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* Step Indicator Styles */
.step-indicator {
  display: flex;
  align-items: flex-start;
  justify-content: center;
  margin-bottom: 2.5rem;
  width: 100%;
  max-width: 700px;
  margin-left: auto;
  margin-right: auto;
}
.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: var(--color-text-muted);
  transition: color 0.3s;
  text-align: center;
  width: 120px;
}
.step-number {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 2px solid var(--color-border);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  background-color: var(--color-card-bg);
  transition: all 0.3s;
  z-index: 1;
}
.step-label { margin-top: 0.5rem; font-size: 0.85rem; font-weight: 500; }
.step-connector {
  flex-grow: 1;
  height: 2px;
  background-color: var(--color-border);
  margin: 0;
  transform: translateY(17px);
  transition: background-color 0.3s;
}
.step.active .step-number { border-color: var(--color-primary); color: var(--color-primary); }
.step.active .step-label { color: var(--color-text-header); }
.step.completed .step-number { background-color: var(--color-primary); border-color: var(--color-primary); color: white; }
.step.completed + .step-connector { background-color: var(--color-primary); }

/* Cards & Grid */
.cards-grid { display: grid; gap: 14px; }
.top-grid { grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); }
.mid-grid { grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); }

.info-card {
  background: var(--color-card-bg, #fff);
  border: 1px solid var(--color-border);
  border-radius: 16px;
  padding: 14px 16px 16px;
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.06);
}
.info-card h3 { margin: 0 0 12px; color: var(--color-primary); font-size: 1.05rem; text-align: center; }
.card-fields { 
  display: grid; 
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); 
  gap: 16px; 
  align-items: start;
}
.hours-grid {
  grid-template-columns: repeat(3, 1fr);
}
.hours-grid .field {
  align-items: center;
}
.hours-grid input {
  text-align: center;
}
.card-fields.single-col { grid-template-columns: 1fr; }
.full-width { grid-column: 1 / -1; }

.field { display: flex; flex-direction: column; gap: 6px; font-size: 0.92rem; color: var(--color-text-body, #444); width: 100%; }
.field.full { grid-column: 1 / -1; }
.pill-select {
  width: 100%; padding: 10px 12px; box-sizing: border-box; border: 1px solid var(--color-border); border-radius: 12px;
  background-color: var(--color-input-bg, #fff); font-family: var(--font-secondary); font-size: 0.95rem;
}
.pill-select:focus { border-color: var(--color-primary); outline: none; }
.pill-input { padding-right: 12px; }

/* Table & Actions */
.table-section { margin-top: 8px; }
.table-card { background: var(--color-card-bg); border: 1px solid var(--color-border); border-radius: 18px; padding: 12px; }
.table-scroll { max-height: 520px; overflow-x: auto; }
table { width: 100%; border-collapse: collapse; font-size: 0.94rem; }
thead th { position: sticky; top: 0; background: var(--color-sidebar-bg); padding: 12px 18px; text-align: left; }
tbody td { padding: 10px 18px; border-bottom: 1px solid var(--color-border); }
.table-input { width: 100%; padding: 8px 10px; box-sizing: border-box; border: 1px solid var(--color-border); border-radius: 10px; }
.btn-add-row { background: none; border: none; color: var(--color-primary); font-weight: 600; cursor: pointer; padding: 12px 16px; }
.step-navigation { display: flex; justify-content: flex-end; gap: 1rem; margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--color-border); }
.btn { padding: 12px 24px; border-radius: 10px; font-weight: 600; cursor: pointer; border: none; }
.btn-primary { background-color: var(--color-primary); color: white; }
.btn-secondary { background-color: var(--color-card-bg); border: 1px solid var(--color-border); }
.btn-delete { background: var(--color-primary); color: white; width: 28px; height: 28px; border-radius: 50%; border: none; cursor: pointer; }
.info-textarea { width: 100%; padding: 8px 10px; box-sizing: border-box; border: 1px solid var(--color-border); border-radius: 10px; resize: vertical; font-family: var(--font-secondary); }
.main-desc { min-height: 100px; }
.info-actions { display: flex; justify-content: flex-end; margin-top: 5px; }
.btn-finish, .btn-add { background-color: var(--color-primary); color: white; border: none; border-radius: 10px; padding: 6px 12px; cursor: pointer; }
.table-footer { display: flex; justify-content: space-between; align-items: center; background: var(--color-sidebar-bg); border-top: 1px solid var(--color-border); border-radius: 0 0 18px 18px; }
.final-totals { display: flex; gap: 14px; padding: 12px 14px; font-weight: 600; }
.final-label { color: var(--color-primary); text-transform: uppercase; }
</style>
