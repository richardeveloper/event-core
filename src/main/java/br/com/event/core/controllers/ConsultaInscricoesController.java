package br.com.event.core.controllers;

import br.com.event.core.entities.Evento;
import br.com.event.core.entities.EventosUsuario;
import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.StatusEventoEnum;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.services.EventosUsuarioService;
import br.com.event.core.services.UsuarioService;
import br.com.event.core.utils.AlertUtils;
import br.com.event.core.utils.ResourceUtils;
import br.com.event.core.utils.MaskUtils;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
public class ConsultaInscricoesController implements Initializable {

  @FXML
  private TextField nomeTextField;

  @FXML
  private ListView<String> usuariosListView;

  @FXML
  private FlowPane cardsContent;

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private EventosUsuarioService eventosUsuarioService;

  private List<Usuario> usuarios = new ArrayList<>();

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    usuariosListView.setPlaceholder(new Label("Nenhum usuário encontrado"));
    cardsContent.getChildren().add(new Label("Selecione um usuário para listar suas inscrições"));

    nomeTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
      if (newValue != null && !newValue.isEmpty()) {
        usuarios = usuarioService.buscarTodosUsuariosPorNome(newValue);

        List<String> nomesUsuarios = usuarios
          .stream()
          .map(MaskUtils::applyInfoUserMask)
          .toList();

        if (!usuarios.isEmpty()) {
          usuariosListView.setItems(FXCollections.observableArrayList(nomesUsuarios));
          usuariosListView.setPlaceholder(new Label("Nenhum usuário encontrado"));
          return;
        }
      }

      usuariosListView.setItems(FXCollections.observableArrayList());
      usuariosListView.setPlaceholder(new Label("Nenhum usuário encontrado"));

      cardsContent.getChildren().clear();
      cardsContent.getChildren().add(new Label("Selecione um usuário para listar suas inscrições"));
    });

    usuariosListView.getSelectionModel().selectedItemProperty().addListener(
      (observableValue, oldValue, newValue) -> {
        if (newValue != null && !newValue.isEmpty()) {
          Optional<Usuario> usuarioEncontrado = usuarios.stream()
            .filter(usuario -> usuario.getNome().equalsIgnoreCase(MaskUtils.removeInfoUserMask(newValue)))
            .findAny();

          if (usuarioEncontrado.isPresent()) {
            Usuario usuario = usuarioEncontrado.get();

            List<Evento> eventos = eventosUsuarioService.buscarTodosEventosUsuarioPorUsuarioId(usuario.getId())
              .stream()
              .map(EventosUsuario::getEvento)
              .toList();

            if (eventos.isEmpty()) {
              cardsContent.getChildren().clear();
              cardsContent.getChildren().add(new Label("%s não está inscrito em nenhum evento.".formatted(usuario.getNome())));
              return;
            }

            cardsContent.getChildren().clear();
            fillContentCards(eventos);
          }
          else {
            cardsContent.getChildren().clear();
            cardsContent.getChildren().add(new Label("Selecione um usuário para listar suas inscrições"));
          }
        }
      });
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

    Label statusLabel = new Label("Status");

    TextField statusTextField = new TextField();
    statusTextField.setText(evento.getStatus().getDescricao());
    statusTextField.setEditable(false);
    statusTextField.setAlignment(Pos.CENTER);
    statusTextField.setPrefColumnCount(evento.getStatus().getDescricao().length());

    HBox firstRow = createRowCard(dataLabel, dataTextField, duracaoLabel, duracaoTextField);
    HBox secondRow = createRowCard(prioridadeLabel, prioridadeTextField, statusLabel, statusTextField);

    Button deleteButton = new Button("Cancelar inscrição");
    deleteButton.getStyleClass().add("edit-button");
    deleteButton.setDisable(true);

    StatusEventoEnum statusEventoEnum = StatusEventoEnum.parse(statusTextField.getText());

    switch (statusEventoEnum) {
      case AGENDADO -> {
        deleteButton.setDisable(false);
        statusTextField.getStyleClass().add("success-card");
      }
      case EM_ANDAMENTO -> statusTextField.getStyleClass().add("success-card");
      case FINALIZADO -> statusTextField.getStyleClass().add("warning-card");
      case CANCELADO -> statusTextField.getStyleClass().add("error-card");
    }

    ImageView icon = ResourceUtils.getIcon("/icons/close.png", 25, 25);

    deleteButton.setGraphic(icon);
    deleteButton.setGraphicTextGap(7.5);
    deleteButton.setContentDisplay(ContentDisplay.RIGHT);

    deleteButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
      Alert alert = AlertUtils.showDeleteEventUserAlert(evento.getNome());

      alert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {

          String selectedItem = usuariosListView.getSelectionModel().getSelectedItem();
          String nomeUsuario = MaskUtils.removeInfoUserMask(selectedItem);

          Usuario usuario;

          try {
            usuario = usuarioService.buscarUsuarioPorNome(nomeUsuario);
            eventosUsuarioService.cancelarInscricao(usuario.getId(), evento.getId());
          }
          catch (ServiceException e) {
            AlertUtils.showWarningAlert(e.getMessage());
            return;
          }
          catch (Exception e) {
            AlertUtils.showErrorAlert("Ocorreu um erro durante o processamento.");
            return;
          }

          List<Evento> eventos = eventosUsuarioService.buscarTodosEventosUsuarioPorUsuarioId(usuario.getId())
            .stream()
            .map(EventosUsuario::getEvento)
            .toList();

          fillContentCards(eventos);

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

}