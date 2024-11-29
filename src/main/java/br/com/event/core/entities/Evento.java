package br.com.event.core.entities;

import br.com.event.core.converters.UpperCaseConverter;
import br.com.event.core.enums.PrioridadeEventoEnum;
import br.com.event.core.enums.StatusEventoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;

@Data
@Entity
@Table(name = "EVENTOS")
public class Evento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "NOME")
  @Convert(converter = UpperCaseConverter.class)
  private String nome;

  @Column(name = "DATA")
  private LocalDateTime data;

  @Column(name = "DURACAO")
  private LocalTime duracao;

  @Column(name = "PRIORIDADE")
  @Enumerated(EnumType.STRING)
  private PrioridadeEventoEnum prioridade;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private StatusEventoEnum status;

}
