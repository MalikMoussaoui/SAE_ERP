<template>
  <DashboardLayout>
    <template #header>
      <h1 class="page-title">MCCC</h1>
    </template>

    <div class="page-surface mccc-surface">
      <section class="cards-grid top-grid">
        <div class="info-card">
          <h3>Organisation generale</h3>
          <div class="card-fields">
            <label class="field">
              <span>Departement</span>
              <select v-model="form.departement" class="pill-select">
                <option value="" disabled>Choisir un departement</option>
                <option v-for="option in departements" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>Annee</span>
              <select v-model="form.annee" class="pill-select">
                <option value="" disabled>Selectionner</option>
                <option v-for="option in annees" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>Semestre</span>
              <select v-model="form.semestre" class="pill-select">
                <option value="" disabled>Selectionner</option>
                <option v-for="option in semestres" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
          </div>
        </div>

        <div class="info-card">
          <h3>Les ressources et SAE</h3>
          <div class="card-fields">
            <label class="field">
              <span>Modalite</span>
              <select v-model="form.modalite" class="pill-select">
                <option value="" disabled>Choisir une modalite</option>
                <option v-for="option in modalites" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>Code APOGEE</span>
              <select v-model="form.codeApogee" class="pill-select">
                <option value="" disabled>Code</option>
                <option v-for="option in codesApogee" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>Type d'evaluation</span>
              <select v-model="form.typeEvaluation" class="pill-select">
                <option value="" disabled>Selectionner</option>
                <option v-for="option in typesEvaluation" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>UE</span>
              <select v-model="form.ue" class="pill-select">
                <option value="" disabled>Selectionner l'UE</option>
                <option v-for="option in ues" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>Niveau de la competence</span>
              <select v-model="form.niveauCompetence" class="pill-select">
                <option value="" disabled>Choisir</option>
                <option v-for="option in niveauxCompetence" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
          </div>
        </div>

        <div class="info-card">
          <h3>Coefficient</h3>
          <div class="card-fields">
            <label class="field">
              <span>Coefficient SAE</span>
              <select v-model="form.coeffSae" class="pill-select">
                <option value="" disabled>Choisir</option>
                <option v-for="option in coeffsSae" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>Coefficient Ressource</span>
              <select v-model="form.coeffRessource" class="pill-select">
                <option value="" disabled>Choisir</option>
                <option v-for="option in coeffsRessource" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>Coefficient Total</span>
              <select v-model="form.coeffTotal" class="pill-select">
                <option value="" disabled>Total</option>
                <option v-for="option in coeffsTotal" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
          </div>
        </div>
      </section>

      <section class="cards-grid mid-grid">
        <div class="info-card">
          <h3>Organisation generale</h3>
          <div class="card-fields">
            <label class="field">
              <span>Regle de validation</span>
              <select v-model="form.regleValidation" class="pill-select">
                <option value="" disabled>Selectionner</option>
                <option v-for="option in reglesValidation" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>Rattachement</span>
              <select v-model="form.rattachement" class="pill-select">
                <option value="" disabled>Selectionner</option>
                <option v-for="option in rattachements" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>Baseline</span>
              <select v-model="form.baseline" class="pill-select">
                <option value="" disabled>Selectionner</option>
                <option v-for="option in baselines" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
          </div>
        </div>

        <div class="info-card">
          <h3>Organisation generale</h3>
          <div class="card-fields">
            <label class="field">
              <span>Responsable pedagogique</span>
              <select v-model="form.responsable" class="pill-select">
                <option value="" disabled>Selectionner</option>
                <option v-for="option in responsables" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>Objectif de l'UE</span>
              <select v-model="form.objectif" class="pill-select">
                <option value="" disabled>Selectionner</option>
                <option v-for="option in objectifs" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
          </div>
        </div>
      </section>

      <section class="table-section">
        <div class="table-card">
          <div class="table-scroll">
            <table>
              <thead>
                <tr>
                  <th>Nom Ressource </th>
                  <th>h CM</th>
                  <th>h TD</th>
                  <th>h TP</th>
                  <th>h DS CM</th>
                  <th>h DS TP</th>
                  <th>Ajouter information</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="row in ressourcesRows" :key="row.id">
                  <td>
                    <input class="table-input" v-model="row.label" :placeholder="`Ressource ${row.id}`" />
                  </td>
                  <td><input class="table-input" v-model="row.hCM" type="number" min="0" step="0.5" /></td>
                  <td><input class="table-input" v-model="row.hTD" type="number" min="0" step="0.5" /></td>
                  <td><input class="table-input" v-model="row.hTP" type="number" min="0" step="0.5" /></td>
                  <td><input class="table-input" v-model="row.hDSCM" type="number" min="0" step="0.5" /></td>
                  <td><input class="table-input" v-model="row.hDSTP" type="number" min="0" step="0.5" /></td>
                  <td class="info-cell">
                    <div v-if="row.showDetails" class="info-editor">
                      <textarea
                        v-model="row.notes"
                        rows="3"
                        class="info-textarea"
                        placeholder="Ajoutez eleves, groupes, duree..."
                      ></textarea>
                      <div class="info-actions">
                        <button type="button" class="btn-finish" @click="row.showDetails = false">Terminer</button>
                      </div>
                    </div>
                    <div v-else class="info-collapsed">
                      <div class="info-preview">{{ row.notes || 'Aucune information' }}</div>
                      <button type="button" class="btn-add" @click="row.showDetails = true">
                        {{ row.notes ? 'Modifier' : 'Ajouter information' }}
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="final-totals single">
            <span class="final-label">TOTAUX HEURES</span>
            <span class="single-total">{{ grandTotal }}</span>
          </div>
        </div>
      </section>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';

export default {
  name: 'McccView',
  components: { DashboardLayout },
  data() {
    return {
      form: {
        departement: '',
        annee: '',
        semestre: '',
        modalite: '',
        codeApogee: '',
        typeEvaluation: '',
        ue: '',
        niveauCompetence: '',
        coeffSae: '',
        coeffRessource: '',
        coeffTotal: '',
        regleValidation: '',
        rattachement: '',
        baseline: '',
        responsable: '',
        objectif: ''
      },
      departements: ['1', '2'],
      annees: ['1', '2'],
      semestres: ['1', '2'],
      modalites: ['1', '2'],
      codesApogee: ['1', '2'],
      typesEvaluation: ['1', '2'],
      ues: ['1', '2'],
      niveauxCompetence: ['1', '2'],
      coeffsSae: ['1', '2'],
      coeffsRessource: ['1', '2'],
      coeffsTotal: ['1', '2'],
      reglesValidation: ['1', '2'],
      rattachements: ['1', '2'],
      baselines: ['1', '2'],
      responsables: ['1', '2'],
      objectifs: ['1', '2'],
      ressourcesRows: [
        { id: 1, label: 'Nom ressource 1', hCM: 0, hTD: 0, hTP: 0, hDSCM: 0, hDSTP: 0, notes: '', showDetails: false },
        { id: 2, label: 'Nom ressource 2', hCM: 0, hTD: 0, hTP: 0, hDSCM: 0, hDSTP: 0, notes: '', showDetails: false },
        { id: 3, label: 'Nom ressource 3', hCM: 0, hTD: 0, hTP: 0, hDSCM: 0, hDSTP: 0, notes: '', showDetails: false },
        { id: 4, label: 'Nom ressource 4', hCM: 0, hTD: 0, hTP: 0, hDSCM: 0, hDSTP: 0, notes: '', showDetails: false },
        { id: 5, label: 'Nom ressource 5', hCM: 0, hTD: 0, hTP: 0, hDSCM: 0, hDSTP: 0, notes: '', showDetails: false },
        { id: 6, label: 'Nom ressource 6', hCM: 0, hTD: 0, hTP: 0, hDSCM: 0, hDSTP: 0, notes: '', showDetails: false }
      ]
    };
  },
  computed: {
    totals() {
      const sum = key => this.ressourcesRows.reduce((acc, row) => acc + (Number(row[key]) || 0), 0);
      return {
        cm: sum('hCM'),
        td: sum('hTD'),
        tp: sum('hTP'),
        dscm: sum('hDSCM'),
        dstp: sum('hDSTP')
      };
    },
    grandTotal() {
      return ['hCM', 'hTD', 'hTP', 'hDSCM', 'hDSTP'].reduce(
        (acc, key) => acc + (this.totals[key === 'hDSCM' ? 'dscm' : key === 'hDSTP' ? 'dstp' : key === 'hCM' ? 'cm' : key === 'hTD' ? 'td' : 'tp'] || 0),
        0
      );
    }
  }
};
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
  background: linear-gradient(145deg, #f7f8ff 0%, #ffffff 60%);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 20px;
  padding: 22px;
  box-shadow: var(--shadow, 0 4px 6px rgba(0,0,0,0.05));
}

.mccc-surface {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.cards-grid {
  display: grid;
  gap: 14px;
}

.top-grid {
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
}

.mid-grid {
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
}

.info-card {
  background: var(--color-card-bg, #fff);
  border: 1px solid var(--color-border, #d7e0ff);
  border-radius: 16px;
  padding: 14px 16px 16px;
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.06);
}

.info-card h3 {
  margin: 0 0 12px;
  color: #c00000;
  font-size: 1.05rem;
  letter-spacing: 0.2px;
  text-align: center;
}

.card-fields {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(190px, 1fr));
  gap: 10px 14px;
  justify-items: center;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 0.92rem;
  color: var(--color-text-body, #444);
  width: 100%;
}

.field span {
  padding-left: 2px;
}

.pill-select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d7e0ff;
  border-radius: 12px;
  background: #fff;
  font-family: var(--font-secondary, 'Montserrat', sans-serif);
  font-size: 0.95rem;
  color: var(--color-text-body, #333);
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='%23c00000'%3E%3Cpath d='M5 8l7 7 7-7z'/%3E%3C/svg%3E");
  background-position: right 12px center;
  background-repeat: no-repeat;
  background-size: 16px 16px;
}

.pill-select:focus {
  border-color: var(--color-primary, #c00000);
  box-shadow: 0 0 0 3px rgba(192, 0, 0, 0.12);
  outline: none;
}

.table-section {
  margin-top: 8px;
}

.table-card {
  background: var(--color-card-bg, white);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 18px;
  padding: 12px;
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.05);
}

.table-scroll {
  max-height: 520px;
  overflow: auto;
}

.final-totals {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 14px;
  background: #fafafa;
  border-top: 1px solid var(--color-border, #e6e6e6);
  border-radius: 0 0 10px 10px;
  font-weight: 600;
  color: var(--color-text-body, #444);
}

.final-label {
  color: #c00000;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.4px;
}

.single-total {
  font-size: 1.05rem;
}

table {
  width: 100%;
  border-collapse: collapse;
  font-family: var(--font-secondary, 'Montserrat', sans-serif);
  font-size: 0.94rem;
}

thead th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: var(--color-sidebar-bg, #f4f4f9);
  color: var(--color-text-header, #333);
  font-weight: 600;
  text-align: left;
  padding: 12px 14px;
  border-bottom: 1px solid var(--color-border, #e6e6e6);
}

tbody td, tfoot td {
  padding: 10px 14px;
  border-bottom: 1px solid var(--color-border, #f0f0f0);
}

tr:nth-child(even) td {
  background: rgba(0, 0, 0, 0.015);
}

.table-input {
  width: 100%;
  padding: 8px 10px;
  border: 1px solid #d7e0ff;
  border-radius: 10px;
  font-size: 0.92rem;
  color: var(--color-text-body, #333);
  outline: none;
}

.table-input:focus {
  border-color: var(--color-primary, #c00000);
  box-shadow: 0 0 0 3px rgba(192, 0, 0, 0.1);
}

.info-cell {
  min-width: 220px;
}

.info-collapsed {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.info-preview {
  min-height: 20px;
  color: var(--color-text-muted, #777);
  font-size: 0.9rem;
}

.info-editor {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-textarea {
  width: 100%;
  padding: 8px 10px;
  border: 1px solid #d7e0ff;
  border-radius: 10px;
  font-size: 0.9rem;
  font-family: var(--font-secondary, 'Montserrat', sans-serif);
  resize: vertical;
  min-height: 64px;
}

.info-textarea:focus {
  border-color: var(--color-primary, #c00000);
  box-shadow: 0 0 0 3px rgba(192, 0, 0, 0.1);
  outline: none;
}

.info-actions {
  display: flex;
  justify-content: flex-end;
}

.btn-add,
.btn-finish {
  align-self: flex-start;
  background-color: var(--color-primary, #c00000);
  color: #fff;
  border: none;
  border-radius: 10px;
  padding: 8px 12px;
  font-weight: 600;
  cursor: pointer;
  font-size: 0.9rem;
  box-shadow: 0 6px 14px rgba(192, 0, 0, 0.18);
  transition: background 0.2s;
}

.btn-add:hover,
.btn-finish:hover {
  background-color: var(--color-primary-dark, #a00000);
}

.totals-row td {
  font-weight: 700;
  background: #fafafa;
}

.totals-label {
  text-transform: uppercase;
  letter-spacing: 0.3px;
  color: #c00000;
}

@media (max-width: 900px) {
  .page-title {
    font-size: 2rem;
  }
  .cards-grid {
    grid-template-columns: 1fr;
  }
  thead {
    display: none;
  }
  tbody tr {
    display: grid;
    grid-template-columns: 1fr;
    gap: 6px;
    padding: 12px;
    border: 1px solid var(--color-border, #eee);
    border-radius: 12px;
    margin-bottom: 10px;
  }
  tbody td {
    border: none;
    background: none;
    padding: 0;
  }
  .table-card {
    padding: 10px;
  }
}
</style>
