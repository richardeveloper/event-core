package br.com.kafka;

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

  private ConfigurableApplicationContext applicationContext;

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void init() {
    applicationContext = SpringApplication.run(EventCoreApplication.class);
  }

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(EventCoreApplication.class.getResource("/home-view.fxml"));
    fxmlLoader.setControllerFactory(applicationContext::getBean);
    Scene scene = new Scene(fxmlLoader.load(), 1240, 680);
    stage.setTitle("Event Core Application");
    stage.setScene(scene);
    stage.show();
  }

}
