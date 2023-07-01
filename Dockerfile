FROM openjdk:17-alpine
WORKDIR /app
ADD ../build/libs/nms-0.0.1-SNAPSHOT.jar app/nms-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "app/nms-0.0.1-SNAPSHOT.jar"]
