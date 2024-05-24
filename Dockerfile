# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Add a volume pointing to /tmp
#VOLUME /tmp
# Set the working directory inside the container
WORKDIR /app


# The application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the container
COPY target/spring-boot-todo-app-0.0.1-SNAPSHOT.jar /app/spring-boot-todo-app-0.0.1-SNAPSHOT.jar

# Make port 8282 available to the world outside this container
EXPOSE 8282

# Run the jar file
ENTRYPOINT ["java","-jar","/spring-boot-todo-app-0.0.1-SNAPSHOT.jar"]