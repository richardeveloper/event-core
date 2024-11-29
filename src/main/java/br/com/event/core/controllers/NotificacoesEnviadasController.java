package br.com.event.core.controllers;

import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.repositories.LogNotificacaoRepository;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificacoesEnviadasController implements Initializable {

  @FXML
  private TableView<LogNotificacao> notificacoesTableView;

  @Autowired
  private LogNotificacaoRepository logNotificacaoRepository;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    createTableColumns();
    fillTableView();
  }

  private void fillTableView() {
    List<LogNotificacao> logsNotificacoes = logNotificacaoRepository.findAllOrderByDataEnvioDesc();
    notificacoesTableView.setItems(FXCollections.observableArrayList(logsNotificacoes));
  }

  private void createTableColumns() {
    TableColumn<LogNotificacao, String> colunaId = createColumn(
      "ID",
      data -> new SimpleStringProperty(data.getValue().getId().toString())
    );

    TableColumn<LogNotificacao, String> colunaNotificacao = createColumn(
      "Notificação",
      data -> new SimpleStringProperty(data.getValue().getNotificacao())
    );

    TableColumn<LogNotificacao, String> colunaTipoNotificacao = createColumn(
      "Tipo de notificação",
      data -> new SimpleStringProperty(data.getValue().getTipoNotificacao().getDescricao())
    );

    TableColumn<LogNotificacao, String> colunaDataEnvio = createColumn(
      "Data de envio",
      data -> new SimpleStringProperty(
        data.getValue().getDataEnvio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:SSS"))
      )
    );

    notificacoesTableView.getColumns().add(colunaId);
    notificacoesTableView.getColumns().add(colunaTipoNotificacao);
    notificacoesTableView.getColumns().add(colunaDataEnvio);
    notificacoesTableView.getColumns().add(colunaNotificacao);
  }

  private TableColumn<LogNotificacao, String> createColumn(String nomeColuna,
    Callback<CellDataFeatures<LogNotificacao, String>, ObservableValue<String>> recuperarValorColuna) {

    TableColumn<LogNotificacao, String> coluna = new TableColumn<>();
    coluna.setText(nomeColuna);
    coluna.setStyle("-fx-alignment: center;");
    coluna.setCellValueFactory(recuperarValorColuna);

    return coluna;
  }
}
