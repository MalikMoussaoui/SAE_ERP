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
      <h1 class="page-title">{{ $t('nav.resourceSheets') }}</h1>
    </template>

    <div class="page-surface resource-surface">
      <div v-if="isReadOnly" class="pdf-actions">
        <button type="button" class="btn btn-secondary" @click="openPdfPreview">
          Consulter PDF
        </button>
        <button type="button" class="btn btn-primary" @click="downloadPdf">
          Telecharger PDF
        </button>
      </div>


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
        <div class="step-connector"></div>
        <div class="step" :class="{ active: currentStep >= 4, completed: currentStep > 4 }">
          <div class="step-number">4</div>
          <div class="step-label">{{ $t('resourceSheet.step4_label') }}</div>
        </div>
        <div class="step-connector"></div>
        <div class="step" :class="{ active: currentStep >= 5, completed: currentStep > 5 }">
          <div class="step-number">5</div>
          <div class="step-label">{{ $t('resourceSheet.step5_label') }}</div>
        </div>
        <div class="step-connector"></div>
        <div class="step" :class="{ active: currentStep >= 6, completed: currentStep > 6 }">
          <div class="step-number">6</div>
          <div class="step-label">{{ $t('resourceSheet.step6_label') }}</div>
        </div>
      </div>

      <!-- ÉTAPE 1 : Identification -->
      <section v-if="showAllSteps || currentStep === 1" class="cards-grid top-grid">
        <div class="info-card full-width">
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
              <span>{{ $t('mccc.department') }}</span>
              <select v-model="form.departement" class="pill-select" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.chooseDepartment') }}</option>
                <option v-for="option in departements" :key="option" :value="option">{{ option }}</option>
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
      </section>

      <!-- ÉTAPE 2 : Volume Horaire & Description -->
      <section v-if="showAllSteps || currentStep === 2" class="cards-grid mid-grid">
        <div class="info-card full-width">
          <h3>{{ $t('resourceSheet.hours') }}</h3>
          <div class="card-fields hours-grid">
            <label class="field">
              <span>{{ $t('resourceSheet.hCM') }}</span>
              <input v-model.number="form.hCM" type="number" min="0" step="0.5" @keydown="onlyNumbers($event, 'hCM')" @input="validatePositive(form, 'hCM')" :class="{ 'pill-invalid': fieldErrors['hCM'] }" class="pill-select" />
              <span v-if="fieldErrors['hCM']" class="error-hint">{{ fieldErrors['hCM'] }}</span>
            </label>
            <label class="field">
              <span>{{ $t('resourceSheet.hTD') }}</span>
              <input v-model.number="form.hTD" type="number" min="0" step="0.5" @keydown="onlyNumbers($event, 'hTD')" @input="validatePositive(form, 'hTD')" :class="{ 'pill-invalid': fieldErrors['hTD'] }" class="pill-select" :disabled="isReadOnly" />
              <span v-if="fieldErrors['hTD']" class="error-hint">{{ fieldErrors['hTD'] }}</span>
            </label>
            <label class="field">
              <span>{{ $t('resourceSheet.hTP') }}</span>
              <input v-model.number="form.hTP" type="number" min="0" step="0.5" @keydown="onlyNumbers($event, 'hTP')" @input="validatePositive(form, 'hTP')" :class="{ 'pill-invalid': fieldErrors['hTP'] }" class="pill-select" :disabled="isReadOnly" />
              <span v-if="fieldErrors['hTP']" class="error-hint">{{ fieldErrors['hTP'] }}</span>
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

      <!-- ETAPE 3 : Modalités d'évaluation -->
      <section v-if="showAllSteps || currentStep === 3" class="cards-grid mid-grid">
        <div class="info-card full-width">
          <h3>{{ $t('resourceSheet.step3_label') }}</h3>
          <div class="card-fields single-col">
            <label class="field full">
              <span>{{ $t('resourceSheet.evaluationType') }}</span>
              <select v-model="form.typeEvaluation" class="pill-select" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('mccc.select') }}</option>
                <option v-for="option in typesEvaluation" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field full">
              <span>{{ $t('resourceSheet.evaluationsPlanned') }}</span>
              <div class="choice-group">
                <label class="choice">
                  <input v-model="form.evaluationsPrevues" type="checkbox" value="DS" :disabled="isReadOnly" />
                  <span>{{ $t('resourceSheet.evaluationDs') }}</span>
                </label>
                <label class="choice">
                  <input v-model="form.evaluationsPrevues" type="checkbox" value="TP_NOTE" :disabled="isReadOnly" />
                  <span>{{ $t('resourceSheet.evaluationTp') }}</span>
                </label>
                <label class="choice">
                  <input v-model="form.evaluationsPrevues" type="checkbox" value="PROJET" :disabled="isReadOnly" />
                  <span>{{ $t('resourceSheet.evaluationProject') }}</span>
                </label>
                <label class="choice">
                  <input v-model="form.evaluationsPrevues" type="checkbox" value="ORAL" :disabled="isReadOnly" />
                  <span>{{ $t('resourceSheet.evaluationOral') }}</span>
                </label>
                <label class="choice">
                  <input v-model="form.evaluationsPrevues" type="checkbox" value="RAPPORT" :disabled="isReadOnly" />
                  <span>{{ $t('resourceSheet.evaluationReport') }}</span>
                </label>
              </div>
            </label>
            <label class="field">
              <span>{{ $t('resourceSheet.resourceCoefficient') }}</span>
              <input v-model.number="form.coefficientRessource" type="number" min="0" step="0.1" @keydown="onlyNumbers($event, 'coefficientRessource')" @input="validatePositive(form, 'coefficientRessource')" :class="{ 'pill-invalid': fieldErrors['coefficientRessource'] }" class="pill-select" :disabled="isReadOnly" />
              <span v-if="fieldErrors['coefficientRessource']" class="error-hint">{{ fieldErrors['coefficientRessource'] }}</span>
            </label>
          </div>
        </div>
      </section>

      <!-- ?%TAPE 4 : R??gles de validation -->
      <section v-if="showAllSteps || currentStep === 4" class="cards-grid mid-grid">
        <div class="info-card full-width">
          <h3>{{ $t('resourceSheet.step4_label') }}</h3>
          <div class="card-fields single-col">
            <label class="field">
              <span>{{ $t('resourceSheet.validationMinScore') }}</span>
              <input v-model.number="form.noteMinimale" type="number" min="0" step="0.1" @keydown="onlyNumbers($event, 'noteMinimale')" @input="validatePositive(form, 'noteMinimale')" :class="{ 'pill-invalid': fieldErrors['noteMinimale'] }" class="pill-select" :disabled="isReadOnly" />
              <span v-if="fieldErrors['noteMinimale']" class="error-hint">{{ fieldErrors['noteMinimale'] }}</span>
            </label>
            <label class="field full">
              <span>{{ $t('resourceSheet.validationCompensation') }}</span>
              <div class="choice-group">
                <label class="choice">
                  <input v-model="form.compensation" type="radio" value="OUI" :disabled="isReadOnly" />
                  <span>{{ $t('common.yes') }}</span>
                </label>
                <label class="choice">
                  <input v-model="form.compensation" type="radio" value="NON" :disabled="isReadOnly" />
                  <span>{{ $t('common.no') }}</span>
                </label>
              </div>
            </label>
            <label class="field full">
              <span>{{ $t('resourceSheet.validationRetake') }}</span>
              <div class="choice-group">
                <label class="choice">
                  <input v-model="form.rattrapage" type="radio" value="OUI" :disabled="isReadOnly" />
                  <span>{{ $t('common.yes') }}</span>
                </label>
                <label class="choice">
                  <input v-model="form.rattrapage" type="radio" value="NON" :disabled="isReadOnly" />
                  <span>{{ $t('common.no') }}</span>
                </label>
              </div>
            </label>
            <label class="field full">
              <span>{{ $t('resourceSheet.validationRetakeMode') }}</span>
              <input v-model="form.modaliteRattrapage" class="pill-select pill-input" :placeholder="$t('resourceSheet.validationRetakePlaceholder')" :disabled="isReadOnly" />
            </label>
          </div>
        </div>
      </section>

      <!-- ETAPE 5 : Organisation pédagogique -->
      <section v-if="showAllSteps || currentStep === 5" class="cards-grid mid-grid">
        <div class="info-card full-width">
          <h3>{{ $t('resourceSheet.step5_label') }}</h3>
          <div class="card-fields single-col">
            <label class="field">
              <span>{{ $t('resourceSheet.pedagogicalManager') }}</span>
              <input v-model="form.responsablePedagogique" class="pill-select pill-input" :disabled="isReadOnly" />
            </label>
            <label class="field">
              <span>{{ $t('resourceSheet.intervenants') }}</span>
              <input v-model="form.intervenants" class="pill-select pill-input" :disabled="isReadOnly" />
            </label>
            <label class="field full">
              <span>{{ $t('resourceSheet.teachingType') }}</span>
              <div class="choice-group">
                <label class="choice">
                  <input v-model="form.typeEnseignement" type="radio" value="PRESENTIEL" :disabled="isReadOnly" />
                  <span>{{ $t('resourceSheet.teachingTypeInPerson') }}</span>
                </label>
                <label class="choice">
                  <input v-model="form.typeEnseignement" type="radio" value="DISTANCIEL" :disabled="isReadOnly" />
                  <span>{{ $t('resourceSheet.teachingTypeRemote') }}</span>
                </label>
                <label class="choice">
                  <input v-model="form.typeEnseignement" type="radio" value="HYBRIDE" :disabled="isReadOnly" />
                  <span>{{ $t('resourceSheet.teachingTypeHybrid') }}</span>
                </label>
              </div>
            </label>
          </div>
        </div>
      </section>

      <!-- ETAPE 6 : Tableau des Séquences -->
      <section v-if="showAllSteps || currentStep === 6" class="table-section">
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
                  <input class="table-input" v-model.number="row.duration" type="number" min="0" step="0.5" @keydown="onlyNumbers($event, 'duration', row.id)" @input="validatePositive(row, 'duration', true)" :class="{ 'pill-invalid': fieldErrors['duration_' + row.id] }" />
                  <span v-if="fieldErrors['duration_' + row.id]" class="error-hint-table">
                      {{ fieldErrors['duration_' + row.id] }}
                    </span>
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
        <button v-if="currentStep < 6" @click="nextStep" class="btn btn-primary">
          {{ $t('mccc.continue') }}
        </button>
        <button v-if="currentStep === 6 && editingId" class="btn btn-danger" type="button" @click="confirmDelete" :disabled="isReadOnly">
          Supprimer
        </button>
        <button v-if="currentStep === 6" class="btn btn-primary" type="button" @click="saveResource" :disabled="isReadOnly">
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
import logoIut from '@/assets/logo-iut.jpg';

export default {
  name: 'FicheRessourceView',
  components: { DashboardLayout, CustomModal },
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
        typeEvaluation: '',
        evaluationsPrevues: [],
        coefficientRessource: 0,
        noteMinimale: 0,
        compensation: '',
        rattrapage: '',
        modaliteRattrapage: '',
        responsablePedagogique: '',
        intervenants: '',
        typeEnseignement: '',
        modalitesEvaluation: '',
        description: ''
      },
      editingId: null,
      isReadOnly: false,
      departements: [
        'BUT Informatique', 'BUT GEA - Gestion des Entreprises et des Administrations',
        'BUT TC - Techniques de Commercialisation', 'BUT Mesures Physiques (MP)',
        'BUT Genie Mecanique et Productique (GMP)', 'BUT GEII - Genie Electrique et Informatique Industrielle',
        'BUT Genie Civil - Construction Durable', 'BUT Genie Biologique (GB)',
        "BUT MMI - Metiers du Multimedia et de l'Internet", 'BUT GIM - Genie Industriel et Maintenance',
        'BUT HSE - Hygiene, Securite, Environnement', 'BUT Carrieres Sociales'
      ],
      semestres: ['S1', 'S2', 'S3', 'S4', 'S5', 'S6'],
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
        "BUT MMI - Metiers du Multimedia et de l'Internet": [
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
      sequencesRows: [
        { id: 1, label: '', type: 'CM', duration: 0, notes: '', showDetails: false }
      ],
      nextRowId: 2,
      errorMessage: '',
      errorTimeout: null,
      mcccEntries: null,
      lastAutoFillKey: null,
      isAutoFillLoading: false,
      fieldErrors: {},
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
    'form.departement'(newDepartement,oldDepartement) {
      this.availableUes = this.uesByDepartement[newDepartement] || [];
      if (oldDepartement && !this.isReadOnly) {
        this.form.ue = '';
      }
      this.maybeAutoFillFromMccc();
    },
    '$route.query.id'() { this.loadFromRoute(); },
    '$route.query.mode'() { this.isReadOnly = this.$route.query.mode === 'view'; },
    'form.code'() { this.maybeAutoFillFromMccc(); },
    'form.semestre'() { this.maybeAutoFillFromMccc(); },
    'form.ue'() { this.maybeAutoFillFromMccc(); }
  },
  computed: {
    showAllSteps() { return this.isReadOnly && this.$route.query.mode === 'view'; },
    totalSequenceHours() { return this.sequencesRows.reduce((acc, row) => acc + (Number(row.duration) || 0), 0); }
  },
  methods: {
    formatEvaluationType(value) { return value ? String(value) : ''; },
    formatEvaluationsPrevues(values) {
      if (!Array.isArray(values) || !values.length) return '';
      const labels = { DS: 'DS', TP_NOTE: 'TP note', PROJET: 'Projet', ORAL: 'Oral', RAPPORT: 'Rapport' };
      return values.map(value => labels[value] || value).join(', ');
    },
    formatTeachingType(value) {
      const labels = { PRESENTIEL: 'Presentiel', DISTANCIEL: 'Distanciel', HYBRIDE: 'Hybride' };
      return labels[value] || value || '';
    },
    openPdfPreview() { this.openPdfWindow(false); },
    downloadPdf() { this.openPdfWindow(true); },
    openPdfWindow(autoPrint) {
      const title = this.form.titre || 'Fiche ressource';
      const totalStudentHours = this.toNumber(this.form.hCM) + this.toNumber(this.form.hTD) + this.toNumber(this.form.hTP);
      const totalSequenceHours = this.sequencesRows.reduce((acc, row) => acc + (this.toNumber(row.duration) || 0), 0);
      const byType = this.sequencesRows.reduce((acc, row) => {
        const key = (row.type || 'Autre').toUpperCase();
        acc[key] = (acc[key] || 0) + (this.toNumber(row.duration) || 0);
        return acc;
      }, {});
      const evals = this.formatEvaluationsPrevues(this.form.evaluationsPrevues);
      const today = new Date().toLocaleDateString('fr-FR');
      const content = `<html><head><meta charset="utf-8" /><title>${title}</title>
<style>
:root { --accent: #c00000; --accent-soft: #f9e7e7; --border: #cfcfcf; --muted: #666; --text: #111; }
* { box-sizing: border-box; }
body { font-family: Arial, Helvetica, sans-serif; color: var(--text); margin: 0; background: #fff; }
.page { position: relative; padding: 18mm 16mm 18mm; min-height: 270mm; }
.page + .page { page-break-before: always; }
.doc-header { display: grid; grid-template-columns: 110px 1fr 1fr 220px; gap: 0; border: 1px solid var(--border); align-items: stretch; }
.doc-header .cell { border-right: 1px solid var(--border); padding: 8px 10px; font-size: 11px; min-height: 60px; display: flex; align-items: center; }
.doc-header .cell:last-child { border-right: 0; }
.logo-box { justify-content: center; font-weight: 700; font-size: 18px; color: var(--accent); }
.logo-box img { max-height: 48px; max-width: 90px; object-fit: contain; display: block; }
.dept-box { text-align: center; font-weight: 700; font-size: 12px; justify-content: center; background: #fff5f5; }
.title-box { text-align: center; font-weight: 700; font-size: 12px; justify-content: center; }
.meta-box { font-size: 10px; line-height: 1.4; display: flex; flex-direction: column; align-items: flex-start; justify-content: center; gap: 2px; }
.meta-box strong { color: var(--accent); }
.section-title { background: var(--accent-soft); color: var(--accent); border: 1px solid var(--border); padding: 8px 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.4px; font-size: 13px; margin: 20px 0 12px; }
.grid-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 14px 20px; }
.grid-3 { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 14px 20px; }
.field { border: 1px solid var(--border); padding: 12px 14px; font-size: 12px; min-height: 46px; }
.field .label { font-size: 11px; color: var(--muted); text-transform: uppercase; letter-spacing: 0.3px; margin-bottom: 6px; }
.field .value { font-size: 13px; white-space: pre-wrap; }
.table { width: 100%; border-collapse: collapse; font-size: 12px; border: 1px solid var(--border); }
.table th, .table td { border: 1px solid var(--border); padding: 10px 12px; vertical-align: top; }
.table th { background: var(--accent-soft); color: var(--accent); text-transform: uppercase; font-size: 11px; letter-spacing: 0.4px; text-align: center; }
.table td:nth-child(2), .table th:nth-child(2) { text-align: center; width: 80px; }
.table td:nth-child(3), .table th:nth-child(3) { text-align: center; width: 90px; }
.table td:nth-child(4), .table th:nth-child(4) { width: 38%; }
.table tbody tr:nth-child(even) { background: #fff7f7; }
.note { font-size: 10px; color: var(--muted); margin-top: 6px; }
.footer { position: absolute; bottom: 10mm; left: 16mm; right: 16mm; display: flex; justify-content: space-between; font-size: 10px; color: var(--muted); border-top: 1px solid var(--border); padding-top: 4px; }
</style></head><body>
<div class="page">
  <div class="doc-header">
    <div class="cell logo-box"><img src="${logoIut}" alt="IUT" /></div>
    <div class="cell dept-box">${this.form.departement || ''}</div>
    <div class="cell title-box">FICHE RESSOURCE</div>
    <div class="cell meta-box">
      <div><strong>Reference:</strong> IU EN FOR 001</div>
      <div>Date de creation: ${today}</div>
      <div>Date de modification: ${today}</div>
      <div>Indice de revision: 001</div>
    </div>
  </div>

  <div class="section-title">Identification</div>
  <div class="grid-2">
    <div class="field"><div class="label">Nom formation</div><div class="value">${this.form.departement || ''}</div></div>
    <div class="field"><div class="label">UE</div><div class="value">${this.form.ue || ''}</div></div>
    <div class="field"><div class="label">Code ressource</div><div class="value">${this.form.code || ''}</div></div>
    <div class="field"><div class="label">Semestre</div><div class="value">${this.form.semestre || ''}</div></div>
    <div class="field"><div class="label">Titre</div><div class="value">${title}</div></div>
    <div class="field"><div class="label">Volume etudiant</div><div class="value">${totalStudentHours || 0} h</div></div>
  </div>

  <div class="section-title">Descriptif</div>
  <div class="field"><div class="label">Objectif de la ressource</div><div class="value">${this.form.description || ''}</div></div>

  <div class="section-title">Evaluation</div>
  <div class="grid-3">
    <div class="field"><div class="label">Type d'evaluation</div><div class="value">${this.formatEvaluationType(this.form.typeEvaluation)}</div></div>
    <div class="field"><div class="label">Evaluations prevues</div><div class="value">${evals || ''}</div></div>
    <div class="field"><div class="label">Coefficient ressource</div><div class="value">${this.form.coefficientRessource || 0}</div></div>
  </div>

  <div class="section-title">Regles de validation</div>
  <div class="grid-3">
    <div class="field"><div class="label">Note minimale</div><div class="value">${this.form.noteMinimale || 0}</div></div>
    <div class="field"><div class="label">Compensation</div><div class="value">${this.form.compensation || ''}</div></div>
    <div class="field"><div class="label">Rattrapage</div><div class="value">${this.form.rattrapage || ''}</div></div>
  </div>
  <div class="field" style="margin-top:10px;"><div class="label">Modalite de rattrapage</div><div class="value">${this.form.modaliteRattrapage || ''}</div></div>

  <div class="footer"><span>IU EN FOR 001</span><span>NOM UE / FICHE RESSOURCE</span><span>Page 1 sur 3</span></div>
</div>

<div class="page">
  <div class="doc-header">
    <div class="cell logo-box"><img src="${logoIut}" alt="IUT" /></div>
    <div class="cell dept-box">${this.form.departement || ''}</div>
    <div class="cell title-box">FICHE RESSOURCE</div>
    <div class="cell meta-box">
      <div><strong>Reference:</strong> IU EN FOR 001</div>
      <div>Date de creation: ${today}</div>
      <div>Date de modification: ${today}</div>
      <div>Indice de revision: 001</div>
    </div>
  </div>

  <div class="section-title">Contenu pedagogique</div>
  <table class="table">
    <thead>
      <tr>
        <th>Sequence</th>
        <th>Type</th>
        <th>Duree (h)</th>
        <th>Details</th>
      </tr>
    </thead>
    <tbody>
      ${this.sequencesRows.map(row => (
          '<tr><td>' + (row.label || '') + '</td><td>' + (row.type || '') + '</td><td>' + (this.toNumber(row.duration) || 0) + '</td><td>' + (row.notes || '') + '</td></tr>'
      )).join('')}
    </tbody>
  </table>

  <div class="footer"><span>IU EN FOR 001</span><span>NOM UE / FICHE RESSOURCE</span><span>Page 2 sur 3</span></div>
</div>

<div class="page">
  <div class="doc-header">
    <div class="cell logo-box"><img src="${logoIut}" alt="IUT" /></div>
    <div class="cell dept-box">${this.form.departement || ''}</div>
    <div class="cell title-box">FICHE RESSOURCE</div>
    <div class="cell meta-box">
      <div><strong>Reference:</strong> IU EN FOR 001</div>
      <div>Date de creation: ${today}</div>
      <div>Date de modification: ${today}</div>
      <div>Indice de revision: 001</div>
    </div>
  </div>

  <div class="section-title">Organisation pedagogique</div>
  <div class="grid-2">
    <div class="field"><div class="label">Responsable pedagogique</div><div class="value">${this.form.responsablePedagogique || ''}</div></div>
    <div class="field"><div class="label">Intervenants</div><div class="value">${this.form.intervenants || ''}</div></div>
    <div class="field"><div class="label">Type d'enseignement</div><div class="value">${this.formatTeachingType(this.form.typeEnseignement)}</div></div>
    <div class="field"><div class="label">Total sequences</div><div class="value">${totalSequenceHours || 0} h</div></div>
  </div>

  <div class="section-title">Repartition / Ressources</div>
  <div class="field"><div class="label">Totaux</div><div class="value">CM ${byType.CM || 0} h, TD ${byType.TD || 0} h, TP ${byType.TP || 0} h, Autre ${byType.AUTRE || 0} h</div></div>

  <div class="footer"><span>IU EN FOR 001</span><span>NOM UE / FICHE RESSOURCE</span><span>Page 3 sur 3</span></div>
</div>
</body></html>`;
      const pdfWindow = window.open('', '_blank');
      if (!pdfWindow) return;
      pdfWindow.document.open();
      pdfWindow.document.write(content);
      pdfWindow.document.close();
      if (autoPrint) { pdfWindow.onload = () => { pdfWindow.focus(); pdfWindow.print(); }; }
    },
    normalizeValue(value) { return (value === null || value === undefined) ? '' : String(value).trim().toLowerCase(); },
    normalizeYesNo(value) {
      if (value === null || value === undefined) return '';
      const n = String(value).trim().toLowerCase();
      if (!n) return '';
      if (['oui','o','yes','y','true','1'].includes(n)) return 'OUI';
      if (['non','n','no','false','0'].includes(n)) return 'NON';
      return String(value).trim();
    },
    mapMcccModaliteToEvaluation(modalite) {
      const n = this.normalizeValue(modalite);
      if (!n) return '';
      if (n.includes('mixte') || n.includes('cci') || n.includes('final')) return 'CC_EXAMEN';
      if (n.includes('terminal')) return 'EXAMEN';
      if (n.includes('continu') || n.includes('projet')) return 'CC';
      return '';
    },
    toNumber(value) {
      if (value === null || value === undefined) return 0;
      const n = String(value).replace(',', '.');
      const p = Number(n);
      return Number.isFinite(p) ? p : 0;
    },
    async fetchMcccEntries() {
      if (this.mcccEntries) return this.mcccEntries;
      const response = await axios.get('/mccc');
      this.mcccEntries = response.data.map((entry) => {
        const f = entry.form || {};
        return {
          ...entry,
          departement: entry.departement || entry.department || f.departement || '',
          ue: entry.ue || f.ue || '',
          semestre: entry.semestre || entry.semester || f.semestre || '',
          code: f.code || f.ressource || '',
          savedAt: entry.savedAt || entry.saved_at || f.savedAt || ''
        };
      });
      return this.mcccEntries;
    },
    buildAutoFillKey() {
      return [this.normalizeValue(this.form.departement), this.normalizeValue(this.form.code), this.normalizeValue(this.form.semestre), this.normalizeValue(this.form.ue)].join('|');
    },
    isDefaultSequences() {
      return this.sequencesRows.length === 1 && !this.sequencesRows[0].label && this.toNumber(this.sequencesRows[0].duration) === 0 && !this.sequencesRows[0].notes;
    },
    async maybeAutoFillFromMccc() {
      if (this.isReadOnly || this.isAutoFillLoading || this.currentStep !== 1) return;
      if (!this.form.departement || !this.form.code || !this.form.semestre || !this.form.ue) return;
      const key = this.buildAutoFillKey();
      if (this.lastAutoFillKey === key) return;
      this.isAutoFillLoading = true;
      try {
        const entries = await this.fetchMcccEntries();
        const nCode = this.normalizeValue(this.form.code);
        const nDep = this.normalizeValue(this.form.departement);
        const nSem = this.normalizeValue(this.form.semestre);
        const nUe = this.normalizeValue(this.form.ue);
        const matches = entries.filter((e) => {
          if (this.normalizeValue(e.departement) !== nDep) return false;
          if (this.normalizeValue(e.semestre) !== nSem) return false;
          if (this.normalizeValue(e.ue) !== nUe) return false;
          const eCode = this.normalizeValue(e.code);
          return eCode ? eCode === nCode : this.normalizeValue(e.form?.ressource) === nCode;
        });
        if (!matches.length) { this.lastAutoFillKey = key; return; }
        const sorted = matches.map(e => ({ e, savedAt: e.savedAt ? new Date(e.savedAt).getTime() : 0 })).sort((a, b) => b.savedAt - a.savedAt);
        this.applyMcccToResource(sorted[0].e);
        this.lastAutoFillKey = key;
      } catch (e) { console.error('Error MCCC autofill', e); } finally { this.isAutoFillLoading = false; }
    },
    applyMcccToResource(mccc) {
      const f = mccc.form || {};
      const rows = Array.isArray(mccc.ressourcesRows) ? mccc.ressourcesRows : [];
      if (!this.form.titre) this.form.titre = f.ressource || f.titre || mccc.ue || '';
      if (!this.form.departement) this.form.departement = mccc.departement || '';
      if (!this.form.semestre) this.form.semestre = mccc.semestre || '';
      if (!this.form.ue) this.form.ue = mccc.ue || '';
      if (!this.form.responsablePedagogique) {
        const r = f.responsable || f.responsablePedagogique || mccc.responsable || mccc.responsablePedagogique || mccc.pedagogicalManager;
        if (r) this.form.responsablePedagogique = r;
      }
      if (!this.form.typeEvaluation) {
        const rt = String(f.typeEvaluation || '').trim();
        if (rt) this.form.typeEvaluation = rt;
        else { const mt = this.mapMcccModaliteToEvaluation(f.modalite); if (mt) this.form.typeEvaluation = mt; }
      }
      if (this.toNumber(this.form.coefficientRessource) === 0) { const c = this.toNumber(f.coeffRessource); if (c > 0) this.form.coefficientRessource = c; }
      if (!this.form.compensation) {
        const v = f.compensation ?? mccc.compensation;
        const mapped = this.normalizeYesNo(v);
        if (mapped) this.form.compensation = mapped;
      }
      if (!this.form.rattrapage) {
        const v = f.rattrapage ?? mccc.rattrapage;
        const mapped = this.normalizeYesNo(v);
        if (mapped) this.form.rattrapage = mapped;
      }
      if (this.toNumber(this.form.hCM) === 0) this.form.hCM = rows.reduce((acc, r) => acc + this.toNumber(r.hCM), 0);
      if (this.toNumber(this.form.hTD) === 0) this.form.hTD = rows.reduce((acc, r) => acc + this.toNumber(r.hTD), 0);
      if (this.toNumber(this.form.hTP) === 0) this.form.hTP = rows.reduce((acc, r) => acc + this.toNumber(r.hTP), 0);
      if (!this.form.description) { const n = rows.map(r => r.notes).filter(Boolean).join('\n'); if (n) this.form.description = n; }
      if (this.isDefaultSequences() && rows.length) {
        this.sequencesRows = rows.map((r, i) => {
          const hcm = this.toNumber(r.hCM), htd = this.toNumber(r.hTD), htp = this.toNumber(r.hTP);
          let t = 'Autre';
          if (hcm > 0 && htd === 0 && htp === 0) t = 'CM';
          if (htd > 0 && hcm === 0 && htp === 0) t = 'TD';
          if (htp > 0 && hcm === 0 && htd === 0) t = 'TP';
          return { id: i + 1, label: r.label || `Ressource ${i + 1}`, type: t, duration: hcm + htd + htp + this.toNumber(r.hDSCM) + this.toNumber(r.hDSTP), notes: r.notes || '', showDetails: false };
        });
        this.nextRowId = this.sequencesRows.length + 1;
      }
    },
    async loadFromRoute() {
      const { id, mode } = this.$route.query;
      this.isReadOnly = mode === 'view';
      if (!id) { this.editingId = null; return; }

      try {
        const response = await axios.get(`/resource-sheets/${id}`);
        const data = response.data;
        console.log("🔥 DONNÉES REÇUES :", data);

        this.editingId = data.id;
        this.form.titre = data.titre || data.title || '';
        this.form.departement = data.departement || data.departmentName || '';
        this.form.code = data.code || '';
        this.form.semestre = data.semestre || '';
        this.form.ue = data.ue || '';
        this.form.description = data.description || data.objectives || '';
        this.form.hCM = data.hCM !== undefined ? data.hCM : (data.hoursCm || 0);
        this.form.hTD = data.hTD !== undefined ? data.hTD : (data.hoursTd || 0);
        this.form.hTP = data.hTP !== undefined ? data.hTP : (data.hoursTp || 0);

        this.form.typeEvaluation = data.typeEvaluation || '';
        this.form.coefficientRessource = data.coefficientRessource || 0;
        this.form.noteMinimale = data.noteMinimale || 0;
        this.form.compensation = data.compensation || '';
        this.form.rattrapage = data.rattrapage || '';
        this.form.modaliteRattrapage = data.modaliteRattrapage || '';
        this.form.responsablePedagogique = data.responsablePedagogique || '';
        this.form.intervenants = data.intervenants || '';
        this.form.typeEnseignement = data.typeEnseignement || data.modalities || '';
        const ev = data.evaluationsPrevues;
        this.form.evaluationsPrevues = Array.isArray(ev) ? ev : (typeof ev === 'string' ? ev.split(',') : []);
        let rows = [];
        const rawSeq = data.sequencesRowsJson || data.sequencesRows;
        if (typeof rawSeq === 'string') {
          try { rows = JSON.parse(rawSeq); } catch (e) { console.error("Err parse JSON", e); }
        } else if (Array.isArray(rawSeq)) {
          rows = rawSeq;
        }

        if (rows.length > 0) {
          this.sequencesRows = rows.map(r => ({ ...r, showDetails: false }));
          this.nextRowId = Math.max(...this.sequencesRows.map(r => Number(r.id) || 0)) + 1;
        } else {
          this.sequencesRows = [{ id: 1, label: '', type: 'CM', duration: 0, notes: '', showDetails: false }];
          this.nextRowId = 2;
        }

        this.availableUes = this.uesByDepartement[this.form.departement] || [];
      } catch (e) {
        console.error("Erreur chargement BDD", e);
      }
    },

    async saveResource() {
      const token = localStorage.getItem('user-token');
      if (!token) {
        alert("Vous devez etre connecte pour enregistrer.");
        return;
      }
      const authConfig = { headers: { Authorization: `Bearer ${token}` } };

      const payload = {
        ...this.form,
        evaluationsPrevues: Array.isArray(this.form.evaluationsPrevues) ? this.form.evaluationsPrevues.join(',') : this.form.evaluationsPrevues,
        sequencesRowsJson: JSON.stringify(this.sequencesRows),
        hoursCm: this.form.hCM,
        hoursTd: this.form.hTD,
        hoursTp: this.form.hTP,
        title: this.form.titre,
        departmentName: this.form.departement,
        objectives: this.form.description,
        modalities: this.form.typeEnseignement
      };

      try {
        const id = this.$route.query.id;
        const mode = this.$route.query.mode;

        if (mode === 'edit' && id) {
          await axios.put(`/resource-sheets/${id}`, payload, authConfig);
        } else {
          await axios.post('/resource-sheets', payload, authConfig);
        }
        this.modal = {
          show: true,
          title: 'Succes',
          message: mode === 'edit' ? 'Fiche modifiee avec succes !' : 'Fiche creee avec succes !',
          type: 'success',
          confirmLabel: 'Retour a la liste',
          showCancel: false
        };
      } catch (e) {
        console.error("Erreur sauvegarde", e);
        this.modal = {
          show: true,
          title: 'Erreur',
          message: "Erreur technique lors de l'enregistrement.",
          type: 'error',
          confirmLabel: 'Fermer',
          showCancel: false
        };
      }
    },
    confirmDelete() {
      this.modal = {
        show: true,
        title: 'Supprimer la fiche ?',
        message: 'Cette action est irréversible. Voulez-vous vraiment supprimer cette fiche ressource ?',
        type: 'warning',
        showCancel: true,
        confirmLabel: 'Supprimer',
        cancelLabel: 'Annuler',
        action: 'delete'
      };
    },
    async deleteResource() {
      try {
        await axios.delete(`/resource-sheets/${this.editingId}`);
        this.$router.push({ name: 'liste-fiches-ressources' });
      } catch (e) {
        console.error(e);
        this.modal = { show: true, title: 'Erreur', message: "Impossible de supprimer.", type: 'error', confirmLabel: 'Fermer', showCancel: false };
      }
    },
    handleModalConfirm() {
      this.modal.show = false;
      if (this.modal.action === 'delete') {
        this.deleteResource();
      } else if (this.modal.type === 'success') {
        this.$router.push({ name: 'liste-fiches-ressources' });
      }
    },

    addRow() { this.sequencesRows.push({ id: this.nextRowId++, label: '', type: 'CM', duration: 0, notes: '', showDetails: false }); },
    deleteRow(id) { if (this.sequencesRows.length > 1) this.sequencesRows = this.sequencesRows.filter(r => r.id !== id); },
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
      if (['-', '+', 'e', 'E'].includes(event.key)) {
        event.preventDefault();
        this.fieldErrors[key] = "Seuls les chiffres positifs sont acceptés.";
        setTimeout(() => {
          this.fieldErrors[key] = "";
        }, 4000);
      }
    },
    nextStep() { if (this.currentStep < 6) this.currentStep++; },
    prevStep() { if (this.currentStep > 1) this.currentStep--; }
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
.pdf-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
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
.choice-group { display: flex; flex-wrap: wrap; gap: 10px; }
.choice { display: inline-flex; align-items: center; gap: 8px; padding: 6px 10px; border: 1px solid var(--color-border); border-radius: 10px; background: var(--color-card-bg); }
.choice input { margin: 0; }

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

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-5px); }
  to { opacity: 1; transform: translateY(0); }
}

.error-hint-table {
  color: #ff4d4d;
  font-size: 0.7rem;
  font-weight: 700;
  position: absolute;
  bottom: 2px;
  left: 18px;
  white-space: nowrap;
  animation: fadeIn 0.3s ease;
}

.table-section td {
  position: relative;
  vertical-align: top;
  padding-bottom: 20px;
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
</style>
