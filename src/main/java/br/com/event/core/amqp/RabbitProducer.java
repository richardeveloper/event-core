package br.com.event.core.amqp;

import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.exceptions.ServiceException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitProducer {

  private final RabbitTemplate rabbitTemplate;

  public RabbitProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void sendMessage(TipoNotificacaoEnum tipoNotificacao, String notificacao) {

    LogNotificacao logNotificacao = new LogNotificacao();
    logNotificacao.setNotificacao(notificacao);
    logNotificacao.setTipoNotificacao(tipoNotificacao);
    logNotificacao.setCodigoNotificacao(UUID.randomUUID().toString().substring(0, 6));
    logNotificacao.setCodigoLote(null);
    logNotificacao.setDataEnvio(LocalDateTime.now());

    switch (tipoNotificacao) {
      case INSCRICAO_CONFIRMADA -> rabbitTemplate.convertAndSend("confirmacao_inscricao", logNotificacao);
      case INSCRICAO_CANCELADA -> rabbitTemplate.convertAndSend("alteracao_data_evento", logNotificacao);
      case ALTERACAO_DATA_EVENTO -> rabbitTemplate.convertAndSend("send_notification", logNotificacao);
      case EVENTO_CANCELADO -> rabbitTemplate.convertAndSend("cancelamento_evento", logNotificacao);
      default -> throw new ServiceException("Não foi possível identificar o tipo de notificação a ser enviada.");
    }

    log.info("Enviando notificação {} - {}.", logNotificacao.getCodigoNotificacao(), logNotificacao.getTipoNotificacao().getDescricao());
  }

}
