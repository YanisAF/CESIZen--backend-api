# CESIZen--backend-api

## Description du Projet

Ce projet est le backend de l'application CESIZen, développé avec Spring Boot. Il fournit une API RESTful pour gérer les utilisateurs, l'authentification, les quiz, les pages de contenu et d'autres fonctionnalités nécessaires à l'application.

## Technologies Utilisées

*   **Spring Boot**: Framework pour le développement rapide d'applications Java.
*   **Java 21**: Langage de programmation.
*   **Maven**: Outil de gestion de projet et de construction.
*   **Spring Data JPA**: Pour l'accès aux données et la persistance avec Hibernate.
*   **PostgreSQL**: Base de données relationnelle.
*   **Spring Security & JWT**: Pour l'authentification et l'autorisation sécurisées.
*   **Spring Mail & Thymeleaf**: Pour l'envoi d'e-mails (par exemple, réinitialisation de mot de passe).
*   **Lombok**: Pour réduire le code boilerplate.
*   **Springdoc OpenAPI UI**: Pour la documentation interactive de l'API (Swagger UI).
*   **Docker & Docker Compose**: Pour la conteneurisation et le déploiement facilité.

## Prérequis

Avant de démarrer l'API, assurez-vous d'avoir installé les éléments suivants :

*   **Java Development Kit (JDK) 21** ou supérieur.
*   **Apache Maven** 3.6.0 ou supérieur.
*   **Docker** et **Docker Compose** (si vous souhaitez utiliser la conteneurisation).
*   Une instance de base de données **PostgreSQL**.

## Configuration

Le projet utilise un fichier `application.yml` pour la configuration. Les informations sensibles (base de données, clés JWT, informations de messagerie) sont gérées via des variables d'environnement.

### Variables d'Environnement

Vous devrez définir les variables d'environnement suivantes :

*   `DB_URL`: URL de connexion à la base de données PostgreSQL (ex: `jdbc:postgresql://localhost:5432/cesizen_db`).
*   `USERNAME_DB`: Nom d'utilisateur de la base de données.
*   `PASSWORD_DB`: Mot de passe de la base de données.
*   `jwt_secret_key`: Clé secrète pour la signature des tokens JWT.
*   `jwt_secret_reset`: Clé secrète pour la réinitialisation des mots de passe JWT.
*   `USERNAME_MAIL`: Nom d'utilisateur pour l'envoi d'e-mails (ex: adresse Gmail).
*   `PASSWORD_MAIL`: Mot de passe de l'application pour l'envoi d'e-mails.
*   `VAULT_HOST`, `VAULT_PORT`, `VAULT_SCHEME`, `VAULT_TOKEN`, `default_context_vault`: Variables liées à HashiCorp Vault si utilisé pour la gestion des secrets. Si non utilisé, ces variables peuvent être omises ou configurées différemment.

Pour un environnement de développement local, vous pouvez créer un fichier `.env` ou définir ces variables directement dans votre shell.

## Démarrage de l'API

Il existe deux méthodes principales pour démarrer l'API :

### 1. Démarrage Local avec Maven

1.  **Cloner le dépôt** (si ce n'est pas déjà fait) :
    ```bash
    git clone <URL_DU_DEPOT>
    cd CESIZen--backend-api
    ```
2.  **Installer les dépendances Maven** :
    ```bash
    mvn clean install
    ```
3.  **Définir les variables d'environnement** (voir section Configuration ci-dessus).
4.  **Démarrer l'application Spring Boot** :
    ```bash
    mvn spring-boot:run
    ```
    L'API sera accessible sur `http://localhost:8080`.

### 2. Démarrage avec Docker Compose

Cette méthode est recommandée pour un environnement de développement complet, car elle inclut également une instance PostgreSQL.

1.  **Cloner le dépôt** (si ce n'est pas déjà fait) :
    ```bash
    git clone <URL_DU_DEPOT>
    cd CESIZen--backend-api
    ```
2.  **Créer un fichier `.env`** à la racine du projet (`CESIZen--backend-api/`) et y ajouter les variables d'environnement requises pour la base de données et l'API. Exemple :
    ```dotenv
    # Variables pour la base de données PostgreSQL
    POSTGRES_USER=cesizenuser
    POSTGRES_PASSWORD=cesizenpassword
    POSTGRES_DB=cesizendb

    # Variables pour l'API Spring Boot
    DB_URL=jdbc:postgresql://postgres:5432/cesizendb
    USERNAME_DB=cesizenuser
    PASSWORD_DB=cesizenpassword
    jwt_secret_key=VotreCleSecreteJWT
    jwt_secret_reset=VotreCleSecreteResetJWT
    USERNAME_MAIL=votre_email@gmail.com
    PASSWORD_MAIL=votre_mot_de_passe_application
    # Variables Vault (si utilisées)
    # VAULT_HOST=...
    # VAULT_PORT=...
    # VAULT_SCHEME=...
    # VAULT_TOKEN=...
    # default_context_vault=...
    ```
    **Important**: Remplacez les valeurs de `PASSWORD_MAIL`, utilisez un mot de passe d'application Gmail avec l'authentification à deux facteurs.

3.  **Créer le réseau nécessaire à la communication entre l'API et le backoffice web. Construire et démarrer les services Docker** :
    
    ```bash
    docker network create app-network || true
    ```

    ```bash
    docker-compose up -d --build
    ```
    Ceci va construire l'image Docker de l'API, démarrer le conteneur de l'API et un conteneur PostgreSQL. L'API sera accessible sur `http://localhost:8080`.

4.  **Arrêter les services Docker** :
    ```bash
    docker-compose down
    ```

## Endpoints Clés (Exemples)

Voici quelques exemples d'endpoints disponibles. La documentation complète est accessible via Swagger UI.

*   **Enregistrement d'utilisateur** :
    *   `POST /api/v1/auth/register`
    *   `POST /api/v1/auth/register-admin`
*   **Connexion** :
    *   `POST /api/v1/auth/login`
*   **Vérification de l'état du backend** :
    *   `GET /api/v1/auth/backend-up`

La documentation interactive de l'API (Swagger UI) sera disponible à l'adresse `http://localhost:8080/swagger-ui.html` une fois l'API démarrée.

## Structure du Projet

Le projet suit une structure Spring Boot standard :

```
CESIZen--backend-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/CESIZen/
│   │   │   ├── controller/     # Contrôleurs REST
│   │   │   ├── service/        # Logique métier
│   │   │   ├── repository/     # Accès aux données (JPA)
│   │   │   ├── model/          # Entités de la base de données
│   │   │   ├── dto/            # Objets de transfert de données
│   │   │   ├── config/         # Configurations diverses
│   │   │   └── ...
│   │   └── resources/          # Fichiers de configuration (application.yml), templates, etc.
│   └── test/                   # Tests unitaires et d'intégration
├── pom.xml                     # Fichier de configuration Maven
├── Dockerfile                  # Dockerfile pour construire l'image de l'API
├── docker-compose.yml          # Fichier Docker Compose pour le déploiement
└── README.md                   # Ce fichier
```