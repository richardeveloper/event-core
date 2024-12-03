package br.com.event.core.configs;

import br.com.event.core.entities.Evento;
import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.PrioridadeEventoEnum;
import br.com.event.core.enums.TipoUsuarioEnum;
import br.com.event.core.services.EventoService;
import br.com.event.core.services.UsuarioService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class StarterConfig implements CommandLineRunner {

  private final UsuarioService usuarioService;

  private final EventoService eventoService;

  private final JdbcTemplate jdbcTemplate;

  public StarterConfig(UsuarioService usuarioService, EventoService eventoService, JdbcTemplate jdbcTemplate) {
    this.usuarioService = usuarioService;
    this.eventoService = eventoService;
    this.jdbcTemplate = jdbcTemplate;
  }

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
    Usuario aluno1 = Usuario.builder()
      .nome("Adao Luiz Ferreira")
      .cpf("26208784620")
      .email("adao.luiz@outlook.com")
      .telefone("30400807092")
      .tipoUsuario(TipoUsuarioEnum.ALUNO)
      .build();

    Usuario aluno2 = Usuario.builder()
      .nome("Celia Caetano de Maedeiros")
      .cpf("34310279031")
      .email("celia.caetano@gmail.com")
      .telefone("65758688482")
      .tipoUsuario(TipoUsuarioEnum.ALUNO)
      .build();

    Usuario aluno3 = Usuario.builder()
      .nome("Eduardo Ambrosio")
      .cpf("15738582004")
      .email("eduardo.ambrosio@yahoo.com.br")
      .telefone("80398763011")
      .tipoUsuario(TipoUsuarioEnum.ALUNO)
      .build();

    Usuario professor1 = Usuario.builder()
      .nome("Nelci Campos Matias")
      .cpf("64422062034")
      .email("nelci.campos@hotmail.com")
      .telefone("79810547168")
      .tipoUsuario(TipoUsuarioEnum.PROFESSOR)
      .build();

    Usuario professor2 = Usuario.builder()
      .nome("Maria Alice Martins")
      .cpf("99348682024")
      .email("maria.alice@yahoo.com.br")
      .telefone("27554020367")
      .tipoUsuario(TipoUsuarioEnum.PROFESSOR)
      .build();

    Usuario visitante = Usuario.builder()
      .nome("Maria Helena Rodrigues")
      .cpf("41737855003")
      .email("maria.helena@outlook.com")
      .telefone("86388919773")
      .tipoUsuario(TipoUsuarioEnum.VISITANTE)
      .build();

    List<Usuario> usuarios = List.of(aluno1, aluno2, aluno3, professor1, professor2, visitante);

    usuarios.forEach(usuarioService::salvarUsuario);
  }

  private void configEventos() {
    Evento evento1 = Evento.builder()
      .nome("Networking: Conectando-se para o Sucesso Profissional")
      .data(LocalDateTime.now().plusMinutes(1))
      .duracao(LocalTime.of(0, 1))
      .prioridade(PrioridadeEventoEnum.OBRIGATORIO_ALUNOS)
      .build();

    Evento evento2 = Evento.builder()
      .nome("Desafios do Ensino Superior na Era Digital")
      .data(LocalDateTime.now().plusMinutes(2))
      .duracao(LocalTime.of(0, 1))
      .prioridade(PrioridadeEventoEnum.OBRIGATORIO_PROFESSORES)
      .build();

    Evento evento3 = Evento.builder()
      .nome("Tecnologias Emergentes e Suas Aplicações no Mundo Real")
      .data(LocalDateTime.now().plusMinutes(3))
      .duracao(LocalTime.of(0, 1))
      .prioridade(PrioridadeEventoEnum.ABERTO)
      .build();

    List<Evento> eventos = List.of(evento1, evento2, evento3);

    eventos.forEach(eventoService::salvarEvento);
  }
}
