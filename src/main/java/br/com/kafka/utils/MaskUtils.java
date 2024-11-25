package br.com.kafka.utils;

public class MaskUtils {

  public static String applyMaskCpf(String cpf) {
    String value = cpf.replaceAll("[^\\d]", "");

    if (value.length() > 11) {
      value = value.substring(0, 11);
    }

    if (value.length() >= 10) {
      value = value.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{1})", "$1.$2.$3-$4");
    }
    else if (value.length() >= 7) {
      value = value.replaceAll("(\\d{3})(\\d{3})(\\d{1})", "$1.$2.$3");
    }
    else if (value.length() >= 4) {
      value = value.replaceAll("(\\d{3})(\\d{1})", "$1.$2");
    }

    return value;
  }

  public static String applyMaskPhone(String newValue) {
    String value = newValue.replaceAll("[^\\d]", "");

    if (value.length() > 11) {
      value = value.substring(0, 11);
    }

    if (value.length() == 1) {
      value = value.replaceAll("(\\d{1})", "($1");
    }
    else if (value.length() == 2) {
      value = value.replaceAll("(\\d{2})", "($1");
    }
    else if (value.length() == 3) {
      value = value.replaceAll("(\\d{3})(\\d{1})", "($1) $2");
    }
    else if (value.length() == 4) {
      value = value.replaceAll("(\\d{2})(\\d{2})", "($1) $2");
    }
    else if (value.length() == 5) {
      value = value.replaceAll("(\\d{2})(\\d{3})", "($1) $2");
    }
    else if (value.length() == 6) {
      value = value.replaceAll("(\\d{2})(\\d{4})", "($1) $2");
    }
    else if (value.length() == 7) {
      value = value.replaceAll("(\\d{2})(\\d{5})", "($1) $2");
    }
    else if (value.length() == 8) {
      value = value.replaceAll("(\\d{2})(\\d{5})(\\d{1})", "($1) $2-$3");
    }
    else if (value.length() == 9) {
      value = value.replaceAll("(\\d{2})(\\d{5})(\\d{2})", "($1) $2-$3");
    }
    else if (value.length() == 10) {
      value = value.replaceAll("(\\d{2})(\\d{5})(\\d{3})", "($1) $2-$3");
    }
    else if (value.length() == 11) {
      value = value.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }

    return value;
  }

  public static String applyMaskDate(String newValue) {
    String value = newValue.replaceAll("[^0-9]", "");

    if (value.length() > 12) {
      value = value.substring(0, 12);
    }

    if (value.length() <= 4) {
      value = value.replaceAll("(\\d{2})(\\d{1})", "$1/$2");
    }
    else if (value.length() <= 9) {
      value = value.replaceAll("(\\d{2})(\\d{2})(\\d{1})", "$1/$2/$3");
    }
    else if (value.length() == 10) {
      value = value.replaceAll("(\\d{2})(\\d{2})(\\d{4})(\\d{2})", "$1/$2/$3 $4");
    }
    else if (value.length() == 11) {
      value = value.replaceAll("(\\d{2})(\\d{2})(\\d{4})(\\d{2})(\\d{1})", "$1/$2/$3 $4:$5");
    }
    else {
      value = value.replaceAll("(\\d{2})(\\d{2})(\\d{4})(\\d{2})(\\d{2})", "$1/$2/$3 $4:$5");
    }

    return value;
  }

  public static String applyMaskTime(String newValue) {
    String value = newValue.replaceAll("[^0-9]", "");

    if (value.length() > 4) {
      value = value.substring(0, 4);
    }

    if (value.length() == 2) {
      value = value.replaceAll("(\\d{2})", "$1");
    }
    else if (value.length() >= 3) {
      value = value.replaceAll("(\\d{2})(\\d{1})", "$1:$2");
    }

    return value;
  }

  public static String removeMask(String cpf) {
    return cpf.replaceAll("[^\\d]", "");
  }

}
