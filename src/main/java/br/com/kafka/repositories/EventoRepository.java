package br.com.kafka.repositories;

import br.com.kafka.entities.Evento;
import br.com.kafka.enums.StatusEventoEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

  boolean existsByNome(String nome);

  List<Evento> findAllByStatus(StatusEventoEnum status);

  @Query(value = "SELECT evento FROM Evento evento ORDER BY evento.data")
  List<Evento> findAllOrderByData();

}
