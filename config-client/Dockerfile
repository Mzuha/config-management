FROM amazoncorretto:21-alpine
WORKDIR /app
COPY build/libs/config-client-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]