package br.com.event.core.enums;

import lombok.Getter;

@Getter
public enum TipoUsuarioEnum {

  ALUNO("Aluno"),
  PROFESSOR("Professor"),
  VISITANTE("Visitante");

  private final String descricao;

  TipoUsuarioEnum(String descricao) {
    this.descricao = descricao;
  }

  public static TipoUsuarioEnum parse(String descricao) {
    if (descricao == null) {
      return null;
    }

    for (TipoUsuarioEnum tipoUsuarioEnum : TipoUsuarioEnum.values()) {
      if (tipoUsuarioEnum.getDescricao().equals(descricao)) {
        return tipoUsuarioEnum;
      }
    }

    throw new AssertionError(descricao);
  }
}
