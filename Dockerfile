FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app

# Copiamos todo el proyecto (incluye mvnw y .mvn si existen)
COPY . .

RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre-jammy AS prod
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# ---------- STAGE 3: dev (imagen para desarrollo con hot-reload) ----------
FROM maven:3.9.4-eclipse-temurin-21 AS dev
WORKDIR /app

# Copiamos mvnw y .mvn para poder usar el wrapper; copiar todo ayuda si no montas el volumen
COPY . .

RUN chmod +x mvnw

EXPOSE 8081

# Nota: al usar docker-compose montando el código en /app, los cambios en la fuente se reflejan.
CMD ["sh", "-c", "if [ -x ./mvnw ] && [ -f ./.mvn/wrapper/maven-wrapper.properties ]; then ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev; else mvn -Dspring-boot.run.profiles=dev spring-boot:run; fi"]