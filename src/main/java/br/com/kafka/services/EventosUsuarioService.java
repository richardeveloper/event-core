package br.com.kafka.services;

import br.com.kafka.entities.Evento;
import br.com.kafka.entities.EventosUsuario;
import br.com.kafka.entities.Usuario;
import br.com.kafka.exceptions.ServiceException;
import br.com.kafka.repositories.EventoRepository;
import br.com.kafka.repositories.EventosUsuarioRepository;
import br.com.kafka.repositories.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class EventosUsuarioService {

  private final EventosUsuarioRepository eventosUsuarioRepository;

  private final UsuarioRepository usuarioRepository;

  private final EventoRepository eventoRepository;

  public EventosUsuarioService(EventosUsuarioRepository eventosUsuarioRepository,
    UsuarioRepository usuarioRepository, EventoRepository eventoRepository) {

    this.eventosUsuarioRepository = eventosUsuarioRepository;
    this.usuarioRepository = usuarioRepository;
    this.eventoRepository = eventoRepository;
  }

  public void realizarInscricao(Long usuarioId, Long eventoId) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
      .orElseThrow(() -> new ServiceException("O usuário informado não foi identificado."));

    Evento evento = eventoRepository.findById(eventoId)
      .orElseThrow(() -> new ServiceException("O evento informado não foi identificado."));

    Optional<EventosUsuario> eventos = eventosUsuarioRepository.findByEventoAndUsuario(evento, usuario);

    if (eventos.isEmpty()) {
      EventosUsuario eventosUsuario = new EventosUsuario();
      eventosUsuario.setEvento(evento);
      eventosUsuario.setUsuario(usuario);

      eventosUsuarioRepository.save(eventosUsuario);

      log.info("A inscrição de {} no evento {} foi realizada com sucesso.", usuario.getNome(), evento.getNome());
      return;
    }

    log.info("O usuário {} já está inscrito no evento {}", usuario.getNome(), evento.getNome());
  }

  @Transactional
  public void cancelarInscricao(Long usuarioId, Long eventoId) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
      .orElseThrow(() -> new ServiceException("O usuário informado não foi identificado."));

    Evento evento = eventoRepository.findById(eventoId)
      .orElseThrow(() -> new ServiceException("O evento informado não foi identificado."));

    Optional<EventosUsuario> eventos = eventosUsuarioRepository.findByEventoAndUsuario(evento, usuario);

    if (eventos.isPresent()) {
      EventosUsuario eventosUsuario = eventos.get();

      eventosUsuarioRepository.delete(eventosUsuario);

      log.info("O cancelamento da inscrição de {} no evento {} foi realizada com sucesso.", usuario.getNome(), evento.getNome());
      return;
    }

    log.info("O usuário {} não está inscrito no evento {}", usuario.getNome(), evento.getNome());
  }

  public List<EventosUsuario> buscarTodosEventosPorEventoId(Long eventoId) {
    return eventosUsuarioRepository.findAllByEventoId(eventoId);
  }

  public List<EventosUsuario> buscarTodosEventosPorUsuarioId(Long usuarioId) {
    return eventosUsuarioRepository.findAllByUsuarioId(usuarioId);
  }

  public int buscarParticipantesEvento(Long eventoId) {
    return eventosUsuarioRepository.countEventosUsuarioByEventoId(eventoId);
  }

  public void apagarTodosUsuariosEvento(List<EventosUsuario> eventosUsuarios) {
    eventosUsuarioRepository.deleteAll(eventosUsuarios);
  }
}
