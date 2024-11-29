package br.com.event.core.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.event.core.exceptions.ServiceException;
import org.junit.jupiter.api.Test;

class ValidationUtilsTest {

  @Test
  public void deveValidarNome() {
    String nomeValido = "Nome Válido";

    String nomeInvalido1 = "Nome@Invalido";
    String nomeInvalido2 = "Nome Invalido 2";
    String nomeInvalido3 = "Nome-Invalido!";

    assertDoesNotThrow(() -> ValidationUtils.validarNome(nomeValido));
    assertThrows(ServiceException.class, () -> ValidationUtils.validarNome(nomeInvalido1));
    assertThrows(ServiceException.class, () -> ValidationUtils.validarNome(nomeInvalido2));
    assertThrows(ServiceException.class, () -> ValidationUtils.validarNome(nomeInvalido3));
  }

  @Test
  public void deveValidarCpf() {
    String cpfValido = "05119675190";

    String cpfInvalido1 = "1234567890a";
    String cpfInvalido2 = "123.456.789";
    String cpfInvalido3 = "123.456.78";

    assertDoesNotThrow(() -> ValidationUtils.validarCpf(cpfValido));
    assertThrows(ServiceException.class, () -> ValidationUtils.validarCpf(cpfInvalido1));
    assertThrows(ServiceException.class, () -> ValidationUtils.validarCpf(cpfInvalido2));
    assertThrows(ServiceException.class, () -> ValidationUtils.validarCpf(cpfInvalido3));
  }

  @Test
  public void deveValidarTelefone() {
    String telefoneValido = "62981608775";

    String telefoneInvalido1 = "9999999999A";
    String telefoneInvalido2 = "99A99A99A99";
    String telefoneInvalido3 = "(99) 99999)";

    assertDoesNotThrow(() -> ValidationUtils.validarTelefone(telefoneValido));
    assertThrows(ServiceException.class, () -> ValidationUtils.validarTelefone(telefoneInvalido1));
    assertThrows(ServiceException.class, () -> ValidationUtils.validarTelefone(telefoneInvalido2));
    assertThrows(ServiceException.class, () -> ValidationUtils.validarTelefone(telefoneInvalido3));
  }

  @Test
  public void deveValidarEmail() {
    String emailValido = "email@email.com";

    String emailInvalido1 = "emailemail.com";
    String emailInvalido2 = "email@email";
    String emailInvalido3 = "emailemailcom";

    assertDoesNotThrow(() -> ValidationUtils.validarEmail(emailValido));
    assertThrows(ServiceException.class, () -> ValidationUtils.validarEmail(emailInvalido1));
    assertThrows(ServiceException.class, () -> ValidationUtils.validarEmail(emailInvalido2));
    assertThrows(ServiceException.class, () -> ValidationUtils.validarEmail(emailInvalido3));
  }

}