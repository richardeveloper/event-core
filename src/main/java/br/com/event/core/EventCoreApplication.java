package br.com.event.core;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class EventCoreApplication extends Application {

  private static final String TITLE = "Event Core Application";
  private static final double WIDTH_SCREEN = 1580.0;
  private static final double HEIGHT_SCREEN = 820.0;

  private static ConfigurableApplicationContext applicationContext;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void init() {
    applicationContext = SpringApplication.run(EventCoreApplication.class);
  }

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(EventCoreApplication.class.getResource("/pages/home-view.fxml"));
    fxmlLoader.setControllerFactory(applicationContext::getBean);

    Scene scene = new Scene(fxmlLoader.load(), WIDTH_SCREEN, HEIGHT_SCREEN);
    stage.setTitle(TITLE);
    stage.setScene(scene);
    stage.show();
  }

}
