
# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from target folder
COPY target/*.jar app.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

