# sa_task_04_06


## Table of Contents

- [Technologies](#technologies)
- [Documentation](#documentation)
- [Installation](#installation)
- [Auth](#auth)

## Technologies

The project uses the following technologies and frameworks:

- Java 11
- Maven (for project management)
- Spring (Framework, Security, Boot, MVC, Data JPA)
- JWT (Authentication)
- Liquibase (db migrations)
- PostgreSQL (as sql db)
- MongoDB (as noSql db)
- H2 (testing sql db)
- Flapdoodle embedded MongoDB (testing noSql db)
- AssertJ (testing)
- Mockito (testing)
- Lombok (for boilerplate reduction)
- Mapstruct (mapping between entities and dtos)
- Springdoc openapi (Swagger documentation)
- JavaDoc (Java documentation)
- Git (for version control)
- Docker (containerization)

## Documentation

Swagger documentation will be available at: http://localhost:8080/swagger-ui.html when app will be running. \
JavaDoc documentation is generated and located in this project at: sa_task_04_06/java-doc/index.html

## Installation

To run the project locally in docker, follow these steps:

1. Clone the repository:

   ```
   git clone https://github.com/YaremaMaksym/sa_task_04_06.git
   ```

2. Run `mvn clean package` to package application.

3. Run `docker compose up -d` to create and start containers.

**The application should now be running on [http://localhost:8080](http://localhost:8080).**

## Auth

Keep in mind that app is using spring security with jwt tokens to auth. \
So to use API you have to be authenticated: 
* register at [http://localhost:8080/api/v1/auth/register](http://localhost:8080/api/v1/auth/register).
* login [http://localhost:8080/api/v1/auth/login](http://localhost:8080/api/v1/auth/login) if you already registered.

Then you should pass bearer token with jwt to all request except swagger documentation page.
