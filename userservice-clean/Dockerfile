FROM openjdk:22-jdk-bullseye
WORKDIR /app
COPY target/userservice-clean-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]