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
- `jwt.secret` : La clé secrète pour les tokens JWT (doit être identique pour les deux microservices).

- `spring.jpa.hibernate.ddl-auto` : La stratégie de génération de la base de données. (validate (par défaut), update, create, create-drop)
- `spring.jpa.properties.hibernate.dialect` : Le dialecte de la base de données.
- `spring.datasource.url` : L'URL de la base de données.
- `spring.datasource.username` : Le nom d'utilisateur de la base de données.
- `spring.datasource.password` : Le mot de passe de la base de données.

**Pour le microservice d'utilisateurs :**
- `admin.username` : Le nom d'utilisateur de l'administrateur créé automatiquement.
- `admin.email` : L'email de l'administrateur créé automatiquement.
- `admin.password` : Le mot de passe de l'administrateur créé automatiquement.
- `admin.auto-create` : Créer automatiquement un utilisateur administrateur au démarrage à partir des valeurs ci-dessus. (true ou false)

### Utilisation des variables d'environnement
Certaines partie de la configuration peuvent être définies par des variables d'environnement. Par exemple, le port du serveur peut être défini par la variable `PORT`. Si la variable est définie, elle remplace la valeur par défaut. Cela permet de définir des valeurs spécifiques à l'environnement sans avoir à modifier les fichiers de configuration.

Voici les autre variables d'environnement que vous pouvez définir :
- `JWT_SECRET` : La clé secrète pour les tokens JWT (`jwt.secret`).
- `ST_ADMIN_USERNAME` : Le nom d'utilisateur de l'administrateur créé automatiquement (`admin.username`).
- `ST_ADMIN_EMAIL` : L'email de l'administrateur créé automatiquement (`admin.email`).
- `ST_ADMIN_PASSWORD` : Le mot de passe de l'administrateur créé automatiquement (`admin.password`).
- `AUTO_CREATE_ADMIN` : Créer automatiquement un utilisateur administrateur au démarrage à partir des valeurs ci-dessus (`admin.auto-create`).
- `PORT` : Le port sur lequel le serveur écoute (`server.port`).
- `DB_HOST` : L'hôte de la base de données (`spring.datasource.url`).
- `DB_PORT` : Le port de la base de données (`spring.datasource.url`).
- `DB_NAME` : Le nom de la base de données (`spring.datasource.url`).
- `DB_USERNAME` : Le nom d'utilisateur de la base de données (`spring.datasource.username`).
- `DB_PASSWORD` : Le mot de passe de la base de données (`spring.datasource.password`).
- `DDL_AUTO` : La stratégie de génération de la base de données (`spring.jpa.hibernate.ddl-auto`).

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

Les ports définie pour le profil de développement sont :
- `activity-service` : 8081
- `user-service` : 8080

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
Dans ces variables, seul `adminToken`, `activityId` et `userId` sont __PAS__ a modifier.
Pour que les tests fonctionnent, il faut faire en sorte qu'il y a un utilisateur avec comme nom d'utilisateur `admin` et comme mot de passe `admin` (par défaut). C'est l'utilisateur créé automatiquement au démarrage du microservice d'utilisateurs si la propriété `admin.auto-create` est à `true` et en laissant les valeurs par défaut.



## Package
Pour créer un package, il suffit de lancer la commande suivante dans le `pom` parent :
```
mvn clean package
```

Cela va créer les fichiers JAR dans les dossiers `target` des microservices.