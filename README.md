# 🎓 ERP Centre de Formation

> Application web de gestion des dossiers et processus administratifs d'un centre de formation, développée pour l'**IUT du Limousin** dans le cadre d'une SAÉ (Situation d'Apprentissage et d'Évaluation).

![Java](https://img.shields.io/badge/Java-21-007396?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-6DB33F?style=flat&logo=spring-boot&logoColor=white)
![Vue.js](https://img.shields.io/badge/Vue.js-3-4FC08D?style=flat&logo=vue.js&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?style=flat&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639?style=flat&logo=nginx&logoColor=white)

---

## 📖 À propos du projet

**ERP Centre de Formation** est une application web full-stack développée en équipe de 5 étudiants en BUT Informatique. Elle automatise et simplifie la gestion administrative d'un centre de formation : suivi des fiches ressources, du TAC et MCCC, analyse et amélioration pédagoqique des enseignant, édition des documents.

Le projet a été **commandité par Mme Sarlot (IUT du Limousin)** et **déployé en production sur ue serveur Linux de l'université**.

---

## ✨ Fonctionnalités principales

L'application est structurée en **6 modules fonctionnels** :
 
### 🔐 1. Authentification & gestion des utilisateurs
- Connexion / déconnexion sécurisées via **JWT** et **Spring Security**
- Inscription et gestion des profils utilisateurs (nom, email, téléphone)
- **Gestion des rôles** : Enseignant, Vacataire, RH, Chef de département, Admin — avec droits spécifiques par rôle
- Rattachement de chaque utilisateur à son département d'origine
### 📚 2. Maquette pédagogique (référentiel)
- **Départements** : création et assignation d'un chef de département
- **Unités d'Enseignement (UE)** : création, rattachement à un semestre, définition des objectifs
- **Ressources et SAÉ** : définition des volumes horaires (CM, TD, TP, Projet) rattachés aux UE
- **Référentiel de compétences** visées par la formation
### 📄 3. Fiches ressources (cœur de l'ERP)
- Création et édition de fiches : objectifs, prérequis, modalités, heures prévues
- **Cycle de vie** : statut brouillon (DRAFT) → validation → archivage
- **Liaison aux compétences** (relation many-to-many)
- Assignation d'un enseignant responsable à chaque fiche
### ⏱️ 5. Suivi des services faits
- Déclaration par les enseignants et vacataires des heures réellement effectuées
- Rattachement de chaque service fait à une fiche ressource précise


---

## 🛠️ Stack technique

### Backend
| Catégorie       | Technologie                                |
| --------------- | ------------------------------------------ |
| Langage         | Java 21 (Amazon Corretto)                  |
| Framework       | Spring Boot 3.3.5                          |
| Sécurité        | Spring Security avec JWT                   |
| ORM             | Spring Data JPA + Hibernate 6.5            |
| Migrations BDD  | Flyway                                     |
| Build           | Maven                                      |
| Outils          | Lombok, SLF4J                              |

### Frontend
| Catégorie       | Technologie                                |
| --------------- | ------------------------------------------ |
| Framework       | Vue.js (SPA)                               |
| Build           | Node.js + npm                              |
| Serveur         | Nginx (en production)                      |

### Base de données
| Catégorie       | Technologie                                |
| --------------- | ------------------------------------------ |
| SGBD            | PostgreSQL 15                              |
| Pool            | HikariCP                                   |
| Administration  | Adminer (web) · pgAdmin 4 (local)          |

### Infrastructure & DevOps
| Catégorie       | Technologie                                |
| --------------- | ------------------------------------------ |
| Conteneurisation| Docker · Docker Compose (4 conteneurs)     |
| Versionnage     | Git · GitHub                               |
| Production      | Linux Debian (serveur IUT, accès SSH/VPN)  |

---

## 🏗️ Architecture

L'application est entièrement conteneurisée et orchestrée via Docker Compose. Quatre services tournent en parallèle et communiquent sur un réseau Docker dédié :

```
┌──────────────┐      ┌──────────────┐      ┌──────────────┐
│   Frontend   │──────│   Backend    │──────│  PostgreSQL  │
│  (Vue + Nginx)│      │ (Spring Boot)│      │  (database)  │
└──────────────┘      └──────────────┘      └──────────────┘
                              │                     │
                              │              ┌──────────────┐
                              └──────────────│   Adminer    │
                                             │  (admin web) │
                                             └──────────────┘
```

- **Frontend** : SPA Vue.js compilée et servie par Nginx
- **Backend** : API REST Spring Boot, sécurisée par JWT
- **Base de données** : PostgreSQL avec migrations versionnées par Flyway
- **Adminer** : interface web légère pour l'administration de la base

---

## 🚀 Installation locale

### Prérequis
- [Docker](https://www.docker.com/) et Docker Compose
- [Git](https://git-scm.com/)

### Étapes

```bash
# 1. Cloner le dépôt
git clone https://github.com/bmarty75/SAE-ERP
cd SAE-ERP

# 2. Configurer les variables d'environnement
cp .env.example .env
# Éditer .env avec vos propres identifiants si besoin

# 3. Lancer l'ensemble des services
docker compose up -d

# 4. Vérifier que les conteneurs tournent
docker compose ps
```

---

## 👥 Équipe

Projet réalisé en équipe de 5 étudiants en BUT Informatique à l'IUT du Limousin.

**rôle ([Benjamin Marty](https://github.com/bmarty75))** : développement **back-end** (équipe back-end de 3 développeurs sur 5).


---

## 📚 Contexte académique

Projet réalisé dans le cadre du **BUT Informatique** à l'**IUT du Limousin** — Université de Limoges.

- **Année** : 2025 – 2026 (BUT2)
- **Module** : SAÉ (Situation d'Apprentissage et d'Évaluation)
- **Commanditaire** : Mme Sarlot (IUT du Limousin)

---
