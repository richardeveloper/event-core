package br.com.event.core.controllers;

import br.com.event.core.entities.Evento;
import br.com.event.core.enums.PrioridadeEventoEnum;
import br.com.event.core.enums.StatusEventoEnum;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.services.EventoService;
import br.com.event.core.utils.AlertUtils;
import br.com.event.core.utils.IconUtils;
import br.com.event.core.utils.MaskUtils;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CadastroEventoController implements Initializable {

  @FXML
  private TextField nomeTextField;

  @FXML
  private TextField dataTextField;

  @FXML
  private TextField duracaoTextField;

  @FXML
  private ComboBox<String> prioridadeComboBox;

  @FXML
  private Button saveButton;

  @Autowired
  private EventoService eventoService;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    prioridadeComboBox.setItems(FXCollections.observableArrayList(
      Arrays.stream(PrioridadeEventoEnum.values()).map(PrioridadeEventoEnum::getDescricao).toArray(String[]::new)
    ));

    dataTextField.textProperty().addListener(
      (observableValue, oldValue, newValue) -> {
      String value = MaskUtils.applyMaskDate(newValue);

      if (!newValue.equals(value)) {
        dataTextField.setText(value);
        dataTextField.selectPositionCaret(value.length());
      }
    });

    duracaoTextField.textProperty().addListener(
      (observableValue, oldValue, newValue) -> {
      String value = MaskUtils.applyMaskTime(newValue);

      if (!newValue.equals(value)) {
        duracaoTextField.setText(value);
        duracaoTextField.selectPositionCaret(value.length());
      }
    });

    saveButton.getStyleClass().add("edit-button");

    ImageView icon = IconUtils.getIcon("/icons/save.png", 25, 25);

    saveButton.setGraphic(icon);
    saveButton.setGraphicTextGap(7.5);
    saveButton.setContentDisplay(ContentDisplay.RIGHT);
  }

  @FXML
  protected void salvarEvento() {

    if (nomeTextField.getText().isBlank()) {
      AlertUtils.showValidateAlert("O campo nome deve ser preenchido.");
      return;
    }

    if (nomeTextField.getText().isBlank()) {
      AlertUtils.showValidateAlert("O campo cpf deve ser preenchido.");
      return;
    }

    if (dataTextField.getText().isBlank()) {
      AlertUtils.showValidateAlert("O campo duração deve ser preenchido.");
      return;
    }

    LocalDateTime data;

    try {
      data = LocalDateTime.parse(dataTextField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    catch (DateTimeParseException e) {
      log.error(e.getMessage(), e);
      AlertUtils.showValidateAlert("O campo data possui valores de data ou horário inválidos.");
      return;
    }

    if (duracaoTextField.getText().isBlank()) {
      AlertUtils.showValidateAlert("O campo duracao deve ser preenchido.");
      return;
    }

    LocalTime duracao;

    try {
      duracao = LocalTime.parse(duracaoTextField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      AlertUtils.showValidateAlert("O campo data possui valores de horas ou minutos inválidos.");
      return;
    }

    if (prioridadeComboBox.getSelectionModel().getSelectedItem() == null) {
      AlertUtils.showValidateAlert("O campo prioridade deve ser preenchido.");
      return;
    }

    Evento evento = Evento.builder()
      .nome(nomeTextField.getText())
      .data(data)
      .duracao(duracao)
      .prioridade(PrioridadeEventoEnum.parse(prioridadeComboBox.getSelectionModel().getSelectedItem()))
      .status(StatusEventoEnum.AGENDADO)
      .build();

    try {
      eventoService.salvarEvento(evento);
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

    AlertUtils.showSuccessAlert("Evento cadastrado com sucesso.");
  }

  private void limparFormulario() {
    nomeTextField.clear();
    dataTextField.clear();
    duracaoTextField.clear();
    prioridadeComboBox.getSelectionModel().clearSelection();
  }

}