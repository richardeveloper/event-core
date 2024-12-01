package br.com.event.core.services;

import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.TipoNotificacaoEnum;
import java.util.List;

public interface LogNotificacaoService {

  List<LogNotificacao> buscarLogNotificacoesMaisRecentes();

  List<LogNotificacao> buscarTodosPorTipoNotificacao(TipoNotificacaoEnum tipoNotificacao);
}
