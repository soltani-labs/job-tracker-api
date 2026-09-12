Petit projet perso pour suivre stages et emploies. Pour remplacerexcel j'ai fait ma propre API avec Spring Boot.

C'est quoi ce projet

L'idée c'est de enregistrer chaque candidature (entreprise, poste, date, lien) et suivre l'évolution : retour, entretien, test, offre, ou refus.

Le truc c'est l'historique des statuts. Si ça passe de "APPLIED" a "PHONE_SCREEN", je peux voir toutes les étapes et la date. Ça évite de écraser l'info.

Les statuts :

APPLIED : candidature envoyée

PHONE_SCREEN : premier appel

TECHNICAL : entretien ou test

OFFER : ils ont proposé le poste

REJECTED : refus

Attention : on peut pas changer le statut de n'importe maniere. Par exemple de APPLIED on peut aller vers PHONE_SCREEN ou REJECTED, mais pas direct a OFFER. Et une fois fini (OFFER ou REJECTED), ça arrete.

Avec quoi c'est fait

Java avec Spring Boot

Spring Data JPA

H2 (base en mémoire, pratique pour tester)

Lombok

Validation avec @NotBlank, @Size...

Comment c'est organisé

domain : les entités (Application et StatusHistory)

dto : les objets pour les requêtes

repository : les interfaces JPA

service : la logique métier

controller : les endpoints de l'API

exception : gestion des erreurs

Les endpoints (Base : /api/applications)

POST /api/applications : créer une candidature

GET /api/applications : récupérer tout (avec filtres)

GET /api/applications/{id} : une candidature précise

PUT /api/applications/{id} : modifier

PATCH /api/applications/{id}/status : changer le statut

DELETE /api/applications/{id} : supprimer

Comment le lancer

Cloner le repo

Ouvrir sur ton IDE (moi IntelliJ)

Lancer JobTrackerApiApplication (sur http://localhost:8080)

Console H2 sur http://localhost:8080/h2-console.

Exemple POST /api/applications :

JSON
{
  "company": "Google",
  "role": "Développeur Backend",
  "jobPostingUrl": "https://exemple.com/offre/123",
  "status": "APPLIED",
  "appliedDate": "2026-09-01",
  "notes": "envoyée via LinkedIn"
}

Améliorations futures (peut-être) :

Vraie base de données (PostgreSQL)

Un petit front-end

Des tests unitaires (faut que je m'y mette)

Authentification

Si tu as des remarques, n hesite pas 
