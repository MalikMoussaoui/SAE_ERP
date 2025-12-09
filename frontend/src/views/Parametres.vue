<template>
  <DashboardLayout>
    <template #header>
      <div class="header-welcome">
        <h1 class="page-title">{{ $t('settings.title') }}</h1>
      </div>
    </template>

    <div class="settings-section">
      <h2 class="section-title">{{ $t('settings.appearance') }}</h2>
      <div class="content-card">
        <h3>{{ $t('settings.appTheme') }}</h3>
        <p class="card-subtitle">{{ $t('settings.themeSubtitle') }}</p>
        <div class="theme-options">
          <label class="theme-option">
            <input type="radio" name="theme" value="light" v-model="selectedTheme">
            <div class="option-box">
              <span class="icon">☀️</span>
              <span>{{ $t('settings.light') }}</span>
            </div>
          </label>
          <label class="theme-option">
            <input type="radio" name="theme" value="dark" v-model="selectedTheme">
            <div class="option-box">
              <span class="icon">🌙</span>
              <span>{{ $t('settings.dark') }}</span>
            </div>
          </label>
        </div>
      </div>
    </div>

    <div class="settings-section">
      <h2 class="section-title">{{ $t('settings.language') }}</h2>
      <div class="content-card">
        <h3>{{ $t('settings.appLanguage') }}</h3>
        <p class="card-subtitle">{{ $t('settings.languageSubtitle') }}</p>
        <div class="theme-options">
          <label class="theme-option">
            <input type="radio" name="lang" value="fr" v-model="selectedLang">
            <div class="option-box">
              <span class="icon">🇫🇷</span>
              <span>{{ $t('settings.french') }}</span>
            </div>
          </label>
          <label class="theme-option">
            <input type="radio" name="lang" value="en" v-model="selectedLang">
            <div class="option-box">
              <span class="icon">🇬🇧</span>
              <span>{{ $t('settings.english') }}</span>
            </div>
          </label>
        </div>
      </div>
    </div>
  </DashboardLayout>
</template>

<script>
import DashboardLayout from '@/components/DashboardLayout.vue';

export default {
  name: 'ParametresView',
  components: {
    DashboardLayout
  },
  data() {
    return {
      selectedTheme: localStorage.getItem('theme') || 'light',
      selectedLang: this.$i18n.locale
    };
  },
  watch: {
    selectedTheme(newTheme) {
      localStorage.setItem('theme', newTheme);
      document.documentElement.setAttribute('data-theme', newTheme);
    },
    selectedLang(newLang) {
      this.$i18n.locale = newLang;
      localStorage.setItem('lang', newLang);
    }
  }
}
</script>

<style scoped>
.page-title {
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--color-text-header, #222);
  margin: 0;
}

.content-card {
  background: var(--color-card-bg, white);
  border: 1px solid var(--color-border, #ddd);
  border-radius: 12px;
  padding: 2rem;
  box-shadow: var(--shadow, 0 4px 6px rgba(0,0,0,0.05));
}

.settings-section {
  margin-bottom: 2.5rem;
  max-width: 800px;
}

.section-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--color-text-muted, #666);
  margin-bottom: 1rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.content-card h3 {
  margin-top: 0;
  margin-bottom: 0.25rem;
  font-family: var(--font-primary, 'Poppins', sans-serif);
  font-size: 1.25rem;
  color: var(--color-text-header, #333);
}

.card-subtitle {
  margin-top: 0;
  margin-bottom: 1.5rem;
  color: var(--color-text-body, #777);
  font-size: 0.95rem;
}

.theme-options {
  display: flex;
  gap: 1rem;
}

.theme-option input[type="radio"] {
  display: none; /* On cache le bouton radio natif */
}

.theme-option .option-box {
  border: 2px solid var(--color-border, #e0e0e0);
  border-radius: 8px;
  padding: 0.7rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  min-width: 100px;
  text-align: center;
}

.theme-option .option-box:hover {
  border-color: #C00000;
  background-color: rgba(192, 0, 0, 0.05);
}

.theme-option input[type="radio"]:checked + .option-box {
  border-color: #C00000;
  background-color: #C00000;
  color: white;
  box-shadow: var(--shadow, 0 4px 10px rgba(192, 0, 0, 0.2));
}

.option-box .icon {
  font-size: 1.3rem;
  margin-bottom: 0.25rem;
}

.option-box span {
  font-weight: 600;
  font-size: 0.9rem;
}
</style>