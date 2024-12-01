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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LOG_NOTIFICACAO")
public class LogNotificacao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "TIPO_NOTIFICACAO")
  private TipoNotificacaoEnum tipoNotificacao;

  @Column(name = "NOTIFICACAO", columnDefinition = "TEXT")
  private String notificacao;

  @Column(name = "DATA_ENVIO")
  private LocalDateTime dataEnvio;

  @Column(name = "NOME_USUARIO")
  private String nomeUsuario;

  @Column(name = "Tipo_USUARIO")
  private String tipoUsuario;

  @Column(name = "NOME_EVENTO")
  private String nomeEvento;

  @Column(name = "DATA_EVENTO")
  private String dataEvento;

}
