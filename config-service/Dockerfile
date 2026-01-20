FROM gradle:8.5-jdk21-alpine AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY config-client/build.gradle.kts config-client/
COPY config-service/build.gradle.kts config-service/

RUN ./gradlew :config-service:dependencies --no-daemon

COPY config-service/src config-service/src
RUN ./gradlew :config-service:bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/config-service/build/libs/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
