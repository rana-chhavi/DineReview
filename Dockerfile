# Use a lightweight OpenJDK 21 image as the base
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot jar into the container
COPY target/restaurant-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on (change if needed)
EXPOSE 8080

# Set the entrypoint to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
