package br.com.event.core.utils;

import br.com.event.core.entities.Evento;
import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.TipoUsuarioEnum;

public class MaskUtils {

  private static final String REGEX_DIFFERENT_NUMBERS = "\\D";

  public static String applyMaskCpf(String value) {
    String newValue = replace(value);

    if (newValue.length() > 11) {
      newValue = newValue.substring(0, 11);
    }

    if (newValue.length() >= 10) {
      newValue = newValue.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{1})", "$1.$2.$3-$4");
    }
    else if (newValue.length() >= 7) {
      newValue = newValue.replaceAll("(\\d{3})(\\d{3})(\\d{1})", "$1.$2.$3");
    }
    else if (newValue.length() >= 4) {
      newValue = newValue.replaceAll("(\\d{3})(\\d{1})", "$1.$2");
    }

    return newValue;
  }

  public static String applyMaskPhone(String value) {
    String newValue = replace(value);

    if (newValue.length() > 11) {
      newValue = newValue.substring(0, 11);
    }

    switch (newValue.length()) {
      case 1:
      case 2:
        newValue = newValue.replaceAll("(\\d{1,2})", "($1");
        break;
      case 3:
      case 4:
        newValue = newValue.replaceAll("(\\d{2})(\\d{1,2})", "($1) $2");
        break;
      case 5:
      case 6:
        newValue = newValue.replaceAll("(\\d{2})(\\d{3,4})", "($1) $2");
        break;
      case 7:
      case 8:
        newValue = newValue.replaceAll("(\\d{2})(\\d{4,5})(\\d{1,2})", "($1) $2-$3");
        break;
      case 9:
      case 10:
        newValue = newValue.replaceAll("(\\d{2})(\\d{5})(\\d{2,3})", "($1) $2-$3");
        break;
      case 11:
        newValue = newValue.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        break;
      default:
        break;
    }

    return newValue;
  }

  public static String applyMaskDate(String value) {
    String newValue = replace(value);

    if (newValue.length() > 12) {
      newValue = newValue.substring(0, 12);
    }

    if (newValue.length() <= 4) {
      newValue = newValue.replaceAll("(\\d{2})(\\d{1})", "$1/$2");
    }
    else if (newValue.length() <= 9) {
      newValue = newValue.replaceAll("(\\d{2})(\\d{2})(\\d{1})", "$1/$2/$3");
    }
    else if (newValue.length() == 10) {
      newValue = newValue.replaceAll("(\\d{2})(\\d{2})(\\d{4})(\\d{2})", "$1/$2/$3 $4");
    }
    else if (newValue.length() == 11) {
      newValue = newValue.replaceAll("(\\d{2})(\\d{2})(\\d{4})(\\d{2})(\\d{1})", "$1/$2/$3 $4:$5");
    }
    else {
      newValue = newValue.replaceAll("(\\d{2})(\\d{2})(\\d{4})(\\d{2})(\\d{2})", "$1/$2/$3 $4:$5");
    }

    return newValue;
  }

  public static String applyMaskTime(String value) {
    String newValue = replace(value);

    if (newValue.length() > 4) {
      newValue = newValue.substring(0, 4);
    }

    if (newValue.length() <= 2) {
      newValue = newValue.replaceAll("(\\d{2})", "$1");
    }
    else if (newValue.length() == 3) {
      newValue = newValue.replaceAll("(\\d{2})(\\d{1})", "$1:$2");
    }
    else {
      newValue = newValue.replaceAll("(\\d{2})(\\d{2})", "$1:$2");
    }

    return newValue;
  }

  public static String applyInfoUserMask(Usuario usuario) {
    if (usuario.getTipoUsuario().equals(TipoUsuarioEnum.VISITANTE)) {
      return "%s - %s".formatted(usuario.getTipoUsuario().getDescricao(), usuario.getNome());
    }

    return "[%s] %s - %s".formatted(usuario.getTipoUsuario().getDescricao(), usuario.getMatricula(), usuario.getNome());
  }

  public static String removeInfoUserMask(String infoUsuario) {
    if (infoUsuario.contains("[") && infoUsuario.contains("]")) {
      infoUsuario = infoUsuario.substring(infoUsuario.indexOf("]") + 1);
    }

    infoUsuario = infoUsuario.trim().replace(" - ", "-");
    int index = infoUsuario.indexOf("-");
    return infoUsuario.substring(index + 1);
  }

  public static String applyInfoEventMask(Evento evento) {
    return "[%s] - %s".formatted(evento.getPrioridade().getDescricao(), evento.getNome());
  }

  public static String removeInfoEventMask(String infoEvento) {
    if (infoEvento == null) {
      return null;
    }

    infoEvento = infoEvento.substring(infoEvento.indexOf("]") + 1);
    return infoEvento.replace(" - ", "");
  }

  public static String removeMask(String value) {
    return replace(value);
  }

  private static String replace(String value) {
    return value.replaceAll(REGEX_DIFFERENT_NUMBERS, "");
  }

}
