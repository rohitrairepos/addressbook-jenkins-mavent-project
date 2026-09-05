# Multi-stage Docker build for the Addressbook application.
# Build with Maven and run with a lightweight Java 25 runtime.

FROM maven:3.9.11-eclipse-temurin-25 AS builder

WORKDIR /build

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=builder /build/target/addressbook-jenkins-maven-project-1.0.0.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
