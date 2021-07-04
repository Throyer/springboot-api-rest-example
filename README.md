<p align="center">
  <a href="https://github.com/Throyer" target="blank"><img src="./assets/tecnologias.png" width="560" alt="Tecnologias" /></a>
</p>

<h1 align="center">Spring Boot API CRUD</h1>
<p align="center">
  Um cadastro de usuários completo, com permissões de acesso, token JWT testes de integração e unitários, no padrão API RESTful.
</p>
<br>
<br>

## Sumario

- [Motivação](#motivação)
- [Próximos passos](#o-que-foi-feito-e-os-proximos-passos)
- [Requisitos](#requisitos)
- [Entidades](#entidades)
- [Instalação](#instalação)
- [Rodando um teste especifico](#rodando-um-teste-especifico)
- [Documentação do Swagger](#documentação-do-swagger)
- [Postman](#postman)
- [Database Migrations](#database-migrations)
- [Variaveis de ambiente](#variaveis-de-ambiente)

# Motivação

<p>
  A ideia desse repositório é a criação de uma api em spring boot,
  com o máximo possível de boas praticas e o mais completa que eu conseguir,
  para servir como uma base para min no futuro, ou para outras pessoas
  que estiverem buscando um guia para a construção de uma api com Spring Boot.
  Qualquer pessoa que quiser contribuir ou usar esse projeto é bem vinda.
</p>

# O que foi feito e os próximos passos

- [X] **JWT**
- [X] **Refresh token**
- [X] users **CRUD**
- [X] _test coverage report **Jacoco report**_
- [X] _tests **JUnity**_
- [X] _database migration **Flyway**_
- [X] _java based migrations_
- [X] _Soft delete e TIMESTAMPS_
- [X] _swagger_

---

## Requisitos

- MariaDB: `^10.3.11`
- Java: `^11`
> recomendo a instalação do maven localmente, mas o projeto tem uma versão portatil nos arquivos [`mvnw`](./mvnw) e [`mvnw.cmd`](./mvnw.cmd)

Esse projeto foi configurado com [Spring Initializr](https://start.spring.io/).

## Entidades

<p>
  <img src="./der/spring_boot_crud_der.png" alt="DER" />
</p>

> arquivo do [draw.io](./der/spring_boot_crud_der.drawio)

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
Assim que a aplicação estiver de pé, fica disponível em: [localhost:8080/api/v1/swagger-ui](https://throyer-crud-api.herokuapp.com//api/v1/documentation/swagger-ui/#/)

## Postman
Clique [**aqui**](./postman/crud_api.postman_collection.json) para acessar o aquivo `json` da coleção do postman.


<br>
<br>

---

## Database Migrations
existem dois scripts `bash` que podem ser usados para criação de arquivos de migração

- Java based migrations
  ```bash
    ./migration_create <migration-name>
    # or zsh users
    bash migration_create.sh <migration-name>
  ```

- SQL based migrations
  ```bash
    ./sql_create <migration-name>
    # or zsh users
    bash sql_create.sh <migration-name>
  ```

---
## Variáveis de ambiente

| **Descrição**                         | **parâmetro**                    | **Valor padrão**          |
| ------------------------------------- | -------------------------------- | ------------------------- |
| contexto da aplicação                 | `contexto`                       | api/v1                    |
| porta da aplicação                    | `port`                           | 8080                      |
| url do banco                          | `db-url`                         | localhost:3306/common_app |
| nome de usuário (banco)               | `db-username`                    | root                      |
| senha do usuário (banco)              | `db-password`                    | root                      |
| mostrar sql na saida                  | `show-sql`                       | false                     |
| tempo de expiração do token em horas  | `token-expiration-time-in-hours` | 24                        |
| valor do secret na geração dos tokens | `token-secret`                   | secret                    |
| máximo de conexões com o banco        | `max-connections`                | 10                        |

> são definidas em: [**application.properties**](./src/main/resources/application.properties)
>
> ```shell
> # para mudar o valor de alguma variável de ambiente
> # na execução basta passar ela como parâmetro. (como --port=80 por exemplo).
> $ java -jar api-1.0.0.RELEASE.jar --port=80
> ```
>
> > [Todas opções do `aplication.properties` **padrões** no Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).
> >
> > [Todas **funcionalidades** do Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html).
