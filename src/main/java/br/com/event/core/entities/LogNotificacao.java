package br.com.event.core.entities;

import br.com.event.core.enums.TipoNotificacaoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "LOG_NOTIFICACAO")
public class LogNotificacao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "CODIGO_NOTIFICACAO")
  private String codigoNotificacao;

  @Column(name = "NOTIFICACAO")
  private String notificacao;

  @Enumerated(EnumType.STRING)
  @Column(name = "TIPO_NOTIFICACAO")
  private TipoNotificacaoEnum tipoNotificacao;

  @Column(name = "CODIGO_LOTE")
  private Integer codigoLote;

  @Column(name = "DATA_ENVIO")
  private LocalDateTime dataEnvio;

}
