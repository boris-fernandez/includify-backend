FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app_springboot_backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_springboot_backend.jar"]