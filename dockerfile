FROM openjdk:16

WORKDIR /app

COPY /target/api-3.0.0.jar /app/app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]