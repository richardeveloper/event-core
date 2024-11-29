package br.com.event.core.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AlertUtils {

  public static void showValidateAlert(String message) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Aviso");
    alert.setHeaderText("Campo inválido");
    alert.setContentText(message);
    alert.setOnShown(event -> positionAlert(alert));

    alert.showAndWait();
  }

  public static void showWarningAlert(String message) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Aviso");
    alert.setHeaderText("Dados inconsistentes encontrados");
    alert.setContentText(message);
    alert.setOnShown(event -> positionAlert(alert));

    alert.showAndWait();
  }

  public static void showSuccessAlert(String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Sucesso");
    alert.setHeaderText("Operação finalizada");
    alert.setContentText(message);
    alert.setOnShown(event -> positionAlert(alert));

    alert.showAndWait();
  }

  public static void showErrorAlert(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Erro");
    alert.setHeaderText("Não foi possível completar a solicitação");
    alert.setContentText(message);
    alert.setOnShown(event -> positionAlert(alert));

    alert.showAndWait();
  }

  public static Alert showDeleteUserAlert(String name) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmação");
    alert.setHeaderText("Excluir usuário");
    alert.setContentText(String.format("Deseja realmente apagar o usuário %s ?", name));
    alert.setOnShown(event -> positionAlert(alert));

    return alert;
  }

  public static Alert showDeleteEventAlert(String name) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmação");
    alert.setHeaderText("Excluir evento");
    alert.setContentText(String.format("Deseja realmente apagar o evento %s ?", name));
    alert.setOnShown(event -> positionAlert(alert));

    return alert;
  }

  public static Alert showDeleteEventUserAlert(String name) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmação");
    alert.setHeaderText("Cancelar inscrição");
    alert.setContentText(String.format("Deseja realmente cancelar a inscrição no evento %s ?", name));
    alert.setOnShown(event -> positionAlert(alert));

    return alert;
  }

  public static void positionAlert(Alert alert) {
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.setX(890);
    stage.setY(430);
  }

  public static void positionDialog(Dialog<?> dialog) {
    Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
    stage.setX(950);
    stage.setY(390);
  }

}
