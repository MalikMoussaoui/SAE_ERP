import { createRouter, createWebHistory } from 'vue-router'
import ConnexionView from '../views/Connexion.vue'
import DashboardView from '../views/Dashboard.vue'
import GestionUtilisateurs from '../views/GestionUtilisateurs.vue'
import Parametres from '../views/Parametres.vue'
import AjoutUtilisateur from '../views/AjoutUtilisateur.vue'

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
      path: '/gestion-utilisateurs',
      name: 'gestion-utilisateurs',
      component: GestionUtilisateurs
    },
    {
      path: '/gestion-utilisateurs/ajouter',
      name: 'gestion-utilisateurs-ajouter',
      component: AjoutUtilisateur
    },
    {
      path: '/parametres',
      name: 'Parametres',
      component: Parametres
    }
  ]
})

export default router
