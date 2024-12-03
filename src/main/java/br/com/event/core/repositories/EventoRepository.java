package br.com.event.core.repositories;

import br.com.event.core.entities.Evento;
import br.com.event.core.enums.PrioridadeEventoEnum;
import br.com.event.core.enums.StatusEventoEnum;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

  boolean existsByNome(String nome);

  List<Evento> findAllByStatus(StatusEventoEnum status);

  @Query(value =
    "SELECT evento FROM Evento evento " +
    "WHERE evento.status IN (:status) " +
    "ORDER BY " +
    "CASE evento.status " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.EM_ANDAMENTO THEN 0 " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.AGENDADO THEN 1 " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.FINALIZADO THEN 2 " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.CANCELADO THEN 3 " +
    "END, " +
    "evento.data"
  )
  List<Evento> findAllByStatusIn(List<StatusEventoEnum> status);

  List<Evento> findAllByPrioridade(PrioridadeEventoEnum prioridadeEvento);

  @Query(value =
    "SELECT evento FROM Evento evento " +
      "WHERE evento.prioridade IN (:prioridades) " +
      "ORDER BY " +
      "CASE evento.status " +
      " WHEN br.com.event.core.enums.StatusEventoEnum.EM_ANDAMENTO THEN 0 " +
      " WHEN br.com.event.core.enums.StatusEventoEnum.AGENDADO THEN 1 " +
      " WHEN br.com.event.core.enums.StatusEventoEnum.FINALIZADO THEN 2 " +
      " WHEN br.com.event.core.enums.StatusEventoEnum.CANCELADO THEN 3 " +
      "END, " +
      "evento.data"
  )
  List<Evento> findAllByPrioridadeIn(List<PrioridadeEventoEnum> prioridades);

  @Query(value =
    "SELECT evento FROM Evento evento " +
    "ORDER BY " +
    "CASE evento.status " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.EM_ANDAMENTO THEN 0 " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.AGENDADO THEN 1 " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.FINALIZADO THEN 2 " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.CANCELADO THEN 3 " +
    "END, " +
    "evento.data")
  List<Evento> findAllOrderByData();

  @Query(value =
    "SELECT evento FROM Evento evento " +
    "WHERE evento.data <= :data " +
    "AND evento.status = :statusEvento " +
    "ORDER BY " +
    "CASE evento.status " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.EM_ANDAMENTO THEN 0 " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.AGENDADO THEN 1 " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.FINALIZADO THEN 2 " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.CANCELADO THEN 3 " +
    "END, " +
    "evento.data"
  )
  List<Evento> findAllByDataAndStatus(LocalDateTime data, StatusEventoEnum statusEvento);

}
