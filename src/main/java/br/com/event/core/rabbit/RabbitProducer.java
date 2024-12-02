package br.com.event.core.rabbit;

import br.com.event.core.dtos.NotificacaoDto;
import br.com.event.core.entities.Evento;
import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitProducer {

  private static final String LOGS_EXCHANGE = "logs_exchange";

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

  public void sendMessage(Usuario usuario, Evento evento, TipoNotificacaoEnum tipoNotificacao, String notificacao) {

    NotificacaoDto notificacaoDto = NotificacaoDto.builder()
      .notificacao(notificacao)
      .tipoNotificacao(tipoNotificacao)
      .dataEnvio(null)
      .nomeUsuario(usuario.getNome())
      .tipoUsuario(usuario.getTipoUsuario())
      .emailUsuario(usuario.getEmail())
      .telefoneUsuario(usuario.getTelefone())
      .nomeEvento(evento.getNome())
      .dataEvento(evento.getData())
      .build();

    String routingKey = generateRoutingKey(tipoNotificacao);

    rabbitTemplate.convertAndSend(LOGS_EXCHANGE, routingKey, notificacaoDto);
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
