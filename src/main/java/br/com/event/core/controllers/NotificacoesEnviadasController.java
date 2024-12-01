package br.com.event.core.controllers;

import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.services.LogNotificacaoService;
import br.com.event.core.utils.IconUtils;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificacoesEnviadasController implements Initializable {

  @FXML
  private TableView<LogNotificacao> notificacoesTableView;

  @FXML
  private ImageView filterIcon;

  @FXML
  private CheckBox filtroConfirmacaoInscricaoCheckBox;

  @FXML
  private CheckBox filtroCancelamentoInscricaoCheckBox;

  @FXML
  private CheckBox filtroAlteracaoDataCheckBox;

  @FXML
  private CheckBox filtroInicioEventoCheckBox;

  @FXML
  private CheckBox filtroFimEventoCheckBox;

  @FXML
  private CheckBox filtroCancelamentoEventoCheckBox;

  @Autowired
  private LogNotificacaoService logNotificacaoService;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    createTableColumns();
    fillTableView();
    filterIcon.setImage(IconUtils.getIcon("/icons/filter.png", 20, 20).getImage());

    filtroConfirmacaoInscricaoCheckBox.selectedProperty().addListener(this::filtroConfirmacaoInscricaoAction);
    filtroCancelamentoInscricaoCheckBox.selectedProperty().addListener(this::filtroCancelamentoInscricaoAction);
    filtroAlteracaoDataCheckBox.selectedProperty().addListener(this::filtroAlteracaoDataAction);
    filtroInicioEventoCheckBox.selectedProperty().addListener(this::filtroInicioEventoAction);
    filtroFimEventoCheckBox.selectedProperty().addListener(this::filtroFimEventoAction);
    filtroCancelamentoEventoCheckBox.selectedProperty().addListener(this::filtroCancelamentoEventoAction);
  }

  public void fillTableView() {
    List<LogNotificacao> logsNotificacoes = logNotificacaoService.buscarLogNotificacoesMaisRecentes();

    if (logsNotificacoes != null) {
      notificacoesTableView.setPlaceholder(new Label("Não foram encontrados registros de notificação"));
    }

    notificacoesTableView.setItems(FXCollections.observableArrayList(logsNotificacoes));
  }

  public void fillTableView(List<LogNotificacao> notificacoes) {
    notificacoesTableView.setItems(FXCollections.observableArrayList());

    if (notificacoes.isEmpty()) {
      notificacoesTableView.setPlaceholder(new Label("Não foram encontrados registros de notificação"));
    }

    notificacoesTableView.setItems(FXCollections.observableArrayList(notificacoes));
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

    TableColumn<LogNotificacao, String> colunaDestinatario = createColumn(
      "Destinatário",
      data -> new SimpleStringProperty(data.getValue().getNomeUsuario())
    );

    TableColumn<LogNotificacao, String> colunaCategoria = createColumn(
      "Categoria",
      data -> new SimpleStringProperty(data.getValue().getTipoUsuario())
    );
    colunaCategoria.setMinWidth(110.0);

    TableColumn<LogNotificacao, String> colunaEvento = createColumn(
      "Evento",
      data -> new SimpleStringProperty(data.getValue().getNomeEvento())
    );

    TableColumn<LogNotificacao, String> colunaDataEvento = createColumn(
      "Data do evento",
      data -> new SimpleStringProperty(data.getValue().getDataEvento())
    );

    notificacoesTableView.getColumns().add(colunaId);
    notificacoesTableView.getColumns().add(colunaTipoNotificacao);
    notificacoesTableView.getColumns().add(colunaDataEnvio);
    notificacoesTableView.getColumns().add(colunaNotificacao);
    notificacoesTableView.getColumns().add(colunaDestinatario);
    notificacoesTableView.getColumns().add(colunaCategoria);
    notificacoesTableView.getColumns().add(colunaEvento);
    notificacoesTableView.getColumns().add(colunaDataEvento);
  }

  private TableColumn<LogNotificacao, String> createColumn(String nomeColuna,
    Callback<CellDataFeatures<LogNotificacao, String>, ObservableValue<String>> recuperarValorColuna) {

    TableColumn<LogNotificacao, String> coluna = new TableColumn<>();
    coluna.setText(nomeColuna);
    coluna.setStyle("-fx-alignment: center;");
    coluna.setCellValueFactory(recuperarValorColuna);

    return coluna;
  }

  private void filtroConfirmacaoInscricaoAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    if (newValue) {
      filtroCancelamentoEventoCheckBox.setSelected(false);
      filtroAlteracaoDataCheckBox.setSelected(false);
      filtroInicioEventoCheckBox.setSelected(false);
      filtroFimEventoCheckBox.setSelected(false);
      filtroCancelamentoEventoCheckBox.setSelected(false);

      filtroConfirmacaoInscricaoCheckBox.setSelected(true);

      List<LogNotificacao> notificacoes = logNotificacaoService.buscarTodosPorTipoNotificacao(TipoNotificacaoEnum.INSCRICAO_CONFIRMADA);

      fillTableView(notificacoes);
    }
    else {
      fillTableView();
    }
  }

  private void filtroCancelamentoInscricaoAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    if (newValue) {
      filtroConfirmacaoInscricaoCheckBox.setSelected(false);
      filtroAlteracaoDataCheckBox.setSelected(false);
      filtroInicioEventoCheckBox.setSelected(false);
      filtroFimEventoCheckBox.setSelected(false);
      filtroCancelamentoEventoCheckBox.setSelected(false);

      filtroCancelamentoInscricaoCheckBox.setSelected(true);

      List<LogNotificacao> notificacoes = logNotificacaoService.buscarTodosPorTipoNotificacao(TipoNotificacaoEnum.INSCRICAO_CANCELADA);

      fillTableView(notificacoes);
    }
    else {
      fillTableView();
    }
  }

  private void filtroAlteracaoDataAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    if (newValue) {
      filtroConfirmacaoInscricaoCheckBox.setSelected(false);
      filtroCancelamentoInscricaoCheckBox.setSelected(false);
      filtroInicioEventoCheckBox.setSelected(false);
      filtroFimEventoCheckBox.setSelected(false);
      filtroCancelamentoEventoCheckBox.setSelected(false);

      filtroAlteracaoDataCheckBox.setSelected(true);

      List<LogNotificacao> notificacoes = logNotificacaoService.buscarTodosPorTipoNotificacao(TipoNotificacaoEnum.ALTERACAO_DATA_EVENTO);

      fillTableView(notificacoes);
    }
    else {
      fillTableView();
    }
  }

  private void filtroInicioEventoAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    if (newValue) {
      filtroConfirmacaoInscricaoCheckBox.setSelected(false);
      filtroCancelamentoInscricaoCheckBox.setSelected(false);
      filtroAlteracaoDataCheckBox.setSelected(false);
      filtroFimEventoCheckBox.setSelected(false);
      filtroCancelamentoEventoCheckBox.setSelected(false);

      filtroInicioEventoCheckBox.setSelected(true);

      List<LogNotificacao> notificacoes = logNotificacaoService.buscarTodosPorTipoNotificacao(TipoNotificacaoEnum.EVENTO_INICIADO);

      fillTableView(notificacoes);
    }
    else {
      fillTableView();
    }
  }

  private void filtroFimEventoAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    if (newValue) {
      filtroConfirmacaoInscricaoCheckBox.setSelected(false);
      filtroCancelamentoInscricaoCheckBox.setSelected(false);
      filtroAlteracaoDataCheckBox.setSelected(false);
      filtroInicioEventoCheckBox.setSelected(false);
      filtroCancelamentoEventoCheckBox.setSelected(false);

      filtroFimEventoCheckBox.setSelected(true);

      List<LogNotificacao> notificacoes = logNotificacaoService.buscarTodosPorTipoNotificacao(TipoNotificacaoEnum.EVENTO_FINALIZADO);

      fillTableView(notificacoes);
    }
    else {
      fillTableView();
    }
  }

  private void filtroCancelamentoEventoAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    if (newValue) {
      filtroConfirmacaoInscricaoCheckBox.setSelected(false);
      filtroCancelamentoInscricaoCheckBox.setSelected(false);
      filtroAlteracaoDataCheckBox.setSelected(false);
      filtroInicioEventoCheckBox.setSelected(false);
      filtroFimEventoCheckBox.setSelected(false);

      filtroCancelamentoEventoCheckBox.setSelected(true);

      List<LogNotificacao> notificacoes = logNotificacaoService.buscarTodosPorTipoNotificacao(TipoNotificacaoEnum.EVENTO_CANCELADO);

      fillTableView(notificacoes);
    }
    else {
      fillTableView();
    }
  }
  
}
