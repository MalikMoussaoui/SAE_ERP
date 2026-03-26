<template>
  <DashboardLayout>
    <CustomModal
      v-if="modal.show"
      :title="modal.title"
      :message="modal.message"
      :type="modal.type"
      :showCancel="modal.showCancel"
      :confirmLabel="modal.confirmLabel"
      :cancelLabel="modal.cancelLabel"
      @close="modal.show = false"
      @confirm="handleModalConfirm"
    />
    <template #header>
      <h1 class="page-title">{{ $t('nav.mccc') }}</h1>
    </template>

    <div class="page-surface mccc-surface">
      <div v-if="!showAllSteps" class="step-indicator">
        <div class="step" :class="{ active: currentStep >= 1, completed: currentStep > 1 }">
          <div class="step-number">1</div>
          <div class="step-label">{{ $t('mccc.step1_label') }}</div>
        </div>
        <div class="step-connector"></div>
        <div class="step" :class="{ active: currentStep >= 2, completed: currentStep > 2 }">
          <div class="step-number">2</div>
          <div class="step-label">{{ $t('mccc.step2_label') }}</div>
        </div>
        <div class="step-connector"></div>
        <div class="step" :class="{ active: currentStep >= 3, completed: currentStep > 3 }">
          <div class="step-number">3</div>
          <div class="step-label">{{ $t('mccc.step3_label') }}</div>
        </div>
      </div>

      <section v-if="showAllSteps || currentStep === 1" class="cards-grid top-grid">
        <div class="info-card">
          <h3>{{ $t('mccc.generalOrganization') }}</h3>
          <div class="card-fields">
            <label class="field">
              <span>{{ $t('mccc.department') }}</span>
              <select v-model="form.departement" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.departement }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.chooseDepartment') }}</option>
                <option v-for="option in departements" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>{{ $t('mccc.year') }}</span>
              <select v-model="form.annee" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.annee }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.select') }}</option>
                <option v-for="option in annees" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>{{ $t('mccc.semester') }}</span>
              <select v-model="form.semestre" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.semestre }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.select') }}</option>
                <option v-for="option in semestres" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
          </div>
        </div>

        <div class="info-card">
          <h3>{{ $t('mccc.resourcesAndSAE') }}</h3>
          <div class="card-fields">
            <label class="field">
              <span>{{ $t('mccc.ue') }}</span>
              <select v-model="form.ue" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.ue }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.selectUE') }}</option>
                <option v-for="option in availableUes" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>{{ $t('mccc.resourceToWrite') }}</span>
              <input v-model="form.code" class="pill-select pill-input" :class="{ 'pill-invalid': showValidationErrors && !form.code }" :placeholder="$t('resourceSheet.resourceCode')" :disabled="isReadOnly" />
            </label>
            <label class="field">
              <span>{{ $t('mccc.competenceLevel') }}</span>
              <select v-model="form.niveauCompetence" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.niveauCompetence }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.choose') }}</option>
                <option v-for="option in niveauxCompetence" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
          </div>
        </div>

        <div class="info-card">
          <h3>{{ $t('mccc.coefficient') }}</h3>
          <div class="card-fields">
            <label class="field">
              <span>{{ $t('mccc.saeCoefficient') }}</span>
              <input v-model="form.coeffSae" type="number" min="0" step="0.1" @keydown="onlyNumbers($event, 'coeffSae')" @input="validatePositive(form, 'coeffSae')" :class="{ 'pill-invalid': fieldErrors['coeffSae'] }" class="pill-select" :placeholder="$t('mccc.choose')" :disabled="isReadOnly" />
              <span v-if="fieldErrors['coeffSae']" class="error-hint">{{ fieldErrors['coeffSae'] }}</span>
            </label>
            <label class="field">
              <span>{{ $t('mccc.resourceCoefficient') }}</span>
              <input v-model="form.coeffRessource" type="number" min="0" step="0.1" @keydown="onlyNumbers($event, 'coeffRessource')" @input="validatePositive(form, 'coeffRessource')" :class="{ 'pill-invalid': fieldErrors['coeffRessource'] }" class="pill-select" :placeholder="$t('mccc.choose')" :disabled="isReadOnly" />
              <span v-if="fieldErrors['coeffRessource']" class="error-hint">{{ fieldErrors['coeffRessource'] }}</span>
            </label>
            <label class="field">
              <span>{{ $t('mccc.totalCoefficient') }}</span>
              <input v-model="form.coeffTotal" type="number" min="0" step="0.1" @keydown="onlyNumbers($event, 'coeffTotal')" @input="validatePositive(form, 'coeffTotal')" :class="{ 'pill-invalid': fieldErrors['coeffTotal'] }" class="pill-select" :placeholder="$t('mccc.total')" :disabled="isReadOnly" />
              <span v-if="fieldErrors['coeffTotal']" class="error-hint">{{ fieldErrors['coeffTotal'] }}</span>
            </label>
          </div>
        </div>
      </section>

      <section v-if="showAllSteps || currentStep === 2" class="cards-grid mid-grid">
        <div class="info-card">
          <h3>{{ $t('mccc.validationAndMore') }}</h3>
          <div class="card-fields">
            <label class="field">
              <span>{{ $t('mccc.evaluationType') }}</span>
              <select v-model="form.typeEvaluation" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.typeEvaluation }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.select') }}</option>
                <option v-for="option in typesEvaluation" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>{{ $t('mccc.validationRule') }}</span>
              <select v-model="form.regleValidation" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.regleValidation }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.select') }}</option>
                <option v-for="option in reglesValidation" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>{{ $t('mccc.retake') }}</span>
              <select v-model="form.rattrapage" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.rattrapage }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.select') }}</option>
                <option value="OUI">{{ $t('common.yes') }}</option>
                <option value="NON">{{ $t('common.no') }}</option>
              </select>
            </label>
            <label class="field">
              <span>{{ $t('mccc.compensation') }}</span>
              <select v-model="form.compensation" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.compensation }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.select') }}</option>
                <option value="OUI">{{ $t('common.yes') }}</option>
                <option value="NON">{{ $t('common.no') }}</option>
              </select>
            </label>
            <label class="field">
              <span>{{ $t('mccc.pedagogicalManager') }}</span>
              <select v-model="form.responsable" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.responsable }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.select') }}</option>
                <option v-for="option in responsables" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field">
              <span>{{ $t('mccc.ueObjective') }}</span>
              <select v-model="form.objectif" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.objectif }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.select') }}</option>
                <option v-for="option in objectifs" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
          </div>
        </div>
      </section>
      <section v-if="showAllSteps || currentStep === 3" class="table-section">
        <div class="table-card">
          <div class="table-scroll">
            <table>
              <thead>
                <tr>
                  <th>{{ $t('mccc.table.resourceName') }}</th>
                  <th>{{ $t('mccc.table.hCM') }}</th>
                  <th>{{ $t('mccc.table.hTD') }}</th>
                  <th>{{ $t('mccc.table.hTP') }}</th>
                  <th>{{ $t('mccc.table.hDSCM') }}</th>
                  <th>{{ $t('mccc.table.hDSTP') }}</th>
                  <th>{{ $t('mccc.table.addInfo') }}</th>
                  <th class="action-header">{{ $t('mccc.table.action') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="row in ressourcesRows" :key="row.id">
                  <td>
                    <input class="table-input" v-model="row.label" :placeholder="$t('mccc.resourcePlaceholder', { id: row.id })" :disabled="isReadOnly" />
                  </td>
                  <td>
                    <input class="table-input" v-model="row.hCM" type="number" min="0" step="0.5" @keydown="onlyNumbers($event, 'hCM', row.id)" @input="validatePositive(row, 'hCM', true)" :class="{ 'pill-invalid': fieldErrors['hCM_' + row.id] }" :disabled="isReadOnly" />
                    <span v-if="fieldErrors['hCM_' + row.id]" class="error-hint-table">{{ fieldErrors['hCM_' + row.id] }}</span>
                  </td>
                  <td>
                    <input class="table-input" v-model="row.hTD" type="number" min="0" step="0.5" @keydown="onlyNumbers($event, 'hTD', row.id)" @input="validatePositive(row, 'hTD', true)" :class="{ 'pill-invalid': fieldErrors['hTD_' + row.id] }" :disabled="isReadOnly" />
                    <span v-if="fieldErrors['hTD_' + row.id]" class="error-hint-table">{{ fieldErrors['hTD_' + row.id] }}</span>
                  </td>
                  <td>
                    <input class="table-input" v-model="row.hTP" type="number" min="0" step="0.5" @keydown="onlyNumbers($event, 'hTP', row.id)" @input="validatePositive(row, 'hTP', true)" :class="{ 'pill-invalid': fieldErrors['hTP_' + row.id] }" :disabled="isReadOnly" />
                    <span v-if="fieldErrors['hTP_' + row.id]" class="error-hint-table">{{ fieldErrors['hTP_' + row.id] }}</span>
                  </td>
                  <td>
                    <input class="table-input" v-model="row.hDSCM" type="number" min="0" step="0.5" @keydown="onlyNumbers($event, 'hDSCM', row.id)" @input="validatePositive(row, 'hDSCM', true)" :class="{ 'pill-invalid': fieldErrors['hDSCM_' + row.id] }" :disabled="isReadOnly" />
                    <span v-if="fieldErrors['hDSCM_' + row.id]" class="error-hint-table">{{ fieldErrors['hDSCM_' + row.id] }}</span>
                  </td>
                  <td>
                    <input class="table-input" v-model="row.hDSTP" type="number" min="0" step="0.5" @keydown="onlyNumbers($event, 'hDSTP', row.id)" @input="validatePositive(row, 'hDSTP', true)" :class="{ 'pill-invalid': fieldErrors['hDSTP_' + row.id] }" :disabled="isReadOnly" />
                    <span v-if="fieldErrors['hDSTP_' + row.id]" class="error-hint-table">{{ fieldErrors['hDSTP_' + row.id] }}</span>
                  </td>
                  <td class="info-cell">
                    <div v-if="row.showDetails" class="info-editor">
                      <textarea
                        v-model="row.notes"
                        rows="3"
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
                    <button v-if="ressourcesRows.length > 1" @click="deleteRow(row.id)" class="btn-delete" :title="$t('mccc.deleteRow')" :disabled="isReadOnly">
                      &times;
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="table-footer">
            <button @click="addRow" class="btn-add-row" :disabled="isReadOnly">
              + {{ $t('mccc.addRow') }}
            </button>
            <span v-if="errorMessage" class="error-text">{{ errorMessage }}</span>
            <div class="final-totals single">
              <span class="final-label">{{ $t('mccc.totalHours') }}</span>
              <span class="single-total">{{ grandTotal }}</span>
            </div>
          </div>
        </div>
      </section>

      <div v-if="!showAllSteps" class="step-navigation">
        <span v-if="stepErrorMessage" class="step-error">{{ stepErrorMessage }}</span>
        <button v-if="currentStep > 1" @click="prevStep" class="btn btn-secondary">
          {{ $t('mccc.back') }}
        </button>
        <button v-if="currentStep < 3" @click="nextStep" class="btn btn-primary">
          {{ $t('mccc.continue') }}
        </button>
        <button v-if="currentStep === 3 && editingId" class="btn btn-danger" type="button" @click="confirmDelete" :disabled="isReadOnly">
          Supprimer
        </button>
        <button v-if="currentStep === 3" class="btn btn-primary" type="button" @click="saveMccc" :disabled="isReadOnly">
          {{ $t('mccc.save') }}
        </button>
      </div>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';
import CustomModal from '@/components/CustomModal.vue';
import axios from 'axios';

export default {
  name: 'McccView',
  components: { DashboardLayout, CustomModal },
    data() {
    return {
      currentStep: 1,
      form: {
        departement: '',
        annee: '',
        semestre: '',
        code: '',
        typeEvaluation: '',
        ue: '',
        niveauCompetence: '',
        coeffSae: '',
        coeffRessource: '',
        coeffTotal: '',
        regleValidation: '',
        rattrapage: '',
        compensation: '',
        rattachement: '',
        bareme: '',
        responsable: '',
        objectif: ''
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
      annees: ['1ere annee (BUT1)', '2eme annee (BUT2)', '3eme annee (BUT3)'],
      typesEvaluation: ['Soutenance', 'QCM', 'SAé', 'Devoir sur table', 'Rapport de stage'],
      uesByDepartement: {
        'BUT Informatique': [
          'UE Realiser des applications',
          'UE Optimiser des applications',
          'UE Administrer des systemes informatiques communicants',
          "UE Gerer des donnees de l'information",
          'UE Conduire un projet',
          'UE Collaborer dans un environnement professionnel'
        ],
        'BUT GEA - Gestion des Entreprises et des Administrations': [
          'UE Gerer les organisations',
          'UE Piloter la performance',
          "UE Produire l information comptable financiere et de gestion",
          'UE Developper une activite economique',
          'UE Conduire un projet',
          'UE Communiquer et travailler en equipe'
        ],
        'BUT TC - Techniques de Commercialisation': [
          'UE Developper la relation client',
          'UE Mettre en oeuvre une strategie marketing',
          'UE Vendre une offre commerciale',
          "UE Analyser le marche et l environnement",
          'UE Conduire un projet commercial',
          'UE Communiquer dans un contexte professionnel'
        ],
        'BUT Mesures Physiques (MP)': [
          'UE Realiser des mesures physiques',
          'UE Exploiter des donnees experimentales',
          'UE Mettre en oeuvre des protocoles experimentaux',
          'UE Caracteriser des systemes physiques',
          'UE Conduire un projet scientifique',
          'UE Communiquer en milieu scientifique'
        ],
        'BUT Genie Mecanique et Productique (GMP)': [
          'UE Concevoir des systemes mecaniques',
          'UE Industrialiser un produit',
          'UE Organiser et gerer la production',
          'UE Ameliorer les performances industrielles',
          'UE Conduire un projet industriel',
          'UE Communiquer dans l industrie'
        ],
        'BUT GEII - Genie Electrique et Informatique Industrielle': [
          'UE Concevoir des systemes electroniques et automatises',
          'UE Programmer et exploiter des systemes industriels',
          'UE Mettre en oeuvre des reseaux industriels',
          'UE Maintenir et ameliorer des installations',
          'UE Conduire un projet industriel',
          'UE Communiquer dans un contexte technique'
        ],
        'BUT Genie Civil - Construction Durable': [
          'UE Concevoir des ouvrages de construction',
          'UE Dimensionner des structures',
          'UE Organiser un chantier',
          'UE Integrer le developpement durable',
          'UE Conduire un projet de construction',
          'UE Communiquer dans le secteur du BTP'
        ],
        'BUT Genie Biologique (GB)': [
          'UE Realiser des analyses biologiques',
          'UE Exploiter des resultats experimentaux',
          'UE Mettre en oeuvre des procedes biologiques',
          'UE Assurer la qualite et la securite',
          'UE Conduire un projet scientifique',
          'UE Communiquer dans un contexte biologique'
        ],
        'BUT MMI - Metiers du Multimedia et de l Internet': [
          'UE Concevoir des produits multimedia',
          'UE Developper des dispositifs interactifs',
          'UE Creer des contenus graphiques et audiovisuels',
          'UE Mettre en oeuvre une strategie de communication',
          'UE Conduire un projet multimedia',
          'UE Travailler en equipe et communiquer'
        ],
        'BUT GIM - Genie Industriel et Maintenance': [
          'UE Maintenir des systemes industriels',
          'UE Ameliorer la fiabilite des equipements',
          'UE Diagnostiquer des pannes',
          'UE Optimiser la maintenance',
          'UE Conduire un projet industriel',
          'UE Communiquer en milieu professionnel'
        ],
        'BUT HSE - Hygiene, Securite, Environnement': [
          'UE Prevenir les risques professionnels',
          'UE Gerer la securite et la sante au travail',
          "UE Proteger l environnement",
          'UE Mettre en conformite reglementaire',
          'UE Conduire un projet HSE',
          'UE Communiquer et sensibiliser'
        ],
        'BUT Carrieres Sociales': [
          'UE Analyser les situations sociales',
          'UE Accompagner des publics',
          'UE Concevoir des actions sociales',
          'UE Travailler en reseau partenarial',
          'UE Conduire un projet social',
          'UE Communiquer dans le champ social'
        ]
      },
      availableUes: [],
      niveauxCompetence: ['Niveau 1 - Decouverte', 'Niveau 2 - Application', 'Niveau 3 - Maitrise', 'Niveau 4 - Approfondissement'],
      coeffsSae: ['0,5', '1', '1,5', '2', '3'],
      coeffsRessource: ['0,25', '0,5', '1', '1,5', '2'],
      coeffsTotal: ['2', '3', '4', '5', '6'],
      reglesValidation: ['Compensation de semestre', 'Capitalisation UE', 'Seuil minimal par ressource', 'Session de rattrapage'],
      rattachements: ['Tronc commun', 'Parcours cybersecurite (INF)', 'Parcours data / IA', 'Parcours creation numerique (MMI)', 'Parcours commerce international (TC)'],
      baremes: ['Referentiel national BUT 2024', 'Adaptation locale IUT', 'Maquette formation initiale', 'Maquette apprentissage'],
      responsables: ['Vacataire', 'Responsable dUE', 'Referent SAE','Professeur'],
      objectifs: [
        'Valider les competences du semestre',
        'Preparer la professionnalisation',
        'Consolider les acquis scientifiques et techniques',
        'Developper les competences transversales'
      ],
      ressourcesRows: [
        { id: 1, label: '', hCM: 0, hTD: 0, hTP: 0, hDSCM: 0, hDSTP: 0, notes: '', showDetails: false }
      ],
      nextRowId: 2,
      errorMessage: '',
      errorTimeout: null,
      fieldErrors: {},
      isHydratingForm: false,
      stepErrorMessage: '',
      showValidationErrors: false,
      modal: {
        show: false,
        title: '',
        message: '',
        type: 'info',
        confirmLabel: 'OK',
        showCancel: false,
        action: null
      }
    };
  },
  created() {
    if (this.form.departement) {
      this.availableUes = this.uesByDepartement[this.form.departement] || [];
    }
    this.loadFromRoute();
  },
  watch: {
    'form.departement'(newDepartement, oldDepartement) {
      this.availableUes = this.uesByDepartement[newDepartement] || [];
      if (!this.isHydratingForm && newDepartement !== oldDepartement) {
        this.form.ue = '';
      }
    },
    '$route.query.id'() {
      this.loadFromRoute();
    },
    '$route.query.mode'() {
      this.isReadOnly = this.$route.query.mode === 'view';
    }
  },
  computed: {
    showAllSteps() {
      return this.isReadOnly && this.$route.query.mode === 'view';
    },
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
      return Object.values(this.totals).reduce((acc, total) => acc + total, 0);
    }
  },
  methods: {
    async loadFromRoute() {
      const { id, mode } = this.$route.query;
      this.isReadOnly = mode === 'view';

      if (!id) {
        this.editingId = null;
        return;
      }

      try {
        this.isHydratingForm = true;
        const response = await axios.get(`/mccc/${id}`);
        const mccc = response.data;

        this.currentStep = 1;
        this.editingId = mccc.id;
        this.form = {
            ...this.form,
            ...(mccc.form || {}),
            departement: mccc.departement || mccc.department || mccc.form?.departement || '',
            ue: mccc.ue || mccc.form?.ue || '',
            annee: mccc.annee || mccc.years || mccc.form?.annee || '',
            semestre: mccc.semestre || mccc.semester || mccc.form?.semestre || '',
            code: mccc.form?.code || mccc.form?.ressource || ''
        };

        if(this.form.departement) {
             this.availableUes = this.uesByDepartement[this.form.departement] || [];
        }

        this.ressourcesRows = mccc.ressourcesRows || [];
        if (this.ressourcesRows.length === 0) {
           this.ressourcesRows = [{ id: 1, label: '', hCM: 0, hTD: 0, hTP: 0, hDSCM: 0, hDSTP: 0, notes: '', showDetails: false }];
        }
        const maxId = this.ressourcesRows.reduce((max, row) => Math.max(max, Number(row.id) || 0), 0);
        this.nextRowId = maxId + 1;

      } catch (error) {
        console.error("Error loading MCCC", error);
      } finally {
        this.isHydratingForm = false;
      }
    },
    validateStep(step) {
      if (step === 1) return !!(this.form.departement && this.form.annee && this.form.semestre && this.form.ue && this.form.code && this.form.niveauCompetence);
      if (step === 2) return !!(this.form.typeEvaluation && this.form.regleValidation && this.form.rattrapage && this.form.compensation && this.form.responsable && this.form.objectif);
      return true;
    },
    nextStep() {
      if (this.validateStep(this.currentStep)) {
        this.stepErrorMessage = '';
        this.showValidationErrors = false;
        if (this.currentStep < 3) this.currentStep++;
      } else {
        this.stepErrorMessage = "Veuillez remplir les champs obligatoires en rouge pour continuer.";
        this.showValidationErrors = true;
      }
    },
    prevStep() {
      this.stepErrorMessage = '';
      this.showValidationErrors = false;
      if (this.currentStep > 1) this.currentStep--;
    },
    addRow() {
      this.ressourcesRows.push({
        id: this.nextRowId++,
        label: '',
        hCM: 0,
        hTD: 0,
        hTP: 0,
        hDSCM: 0,
        hDSTP: 0,
        notes: '',
        showDetails: false
      });
    },
    deleteRow(rowId) {
      if (this.ressourcesRows.length <= 1) return;
      this.ressourcesRows = this.ressourcesRows.filter(row => row.id !== rowId);
    },
    validatePositive(target, field, isTable = false) {
      const key = isTable ? `${field}_${target.id}` : field;
      if (target[field] < 0) {
        target[field] = 0;
        this.fieldErrors[key] = "Les nombres négatifs ne sont pas autorisés.";
        setTimeout(() => {
          this.fieldErrors[key] = "";
        }, 4000);
      } else {
        this.fieldErrors[key] = "";
      }
    },
    onlyNumbers(event, field, targetId = null) {
      const key = targetId ? `${field}_${targetId}` : field;
      
      if (event.key.length > 1 || event.ctrlKey || event.metaKey) return;
      if (['.', ','].includes(event.key)) return;

      if (['-', '+'].includes(event.key)) {
        event.preventDefault();
        this.fieldErrors[key] = "Les nombres négatifs ne sont pas autorisés.";
        setTimeout(() => { this.fieldErrors[key] = ""; }, 4000);
        return;
      }

      if (!/^[0-9]$/.test(event.key)) {
        event.preventDefault();
        this.fieldErrors[key] = "Seuls les chiffres sont acceptés.";
        setTimeout(() => { this.fieldErrors[key] = ""; }, 4000);
      }
    },
    async saveMccc() {
      const payload = {
        department: this.form.departement,
        ue: this.form.ue,
        years: this.form.annee,
        semester: this.form.semestre,
        form: { ...this.form, ressource: this.form.code || this.form.ressource || '' },
        ressourcesRows: this.ressourcesRows
      };

      try {
        if (this.editingId) {
             await axios.put(`/mccc/${this.editingId}`, payload);
        } else {
             await axios.post('/mccc', payload);
        }
        this.modal = {
          show: true,
          title: 'Succès',
          message: 'La fiche MCCC a été enregistrée avec succès.',
          type: 'success',
          confirmLabel: 'Retour à la liste',
          showCancel: false
        };
      } catch (e) {
        console.error("Error saving MCCC", e);
        this.modal = {
          show: true,
          title: 'Erreur',
          message: "Une erreur est survenue lors de l'enregistrement.",
          type: 'error',
          confirmLabel: 'Fermer',
          showCancel: false
        };
      }
    },
    confirmDelete() {
      this.modal = {
        show: true,
        title: 'Supprimer la MCCC ?',
        message: 'Cette action est irréversible. Voulez-vous vraiment supprimer cette fiche ?',
        type: 'warning',
        showCancel: true,
        confirmLabel: 'Supprimer',
        cancelLabel: 'Annuler',
        action: 'delete'
      };
    },
    async deleteMccc() {
      try {
        await axios.delete(`/mccc/${this.editingId}`);
        this.$router.push({ name: 'liste-mccc' });
      } catch (e) {
        console.error(e);
        this.modal = { show: true, title: 'Erreur', message: "Impossible de supprimer.", type: 'error', confirmLabel: 'Fermer', showCancel: false };
      }
    },
    handleModalConfirm() {
      this.modal.show = false;
      if (this.modal.action === 'delete') {
        this.deleteMccc();
      } else if (this.modal.type === 'success') {
        this.$router.push({ name: 'liste-mccc' });
      }
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
  background: var(--color-sidebar-bg, #f7f8ff);
  border: 1px solid var(--color-border);
  border-radius: 20px;
  padding: 22px;
  box-shadow: var(--shadow, 0 4px 6px rgba(0,0,0,0.05));
}

.mccc-surface {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

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

.step-label {
  margin-top: 0.5rem;
  font-size: 0.85rem;
  font-weight: 500;
}

.step-connector {
  flex-grow: 1;
  height: 2px;
  background-color: var(--color-border);
  margin: 0;
  transform: translateY(17px);
  transition: background-color 0.3s;
}

.step.active .step-number {
  border-color: var(--color-primary);
  color: var(--color-primary);
}
.step.active .step-label {
  color: var(--color-text-header);
}

.step.completed .step-number {
  background-color: var(--color-primary);
  border-color: var(--color-primary);
  color: white;
}

.step.completed + .step-connector {
  background-color: var(--color-primary);
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
  border: 1px solid var(--color-border);
  border-radius: 16px;
  padding: 14px 16px 16px;
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.06);
}

.info-card h3 {
  margin: 0 0 12px;
  color: var(--color-primary);
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
  box-sizing: border-box;
  border: 1px solid var(--color-border);
  border-radius: 12px;
  background-color: var(--color-input-bg, #fff);
  font-family: var(--font-secondary, 'Montserrat', sans-serif);
  font-size: 0.95rem;
  color: #000;
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='%23c00000'%3E%3Cpath d='M5 8l7 7 7-7z'/%3E%3C/svg%3E");
  background-position: right 12px center;
  background-repeat: no-repeat;
  background-size: 16px 16px;
}

.pill-select:disabled {
  background-color: var(--color-sidebar-bg, #f4f4f4);
  color: var(--color-text-muted, #777);
  cursor: not-allowed;
}

.pill-select:focus {
  border-color: var(--color-primary, #c00000);
  box-shadow: 0 0 0 3px rgba(var(--color-primary-rgb, 192, 0, 0), 0.12);
  outline: none;
}

.pill-input {
  background-image: none;
  padding-right: 12px;
}

.table-section {
  margin-top: 8px;
}

.table-card {
  background: var(--color-card-bg, white);
  border: 1px solid var(--color-border);
  border-radius: 18px;
  padding: 12px;
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.05);
}

.table-scroll {
  max-height: 520px;
  overflow-x: auto;
}

.table-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--color-sidebar-bg, #f4f4f9);
  border-top: 1px solid var(--color-border);
  border-radius: 0 0 18px 18px;
}

.final-totals {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 14px;
  font-weight: 600;
  color: var(--color-text-body, #444);
}

.final-label {
  color: var(--color-primary);
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
  padding: 12px 18px;
  border-bottom: 1px solid var(--color-border);
}

thead th:first-child {
  border-top-left-radius: 10px;
}

thead th:last-child {
  border-top-right-radius: 10px;
}

tbody td, tfoot td {
  padding: 10px 18px;
  border-bottom: 1px solid var(--color-border);
}

tr:nth-child(even) td {
  background: var(--color-table-stripe, rgba(0, 0, 0, 0.015));
}

.table-input {
  width: 100%;
  padding: 8px 10px;
  border: 1px solid var(--color-border);
  border-radius: 10px;
  font-size: 0.92rem;
  color: #000;
  outline: none;
}

.table-input::placeholder {
  color: #000;
  opacity: 1; /* Pour Firefox */
}

.table-input:focus {
  border-color: var(--color-primary, #c00000);
  box-shadow: 0 0 0 3px rgba(var(--color-primary-rgb, 192, 0, 0), 0.1);
}

.table-input:disabled,
.info-textarea:disabled {
  background: var(--color-sidebar-bg, #f4f4f4);
  color: var(--color-text-muted, #777);
  cursor: not-allowed;
}

.action-header {
  text-align: center;
  width: 40px;
  padding-left: 5px;
  padding-right: 10px;
}

.info-cell {
  min-width: 180px;
  padding-right: 5px;
}

.action-cell {
  text-align: center;
  vertical-align: middle;
  width: 40px;
  padding-left: 5px;
  padding-right: 10px;
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
  word-break: break-word;
}

.info-preview.has-content {
  color: var(--color-text-header, #333);
  white-space: pre-wrap;
}

.info-editor {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-textarea {
  width: 100%;
  padding: 8px 10px;
  border: 1px solid var(--color-border);
  border-radius: 10px;
  font-size: 0.9rem;
  font-family: var(--font-secondary, 'Montserrat', sans-serif);
  resize: vertical;
  min-height: 64px;
  color: var(--color-text-header, #333);
}

.info-textarea::placeholder {
  color: var(--color-text-muted, #777);
}

.info-textarea:focus {
  border-color: var(--color-primary, #c00000);
  box-shadow: 0 0 0 3px rgba(var(--color-primary-rgb, 192, 0, 0), 0.1);
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

.btn-add-row {
  background: none;
  border: none;
  color: var(--color-primary, #c00000);
  font-weight: 600;
  cursor: pointer;
  padding: 12px 16px;
  border-radius: 10px;
  font-size: 0.95rem;
  margin-left: 6px;
}

.btn-add-row:hover {
  text-decoration: underline;
}

.error-text {
  color: var(--color-primary);
  font-size: 0.9rem;
  font-weight: 600;
  animation: fadeIn 0.3s ease-in-out;
}

.btn-delete {
  background: var(--color-primary);
  border: 1px solid var(--color-primary);
  color: white;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 1.5rem;
  font-weight: normal;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  line-height: 1;
  padding: 0 0 2px 0;
  transition: all 0.2s;
}

.btn-add[disabled],
.btn-finish[disabled],
.btn-delete[disabled],
.btn-add-row[disabled],
.btn[disabled] {
  opacity: 0.6;
  cursor: not-allowed;
  box-shadow: none;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.btn-add:hover,
.btn-finish:hover {
  background-color: var(--color-primary-dark, #a00000);
}

.step-navigation {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 1rem;
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--color-border);
}

.step-error {
  color: #dc2626;
  font-weight: 600;
  font-size: 0.9rem;
  margin-right: auto;
  animation: fadeIn 0.3s ease;
}

.step-navigation .btn {
  padding: 12px 24px;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary {
  background-color: var(--color-primary, #c00000);
  color: white;
  box-shadow: 0 4px 14px rgba(192, 0, 0, 0.2);
}
.btn-primary:hover {
  background-color: var(--color-primary-dark, #a00000);
  box-shadow: 0 6px 16px rgba(192, 0, 0, 0.25);
}

.btn-secondary {
  background-color: var(--color-card-bg);
  color: var(--color-text-body);
  border: 1px solid var(--color-border);
}
.btn-secondary:hover {
  background-color: var(--color-hover-bg);
}

.totals-row td {
  font-weight: 700;
  background: var(--color-sidebar-bg, #f4f4f9);
}

.totals-label {
  text-transform: uppercase;
  letter-spacing: 0.3px;
  color: var(--color-primary);
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

.btn-danger {
  background-color: #fff;
  color: #dc2626;
  border: 1px solid #dc2626;
  margin-right: 10px;
}
.btn-danger:hover {
  background-color: #fee2e2;
}

.pill-invalid {
  border: 2px solid #ff4d4d !important;
  background-color: #fff5f5 !important;
  animation: shake 0.4s ease-in-out;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-4px); }
  75% { transform: translateX(4px); }
}

.error-hint {
  color: #ff4d4d;
  font-size: 0.75rem;
  font-weight: 600;
  margin-top: 4px;
  animation: fadeIn 0.3s ease;
}

.error-hint-table {
  color: #ff4d4d;
  font-size: 0.7rem;
  font-weight: 700;
  display: block;
  margin-top: 4px;
  white-space: normal;
  line-height: 1.2;
  animation: fadeIn 0.3s ease;
}

.table-section td {
  vertical-align: top;
}
</style>
