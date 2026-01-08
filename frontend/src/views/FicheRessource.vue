<template>
  <DashboardLayout>
    <template #header>
      <h1 class="page-title">Fiche Ressource</h1>
    </template>

    <div class="page-surface resource-sheet">
      <section class="card">
        <header class="card-header">
          <h2>1. Informations générales et volumes horaires</h2>
        </header>
        <div class="fields-row">
          <label class="field">
            <span>Semestre</span>
            <select v-model="form.semestre">
              <option value="" disabled>Choisir</option>
              <option v-for="option in semestres" :key="option" :value="option">{{ option }}</option>
            </select>
          </label>
          <label class="field">
            <span>UE</span>
            <select v-model="form.ue">
              <option value="" disabled>Choisir</option>
              <option v-for="option in ues" :key="option" :value="option">{{ option }}</option>
            </select>
          </label>
          <label class="field">
            <span>Ressource</span>
            <select v-model="form.ressource">
              <option value="" disabled>Choisir</option>
              <option v-for="option in ressources" :key="option" :value="option">{{ option }}</option>
            </select>
          </label>
          <label class="field coefficient">
            <span>Coefficient</span>
            <input v-model="form.coefficient" type="number" min="0" step="0.1" />
          </label>
        </div>

        <div class="table-wrapper">
          <table class="hours-table">
            <thead>
              <tr>
                <th>Heures CM</th>
                <th>Heures TD</th>
                <th>Heures TP</th>
                <th>Heures Contrôle</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td><input class="hours-input" v-model="form.heures.cm" type="number" min="0" step="0.5" /></td>
                <td><input class="hours-input" v-model="form.heures.td" type="number" min="0" step="0.5" /></td>
                <td><input class="hours-input" v-model="form.heures.tp" type="number" min="0" step="0.5" /></td>
                <td><input class="hours-input" v-model="form.heures.controle" type="number" min="0" step="0.5" /></td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <section class="card">
        <header class="card-header">
          <h2>2. Objectif de la ressource</h2>
        </header>
        <div class="objectives">
          <label class="field">
            <span>Prérequis</span>
            <textarea v-model="form.prerequis" rows="4"></textarea>
          </label>
          <label class="field">
            <span>Objectif de la ressource</span>
            <textarea v-model="form.objectif" rows="4"></textarea>
          </label>
        </div>
      </section>

      <section class="card">
        <header class="card-header">
          <h2>3. SAé</h2>
        </header>
        <label class="field">
          <span>SAé concernée(s)</span>
          <input v-model="form.sae" type="text" />
        </label>
      </section>

      <section class="card">
        <header class="card-header">
          <h2>4. Connaissances que font apprendre les étudiants</h2>
        </header>
        <label class="field">
          <span>Connaissance à apprendre</span>
          <textarea v-model="form.connaissance" rows="4"></textarea>
        </label>
      </section>

      <section class="card">
        <header class="card-header">
          <h2>5. Descriptif du cours</h2>
        </header>
        <div class="course-grid">
          <label class="field">
            <span>CM1</span>
            <input v-model="form.descriptif.cm1" type="text" />
          </label>
          <label class="field">
            <span>CM2</span>
            <input v-model="form.descriptif.cm2" type="text" />
          </label>
          <label class="field">
            <span>TD1</span>
            <input v-model="form.descriptif.td1" type="text" />
          </label>
          <label class="field">
            <span>TD2</span>
            <input v-model="form.descriptif.td2" type="text" />
          </label>
        </div>
      </section>

      <section class="card">
        <header class="card-header">
          <h2>6. Retour sur cours</h2>
        </header>
        <div class="feedback-grid">
          <label class="field">
            <span>Retour des étudiants</span>
            <textarea v-model="form.retours.etudiants" rows="4"></textarea>
          </label>
          <label class="field">
            <span>Retour de l'équipe pédagogique</span>
            <textarea v-model="form.retours.equipe" rows="4"></textarea>
          </label>
          <label class="field">
            <span>Heures prévues / Heures réalisées</span>
            <textarea v-model="form.retours.heures" rows="4"></textarea>
          </label>
        </div>
      </section>

      <section class="card">
        <header class="card-header">
          <h2>7. Tableau recapitulatif des heures</h2>
        </header>
        <div class="recap-table-wrapper">
          <table class="recap-table">
            <thead>
              <tr>
                <th>Ressource</th>
                <th>h CM</th>
                <th>h TD</th>
                <th>h TP</th>
                <th>h DS CM</th>
                <th>h DS TP</th>
                <th>Renseignement complementaire (Nombre eleve, de groupe, duree des seances)</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="row in form.recapRows" :key="row.id">
                <td><input v-model="row.label" type="text" /></td>
                <td><input v-model="row.hCM" type="number" min="0" step="0.5" /></td>
                <td><input v-model="row.hTD" type="number" min="0" step="0.5" /></td>
                <td><input v-model="row.hTP" type="number" min="0" step="0.5" /></td>
                <td><input v-model="row.hDSCM" type="number" min="0" step="0.5" /></td>
                <td><input v-model="row.hDSTP" type="number" min="0" step="0.5" /></td>
                <td><textarea v-model="row.notes" rows="2"></textarea></td>
              </tr>
            </tbody>
            <tfoot>
              <tr>
                <td class="totals-label">Totaux</td>
                <td class="totals-cell">{{ recapTotals.cm }}</td>
                <td class="totals-cell">{{ recapTotals.td }}</td>
                <td class="totals-cell">{{ recapTotals.tp }}</td>
                <td class="totals-cell">{{ recapTotals.dscm }}</td>
                <td class="totals-cell">{{ recapTotals.dstp }}</td>
                <td class="totals-cell"></td>
              </tr>
            </tfoot>
          </table>
        </div>
      </section>

      <footer class="footer">
        <button type="button" class="cta ghost">Retour</button>
        <span class="step">4/4</span>
        <button type="button" class="cta">Valider</button>
      </footer>
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
      semestres: ['S1', 'S2', 'S3', 'S4', 'S5', 'S6'],
      ressources: ['1', '2'],
      ues: [],
      mcccEntries: [],
      currentMccc: null,
      form: {
        semestre: '',
        ue: '',
        ressource: '',
        coefficient: '',
        heures: { cm: '', td: '', tp: '', controle: '' },
        prerequis: '',
        objectif: '',
        sae: '',
        connaissance: '',
        descriptif: { cm1: '', cm2: '', td1: '', td2: '' },
        retours: { etudiants: '', equipe: '', heures: '' },
        recapRows: [
          { id: 1, label: '', hCM: '', hTD: '', hTP: '', hDSCM: '', hDSTP: '', notes: '' },
          { id: 2, label: '', hCM: '', hTD: '', hTP: '', hDSCM: '', hDSTP: '', notes: '' },
          { id: 3, label: '', hCM: '', hTD: '', hTP: '', hDSCM: '', hDSTP: '', notes: '' },
          { id: 4, label: '', hCM: '', hTD: '', hTP: '', hDSCM: '', hDSTP: '', notes: '' },
          { id: 5, label: '', hCM: '', hTD: '', hTP: '', hDSCM: '', hDSTP: '', notes: '' }
        ]
      }
    };
  },
  created() {
    this.loadMcccIndex();
  },
  watch: {
    'form.ue'(newUe) {
      this.applyMcccFromUe(newUe);
    },
    'form.ressource'(newRessource) {
      this.applyResourceFromMccc(newRessource);
    }
  },
  computed: {
    recapTotals() {
      const sum = key => this.form.recapRows.reduce((acc, row) => acc + (Number(row[key]) || 0), 0);
      return {
        cm: sum('hCM'),
        td: sum('hTD'),
        tp: sum('hTP'),
        dscm: sum('hDSCM'),
        dstp: sum('hDSTP')
      };
    }
  },
  methods: {
    loadMcccIndex() {
      const list = JSON.parse(localStorage.getItem('mcccList') || '[]');
      this.mcccEntries = list;
      this.ues = [...new Set(list.map(entry => entry.ue).filter(Boolean))];
    },
    findLatestMcccForUe(ueLabel) {
      const matches = this.mcccEntries.filter(entry => entry.ue === ueLabel);
      if (!matches.length) return null;
      return matches
        .sort((a, b) => new Date(b.savedAt || 0) - new Date(a.savedAt || 0))[0];
    },
    applyMcccFromUe(ueLabel) {
      if (!ueLabel) {
        this.currentMccc = null;
        return;
      }

      if (!this.mcccEntries.length) {
        this.loadMcccIndex();
      }

      const entry = this.findLatestMcccForUe(ueLabel);
      if (!entry) {
        this.currentMccc = null;
        return;
      }

      this.currentMccc = entry;
      const mcccForm = entry.form || {};

      if (mcccForm.semestre) this.form.semestre = mcccForm.semestre;
      if (mcccForm.coeffRessource !== undefined && mcccForm.coeffRessource !== '') {
        this.form.coefficient = mcccForm.coeffRessource;
      }
      if (mcccForm.objectif) this.form.objectif = mcccForm.objectif;

      const rows = (entry.ressourcesRows || []).map((row, index) => ({
        id: row.id || index + 1,
        label: row.label || '',
        hCM: row.hCM ?? '',
        hTD: row.hTD ?? '',
        hTP: row.hTP ?? '',
        hDSCM: row.hDSCM ?? '',
        hDSTP: row.hDSTP ?? '',
        notes: row.notes || ''
      }));

      if (rows.length) {
        this.form.recapRows = rows;
        this.ressources = rows.map(row => row.label).filter(Boolean);
        if (!this.ressources.includes(this.form.ressource)) {
          this.form.ressource = '';
        }
      }

      this.applyResourceFromMccc(this.form.ressource);
    },
    applyResourceFromMccc(resourceLabel) {
      if (!this.currentMccc || !resourceLabel) return;
      const row = (this.currentMccc.ressourcesRows || []).find(item => item.label === resourceLabel);
      if (!row) return;
      this.form.heures = {
        cm: row.hCM ?? '',
        td: row.hTD ?? '',
        tp: row.hTP ?? '',
        controle: (Number(row.hDSCM) || 0) + (Number(row.hDSTP) || 0)
      };
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
  background: var(--color-sidebar-bg, #f7f8ff);
  border: 1px solid var(--color-border, #dfe4ef);
  border-radius: 20px;
  padding: 20px 20px 16px;
  box-shadow: var(--shadow, 0 4px 6px rgba(0,0,0,0.05));
}

.resource-sheet {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.card {
  background: var(--color-card-bg, #fff);
  border: 1px solid var(--color-border, #d3d7e0);
  border-radius: 16px;
  padding: 14px 16px 18px;
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.06);
}

.card-header h2 {
  margin: 0 0 12px;
  font-size: 1.3rem;
  font-weight: 700;
  color: var(--color-primary, #c00000);
}

.fields-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
  align-items: end;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 0.95rem;
}

.field span {
  color: var(--color-text-body, #444);
}

select,
input,
textarea {
  width: 100%;
  border: 1px solid var(--color-border, #c8ceda);
  border-radius: 12px;
  padding: 10px 12px;
  font-size: 0.95rem;
  font-family: var(--font-secondary, 'Montserrat', sans-serif);
  background: var(--color-input-bg, #fff);
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
  box-sizing: border-box;
}

select:focus,
input:focus,
textarea:focus {
  border-color: var(--color-primary, #c00000);
  box-shadow: 0 0 0 3px rgba(var(--color-primary-rgb, 192, 0, 0), 0.12);
}

.coefficient input {
  max-width: 200px;
}

.table-wrapper {
  margin-top: 12px;
  border: none;
  border-radius: 0;
  overflow: hidden;
  padding: 0;
}

.hours-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

.hours-table th,
.hours-table td {
  border: 1px solid var(--color-border, #c8ceda);
  padding: 8px 10px;
  text-align: left;
  background: var(--color-card-bg, #fff);
  width: 25%;
}

.hours-table th {
  background: var(--color-sidebar-bg, #f4f4f9);
  font-weight: 700;
}

.hours-input {
  width: 100%;
  box-sizing: border-box;
  margin: 0;
  border-radius: 8px;
}

.objectives {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  column-gap: 16px;
  row-gap: 12px;
  width: 100%;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 10px 14px;
}

.feedback-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 12px;
}

.recap-table-wrapper {
  overflow-x: auto;
}

.recap-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

.recap-table th,
.recap-table td {
  border: 1px solid var(--color-border, #c8ceda);
  padding: 8px 10px;
  background: var(--color-card-bg, #fff);
  vertical-align: top;
}

.recap-table th {
  background: var(--color-sidebar-bg, #f4f4f9);
  font-weight: 700;
}

.recap-table input,
.recap-table textarea {
  width: 100%;
  box-sizing: border-box;
}

.recap-table textarea {
  min-height: 52px;
  resize: vertical;
}

.totals-label {
  font-weight: 700;
  background: var(--color-sidebar-bg, #f4f4f9);
}

.totals-cell {
  font-weight: 700;
  text-align: center;
  background: var(--color-sidebar-bg, #f4f4f9);
}

textarea {
  resize: vertical;
  min-height: 90px;
}

.footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 4px 2px;
  color: var(--color-text-body, #444);
  font-weight: 600;
}

.cta.ghost {
  background: var(--color-card-bg, #fff);
  color: var(--color-primary, #c00000);
  border: 1px solid var(--color-primary, #c00000);
  box-shadow: none;
}

.cta {
  background: var(--color-primary, #c00000);
  color: #fff;
  border: none;
  border-radius: 10px;
  padding: 10px 18px;
  font-weight: 700;
  cursor: pointer;
  box-shadow: 0 10px 20px rgba(192, 0, 0, 0.2);
  transition: background 0.2s ease, transform 0.1s ease;
}

.cta:hover {
  background: var(--color-primary-dark, #a00000);
}

.cta:active {
  transform: translateY(1px);
}

.step {
  font-size: 0.95rem;
}

@media (max-width: 720px) {
  .fields-row {
    grid-template-columns: 1fr;
  }
  .coefficient input {
    max-width: none;
  }
}
</style>
