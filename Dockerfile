FROM openjdk:8-jdk-alpine
ADD target/dps.jar dps.jar
expose 8080
ENTRYPOINT ["java","-jar","dps.jar"]
