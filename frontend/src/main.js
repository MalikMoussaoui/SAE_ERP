import { createApp } from 'vue'
import App from './App.vue'

// 1. On importe le "GPS" (le routeur)
import router from './router'
import i18n from './i18n' // On importe notre configuration i18n

// On crée l'application
const app = createApp(App)

// 2. On dit à Vue d'utiliser le "GPS"
app.use(router)

// On dit aussi à Vue d'utiliser le système de traduction
app.use(i18n)

// 3. On "monte" l'application dans la page
app.mount('#app')