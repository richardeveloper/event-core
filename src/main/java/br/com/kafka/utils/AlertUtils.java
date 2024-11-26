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
    alert.setHeaderText("Operação finalizada");
    alert.setContentText(message);

    alert.showAndWait();
  }

  public static void showErrorAlert(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Erro");
    alert.setHeaderText("Algo inesperado aconteceu");
    alert.setContentText(message);

    alert.showAndWait();
  }

  public static Alert createDeleteUserAlert(String name) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmação");
    alert.setHeaderText("Excluir usuário");
    alert.setContentText(String.format("Deseja realmente apagar o usuário %s ?", name));

    return alert;
  }

  public static Alert createDeleteEventAlert(String name) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmação");
    alert.setHeaderText("Excluir evento");
    alert.setContentText(String.format("Deseja realmente registro o evento %s ?", name));

    return alert;
  }


}
