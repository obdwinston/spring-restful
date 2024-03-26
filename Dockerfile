FROM --platform=linux/amd64 openjdk:17-jdk-alpine
MAINTAINER database.com
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]