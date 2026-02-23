FROM eclipse-temurin:21-jre-noble
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]