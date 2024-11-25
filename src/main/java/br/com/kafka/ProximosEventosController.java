package br.com.kafka;

import br.com.kafka.entities.Evento;
import br.com.kafka.exceptions.ServiceException;
import br.com.kafka.services.EventoService;
import br.com.kafka.utils.AlertUtils;
import br.com.kafka.utils.MaskUtils;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProximosEventosController implements Initializable {

  @FXML
  private FlowPane cardsContent;

  @Autowired
  private EventoService eventoService;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    fillPage();
  }

  private void fillPage() {
    List<Evento> eventos = eventoService.findAllOrderByDataDesc();

    for (Evento evento : eventos) {
      VBox card = createCard(evento);
      cardsContent.getChildren().add(card);
    }
  }

  private VBox createCard(Evento evento) {
    VBox card = new VBox();
    card.setSpacing(10);
    card.setPadding(new Insets(10, 10, 10, 10));
    card.setPrefWidth(380);
    card.setPrefHeight(240);

    card.getStyleClass().add("card");

    Label nomeLabel = new Label(evento.getNome().toUpperCase());
    nomeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

    Label dataLabel = new Label("Data do Evento");
    TextField dataTextField = new TextField();
    dataTextField.setText(evento.getData());
    dataTextField.setEditable(false);

    Button dataButton = new Button("Editar");
    dataButton.setOnAction(event -> createDataDialog(evento, dataTextField));
    HBox dataRow = new HBox(10, dataTextField, dataButton);
    HBox.setHgrow(dataTextField, Priority.ALWAYS);

    Label duracaoLabel = new Label("Duração do Evento");
    TextField duracaoTextField = new TextField();
    duracaoTextField.setText(evento.getDuracao().toString());
    duracaoTextField.setEditable(false);

    Button duracaoButton = new Button("Editar");
    duracaoButton.setOnAction(event -> createDuracaoDialog(evento, duracaoTextField));
    HBox duracaoRow = new HBox(10, duracaoTextField, duracaoButton);
    HBox.setHgrow(duracaoTextField, Priority.ALWAYS);

    Label statusLabel = new Label("Status do Evento");
    TextField statusTextField = new TextField();
    statusTextField.setText(evento.getStatus().getDescricao());
    statusTextField.setEditable(false);

    card.getChildren().addAll(
      nomeLabel,
      dataLabel,
      dataRow,
      duracaoLabel,
      duracaoRow,
      statusLabel,
      statusTextField
    );

    return card;
  }

  private void createDataDialog(Evento evento, TextField textField) {
    Dialog<Void> dialog = new Dialog<>();
    dialog.setTitle("Editar");
    dialog.setHeaderText("Preencha a nova data do evento");

    Label dataAtualLabel = new Label("Data atual");
    TextField dataTextField = new TextField();
    dataTextField.setPromptText(textField.getText());
    dataTextField.setDisable(true);

    Label novaDataLabel = new Label("Nova data");
    TextField novaDataTextField = new TextField();
    novaDataTextField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        String value = MaskUtils.applyMaskDate(newValue);

        if (!value.equals(newValue)) {
          novaDataTextField.setText(value);
          novaDataTextField.selectPositionCaret(value.length());
        }
      }
    });

    VBox vbox = new VBox(10, dataAtualLabel, dataTextField, novaDataLabel, novaDataTextField);
    dialog.getDialogPane().setContent(vbox);

    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

    Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

    okButton.setOnAction(actionEvent -> {
      String novaData = novaDataTextField.getText();

      if (novaData.isBlank()) {
        AlertUtils.showValidateAlert("O campo nova data deve ser preenchido.");
        dialog.close();
      }

      evento.setData(LocalDateTime.parse(novaData, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

      try {
        eventoService.update(evento.getId(), evento);
      }
      catch (ServiceException e) {
        AlertUtils.showValidateAlert(e.getMessage());
        dialog.close();
      }
      catch (Exception e) {
        AlertUtils.showErrorAlert("Ocorreu um erro durante o processamento.");
        dialog.close();
      }

      AlertUtils.showSuccessAlert("Evento atualizado com sucesso. Uma notificação será enviada aos participantes.");

      cardsContent.getChildren().clear();
      fillPage();

      dialog.close();
    });

    dialog.setOnCloseRequest(e -> {
      dialog.close();
    });

    dialog.show();
  }

  private void createDuracaoDialog(Evento evento, TextField textField) {
    Dialog<Void> dialog = new Dialog<>();
    dialog.setTitle("Editar");
    dialog.setHeaderText("Preencha a nova duração do evento");

    Label duracaoAtualLabel = new Label("Duração atual");
    TextField duracaoTextField = new TextField();
    duracaoTextField.setPromptText(textField.getText());
    duracaoTextField.setDisable(true);

    Label novaDuracaoLabel = new Label("Nova duração");
    TextField novaDuracaoTextField = new TextField();
    novaDuracaoTextField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        String value = MaskUtils.applyMaskTime(newValue);

        if (!value.equals(newValue)) {
          novaDuracaoTextField.setText(value);
          novaDuracaoTextField.selectPositionCaret(value.length());
        }
      }
    });

    VBox vbox = new VBox(10, duracaoAtualLabel, duracaoTextField, novaDuracaoLabel, novaDuracaoTextField);
    dialog.getDialogPane().setContent(vbox);

    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

    Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
    okButton.setOnAction(actionEvent -> {
      String novaDuracao = novaDuracaoTextField.getText();

      if (novaDuracao.isBlank()) {
        AlertUtils.showValidateAlert("O campo nova duração deve ser preenchido.");
        dialog.close();
      }

      evento.setDuracao(LocalTime.parse(novaDuracao));

      try {
        eventoService.update(evento.getId(), evento);
      }
      catch (ServiceException e) {
        AlertUtils.showValidateAlert(e.getMessage());
        dialog.close();
      }
      catch (Exception e) {
        AlertUtils.showErrorAlert("Ocorreu um erro durante o processamento.");
        dialog.close();
      }

      AlertUtils.showSuccessAlert("Evento atualizado com sucesso. Uma notificação será enviada aos participantes.");

      cardsContent.getChildren().clear();
      fillPage();

      dialog.close();
    });

    dialog.setOnCloseRequest(e -> {
      dialog.close();
    });

    dialog.show();
  }

}