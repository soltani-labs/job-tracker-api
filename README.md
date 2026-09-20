<div align="center">

# Job Tracker API

### API REST pour suivre ses candidatures et son évolution professionnelle

Une petite API personnelle développée avec **Java et Spring Boot** pour remplacer un simple fichier Excel et suivre ses candidatures de manière structurée.

<br>

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge\&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen?style=for-the-badge\&logo=springboot)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data-JPA-blue?style=for-the-badge)
![H2](https://img.shields.io/badge/Database-H2-lightgrey?style=for-the-badge)
![Lombok](https://img.shields.io/badge/Lombok-red?style=for-the-badge)

</div>

---

## À propos du projet

**Job Tracker API** est une API REST permettant de gérer ses candidatures à des stages et à des emplois.

L'objectif est simple : enregistrer chaque candidature et suivre son évolution au fil du temps.

Pour chaque candidature, on peut enregistrer :

* l'entreprise
* le poste
* la date de candidature
* le lien vers l'offre
* des notes personnelles
* le statut actuel
* l'historique complet des changements de statut

L'un des points importants du projet est la **gestion de l'historique des statuts**.

Par exemple, si une candidature passe de :

```text
APPLIED
   ↓
PHONE_SCREEN
   ↓
TECHNICAL
   ↓
OFFER
```

les anciennes étapes ne sont pas écrasées. Chaque changement est enregistré avec sa date.

---

## Workflow des candidatures

Le projet utilise plusieurs statuts pour représenter l'évolution d'une candidature :

| Statut         | Description                             |
| -------------- | --------------------------------------- |
| `APPLIED`      | Candidature envoyée                     |
| `PHONE_SCREEN` | Premier appel, généralement avec les RH |
| `TECHNICAL`    | Entretien technique ou test             |
| `OFFER`        | Une offre a été proposée                |
| `REJECTED`     | Candidature refusée                     |

### Transitions

Les changements de statut ne sont pas complètement libres.

Par exemple :

```text
              ┌──────────────┐
              │   APPLIED    │
              └──────┬───────┘
                     │
              ┌──────┴───────┐
              ↓              ↓
       PHONE_SCREEN       REJECTED
              │
              ↓
          TECHNICAL
              │
              ↓
            OFFER
```

Une candidature terminée avec `OFFER` ou `REJECTED` ne peut plus avancer vers un autre statut.

---

## Fonctionnalités

* [x] Créer une candidature
* [x] Consulter toutes les candidatures
* [x] Consulter une candidature par son ID
* [x] Modifier une candidature
* [x] Supprimer une candidature
* [x] Changer le statut d'une candidature
* [x] Conserver l'historique des statuts
* [x] Filtrer les candidatures
* [x] Validation des données entrantes
* [x] Gestion des erreurs
* [x] Validation des transitions de statut

---

## Technologies utilisées

| Technologie         | Utilisation                   |
| ------------------- | ----------------------------- |
| **Java**            | Langage principal             |
| **Spring Boot**     | Framework backend             |
| **Spring Data JPA** | Accès aux données             |
| **H2**              | Base de données en mémoire    |
| **Lombok**          | Réduction du code boilerplate |
| **Bean Validation** | Validation des données        |

---

## Architecture du projet

Le projet suit une organisation classique en couches :

```text
src/main/java/job_tracker_api
│
├── controller
│   └── Endpoints REST
│
├── service
│   └── Logique métier
│
├── repository
│   └── Accès aux données avec JPA
│
├── domain
│   ├── Application
│   └── StatusHistory
│
├── dto
│   └── Objets utilisés pour les requêtes et réponses
│
└── exception
    └── Gestion des erreurs métier
```

### Responsabilité des couches

**Controller**

Reçoit les requêtes HTTP et retourne les réponses.

**Service**

Contient la logique métier, notamment les règles de changement de statut.

**Repository**

Communique avec la base de données grâce à Spring Data JPA.

**Domain**

Contient les entités principales de l'application.

**DTO**

Permet de contrôler les données reçues et retournées par l'API.

**Exception**

Centralise la gestion des erreurs et des règles métier invalides.

---

## API Endpoints

Base URL :

```text
http://localhost:8080/api/applications
```

| Méthode  | Endpoint                        | Description                |
| -------- | ------------------------------- | -------------------------- |
| `POST`   | `/api/applications`             | Créer une candidature      |
| `GET`    | `/api/applications`             | Récupérer les candidatures |
| `GET`    | `/api/applications/{id}`        | Récupérer une candidature  |
| `PUT`    | `/api/applications/{id}`        | Modifier une candidature   |
| `PATCH`  | `/api/applications/{id}/status` | Changer le statut          |
| `DELETE` | `/api/applications/{id}`        | Supprimer une candidature  |

---

## Exemple : créer une candidature

### `POST /api/applications`

```json
{
  "company": "Google",
  "role": "Développeur Backend",
  "jobPostingUrl": "https://exemple.com/offre/123",
  "status": "APPLIED",
  "appliedDate": "2026-09-01",
  "notes": "Envoyée via LinkedIn"
}
```

L'API valide les données avant de créer la candidature.

Par exemple, certains champs obligatoires utilisent des annotations comme :

```java
@NotBlank
@Size
```

---

## Exemple de workflow

Une candidature peut évoluer comme ceci :

```text
1. Candidature envoyée
        ↓
2. Premier appel RH
        ↓
3. Entretien / test technique
        ↓
4. Offre
```

À chaque changement, une entrée est ajoutée dans `StatusHistory`.

Cela permet de garder une trace du parcours de la candidature au lieu de simplement remplacer l'ancien statut.

---

## Lancer le projet

### 1. Cloner le repository

```bash
git clone <repository-url>
```

### 2. Ouvrir le projet

Ouvrir le projet avec votre IDE.

**IntelliJ IDEA** est utilisé pour le développement de ce projet.

### 3. Lancer l'application

Exécuter :

```text
JobTrackerApiApplication
```

L'API sera disponible sur :

```text
http://localhost:8080
```

### 4. Accéder à la console H2

La console H2 est disponible sur :

```text
http://localhost:8080/h2-console
```

La base H2 fonctionne en mémoire, ce qui permet de lancer facilement le projet sans installer PostgreSQL.

---

## Améliorations prévues

Quelques améliorations possibles pour les prochaines versions :

* [ ] Remplacer H2 par PostgreSQL
* [ ] Ajouter un petit frontend
* [ ] Ajouter des tests unitaires et d'intégration
* [ ] Ajouter une authentification
* [ ] Ajouter des statistiques sur les candidatures
* [ ] Ajouter une documentation Swagger / OpenAPI

---

## Pourquoi ce projet ?

Ce projet a été créé comme **petit projet personnel et portfolio** afin de pratiquer plusieurs concepts importants de développement backend avec Spring Boot :

* REST API
* Spring Data JPA
* Architecture en couches
* DTOs
* Validation
* Gestion des exceptions
* Logique métier
* Gestion des transitions d'état
* Historisation des données

---

<div align="center">

### Built with Java & Spring Boot

Merci d'avoir consulté le projet.

Si vous avez des remarques ou des suggestions d'amélioration, n'hésitez pas à les partager.

</div>
