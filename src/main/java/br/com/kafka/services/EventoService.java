package br.com.kafka.services;

import br.com.kafka.entities.Evento;
import br.com.kafka.entities.Usuario;
import br.com.kafka.enums.StatusEventoEnum;
import br.com.kafka.enums.TipoUsuarioEnum;
import br.com.kafka.exceptions.ServiceException;
import br.com.kafka.repositories.EventoRepository;
import br.com.kafka.repositories.UsuarioRepository;
import br.com.kafka.utils.ValidationUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventoService {

  private final EventoRepository eventoRepository;

  private final UsuarioRepository usuarioRepository;

  public EventoService(EventoRepository eventoRepository, UsuarioRepository usuarioRepository) {
    this.eventoRepository = eventoRepository;
    this.usuarioRepository = usuarioRepository;
  }

  public void salvarEvento(Evento evento) {
    validarCampos(evento);

    if (eventoRepository.existsByNome(evento.getNome())) {
      throw new ServiceException("O nome %s já está sendo utilizado.".formatted(evento.getNome()));
    }

    Evento save = eventoRepository.save(evento);

    switch (evento.getPrioridade()) {
      case ABERTO -> {}
      case OBRIGATORIO_ALUNOS -> {
        List<Usuario> alunos = usuarioRepository.findByTipoUsuario(TipoUsuarioEnum.ALUNO);
        alunos.forEach(aluno -> realizarInscricaoUsuario(aluno.getId(), save.getId()));
        log.info("Alunos cadastrados no evento {}.", evento.getNome());
      }
      case OBRIGATORIO_PROFESSORES -> {
        List<Usuario> professores = usuarioRepository.findByTipoUsuario(TipoUsuarioEnum.PROFESSOR);
        professores.forEach(professor -> realizarInscricaoUsuario(professor.getId(), save.getId()));
        log.info("Professores cadastrados no evento {}.", evento.getNome());
      }
    }
  }

  public void editarEvento(Long id, Evento novoEvento) {
    validarCampos(novoEvento);

    Evento evento = buscarEventoPorId(id);

    evento.setNome(novoEvento.getNome());
    evento.setData(novoEvento.getData());
    evento.setDuracao(novoEvento.getDuracao());

    eventoRepository.save(evento);
  }

  public void realizarInscricaoUsuario(Long usuarioId, Long eventoId) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
      .orElseThrow(() -> new ServiceException("O usuário informado não foi identificado."));

    Evento evento = buscarEventoPorId(eventoId);

    if (evento.getParticipantes().contains(usuario)) {
      log.info("O usuário {} já está inscrito no evento {}", usuario.getNome(), evento.getNome());
      return;
    }

    evento.adicionarParticipante(usuario);

    eventoRepository.save(evento);
  }

  public void cancelarInscricaoUsuario(Long usuarioId, Long eventoId) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
      .orElseThrow(() -> new ServiceException("O usuário informado não foi identificado."));

    Evento evento = buscarEventoPorId(eventoId);

    if (!evento.getParticipantes().contains(usuario)) {
      log.info("O usuário {} não está inscrito no evento {}", usuario.getNome(), evento.getNome());
      return;
    }

    evento.removerParticipante(usuario);

    eventoRepository.save(evento);
  }

  public void cancelarEvento(Long id) {
    Evento evento = buscarEventoPorId(id);

    evento.setStatus(StatusEventoEnum.CANCELADO);
    evento.getParticipantes().clear();

    eventoRepository.save(evento);
  }

  public Evento buscarEventoPorId(Long id) {
    return eventoRepository.findById(id)
      .orElseThrow(() -> new ServiceException("O evento informado não foi identificado."));
  }

  public List<Evento> buscarTodosEventos() {
    return eventoRepository.findAll();
  }

  public List<Evento> buscarEventosMaisProximos() {
    return eventoRepository.findAllOrderByDataDesc();
  }

  public List<Evento> buscarEventosAgendados() {
    return eventoRepository.findAllByStatus(StatusEventoEnum.AGENDADO);
  }

  public void apagarEvento(Long id) {
    eventoRepository.deleteById(id);
  }

  private void validarCampos(Evento evento) {
    ValidationUtils.validarNome(evento.getNome());
    ValidationUtils.validarData(evento.getData());
    ValidationUtils.validarDuracao(evento.getDuracao());
    ValidationUtils.validarCampo(evento.getPrioridade(), "prioridade");
  }

  public List<Evento> buscarEventosUsuario(Long usuarioId) {
    return eventoRepository.findAllByUsuarioId(usuarioId);
  }
}
