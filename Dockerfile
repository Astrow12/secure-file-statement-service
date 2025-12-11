# --------------------
# 1. Build Stage
# --------------------
FROM eclipse-temurin:21-jdk-jammy as builder

WORKDIR /build

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x mvnw
RUN ./mvnw -q -DskipTests package

# --------------------
# 2. Runtime Stage
# --------------------
FROM eclipse-temurin:21-jdk-jammy

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*


RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Set working directory
WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

EXPOSE 8080
EXPOSE 8081

# Setting JVM options for proper garbage collection and memory management
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport -XX:+UseG1GC"
# ENV SPRING_PROFILES_ACTIVE=prod

# Add Spring Boot actuator health check endpoint
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl --no-verbose --tries=1 --spider -f http://localhost:8081/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
