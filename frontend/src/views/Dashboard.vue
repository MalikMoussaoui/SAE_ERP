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
            <p class="subtitle">{{ latestSheet.code }} - {{ latestSheet.semestre }}</p>
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
          
          <div v-if="notificationsLoading" class="loading-state">
            <p>Chargement...</p>
          </div>

          <template v-else-if="unreadCount === 0">
            <div class="icon-success">
              <span>OK</span>
            </div>
            <h3>{{ $t('dashboard.notifications') }}</h3>
            <p>{{ $t('dashboard.noNotifications') }}</p>
          </template>

          <template v-else>
            <div class="icon-warning">
              <span>!</span>
            </div>
            <h3>{{ $t('dashboard.notifications') }} ({{ unreadCount }})</h3>
            
            <div class="notification-list-container">
              <ul class="notification-list">
                <li v-for="notif in notifications" :key="notif.entityId" class="notification-item">
                  <router-link :to="notif.actionUrl" class="notification-link">
                    <span :class="['urgency-dot', notif.urgency.toLowerCase()]"></span>
                    <span class="notif-content">
                      <strong>{{ notif.type }}</strong> : {{ notif.message }}
                    </span>
                  </router-link>
                </li>
              </ul>
            </div>
          </template>

        </div>

      </section>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';
import axios from 'axios';

export default {
  name: 'DashboardView',
  components: {
    DashboardLayout
  },
  data() {
    return {
      userName: '',
      latestSheet: null,
      // Nouvelles variables pour les notifications
      notifications: [],
      notificationsLoading: false
    };
  },
  computed: {
    unreadCount() {
      return this.notifications.length;
    }
  },
  async mounted() {
    this.userName = localStorage.getItem('userName') || 'Utilisateur';
    
    // On charge les stats et les notifications en même temps
    await Promise.all([
      this.fetchStats(),
      this.fetchNotifications()
    ]);
  },
  methods: {
    async fetchStats() {
      try {
        const response = await axios.get('http://localhost:8080/api/dashboard/stats', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        });
        this.latestSheet = response.data.latestSheet;
      } catch (error) {
        this.latestSheet = null;
      }
    },
    
    // Nouvelle méthode locale pour récupérer les notifications
    async fetchNotifications() {
      this.notificationsLoading = true;
      try {
        const response = await axios.get('http://localhost:8080/api/notifications/pending', {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        });
        this.notifications = response.data;
      } catch (error) {
        console.error("Erreur notifications:", error);
        this.notifications = [];
      } finally {
        this.notificationsLoading = false;
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
  display: flex;
  flex-direction: column;
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
  align-items: center;
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
  font-size: 1rem;
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
  margin-top: auto;
  font-family: var(--font-primary, 'Poppins', sans-serif);
}
.btn-primary:hover {
  background-color: var(--color-primary-dark, #a00000);
  box-shadow: 0 5px 15px rgba(192, 0, 0, 0.3);
}
.icon-warning {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: #ffc107;
  color: #333;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
}
.loading-state {
  color: #888;
  margin: auto;
}
.notification-list-container {
  width: 100%;
  max-height: 200px;
  overflow-y: auto;
  margin-top: 1rem;
  border-top: 1px solid #eee;
  text-align: left;
}
.notification-list-container::-webkit-scrollbar {
  width: 6px;
}
.notification-list-container::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 4px;
}
.notification-list {
  list-style: none;
  padding: 0;
  margin: 0;
}
.notification-item {
  border-bottom: 1px solid #f5f5f5;
}
.notification-item:last-child {
  border-bottom: none;
}
.notification-link {
  display: flex;
  align-items: center;
  padding: 12px 8px;
  text-decoration: none;
  color: var(--color-text-body, #333);
  transition: background-color 0.2s;
  border-radius: 6px;
}
.notification-link:hover {
  background-color: #f9f9f9;
}
.notif-content {
  font-size: 0.9rem;
  line-height: 1.3;
}
.urgency-dot {
  display: inline-block;
  min-width: 10px;
  height: 10px;
  border-radius: 50%;
  margin-right: 12px;
  flex-shrink: 0;
}
.urgency-dot.high { background-color: #dc3545; }
.urgency-dot.medium { background-color: #ffc107; }
.urgency-dot.low { background-color: #17a2b8; }
</style>