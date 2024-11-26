package br.com.kafka.enums;

import lombok.Getter;

@Getter
public enum PrioridadeEventoEnum {

  ABERTO("Aberto ao público"),
  OBRIGATORIO_ALUNOS("Obrigatório para alunos"),
  OBRIGATORIO_PROFESSORES("Obrigatório para professores");

  private String descricao;

  PrioridadeEventoEnum(String descricao) {
    this.descricao = descricao;
  }

  public static PrioridadeEventoEnum parse(String descricao) {
    if (descricao == null) {
      return null;
    }

    for (PrioridadeEventoEnum tipoUsuarioEnum : PrioridadeEventoEnum.values()) {
      if (tipoUsuarioEnum.getDescricao().equals(descricao)) {
        return tipoUsuarioEnum;
      }
    }

    throw new AssertionError(descricao);
  }
}
