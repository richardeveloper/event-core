package br.com.event.core.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitConfig {

  private static final String LOGS_EXCHANGE = "logs_exchange";

  private static final String CONFIRMACAO_INSCRICAO = "confirmacao_inscricao";
  private static final String CANCELAMENTO_INSCRICAO = "cancelamento_inscricao";
  private static final String ALTERACAO_DATA_EVENTO = "alteracao_data_evento";
  private static final String INICIO_EVENTO = "inicio_evento";
  private static final String FIM_EVENTO = "fim_evento";
  private static final String CANCELAMENTO_EVENTO = "cancelamento_evento";
  private static final String LOGS_NOTIFICACOES = "logs_notificacoes";

  private static final String ALL_QUEUES = "#";

  /**
   *  CONFIGS
   */
  @Bean
  public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin) {
    return (event) -> rabbitAdmin.initialize();
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(messageConverter);

    return rabbitTemplate;
  }

  /**
   *  QUEUES
   */
  @Bean
  public Queue confirmacaoInscricaoQueue() {
    return new Queue(CONFIRMACAO_INSCRICAO, true);
  }

  @Bean
  public Queue cancelamentoInscricaoQueue() {
    return new Queue(CANCELAMENTO_INSCRICAO, true);
  }

  @Bean
  public Queue alteracaoDataEventoQueue() {
    return new Queue(ALTERACAO_DATA_EVENTO, true);
  }

  @Bean
  public Queue inicioEventoQueue() {
    return new Queue(INICIO_EVENTO, true);
  }

  @Bean
  public Queue fimEventoQueue() {
    return new Queue(FIM_EVENTO, true);
  }

  @Bean
  public Queue cancelamentoEventoQueue() {
    return new Queue(CANCELAMENTO_EVENTO, true);
  }

  @Bean
  public Queue logsNotificacoesQueue() {
    return new Queue(LOGS_NOTIFICACOES, true);
  }

  /**
   *  EXCHANGE
   */
  @Bean
  public Exchange topicExchange() {
    return ExchangeBuilder
      .topicExchange(LOGS_EXCHANGE)
      .durable(true)
      .build();
  }

  /**
   *  BINDINGS
   */
  @Bean
  public Binding bindingConfirmacaoInscricao(Queue confirmacaoInscricaoQueue, Exchange topicExchange) {
    return BindingBuilder.bind(confirmacaoInscricaoQueue)
      .to(topicExchange)
      .with(CONFIRMACAO_INSCRICAO)
      .noargs();
  }

  @Bean
  public Binding bindingCancelamentoInscricao(Queue cancelamentoInscricaoQueue, Exchange topicExchange) {
    return BindingBuilder.bind(cancelamentoInscricaoQueue)
      .to(topicExchange)
      .with(CANCELAMENTO_INSCRICAO)
      .noargs();
  }

  @Bean
  public Binding bindingAlteracaoDataEvento(Queue alteracaoDataEventoQueue, Exchange topicExchange) {
    return BindingBuilder.bind(alteracaoDataEventoQueue)
      .to(topicExchange)
      .with(ALTERACAO_DATA_EVENTO)
      .noargs();
  }

  @Bean
  public Binding bindingInicioEvento(Queue inicioEventoQueue, Exchange topicExchange) {
    return BindingBuilder.bind(inicioEventoQueue)
      .to(topicExchange)
      .with(INICIO_EVENTO)
      .noargs();
  }

  @Bean
  public Binding bindingFimEvento(Queue fimEventoQueue, Exchange topicExchange) {
    return BindingBuilder.bind(fimEventoQueue)
      .to(topicExchange)
      .with(FIM_EVENTO)
      .noargs();
  }

  @Bean
  public Binding bindingCancelamentoEvento(Queue cancelamentoEventoQueue, Exchange topicExchange) {
    return BindingBuilder.bind(cancelamentoEventoQueue)
      .to(topicExchange)
      .with(CANCELAMENTO_EVENTO)
      .noargs();
  }

  @Bean
  public Binding bindingLogsNotificacoes(Queue logsNotificacoesQueue, Exchange topicExchange) {
    return BindingBuilder.bind(logsNotificacoesQueue)
      .to(topicExchange)
      .with(ALL_QUEUES)
      .noargs();
  }

}
