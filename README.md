# 🛡️ Spring Boot Angular Etude (SBAE)
Ce projet à pour but de développer une application web simple en Spring Boot et Angular, connectée à une base PostgreSQL via Docker Compose, permettant de rechercher des incidents dans une base contenant 100 000 entrées. L'objectif est de mesurer et améliorer les performances de cette recherche.

---

## 🛠️ Stack Technique
| Technologie                     | Utilisation |
|:--------------------------------| :--- |
| **Java 25 / Spring Boot 3.5.6** | Backend & API Rest |
| **Angular 20**                  | Frontend Standalone |
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

1. Se positionner à la racine du projet sbea.
2. Compiler et installer les dépendances :
    ```bash
    mvn clean install
3. Démarrer l'application :
    ```bash
    mvn spring-boot:run
4. Vérification : Attendre le message ```Started SbaeApplication``` dans la console.

### 🅰️ 3. Frontend (Angular)
L'interface utilisateur nécessite Node.js 20+ et Angular CLI.

1. Se positionner dans le dossier sbae-frontEnd.
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

> **Analyse** : Le moteur SQL est conçu pour filtrer des millions de lignes via des algorithmes optimisés. Réduction importante du nombre de requêtes.

