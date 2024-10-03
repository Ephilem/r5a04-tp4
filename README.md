# TP Implementation de micrservices avec Spring Boot
**Par : Raphael AIME 3A1**

## Introduction
Ce projet est un TP de microservices utilisant Spring Boot. L'objectif est de créer deux microservices :
- Un microservice qui permet de gérer des activités sportives.
- Un microservice qui permet de gérer des utilisateurs ainsi que l'authentification JWT.

**Le projet utilise la version 17 de Java.**

## Structure du projet
Le projet utilise la fonctionnalité de modules de Maven. Dans le répertoire principal, vous trouverez :
- `activity-service` : Le microservice qui gère les activités.
- `user-service` : Le microservice qui gère les utilisateurs.
- Un fichier `pom.xml` parent qui permet de construire et gérer les versions des dépendances.

## Configuration
Chaque microservice utilise des profils Spring (dev et prod) pour la configuration. Le profil "dev" est utilisé pour le développement local.

Les fichiers de configuration principaux sont :
- `application.properties` : Contient les valeurs par défaut.
- `application-dev.properties` : Configuration spécifique au profil de développement.
- `application-prod.properties` : Configuration spécifique au profil de production.

Ils se trouvent dans le dossier `resources` de chaque microservice. Les valeurs par défaut sont renseignées dans le fichier `application.properties`, et par rapport au profil, les valeurs spécifiques sont renseignées dans les fichiers `application-dev.properties` et `application-prod.properties` en priorité. Par exemple dans les deux microservices, vous pouvez voir que le port par défaut est 8080, mais pour le profil de développement, le port est 8081, et cela est défini dans le fichier `application-dev.properties` qui remplace la valeur par défaut. (Qui est 8080 ou la variable d'environnement `PORT`)

### Propriétés importantes de configuration

**Pour les deux microservices :**
- `server.port` : Le port sur lequel le serveur écoute.
- `spring.profiles.active` : Le profil actif (dev ou prod). (Est défini automatiquement par Maven si vous utiliser la commande `mvn spring-boot:run ...`)
- `spring.datasource.*` : Configuration de la base de données MariaDB.
- `jwt.secret` : La clé secrète pour les tokens JWT (doit être identique pour les deux microservices).

**Pour le microservice d'utilisateurs :**
- `admin.username` : Le nom d'utilisateur de l'administrateur créé automatiquement.
- `admin.email` : L'email de l'administrateur créé automatiquement.
- `admin.password` : Le mot de passe de l'administrateur créé automatiquement.
- `admin.auto-create` : Créer automatiquement un utilisateur administrateur au démarrage à partir des valeurs ci-dessus. (true ou false)

## Utilisation

Pour lancer les microservices, il suffit d'exécuter la commande suivante dans le dossier de chaque microservice :
```bash
mvn clean spring-boot:run -Dspring-boot.run.profiles=dev
```

Bien sur si vous voulez lancer la version de production, il suffit de changer le profil par `prod` :
```bash
mvn clean spring-boot:run -Dspring-boot.run.profiles=prod
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
Un fichier `postman.json` est disponible pour que vous puissiez tester les endpoints. Après avoir importé le fichier dans Postman, vous pouvez exécuter la collection de tests. (Vérifier bien les variable de collection que vous venez d'importer)
Dans ces variables, seul adminToken, activityId et userId sont PAS a modifier.
Pour que les tests fonctionnent, il faut faire en sorte qu'il y a un utilisateur avec comme nom d'utilisateur `admin` et comme mot de passe `admin` (par défaut). C'est l'utilisateur créé automatiquement au démarrage du microservice d'utilisateurs si la propriété `admin.auto-create` est à `true` et en laissant les valeurs par défaut.



## Package
Pour créer un package, il suffit de lancer la commande suivante dans le `pom` parent :
```
mvn clean package
```

Cela va créer les fichiers JAR dans les dossiers `target` des microservices.