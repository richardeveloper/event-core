package br.com.kafka.configs;

import br.com.kafka.entities.Evento;
import br.com.kafka.entities.Usuario;
import br.com.kafka.enums.PrioridadeEventoEnum;
import br.com.kafka.enums.StatusEventoEnum;
import br.com.kafka.enums.TipoUsuarioEnum;
import br.com.kafka.repositories.EventoRepository;
import br.com.kafka.repositories.UsuarioRepository;
import br.com.kafka.services.EventoService;
import br.com.kafka.services.EventosUsuarioService;
import br.com.kafka.services.UsuarioService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class StarterConfiguration implements CommandLineRunner {

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private EventoService eventoService;

  @Autowired
  private EventosUsuarioService eventosUsuarioService;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private EventoRepository eventoRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public void run(String... args) throws Exception {
    configSequence();
    configUsuarios();
    configEventos();
  }

  private void configSequence() {
    String dropSequenceSQL = "DROP SEQUENCE IF EXISTS user_code_seq";

    jdbcTemplate.execute(dropSequenceSQL);

    String createSequenceSQL =
      "CREATE SEQUENCE IF NOT EXISTS user_code_seq " +
      "START WITH 1 " +
      "INCREMENT BY 1 " +
      "NO MINVALUE " +
      "NO MAXVALUE " +
      "CACHE 1;";

    jdbcTemplate.execute(createSequenceSQL);
  }

  private void configUsuarios() {
    Usuario usuario1 = new Usuario();
    usuario1.setNome("Adao Luiz Ferreira");
    usuario1.setCpf("26208784620");
    usuario1.setEmail("adao.luiz@outlook.com");
    usuario1.setTelefone("30400807092");
    usuario1.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario2 = new Usuario();
    usuario2.setNome("Celia Caetano de Maedeiros");
    usuario2.setCpf("34310279031");
    usuario2.setEmail("celia.caetano@gmail.com");
    usuario2.setTelefone("65758688482");
    usuario2.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario3 = new Usuario();
    usuario3.setNome("Eduardo Ambrosio");
    usuario3.setCpf("15738582004");
    usuario3.setEmail("eduardo.ambrosio@yahoo.com.br");
    usuario3.setTelefone("80398763011");
    usuario3.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario4 = new Usuario();
    usuario4.setNome("Raiumunda Cardoso de Freitas");
    usuario4.setCpf("78791968038");
    usuario4.setEmail("raiumunda.cardoso@uol.com");
    usuario4.setTelefone("36568875408");
    usuario4.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario5 = new Usuario();
    usuario5.setNome("Francisco Antonio Cardoso");
    usuario5.setCpf("49276781056");
    usuario5.setEmail("francisco.antonio@gmail.com");
    usuario5.setTelefone("69202950714");
    usuario5.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario6 = new Usuario();
    usuario6.setNome("Nelci Campos Matias");
    usuario6.setCpf("64422062034");
    usuario6.setEmail("nelci.campos@hotmail.com");
    usuario6.setTelefone("79810547168");
    usuario6.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario7 = new Usuario();
    usuario7.setNome("Maria Alice Martins");
    usuario7.setCpf("99348682024");
    usuario7.setEmail("maria.alice@yahoo.com.br");
    usuario7.setTelefone("27554020367");
    usuario7.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario8 = new Usuario();
    usuario8.setNome("Luana Janaina Lopes");
    usuario8.setCpf("04694988034");
    usuario8.setEmail("luana.janaina@gmail.com");
    usuario8.setTelefone("42114234713");
    usuario8.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario9 = new Usuario();
    usuario9.setNome("Maria Helena Rodrigues");
    usuario9.setCpf("41737855003");
    usuario9.setEmail("maria.helena@outlook.com");
    usuario9.setTelefone("86388919773");
    usuario9.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario10 = new Usuario();
    usuario10.setNome("Vera Lucia de Araujo");
    usuario10.setCpf("01640297022");
    usuario10.setEmail("vera.lucia@yahoo.com.br");
    usuario10.setTelefone("77128568464");
    usuario10.setTipoUsuario(TipoUsuarioEnum.ALUNO);

    Usuario usuario11 = new Usuario();
    usuario11.setNome("Fabrício Morais Almeira");
    usuario11.setCpf("69852314651");
    usuario11.setEmail("fabricio.morais@outlook.com");
    usuario11.setTelefone("62985632145");
    usuario11.setTipoUsuario(TipoUsuarioEnum.PROFESSOR);

    Usuario usuario12 = new Usuario();
    usuario12.setNome("Mônica Alves de Araujo");
    usuario12.setCpf("16325988740");
    usuario12.setEmail("monica.alves@yahoo.com.br");
    usuario12.setTelefone("83928568464");
    usuario12.setTipoUsuario(TipoUsuarioEnum.PROFESSOR);

    List<Usuario> usuarios = List.of(usuario1, usuario2, usuario3, usuario4, usuario5, usuario6,
      usuario7, usuario8, usuario9, usuario10, usuario11, usuario12);

    usuarios.forEach(usuario -> usuarioService.salvarUsuario(usuario));
  }

  private void configEventos() {
    Evento evento1 = new Evento();
    evento1.setNome("Desvendando a Inteligência Artificial");
    evento1.setData(LocalDateTime.now().plusHours(2));
    evento1.setDuracao(LocalTime.of(1, 0));
    evento1.setStatus(StatusEventoEnum.AGENDADO);
    evento1.setPrioridade(PrioridadeEventoEnum.ABERTO);

    Evento evento2 = new Evento();
    evento2.setNome("A Arte da Arquitetura de Software");
    evento2.setData(LocalDateTime.now().plusDays(2).plusHours(4).minusMinutes(40));
    evento2.setDuracao(LocalTime.of(0, 50));
    evento2.setStatus(StatusEventoEnum.AGENDADO);
    evento2.setPrioridade(PrioridadeEventoEnum.ABERTO);

    Evento evento3 = new Evento();
    evento3.setNome("Boas práticas no ensino a distância");
    evento3.setData(LocalDateTime.now().plusDays(1).plusHours(6));
    evento3.setDuracao(LocalTime.of(2, 10));
    evento3.setStatus(StatusEventoEnum.AGENDADO);
//    evento3.setPrioridade(PrioridadeEventoEnum.OBRIGATORIO_PROFESSORES);
    evento3.setPrioridade(PrioridadeEventoEnum.ABERTO);

    Evento evento4 = new Evento();
    evento4.setNome("A Revolução dos Frameworks");
    evento4.setData(LocalDateTime.now().plusDays(10).plusHours(11).minusMinutes(30));
    evento4.setDuracao(LocalTime.of(1, 15));
    evento4.setStatus(StatusEventoEnum.AGENDADO);
    evento4.setPrioridade(PrioridadeEventoEnum.ABERTO);

    Evento evento5 = new Evento();
    evento5.setNome("Apresentação da ementa do curso");
    evento5.setData(LocalDateTime.now().plusDays(3).plusHours(3));
    evento5.setDuracao(LocalTime.of(1, 40));
    evento5.setStatus(StatusEventoEnum.AGENDADO);
    evento5.setPrioridade(PrioridadeEventoEnum.OBRIGATORIO_ALUNOS);

    List<Evento> eventos = List.of(evento1, evento2, evento3, evento4, evento5);

    eventos.forEach(evento -> eventoService.salvarEvento(evento));
  }
}
