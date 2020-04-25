<p align="center">
  <a href="https://github.com/Throyer" target="blank"><img src="./assets/tecnologias.png" alt="Spring boot Logo" /></a>
</p>

<h1 align="center">Spring Boot API CRUD</h1>
<p align="center">
    Aplicação em spring boot RESTful, com testes de integração,
    documentação automatica com Swagger e token JWT.
</p>

# Motivação
<p>
A ideia desse repositorio é a criação de uma api em spring boot, com o maximo possivel de boas praticas e o mais completa possivel, para servir como uma base para min no futuro, ou para outras pessoas que estiverem buscando um guia para a construição de uma
api com spring. Qualquer pessoa que quiser contribuir ou usar esse projeto é bem vinda.
</p>

# O que foi feito e os proximos passos

- [X] Autenticação com Spring Security e Token JWT.
- [ ] Crud completo de Usuario e Permissões.
    - [X] Crud de Usuario
    - [ ] Crud de Permissões
- [ ] Testes de Integração de todos controllers
    - [ ] controller `"/usuarios"`
        - [X] POST
    - [ ] controller `"/permissoes"`
- [X] Swagger
- [ ] Migrações do banco com Flyway

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
