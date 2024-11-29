package br.com.event.core.services;

import br.com.event.core.entities.Evento;
import br.com.event.core.entities.EventosUsuario;
import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.kafka.KafkaProducer;
import br.com.event.core.repositories.EventoRepository;
import br.com.event.core.repositories.EventosUsuarioRepository;
import br.com.event.core.repositories.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  private final KafkaProducer kafkaProducer;

  public EventosUsuarioService(EventosUsuarioRepository eventosUsuarioRepository,
    UsuarioRepository usuarioRepository, EventoRepository eventoRepository,
    KafkaProducer kafkaProducer) {

    this.eventosUsuarioRepository = eventosUsuarioRepository;
    this.usuarioRepository = usuarioRepository;
    this.eventoRepository = eventoRepository;
    this.kafkaProducer = kafkaProducer;
  }

  public void realizarInscricao(Long usuarioId, Long eventoId) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
      .orElseThrow(() -> new ServiceException("O usuário informado não foi identificado."));

    Evento evento = eventoRepository.findById(eventoId)
      .orElseThrow(() -> new ServiceException("O evento informado não foi identificado."));

    Optional<EventosUsuario> eventos = eventosUsuarioRepository.findByEventoAndUsuario(evento, usuario);

    if (eventos.isPresent()) {
      log.info("O usuário {} já está inscrito no evento {}", usuario.getNome(), evento.getNome());
      return;
    }

    EventosUsuario eventosUsuario = new EventosUsuario();
    eventosUsuario.setEvento(evento);
    eventosUsuario.setUsuario(usuario);

    eventosUsuarioRepository.save(eventosUsuario);

    String message = "%s sua inscrição no evento %s foi realizada com sucesso."
      .formatted(usuario.getNome(), evento.getNome());

    kafkaProducer.sendMessage(TipoNotificacaoEnum.INSCRICAO_CONFIRMADA, message);
  }

  @Transactional
  public void cancelarInscricao(Long usuarioId, Long eventoId) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
      .orElseThrow(() -> new ServiceException("O usuário informado não foi identificado."));

    Evento evento = eventoRepository.findById(eventoId)
      .orElseThrow(() -> new ServiceException("O evento informado não foi identificado."));

    Optional<EventosUsuario> eventos = eventosUsuarioRepository.findByEventoAndUsuario(evento, usuario);

    if (eventos.isEmpty()) {
      log.info("O usuário {} não está inscrito no evento {}", usuario.getNome(), evento.getNome());
      return;
    }

    EventosUsuario eventosUsuario = eventos.get();

    eventosUsuarioRepository.delete(eventosUsuario);

    String message = "%s sua inscrição no evento %s foi cancelada com sucesso."
      .formatted(usuario.getNome(), evento.getNome());

    kafkaProducer.sendMessage(TipoNotificacaoEnum.INSCRICAO_CANCELADA, message);
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
