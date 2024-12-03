package br.com.event.core.services;

import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.TipoUsuarioEnum;
import br.com.event.core.exceptions.ServiceException;
import java.util.List;

public interface UsuarioService {

  void salvarUsuario(Usuario usuario) throws ServiceException;

  Usuario buscarUsuarioPorId(Long id) throws ServiceException;

  Usuario buscarUsuarioPorNome(String nome) throws ServiceException;

  List<Usuario> buscarTodosUsuarios();

  List<Usuario> buscarTodosUsuariosPorNome(String nome);

  List<Usuario> buscarTodosUsuariosPorTiposUsuarios(List<TipoUsuarioEnum> tipoUsuario);

  void apagarUsuario(Long id) throws ServiceException;

}
