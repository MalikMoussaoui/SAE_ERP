# SAE_ERP

## Guide d'installation

Ce projet est compose de :

- un frontend `Vue 3 + Vite`
- un backend `Spring Boot 3`
- une base de donnees `PostgreSQL 15` lancee avec `Docker Compose`

Le frontend dialogue avec le backend via l'URL `http://localhost:8080/api`. Le backend dialogue avec PostgreSQL sur `localhost:5432`.

## 1. Prerequis

Installer les outils suivants sur la machine :

- `Git`
- `Node.js` version `20.19.0` minimum
- `npm`
- `Java JDK 21`
- `Maven 3.9+`
- `Docker Desktop` avec `Docker Compose`

Verification rapide :

```powershell
node -v
npm -v
java -version
mvn -v
docker --version
docker compose version
```

## 2. Recuperer le projet

Cloner le depot puis se placer a la racine du projet :

```powershell
git clone <URL_DU_DEPOT>
cd SAE_ERP
```

Si le depot est deja present, il suffit d'ouvrir le dossier `SAE_ERP`.

## 3. Installer les dependances du frontend

Ouvrir un terminal a la racine du projet puis executer :

```powershell
cd frontend
npm install
```

Cette commande installe toutes les dependances Vue/Vite necessaires au frontend.

## 4. Demarrer la base de donnees

Depuis un second terminal :

```powershell
cd backend
docker compose up -d
```

Services demarres :

- PostgreSQL : `localhost:5432`
- Adminer : `http://localhost:8081`

Parametres de connexion PostgreSQL :

- base : `erp`
- utilisateur : `erp`
- mot de passe : `erp`

## 5. Demarrer le backend

Toujours dans le dossier `backend`, lancer :

```powershell
mvn spring-boot:run
```

Configuration du backend :

- port applicatif : `8080`
- URL API : `http://localhost:8080/api`
- migrations : gerees automatiquement par `Flyway`
- donnees initiales : creees automatiquement au demarrage par le seeder

Le backend est pret lorsque Spring Boot indique qu'il ecoute sur le port `8080`.

## 6. Demarrer le frontend

Depuis un troisieme terminal :

```powershell
cd frontend
npm run dev
```

Vite affichera une URL similaire a :

```text
http://localhost:5173
```

Ouvrir ensuite cette URL dans le navigateur.

## 7. Comptes de connexion par defaut

Le backend cree automatiquement plusieurs comptes de demonstration. Le mot de passe par defaut est :

```text
1234
```

Comptes utiles :

- administrateur : `admin@unilim.fr`
- direction : `direction@unilim.fr`
- RH : `rh@unilim.fr`
- responsable pedagogique : `alan.turing@unilim.fr`
- enseignant : `ada.lovelace@unilim.fr`
- enseignant : `tim.berners@unilim.fr`
- referent : `edgar.codd@unilim.fr`
- vacataire : `elon.musk@tesla.com`

Pour une premiere verification, utiliser le compte administrateur :

- email : `admin@unilim.fr`
- mot de passe : `1234`

## 8. URLs utiles

- frontend : `http://localhost:5173`
- backend : `http://localhost:8080`
- API : `http://localhost:8080/api`
- Adminer : `http://localhost:8081`
- Swagger UI : `http://localhost:8080/swagger-ui/index.html`

## 9. Ordre de lancement recommande

Pour eviter les erreurs de connexion, respecter cet ordre :

1. lancer Docker Desktop
2. lancer la base avec `docker compose up -d` dans `backend`
3. lancer le backend avec `mvn spring-boot:run`
4. lancer le frontend avec `npm run dev`
5. ouvrir `http://localhost:5173`

## 10. Verification de bon fonctionnement

L'installation est consideree comme correcte si :

- PostgreSQL tourne sur le port `5432`
- le backend repond sur `http://localhost:8080`
- le frontend s'ouvre sur `http://localhost:5173`
- la connexion avec `admin@unilim.fr / 1234` fonctionne
- les donnees initiales sont visibles dans l'application

## 11. Arret des services

Pour arreter le frontend : fermer le terminal ou faire `Ctrl + C`.

Pour arreter le backend : fermer le terminal ou faire `Ctrl + C`.

Pour arreter la base de donnees :

```powershell
cd backend
docker compose down
```

## 12. Reinitialiser la base de donnees

Si vous souhaitez repartir d'une base vide avec reseeding automatique :

```powershell
cd backend
docker compose down -v
docker compose up -d
```

Puis relancer le backend avec :

```powershell
mvn spring-boot:run
```

Attention : `docker compose down -v` supprime les donnees PostgreSQL stockees dans le volume Docker.

## 13. Probleme frequents

### Port 5432 deja occupe

Une autre instance PostgreSQL est probablement deja lancee sur la machine. Il faut soit l'arreter, soit modifier le mapping de port dans `backend/docker-compose.yml`.

### Port 8080 deja occupe

Une autre application Java ou web utilise deja ce port. Il faut la fermer ou changer le port dans `backend/src/main/resources/application.yml`.

### Le frontend ne joint pas le backend

Verifier que :

- le backend tourne bien sur le port `8080`
- l'URL `axios.defaults.baseURL` dans [frontend/src/main.js](C:/Users/matte/Desktop/SAE_ERP/frontend/src/main.js) vaut bien `http://localhost:8080/api`

### Erreur de connexion a la base

Verifier que le conteneur PostgreSQL est bien demarre :

```powershell
cd backend
docker compose ps
```

## 14. Elements techniques importants

- aucune variable d'environnement supplementaire n'est necessaire pour un lancement local standard
- les migrations SQL sont dans `backend/src/main/resources/db/migration`
- le seeding de donnees est gere par `DatabaseSeeder`
- le frontend utilise une URL de backend configuree en dur pour `localhost`

## 15. Verification effectuee sur ce depot

Les verifications suivantes ont ete executees avec succes sur ce projet :

- build frontend : `npm run build`
- tests backend : `mvn test -q`

Le guide ci-dessus est donc aligne avec la configuration actuellement presente dans le depot.
