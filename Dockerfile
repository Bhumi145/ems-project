FROM eclipse-temurin:19-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

# 👇 important
ENTRYPOINT ["sh", "-c", "sleep 20 && java -jar app.jar"]
