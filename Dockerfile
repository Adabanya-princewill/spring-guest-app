# Use an official Maven image to build the app
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy project files
COPY . .

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Build the application
RUN ./mvnw package -DskipTests

# Use a lightweight JRE to run the app
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
