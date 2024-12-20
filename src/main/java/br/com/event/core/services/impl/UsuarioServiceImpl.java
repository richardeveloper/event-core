package br.com.event.core.services.impl;

import br.com.event.core.entities.EventosUsuario;
import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.TipoUsuarioEnum;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.repositories.UsuarioRepository;
import br.com.event.core.services.EventosUsuarioService;
import br.com.event.core.services.GeradorMatriculaService;
import br.com.event.core.services.UsuarioService;
import br.com.event.core.utils.MaskUtils;
import br.com.event.core.utils.ValidationUtils;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {

  private final UsuarioRepository usuarioRepository;

  private final GeradorMatriculaService geradorMatriculaService;

  private final EventosUsuarioService eventosUsuarioService;

  public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
    GeradorMatriculaServiceImpl geradorMatriculaService, EventosUsuarioService eventosUsuarioService) {

    this.usuarioRepository = usuarioRepository;
    this.geradorMatriculaService = geradorMatriculaService;
    this.eventosUsuarioService = eventosUsuarioService;
  }

  @Override
  public void salvarUsuario(Usuario usuario) throws ServiceException {
    validarCampos(usuario);

    if (usuarioRepository.existsByNome(usuario.getNome())) {
      throw new ServiceException("O nome %s já está sendo utilizado.".formatted(usuario.getNome()));
    }

    if (usuarioRepository.existsByCpf(usuario.getCpf())) {
      throw new ServiceException("O cpf %s já está sendo utilizado.".formatted(MaskUtils.applyMaskCpf(usuario.getCpf())));
    }

    if (usuarioRepository.existsByEmail(usuario.getEmail())) {
      throw new ServiceException("O email %s já está sendo utilizado.".formatted(usuario.getEmail()));
    }

    if (usuarioRepository.existsByTelefone(usuario.getTelefone())) {
      throw new ServiceException("O telefone %s já está sendo utilizado.".formatted(MaskUtils.applyMaskPhone(usuario.getTelefone())));
    }

    if (!usuario.getTipoUsuario().equals(TipoUsuarioEnum.VISITANTE)) {
      String matricula = geradorMatriculaService.gerarMatricula(LocalDateTime.now(), usuario.getCpf());
      usuario.setMatricula(matricula);
    }

    usuarioRepository.save(usuario);
  }

  @Override
  public Usuario buscarUsuarioPorId(Long id) throws ServiceException {
    return usuarioRepository.findById(id)
      .orElseThrow(() -> new ServiceException("O usuário informado não foi identificado."));
  }

  public Usuario buscarUsuarioPorNome(String nome) throws ServiceException {
    return usuarioRepository.findByNome(nome)
      .orElseThrow(() -> new ServiceException("Não foi idetificado usuário com nome %s.".formatted(nome)));
  }

  @Override
  public List<Usuario> buscarTodosUsuarios() {
    return usuarioRepository.findAll();
  }

  @Override
  public List<Usuario> buscarTodosUsuariosPorNome(String nome) {
    return usuarioRepository.findByNomeLike(nome);
  }

  @Override
  public List<Usuario> buscarTodosUsuariosPorTiposUsuarios(List<TipoUsuarioEnum> tiposUsuarios) {
    return usuarioRepository.findByTipoUsuarioIn(tiposUsuarios);
  }

  public void apagarUsuario(Long id) throws ServiceException {
    Usuario usuario = buscarUsuarioPorId(id);

    List<EventosUsuario> eventosUsuarios = eventosUsuarioService.buscarTodosEventosUsuarioPorUsuarioId(usuario.getId());

    if (!eventosUsuarios.isEmpty()) {
      eventosUsuarioService.apagarTodosUsuariosEvento(eventosUsuarios);
    }

    usuarioRepository.delete(usuario);
  }

  private void validarCampos(Usuario usuario) throws ServiceException {
    ValidationUtils.validarNome(usuario.getNome());
    ValidationUtils.validarCpf(usuario.getCpf());
    ValidationUtils.validarEmail(usuario.getEmail());
    ValidationUtils.validarTelefone(usuario.getTelefone());
    ValidationUtils.validarCampo(usuario.getTipoUsuario(), "tipoUsuario");
  }
}
