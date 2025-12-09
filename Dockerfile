FROM eclipse-temurin:21-jdk-jammy

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*


RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Set working directory
WORKDIR /app

# Copy Maven/Gradle build artifact into container
# Assuming the jar is in target folder and named app.jar
COPY target/statementapi.jar app.jar


# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

EXPOSE 8080
EXPOSE 8081

# Setting JVM options for proper garbage collection and memory management
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport -XX:+UseG1GC"
ENV SPRING_PROFILES_ACTIVE=prod

# Add Spring Boot actuator health check endpoint
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl --no-verbose --tries=1 --spider -f http://localhost:8081/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
