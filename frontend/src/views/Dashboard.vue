<template>
  <DashboardLayout>
    <template #header>
      <div class="header-welcome">
        <h2>{{ $t('dashboard.greeting') }} {{ userName }}</h2>
        <h1>{{ $t('dashboard.title') }}</h1>
      </div>
    </template>

    <div class="page-surface">
      <section class="content-cards">
        <div class="card">
          <h3>{{ $t('dashboard.fillSheet') }}</h3>

          <template v-if="latestSheet">
            <p>{{ latestSheet.titre || 'Sans titre' }}</p>
            <p class="subtitle">{{ latestSheet.code }} — {{ latestSheet.semestre }}</p>
            <button @click="goToSheet(latestSheet.id)" class="btn-primary">
              {{ $t('dashboard.complete') }}
            </button>
          </template>

          <template v-else>
            <p>Aucune fiche trouvée</p>
            <p class="subtitle">Commencez par créer une ressource</p>
            <button @click="$router.push('/fiche-ressource')" class="btn-primary">
              Créer une fiche
            </button>
          </template>
        </div>

        <div class="card card-notifications">
          <div class="icon-success">
            <span>✔</span>
          </div>
          <h3>{{ $t('dashboard.notifications') }}</h3>
          <p>{{ $t('dashboard.noNotifications') }}</p>
        </div>
      </section>

      <h2 class="analytics-title">Analyse du département</h2>
      <section class="content-cards analytics-grid">
        <div class="card chart-card">
          <h3>Répartition des heures</h3>
          <div class="chart-wrapper">
            <Pie v-if="loaded" :data="chartDataHeures" :options="chartOptions" />
            <div v-else class="loading-text">Récupération des données...</div>
          </div>
        </div>

        <div class="card chart-card">
          <h3>Effectifs par rôle</h3>
          <div class="chart-wrapper">
            <Bar v-if="loaded" :data="chartDataRoles" :options="chartOptions" />
            <div v-else class="loading-text">Récupération des données...</div>
          </div>
        </div>
      </section>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';
import axios from 'axios';
import { Pie, Bar } from 'vue-chartjs';
import {
  Chart as ChartJS, Title, Tooltip, Legend, ArcElement,
  CategoryScale, LinearScale, BarElement
} from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, ArcElement, CategoryScale, LinearScale, BarElement);

export default {
  name: 'DashboardView',
  components: {
    DashboardLayout,
    Pie,
    Bar
  },
  data() {
    return {
      userName: '',
      loaded: false,
      chartDataHeures: null,
      chartDataRoles: null,
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
            labels: { color: '#888', font: { family: 'Poppins', size: 12 } }
          }
        }
      },
      latestSheet: null
    };
  },
  async mounted() {
    this.userName = localStorage.getItem('userName') || 'Utilisateur';
    await this.fetchStats();
  },
  methods: {
    async fetchStats() {
      try {
        const response = await axios.get('http://localhost:8080/api/dashboard/stats');
        const stats = response.data;

        this.latestSheet = stats.latestSheet;

        this.chartDataHeures = {
          labels: ['CM', 'TD', 'TP'],
          datasets: [{
            backgroundColor: ['#C00000', '#555555', '#333333'],
            borderWidth: 0,
            data: [stats.hoursByType.CM, stats.hoursByType.TD, stats.hoursByType.TP]
          }]
        };

        this.chartDataRoles = {
          labels: Object.keys(stats.usersByRole),
          datasets: [{
            label: 'Utilisateurs',
            backgroundColor: '#C00000',
            borderRadius: 4,
            data: Object.values(stats.usersByRole)
          }]
        };
        this.loaded = true;
      } catch (error) {
        this.chartDataHeures = {
          labels: ['CM', 'TD', 'TP'],
          datasets: [{ backgroundColor: ['#C00000', '#555', '#222'], data: [15, 30, 25] }]
        };
        this.chartDataRoles = {
          labels: ['Admin', 'Enseignant', 'RH'],
          datasets: [{ label: 'Effectifs', backgroundColor: '#C00000', data: [1, 8, 2] }]
        };
        this.loaded = true;
      }
    },
    goToSheet(id) {
      this.$router.push(`/fiche-ressource/${id}`);
    }
  }
}
</script>

<style scoped>
.header-welcome h1 {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 2rem;
  font-weight: 700;
  color: var(--color-text-header, #333);
  margin: 0.25rem 0 0 0;
}
.header-welcome h2 {
  font-size: 1.25rem;
  font-weight: 500;
  color: var(--color-text-body, #555);
  margin: 0;
}
.page-surface {
  background: var(--color-card-bg, #ffffff);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 20px;
  padding: 20px;
  box-shadow: var(--shadow, 0 4px 6px rgba(0,0,0,0.05));
}
.content-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}
.card {
  background: var(--color-card-bg, #ffffff);
  padding: 2rem;
  border-radius: 20px;
  box-shadow: var(--shadow, 0 10px 30px rgba(0, 0, 0, 0.07));
  border: 1px solid var(--color-border, #eef);
}
.card h3 {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--color-text-header, #333);
  margin: 0 0 0.5rem 0;
}
.card p {
  font-size: 1rem;
  color: var(--color-text-body, #555);
  margin: 0.25rem 0;
}
.card p.subtitle {
  font-size: 0.9rem;
  color: #888;
  margin-bottom: 1.5rem;
}
.card-notifications {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}
.icon-success {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: #28a745;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
}
.btn-primary {
  width: 100%;
  padding: 14px;
  border: none;
  border-radius: 10px;
  background-color: var(--color-primary, #C00000);
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  margin-top: 1rem;
  font-family: var(--font-primary, 'Poppins', sans-serif);
}
.btn-primary:hover {
  background-color: var(--color-primary-dark, #a00000);
  box-shadow: 0 5px 15px rgba(192, 0, 0, 0.3);
}
.analytics-title {
  font-family: 'Poppins', sans-serif;
  font-size: 1.5rem;
  font-weight: 700;
  margin: 2rem 0 1.5rem 0;
  color: var(--color-text-header);
}
.chart-card {
  min-height: 400px;
  display: flex;
  flex-direction: column;
}
.chart-wrapper {
  flex: 1;
  position: relative;
  margin-top: 1rem;
  min-height: 250px;
}
.loading-text {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #888;
  font-style: italic;
}
</style>