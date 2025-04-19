# Dockerfile
FROM eclipse-temurin:17-jdk
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

#./mvnw clean package -DskipTests
#mvn clean package -DskipTests
#docker-compose up --build
