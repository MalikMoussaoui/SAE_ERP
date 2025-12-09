import { createApp } from 'vue'
import App from './App.vue'

import router from './router'
import i18n from './i18n'

// Applique le thème sauvegardé dès le chargement pour éviter un flash de contenu
const savedTheme = localStorage.getItem('theme') || 'light';
document.documentElement.setAttribute('data-theme', savedTheme);

const app = createApp(App)

app.use(router)
app.use(i18n)

app.mount('#app')