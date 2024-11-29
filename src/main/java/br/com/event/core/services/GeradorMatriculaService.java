package br.com.event.core.services;

import java.time.LocalDateTime;

public interface GeradorMatriculaService {

  String gerarMatricula(LocalDateTime data, String cpf);

}
