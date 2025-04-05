# Stage 1: Build the application
FROM maven:3.8.6-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 80

# Set environment variables (can be overridden at runtime)
ENV DB_HOST=127.0.0.1
ENV DB_PORT=5432
ENV DB_NAME=testdb
ENV DB_USER=postgres
ENV DB_PASSWORD=''
ENV KEYCLOAK_HOST=127.0.0.1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

