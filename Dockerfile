# --- Stage 1: Build ---
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# --- Stage 2: Runtime ---
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/ecommerce-app-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
