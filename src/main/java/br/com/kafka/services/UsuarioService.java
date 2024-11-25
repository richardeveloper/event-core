package br.com.kafka.services;

import br.com.kafka.entities.Usuario;
import br.com.kafka.exceptions.ServiceException;
import br.com.kafka.repositories.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  public void save(Usuario usuario) {
    if (usuarioRepository.existsByNome(usuario.getNome())) {
      throw new ServiceException("O nome %s já está sendo utilizado.".formatted(usuario.getNome()));
    }

    usuarioRepository.save(usuario);
  }

  public List<Usuario> findAll() {
    return usuarioRepository.findAll();
  }

  public Usuario findByNome(String nome) {
    return usuarioRepository.findByNome(nome)
      .orElseThrow(() -> new ServiceException("Usuário não encontrado"));
  }

  public void delete(Long id) {
    usuarioRepository.deleteById(id);
  }

}
