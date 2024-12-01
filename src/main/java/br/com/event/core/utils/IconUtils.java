package br.com.event.core.utils;

import br.com.event.core.EventCoreApplication;
import br.com.event.core.controllers.HomeController;
import br.com.event.core.exceptions.ServiceException;
import java.net.URL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconUtils {

  public static ImageView getIcon(String path, double width, double height) throws ServiceException {
    URL resource = HomeController.class.getResource(path);

    if (resource != null) {
      Image image = new Image(resource.toExternalForm());
      ImageView icon = new ImageView(image);
      icon.setFitHeight(height);
      icon.setFitWidth(width);

      return icon;
    }

    throw new ServiceException("O recurso informado não foi encontrado.");
  }

}
