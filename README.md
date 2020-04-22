<p align="center">
  <a href="https://github.com/Throyer" target="blank"><img src="./spring-boot_logo.png" width="120" alt="Spring boot Logo" /></a>
</p>

<h2 align="center">Spring Boot API CRUD</h2>
<p align="center">
    Aplicação em spring boot RESTful, com testes de integração,
    documentação automatica com Swagger e token JWT.
</p>

# Requisitos

- MariaDB: `^10.3.11`
- Java: `^11`

Esse projeto foi configurado com [Spring Initializr](https://start.spring.io/).

## Instalação

Clone o repositório. [Git](https://git-scm.com/)

```shell
git clone git@github.com:Throyer/springboot-api-crud.git # SSH

git clone https://github.com/Throyer/springboot-api-crud.git # HTTP
```

Entre na pasta do projeto

```bash
cd springboot-api-crud
```

Baixe as dependencias

```bash
mvnw install
```

Rode a aplicação

```bash
mvnw spring-boot:run
```

Documentação swagger: http://localhost:8080/api/v1/swagger-ui.html

## Variaveis de ambiente

> são definidas em: [**application.properties**](./src/main/resources/application.properties)

para definir variaveis de ambiente durante a execução basta passar o parametro:

> por exemplo mudar a porta use `--port=<porta_desejada>`:
>
> ```bash
> java -jar api-1.0.0.RELEASE.jar --port=80
> ```

| **Descrição**                                     | **parametro**                    | **Valor padrão**          |
| ------------------------------------------------- | -------------------------------- | ------------------------- |
| contexto da aplicação                             | `contexto`                       | api/v1                    |
| porta da aplicação                                | `port`                           | 8080                      |
| url do banco                                      | `db-url`                         | localhost:3306/common_app |
| nome de usuario (banco)                           | `db-username`                    | root                      |
| senha do usuario (banco)                          | `db-password`                    | root                      |
| mostrar sql na saida                              | `show-sql`                       | false                     |
| `criar`/`atualizar`/`validar` as tabelas no banco | `ddl-auto`                       | update                    |
| tempo de expiração do token em horas              | `token-expiration-time-in-hours` | 24                        |
| valor do secret na geração dos tokens             | `token-secret`                   | secret                    |
| maximo de conexões com o banco                    | `max-connections`                | 10                        |
