package br.com.kafka.controllers;

import br.com.kafka.entities.Usuario;
import br.com.kafka.enums.TipoUsuarioEnum;
import br.com.kafka.exceptions.ServiceException;
import br.com.kafka.services.UsuarioService;
import br.com.kafka.utils.AlertUtils;
import br.com.kafka.utils.MaskUtils;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CadastroUsuarioController implements Initializable {

  @FXML
  private TextField nomeTextField;

  @FXML
  private TextField cpfTextField;

  @FXML
  private TextField emailTextField;

  @FXML
  private TextField telefoneTextField;

  @FXML
  private ComboBox<String> cargoComboBox;

  @FXML
  private Button saveButton;

  @Autowired
  private UsuarioService usuarioService;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    cargoComboBox.setItems(FXCollections.observableArrayList(
      Arrays.stream(TipoUsuarioEnum.values()).map(TipoUsuarioEnum::getDescricao).toArray(String[]::new)
    ));

    cpfTextField.textProperty().addListener(
      (observable, oldValue, newValue) -> {
      String value = MaskUtils.applyMaskCpf(newValue);

      if (!newValue.equals(value)) {
        cpfTextField.setText(value);
        cpfTextField.positionCaret(value.length());
      }
    });

    telefoneTextField.textProperty().addListener(
      (observable, oldValue, newValue) -> {
      String value = MaskUtils.applyMaskPhone(newValue);

      if (!newValue.equals(value)) {
        telefoneTextField.setText(value);
        telefoneTextField.positionCaret(value.length());
      }
    });

    saveButton.getStyleClass().add("edit-button");

    Image image = new Image(getClass().getResource("/icons/save.png").toExternalForm());

    ImageView icon = new ImageView(image);
    icon.setFitHeight(25);
    icon.setFitWidth(25);

    saveButton.setGraphic(icon);
    saveButton.setGraphicTextGap(7.5);
    saveButton.setContentDisplay(ContentDisplay.RIGHT);
  }

  @FXML
  protected void salvarUsuario(ActionEvent event) {

    if (nomeTextField.getText().isBlank()) {
      AlertUtils.showValidateAlert("O campo nome deve ser preenchido.");
      return;
    }

    if (cpfTextField.getText().isBlank()) {
      AlertUtils.showValidateAlert("O campo cpf deve ser preenchido.");
      return;
    }

    String cpf = MaskUtils.removeMask(cpfTextField.getText());

    if (cpf.length() != 11) {
      AlertUtils.showValidateAlert("O campo cpf deve ter 11 dígitos.");
      return;
    }

    if (emailTextField.getText().isBlank()) {
      AlertUtils.showValidateAlert("O campo email deve ser preenchido.");
      return;
    }

    if (telefoneTextField.getText().isBlank()) {
      AlertUtils.showValidateAlert("O campo telefone deve ser preenchido.");
      return;
    }

    String telefone = MaskUtils.removeMask(cpfTextField.getText());

    if (telefone.length() != 11) {
      AlertUtils.showValidateAlert("O campo telefone deve ter 11 dígitos.");
      return;
    }

    if (cargoComboBox.getSelectionModel().getSelectedItem() == null) {
      AlertUtils.showValidateAlert("O campo tipo de usuário deve ser preenchido.");
      return;
    }

    Usuario usuario = new Usuario();
    usuario.setNome(nomeTextField.getText());
    usuario.setCpf(cpf);
    usuario.setEmail(emailTextField.getText());
    usuario.setTelefone(telefone);
    usuario.setTipoUsuario(TipoUsuarioEnum.parse(cargoComboBox.getSelectionModel().getSelectedItem()));

    try {
      usuarioService.salvarUsuario(usuario);
    }
    catch (ServiceException e) {
      AlertUtils.showWarningAlert(e.getMessage());
      return;
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      AlertUtils.showErrorAlert("Ocorreu um erro durante o processamento.");
      return;
    }

    limparFormulario();

    AlertUtils.showSuccessAlert("Usuário cadastrado com sucesso.");
  }

  private void limparFormulario() {
    nomeTextField.clear();
    cpfTextField.clear();
    emailTextField.clear();
    telefoneTextField.clear();
    cargoComboBox.getSelectionModel().clearSelection();
  }

}