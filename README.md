<p align="center">
  <a href="https://throyer-crud-api.herokuapp.com" target="blank"><img src="./assets/demo.gif" alt="Demonstration" /></a>
</p>

<h1 align="center">Spring Boot API CRUD</h1>
<p align="center">
  Um cadastro de usuários completo, com permissões de acesso, token JWT testes de integração e unitários, no padrão API RESTful.
</p>
<br>
<br>

[**Live demo on heroku**](https://throyer-crud-api.herokuapp.com)

## Sumario

- [Features](#features)
- [Requisitos](#requisitos)
- [Entidades](#entidades)
- [Instalação](#instalação)
- [Rodando um teste especifico](#rodando-um-teste-especifico)
- [Documentação do Swagger](#documentação-do-swagger)
- [Postman](#postman)
- [Database Migrations](#database-migrations)
- [Variaveis de ambiente](#variáveis-de-ambiente)

# Features

<p align="center">
  <a href="https://throyer-crud-api.herokuapp.com" target="blank"><img src="https://i.imgur.com/YWjBtvG.png"  alt="Tecnologias" /></a>
</p>



## Requisitos

- MariaDB: `^10.6.1`
- Java: `^17`
> recomendo a instalação do maven localmente, mas o projeto tem uma versão portatil nos arquivos [`mvnw`](./mvnw) e [`mvnw.cmd`](./mvnw.cmd)

Esse projeto foi configurado com [Spring Initializr](https://start.spring.io/).

## Entidades

<p>
  <img src="./database_diagram/spring_boot_crud_database_diagram.png" alt="database diagram" />
</p>

> arquivo do [draw.io](./der/spring_boot_crud_database_diagram.drawio)

## Instalação

> Caso tiver o maven instalado localmente substitua `mvnw` por `mvn` (_para usuários do zsh adicione o comando `bash` antes de mvnw_)


```shell
# Clone o repositório e acesse o diretório.
$ git clone git@github.com:Throyer/springboot-api-crud.git && cd springboot-api-crud

# Baixe as dependencias (o parametro -DskipTests pula os testes)
$ mvnw install -DskipTests

# Rode a aplicação
$ mvnw spring-boot:run

# Para rodar os testes
$ mvnw test

# Para buildar para produção
$ mvnw clean package

# Para gerar o relatório de cobertura apos os testes (fica disponível em: target/site/jacoco/index.html)
$ mvnw jacoco:report
```


## Rodando um teste especifico
use o parâmetro `-Dtest=<Classe>#<metodo>`


por exemplo o teste de integração de criação usuário:
```
$ mvnw test -Dtest=UsuariosControllerIntegrationTests#should_save_a_new_user
```


## Documentação do Swagger
Assim que a aplicação estiver de pé, fica disponível em: [localhost:8080/documentation](localhost:8080/documentation)


[exemplo no heroku](https://throyer-crud-api.herokuapp.com/documentation)

## Postman
>Clique [**aqui**](./postman/crud_api.postman_collection.json) para acessar o aquivo `json` da coleção do postman.
>
>> _🚨 this file deprecated!_

<br>
<br>

---

## Database Migrations
Criando arquivos de arquivos de migração

- Java based migrations
  ```bash
  mvn migration:generate -Dname=my-migration-name
  ```

- SQL based migrations
  ```bash
  mvn migration:generate -Dname=my-migration-name -Dsql
  ```

---

## Variáveis de ambiente

| **Descrição**                               | **parâmetro**                          | **Valor padrão**          |
| ------------------------------------------- | -------------------------------------- | ------------------------- |
| porta da aplicação                          | `SERVER_PORT`                          | 8080                      |
| url do banco                                | `DB_URL`                               | localhost:3306/common_app |
| nome de usuário (banco)                     | `DB_USERNAME`                          | root                      |
| senha do usuário (banco)                    | `DB_PASSWORD`                          | root                      |
| mostrar sql na saida                        | `DB_SHOW_SQL`                          | false                     |
| máximo de conexões com o banco              | `DB_MAX_CONNECTIONS`                   | 5                         |
| valor do secret na geração dos tokens       | `TOKEN_SECRET`                         | secret                    |
| tempo de expiração do token em horas        | `TOKEN_EXPIRATION_IN_HOURS`            | 24                        |
| tempo de expiração do refresh token em dias | `REFRESH_TOKEN_EXPIRATION_IN_DAYS`     | 7                         |
| endereço do servidor smtp                   | `SMTP_HOST`                            | smtp.gmail.com            |
| porta do servidor smtp                      | `SMTP_PORT`                            | 587                       |
| nome de usuário smtp                        | `SMTP_USERNAME`                        | user                      |
| senha do servidor smtp                      | `SMTP_PASSWORD`                        | secret                    |

> são definidas em: [**application.properties**](./src/main/resources/application.properties)
>
> ```shell
> # para mudar o valor de alguma variável de ambiente
> # na execução basta passar ela como parâmetro. (como --SERVER_PORT=80 por exemplo).
> $ java -jar api-3.0.3.RELEASE.jar --SERVER_PORT=80
> ```
>
> > [Todas opções do `aplication.properties` **padrões** no Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).
> >
> > [Todas **funcionalidades** do Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html).
