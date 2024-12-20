package br.com.event.core.controllers;

import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.TipoUsuarioEnum;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.services.UsuarioService;
import br.com.event.core.utils.AlertUtils;
import br.com.event.core.utils.MaskUtils;
import br.com.event.core.utils.ResourceUtils;
import java.net.URL;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsultaUsuariosController implements Initializable {

  @FXML
  private FlowPane cardsContent;

  @FXML
  private CheckBox filtroAlunosCheckBox;

  @FXML
  private CheckBox filtroProfessoresCheckBox;

  @FXML
  private CheckBox filtroVisitantesCheckBox;

  @FXML
  private CheckBox filtroTodosCheckBox;

  @FXML
  private ImageView filterIcon;

  @Autowired
  private UsuarioService usuarioService;

  private List<CheckBox> checkBoxList;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    fillContentCards();
    setUpFilters();
  }

  private void fillContentCards() {
    cardsContent.getChildren().clear();

    List<Usuario> usuarios = usuarioService.buscarTodosUsuarios();

    if (usuarios.isEmpty()) {
      cardsContent.getChildren().add(new Label("Nenhum usuário foi encontrado"));
    }

    for (Usuario usuario : usuarios) {
      VBox card = createCard(usuario);
      cardsContent.getChildren().add(card);
    }
  }

  private void fillContentCards(List<Usuario> usuarios) {
    cardsContent.getChildren().clear();

    if (usuarios.isEmpty()) {
      cardsContent.getChildren().add(new Label("Nenhum usuário foi encontrado"));
    }

    for (Usuario usuario : usuarios) {
      VBox card = createCard(usuario);
      cardsContent.getChildren().add(card);
    }
  }

  private void setUpFilters() {
    filtroAlunosCheckBox.setText(TipoUsuarioEnum.ALUNO.getDescricao());
    filtroAlunosCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroProfessoresCheckBox.setText(TipoUsuarioEnum.PROFESSOR.getDescricao());
    filtroProfessoresCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroVisitantesCheckBox.setText(TipoUsuarioEnum.VISITANTE.getDescricao());
    filtroVisitantesCheckBox.selectedProperty().addListener(this::filtrosAction);

    filtroTodosCheckBox.selectedProperty().addListener(this::filtroTodosAction);

    filterIcon.setImage(ResourceUtils.getIcon("/icons/filter.png", 20, 20).getImage());

    checkBoxList = List.of(
      filtroAlunosCheckBox,
      filtroProfessoresCheckBox,
      filtroVisitantesCheckBox
    );
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
    matriculaTextField.setText(usuario.getMatricula() != null ? usuario.getMatricula() : "-");
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

    Label tipoUsuarioLabel = new Label("Tipo de usuário");

    TextField tipoUsuarioTextField = new TextField();
    tipoUsuarioTextField.setText(usuario.getTipoUsuario().getDescricao());
    tipoUsuarioTextField.setEditable(false);
    tipoUsuarioTextField.setAlignment(Pos.CENTER);

    Button deleteButton = new Button("Apagar usuário");
    deleteButton.getStyleClass().add("delete-button");

    ImageView icon = ResourceUtils.getIcon("/icons/trash.png", 25, 25);

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

          fillContentCards();

          AlertUtils.showSuccessAlert("Usuário apagado com sucesso.");
        }
      });
    });

    HBox footer = new HBox(10, deleteButton);
    footer.setAlignment(Pos.BASELINE_RIGHT);

    HBox firstRow = createRowCard(cpfLabel, cpfTextField, matriculaLabel, matriculaTextField);
    HBox secondRow = createRowCard(telefoneLabel, telefoneTextField, tipoUsuarioLabel, tipoUsuarioTextField);

    card.getChildren().addAll(
      nomeRow,
      createSpace(),
      firstRow,
      emailLabel,
      emailTextField,
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

    return new HBox(10, firstColumn, secondColumn);
  }

  private Region createSpace() {
    Region space = new Region();
    space.setPrefWidth(10);
    return space;
  }

  private void filtrosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    List<TipoUsuarioEnum> tipoUsuarioList = new ArrayList<>();

    for (CheckBox checkBox : checkBoxList) {
      if (checkBox.isSelected()) {
        String tipoUsuario = checkBox.getText();

        TipoUsuarioEnum tipoNotificacaoEnum = TipoUsuarioEnum.parse(tipoUsuario);
        tipoUsuarioList.add(tipoNotificacaoEnum);
      }
    }

    if (tipoUsuarioList.isEmpty()) {
      fillContentCards();
      return;
    }

    List<Usuario> usuarios = usuarioService.buscarTodosUsuariosPorTiposUsuarios(tipoUsuarioList);

    fillContentCards(usuarios);
  }

  private void filtroTodosAction(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    if (newValue) {
      checkBoxList.forEach(checkBox -> checkBox.setSelected(true));

      List<TipoUsuarioEnum> tipoUsuarioList = Arrays.stream(TipoUsuarioEnum.values()).toList();

      if (tipoUsuarioList.isEmpty()) {
        fillContentCards();
        return;
      }

      List<Usuario> usuarios = usuarioService.buscarTodosUsuariosPorTiposUsuarios(tipoUsuarioList);

      fillContentCards(usuarios);
    }
    else {
      checkBoxList.forEach(checkBox -> checkBox.setSelected(false));
    }
  }

}