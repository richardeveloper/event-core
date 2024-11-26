package br.com.kafka.entities;

import br.com.kafka.enums.PrioridadeEventoEnum;
import br.com.kafka.enums.StatusEventoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "USUARIOS_EVENTO",
    joinColumns = @JoinColumn(name = "USUARIO_ID"),
    inverseJoinColumns = @JoinColumn(name = "EVENTO_ID")
  )
  private List<Usuario> participantes = new ArrayList<>();

  public void adicionarParticipante(Usuario usuario) {
    this.participantes.add(usuario);
  }

  public void removerParticipante(Usuario usuario) {
    this.participantes.remove(usuario);
  }

  public int getQuantidadeParticipantes() {
    return participantes.size();
  }

//  public String getData() {
//    if (this.data == null) {
//      return null;
//    }
//    return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
//  }
}
