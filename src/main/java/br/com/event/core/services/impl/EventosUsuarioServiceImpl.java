package br.com.event.core.services.impl;

import br.com.event.core.entities.Evento;
import br.com.event.core.entities.EventosUsuario;
import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.enums.TipoUsuarioEnum;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.kafka.KafkaProducer;
import br.com.event.core.repositories.EventoRepository;
import br.com.event.core.repositories.EventosUsuarioRepository;
import br.com.event.core.repositories.UsuarioRepository;
import br.com.event.core.services.EventosUsuarioService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class EventosUsuarioServiceImpl implements EventosUsuarioService {

  private final EventosUsuarioRepository eventosUsuarioRepository;

  private final UsuarioRepository usuarioRepository;

  private final EventoRepository eventoRepository;

  private final KafkaProducer kafkaProducer;

  public EventosUsuarioServiceImpl(EventosUsuarioRepository eventosUsuarioRepository,
    UsuarioRepository usuarioRepository, EventoRepository eventoRepository,
    KafkaProducer kafkaProducer) {

    this.eventosUsuarioRepository = eventosUsuarioRepository;
    this.usuarioRepository = usuarioRepository;
    this.eventoRepository = eventoRepository;
    this.kafkaProducer = kafkaProducer;
  }

  @Override
  public void realizarInscricao(Long usuarioId, Long eventoId) throws ServiceException {
    Usuario usuario = usuarioRepository.findById(usuarioId)
      .orElseThrow(() -> new ServiceException("O usuário informado não foi identificado."));

    Evento evento = eventoRepository.findById(eventoId)
      .orElseThrow(() -> new ServiceException("O evento informado não foi identificado."));

    switch (evento.getPrioridade()) {
      case SOMENTE_ALUNOS -> {
        if (!usuario.getTipoUsuario().equals(TipoUsuarioEnum.ALUNO)) {
          throw new ServiceException("O evento %s é oferecido somente para alunos.".formatted(evento.getNome()));
        }
      }
      case SOMENTE_PROFESSORES -> {
        if (!usuario.getTipoUsuario().equals(TipoUsuarioEnum.PROFESSOR)) {
          throw new ServiceException("O evento %s é oferecido somente para professores.".formatted(evento.getNome()));
        }
      }
    }

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

  @Override
  @Transactional
  public void cancelarInscricao(Long usuarioId, Long eventoId) throws ServiceException {
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

  @Override
  public List<EventosUsuario> buscarTodosEventosPorEventoId(Long eventoId) {
    return eventosUsuarioRepository.findAllByEventoId(eventoId);
  }

  @Override
  public List<EventosUsuario> buscarTodosEventosPorUsuarioId(Long usuarioId) {
    return eventosUsuarioRepository.findAllByUsuarioId(usuarioId);
  }

  @Override
  public int buscarParticipantesEvento(Long eventoId) {
    return eventosUsuarioRepository.countEventosUsuarioByEventoId(eventoId);
  }

  @Override
  public void apagarTodosUsuariosEvento(List<EventosUsuario> eventosUsuarios) {
    eventosUsuarioRepository.deleteAll(eventosUsuarios);
  }
}
