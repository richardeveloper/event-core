package br.com.kafka.exceptions;

public class ServiceException extends RuntimeException {

  public ServiceException(String message) {
    super(message);
  }
}
