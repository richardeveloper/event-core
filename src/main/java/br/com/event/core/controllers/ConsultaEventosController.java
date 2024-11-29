package br.com.event.core.controllers;

import br.com.event.core.entities.Evento;
import br.com.event.core.enums.StatusEventoEnum;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.services.EventoService;
import br.com.event.core.services.EventosUsuarioService;
import br.com.event.core.utils.AlertUtils;
import br.com.event.core.utils.MaskUtils;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsultaEventosController implements Initializable {

  @FXML
  private FlowPane cardsContent;

  @FXML
  private CheckBox filtroAgendadosCheckBox;

  @FXML
  private CheckBox filtroCanceladosCheckBox;

  @FXML
  private CheckBox filtroFinalizadosCheckBox;

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

    filtroAgendadosCheckBox.selectedProperty().addListener(this::filtroAgendadosAction);
    filtroCanceladosCheckBox.selectedProperty().addListener(this::filtroCanceladosAction);
    filtroFinalizadosCheckBox.selectedProperty().addListener(this::filtroFinalizadosAction);
    filtroTodosCheckBox.selectedProperty().addListener(this::filtroTodosAction);
  }

  private void fillContentCards() {
    List<Evento> eventos = eventoService.buscarTodosEventos();

    if (eventos.isEmpty()) {
      cardsContent.getChildren().add(new Label("Nenhum evento foi encontrado"));
    }

    for (Evento evento : eventos) {
      VBox card = createCard(evento);
      cardsContent.getChildren().add(card);
    }
  }

  private void fillContentCards(List<Evento> eventos) {
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

    Label nomeLabel = new Label(evento.getNome());
    nomeLabel.getStyleClass().add("card-title");
    HBox nomeRow = new HBox(nomeLabel);
    nomeRow.setAlignment(Pos.CENTER);

    Label dataLabel = new Label("Data do evento");

    TextField dataTextField = new TextField();
    dataTextField.setText(MaskUtils.applyMaskDate(evento.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
    dataTextField.setEditable(false);
    dataTextField.setAlignment(Pos.CENTER);

    Label duracaoLabel = new Label("Duração do evento");

    TextField duracaoTextField = new TextField();
    duracaoTextField.setText(MaskUtils.applyMaskTime(evento.getDuracao().toString()));
    duracaoTextField.setEditable(false);
    duracaoTextField.setAlignment(Pos.CENTER);

    Label prioridadeLabel = new Label("Prioridade do evento");

    TextField prioridadeTextField = new TextField();
    prioridadeTextField.setText(evento.getPrioridade().getDescricao());
    prioridadeTextField.setEditable(false);
    prioridadeTextField.setAlignment(Pos.CENTER);

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

    StatusEventoEnum status = StatusEventoEnum.parse(statusTextField.getText());

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

    HBox firstRow = createRowCard(dataLabel, dataTextField, duracaoLabel, duracaoTextField);
    HBox secondRow = createRowCard(participantesLabel, participantesTextField, statusLabel, statusTextField);

    Button deleteButton = new Button("Apagar evento");
    deleteButton.getStyleClass().add("delete-button");

    Image image = new Image(getClass().getResource("/icons/trash.png").toExternalForm());

    ImageView icon = new ImageView(image);
    icon.setFitHeight(25);
    icon.setFitWidth(25);

    deleteButton.setGraphic(icon);
    deleteButton.setGraphicTextGap(7.5);
    deleteButton.setContentDisplay(ContentDisplay.RIGHT);

    deleteButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
      Alert alert = AlertUtils.showDeleteEventAlert(evento.getNome());

      alert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {

          try {
            eventoService.apagarEvento(evento.getId());
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

          if (filtroAgendadosCheckBox.isSelected()) {
            filtroAgendadosCheckBox.setSelected(false);
            filtroAgendadosCheckBox.setSelected(true);
          }
          else if (filtroCanceladosCheckBox.isSelected()) {
            filtroCanceladosCheckBox.setSelected(false);
            filtroCanceladosCheckBox.setSelected(true);
          }
          else if (filtroFinalizadosCheckBox.isSelected()) {
            filtroFinalizadosCheckBox.setSelected(false);
            filtroFinalizadosCheckBox.setSelected(true);
          }
          else {
            filtroTodosCheckBox.setSelected(false);
            filtroTodosCheckBox.setSelected(true);
          }

          AlertUtils.showSuccessAlert("Evento apagado com sucesso.");
        }
      });
    });

    HBox footer = new HBox(10, deleteButton);
    footer.setAlignment(Pos.BASELINE_RIGHT);

    Region space1 = new Region();
    space1.setPrefWidth(10);

    Region space2 = new Region();
    space2.setPrefWidth(10);

    card.getChildren().addAll(
      nomeRow,
      space1,
      firstRow,
      secondRow,
      prioridadeLabel,
      prioridadeTextField,
      space2,
      footer
    );

    return card;
  }

  private HBox createRowCard(Label primeiroLabel, TextField primeiroTextField, Label segundoLabel,
    TextField segundoTextField) {

    VBox firstColumn = new VBox(10);
    firstColumn.getChildren().addAll(primeiroLabel, primeiroTextField);

    VBox secondColumn = new VBox(10);
    secondColumn.getChildren().addAll(segundoLabel, segundoTextField);

    HBox row = new HBox(10, firstColumn, secondColumn);
    HBox.setHgrow(firstColumn, Priority.ALWAYS);

    return row;
  }

  private void filtroAgendadosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    if (newValue) {
      filtroCanceladosCheckBox.setSelected(false);
      filtroFinalizadosCheckBox.setSelected(false);
      filtroTodosCheckBox.setSelected(false);

      filtroAgendadosCheckBox.setSelected(true);

      List<Evento> eventos = eventoService.buscarTodosEventosPorStatus(StatusEventoEnum.AGENDADO);

      fillContentCards(eventos);
    }
  }

  private void filtroCanceladosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    if (newValue) {
      filtroAgendadosCheckBox.setSelected(false);
      filtroFinalizadosCheckBox.setSelected(false);
      filtroTodosCheckBox.setSelected(false);

      filtroCanceladosCheckBox.setSelected(true);

      List<Evento> eventos = eventoService.buscarTodosEventosPorStatus(StatusEventoEnum.CANCELADO);

      fillContentCards(eventos);
    }
  }

  private void filtroFinalizadosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    if (newValue) {
      filtroAgendadosCheckBox.setSelected(false);
      filtroCanceladosCheckBox.setSelected(false);
      filtroTodosCheckBox.setSelected(false);

      filtroFinalizadosCheckBox.setSelected(true);

      List<Evento> eventos = eventoService.buscarTodosEventosPorStatus(StatusEventoEnum.FINALIZADO);

      fillContentCards(eventos);
    }
  }

  private void filtroTodosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    if (newValue) {
      filtroAgendadosCheckBox.setSelected(false);
      filtroCanceladosCheckBox.setSelected(false);
      filtroFinalizadosCheckBox.setSelected(true);

      filtroTodosCheckBox.setSelected(true);

      List<Evento> eventos = eventoService.buscarTodosEventos();

      fillContentCards(eventos);
    }
  }
}