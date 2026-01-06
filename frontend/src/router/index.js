import { createRouter, createWebHistory } from 'vue-router'
import ConnexionView from '../views/Connexion.vue'
import DashboardView from '../views/Dashboard.vue'
import GestionUtilisateurs from '../views/GestionUtilisateurs.vue'
import Parametres from '../views/Parametres.vue'
import AjoutUtilisateur from '../views/AjoutUtilisateur.vue'
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
      component: DashboardView
    },
    {
      path: '/fiche-ressource',
      name: 'fiche-ressource',
      component: FicheRessourceView
    },
    {
      path: '/liste-fiches-ressources',
      name: 'liste-fiches-ressources',
      component: ListeFichesRessources
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
      path: '/liste-mccc',
      name: 'liste-mccc',
      component: ListeMccc
    },
    {
      path: '/tac',
      name: 'tac',
      component: TacView
    },
    {
      path: '/enseignants',
      name: 'enseignants',
      component: EnseignantsVacataires
    },
    {
      path: '/settings',
      name: 'settings',
      component: Parametres
    },
    {
      path: '/aide',
      name: 'aide',
      component: AideView
    }
  ]
})

export default router
