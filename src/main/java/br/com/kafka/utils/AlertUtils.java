package br.com.kafka.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtils {
  public static void showValidateAlert(String message) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Aviso");
    alert.setHeaderText("Campo inválido");
    alert.setContentText(message);

    alert.showAndWait();
  }

  public static void showSuccessAlert(String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Sucesso");
    alert.setHeaderText("Operação concluída");
    alert.setContentText(message);

    alert.showAndWait();
  }

  public static void showErrorAlert(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Sucesso");
    alert.setHeaderText("Ocorreu um erro durante o processamento");
    alert.setContentText(message);

    alert.showAndWait();
  }

  public static Alert createDeleteAlert(String name) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmação");
    alert.setHeaderText("Excluir registro");
    alert.setContentText(String.format("Deseja realmente registro o contato %s ?", name));

    return alert;
  }


}
