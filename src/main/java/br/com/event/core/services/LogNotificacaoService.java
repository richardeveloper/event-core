package br.com.event.core.services;

import br.com.event.core.entities.LogNotificacao;
import java.util.List;

public interface LogNotificacaoService {

  List<LogNotificacao> buscarLogNotificacoesMaisRecentes();

}
