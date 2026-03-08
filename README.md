# sbae - spring boot angular etude
Ce projet à pour but de développer une application web simple en Spring Boot et Angular, connectée à une base PostgreSQL via Docker Compose, permettant de rechercher des incidents dans une base contenant 100 000 entrées. L'objectif est de mesurer et améliorer les performances de cette recherche.

__Lancement du projet.__

------ _Docker_ ---------

    1. Se positionner à la racine du module sbea-database
    2. Lancer la commande docker compose up -d
    3. Vérifie le lancement dans les logs ainsi que l'exécution des scripts

------ _BackEnd_ ----------

    1. Se positionner à la racine du projet sbea
    2. Lancer la commande mvn clean install
    3. Lancer la commande mvn spring-boot:run
    4. Vérifier dans la console que l'application a bien démarré