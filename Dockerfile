FROM openjdk:23-ea-17-jdk-bullseye

WORKDIR /dockerApp

COPY /target/ToDoList-0.0.1-SNAPSHOT.jar /dockerApp/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]     


