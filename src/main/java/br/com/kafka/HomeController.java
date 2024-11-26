package br.com.kafka;

import java.net.URL;
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
    Image calendar = new Image(getClass().getResource("/icons/subscribe.png").toExternalForm());
    inscricoesIcon.setImage(calendar);

    Image user = new Image(getClass().getResource("/icons/user.png").toExternalForm());
    usuariosIcon.setImage(user);

    Image event = new Image(getClass().getResource("/icons/event.png").toExternalForm());
    eventosIcon.setImage(event);

    Image logs = new Image(getClass().getResource("/icons/dashboard.png").toExternalForm());
    logsIcons.setImage(logs);
  }

  @FXML
  protected void redirectProximosEventosPage() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/proximos-eventos-view.fxml"));
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
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/realiza-inscricao-view.fxml"));
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
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/consulta-inscricoes-view.fxml"));
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
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cadastro-usuario-view.fxml"));
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
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/consulta-usuarios-view.fxml"));
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
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cadastro-evento-view.fxml"));
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
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/consulta-eventos-view.fxml"));
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

  private void disableAllButtons() {
    proximosEventos.getStyleClass().remove("menu-sub-item-on");
    proximosEventos.getStyleClass().remove("menu-sub-item-on");
    realizaInscricao.getStyleClass().remove("menu-sub-item-on");
    consultaInscricoes.getStyleClass().remove("menu-sub-item-on");
    cadastroUsuario.getStyleClass().remove("menu-sub-item-on");
    consultaUsuarios.getStyleClass().remove("menu-sub-item-on");
    cadastroEvento.getStyleClass().remove("menu-sub-item-on");
    consultaEventos.getStyleClass().remove("menu-sub-item-on");
    notificacoesEnviadas.getStyleClass().remove("menu-sub-item-on");
  }

}