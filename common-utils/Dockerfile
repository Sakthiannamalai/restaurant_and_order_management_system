FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/common-utils-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

