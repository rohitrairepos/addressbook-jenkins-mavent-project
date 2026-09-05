# Addressbook Jenkins Maven Project

A modern, intentionally simple Addressbook application designed for learning and testing Jenkins CI/CD pipelines.

The project keeps the simple idea of the original Vaadin Addressbook example, but uses a modern Java/Spring Boot stack and includes Docker and Jenkins CI/CD examples.

## Technology stack

- Java 25 (LTS)
- Spring Boot 4.x
- Maven
- Spring Web
- Jakarta Validation
- JUnit 5 / Spring Boot Test
- Embedded Tomcat
- Docker
- Docker Compose
- Jenkins Declarative Pipeline
- Docker Hub

Java 25 is the LTS Java line used by this project. Java 26 is the current non-LTS feature release.

## Application

The application provides a small REST API and a browser-based UI.

### REST endpoints

```text
GET    /api/contacts
GET    /api/contacts/{id}
GET    /api/contacts?search=rohit
POST   /api/contacts
PUT    /api/contacts/{id}
DELETE /api/contacts/{id}
```

### Run locally

```bash
java -version
mvn -version
mvn clean test
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

## Build the JAR

```bash
mvn clean package
```

The generated artifact is created under `target/`.

Run it with:

```bash
java -jar target/addressbook-jenkins-maven-project-1.0.0.jar
```

## Docker

The repository includes a multi-stage `Dockerfile` that builds the application with Maven and runs it on a Java 25 JRE image.

### Build the image

```bash
docker build -t addressbook-jenkins-maven-project:latest .
```

### Run the container

```bash
docker run -d \
  --name addressbook \
  -p 8080:8080 \
  addressbook-jenkins-maven-project:latest
```

Open:

```text
http://localhost:8080
```

### Docker Compose

Start the application with:

```bash
docker compose up --build
```

Stop it with:

```bash
docker compose down
```

## Docker Hub

The Jenkins pipeline is configured to build and push the application image to the Docker Hub repository:

```text
rohitdocker13/deopslabrepo
```

Example image:

```text
rohitdocker13/deopslabrepo:latest
```

You can supply a custom tag from Jenkins, for example:

```text
IMAGE_TAG=1.0.0
```

which produces:

```text
rohitdocker13/deopslabrepo:1.0.0
```

### Docker Hub authentication

The Jenkins pipeline does not store Docker Hub credentials in source control.

Create a Jenkins credential with:

```text
Kind: Username with password
ID: dockerhub-credentials
Username: rohitdocker13
Password: Docker Hub access token
```

The pipeline uses `docker login --password-stdin`, pushes the image, and logs out afterward.

## Jenkins

This repository includes a Declarative `Jenkinsfile` designed for Jenkins training.

The current pipeline flow is:

```text
Checkout
   ↓
Environment
   ↓
Build
   ↓
Test
   ↓
Archive
   ↓
Docker Build
   ↓
Docker Push
   ↓
Deploy
```

### Jenkins tools expected

Configure these tools in Jenkins:

```text
JDK:
  jdk-25

Maven:
  Maven-3

Agent label:
  ubuntu-agent
```

The Ubuntu Jenkins agent must also have Docker installed and allow the Jenkins user to run Docker commands.

### Pipeline parameters

| Parameter | Purpose |
|---|---|
| `BRANCH` | Git branch to build |
| `ENVIRONMENT` | `dev`, `staging`, or `production` |
| `RUN_TESTS` | Enable or disable Maven tests |
| `BUILD_DOCKER` | Enable or disable Docker image creation |
| `PUSH_DOCKER` | Enable or disable Docker Hub push |
| `IMAGE_TAG` | Tag used for the Docker image |

### Example Jenkins values

```text
BRANCH=master
ENVIRONMENT=dev
RUN_TESTS=true
BUILD_DOCKER=true
PUSH_DOCKER=true
IMAGE_TAG=latest
```

The resulting Docker image is:

```text
rohitdocker13/deopslabrepo:latest
```

## Jenkins pipeline details

### Checkout

The pipeline checks out the selected Git branch from this repository.

### Environment

The pipeline prints the Java, Maven and Docker versions and the selected deployment environment.

### Build

Maven creates the executable Spring Boot JAR:

```bash
mvn -B -DskipTests clean package
```

### Test

When `RUN_TESTS=true`, Maven tests are executed and Jenkins publishes the JUnit XML reports.

### Archive

The generated JAR is archived by Jenkins so it can be downloaded from the build record.

### Docker Build

When `BUILD_DOCKER=true`, Jenkins builds:

```text
rohitdocker13/deopslabrepo:${IMAGE_TAG}
```

### Docker Push

When both `BUILD_DOCKER=true` and `PUSH_DOCKER=true`, Jenkins authenticates with Docker Hub using the `dockerhub-credentials` Jenkins credential and pushes the image.

### Deploy

The current `Deploy` stage is intentionally a training/demo deployment. It copies the JAR into an environment-specific directory rather than deploying to a real server.

This makes it safe to use while learning Jenkins. It can later be replaced with:

```text
Dev server
   ↓
Staging server
   ↓
Production server
```

or with container deployment using Docker Compose, SSH, Kubernetes, or another deployment target.

## Project layout

```text
addressbook-jenkins-maven-project/
├── .dockerignore
├── Application.yaml
├── Dockerfile
├── docker-compose.yml
├── docker-compose.prod.yml
├── Directly on server
├── Jenkinsfile
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/rohitrai/addressbook/
    │   │   ├── AddressbookApplication.java
    │   │   ├── controller/ContactController.java
    │   │   ├── model/Contact.java
    │   │   └── service/ContactService.java
    │   └── resources/
    │       ├── application.properties
    │       └── static/index.html
    └── test/
        └── java/com/rohitrai/addressbook/ContactServiceTest.java
```

## Original project reference

The project is based on the simple use case of the Vaadin Addressbook tutorial:

https://github.com/vaadin/addressbook

The original project uses a Java 8-era Vaadin/Jetty stack. This repository intentionally modernizes that idea for current Java, Docker and Jenkins training.

## Suggested Jenkins exercises

1. Run a basic Maven build.
2. Run unit tests and publish JUnit reports.
3. Archive the JAR artifact.
4. Build a Docker image.
5. Push the image to a private Docker Hub repository.
6. Add branch parameters.
7. Add environment parameters.
8. Add `when` conditions for staging and production.
9. Add manual approval before production deployment.
10. Deploy the Docker image to a remote server.
11. Add Docker Compose deployment.
12. Move deployment to Kubernetes.
