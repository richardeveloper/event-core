package br.com.event.core.dtos;

import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.enums.TipoUsuarioEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoDto {

  private String notificacao;
  private TipoNotificacaoEnum tipoNotificacao;
  private LocalDateTime dataEnvio;

  private String nomeUsuario;
  private TipoUsuarioEnum tipoUsuario;
  private String emailUsuario;
  private String telefoneUsuario;

  private String nomeEvento;
  private LocalDateTime dataEvento;

}
