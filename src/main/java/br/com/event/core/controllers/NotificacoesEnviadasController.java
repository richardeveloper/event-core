package br.com.event.core.controllers;

import br.com.event.core.entities.Evento;
import br.com.event.core.entities.LogNotificacao;
import br.com.event.core.enums.StatusEventoEnum;
import br.com.event.core.enums.TipoNotificacaoEnum;
import br.com.event.core.services.LogNotificacaoService;
import br.com.event.core.utils.ResourceUtils;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
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

  @FXML
  private CheckBox filtroTodosCheckBox;

  @Autowired
  private LogNotificacaoService logNotificacaoService;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    createTableColumns();
    fillTableView();
    setUpFilters();
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

  private void setUpFilters() {
    filtroConfirmacaoInscricaoCheckBox.setText(TipoNotificacaoEnum.INSCRICAO_CONFIRMADA.getDescricao());
    filtroConfirmacaoInscricaoCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroCancelamentoInscricaoCheckBox.setText(TipoNotificacaoEnum.INSCRICAO_CANCELADA.getDescricao());
    filtroCancelamentoInscricaoCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroAlteracaoDataCheckBox.setText(TipoNotificacaoEnum.ALTERACAO_DATA_EVENTO.getDescricao());
    filtroAlteracaoDataCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroInicioEventoCheckBox.setText(TipoNotificacaoEnum.EVENTO_INICIADO.getDescricao());
    filtroInicioEventoCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroFimEventoCheckBox.setText(TipoNotificacaoEnum.EVENTO_FINALIZADO.getDescricao());
    filtroFimEventoCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroCancelamentoEventoCheckBox.setText(TipoNotificacaoEnum.EVENTO_CANCELADO.getDescricao());
    filtroCancelamentoEventoCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroTodosCheckBox.selectedProperty().addListener(this::filtroTodosAction);

    filterIcon.setImage(ResourceUtils.getIcon("/icons/filter.png", 20, 20).getImage());
  }

  private void createTableColumns() {
    TableColumn<LogNotificacao, String> colunaId = createColumn(
      "ID",
      data -> new SimpleStringProperty(data.getValue().getId().toString())
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

    TableColumn<LogNotificacao, String> colunaNotificacao = createColumn(
      "Notificação",
      data -> new SimpleStringProperty(data.getValue().getNotificacao())
    );

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
    notificacoesTableView.getColumns().add(colunaDestinatario);
    notificacoesTableView.getColumns().add(colunaCategoria);
    notificacoesTableView.getColumns().add(colunaNotificacao);
    notificacoesTableView.getColumns().add(colunaEvento);
    notificacoesTableView.getColumns().add(colunaDataEvento);

    notificacoesTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  private TableColumn<LogNotificacao, String> createColumn(String nomeColuna,
    Callback<CellDataFeatures<LogNotificacao, String>, ObservableValue<String>> recuperarValorColuna) {

    TableColumn<LogNotificacao, String> coluna = new TableColumn<>();
    coluna.setText(nomeColuna);
    coluna.setStyle("-fx-alignment: center;");
    coluna.setCellValueFactory(recuperarValorColuna);

    return coluna;
  }

  private void filtrosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    List<TipoNotificacaoEnum> tipoNotificacaoList = new ArrayList<>();

    List<CheckBox> checkBoxList = List.of(
      filtroConfirmacaoInscricaoCheckBox,
      filtroCancelamentoInscricaoCheckBox,
      filtroAlteracaoDataCheckBox,
      filtroInicioEventoCheckBox,
      filtroFimEventoCheckBox,
      filtroCancelamentoEventoCheckBox
    );

    for (CheckBox checkBox : checkBoxList) {
      if (checkBox.isSelected()) {
        String name = checkBox.getText();

        TipoNotificacaoEnum tipoNotificacaoEnum = TipoNotificacaoEnum.parse(name);
        tipoNotificacaoList.add(tipoNotificacaoEnum);
      }
    }

    if (tipoNotificacaoList.isEmpty()) {
      fillTableView();
      return;
    }

    List<LogNotificacao> notificacoes = logNotificacaoService.buscarTodosPorTiposNotificacoes(tipoNotificacaoList);

    fillTableView(notificacoes);
  }

  private void filtroTodosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    List<CheckBox> checkBoxList = List.of(
      filtroConfirmacaoInscricaoCheckBox,
      filtroCancelamentoInscricaoCheckBox,
      filtroAlteracaoDataCheckBox,
      filtroInicioEventoCheckBox,
      filtroFimEventoCheckBox,
      filtroCancelamentoEventoCheckBox
    );

    if (newValue) {
      checkBoxList.forEach(checkBox -> checkBox.setSelected(true));

      filtroTodosCheckBox.setSelected(true);

      List<TipoNotificacaoEnum> tipoNotificacaoList = Arrays.stream(TipoNotificacaoEnum.values()).toList();

      if (tipoNotificacaoList.isEmpty()) {
        fillTableView();
        return;
      }

      List<LogNotificacao> notificacoes = logNotificacaoService.buscarTodosPorTiposNotificacoes(tipoNotificacaoList);

      fillTableView(notificacoes);
    }
    else {
      checkBoxList.forEach(checkBox -> checkBox.setSelected(false));
    }
  }
  
}
