package br.com.kafka.enums;

import lombok.Getter;

@Getter
public enum StatusEventoEnum {

  AGENDADO("AGENDADO"),
  CANCELADO("CANCELADO"),
  FINALIZADO("FINALIZADO");

  private final String descricao;

  StatusEventoEnum(String descricao) {
    this.descricao = descricao;
  }

  public static StatusEventoEnum parse(String descricao) {
    if (descricao == null) {
      return null;
    }

    for (StatusEventoEnum tipoUsuarioEnum : StatusEventoEnum.values()) {
      if (tipoUsuarioEnum.getDescricao().equalsIgnoreCase(descricao)) {
        return tipoUsuarioEnum;
      }
    }

    throw new AssertionError(descricao);
  }
}
