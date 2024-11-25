package br.com.kafka.configs;

import br.com.kafka.entities.Evento;
import br.com.kafka.entities.Usuario;
import br.com.kafka.enums.PrioridadeEventoEnum;
import br.com.kafka.enums.StatusEventoEnum;
import br.com.kafka.repositories.EventoRepository;
import br.com.kafka.repositories.UsuarioRepository;
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


  @Override
  public void run(String... args) throws Exception {
    Usuario usuario1 = new Usuario();
    usuario1.setNome("Usuario I");
    usuario1.setCpf("12345678900");
    usuario1.setEmail("usuario1@email.com");
    usuario1.setTelefone("12345678900");

    Usuario usuario2 = new Usuario();
    usuario2.setNome("Usuario II");
    usuario2.setCpf("98765432100");
    usuario2.setEmail("usuario2@email.com");
    usuario2.setTelefone("00987654321");

    usuarioRepository.saveAll(List.of(usuario1, usuario2));

    Evento evento1 = new Evento();
    evento1.setNome("Grande evento espetacular maravilhoso");
    evento1.setData(LocalDateTime.now().minusDays(10));
    evento1.setDuracao(LocalTime.of(2, 30));
    evento1.setStatus(StatusEventoEnum.FINALIZADO);
    evento1.setPrioridade(PrioridadeEventoEnum.OPCIONAL);

    Evento evento2 = new Evento();
    evento2.setNome("Grande palestra espetacular maravilhosa");
    evento2.setData(LocalDateTime.now().plusDays(10));
    evento2.setDuracao(LocalTime.of(2, 30));
    evento2.setStatus(StatusEventoEnum.AGENDADO);
    evento2.setPrioridade(PrioridadeEventoEnum.OPCIONAL);

    eventoRepository.saveAll(List.of(evento1, evento2));
  }
}
