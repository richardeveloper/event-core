package br.com.kafka;

import br.com.kafka.entities.Evento;
import br.com.kafka.enums.PrioridadeEventoEnum;
import br.com.kafka.enums.StatusEventoEnum;
import br.com.kafka.exceptions.ServiceException;
import br.com.kafka.services.EventoService;
import br.com.kafka.utils.AlertUtils;
import br.com.kafka.utils.MaskUtils;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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

  @Autowired
  private EventoService eventoService;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    prioridadeComboBox.setItems(FXCollections.observableArrayList(
      Arrays.stream(PrioridadeEventoEnum.values()).map(PrioridadeEventoEnum::getDescricao).toArray(String[]::new)
    ));

    dataTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
      String value = MaskUtils.applyMaskDate(newValue);

      if (!value.equals(newValue)) {
        dataTextField.setText(value);
        dataTextField.selectPositionCaret(value.length());
      }
    });

    duracaoTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
      String value = MaskUtils.applyMaskTime(newValue);

      if (!value.equals(newValue)) {
        duracaoTextField.setText(value);
        duracaoTextField.selectPositionCaret(value.length());
      }
    });
  }

  @FXML
  protected void saveEvento(ActionEvent event) {

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

    Evento evento = new Evento();
    evento.setNome(nomeTextField.getText());
    evento.setData(data);
    evento.setDuracao(duracao);
    evento.setPrioridade(PrioridadeEventoEnum.parse(prioridadeComboBox.getSelectionModel().getSelectedItem()));
    evento.setStatus(StatusEventoEnum.AGENDADO);

    try {
      eventoService.save(evento);
    }
    catch (ServiceException e) {
      AlertUtils.showErrorAlert(e.getMessage());
      return;
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      AlertUtils.showErrorAlert("Ocorreu um erro durante o processamento.");
      return;
    }


    AlertUtils.showSuccessAlert("Evento cadastrado com sucesso.");
  }

}