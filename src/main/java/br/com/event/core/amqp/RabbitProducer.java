package br.com.event.core.amqp;

import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.exceptions.ServiceException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitProducer {

  private static final String CONFIRMACAO_INSCRICAO = "confirmacao_inscricao";
  private static final String CANCELAMENTO_INSCRICAO = "cancelamento_inscricao";
  private static final String ALTERACAO_DATA_EVENTO = "alteracao_data_evento";
  private static final String INICIO_EVENTO = "inicio_evento";
  private static final String FIM_EVENTO = "fim_evento";
  private static final String CANCELAMENTO_EVENTO = "cancelamento_evento";

  private final RabbitTemplate rabbitTemplate;

  public RabbitProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void sendMessage(TipoNotificacaoEnum tipoNotificacao, String notificacao) {

    LogNotificacao logNotificacao = LogNotificacao.builder()
      .notificacao(notificacao)
      .tipoNotificacao(tipoNotificacao)
      .dataEnvio(LocalDateTime.now())
      .build();

    String routingKey = generateRoutingKey(tipoNotificacao);

    rabbitTemplate.convertAndSend(routingKey, logNotificacao);
  }

  private static String generateRoutingKey(TipoNotificacaoEnum tipoNotificacao) {
    String routingKey;

    switch (tipoNotificacao) {
      case INSCRICAO_CONFIRMADA -> routingKey = CONFIRMACAO_INSCRICAO;
      case INSCRICAO_CANCELADA -> routingKey = CANCELAMENTO_INSCRICAO;
      case ALTERACAO_DATA_EVENTO -> routingKey =  ALTERACAO_DATA_EVENTO;
      case EVENTO_INICIADO -> routingKey = INICIO_EVENTO;
      case EVENTO_FINALIZADO -> routingKey = FIM_EVENTO;
      case EVENTO_CANCELADO -> routingKey = CANCELAMENTO_EVENTO;
      default -> throw new ServiceException("Não foi possível identificar o tipo de notificação a ser enviada.");
    }

    return routingKey;
  }

}
