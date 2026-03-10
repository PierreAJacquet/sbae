# 🛡️ Spring Boot Angular Etude (SBAE)
Ce projet à pour but de développer une application web simple en Spring Boot et Angular, connectée à une base PostgreSQL via Docker Compose, permettant de rechercher des incidents dans une base contenant 100 000 entrées. L'objectif est de mesurer et améliorer les performances de cette recherche.

---

## 🛠️ Stack Technique
| Technologie                     | Utilisation |
|:--------------------------------| :--- |
| **Java 25 / Spring Boot 3.5.6** | Backend & API Rest |
| **Angular 21**                  | Frontend Standalone |
| **PostgreSQL**                  | Stockage & Indexation |
| **JPA / Hibernate**             | ORM & Mapping |

---

## ⚙️ Guide de Lancement

Le projet est décomposé en trois couches. Suivez les étapes dans l'ordre pour assurer la connectivité entre les services.

### 🗄️ 1. Base de données (Docker)
L'infrastructure repose sur un conteneur PostgreSQL géré par Docker Compose.
1. Se positionner dans le module `sbea-database`.
2. Lancer l'infrastructure :
   ```bash
   docker compose up -d
3. Vérification : Consultez les logs Docker pour confirmer l'exécution des scripts d'initialisation SQL.

### ☕ 2. Backend (Spring Boot)
Le serveur API gère la logique métier et l'accès aux données.
Les versions nécessaires sont Java 25 et Maven 3.5.X

1. Se positionner dans le module sbea-webapp.
2. Compiler et installer les dépendances :
    ```bash
    mvn clean install
3. Démarrer l'application :
    ```bash
    mvn spring-boot:run
4. Vérification : Attendre le message ```Started SbaeApplication``` dans la console.

### 🅰️ 3. Frontend (Angular)
L'interface utilisateur nécessite Node.js 20+ et Npm.

1. Se positionner dans le dossier sbae-frontend.
2. Installer les packages :
    ```bash
    npm install
3. Lancer le serveur de développement :
    ```bash
    ng start
4. Accès : L'application est disponible sur http://localhost:4200/

---

## 📉 Analyse des Performances & Optimisation

#### 🚀 Étape 1 : Optimisation de la couche de données (Requêtes @Query JPQL)

* **Mécanisme** : Remplacement du filtrage par Stream Java (côté applicatif) par une requête SQL paramétrée (côté serveur de base de données).
* **Impact** : Suppression du chargement massif en RAM. Seuls les résultats filtrés transitent sur le réseau entre PostgreSQL et Spring Boot.
* **Correction** : Lors de l'optimisation, reprise de la logique pour inclure des "like' au lieu d'égalité stricte (Influe sur le gain constaté)
* **Test** : Pour les deux scénarios de mesure, demande de récupération de l'ensemble des lignes de la base.

| Méthode | Temps Moyen |   Gain    |
| :--- |:-----------:|:---------:|
| **Stream Java (Initial)** | **3.488s**  |     -     |
| **@Query SQL (Optimisé)** | **2.389s**  | **31,5%** |

> **Analyse technique** : Le moteur SQL est conçu pour filtrer des millions de lignes via des algorithmes optimisés. Réduction importante du nombre de requêtes.

#### ⚡ Étape 2 : Indexation des colonnes (PostgreSQL Indexing)

* **Mécanisme** : Mise en place d'index **GIN (Generalized Inverted Index)** avec l'extension `pg_trgm` pour optimiser les recherches textuelles partielles (`LIKE`).
* **Objectif** : Passer d'un *Sequential Scan* (lecture totale de la table) à un *Index Scan* (accès direct aux données).
* **Test** : Pour les deux scénarios de mesure, demande de récupération de l'ensemble des lignes de la base.


| Méthode | Temps Moyen |   Gain    |
| :--- |:-----------:|:---------:|
| **Filtrage SQL (@Query seul)** | **2.389s**  |     -     |
| **Filtrage SQL + Indexation** | **1.589s**  | **33,5%** |

> **Analyse technique** : L'index GIN permet à PostgreSQL de découper les mots en trigrammes. Lors d'une recherche, le moteur ne parcourt plus la table mais consulte l'index, ce qui réduit la complexité de recherche de manière drastique, surtout sur des volumes de 100 000+ lignes.

#### 🏆 Étape 3 : Optimisation du flux de données (Pagination back to front)

* **Mécanisme** : Implémentation de la pagination serveur via Spring Data Pageable et transformation des entités en DTO à la volée.
* **Impact** : Réduction massive de la charge réseau et du rendu DOM. Le serveur ne renvoie plus 15 Mo de JSON, mais seulement 2 Ko par page (10 résultats).
* **Correction** : Utilisation de Page.map() côté Service pour transformer uniquement les objets visibles, préservant les ressources CPU du serveur.
* **Amélioration** : Rendu du tableau adapté à la pagination.
* **Test** : Pour les deux scénarios de mesure, demande de récupération de l'ensemble des lignes de la base. Dans le second test, chargement de la première page de résultats (10 lignes) après filtrage sur 100 000 entrées

| Méthode                             | Temps Moyen |   Gain    |
|:------------------------------------|:-----------:|:---------:|
| **Flux Complet (List)**             | **1.589s**  |     -     |
| **Flux Paginé (Page, Cache Froid)** | **0.352s**  | **77,8%** |
| **Flux Paginé (Page, Cache Chaud)** | **0.047s**  | **97,0%** |

> **Analyse de la variance** : On observe une fluctuation entre 0.352s (Premier accès/Cache froid) et 0.047s (Accès répété/Cache chaud). Cette stabilité sous la barre des 0.5s garantit une expérience utilisateur fluide, même dans le scénario le moins favorable.

> **Analyse technique** : Cette étape est la clé de la scalabilité. Sans elle, l'application s'effondrerait avec 1 million de lignes, même avec des index parfaits. Avec la pagination, le coût de traitement devient constant, quel que soit le volume total de la base de données.

#### 🛡️ Étape 4 : Hardening et Intégrité des données

* **Mécanisme** : Mise en place d'une validation symétrique entre la base de données (VARCHAR calibrés, contraintes CHECK) et le Front-end (Validators.maxLength et Validators.email sur Angular).
* **Impact** : Amélioration de la robustesse globale. Le Front-end intercepte les saisies invalides avant l'envoi, tandis que le moteur PostgreSQL optimise l'alignement des données en mémoire vive grâce à des tailles de champs prévisibles.
* **Correction** : Alignement strict des longueurs et ajout d'une contrainte de domaine sur la sévérité au niveau physique de la table.
* **Test** : Tentative de saisie de données hors limites (title = 1000 caractères) contre limite définit (title = 100 caractères) et mesure de la stabilité du temps de réponse sur le cache froid.

| Méthode                            | Temps Moyen (Cache Froid) |   Gain    |
|:-----------------------------------|:-------------------------:|:---------:|
| **Sans Contraintes**               |        **0.352s**         |     -     |
| **Avec Contraintes**               |        **0.330s**         | **6.25%** |

> **Analyse technique** : Cette étape stabilise la prédictibilité du système. En limitant la taille des colonnes, PostgreSQL affine ses statistiques de coût : le moteur estime plus justement la largeur des lignes en RAM, évitant des écritures temporaires sur disque (Swap) lors des jointures complexes. Côté client, l'usage des Validators garantit que 100% des requêtes traitées par Spring Boot sont conformes, économisant ainsi des cycles CPU inutiles. Ce gain de 6,25% sur un système déjà optimisé confirme que l'intégrité des types de données est un levier pour atteindre une meilleure performance.
