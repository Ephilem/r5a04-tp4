# TP Microservice Spring Boot
**Par : Raphael AIME 2C1**

## Introduction
Ce projet est un TP de microservices utilisant Spring Boot. L'objectif est de créer deux microservices :
- Un microservice qui permet de gérer des activités sportives.
- Un microservice qui permet de gérer des utilisateurs ainsi que l'authentification JWT.

## Structure du projet
Pour simplifier le développement, j'ai décidé d'utiliser la fonctionnalité de modules de Maven.
Dans le fichier source à décompresser, vous trouverez deux dossiers :
- `activity-service` : Le microservice qui gère les activités.
- `user-service` : Le microservice qui gère les utilisateurs.
  Dans le même répertoire, vous trouverez un fichier `pom.xml` qui permet de construire et gérer les versions des dépendances.

## Utilisation
Chaque microservice utilise un fichier `application.properties` pour stocker les informations de configuration. Les valeurs par défaut sont renseignées dans le dossier `resources` des microservices respectifs.

Voici les propriétés importantes pour chaque microservice :
- `jwt.secret` : Doit obligatoirement être définie pour les deux microservices. C'est la clé secrète utilisée pour générer et vérifier les tokens JWT. Attention, elle doit être identique pour les deux microservices.
- `server.port` : Le port sur lequel le serveur écoute.
- `activity-data.json.path` : Le chemin vers le fichier JSON contenant les activités (uniquement pour le microservice d'activités).
- `user-data.json.path` : Le chemin vers le fichier JSON contenant les utilisateurs (uniquement pour le microservice d'utilisateurs).

Pour lancer les microservices, il suffit d'exécuter la commande suivante dans le dossier de chaque microservice :
```
mvn clean spring-boot:run
```
![console.png](screenshot/console.png)

Les ports par défaut sont les suivants :
- `activity-service` : 8080
- `user-service` : 8081

## Fonctionnalités
- CRUD sur les activités. (Pour le microservice d'activités)
- CRUD sur les utilisateurs. (Pour le microservice d'utilisateurs)
- Authentification JWT.
- Pagination des résultats. (Pour les deux microservices)
- Filtrage des résultats. (Pour les deux microservices)
- Tri des résultats. (Pour les deux microservices)

## Swagger
Chaque microservice utilise Swagger pour documenter les endpoints. Pour accéder à la documentation, il suffit de se rendre à l'URL suivante :
- `<url du microservice>/openapi/swagger-ui/index.html`

## Tests
Pour les tests, j'ai utilisé un outil utilisé en R4.02 : Postman.
J'ai pu créer une série de tests pour chaque microservice.
![postman.png](./screenshot/postman.png)
Un fichier `postman.json` est disponible pour que vous puissiez tester les endpoints.

## Package
Pour créer un package, il suffit de lancer la commande suivante dans le `pom` parent :
```
mvn clean package
```

Cela va créer les fichiers JAR dans les dossiers `target` des microservices.