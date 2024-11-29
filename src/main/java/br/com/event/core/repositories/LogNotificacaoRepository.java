package br.com.event.core.repositories;

import br.com.event.core.entities.LogNotificacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogNotificacaoRepository extends JpaRepository<LogNotificacao, Long> {

  @Query(value = "SELECT notificacao FROM LogNotificacao notificacao ORDER BY notificacao.dataEnvio DESC")
  List<LogNotificacao> findAllOrderByDataEnvioDesc();

}
