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
      semestres: ['1', '2'],
      ressources: ['1', '2'],
      form: {
        semestre: '',
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
  }
};
</script>

<style scoped>
.page-title {
  font-family: 'Poppins', sans-serif;
  font-size: 2.2rem;
  font-weight: 700;
  margin: 0;
}

.page-surface {
  background: linear-gradient(120deg, #f4f6fb 0%, #ffffff 60%);
  border: 1px solid #dfe4ef;
  border-radius: 16px;
  padding: 18px 18px 14px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.08);
}

.resource-sheet {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.card {
  background: #f9fbff;
  border: 1px solid #d3d7e0;
  border-radius: 14px;
  padding: 14px 16px 18px;
}

.card-header h2 {
  margin: 0 0 12px;
  font-size: 1.3rem;
  font-weight: 700;
  color: #1e1e1e;
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
  color: #444;
}

select,
input,
textarea {
  width: 100%;
  border: 1px solid #c8ceda;
  border-radius: 8px;
  padding: 10px 12px;
  font-size: 0.95rem;
  font-family: 'Montserrat', sans-serif;
  background: #fff;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
  box-sizing: border-box;
}

select:focus,
input:focus,
textarea:focus {
  border-color: #c00000;
  box-shadow: 0 0 0 3px rgba(192, 0, 0, 0.15);
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
  border: 1px solid #c8ceda;
  padding: 8px 10px;
  text-align: left;
  background: #fff;
  width: 25%;
}

.hours-table th {
  background: #edf1f9;
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
  border: 1px solid #c8ceda;
  padding: 8px 10px;
  background: #fff;
  vertical-align: top;
}

.recap-table th {
  background: #edf1f9;
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
  background: #f6f8fc;
}

.totals-cell {
  font-weight: 700;
  text-align: center;
  background: #f6f8fc;
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
  color: #444;
  font-weight: 600;
}

.cta.ghost {
  background: #fff;
  color: #c00000;
  border: 1px solid #c00000;
  box-shadow: none;
}

.cta {
  background: #c00000;
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
  background: #a00000;
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
