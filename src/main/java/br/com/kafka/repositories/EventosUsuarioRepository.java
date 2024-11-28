package br.com.kafka.repositories;

import br.com.kafka.entities.Evento;
import br.com.kafka.entities.EventosUsuario;
import br.com.kafka.entities.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventosUsuarioRepository extends JpaRepository<EventosUsuario, Long> {

  Optional<EventosUsuario> findByEventoAndUsuario(Evento evento, Usuario usuario);

  List<EventosUsuario> findAllByEventoId(Long eventoId);

  List<EventosUsuario> findAllByUsuarioId(Long usuarioId);

  int countEventosUsuarioByEventoId(Long eventoId);
}
