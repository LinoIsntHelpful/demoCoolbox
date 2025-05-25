# Stage 1: build con Maven e Java 21
FROM maven:3.9.4-openjdk-21 AS build
WORKDIR /app
COPY . .
# Impostiamo il wrapper Maven eseguibile
RUN chmod +x mvnw
# Compiliamo il jar senza test
RUN ./mvnw clean package -DskipTests

# Stage 2: runtime con JRE Java 21
FROM openjdk:21-jdk-slim
WORKDIR /app
# Copiamo il jar appena creato
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]
