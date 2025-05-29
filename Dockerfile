# Hey, I'm using java 21 btw
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn clean package

FROM eclipse-temurin:21-jre 

WORKDIR /app

# Copy the packaged JAR from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Expose the port the app will run on
EXPOSE 8081
