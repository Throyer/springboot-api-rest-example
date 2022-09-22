>[🇧🇷 In Portuguese](https://github.com/Throyer/springboot-api-crud/blob/master/assets/readme.md#spring-boot-api-crud)
>
> [🐬 MySQL/MariaDB (outdated) implementation](https://github.com/Throyer/springboot-api-crud/tree/mariadb#readme)

<p align="center">
  <a href="https://github.com/Throyer" target="blank"><img src="./assets/images/tecnologias.png" width="560" alt="Tecnologias" /></a>
</p>

<h1 align="center">Spring Boot API RESTful</h1>
<p align="center">
  A complete user registry, with access permissions, JWT token, integration and unit tests, using the RESTful API pattern.
</p>

<br>
<br>

[**Live demo on heroku**](https://throyer-crud-api.herokuapp.com)

<p align="center">
  <a href="https://throyer-crud-api.herokuapp.com" target="blank"><img src="./assets/images/demo.gif" alt="Demonstration" /></a>
</p>

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Entities](#entities)
- [Installation](#installation)
- [Running a specific test](#tests)
- [Swagger](#swagger)
- [Database Migrations](#database-migrations)
- [Docker](#docker-examples)
- [Environment variables](#environment-variables)

# Features

<p align="center">
  <a href="https://throyer-crud-api.herokuapp.com" target="blank"><img src="./assets/images/features.png"  alt="Tecnologias" /></a>
</p>



## Requirements

- Postgres: `^13`
- Java: `^17`
- Maven: `^3.8.4`

This project was started with [Spring Initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.6.2&packaging=jar&jvmVersion=17&groupId=com.github.throyer.common&artifactId=api&name=api&description=CRUD%20API&packageName=com.github.throyer.common.api&dependencies=devtools,lombok,web,security,thymeleaf,mysql,h2,data-jpa,flyway,jooq).

## Entities

<p>
  <img src="./assets/database/diagram.png" alt="database diagram" />
</p>

>[🚨 draw.io file here](./assets/database/diagram.drawio)

## Installation
> 🚨 check [requirements](#requirements) or if you are using docker check [docker development instructions](#docker-examples)

- clone the repository and access the directory.
  ```shell
  git clone git@github.com:Throyer/springboot-api-crud.git crud && cd crud
  ```
- download dependencies
  ```shell
  mvn -f api/pom.xml install -DskipTests
  ```
- run the application 
  - home http://localhost:8080
  - api http://localhost:8080/api
  - swagger http://localhost:8080/docs
  ```shell
  mvn -f api/pom.xml spring-boot:run
  ```
- running the tests
  ```shell
  mvn -f api/pom.xml test
  ```
- to build for production
  ```shell
  mvn -f api/pom.xml clean package
  ```
- to generate the coverage report after testing `(available at: api/target/site/jacoco/index.html)`
  ```shell
  mvn -f api/pom.xml jacoco:report
  ```

## Tests
[![Coverage Status](https://coveralls.io/repos/github/Throyer/springboot-api-crud/badge.svg?branch=master)](https://coveralls.io/repos/github/Throyer/springboot-api-crud/badge.svg?branch=master)

## Running a specific test
use the parameter `-Dtest=<class>#<method>`

- for example the integration test. creating a user:
  ```shell
  mvn -f api/pom.xml test -Dtest=UsersControllerTests#should_save_a_new_user
  ```


## Swagger
Once the application is up, it is available at: [localhost:8080/docs](http://localhost:8080/docs)
> 🚨 if you set `SWAGGER_USERNAME` and `SWAGGER_PASSWORD` on `.env` file this route require authentication

[example on heroku](https://throyer-crud-api.herokuapp.com/docs)

---

## Database Migrations
Creating database migration files

> 🚨 check [requirements](#requirements)
>
> if you using docker-compose
> ```
> .docker/scripts/mvn migration:generate -Dname=my-migration-name
> ```

- Java based migrations
  ```bash
  mvn -f api/pom.xml migration:generate -Dname=my-migration-name
  ```

- SQL based migrations
  ```bash
  mvn -f api/pom.xml migration:generate -Dname=my-migration-name -Dsql
  ```

---

## Docker examples

> 🚨 create `environment` file and add permission to execute scripts
>
> ```shell
> cp .docker/.env.example .docker/.env && chmod -R +x .docker/scripts
> ```

- docker-compose for development
  - starting containers
  ```
  .docker/scripts/develop up -d
  ```
  
  - removing contaiers
  ```
  .docker/scripts/develop down
  ```

  - show backend logs
  ```
  .docker/scripts/develop logs -f api
  ```

- docker-compose for production
  ```
  .docker/scripts/production up -d --build
  ```

  ```
  .docker/scripts/production down
  ```

## Environment variables

| **Description**                          | **Parameter**                      | **Default values**        |
|------------------------------------------|------------------------------------|---------------------------|
| server port                              | `SERVER_PORT`                      | 8080                      |
| database host                            | `DB_HOST`                          | localhost                 |
| database port                            | `DB_PORT`                          | 5432                      |
| database name                            | `DB_NAME`                          | example                   |
| database username                        | `DB_USERNAME`                      | root                      |
| database user password                   | `DB_PASSWORD`                      | root                      |
| displays the generated sql in the logger | `DB_SHOW_SQL`                      | false                     |
| set maximum database connections         | `DB_MAX_CONNECTIONS`               | 5                         |
| secret value in token generation         | `TOKEN_SECRET`                     | secret                    |
| token expiration time in hours           | `TOKEN_EXPIRATION_IN_HOURS`        | 24                        |
| refresh token expiry time in days        | `REFRESH_TOKEN_EXPIRATION_IN_DAYS` | 7                         |
| SMTP server address                      | `SMTP_HOST`                        | smtp.gmail.com            |
| SMTP server port                         | `SMTP_PORT`                        | 587                       |
| SMTP username                            | `SMTP_USERNAME`                    | user                      |
| SMTP server password                     | `SMTP_PASSWORD`                    | secret                    |
| time for recovery email to expire        | `MINUTES_TO_EXPIRE_RECOVERY_CODE`  | 20                        |
| max requests per minute                  | `MAX_REQUESTS_PER_MINUTE`          | 50                        |
| swagger url                              | `SWAGGER_URL`                      | /docs                     |
| swagger username                         | `SWAGGER_USERNAME`                 | `null`                    |
| swagger password                         | `SWAGGER_PASSWORD`                 | `null`                    |

> these variables are defined in: [**application.properties**](./src/main/resources/application.properties)
>
> ```shell
> # to change the value of some environment variable at runtime
> # on execution, just pass it as a parameter. (like --SERVER_PORT=80).
> $ java -jar api-4.1.2.jar --SERVER_PORT=80
> ```


> [All options of `aplication.properties` here](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).
>
> [All **features** of Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html).
