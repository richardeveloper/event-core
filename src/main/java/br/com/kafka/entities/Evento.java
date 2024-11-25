package br.com.kafka.entities;

import br.com.kafka.enums.PrioridadeEventoEnum;
import br.com.kafka.enums.StatusEventoEnum;
import br.com.kafka.enums.TipoUsuarioEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EVENTOS")
public class Evento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "NOME")
  private String nome;

  @Column(name = "DATA")
  private LocalDateTime data;

  @Column(name = "DURACAO")
  private LocalTime duracao;

  @Enumerated(EnumType.STRING)
  @Column(name = "PRIORIDADE")
  private PrioridadeEventoEnum prioridade;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private StatusEventoEnum status;

  public String getData() {
    if (this.data == null) {
      return null;
    }
    return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
  }
}
