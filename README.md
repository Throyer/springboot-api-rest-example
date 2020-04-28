<p align="center">
  <a href="https://github.com/Throyer" target="blank"><img src="./assets/tecnologias.png" width="560" alt="Spring boot Logo" /></a>
</p>

<h1 align="center">Spring Boot API CRUD</h1>
<p align="center">
    Aplicação em spring boot RESTful, com testes de integração,
    documentação automatica com Swagger e token JWT.
</p>
<br>
<br>

# Motivação

<p>
A ideia desse repositorio é a criação de uma api em spring boot, com o maximo possivel de boas praticas e o mais completa possivel, para servir como uma base para min no futuro, ou para outras pessoas que estiverem buscando um guia para a construição de uma
api com spring. Qualquer pessoa que quiser contribuir ou usar esse projeto é bem vinda.
</p>

# O que foi feito e os proximos passos

- [X] *autenticação com **Spring Security** com **JWT***.
  - [ ] **refresh token**
- [ ] **cadastro completo de usuarios e permissões**.
  - [X] *usuarios*
  - [ ] **permissões**
- [ ] **testes de integração de todos controllers**
  - [ ] `"/usuarios"`
  - [ ] `"/permissoes"`
- [X] *swagger*
- [X] *database migration **Flyway***
- [X] *Soft delete e TIMESTAMPS*

# Requisitos

- MariaDB: `^10.3.11`
- Java: `^11`

Esse projeto foi configurado com [Spring Initializr](https://start.spring.io/).

## Instalação
```shell
# Clone o repositório e acesse o diretorio.
$ git clone git@github.com:Throyer/springboot-api-crud.git && cd springboot-api-crud

# Baixe as dependencias
$ mvnw install

# Rode a aplicação
$ mvnw spring-boot:run

# Para rodar os testes
$ mvnw test

# Para buildar para produção
$ mvnw clean package
```
> Quando a aplicação estiver rodando acesse a documentação em:
> http://localhost:8080/api/v1/swagger-ui.html
<br>
<br>


____________________________________________________


## Variaveis de ambiente


| **Descrição**                         | **parametro**                    | **Valor padrão**          |
| ------------------------------------- | -------------------------------- | ------------------------- |
| contexto da aplicação                 | `contexto`                       | api/v1                    |
| porta da aplicação                    | `port`                           | 8080                      |
| url do banco                          | `db-url`                         | localhost:3306/common_app |
| nome de usuario (banco)               | `db-username`                    | root                      |
| senha do usuario (banco)              | `db-password`                    | root                      |
| mostrar sql na saida                  | `show-sql`                       | false                     |
| tempo de expiração do token em horas  | `token-expiration-time-in-hours` | 24                        |
| valor do secret na geração dos tokens | `token-secret`                   | secret                    |
| maximo de conexões com o banco        | `max-connections`                | 10                        |


> são definidas em: [**application.properties**](./src/main/resources/application.properties)
> ```shell
> # para mudar o valor de alguma variavel de ambiente na execução basta colocar como parametro na execução
> $ java -jar api-1.0.0.RELEASE.jar --port=80
> ```
