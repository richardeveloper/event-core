package br.com.event.core.enums;

import lombok.Getter;

@Getter
public enum TipoNotificacaoEnum {

  INSCRICAO_CONFIRMADA("Confirmação de inscrição"),
  INSCRICAO_CANCELADA("Cancelamento de inscrição"),
  ALTERACAO_DATA_EVENTO("Alteração na data do evento"),
  EVENTO_INICIADO("Início de evento"),
  EVENTO_FINALIZADO("Fim do evento"),
  EVENTO_CANCELADO("Cancelamento de evento");

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
