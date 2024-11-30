package br.com.event.core.amqp;

import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.repositories.LogNotificacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitConsumer {

  private final LogNotificacaoRepository logNotificacaoRepository;

  public RabbitConsumer(LogNotificacaoRepository logNotificacaoRepository) {
    this.logNotificacaoRepository = logNotificacaoRepository;
  }

  @RabbitListener(queues = "confirmacao_inscricao")
  public void readConfirmacaoInscricacao(LogNotificacao notificacao) {
    readMessage(notificacao);
  }

  @RabbitListener(queues = "cancelamento_inscricao")
  public void readCancelamentoInscricacao(LogNotificacao notificacao) {
    readMessage(notificacao);
  }

  @RabbitListener(queues ="alteracao_data_evento")
  public void readAlteracaoDataEvento(LogNotificacao notificacao) {
    readMessage(notificacao);
  }

  @RabbitListener(queues = "cancelamento_evento")
  public void readCancelamentoEvento(LogNotificacao notificacao) {
    readMessage(notificacao);
  }

  private void readMessage(LogNotificacao notificacao) {
    log.info("Lendo notificação {} - {}.", notificacao.getCodigoNotificacao(), notificacao.getTipoNotificacao().getDescricao());

    logNotificacaoRepository.save(notificacao);

    log.info(notificacao.getNotificacao());
  }

}
