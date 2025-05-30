# Stage 1: Build the application
FROM maven:3.9.5-eclipse-temurin-17 as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:25-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/assistant-service-0.0.1-SNAPSHOT.jar /app/assistant-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "assistant-service.jar"]