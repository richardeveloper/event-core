package br.com.event.core.services;

import br.com.event.core.entities.Evento;
import br.com.event.core.entities.EventosUsuario;
import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.StatusEventoEnum;
import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.enums.TipoUsuarioEnum;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.kafka.KafkaProducer;
import br.com.event.core.repositories.EventoRepository;
import br.com.event.core.repositories.UsuarioRepository;
import br.com.event.core.utils.ValidationUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventoService {

  private final EventoRepository eventoRepository;

  private final UsuarioRepository usuarioRepository;

  private final EventosUsuarioService eventosUsuarioService;

  private final KafkaProducer kafkaProducer;

  public EventoService(EventoRepository eventoRepository, UsuarioRepository usuarioRepository,
    EventosUsuarioService eventosUsuarioService, KafkaProducer kafkaProducer) {

    this.eventoRepository = eventoRepository;
    this.usuarioRepository = usuarioRepository;
    this.eventosUsuarioService = eventosUsuarioService;
    this.kafkaProducer = kafkaProducer;
  }

  public void salvarEvento(Evento evento) throws ServiceException {
    validarCampos(evento);

    if (eventoRepository.existsByNome(evento.getNome())) {
      throw new ServiceException("O nome %s já está sendo utilizado.".formatted(evento.getNome()));
    }

    Evento save = eventoRepository.save(evento);

    switch (evento.getPrioridade()) {
      case OBRIGATORIO_ALUNOS -> {
        List<Usuario> alunos = usuarioRepository.findByTipoUsuario(TipoUsuarioEnum.ALUNO);
        alunos.forEach(aluno -> eventosUsuarioService.realizarInscricao(aluno.getId(), save.getId()));

        log.info("Alunos cadastrados no evento {}.", evento.getNome());
      }
      case OBRIGATORIO_PROFESSORES -> {
        List<Usuario> professores = usuarioRepository.findByTipoUsuario(TipoUsuarioEnum.PROFESSOR);
        professores.forEach(professor -> eventosUsuarioService.realizarInscricao(professor.getId(), save.getId()));

        log.info("Professores cadastrados no evento {}.", evento.getNome());
      }
    }
  }

  public void editarEvento(Long id, Evento novoEvento) throws ServiceException {
    validarCampos(novoEvento);

    Evento evento = buscarEventoPorId(id);

    LocalDateTime dataAnterior = null;

    if (!evento.getData().equals(novoEvento.getData())) {
      dataAnterior = evento.getData();
    }

    evento.setNome(novoEvento.getNome());
    evento.setData(novoEvento.getData());
    evento.setDuracao(novoEvento.getDuracao());

    Evento save = eventoRepository.save(evento);

    if (dataAnterior != null) {
      List<Usuario> usuarios = eventosUsuarioService.buscarTodosEventosPorEventoId(save.getId())
        .stream()
        .map(EventosUsuario::getUsuario)
        .toList();

      for (Usuario usuario : usuarios) {
        String message = "Atenção %s ! O evento %s teve sua data alterada de %s para %s.".formatted(
          usuario.getNome(),
          evento.getNome(),
          dataAnterior.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
          novoEvento.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );

        log.info("Enviando notificação de alteração de data do evento.");
        kafkaProducer.sendMessage(TipoNotificacaoEnum.ALTERACAO_DATA_EVENTO, message);
      }
    }
  }

  public void cancelarEvento(Long id) throws ServiceException {
    Evento evento = buscarEventoPorId(id);

    evento.setStatus(StatusEventoEnum.CANCELADO);

    List<EventosUsuario> eventosUsuarios = eventosUsuarioService.buscarTodosEventosPorEventoId(evento.getId());

    if (eventosUsuarios.isEmpty()) {
      return;
    }

    List<Usuario> usuarios = eventosUsuarios
      .stream()
      .map(EventosUsuario::getUsuario)
      .toList();

    eventosUsuarioService.apagarTodosUsuariosEvento(eventosUsuarios);

    eventoRepository.save(evento);

    for (Usuario usuario : usuarios) {
      String message = "Atenção %s ! O evento %s foi cancelado, entre em contato para mais informações."
        .formatted(usuario.getNome(), eventosUsuarios.get(0).getEvento().getNome());

      log.info("Enviando notificação de cancelamento de evento.");
      kafkaProducer.sendMessage(TipoNotificacaoEnum.EVENTO_CANCELADO, message);
    }
  }

  public Evento buscarEventoPorId(Long id) throws ServiceException {
    return eventoRepository.findById(id)
      .orElseThrow(() -> new ServiceException("O evento informado não foi identificado."));
  }

  public List<Evento> buscarTodosEventos() {
    return eventoRepository.findAll();
  }

  public List<Evento> buscarEventosMaisProximos() {
    return eventoRepository.findAllOrderByData();
  }

  public List<Evento> buscarEventosAgendados() {
    return eventoRepository.findAllByStatus(StatusEventoEnum.AGENDADO);
  }

  public void apagarEvento(Long id) throws ServiceException {
    try {
      eventoRepository.deleteById(id);
    }
    catch (DataIntegrityViolationException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Não foi possível apagar o evento pois existem usuários inscritos nele, cancele o evento para continuar.");
    }
  }

  private void validarCampos(Evento evento) throws ServiceException {
    ValidationUtils.validarNome(evento.getNome());
    ValidationUtils.validarData(evento.getData());
    ValidationUtils.validarDuracao(evento.getDuracao());
    ValidationUtils.validarCampo(evento.getPrioridade(), "prioridade");
  }

}
