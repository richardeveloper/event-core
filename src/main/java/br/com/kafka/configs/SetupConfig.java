package br.com.kafka.configs;

import br.com.kafka.entities.Evento;
import br.com.kafka.entities.Usuario;
import br.com.kafka.enums.PrioridadeEventoEnum;
import br.com.kafka.enums.StatusEventoEnum;
import br.com.kafka.enums.TipoUsuarioEnum;
import br.com.kafka.repositories.EventoRepository;
import br.com.kafka.repositories.UsuarioRepository;
import br.com.kafka.services.EventoService;
import br.com.kafka.services.UsuarioService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SetupConfig implements CommandLineRunner {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private EventoRepository eventoRepository;

  @Autowired
  private EventoService eventoService;

  @Autowired
  private UsuarioService usuarioService;

  @Override
  public void run(String... args) throws Exception {
    Usuario usuario1 = new Usuario();
    usuario1.setNome("Erich Gamma");
    usuario1.setCpf("12345678900");
    usuario1.setEmail("erich.gamma@email.com");
    usuario1.setTelefone("12345678900");
    usuario1.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario2 = new Usuario();
    usuario2.setNome("Richard Helm");
    usuario2.setCpf("98765432100");
    usuario2.setEmail("richard.helm@email.com");
    usuario2.setTelefone("00987654321");
    usuario2.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario3 = new Usuario();
    usuario3.setNome("Ralph Jhonson");
    usuario3.setCpf("98523648121");
    usuario3.setEmail("ralph.jhonson@email.com");
    usuario3.setTelefone("62698575266");
    usuario3.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario4 = new Usuario();
    usuario4.setNome("Jhon Vlissides");
    usuario4.setCpf("65897410253");
    usuario4.setEmail("john.vlissides@email.com");
    usuario4.setTelefone("98523654120");
    usuario4.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    List<Usuario> usuarios = usuarioRepository.saveAll(List.of(usuario1, usuario2, usuario3, usuario4));

    Evento evento1 = new Evento();
    evento1.setNome("Como perder tempo com JavaFX");
    evento1.setData(LocalDateTime.now().minusDays(10));
    evento1.setDuracao(LocalTime.of(2, 30));
    evento1.setStatus(StatusEventoEnum.FINALIZADO);
    evento1.setPrioridade(PrioridadeEventoEnum.ABERTO);

    Evento evento2 = new Evento();
    evento2.setNome("Impactos do uso do JavaFX na socidade");
    evento2.setData(LocalDateTime.now().plusDays(2).plusHours(4).minusMinutes(40));
    evento2.setDuracao(LocalTime.of(2, 30));
    evento2.setStatus(StatusEventoEnum.AGENDADO);
    evento2.setPrioridade(PrioridadeEventoEnum.ABERTO);

    Evento evento3 = new Evento();
    evento3.setNome("A evolução do JavaFX no século XXI");
    evento3.setData(LocalDateTime.now().plusDays(6).plusHours(2).minusMinutes(10));
    evento3.setDuracao(LocalTime.of(2, 30));
    evento3.setStatus(StatusEventoEnum.AGENDADO);
    evento3.setPrioridade(PrioridadeEventoEnum.ABERTO);

    List<Evento> eventos = eventoRepository.saveAll(List.of(evento1, evento2, evento3));

    eventoService.realizarInscricaoUsuario(usuarios.get(0).getId(), eventos.get(0).getId());
  }
}
