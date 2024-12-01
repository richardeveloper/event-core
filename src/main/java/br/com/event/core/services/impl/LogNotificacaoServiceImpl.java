package br.com.event.core.services.impl;

import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.repositories.LogNotificacaoRepository;
import br.com.event.core.services.LogNotificacaoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogNotificacaoServiceImpl implements LogNotificacaoService {

  @Autowired
  private LogNotificacaoRepository logNotificacaoRepository;

  @Override
  public List<LogNotificacao> buscarLogNotificacoesMaisRecentes() {
    return logNotificacaoRepository.findAllOrderByDataEnvioDesc();
  }
}
