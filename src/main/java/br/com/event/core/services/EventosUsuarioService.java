package br.com.event.core.services;

import br.com.event.core.entities.EventosUsuario;
import br.com.event.core.exceptions.ServiceException;
import java.util.List;

public interface EventosUsuarioService {

  void realizarInscricao(Long usuarioId, Long eventoId) throws ServiceException;

  void cancelarInscricao(Long usuarioId, Long eventoId) throws ServiceException;

  List<EventosUsuario> buscarTodosEventosPorEventoId(Long eventoId);

  List<EventosUsuario> buscarTodosEventosPorUsuarioId(Long usuarioId);

  int buscarParticipantesEvento(Long eventoId);

  void apagarTodosUsuariosEvento(List<EventosUsuario> eventosUsuarios);

}
