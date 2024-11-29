package br.com.event.core.kafka;

import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.repositories.LogNotificacaoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

  private final ObjectMapper objectMapper;

  private final LogNotificacaoRepository logNotificacaoRepository;

  public KafkaConsumer(ObjectMapper objectMapper, LogNotificacaoRepository logNotificacaoRepository) {

    this.objectMapper = objectMapper;
    this.logNotificacaoRepository = logNotificacaoRepository;
  }

  @KafkaListener(topics = "send_notification", groupId = "kafka-consumer")
  public void receiveMessage(String message) {

    try {
      LogNotificacao logNotificacao = objectMapper.readValue(message, LogNotificacao.class);

      log.info("Lendo notificação {} - {}.", logNotificacao.getCodigoNotificacao(), logNotificacao.getTipoNotificacao().getDescricao());

      logNotificacaoRepository.save(logNotificacao);

      log.info(logNotificacao.getNotificacao());
    }
    catch (JsonProcessingException e) {
      log.error(e.getMessage());
      throw new ServiceException(e.getMessage());
    }
  }

}
