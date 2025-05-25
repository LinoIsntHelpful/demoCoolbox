# Stage 1: build con Maven
FROM maven:3.8.7-jdk-17 AS build
WORKDIR /app
COPY . .
# Assicuriamoci che il wrapper sia eseguibile
RUN chmod +x mvnw
# Compiliamo il jar senza test
RUN ./mvnw clean package -DskipTests

# Stage 2: runtime con JRE leggero
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copiamo il jar appena creato
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]
