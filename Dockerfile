FROM openjdk:17-slim
WORKDIR /app
EXPOSE 8080
COPY target/*.jar /app/online-grocery-store-app.jar
CMD ["java", "-jar", "online-grocery-store-app.jar"]