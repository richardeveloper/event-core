package br.com.event.core.entities;

import br.com.event.core.converters.UpperCaseConverter;
import br.com.event.core.enums.TipoUsuarioEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "USUARIOS")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "NOME")
  @Convert(converter = UpperCaseConverter.class)
  private String nome;

  @Column(name = "CPF", length = 11)
  private String cpf;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "TELEFONE", length = 11)
  private String telefone;

  @Column(name = "MATRICULA")
  private String matricula;

  @Column(name = "TIPO_USUARIO")
  @Enumerated(EnumType.STRING)
  private TipoUsuarioEnum tipoUsuario;

}
