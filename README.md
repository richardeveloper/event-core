# ADS1242 - Trabalho Final - Mensageria e Streams em Aplicações

<div align="center">

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/javafx-%23FF0000.svg?style=for-the-badge&logo=javafx&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)

</div>

## Instruções

<p align="justify">
Você deverá desenvolver um sistema de mensageria utilizando RabbitMQ ou Kafka, integrando o mensageiro a um aplicativo JavaFX e a um banco de dados relacional (PostgreSQL ou outro de sua escolha). Este trabalho pode ser realizado individualmente ou em duplas, e o tema poderá ser escolhido dentre os listados abaixo.
</p>

## Arquitetura e Funcionalidades

- O sistema deverá contar com cliente e servidor, comunicando-se exclusivamente através do mensageiro (RabbitMQ ou Kafka).
- Deve ser implementado um CRUD simples, utilizando JavaFX como interface do cliente.
- O mensageiro será responsável por toda a comunicação entre cliente e servidor.
Alterações no banco de dados (inserção, exclusão, atualização) deverão ser realizadas por meio dos @RabbitListener ou @KafkaListener implementados no servidor.
- O sistema deverá refletir as alterações em tempo real entre cliente e servidor, através das mensagens enviadas pelo mensageiro.

## Motivação

### Organizador de Eventos e Palestras com Notificações

<p align="justify"> 
Permitir criar eventos/palestras, gerenciar a lista de participantes e enviar lembretes sobre horários ou mudanças de agenda. CRUD: Cadastro de eventos, participantes e notificações enviadas.
</p>

<br>

> [!TIP]
>
> ### Rodar aplicação
>
> **Requisitos:**
> - Java 17 ou superior
> - <a href="http://localhost:8080/api-docs" style="text-decoration: none;">Java FX 17.0.13</a>
> - PostgreSQL
>
> **Variáveis para execução:**
> 
> ```
> --module-path /path/to/lib/javafx-sdk-17.0.13/lib --add-modules javafx.controls,javafx.fxml
> ```
> 
> **Variáveis para conexão com banco de dados:**
>
> ```
> DB_URL="jdbc:postgresql://localhost:5432/banco_de_dados"
> DB_USERNAME="postgres"
> DB_PASSWORD="postgres"
> ```
