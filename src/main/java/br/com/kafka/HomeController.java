package br.com.kafka;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HomeController {

  @FXML
  private AnchorPane content;

  @Autowired
  private ConfigurableApplicationContext applicationContext;

  @FXML
  protected void redirectProximosEventosPage() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/proximos-eventos-view.fxml"));
      fxmlLoader.setControllerFactory(applicationContext::getBean);

      AnchorPane novoContent = fxmlLoader.load();
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
      content.getChildren().clear();
      content.getChildren().add(novoContent);
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}