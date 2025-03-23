# ====== Stage 1: Build Stage ======
FROM eclipse-temurin:21-jdk AS builder
LABEL maintainer="prakash.rungta@gmail.com"

# Set working directory
WORKDIR /app

# Copy Maven Wrapper & POM first to leverage caching
COPY ./mvnw ./pom.xml ./
COPY .mvn .mvn

# Download dependencies separately to optimize caching
RUN ./mvnw dependency:go-offline -B

# Copy source code & build the application
COPY ./src src
RUN ./mvnw clean package -DskipTests

# ====== Stage 2: Minimal Runtime Image ======
FROM eclipse-temurin:21-jre
LABEL maintainer="prakash.rungta@gmail.com"

# Set working directory
WORKDIR /app

# Use non-root user for security
RUN useradd -m springuser
USER springuser

# Copy only the built JAR from the builder stage
COPY --from=builder /app/target/cp-order-service-0.0.1.jar cp-order-service.jar

# Expose port
EXPOSE 8080

## Define health check (requires Spring Boot Actuator)
#HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
#  CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM Performance Optimizations
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "cp-order-service.jar"]
