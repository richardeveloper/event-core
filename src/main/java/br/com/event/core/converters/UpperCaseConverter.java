package br.com.event.core.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UpperCaseConverter implements AttributeConverter<String, String> {

  @Override
  public String convertToDatabaseColumn(String string) {
    if (string == null) {
      return null;
    }
    return string.toUpperCase();
  }

  @Override
  public String convertToEntityAttribute(String string) {
    if (string == null) {
      return null;
    }
    return string.toUpperCase();
  }
}
