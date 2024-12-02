package br.com.event.core.rabbit;

import br.com.event.core.dtos.NotificacaoDto;
import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.repositories.LogNotificacaoRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
  private static final String LOGS_NOTIFICACOES = "logs_notificacoes";

  private final LogNotificacaoRepository logNotificacaoRepository;

  public RabbitConsumer(LogNotificacaoRepository logNotificacaoRepository) {
    this.logNotificacaoRepository = logNotificacaoRepository;
  }

  @RabbitListener(queues = CONFIRMACAO_INSCRICAO)
  public void readConfirmacaoInscricacao(NotificacaoDto notificacao) {
    log.info(notificacao.getNotificacao());
  }

  @RabbitListener(queues = CANCELAMENTO_INSCRICAO)
  public void readCancelamentoInscricacao(NotificacaoDto notificacao) {
    log.info(notificacao.getNotificacao());
  }

  @RabbitListener(queues = ALTERACAO_DATA_EVENTO)
  public void readAlteracaoDataEvento(NotificacaoDto notificacao) {
    log.info(notificacao.getNotificacao());
  }

  @RabbitListener(queues = INICIO_EVENTO)
  public void readInicioEvento(NotificacaoDto notificacao) {
    log.info(notificacao.getNotificacao());
  }

  @RabbitListener(queues = FIM_EVENTO)
  public void readFimEvento(NotificacaoDto notificacao) {
    log.info(notificacao.getNotificacao());
  }

  @RabbitListener(queues = CANCELAMENTO_EVENTO)
  public void readCancelamentoEvento(NotificacaoDto notificacao) {
    log.info(notificacao.getNotificacao());
  }

  @RabbitListener(queues = LOGS_NOTIFICACOES)
  public void readLogsNotificacoes(NotificacaoDto notificacao) {
    readMessage(notificacao);
  }

  private void readMessage(NotificacaoDto notificacao) {

    LogNotificacao logNotificacao = LogNotificacao.builder()
      .notificacao(notificacao.getNotificacao())
      .tipoNotificacao(notificacao.getTipoNotificacao())
      .dataEnvio(LocalDateTime.now())
      .nomeUsuario(notificacao.getNomeUsuario())
      .tipoUsuario(notificacao.getTipoUsuario().getDescricao())
      .nomeEvento(notificacao.getNomeEvento())
      .dataEvento(notificacao.getDataEvento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
      .build();

    logNotificacaoRepository.save(logNotificacao);
  }

}
