# Etapa 1: Build con Maven y Java 21
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Imagen final (más liviana)
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/Alexander-0.0.1-SNAPSHOT.jar /app/alexander.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/alexander.jar"]
