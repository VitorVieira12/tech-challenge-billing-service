FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]
