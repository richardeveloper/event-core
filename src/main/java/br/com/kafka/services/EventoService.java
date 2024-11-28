package br.com.kafka.services;

import br.com.kafka.entities.Evento;
import br.com.kafka.entities.EventosUsuario;
import br.com.kafka.entities.Usuario;
import br.com.kafka.enums.StatusEventoEnum;
import br.com.kafka.enums.TipoUsuarioEnum;
import br.com.kafka.exceptions.ServiceException;
import br.com.kafka.repositories.EventoRepository;
import br.com.kafka.repositories.UsuarioRepository;
import br.com.kafka.utils.ValidationUtils;
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

  public EventoService(EventoRepository eventoRepository, UsuarioRepository usuarioRepository,
    EventosUsuarioService eventosUsuarioService) {

    this.eventoRepository = eventoRepository;
    this.usuarioRepository = usuarioRepository;
    this.eventosUsuarioService = eventosUsuarioService;
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

    evento.setNome(novoEvento.getNome());
    evento.setData(novoEvento.getData());
    evento.setDuracao(novoEvento.getDuracao());

    eventoRepository.save(evento);
  }

  public void cancelarEvento(Long id) throws ServiceException {
    Evento evento = buscarEventoPorId(id);

    evento.setStatus(StatusEventoEnum.CANCELADO);

    List<EventosUsuario> eventosUsuarios = eventosUsuarioService.buscarTodosEventosPorEventoId(evento.getId());
    eventosUsuarioService.apagarTodosUsuariosEvento(eventosUsuarios);

    eventoRepository.save(evento);
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
      throw new ServiceException("Não foi possível apagar o evento, pois existem usuários inscritos nele.");
    }
  }

  private void validarCampos(Evento evento) throws ServiceException {
    ValidationUtils.validarNome(evento.getNome());
    ValidationUtils.validarData(evento.getData());
    ValidationUtils.validarDuracao(evento.getDuracao());
    ValidationUtils.validarCampo(evento.getPrioridade(), "prioridade");
  }

}
