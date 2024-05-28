# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the application's jar file into the container
COPY target/spring-boot-todo-app-0.0.1-SNAPSHOT.jar app.jar

# Make port 8282 available to the world outside this container
EXPOSE 8282

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
