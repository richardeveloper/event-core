package br.com.event.core.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HomeController implements Initializable {

  @FXML
  private AnchorPane content;

  @FXML
  private ImageView inscricoesIcon;

  @FXML
  private ImageView usuariosIcon;

  @FXML
  private ImageView eventosIcon;

  @FXML
  private ImageView logsIcons;

  @FXML
  private Button proximosEventos;

  @FXML
  private Button realizaInscricao;

  @FXML
  private Button consultaInscricoes;

  @FXML
  private Button cadastroUsuario;

  @FXML
  private Button consultaUsuarios;

  @FXML
  private Button cadastroEvento;

  @FXML
  private Button consultaEventos;

  @FXML
  private Button notificacoesEnviadas;

  @Autowired
  private ConfigurableApplicationContext applicationContext;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    setupIcons();
  }

  @FXML
  protected void redirectProximosEventosPage() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
        "/pages/proximos-eventos-view.fxml"));
      fxmlLoader.setControllerFactory(applicationContext::getBean);

      AnchorPane novoContent = fxmlLoader.load();;
      disableAllButtons();
      proximosEventos.getStyleClass().add("menu-sub-item-on");

      content.getChildren().clear();
      content.getChildren().add(novoContent);
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @FXML
  protected void redirectRealizarInscricaoPage() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
        "/pages/realiza-inscricao-view.fxml"));
      fxmlLoader.setControllerFactory(applicationContext::getBean);

      AnchorPane novoContent = fxmlLoader.load();
      disableAllButtons();
      realizaInscricao.getStyleClass().add("menu-sub-item-on");

      content.getChildren().clear();
      content.getChildren().add(novoContent);
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @FXML
  protected void redirectConsultarInscricoesPage() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pages/consulta-inscricoes-view.fxml"));
      fxmlLoader.setControllerFactory(applicationContext::getBean);

      AnchorPane novoContent = fxmlLoader.load();
      disableAllButtons();
      consultaInscricoes.getStyleClass().add("menu-sub-item-on");

      content.getChildren().clear();
      content.getChildren().add(novoContent);
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @FXML
  protected void redirectCadastroUsuarioPage() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pages/cadastro-usuario-view.fxml"));
      fxmlLoader.setControllerFactory(applicationContext::getBean);

      AnchorPane novoContent = fxmlLoader.load();
      disableAllButtons();
      cadastroUsuario.getStyleClass().add("menu-sub-item-on");

      content.getChildren().clear();
      content.getChildren().add(novoContent);
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @FXML
  protected void redirectConsultaUsuariosPage() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pages/consulta-usuarios-view.fxml"));
      fxmlLoader.setControllerFactory(applicationContext::getBean);

      AnchorPane novoContent = fxmlLoader.load();
      disableAllButtons();
      consultaUsuarios.getStyleClass().add("menu-sub-item-on");

      content.getChildren().clear();
      content.getChildren().add(novoContent);
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @FXML
  protected void redirectCadastroEventoPage() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pages/cadastro-evento-view.fxml"));
      fxmlLoader.setControllerFactory(applicationContext::getBean);

      AnchorPane novoContent = fxmlLoader.load();
      disableAllButtons();
      cadastroEvento.getStyleClass().add("menu-sub-item-on");

      content.getChildren().clear();
      content.getChildren().add(novoContent);
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @FXML
  protected void redirectConsultaEventosPage() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pages/consulta-eventos-view.fxml"));
      fxmlLoader.setControllerFactory(applicationContext::getBean);

      AnchorPane novoContent = fxmlLoader.load();
      disableAllButtons();
      consultaEventos.getStyleClass().add("menu-sub-item-on");

      content.getChildren().clear();
      content.getChildren().add(novoContent);
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @FXML
  protected void redirectNotificacoesEnviadasPage() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pages/notificacoes-enviadas-view.fxml"));
      fxmlLoader.setControllerFactory(applicationContext::getBean);

      AnchorPane novoContent = fxmlLoader.load();
      disableAllButtons();
      notificacoesEnviadas.getStyleClass().add("menu-sub-item-on");

      content.getChildren().clear();
      content.getChildren().add(novoContent);
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private void setupIcons() {
    Image calendar = new Image(getClass().getResource("/icons/subscribe.png").toExternalForm());
    inscricoesIcon.setImage(calendar);

    Image user = new Image(getClass().getResource("/icons/user.png").toExternalForm());
    usuariosIcon.setImage(user);

    Image event = new Image(getClass().getResource("/icons/event.png").toExternalForm());
    eventosIcon.setImage(event);

    Image logs = new Image(getClass().getResource("/icons/dashboard.png").toExternalForm());
    logsIcons.setImage(logs);
  }

  private void disableAllButtons() {
    List<Button> itemsMenu = List.of(
      proximosEventos,
      realizaInscricao,
      consultaInscricoes,
      cadastroUsuario,
      consultaUsuarios,
      cadastroEvento,
      consultaEventos,
      notificacoesEnviadas
    );

    itemsMenu.forEach(item -> item.getStyleClass().remove("menu-sub-item-on"));
  }

}