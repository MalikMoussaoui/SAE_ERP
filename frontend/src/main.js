import { createApp } from 'vue'
import { createI18n } from 'vue-i18n'
import App from './App.vue'
import router from './router'
import { messages } from './locales/translations'
import axios from 'axios'

const i18n = createI18n({
    legacy: true,
    globalInjection: true,
    locale: 'fr',
    fallbackLocale: 'fr',
    messages: messages
})

axios.defaults.baseURL = 'http://localhost:8080/api';
// axios.defaults.baseURL = 'http://164.81.120.78:8080/api';


axios.interceptors.request.use(request => {
    const token = localStorage.getItem('user-token');
    if (token) {
        // On ajoute l'en-tête "Authorization: Bearer un_token_super_long..."
        request.headers.Authorization = `Bearer ${token}`;
    }
    return request;
}, error => {
    return Promise.reject(error);
});

const app = createApp(App)

app.use(router)
app.use(i18n)

app.mount('#app')