# Utiliza una imagen oficial de Maven con Java 21 para construir la app
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Utiliza una imagen ligera de Java 21 para ejecutar la app
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 35000
CMD ["java", "-jar", "app.jar"]
