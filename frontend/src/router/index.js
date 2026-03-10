import { createRouter, createWebHistory } from 'vue-router'
import ConnexionView from '../views/Connexion.vue'
import DashboardView from '../views/Dashboard.vue'
import GestionUtilisateurs from '../views/GestionUtilisateurs.vue'
import Parametres from '../views/Parametres.vue'
import McccView from '../views/Mccc.vue'
import FicheRessourceView from '../views/FicheRessource.vue'
import ListeFichesRessources from '../views/ListeFichesRessources.vue'
import ListeMccc from '../views/ListeMccc.vue'
import AideView from '../views/Aide.vue'
import EnseignantsVacataires from '../views/EnseignantsVacataires.vue'
import TacView from '../views/Tac.vue'


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
      component: DashboardView,
      meta: { requiresAuth: true }
    },
    {
      path: '/fiche-ressource/:id?',
      name: 'fiche-ressource',
      component: FicheRessourceView,
      props: true,
      meta: { requiresAuth: true }
    },
    {
      path: '/liste-fiches-ressources',
      name: 'liste-fiches-ressources',
      component: ListeFichesRessources,
      meta: { requiresAuth: true }
    },
    {
      path: '/user-management',
      name: 'user-management',
      component: GestionUtilisateurs,
      meta: { requiresAuth: true, roles: ['ADMINISTRATEUR'] }
    },
    {
      path: '/mccc',
      name: 'mccc',
      component: McccView,
      meta: { requiresAuth: true }
    },
    {
      path: '/liste-mccc',
      name: 'liste-mccc',
      component: ListeMccc,
      meta: { requiresAuth: true }
    },
    {
      path: '/tac',
      name: 'tac',
      component: TacView,
      meta: { requiresAuth: true }
    },
    {
      path: '/enseignants',
      name: 'enseignants',
      component: EnseignantsVacataires,
      meta: { requiresAuth: true }
    },
    {
      path: '/settings',
      name: 'settings',
      component: Parametres,
      meta: { requiresAuth: true }
    },
    {
      path: '/aide',
      name: 'aide',
      component: AideView,
      meta: { requiresAuth: true }
    }
  ]
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('user-token');
  const userRole = localStorage.getItem('userRole');

  if (to.meta.requiresAuth && !token) {
    next('/connexion');
  } else if (to.meta.roles && !to.meta.roles.includes(userRole)) {
    next('/dashboard');
  } else {
    next();
  }
});

export default router
