# Addressbook Jenkins Maven Project

A modern, intentionally simple Addressbook application designed for learning and testing Jenkins CI/CD pipelines.

## Why this project exists

The original Vaadin Addressbook example is a useful small Java application, but its Maven configuration is based on an old Java 8/Vaadin 8 stack. This repository keeps the simple Addressbook idea while using a current Java and Spring Boot stack.

## Technology stack

- Java 25 (LTS)
- Spring Boot 4.x
- Maven
- Spring Web
- Jakarta Validation
- JUnit 5 / Spring Boot Test
- Embedded Tomcat

Java 25 is the current LTS release line as of this project update. Oracle lists JDK 25 updates including 25.0.4.1, released August 18, 2026. Java 26 is the current non-LTS feature release. See the official Oracle Java release notes for exact update levels.

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

The generated artifact will be under `target/`.

## Run the JAR

```bash
java -jar target/addressbook-jenkins-maven-project-1.0.0.jar
```

## Jenkins

This repository includes a Declarative `Jenkinsfile` designed for Jenkins training.

The pipeline demonstrates:

```text
Checkout
   ↓
Environment / Tool versions
   ↓
Build
   ↓
Test
   ↓
Archive artifact
   ↓
Deploy simulation
```

### Jenkins tools expected

Configure these global tools in Jenkins before running the pipeline:

```text
JDK:
  jdk-25

Maven:
  Maven-3

Agent label:
  ubuntu-agent
```

You can change the labels/tool names in the Jenkinsfile to match your Jenkins controller.

### Pipeline parameters

| Parameter | Purpose |
|---|---|
| `BRANCH` | Git branch to build |
| `ENVIRONMENT` | dev, staging, or production |
| `JAVA_VERSION` | Demonstrates the selected Java target/configuration |
| `RUN_TESTS` | Enable/disable tests |
| `ARCHIVE_ARTIFACT` | Enable/disable artifact archiving |

> Note: the actual Java runtime used by a Jenkins agent is selected by the Jenkins `tools` configuration. The `JAVA_VERSION` parameter is intentionally included as a teaching/demo parameter; it does not install or switch JDKs by itself.

## Suggested Jenkins exercises

1. Run a basic Maven build.
2. Add unit-test reporting.
3. Add artifact archiving.
4. Add branch parameters.
5. Add environment parameters.
6. Add `when` conditions for staging/production.
7. Add a manual `input` approval before production.
8. Split tests and packaging into parallel stages.
9. Run the pipeline on an Ubuntu Jenkins agent.
10. Add Docker image creation and deployment.

## Project layout

```text
addressbook-jenkins-maven-project/
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
    │   └── resources/static/index.html
    └── test/
        └── java/com/rohitrai/addressbook/ContactServiceTest.java
```

## Original project reference

This project is based on the idea and simple use case of the Vaadin Addressbook tutorial:

https://github.com/vaadin/addressbook

The original project uses Java 8-era dependencies and an old Vaadin/Jetty stack. This repository is intentionally modernized for current Jenkins and Java training.
