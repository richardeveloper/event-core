package br.com.kafka.services;

import br.com.kafka.entities.Usuario;
import br.com.kafka.exceptions.ServiceException;
import br.com.kafka.repositories.UsuarioRepository;
import br.com.kafka.utils.ValidationUtils;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;

  public UsuarioService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public void salvarUsuario(Usuario usuario) {
    validarCampos(usuario);

    if (usuarioRepository.existsByNome(usuario.getNome())) {
      throw new ServiceException("O nome %s já está sendo utilizado.".formatted(usuario.getNome()));
    }

    usuarioRepository.save(usuario);
  }

  public Usuario buscarUsuarioPorId(Long id) {
    return usuarioRepository.findById(id)
      .orElseThrow(() -> new ServiceException("O usuário informado não foi identificado."));
  }

  public Usuario buscarUsuarioPorNome(String nome) {
    return usuarioRepository.findByNome(nome)
      .orElseThrow(() -> new ServiceException("Não foi idetificado usuário com nome %s.".formatted(nome)));
  }

  public List<Usuario> buscarTodosUsuarios() {
    return usuarioRepository.findAll();
  }

  public void apagarUsuario(Long id) {
    usuarioRepository.deleteById(id);
  }

  private void validarCampos(Usuario usuario) {
    ValidationUtils.validarNome(usuario.getNome());
    ValidationUtils.validarCpf(usuario.getCpf());
    ValidationUtils.validarEmail(usuario.getEmail());
    ValidationUtils.validarTelefone(usuario.getTelefone());
    ValidationUtils.validarCampo(usuario.getTipoUsuario(), "tipoUsuario");
  }

  public List<Usuario> buscarUsuarioPorNomeLike(String nome) {
    return usuarioRepository.findByNomeLike(nome);
  }
}
