import { createApp } from 'vue'
import { createI18n } from 'vue-i18n'
import axios from 'axios'
import App from './App.vue'
import router from './router'
import { messages } from './locales/translations'

const i18n = createI18n({
    legacy: true,
    globalInjection: true,
    locale: 'fr',
    fallbackLocale: 'fr',
    messages: messages
})

const app = createApp(App)

const token = localStorage.getItem('user-token')
if (token) {
    axios.defaults.headers.common.Authorization = `Bearer ${token}`
}

app.use(router)
app.use(i18n)

app.mount('#app')
