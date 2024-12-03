package br.com.event.core.controllers;

import br.com.event.core.entities.Evento;
import br.com.event.core.enums.PrioridadeEventoEnum;
import br.com.event.core.enums.StatusEventoEnum;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.services.EventoService;
import br.com.event.core.services.EventosUsuarioService;
import br.com.event.core.utils.AlertUtils;
import br.com.event.core.utils.MaskUtils;
import br.com.event.core.utils.ResourceUtils;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
  private ImageView filterIcon;

  @Autowired
  private EventoService eventoService;

  @Autowired
  private EventosUsuarioService eventosUsuarioService;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    fillContentCards();
    setUpFilters();
  }

  private void fillContentCards() {
    cardsContent.getChildren().clear();

    List<Evento> eventos = eventoService.buscarEventosMaisProximos();

    if (eventos.isEmpty()) {
      cardsContent.getChildren().add(new Label("Nenhum evento foi encontrado"));
    }

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

  private void setUpFilters() {
    filtroAbertoPublicoCheckBox.setText(PrioridadeEventoEnum.ABERTO.getDescricao());
    filtroAbertoPublicoCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroObrigatorioAlunoCheckBox.setText(PrioridadeEventoEnum.OBRIGATORIO_ALUNOS.getDescricao());
    filtroObrigatorioAlunoCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroObrigatorioProfessorCheckBox.setText(PrioridadeEventoEnum.OBRIGATORIO_PROFESSORES.getDescricao());
    filtroObrigatorioProfessorCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroSomenteAlunosCheckBox.setText(PrioridadeEventoEnum.SOMENTE_ALUNOS.getDescricao());
    filtroSomenteAlunosCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroSomenteProfessoresCheckBox.setText(PrioridadeEventoEnum.OBRIGATORIO_PROFESSORES.getDescricao());
    filtroSomenteProfessoresCheckBox.selectedProperty().addListener(this::filtrosAction);

    filterIcon.setImage(ResourceUtils.getIcon("/icons/filter.png", 20, 20).getImage());
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
    dataButton.setDisable(true);
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
    duracaoButton.setDisable(true);
    duracaoButton.setOnAction(event -> createDuracaoDialog(evento, duracaoTextField));

    HBox duracaoRow = new HBox(10, duracaoTextField, duracaoButton);
    HBox.setHgrow(duracaoTextField, Priority.ALWAYS);

    Label participantesLabel = new Label("Participantes");

    int participantes = eventosUsuarioService.buscarQuantidadeParticipantes(evento.getId());

    TextField participantesTextField = new TextField();
    participantesTextField.setText(String.valueOf(participantes));
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

    ImageView icon = ResourceUtils.getIcon("/icons/close.png", 25, 25);

    cancelButton.setGraphic(icon);
    cancelButton.setGraphicTextGap(7.5);
    cancelButton.setContentDisplay(ContentDisplay.RIGHT);

    StatusEventoEnum statusEventoEnum = StatusEventoEnum.parse(statusTextField.getText());

    switch (statusEventoEnum) {
      case EM_ANDAMENTO -> statusTextField.getStyleClass().add("success-card");
      case AGENDADO -> {
        dataButton.setDisable(false);
        duracaoButton.setDisable(false);
        cancelButton.setDisable(false);
        cancelButton.setOnAction(event -> cancelEventAction(evento));
        statusTextField.getStyleClass().add("success-card");
      }
      case FINALIZADO -> statusTextField.getStyleClass().add("warning-card");
      case CANCELADO -> statusTextField.getStyleClass().add("error-card");
    }


    VBox colunaParticipantes = new VBox(10);
    colunaParticipantes.getChildren().addAll(participantesLabel, participantesTextField);

    VBox colunaStatus = new VBox(10);
    colunaStatus.getChildren().addAll(statusLabel, statusTextField);

    HBox participantesRow = new HBox(10);
    participantesRow.getChildren().addAll(colunaParticipantes, colunaStatus);

    HBox cancelRow = new HBox(10, cancelButton);
    cancelRow.setAlignment(Pos.CENTER_RIGHT);

    card.getChildren().addAll(
      nomeRow,
      createSpace(),
      dataLabel,
      dataRow,
      duracaoLabel,
      duracaoRow,
      participantesRow,
      prioridadeLabel,
      prioridadeTextField,
      createSpace(),
      cancelRow
    );

    return card;
  }

  private Region createSpace() {
    Region space = new Region();
    space.setPrefWidth(10);
    return space;
  }

  private void createDateDialog(Evento evento, TextField textField) {
    Dialog<Void> dialog = new Dialog<>();
    dialog.setTitle("Editar data");
    dialog.setHeaderText("Digite a nova data do evento");
    dialog.setOnShown(event -> AlertUtils.positionDialog(dialog));
    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
    dialog.getDialogPane().getStylesheets().add(ResourceUtils.getStyle());

    Label dataAtualLabel = new Label("Data atual");

    TextField dataTextField = new TextField();
    dataTextField.setPromptText(textField.getText());
    dataTextField.setEditable(false);
    dataTextField.setAlignment(Pos.CENTER);

    Label novaDataLabel = new Label("Nova data");

    TextField novaDataTextField = new TextField();
    novaDataTextField.setAlignment(Pos.CENTER);
    novaDataTextField.textProperty().addListener(
      (observableValue, oldValue, newValue) -> {
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
    dialog.getDialogPane().getStylesheets().add(ResourceUtils.getStyle());

    Label duracaoAtualLabel = new Label("Duração atual");

    TextField duracaoTextField = new TextField();
    duracaoTextField.setPromptText(textField.getText());
    duracaoTextField.setEditable(false);
    duracaoTextField.setAlignment(Pos.CENTER);

    Label novaDuracaoLabel = new Label("Nova duração");

    TextField novaDuracaoTextField = new TextField();
    novaDuracaoTextField.setAlignment(Pos.CENTER);
    novaDuracaoTextField.textProperty().addListener(
      (observableValue, oldValue, newValue) -> {
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
      return;
    }
    catch (Exception e) {
      AlertUtils.showErrorAlert("Ocorreu um erro durante o processamento.");
      dialog.close();
      return;
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

  private void filtrosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    List<PrioridadeEventoEnum> prioridadeEventoList = new ArrayList<>();

    List<CheckBox> checkBoxList = List.of(
      filtroAbertoPublicoCheckBox,
      filtroObrigatorioAlunoCheckBox,
      filtroObrigatorioProfessorCheckBox,
      filtroSomenteAlunosCheckBox,
      filtroSomenteProfessoresCheckBox
    );

    for (CheckBox checkBox : checkBoxList) {
      if (checkBox.isSelected()) {
        String name = checkBox.getText();

        PrioridadeEventoEnum prioridadeEventoEnum = PrioridadeEventoEnum.parse(name);
        prioridadeEventoList.add(prioridadeEventoEnum);
      }
    }

    if (prioridadeEventoList.isEmpty()) {
      fillContentCards();
      return;
    }

    List<Evento> proximosEventos = eventoService.buscarTodosEventosPorPrioridades(prioridadeEventoList);

    fillContentCards(proximosEventos);
  }

}