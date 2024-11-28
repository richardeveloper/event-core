package br.com.kafka.services;

import br.com.kafka.entities.Usuario;
import br.com.kafka.exceptions.ServiceException;
import br.com.kafka.repositories.UsuarioRepository;
import br.com.kafka.utils.ValidationUtils;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;

  private final GeradorMatriculaService geradorMatriculaService;

  public UsuarioService(UsuarioRepository usuarioRepository, GeradorMatriculaService geradorMatriculaService) {
    this.usuarioRepository = usuarioRepository;
    this.geradorMatriculaService = geradorMatriculaService;
  }

  public void salvarUsuario(Usuario usuario) throws ServiceException {
    validarCampos(usuario);

    if (usuarioRepository.existsByNome(usuario.getNome())) {
      throw new ServiceException("O nome %s já está sendo utilizado.".formatted(usuario.getNome()));
    }

    String matricula = geradorMatriculaService.gerarMatricula(LocalDateTime.now(), usuario.getCpf());
    usuario.setMatricula(matricula);

    usuarioRepository.save(usuario);
  }

  public List<Usuario> buscarTodosUsuarios() {
    return usuarioRepository.findAll();
  }

  public List<Usuario> buscarTodosUsuariosPorNome(String nome) {
    return usuarioRepository.findByNomeLike(nome);
  }

  public Usuario buscarUsuarioPorId(Long id) throws ServiceException {
    return usuarioRepository.findById(id)
      .orElseThrow(() -> new ServiceException("O usuário informado não foi identificado."));
  }

  public Usuario buscarUsuarioPorNome(String nome) throws ServiceException {
    return usuarioRepository.findByNome(nome)
      .orElseThrow(() -> new ServiceException("Não foi idetificado usuário com nome %s.".formatted(nome)));
  }

  public void apagarUsuario(Long id) throws ServiceException {
    try {
      usuarioRepository.deleteById(id);
    }
    catch (DataIntegrityViolationException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Não foi possível apagar o usuário, pois ele está inscrito em eventos.");
    }
  }

  private void validarCampos(Usuario usuario) throws ServiceException {
    ValidationUtils.validarNome(usuario.getNome());
    ValidationUtils.validarCpf(usuario.getCpf());
    ValidationUtils.validarEmail(usuario.getEmail());
    ValidationUtils.validarTelefone(usuario.getTelefone());
    ValidationUtils.validarCampo(usuario.getTipoUsuario(), "tipoUsuario");
  }

}
