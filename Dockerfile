# Stage 1: build con Maven e Java 21
FROM maven:3.9.4-jdk-21-slim AS build
WORKDIR /app
COPY . .
# Compiliamo il jar senza test usando Maven installato nell'immagine
RUN mvn clean package -DskipTests

# Stage 2: runtime con JRE Java 21
FROM openjdk:21-jdk-slim
WORKDIR /app
# Copiamo il jar appena creato
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]
