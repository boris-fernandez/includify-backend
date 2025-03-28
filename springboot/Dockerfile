FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/springboot-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_springboot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_springboot.jar"]