package br.com.kafka.repositories;

import br.com.kafka.entities.Evento;
import br.com.kafka.entities.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

  boolean existsByNome(String nome);

  @Query(value = "SELECT evento FROM Evento evento ORDER BY evento.data DESC")
  List<Evento> findAllOrderByDataDesc();
}
