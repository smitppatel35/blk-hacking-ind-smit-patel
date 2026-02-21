# docker build -t blk-hacking-ind-smit-patel .
# docker run -p 5477:5477 blk-hacking-ind-smit-patel

FROM maven:3.9.6-eclipse-temurin-21 AS build
LABEL authors="Smit Patel"

WORKDIR /app

# Copy pom.xml and download dependencies first (layer caching)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Expose API port as required by BlackRock Challenge
EXPOSE 5477

# Copy only the packaged jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]