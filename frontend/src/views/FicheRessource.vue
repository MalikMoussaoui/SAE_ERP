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
        <button v-if="canDuplicate && editingId" type="button" class="btn btn-secondary" @click="duplicateResource">
          {{ $t('common.duplicate') }}
        </button>
        <button type="button" class="btn btn-secondary" @click="openPdfPreview">
          {{ $t('common.viewPdf') }}
        </button>
        <button type="button" class="btn btn-primary" @click="downloadPdf">
          {{ $t('common.downloadPdf') }}
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
          <h3>{{ $t('resourceSheet.identificationCard') }}</h3>
          <div class="card-fields">
            <label class="field">
              <span>{{ $t('resourceSheet.resourceCode') }}</span>
              <input v-model="form.code" class="pill-select pill-input" :class="{ 'pill-invalid': showValidationErrors && !form.code }" placeholder="R1.XX" :disabled="isReadOnly" />
            </label>
            <label class="field">
              <span>{{ $t('resourceSheet.semester') }}</span>
              <select v-model="form.semestre" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.semestre }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('resourceSheet.selectOption') }}</option>
                <option v-for="option in semestres" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field full">
              <span>{{ $t('resourceSheet.department') }}</span>
              <select v-model="form.departement" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.departement }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('resourceSheet.chooseDepartment') }}</option>
                <option v-for="option in departements" :key="option.id || option" :value="option.label || option">{{ option.label || option }}</option>
              </select>
            </label>
            <label class="field full">
              <span>{{ $t('resourceSheet.ue') }}</span>
              <select v-model="form.ue" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.ue }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('resourceSheet.selectUE') }}</option>
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
                :class="{ 'pill-invalid': showValidationErrors && !form.description }"
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
              <select v-model="form.typeEvaluation" class="pill-select" :class="{ 'pill-invalid': showValidationErrors && !form.typeEvaluation }" :disabled="isReadOnly">
                <option value="" disabled>{{ $t('resourceSheet.selectOption') }}</option>
                <option v-for="option in typesEvaluation" :key="option" :value="option">{{ option }}</option>
              </select>
            </label>
            <label class="field full">
              <span>{{ $t('resourceSheet.evaluationsPlanned') }}</span>
              <div class="choice-group" :class="{ 'pill-invalid': showValidationErrors && form.evaluationsPrevues.length === 0 }">
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
              <div class="choice-group" :class="{ 'pill-invalid': showValidationErrors && !form.compensation }">
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
              <div class="choice-group" :class="{ 'pill-invalid': showValidationErrors && !form.rattrapage }">
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
              <span>{{ $t('resourceSheet.pedagogicalFeedback') }}</span>
              <textarea
                v-model="form.pedagogicalFeedback"
                class="info-textarea"
                rows="3"
                :placeholder="$t('resourceSheet.pedagogicalFeedbackPlaceholder')"
                :disabled="isReadOnly"
              ></textarea>
            </label>
            <label class="field full">
              <span>{{ $t('resourceSheet.teachingType') }}</span>
              <div class="choice-group" :class="{ 'pill-invalid': showValidationErrors && !form.typeEnseignement }">
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
                  <input class="table-input" v-model="row.label" :placeholder="$t('resourceSheet.sequencePlaceholder')" :disabled="isReadOnly" />
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
                          :placeholder="$t('resourceSheet.sequenceDetailsPlaceholder')"
                          :disabled="isReadOnly"
                      ></textarea>
                    <div class="info-actions">
                      <button type="button" class="btn-finish" @click="row.showDetails = false" :disabled="isReadOnly">{{ $t('resourceSheet.finish') }}</button>
                    </div>
                  </div>
                  <div v-else class="info-collapsed">
                    <div class="info-preview" :class="{ 'has-content': row.notes }">{{ row.notes || $t('resourceSheet.noDetails') }}</div>
                    <button type="button" class="btn-add" @click="row.showDetails = true" :disabled="isReadOnly">
                      {{ row.notes ? $t('resourceSheet.editDetails') : $t('resourceSheet.addDetails') }}
                    </button>
                  </div>
                </td>
                <td class="action-cell">
                  <button v-if="sequencesRows.length > 1" @click="deleteRow(row.id)" class="btn-delete" :title="$t('resourceSheet.deleteSequence')" :disabled="isReadOnly">
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
              <span class="final-label">{{ $t('resourceSheet.totalHours') }}</span>
              <span class="single-total">{{ totalSequenceHours }}</span>
            </div>
          </div>
        </div>
      </section>

      <!-- Navigation -->
      <div v-if="!showAllSteps" class="step-navigation">
        <span v-if="stepErrorMessage" class="step-error">{{ stepErrorMessage }}</span>
        <button v-if="currentStep > 1" @click="prevStep" class="btn btn-secondary">
          {{ $t('resourceSheet.back') }}
        </button>
        <button v-if="currentStep < 6" @click="nextStep" class="btn btn-primary">
          {{ $t('resourceSheet.continue') }}
        </button>

        <template v-if="currentStep === 6">
            <button v-if="canDelete && editingId" class="btn btn-danger" type="button" @click="confirmDelete" :disabled="isReadOnly && canDelete !== true">
              {{ $t('common.delete') }}
            </button>
            
            <button v-if="canDuplicate && editingId" class="btn btn-secondary" type="button" @click="duplicateResource">
              {{ $t('common.duplicate') }}
            </button>
            <button v-if="canValidate && !form.isValidated && editingId" class="btn btn-success" type="button" @click="validateResource">
              {{ $t('common.validate') }}
            </button>
            <button v-if="canSubmit && editingId" class="btn btn-primary" style="background:#c97b00;border:none;" type="button" @click="submitResource">
              Soumettre à la direction
            </button>
            
            <button class="btn btn-primary" type="button" @click="saveResource" v-if="hasEditingRights && !isReadOnly">
              {{ $t('resourceSheet.save') }}
            </button>
        </template>
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
        pedagogicalFeedback: '',
        typeEnseignement: '',
        modalitesEvaluation: '',
        description: '',
        isValidated: false,
        isSubmitted: false
      },
      stepErrorMessage: '',
      showValidationErrors: false,
      editingId: null,
      isReadOnly: false,
      departements: [],
      allUes: [],
      semestres: ['S1', 'S2', 'S3', 'S4', 'S5', 'S6'],
      typesEvaluation: ['Soutenance', 'QCM', 'SAé', 'Devoir sur table', 'Rapport de stage'],
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
  async created() {
    await this.fetchDepartmentsAndUes();
    this.loadFromRoute();
  },
  watch: {
    '$route.params.id'() { this.loadFromRoute(); },
    'form.departement'(newDepartement,oldDepartement) {
      if (oldDepartement && !this.isReadOnly) {
        this.form.ue = '';
      }
      this.maybeAutoFillFromMccc();
    },
    'form.semestre'(newSem, oldSem) {
      if (oldSem && !this.isReadOnly) {
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
    availableUes() {
      if (!this.form.departement) return [];
      let ues = this.allUes.filter(u => u.department && u.department.label === this.form.departement);
      if (this.form.semestre) {
        const semNum = parseInt(this.form.semestre.replace('S', ''));
        if (!isNaN(semNum)) {
          ues = ues.filter(u => u.semester === semNum);
        }
      }
      return ues.map(u => u.title);
    },
    showAllSteps() { return this.isReadOnly && this.$route.query.mode === 'view'; },
    totalSequenceHours() { return this.sequencesRows.reduce((acc, row) => acc + (Number(row.duration) || 0), 0); },
    canDuplicate() {
      const role = localStorage.getItem('userRole');
      if (role === 'ADMINISTRATEUR' || role === 'RH') return true;

      if (role === 'RESPONSABLE_PEDAGOGIQUE') {
        const email = localStorage.getItem('userEmail') || '';
        const userName = (localStorage.getItem('userName') || '').toLowerCase();
        const resp = (this.form.responsablePedagogique || '').toLowerCase();
        const isCreator = this.form.createdBy && this.form.createdBy === email;
        const isResponsable = resp && userName && this.isNameMatched(userName, resp);
        return isCreator || isResponsable;
      }

      return false;
    },
    canValidate() {
      const role = localStorage.getItem('userRole');
      if (role === 'ADMINISTRATEUR' || role === 'RH') return true;
      if (role === 'RESPONSABLE_PEDAGOGIQUE') {
        const userName = (localStorage.getItem('userName') || '').toLowerCase();
        const resp = (this.form.responsablePedagogique || '').toLowerCase();
        return this.isNameMatched(userName, resp);
      }
      return false;
    },
    canDelete() {
      const role = localStorage.getItem('userRole');
      if (this.form.isValidated && role !== 'ADMINISTRATEUR') return false;

      const email = localStorage.getItem('userEmail') || '';
      const isCreator = this.editingId && this.form.createdBy && this.form.createdBy === email;
      return role === 'ADMINISTRATEUR' || isCreator;
    },
    hasEditingRights() {
      const role = localStorage.getItem('userRole');
      if (this.form.isValidated && role !== 'ADMINISTRATEUR') return false;

      if (role === 'ADMINISTRATEUR' || role === 'RH') return true;

      // Un utilisateur a le droit de créer une nouvelle fiche par défaut
      // (La gestion d'interdiction de création pour les Vacataires est gérée dans saveResource)
      if (!this.editingId) return true;

      const email = localStorage.getItem('userEmail') || '';
      if (this.editingId && this.form.createdBy && this.form.createdBy === email) return true;

      const userName = (localStorage.getItem('userName') || '').toLowerCase();
      const resp = (this.form.responsablePedagogique || '').toLowerCase();
      const intervenants = (this.form.intervenants || '').toLowerCase();

      const isResp = this.isNameMatched(userName, resp);
      const isInter = this.isNameMatched(userName, intervenants);

      if (role === 'RESPONSABLE_PEDAGOGIQUE') {
        return isResp || isInter;
      }
      if (role === 'TEACHER' || role === 'VACATAIRE') {
        return isInter || isResp;
      }

      return false;
    },
    canSubmit() {
      const email = localStorage.getItem('userEmail') || '';
      return this.editingId && this.form.createdBy && this.form.createdBy === email && !this.form.isSubmitted && !this.form.isValidated;
    }
  },
  methods: {
    async fetchDepartmentsAndUes() {
      try {
        const [deptRes, ueRes] = await Promise.all([
          axios.get('/departments'),
          axios.get('/ues')
        ]);
        this.departements = deptRes.data.filter(d => ueRes.data.some(u => u.department && (u.department.id === d.id || u.department.label === d.label)));
        this.allUes = ueRes.data;
      } catch (error) {
        console.error("Erreur chargement départements/UEs", error);
      }
    },
    isNameMatched(userName, fieldText) {
      if (!userName || !fieldText) return false;
      if (fieldText.includes(userName)) return true;
      const parts = userName.split(/\s+/);
      return parts.some(p => p.length > 2 && fieldText.includes(p));
    },
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
    <div class="field" style="grid-column: 1 / -1;"><div class="label">Retour equipe pedagogique</div><div class="value">${this.form.pedagogicalFeedback || ''}</div></div>
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
    resetForm() {
      this.form = {
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
        pedagogicalFeedback: '',
        typeEnseignement: '',
        modalitesEvaluation: '',
        description: '',
        isValidated: false,
        isSubmitted: false,
        createdBy: ''
      };
      this.sequencesRows = [{ id: 1, label: '', type: 'CM', duration: 0, notes: '', showDetails: false }];
      this.nextRowId = 2;
      this.fieldErrors = {};
      this.lastAutoFillKey = null;
    },
    async loadFromRoute() {
      const id = this.$route.params.id || this.$route.query.id;
      const mode = this.$route.query.mode;
      this.isReadOnly = mode === 'view';
      if (!id) {
        this.editingId = null;
        this.resetForm();
        return;
      }

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
        this.form.isValidated = data.validated || data.isValidated || false;
        this.form.isSubmitted = data.submitted || data.isSubmitted || false;
        this.form.hCM = data.hCM !== undefined ? data.hCM : (data.hoursCm || 0);
        this.form.hTD = data.hTD !== undefined ? data.hTD : (data.hoursTd || 0);
        this.form.hTP = data.hTP !== undefined ? data.hTP : (data.hoursTp || 0);
        this.form.createdBy = data.createdBy || '';

        this.form.typeEvaluation = data.typeEvaluation || '';
        this.form.coefficientRessource = data.coefficientRessource || 0;
        this.form.noteMinimale = data.noteMinimale || 0;
        this.form.compensation = data.compensation || '';
        this.form.rattrapage = data.rattrapage || '';
        this.form.modaliteRattrapage = data.modaliteRattrapage || '';
        this.form.responsablePedagogique = data.responsablePedagogique || '';
        this.form.intervenants = data.intervenants || '';
        this.form.pedagogicalFeedback = data.pedagogicalFeedback || data.retourEquipePedagogique || '';
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

        if (mode !== 'view') {
          this.isReadOnly = !this.hasEditingRights;
          const role = localStorage.getItem('userRole');
          if (this.form.isSubmitted && role !== 'ADMINISTRATEUR' && role !== 'RH' && role !== 'RESPONSABLE_PEDAGOGIQUE') {
             this.isReadOnly = true;
          }
        }

      } catch (e) {
        console.error("Erreur chargement BDD", e);
      }
    },

    async saveResource() {
      const token = localStorage.getItem('user-token');
      // on teste si la personne est bien connecté
      if (!token) {
        alert("Vous devez être connecté pour enregistrer.");
        return;
      }

      const role = localStorage.getItem('userRole');
      if (role === 'VACATAIRE' && !this.editingId) {
        this.errorMessage = "Accès refusé. Les vacataires ne peuvent pas créer de nouvelle fiche.";
        return;
      }
      const authConfig = { headers: { Authorization: `Bearer ${token}` } };

      // On regarde si on est en édition ou en création
      const currentId = this.editingId || this.$route.params.id || this.$route.query.id;
      const isEditing = !!currentId;

      //on assure que les données de chaque champs correspondent entre frontend et backend
      const payload = {
        ...this.form,
        evaluationsPrevues: Array.isArray(this.form.evaluationsPrevues) ? this.form.evaluationsPrevues.join(',') : this.form.evaluationsPrevues,
        sequencesRowsJson: JSON.stringify(this.sequencesRows),
        pedagogicalFeedback: this.form.pedagogicalFeedback,
        retourEquipePedagogique: this.form.pedagogicalFeedback,
        hoursCm: this.form.hCM,
        hoursTd: this.form.hTD,
        hoursTp: this.form.hTP,
        title: this.form.titre,
        departmentName: this.form.departement,
        objectives: this.form.description,
        modalities: this.form.typeEnseignement
      };

      try {
        if (isEditing) { //cas pour edition
          await axios.put(`/resource-sheets/${currentId}`, payload, authConfig);
        } else { // pour la creation
          await axios.post('/resource-sheets', payload, authConfig);
        }

        // message de succès
        this.modal = {
          show: true,
          title: 'Succès',
          message: isEditing ? 'Fiche modifiée avec succès !' : 'Fiche créée avec succès !',
          type: 'success',
          confirmLabel: 'Retour à la liste',
          showCancel: false
        };
        // message d'erreur
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
    async duplicateResource() {
      const token = localStorage.getItem('user-token');
      if (!token) return;
      try {
        const response = await axios.post(`/resource-sheets/${this.editingId}/duplicate`, {}, {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.modal = {
          show: true,
          title: 'Succès',
          message: 'Fiche dupliquée avec succès !',
          type: 'success',
          confirmLabel: 'Retour à la liste',
          showCancel: false
        };
      } catch (e) {
        console.error("Erreur duplication", e);
        this.modal = { show: true, title: 'Erreur', message: "Impossible de dupliquer la fiche.", type: 'error', confirmLabel: 'Fermer', showCancel: false };
      }
    },
    async validateResource() {
      if (!this.editingId) return;
      try {
        await axios.post(`/resource-sheets/${this.editingId}/validate`);
        this.form.isValidated = true;
        this.isReadOnly = !this.hasEditingRights; // This naturally locks out non-admins
        this.modal = {
          show: true,
          title: 'Succès',
          message: 'Fiche validée avec succès ! Elle est désormais verrouillée.',
          type: 'success',
          confirmLabel: 'OK',
          showCancel: false
        };
      } catch (e) {
        console.error("Erreur validation", e);
        this.modal = {
          show: true,
          title: 'Erreur',
          message: "Impossible de valider la fiche.",
          type: 'error',
          confirmLabel: 'Fermer',
          showCancel: false
        };
      }
    },
    async submitResource() {
      if (!this.editingId) return;
      try {
        await axios.post(`/resource-sheets/${this.editingId}/submit`);
        this.form.isSubmitted = true;
        this.isReadOnly = true;
        this.modal = {
          show: true,
          title: 'Succès',
          message: 'Fiche soumise avec succès ! Elle est désormais verrouillée pour édition en attendant la validation.',
          type: 'success',
          confirmLabel: 'OK',
          showCancel: false
        };
      } catch (e) {
        console.error("Erreur soumission", e);
        this.modal = {
          show: true,
          title: 'Erreur',
          message: "Impossible de soumettre la fiche.",
          type: 'error',
          confirmLabel: 'Fermer',
          showCancel: false
        };
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
      
      if (event.key.length > 1 || event.ctrlKey || event.metaKey) {
        return;
      }

      if (['.', ','].includes(event.key)) {
        return;
      }

      if (['-', '+'].includes(event.key)) {
        event.preventDefault();
        this.fieldErrors[key] = "Les nombres négatifs ne sont pas autorisés.";
        setTimeout(() => {
          this.fieldErrors[key] = "";
        }, 4000);
        return;
      }

      if (!/^[0-9]$/.test(event.key)) {
        event.preventDefault();
        this.fieldErrors[key] = "Seuls les chiffres sont acceptés.";
        setTimeout(() => {
          this.fieldErrors[key] = "";
        }, 4000);
      }
    },
    validateStep(step) {
      if (step === 1) return !!(this.form.code && this.form.semestre && this.form.departement && this.form.ue);
      if (step === 2) return !!this.form.description;
      if (step === 3) return !!(this.form.typeEvaluation && this.form.evaluationsPrevues.length > 0);
      if (step === 4) return !!(this.form.compensation && this.form.rattrapage);
      if (step === 5) return !!this.form.typeEnseignement;
      return true;
    },
    nextStep() {
      if (this.validateStep(this.currentStep)) {
        this.stepErrorMessage = '';
        this.showValidationErrors = false;
        if (this.currentStep < 6) this.currentStep++;
      } else {
        this.stepErrorMessage = "Veuillez remplir les champs obligatoires en rouge pour continuer.";
        this.showValidationErrors = true;
      }
    },
    prevStep() {
      this.stepErrorMessage = '';
      this.showValidationErrors = false;
      if (this.currentStep > 1) this.currentStep--;
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
.step-navigation { display: flex; justify-content: flex-end; align-items: center; gap: 1rem; margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--color-border); }
.step-error { color: #dc2626; font-weight: 600; font-size: 0.9rem; margin-right: auto; animation: fadeIn 0.3s ease; }
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
.choice-group.pill-invalid { padding: 6px; border-radius: 12px; }
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
  display: block;
  margin-top: 4px;
  white-space: normal;
  line-height: 1.2;
  animation: fadeIn 0.3s ease;
}

.table-section td {
  vertical-align: top;
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
