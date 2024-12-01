package br.com.event.core.enums;

import lombok.Getter;

@Getter
public enum StatusEventoEnum {

  AGENDADO("AGENDADO"),
  CANCELADO("CANCELADO"),
  FINALIZADO("FINALIZADO"),
  EM_ANDAMENTO("EM ANDAMENTO");

  private final String descricao;

  StatusEventoEnum(String descricao) {
    this.descricao = descricao;
  }

  public static StatusEventoEnum parse(String descricao) {
    if (descricao == null) {
      return null;
    }

    for (StatusEventoEnum statusEventoEnum : StatusEventoEnum.values()) {
      if (statusEventoEnum.getDescricao().equalsIgnoreCase(descricao)) {
        return statusEventoEnum;
      }
    }

    throw new AssertionError(descricao);
  }
}
