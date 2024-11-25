package br.com.kafka.entities;

import br.com.kafka.enums.TipoUsuarioEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USUARIOS")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "NOME")
  private String nome;

  @Column(name = "CPF", length = 11)
  private String cpf;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "TELEFONE", length = 11)
  private String telefone;

  @Enumerated(EnumType.STRING)
  @Column(name = "TIPO_USUARIO")
  private TipoUsuarioEnum tipoUsuario;

}
