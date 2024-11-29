package br.com.event.core.utils;

import br.com.event.core.exceptions.ServiceException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ValidationUtils {

  private static final int CPF_LENGHT = 11;
  private static final int PHONE_LENGHT = 11;
  private static final String ONLY_NUMBERS = "^[0-9]+$";
  private static final String ONLY_LETTERS = "^[A-Za-zÀ-ÿ ]+$";
  private static final String DIFFERENT_EMAIL_FORMAT = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

  public static void validarNome(String nome) {
    if (nome == null || nome.isEmpty()) {
      throw new ServiceException("O campo nome deve ser preenchido.");
    }

    if (!nome.matches(ONLY_LETTERS)) {
      throw new ServiceException("O campo nome deve ser preenchido apenas com letras.");
    }
  }
  
  public static void validarCpf(String cpf) {
    if (cpf == null || cpf.isEmpty()) {
      throw new ServiceException("O campo cpf deve ser preenchido.");
    }

    if (cpf.length() != CPF_LENGHT) {
      throw new ServiceException("O campo cpf deve ser preenchido com 11 digítos.");
    }

    if (!cpf.matches(ONLY_NUMBERS)) {
      throw new ServiceException("O campo cpf deve ser preenchido apenas com números.");
    }
  }
  
  public static void validarEmail(String email) {
    if (email == null || email.isEmpty()) {
      throw new ServiceException("O campo email deve ser preenchido.");
    }

    if (!email.matches(DIFFERENT_EMAIL_FORMAT)) {
      throw new ServiceException("O campo email está fora do padrão [exemplo@exemplo.com].");
    }
  }
  
  public static void validarTelefone(String telefone) {
    if (telefone == null || telefone.isEmpty()) {
      throw new ServiceException("O campo telefone deve ser preenchido.");
    }

    if (!telefone.matches(ONLY_NUMBERS)) {
      throw new ServiceException("O campo telefone deve ser preenchido apenas com números.");
    }

    if (telefone.length() != PHONE_LENGHT) {
      throw new ServiceException("O campo telefone deve ser preenchido com 11 digítos.");
    }
  }

  public static void validarData(LocalDateTime data) {
    if (data == null) {
      throw new ServiceException("O campo data deve ser preenchido.");
    }

    if (data.isBefore(LocalDateTime.now())) {
      throw new ServiceException("A data do evento deve ser superior ou igual a data atual.");
    }
  }

  public static void validarDuracao(LocalTime duracao) {
    if (duracao == null) {
      throw new ServiceException("O campo duracao deve ser preenchido.");
    }
  }

  public static void validarCampo(Object valorCampo, String nomeCampo) {
    if (valorCampo == null) {
      throw new ServiceException("O campo %s deve ser preenchido.".formatted(nomeCampo));
    }
  }

}
