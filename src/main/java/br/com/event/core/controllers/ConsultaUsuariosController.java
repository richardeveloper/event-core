package br.com.event.core.controllers;

import br.com.event.core.entities.Usuario;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.services.UsuarioService;
import br.com.event.core.utils.AlertUtils;
import br.com.event.core.utils.MaskUtils;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsultaUsuariosController implements Initializable {

  @FXML
  private FlowPane cardsContent;

  @Autowired
  private UsuarioService usuarioService;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    fillContentCards();
  }

  private void fillContentCards() {
    List<Usuario> usuarios = usuarioService.buscarTodosUsuarios();

    for (Usuario usuario : usuarios) {
      VBox card = createCard(usuario);
      cardsContent.getChildren().add(card);
    }
  }

  private VBox createCard(Usuario usuario) {
    VBox card = new VBox();
    card.setSpacing(10);

    card.getStyleClass().add("card");

    Label nomeLabel = new Label(usuario.getNome());
    nomeLabel.getStyleClass().add("card-title");
    HBox nomeRow = new HBox(nomeLabel);
    nomeRow.setAlignment(Pos.CENTER);

    Label cpfLabel = new Label("CPF");

    TextField cpfTextField = new TextField();
    cpfTextField.setText(MaskUtils.applyMaskCpf(usuario.getCpf()));
    cpfTextField.setEditable(false);
    cpfTextField.setAlignment(Pos.CENTER);

    Label matriculaLabel = new Label("Matrícula");

    TextField matriculaTextField = new TextField();
    matriculaTextField.setText(usuario.getMatricula());
    matriculaTextField.setEditable(false);
    matriculaTextField.setAlignment(Pos.CENTER);

    Label emailLabel = new Label("E-mail");

    TextField emailTextField = new TextField();
    emailTextField.setText(usuario.getEmail());
    emailTextField.setEditable(false);
    emailTextField.setAlignment(Pos.CENTER);

    Label telefoneLabel = new Label("Telefone");

    TextField telefoneTextField = new TextField();
    telefoneTextField.setText(MaskUtils.applyMaskPhone(usuario.getTelefone()));
    telefoneTextField.setEditable(false);
    telefoneTextField.setAlignment(Pos.CENTER);

    Label tipoUsuarioLabel = new Label("Tipo de Usuário");

    TextField tipoUsuarioTextField = new TextField();
    tipoUsuarioTextField.setText(usuario.getTipoUsuario().getDescricao());
    tipoUsuarioTextField.setEditable(false);
    tipoUsuarioTextField.setAlignment(Pos.CENTER);

    Button deleteButton = new Button("Apagar usuário");
    deleteButton.getStyleClass().add("delete-button");

    Image image = new Image(getClass().getResource("/icons/trash.png").toExternalForm());

    ImageView icon = new ImageView(image);
    icon.setFitHeight(25);
    icon.setFitWidth(25);

    deleteButton.setGraphic(icon);
    deleteButton.setGraphicTextGap(7.5);
    deleteButton.setContentDisplay(ContentDisplay.RIGHT);

    deleteButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
      Alert alert = AlertUtils.showDeleteUserAlert(usuario.getNome());

      alert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {

          try {
            usuarioService.apagarUsuario(usuario.getId());
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

          AlertUtils.showSuccessAlert("Usuário apagado com sucesso.");
        }
      });
    });

    HBox footer = new HBox(10, deleteButton);
    footer.setAlignment(Pos.BASELINE_RIGHT);

    HBox firstRow = createRowCard(cpfLabel, cpfTextField, matriculaLabel, matriculaTextField);
    HBox secondRow = createRowCard(telefoneLabel, telefoneTextField, tipoUsuarioLabel, tipoUsuarioTextField);

    Region space1 = new Region();
    space1.setPrefWidth(10);

    Region space2 = new Region();
    space2.setPrefWidth(10);

    card.getChildren().addAll(
      nomeRow,
      space1,
      firstRow,
      emailLabel,
      emailTextField,
      secondRow,
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

    return new HBox(10, firstColumn, secondColumn);
  }

}