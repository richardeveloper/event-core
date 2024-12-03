package br.com.event.core.services;

import br.com.event.core.entities.Evento;
import br.com.event.core.enums.PrioridadeEventoEnum;
import br.com.event.core.enums.StatusEventoEnum;
import br.com.event.core.exceptions.ServiceException;
import java.util.List;

public interface EventoService {

  void salvarEvento(Evento evento) throws ServiceException;
  
  void editarEvento(Long id, Evento novoEvento) throws ServiceException;
  
  void cancelarEvento(Long id) throws ServiceException;

  Evento buscarEventoPorId(Long id) throws ServiceException;

  List<Evento> buscarTodosEventos();

  List<Evento> buscarTodosEventosPorStatus(List<StatusEventoEnum> statusEvento);

  List<Evento> buscarTodosEventosPorPrioridades(List<PrioridadeEventoEnum> prioridades);

  List<Evento> buscarEventosMaisProximos();

  List<Evento> buscarEventosAgendados();

  void apagarEvento(Long id) throws ServiceException;
}
