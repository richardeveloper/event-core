package br.com.event.core.enums;

import lombok.Getter;

@Getter
public enum TipoNotificacaoEnum {

  INSCRICAO_CONFIRMADA("Confirmação de inscrição"),
  INSCRICAO_CANCELADA("Confirmação de cancelamento"),
  ALTERACAO_DATA_EVENTO("Alteração na data do evento"),
  EVENTO_INICIADO("Evento iniciado"),
  EVENTO_FINALIZADO("Evento finalizado"),
  EVENTO_CANCELADO("Evento cancelado");

  private final String descricao;

  TipoNotificacaoEnum(String descricao) {
    this.descricao = descricao;
  }

  public static TipoNotificacaoEnum parse(String descricao) {
    if (descricao == null) {
      return null;
    }

    for (TipoNotificacaoEnum tipoNotificacaoEnum : TipoNotificacaoEnum.values()) {
      if (tipoNotificacaoEnum.getDescricao().equals(descricao)) {
        return tipoNotificacaoEnum;
      }
    }

    throw new AssertionError(descricao);
  }

}
