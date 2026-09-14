FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY ./target/*.jar app.jar
expose 8080
ENTRYPOINT ["java", "-jar", "app.jar"]