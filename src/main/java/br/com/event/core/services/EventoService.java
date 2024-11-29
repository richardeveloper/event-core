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

  List<Evento> buscarTodosEventosPorStatus(StatusEventoEnum statusEvento);

  List<Evento> buscarTodosEventosPorPrioridade(PrioridadeEventoEnum prioridadeEvento);

  List<Evento> buscarEventosMaisProximos();

  List<Evento> buscarEventosAgendados();

  void apagarEvento(Long id) throws ServiceException;

}
