import { createRouter, createWebHistory } from 'vue-router'
import ConnexionView from '../views/Connexion.vue'
import DashboardView from '../views/Dashboard.vue'
import GestionUtilisateurs from '../views/GestionUtilisateurs.vue'
import Parametres from '../views/Parametres.vue'
import AjoutUtilisateur from '../views/AjoutUtilisateur.vue'
import McccView from '../views/Mccc.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/connexion',
      name: 'connexion',
      component: ConnexionView
    },
    {
      path: '/',
      redirect: '/connexion'
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: DashboardView
    },
    {
      path: '/user-management',
      name: 'user-management',
      component: GestionUtilisateurs
    },
    {
      path: '/user-management/add',
      name: 'user-management-add',
      component: AjoutUtilisateur
    },
    {
      path: '/mccc',
      name: 'mccc',
      component: McccView
    },
    {
      path: '/settings',
      name: 'settings',
      component: Parametres
    }
  ]
})

export default router
