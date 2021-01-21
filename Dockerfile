FROM openjdk:14-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY build/libs/*.jar home/spring/application.jar
ENTRYPOINT ["java", "-jar", "/home/spring/application.jar"]