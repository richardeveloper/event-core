package br.com.event.core.controllers;

import br.com.event.core.entities.Evento;
import br.com.event.core.enums.PrioridadeEventoEnum;
import br.com.event.core.enums.StatusEventoEnum;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.services.EventoService;
import br.com.event.core.services.EventosUsuarioService;
import br.com.event.core.utils.AlertUtils;
import br.com.event.core.utils.MaskUtils;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProximosEventosController implements Initializable {

  @FXML
  private FlowPane cardsContent;

  @FXML
  private CheckBox filtroAbertoPublicoCheckBox;

  @FXML
  private CheckBox filtroObrigatorioAlunoCheckBox;

  @FXML
  private CheckBox filtroObrigatorioProfessorCheckBox;

  @FXML
  private CheckBox filtroSomenteAlunosCheckBox;

  @FXML
  private CheckBox filtroSomenteProfessoresCheckBox;

  @FXML
  private CheckBox filtroTodosCheckBox;

  @FXML
  private ImageView filterIcon;

  @Autowired
  private EventoService eventoService;

  @Autowired
  private EventosUsuarioService eventosUsuarioService;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    fillContentCards();
    configFilterIcon();

    filtroAbertoPublicoCheckBox.selectedProperty().addListener(this::filtroAbertoPublicAction);
    filtroObrigatorioAlunoCheckBox.selectedProperty().addListener(this::filtroObrigatorioAlunoAction);
    filtroObrigatorioProfessorCheckBox.selectedProperty().addListener(this::filtroObrigatorioProfessorAction);
    filtroSomenteAlunosCheckBox.selectedProperty().addListener(this::filtroSomenteAlunosAction);
    filtroSomenteProfessoresCheckBox.selectedProperty().addListener(this::filtroSomenteProfessoresAction);
    filtroTodosCheckBox.selectedProperty().addListener(this::filtroTodosAction);
  }


  private void fillContentCards() {
    List<Evento> eventos = eventoService.buscarEventosMaisProximos();

    for (Evento evento : eventos) {
      VBox card = createCard(evento);
      cardsContent.getChildren().add(card);
    }
  }

  private void fillContentCards(List<Evento> eventos ) {
    cardsContent.getChildren().clear();

    if (eventos.isEmpty()) {
      cardsContent.getChildren().add(new Label("Nenhum evento foi encontrado"));
    }

    for (Evento evento : eventos) {
      VBox card = createCard(evento);
      cardsContent.getChildren().add(card);
    }
  }

  private void configFilterIcon() {
    Image image = new Image(getClass().getResource("/icons/filter.png").toExternalForm());
    filterIcon.setImage(image);
    filterIcon.setFitWidth(20);
    filterIcon.setFitHeight(20);
  }

  private VBox createCard(Evento evento) {
    VBox card = new VBox();
    card.setSpacing(10);

    card.getStyleClass().add("card");

    Label nomeLabel = new Label(evento.getNome().toUpperCase());
    nomeLabel.getStyleClass().add("card-title");
    HBox nomeRow = new HBox(nomeLabel);
    nomeRow.setAlignment(Pos.CENTER);

    Label dataLabel = new Label("Data do evento");

    TextField dataTextField = new TextField();
    dataTextField.setText(evento.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    dataTextField.setEditable(false);
    dataTextField.setAlignment(Pos.CENTER);

    Button dataButton = new Button("Editar");
    dataButton.getStyleClass().add("edit-button");
    dataButton.setOnAction(event -> createDateDialog(evento, dataTextField));
    HBox dataRow = new HBox(10, dataTextField, dataButton);
    HBox.setHgrow(dataTextField, Priority.ALWAYS);

    Label duracaoLabel = new Label("Duração do evento");

    TextField duracaoTextField = new TextField();
    duracaoTextField.setText(evento.getDuracao().toString());
    duracaoTextField.setEditable(false);
    duracaoTextField.setAlignment(Pos.CENTER);

    Button duracaoButton = new Button("Editar");
    duracaoButton.getStyleClass().add("edit-button");
    duracaoButton.setOnAction(event -> createDuracaoDialog(evento, duracaoTextField));

    HBox duracaoRow = new HBox(10, duracaoTextField, duracaoButton);
    HBox.setHgrow(duracaoTextField, Priority.ALWAYS);

    Label participantesLabel = new Label("Participantes");

    TextField participantesTextField = new TextField();
    participantesTextField.setText(String.valueOf(eventosUsuarioService.buscarParticipantesEvento(evento.getId())));
    participantesTextField.setEditable(false);
    participantesTextField.setAlignment(Pos.CENTER);

    Label statusLabel = new Label("Status");

    TextField statusTextField = new TextField();
    statusTextField.setText(evento.getStatus().getDescricao());
    statusTextField.setEditable(false);
    statusTextField.setAlignment(Pos.CENTER);

    Label prioridadeLabel = new Label("Prioridade do evento");

    TextField prioridadeTextField = new TextField();
    prioridadeTextField.setText(evento.getPrioridade().getDescricao());
    prioridadeTextField.setEditable(false);
    prioridadeTextField.setAlignment(Pos.CENTER);

    Button cancelButton = new Button("Cancelar evento");
    cancelButton.getStyleClass().add("edit-button");
    cancelButton.setDisable(true);

    Image image = new Image(getClass().getResource("/icons/close.png").toExternalForm());

    ImageView icon = new ImageView(image);
    icon.setFitHeight(25);
    icon.setFitWidth(25);

    cancelButton.setGraphic(icon);
    cancelButton.setGraphicTextGap(7.5);
    cancelButton.setContentDisplay(ContentDisplay.RIGHT);

    StatusEventoEnum status = StatusEventoEnum.parse(statusTextField.getText());

    if (status.equals(StatusEventoEnum.AGENDADO)) {
      cancelButton.setDisable(false);
      cancelButton.setOnAction(event -> cancelEventAction(evento));
    }

    switch (status) {
      case AGENDADO:
        statusTextField.getStyleClass().add("success-card");
        break;
      case FINALIZADO:
        statusTextField.getStyleClass().add("warning-card");
        break;
      case CANCELADO:
        statusTextField.getStyleClass().add("error-card");
        break;
    }

    VBox colunaParticipantes = new VBox(10);
    colunaParticipantes.getChildren().addAll(participantesLabel, participantesTextField);

    VBox colunaStatus = new VBox(10);
    colunaStatus.getChildren().addAll(statusLabel, statusTextField);

    HBox participantesRow = new HBox(10);
    participantesRow.getChildren().addAll(colunaParticipantes, colunaStatus);

    HBox cancelRow = new HBox(10, cancelButton);
    cancelRow.setAlignment(Pos.CENTER_RIGHT);

    Region space1 = new Region();
    space1.setPrefWidth(10);

    Region space2 = new Region();
    space2.setPrefWidth(10);

    card.getChildren().addAll(
      nomeRow,
      space1,
      dataLabel,
      dataRow,
      duracaoLabel,
      duracaoRow,
      participantesRow,
      prioridadeLabel,
      prioridadeTextField,
      space2,
      cancelRow
    );

    return card;
  }

  private void createDateDialog(Evento evento, TextField textField) {
    Dialog<Void> dialog = new Dialog<>();
    dialog.setTitle("Editar data");
    dialog.setHeaderText("Digite a nova data do evento");
    dialog.setOnShown(event -> AlertUtils.positionDialog(dialog));
    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
    dialog.getDialogPane().getStylesheets().add(getClass().getResource("/pages/Style.css").toExternalForm());

    Label dataAtualLabel = new Label("Data atual");

    TextField dataTextField = new TextField();
    dataTextField.setPromptText(textField.getText());
    dataTextField.setEditable(false);
    dataTextField.setAlignment(Pos.CENTER);

    Label novaDataLabel = new Label("Nova data");

    TextField novaDataTextField = new TextField();
    novaDataTextField.setAlignment(Pos.CENTER);
    novaDataTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
      String value = MaskUtils.applyMaskDate(newValue);

      if (!newValue.equals(value)) {
        novaDataTextField.setText(value);
        novaDataTextField.selectPositionCaret(value.length());
      }
    });

    VBox vbox = new VBox(10, dataAtualLabel, dataTextField, novaDataLabel, novaDataTextField);
    dialog.getDialogPane().setContent(vbox);

    Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
    okButton.setOnAction(actionEvent -> editDateAction(evento, novaDataTextField, dialog));

    dialog.setOnCloseRequest(e -> dialog.close());

    dialog.show();
  }

  private void editDateAction(Evento evento, TextField novaDataTextField, Dialog<Void> dialog) {
    String novaData = novaDataTextField.getText();

    if (novaData.isBlank()) {
      AlertUtils.showValidateAlert("O campo nova data deve ser preenchido.");
      dialog.close();
    }

    evento.setData(LocalDateTime.parse(novaData, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

    try {
      eventoService.editarEvento(evento.getId(), evento);
    }
    catch (ServiceException e) {
      AlertUtils.showWarningAlert(e.getMessage());
      dialog.close();
    }
    catch (Exception e) {
      AlertUtils.showErrorAlert("Ocorreu um erro durante o processamento.");
      dialog.close();
    }

    cardsContent.getChildren().clear();
    fillContentCards();

    AlertUtils.showSuccessAlert("Evento atualizado com sucesso. \nOs usuários inscritos serão notificados.");

    dialog.close();
  }

  private void createDuracaoDialog(Evento evento, TextField textField) {
    Dialog<Void> dialog = new Dialog<>();
    dialog.setTitle("Editar duração");
    dialog.setHeaderText("Digite a nova duração do evento");
    dialog.setOnShown(event -> AlertUtils.positionDialog(dialog));
    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
    dialog.getDialogPane().getStylesheets().add(getClass().getResource("/pages/Style.css").toExternalForm());

    Label duracaoAtualLabel = new Label("Duração atual");

    TextField duracaoTextField = new TextField();
    duracaoTextField.setPromptText(textField.getText());
    duracaoTextField.setEditable(false);
    duracaoTextField.setAlignment(Pos.CENTER);

    Label novaDuracaoLabel = new Label("Nova duração");

    TextField novaDuracaoTextField = new TextField();
    novaDuracaoTextField.setAlignment(Pos.CENTER);
    novaDuracaoTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
      String value = MaskUtils.applyMaskTime(newValue);

      if (!newValue.equals(value)) {
        novaDuracaoTextField.setText(value);
        novaDuracaoTextField.selectPositionCaret(value.length());
      }
    });

    VBox vbox = new VBox(10, duracaoAtualLabel, duracaoTextField, novaDuracaoLabel, novaDuracaoTextField);
    dialog.getDialogPane().setContent(vbox);

    Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
    okButton.setOnAction(actionEvent -> editDurationAction(evento, novaDuracaoTextField, dialog));

    dialog.setOnCloseRequest(e -> dialog.close());

    dialog.show();
  }

  private void editDurationAction(Evento evento, TextField novaDuracaoTextField, Dialog<Void> dialog) {
    String novaDuracao = novaDuracaoTextField.getText();

    if (novaDuracao.isBlank()) {
      AlertUtils.showValidateAlert("O campo nova duração deve ser preenchido.");
      dialog.close();
    }

    evento.setDuracao(LocalTime.parse(novaDuracao));

    try {
      eventoService.editarEvento(evento.getId(), evento);
    }
    catch (ServiceException e) {
      AlertUtils.showWarningAlert(e.getMessage());
      dialog.close();
    }
    catch (Exception e) {
      AlertUtils.showErrorAlert("Ocorreu um erro durante o processamento.");
      dialog.close();
    }

    cardsContent.getChildren().clear();
    fillContentCards();

    AlertUtils.showSuccessAlert("Evento atualizado com sucesso. \nOs usuários inscritos serão notificados.");

    dialog.close();
  }

  private void cancelEventAction(Evento evento) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmação");
    alert.setHeaderText("Cancelar evento");
    alert.setContentText("Deseja mesmo cancelar o evento %s ?".formatted(evento.getNome()));
    alert.setOnShown(event -> AlertUtils.positionAlert(alert));

    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) {
        try {
          eventoService.cancelarEvento(evento.getId());
        }
        catch (ServiceException e) {
          AlertUtils.showWarningAlert(e.getMessage());
          return;
        }
        catch (Exception e) {
          AlertUtils.showErrorAlert("Ocorreu um erro durante o processamento.");
          return;
        }

        cardsContent.getChildren().clear();
        fillContentCards();

        AlertUtils.showSuccessAlert("Evento cancelado com sucesso. \nOs usuários inscritos serão notificados.");
      }
    });
  }

  private void filtroAbertoPublicAction(ObservableValue<? extends Boolean> observable,
    Boolean oldValue, Boolean newValue) {

    if (newValue) {
      filtroObrigatorioAlunoCheckBox.setSelected(false);
      filtroObrigatorioProfessorCheckBox.setSelected(false);
      filtroSomenteAlunosCheckBox.setSelected(false);
      filtroSomenteProfessoresCheckBox.setSelected(false);
      filtroTodosCheckBox.setSelected(false);

      filtroAbertoPublicoCheckBox.setSelected(true);

      List<Evento> eventos = eventoService.buscarTodosEventosPorPrioridade(PrioridadeEventoEnum.ABERTO);

      fillContentCards(eventos);
    }
  }

  private void filtroObrigatorioAlunoAction(ObservableValue<? extends Boolean> observable, Boolean oldValue,
    Boolean newValue) {
    if (newValue) {
      filtroAbertoPublicoCheckBox.setSelected(false);
      filtroObrigatorioProfessorCheckBox.setSelected(false);
      filtroSomenteAlunosCheckBox.setSelected(false);
      filtroSomenteProfessoresCheckBox.setSelected(false);
      filtroTodosCheckBox.setSelected(false);

      filtroObrigatorioAlunoCheckBox.setSelected(true);

      List<Evento> eventos = eventoService.buscarTodosEventosPorPrioridade(
        PrioridadeEventoEnum.OBRIGATORIO_ALUNOS);

      fillContentCards(eventos);
    }
  }

  private void filtroObrigatorioProfessorAction(ObservableValue<? extends Boolean> observable, Boolean oldValue,
    Boolean newValue) {
    if (newValue) {
      filtroAbertoPublicoCheckBox.setSelected(false);
      filtroObrigatorioAlunoCheckBox.setSelected(false);
      filtroSomenteAlunosCheckBox.setSelected(false);
      filtroSomenteProfessoresCheckBox.setSelected(false);
      filtroTodosCheckBox.setSelected(false);

      filtroObrigatorioProfessorCheckBox.setSelected(true);

      List<Evento> eventos = eventoService.buscarTodosEventosPorPrioridade(
        PrioridadeEventoEnum.OBRIGATORIO_PROFESSORES);

      fillContentCards(eventos);
    }
  }

  private void filtroSomenteAlunosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue,
    Boolean newValue) {
    if (newValue) {
      filtroAbertoPublicoCheckBox.setSelected(false);
      filtroObrigatorioAlunoCheckBox.setSelected(false);
      filtroObrigatorioProfessorCheckBox.setSelected(false);
      filtroSomenteProfessoresCheckBox.setSelected(false);
      filtroTodosCheckBox.setSelected(false);

      filtroSomenteAlunosCheckBox.setSelected(true);

      List<Evento> eventos = eventoService.buscarTodosEventosPorPrioridade(
        PrioridadeEventoEnum.SOMENTE_ALUNOS);

      fillContentCards(eventos);
    }
  }

  private void filtroSomenteProfessoresAction(ObservableValue<? extends Boolean> observable, Boolean oldValue,
    Boolean newValue) {
    if (newValue) {
      filtroAbertoPublicoCheckBox.setSelected(false);
      filtroObrigatorioAlunoCheckBox.setSelected(false);
      filtroObrigatorioProfessorCheckBox.setSelected(false);
      filtroSomenteAlunosCheckBox.setSelected(false);
      filtroTodosCheckBox.setSelected(false);

      filtroSomenteProfessoresCheckBox.setSelected(true);

      List<Evento> eventos = eventoService.buscarTodosEventosPorPrioridade(
        PrioridadeEventoEnum.SOMENTE_PROFESSORES);

      fillContentCards(eventos);
    }
  }

  private void filtroTodosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue,
    Boolean newValue) {
    if (newValue) {
      filtroAbertoPublicoCheckBox.setSelected(false);
      filtroObrigatorioAlunoCheckBox.setSelected(false);
      filtroObrigatorioProfessorCheckBox.setSelected(false);
      filtroSomenteAlunosCheckBox.setSelected(false);
      filtroSomenteProfessoresCheckBox.setSelected(false);

      filtroTodosCheckBox.setSelected(true);

      List<Evento> eventos = eventoService.buscarTodosEventos();

      fillContentCards(eventos);
    }
  }
}