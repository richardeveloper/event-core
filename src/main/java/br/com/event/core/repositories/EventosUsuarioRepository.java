package br.com.event.core.repositories;

import br.com.event.core.entities.Evento;
import br.com.event.core.entities.EventosUsuario;
import br.com.event.core.entities.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventosUsuarioRepository extends JpaRepository<EventosUsuario, Long> {

  Optional<EventosUsuario> findByEventoAndUsuario(Evento evento, Usuario usuario);

  @Query(value =
    "SELECT eventosUsuario FROM EventosUsuario eventosUsuario " +
    "WHERE eventosUsuario.evento.id = :eventoId " +
    "ORDER BY " +
    "CASE eventosUsuario.evento.status " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.EM_ANDAMENTO THEN 0 " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.AGENDADO THEN 1 " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.FINALIZADO THEN 2 " +
    " WHEN br.com.event.core.enums.StatusEventoEnum.CANCELADO THEN 3 " +
    "END, " +
    "eventosUsuario.evento.data"
  )
  List<EventosUsuario> findAllByEventoId(Long eventoId);

  @Query(value =
    "SELECT eventosUsuario FROM EventosUsuario eventosUsuario " +
      "WHERE eventosUsuario.usuario.id = :usuarioId " +
      "ORDER BY " +
      "CASE eventosUsuario.evento.status " +
      " WHEN br.com.event.core.enums.StatusEventoEnum.EM_ANDAMENTO THEN 0 " +
      " WHEN br.com.event.core.enums.StatusEventoEnum.AGENDADO THEN 1 " +
      " WHEN br.com.event.core.enums.StatusEventoEnum.FINALIZADO THEN 2 " +
      " WHEN br.com.event.core.enums.StatusEventoEnum.CANCELADO THEN 3 " +
      "END, " +
      "eventosUsuario.evento.data"
  )
  List<EventosUsuario> findAllByUsuarioId(Long usuarioId);

  int countEventosUsuarioByEventoId(Long eventoId);

}
