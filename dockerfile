FROM maven:3.8.3

WORKDIR /app

ENTRYPOINT ["mvn", "spring-boot:run"]