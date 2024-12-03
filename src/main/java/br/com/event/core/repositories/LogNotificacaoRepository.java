package br.com.event.core.repositories;

import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.enums.TipoNotificacaoEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogNotificacaoRepository extends JpaRepository<LogNotificacao, Long> {

  @Query(value = "SELECT notificacao FROM LogNotificacao notificacao ORDER BY notificacao.dataEnvio DESC")
  List<LogNotificacao> findAllOrderByDataEnvioDesc();

  @Query(value = "SELECT notificacao FROM LogNotificacao notificacao WHERE notificacao.tipoNotificacao = :tipoNotificacao ORDER BY notificacao.dataEnvio DESC")
  List<LogNotificacao> findAllByTipoNotificacaoOrderByDataEnvioDesc(TipoNotificacaoEnum tipoNotificacao);

  @Query(value = "SELECT notificacao FROM LogNotificacao notificacao WHERE notificacao.tipoNotificacao IN (:tiposNotificacoes) ORDER BY notificacao.dataEnvio DESC")
  List<LogNotificacao> findAllByTiposNotificacoesOrderByDataEnvioDesc(List<TipoNotificacaoEnum> tiposNotificacoes);

}
