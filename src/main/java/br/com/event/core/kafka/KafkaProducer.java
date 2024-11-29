package br.com.event.core.kafka;

import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.exceptions.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  private final ObjectMapper objectMapper;

  public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public void sendMessage(TipoNotificacaoEnum tipoNotificacao, String message) {
    LogNotificacao logNotificacao = new LogNotificacao();
    logNotificacao.setNotificacao(message);
    logNotificacao.setTipoNotificacao(tipoNotificacao);
    logNotificacao.setCodigoNotificacao(UUID.randomUUID().toString().substring(0, 6));
    logNotificacao.setCodigoLote(null);
    logNotificacao.setDataEnvio(LocalDateTime.now());

    try {
      String json = objectMapper.writeValueAsString(logNotificacao);

      this.kafkaTemplate.send("send_notification", json);

      log.info("Enviando notificação {} - {}.", logNotificacao.getCodigoNotificacao(), logNotificacao.getTipoNotificacao().getDescricao());
    }
    catch (JsonProcessingException e) {
      log.error(e.getMessage());
      throw new ServiceException(e.getMessage());
    }

  }

}
