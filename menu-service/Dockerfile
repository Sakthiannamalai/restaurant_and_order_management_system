FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/menu-service-*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
