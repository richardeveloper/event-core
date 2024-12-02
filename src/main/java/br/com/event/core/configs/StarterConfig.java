package br.com.event.core.configs;

import br.com.event.core.entities.Evento;
import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.PrioridadeEventoEnum;
import br.com.event.core.enums.TipoUsuarioEnum;
import br.com.event.core.repositories.EventoRepository;
import br.com.event.core.repositories.UsuarioRepository;
import br.com.event.core.services.EventoService;
import br.com.event.core.services.EventosUsuarioService;
import br.com.event.core.services.UsuarioService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class StarterConfig implements CommandLineRunner {

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
  public void run(String... args) {
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
    Usuario usuario1 = Usuario.builder()
      .nome("Adao Luiz Ferreira")
      .cpf("26208784620")
      .email("adao.luiz@outlook.com")
      .telefone("30400807092")
      .tipoUsuario(TipoUsuarioEnum.ALUNO)
      .build();

    Usuario usuario2 = Usuario.builder()
      .nome("Celia Caetano de Maedeiros")
      .cpf("34310279031")
      .email("celia.caetano@gmail.com")
      .telefone("65758688482")
      .tipoUsuario(TipoUsuarioEnum.ALUNO)
      .build();

    Usuario usuario3 = Usuario.builder()
      .nome("Eduardo Ambrosio")
      .cpf("15738582004")
      .email("eduardo.ambrosio@yahoo.com.br")
      .telefone("80398763011")
      .tipoUsuario(TipoUsuarioEnum.ALUNO)
      .build();

    Usuario usuario4 = Usuario.builder()
      .nome("Raiumunda Cardoso de Freitas")
      .cpf("78791968038")
      .email("raiumunda.cardoso@uol.com")
      .telefone("36568875408")
      .tipoUsuario(TipoUsuarioEnum.ALUNO)
      .build();

    Usuario usuario5 = Usuario.builder()
      .nome("Francisco Antonio Cardoso")
      .cpf("49276781056")
      .email("francisco.antonio@gmail.com")
      .telefone("69202950714")
      .tipoUsuario(TipoUsuarioEnum.ALUNO)
      .build();

    Usuario usuario6 = Usuario.builder()
      .nome("Nelci Campos Matias")
      .cpf("64422062034")
      .email("nelci.campos@hotmail.com")
      .telefone("79810547168")
      .tipoUsuario(TipoUsuarioEnum.PROFESSOR)
      .build();

    Usuario usuario7 = Usuario.builder()
      .nome("Maria Alice Martins")
      .cpf("99348682024")
      .email("maria.alice@yahoo.com.br")
      .telefone("27554020367")
      .tipoUsuario(TipoUsuarioEnum.PROFESSOR)
      .build();

    Usuario usuario8 = Usuario.builder()
      .nome("Luana Janaina Lopes")
      .cpf("04694988034")
      .email("luana.janaina@gmail.com")
      .telefone("42114234713")
      .tipoUsuario(TipoUsuarioEnum.PROFESSOR)
      .build();

    Usuario usuario9 = Usuario.builder()
      .nome("Maria Helena Rodrigues")
      .cpf("41737855003")
      .email("maria.helena@outlook.com")
      .telefone("86388919773")
      .tipoUsuario(TipoUsuarioEnum.VISITANTE)
      .build();

    Usuario usuario10 = Usuario.builder()
      .nome("Vera Lucia de Araujo")
      .cpf("01640297022")
      .email("vera.lucia@yahoo.com.br")
      .telefone("77128568464")
      .tipoUsuario(TipoUsuarioEnum.VISITANTE)
      .build();

    List<Usuario> usuarios = List.of(usuario1, usuario2, usuario3, usuario4, usuario5, usuario6, usuario7, usuario8, usuario9, usuario10);

    usuarios.forEach(usuario -> usuarioService.salvarUsuario(usuario));
  }

  private void configEventos() {
    Evento evento1 = Evento.builder()
      .nome("Apresentação da Ementa do Curso")
      .data(LocalDateTime.now().plusMinutes(1))
      .duracao(LocalTime.of(0, 1))
      .prioridade(PrioridadeEventoEnum.OBRIGATORIO_ALUNOS)
      .build();

    Evento evento2 = Evento.builder()
      .nome("Boas Práticas no Ensino a Distância")
      .data(LocalDateTime.now().plusMinutes(2))
      .duracao(LocalTime.of(0, 1))
      .prioridade(PrioridadeEventoEnum.OBRIGATORIO_PROFESSORES)
      .build();

    Evento evento3 = Evento.builder()
      .nome("A Tecnologia em Nosso Cotidiano")
      .data(LocalDateTime.now().plusMinutes(3))
      .duracao(LocalTime.of(0, 1))
      .prioridade(PrioridadeEventoEnum.ABERTO)
      .build();

    Evento evento4 = Evento.builder()
      .nome("Internet das Coisas: A Revolução Conectada")
      .data(LocalDateTime.now().plusMinutes(4))
      .duracao(LocalTime.of(0, 1))
      .prioridade(PrioridadeEventoEnum.SOMENTE_ALUNOS)
      .build();

    Evento evento5 = Evento.builder()
      .nome("Como Utilizar a I.A em Sala de Aula")
      .data(LocalDateTime.now().plusMinutes(5))
      .duracao(LocalTime.of(0, 1))
      .prioridade(PrioridadeEventoEnum.SOMENTE_PROFESSORES)
      .build();

    List<Evento> eventos = List.of(evento3, evento1, evento2, evento4, evento5);

    eventos.forEach(evento -> eventoService.salvarEvento(evento));
  }
}
