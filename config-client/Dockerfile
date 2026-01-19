FROM gradle:8.5-jdk21-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew :config-client:clean bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/config-client/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]