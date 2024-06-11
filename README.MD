# sa_task_04_06


## Table of Contents

- [Technologies](#technologies)
- [Documentation](#documentation)
- [Installation](#installation)

## Technologies

The project uses the following technologies and frameworks:

[//]: # (- Liquibase &#40;db migrations&#41;)
- Java 11
- Maven (for project management)
- Spring (Framework, Security, Boot, MVC, Data JPA)
- JWT (Authentication)
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

The application should now be running on [http://localhost:8080](http://localhost:8080).
