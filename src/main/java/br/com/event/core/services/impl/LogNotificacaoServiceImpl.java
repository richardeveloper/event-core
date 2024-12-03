package br.com.event.core.services.impl;

import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.repositories.LogNotificacaoRepository;
import br.com.event.core.services.LogNotificacaoService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogNotificacaoServiceImpl implements LogNotificacaoService {

  private final LogNotificacaoRepository logNotificacaoRepository;

  public LogNotificacaoServiceImpl(LogNotificacaoRepository logNotificacaoRepository) {
    this.logNotificacaoRepository = logNotificacaoRepository;
  }

  @Override
  public List<LogNotificacao> buscarLogNotificacoesMaisRecentes() {
    return logNotificacaoRepository.findAllOrderByDataEnvioDesc();
  }

  @Override
  public List<LogNotificacao> buscarTodosPorTiposNotificacoes(List<TipoNotificacaoEnum> tiposNotificacoes) {
    return logNotificacaoRepository.findAllByTiposNotificacoesOrderByDataEnvioDesc(tiposNotificacoes);
  }
}
