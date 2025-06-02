# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-21-alpine as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:25-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/assistant-service-0.0.1-SNAPSHOT.jar /app/assistant-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "assistant-service.jar", "--spring.profiles.active=prod"]