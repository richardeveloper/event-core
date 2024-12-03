package br.com.event.core.services.impl;

import br.com.event.core.rabbit.RabbitProducer;
import br.com.event.core.entities.Evento;
import br.com.event.core.entities.EventosUsuario;
import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.enums.TipoUsuarioEnum;
import br.com.event.core.exceptions.ServiceException;
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

  private final RabbitProducer rabbitProducer;

  public EventosUsuarioServiceImpl(EventosUsuarioRepository eventosUsuarioRepository,
    UsuarioRepository usuarioRepository, EventoRepository eventoRepository,
    RabbitProducer rabbitProducer) {

    this.eventosUsuarioRepository = eventosUsuarioRepository;
    this.usuarioRepository = usuarioRepository;
    this.eventoRepository = eventoRepository;
    this.rabbitProducer = rabbitProducer;
  }

  @Override
  public void realizarInscricao(Long usuarioId, Long eventoId) throws ServiceException {
    Usuario usuario = usuarioRepository.findById(usuarioId)
      .orElseThrow(() -> new ServiceException("O usuário informado não foi identificado."));

    Evento evento = eventoRepository.findById(eventoId)
      .orElseThrow(() -> new ServiceException("O evento informado não foi identificado."));

    switch (evento.getStatus()) {
      case EM_ANDAMENTO -> throw new ServiceException("Não foi possível realizar a inscrição pois o evento já está em andamento.");
      case FINALIZADO -> throw new ServiceException("Não foi possível realizar a inscrição pois o evento já foi finalizado.");
      case CANCELADO -> throw new ServiceException("Não foi possível realizar a inscrição pois o evento foi cancelado.");
    }

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

    EventosUsuario eventosUsuario = EventosUsuario.builder()
      .usuario(usuario)
      .evento(evento)
      .build();

    eventosUsuarioRepository.save(eventosUsuario);

    String message = "Sua inscrição no evento %s foi realizada com sucesso.".formatted(evento.getNome());

    rabbitProducer.sendMessage(usuario, evento, TipoNotificacaoEnum.INSCRICAO_CONFIRMADA, message);
  }

  @Override
  @Transactional
  public void cancelarInscricao(Long usuarioId, Long eventoId) throws ServiceException {
    Usuario usuario = usuarioRepository.findById(usuarioId)
      .orElseThrow(() -> new ServiceException("O usuário informado não foi identificado."));

    Evento evento = eventoRepository.findById(eventoId)
      .orElseThrow(() -> new ServiceException("O evento informado não foi identificado."));

    switch (evento.getStatus()) {
      case EM_ANDAMENTO -> throw new ServiceException("Não foi possível cancelar a inscrição pois o evento já está em andamento.");
      case FINALIZADO -> throw new ServiceException("Não foi possível cancelar a inscrição pois o evento já foi finalizado.");
      case CANCELADO -> throw new ServiceException("Não foi possível cancelar a inscrição pois o evento foi cancelado.");
    }

    Optional<EventosUsuario> eventos = eventosUsuarioRepository.findByEventoAndUsuario(evento, usuario);

    if (eventos.isEmpty()) {
      log.info("O usuário {} não está inscrito no evento {}", usuario.getNome(), evento.getNome());
      return;
    }

    EventosUsuario eventosUsuario = eventos.get();

    eventosUsuarioRepository.delete(eventosUsuario);

    String message = "Sua inscrição no evento %s foi cancelada com sucesso.".formatted(evento.getNome());

    rabbitProducer.sendMessage(usuario, evento, TipoNotificacaoEnum.INSCRICAO_CANCELADA, message);
  }

  @Override
  public List<EventosUsuario> buscarTodosEventosUsuarioPorEventoId(Long eventoId) {
    return eventosUsuarioRepository.findAllByEventoId(eventoId);
  }

  @Override
  public List<EventosUsuario> buscarTodosEventosUsuarioPorUsuarioId(Long usuarioId) {
    return eventosUsuarioRepository.findAllByUsuarioId(usuarioId);
  }

  @Override
  public int buscarQuantidadeParticipantes(Long eventoId) {
    return eventosUsuarioRepository.countEventosUsuarioByEventoId(eventoId);
  }

  @Override
  public void apagarTodosUsuariosEvento(List<EventosUsuario> eventosUsuarios) {
    eventosUsuarioRepository.deleteAll(eventosUsuarios);
  }
}
