# Etapa 1: construir el JAR
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

COPY . .

# 🔧 Dar permisos de ejecución al wrapper de Maven
RUN chmod +x mvnw

# Ejecutar Maven
RUN ./mvnw clean package -DskipTests

# Etapa 2: ejecutar la aplicación
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]