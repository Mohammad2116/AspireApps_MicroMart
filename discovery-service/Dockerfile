FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY ./target/*.jar app.jar
EXPOSE 8080
ENTERYPOINT ["java", "-jar", "app.jar"]