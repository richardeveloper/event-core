package br.com.event.core.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {

  @Bean
  public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin) {
    return event -> rabbitAdmin.initialize();
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
    Jackson2JsonMessageConverter messageConverter) {

    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(messageConverter);

    return rabbitTemplate;
  }

  @Bean
  public Queue confirmacaoInscricaoQueue() {
    return new Queue(
      "confirmacao_inscricao",
      true
    );
  }

  @Bean
  public Queue cancelamentoInscricaoQueue() {
    return new Queue(
      "cancelamento_inscricao",
      true
    );
  }

  @Bean
  public Queue alteracaoDataEventoQueue() {
    return new Queue(
      "alteracao_data_evento",
      true
    );
  }

  @Bean
  public Queue cancelamentoEventoQueue() {
    return new Queue(
      "cancelamento_evento",
      true
    );
  }
}
