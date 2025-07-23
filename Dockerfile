# Stage 1: Build the application using Maven
FROM maven:3.9-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to download dependencies first.
# This leverages Docker's layer caching, so dependencies are not re-downloaded
# unless pom.xml changes.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of your application's source code
COPY src ./src

# Package the application, skipping the tests during the build
RUN mvn clean package -DskipTests

# ---

# Stage 2: Create the final, smaller image for running the application
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the built .jar file from the 'build' stage to the final image
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Set the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]