package br.com.event.core.amqp;

import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.repositories.LogNotificacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitConsumer {

  private static final String CONFIRMACAO_INSCRICAO = "confirmacao_inscricao";
  private static final String CANCELAMENTO_INSCRICAO = "cancelamento_inscricao";
  private static final String ALTERACAO_DATA_EVENTO = "alteracao_data_evento";
  private static final String INICIO_EVENTO = "inicio_evento";
  private static final String FIM_EVENTO = "fim_evento";
  private static final String CANCELAMENTO_EVENTO = "cancelamento_evento";

  private final LogNotificacaoRepository logNotificacaoRepository;

  public RabbitConsumer(LogNotificacaoRepository logNotificacaoRepository) {
    this.logNotificacaoRepository = logNotificacaoRepository;
  }

  @RabbitListener(queues = CONFIRMACAO_INSCRICAO)
  public void readConfirmacaoInscricacao(LogNotificacao notificacao) {
    readMessage(notificacao);
  }

  @RabbitListener(queues = CANCELAMENTO_INSCRICAO)
  public void readCancelamentoInscricacao(LogNotificacao notificacao) {
    readMessage(notificacao);
  }

  @RabbitListener(queues = ALTERACAO_DATA_EVENTO)
  public void readAlteracaoDataEvento(LogNotificacao notificacao) {
    readMessage(notificacao);
  }

  @RabbitListener(queues = INICIO_EVENTO)
  public void readInicioEvento(LogNotificacao notificacao) {
    readMessage(notificacao);
  }

  @RabbitListener(queues = FIM_EVENTO)
  public void readFimEvento(LogNotificacao notificacao) {
    readMessage(notificacao);
  }

  @RabbitListener(queues = CANCELAMENTO_EVENTO)
  public void readCancelamentoEvento(LogNotificacao notificacao) {
    readMessage(notificacao);
  }

  private void readMessage(LogNotificacao logNotificacao) {
    logNotificacaoRepository.save(logNotificacao);
    log.info(logNotificacao.getNotificacao());
  }

}
