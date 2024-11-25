package br.com.kafka.services;

import br.com.kafka.entities.Evento;
import br.com.kafka.exceptions.ServiceException;
import br.com.kafka.repositories.EventoRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventoService {

  @Autowired
  private EventoRepository eventoRepository;

  public Evento save(Evento evento) {
    if (eventoRepository.existsByNome(evento.getNome())) {
      throw new ServiceException("O nome %s já está sendo utilizado.".formatted(evento.getNome()));
    }

    return eventoRepository.save(evento);
  }

  public List<Evento> findAll() {
    return eventoRepository.findAll();
  }



  public void delete(Long id) {
    eventoRepository.deleteById(id);
  }

  public List<Evento> findAllOrderByDataDesc() {
    return eventoRepository.findAllOrderByDataDesc();
  }

  public Evento update(Long id, Evento novoEvento) {
    Evento evento = eventoRepository.findById(id)
      .orElseThrow(() -> new ServiceException("Evento não encontrado."));

    evento.setNome(novoEvento.getNome());
    evento.setData(LocalDateTime.parse(novoEvento.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    evento.setDuracao(novoEvento.getDuracao());

    return eventoRepository.save(evento);
  }
}
