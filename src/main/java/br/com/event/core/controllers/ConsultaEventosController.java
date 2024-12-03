package br.com.event.core.controllers;

import br.com.event.core.entities.Evento;
import br.com.event.core.enums.StatusEventoEnum;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.services.EventoService;
import br.com.event.core.services.EventosUsuarioService;
import br.com.event.core.utils.AlertUtils;
import br.com.event.core.utils.MaskUtils;
import br.com.event.core.utils.ResourceUtils;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
  private CheckBox filtroEmAndamentoCheckBox;

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

  private List<CheckBox> checkBoxList;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    fillContentCards();
    setUpFilters();
  }

  private void fillContentCards() {
    cardsContent.getChildren().clear();

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

  private void setUpFilters() {
    filtroEmAndamentoCheckBox.setText(StatusEventoEnum.EM_ANDAMENTO.getDescricao());
    filtroEmAndamentoCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroAgendadosCheckBox.setText(StatusEventoEnum.AGENDADO.getDescricao());
    filtroAgendadosCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroCanceladosCheckBox.setText(StatusEventoEnum.CANCELADO.getDescricao());
    filtroCanceladosCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroFinalizadosCheckBox.setText(StatusEventoEnum.FINALIZADO.getDescricao());
    filtroFinalizadosCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroTodosCheckBox.selectedProperty().addListener(this::filtroTodosAction);

    filterIcon.setImage(ResourceUtils.getIcon("/icons/filter.png", 20, 20).getImage());

    checkBoxList = List.of(
      filtroEmAndamentoCheckBox,
      filtroAgendadosCheckBox,
      filtroCanceladosCheckBox,
      filtroFinalizadosCheckBox
    );
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

    StatusEventoEnum statusEventoEnum = StatusEventoEnum.parse(statusTextField.getText());

    switch (statusEventoEnum) {
      case EM_ANDAMENTO, AGENDADO -> statusTextField.getStyleClass().add("success-card");
      case FINALIZADO -> statusTextField.getStyleClass().add("warning-card");
      case CANCELADO -> statusTextField.getStyleClass().add("error-card");
    }

    HBox firstRow = createRowCard(dataLabel, dataTextField, duracaoLabel, duracaoTextField);
    HBox secondRow = createRowCard(participantesLabel, participantesTextField, statusLabel, statusTextField);

    Button deleteButton = new Button("Apagar evento");
    deleteButton.getStyleClass().add("delete-button");

    ImageView icon = ResourceUtils.getIcon("/icons/trash.png", 25, 25);

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

          fillContentCards();

          AlertUtils.showSuccessAlert("Evento apagado com sucesso.");
        }
      });
    });

    HBox footer = new HBox(10, deleteButton);
    footer.setAlignment(Pos.BASELINE_RIGHT);

    card.getChildren().addAll(
      nomeRow,
      createSpace(),
      firstRow,
      secondRow,
      prioridadeLabel,
      prioridadeTextField,
      createSpace(),
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

  private Region createSpace() {
    Region space = new Region();
    space.setPrefWidth(10);
    return space;
  }

  private void filtrosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    List<StatusEventoEnum> statusEventoList = new ArrayList<>();

    for (CheckBox checkBox : checkBoxList) {
      if (checkBox.isSelected()) {
        String statusEvento = checkBox.getText();

        StatusEventoEnum statusEventoEnum = StatusEventoEnum.parse(statusEvento);
        statusEventoList.add(statusEventoEnum);
      }
    }

    if (statusEventoList.isEmpty()) {
      fillContentCards();
      return;
    }

    List<Evento> eventos = eventoService.buscarTodosEventosPorStatus(statusEventoList);

    fillContentCards(eventos);
  }

  private void filtroTodosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

    if (newValue) {
      checkBoxList.forEach(checkBox -> checkBox.setSelected(true));

      List<StatusEventoEnum> statusEventoList = Arrays.stream(StatusEventoEnum.values()).toList();

      if (statusEventoList.isEmpty()) {
        fillContentCards();
        return;
      }

      List<Evento> eventos = eventoService.buscarTodosEventosPorStatus(statusEventoList);

      fillContentCards(eventos);
    }
    else {
      checkBoxList.forEach(checkBox -> checkBox.setSelected(false));
    }
  }

}