package br.com.event.core.services;

import br.com.event.core.repositories.UsuarioRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class GeradorMatriculaService {

  private static final int START_INDEX = 0;
  private static final int END_INDEX = 6;

  private static final int DEFAULT_CODE_LENGTH = 5;
  private static final String FILL_CHARACTER_CODE = "0";

  private static final String REGEX_DIFFERENT_NUMBERS = "\\D";
  private static final String REPLACE_DIFFERENT_NUMBERS = "";

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

  private final UsuarioRepository usuarioRepository;

  public GeradorMatriculaService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public String gerarMatricula(LocalDateTime data, String cpf) {
    String primeiraParte = data.format(DATE_TIME_FORMATTER)
      .replaceAll(REGEX_DIFFERENT_NUMBERS, REPLACE_DIFFERENT_NUMBERS)
      .substring(START_INDEX, END_INDEX);

    String segundaParte = cpf.substring(START_INDEX, END_INDEX);

    Long nextCode = usuarioRepository.generateNextUserCode();

    StringBuilder terceiraParte = new StringBuilder();

    String code = String.valueOf(nextCode);
    int diff = DEFAULT_CODE_LENGTH - code.length();

    terceiraParte.append(FILL_CHARACTER_CODE.repeat(diff));
    terceiraParte = new StringBuilder(terceiraParte.toString().concat(code));

    return primeiraParte + segundaParte + terceiraParte;
  }

}
