package br.com.event.core.schedules;

import br.com.event.core.rabbit.RabbitProducer;
import br.com.event.core.entities.Evento;
import br.com.event.core.entities.EventosUsuario;
import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.StatusEventoEnum;
import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.repositories.EventoRepository;
import br.com.event.core.services.EventosUsuarioService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class EventScheduler {

  private final EventosUsuarioService eventosUsuarioService;

  private final EventoRepository eventoRepository;

  private final RabbitProducer rabbitProducer;

  public EventScheduler(EventosUsuarioService eventosUsuarioService, EventoRepository eventoService,
    RabbitProducer rabbitProducer) {

    this.eventosUsuarioService = eventosUsuarioService;
    this.eventoRepository = eventoService;
    this.rabbitProducer = rabbitProducer;
  }

  @Scheduled(fixedDelay = 60000)
  public void verificarEventosIniciados() {
    log.info("Iniciando verificação dos eventos agendados.");

    LocalDateTime data = LocalDate.now().atTime(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), 59);

    List<Evento> eventos = eventoRepository.findAllByDataAndStatus(data , StatusEventoEnum.AGENDADO);

    for (Evento evento : eventos) {
      evento.setStatus(StatusEventoEnum.EM_ANDAMENTO);
      eventoRepository.save(evento);

      String message = "O evento %s já vai começar, não perca !".formatted(evento.getNome());

      List<Usuario> usuarios = eventosUsuarioService.buscarTodosEventosUsuarioPorEventoId(evento.getId())
        .stream()
        .map(EventosUsuario::getUsuario)
        .toList();

      usuarios.forEach(usuario -> rabbitProducer.sendMessage(usuario, evento, TipoNotificacaoEnum.EVENTO_INICIADO, message));
    }

    log.info("Encerrando verificação dos eventos agendados.");
  }

  @Scheduled(fixedDelay = 60000)
  public void verificarEventosFinalizados() {
    log.info("Iniciando verificação dos eventos finalizados.");

    LocalDateTime data = LocalDate.now().atTime(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), 59);

    List<Evento> eventos = eventoRepository.findAllByDataAndStatus(data, StatusEventoEnum.EM_ANDAMENTO);

    for (Evento evento : eventos) {
      if (evento.getData().plusMinutes(evento.getDuracao().getLong(ChronoField.MINUTE_OF_DAY)).isBefore(LocalDateTime.now())) {

        evento.setStatus(StatusEventoEnum.FINALIZADO);
        Evento save = eventoRepository.save(evento);

        List<Usuario> usuarios = eventosUsuarioService.buscarTodosEventosUsuarioPorEventoId(save.getId())
          .stream()
          .map(EventosUsuario::getUsuario)
          .toList();

        String message = "Em breve o certificado do evento %s estará disponível no site.".formatted(evento.getNome());

        usuarios.forEach(usuario -> rabbitProducer.sendMessage(usuario, evento, TipoNotificacaoEnum.EVENTO_FINALIZADO, message));
      }
    }

    log.info("Encerrando verificação dos eventos finalizados.");
  }

}
