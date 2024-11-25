package br.com.kafka;

import br.com.kafka.entities.Usuario;
import br.com.kafka.services.UsuarioService;
import br.com.kafka.utils.AlertUtils;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsultaUsuariosController implements Initializable {

  @FXML
  private TableView<Usuario> usuariosTableView;

  @Autowired
  private UsuarioService usuarioService;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    createTableColumns();
    fillTableView();
  }

  private void createTableColumns() {
    TableColumn<Usuario, Long> colunaId = createIdColumn();
    TableColumn<Usuario, String> colunaNome = createNameColumn();
    TableColumn<Usuario, String> colunaCpf = createCpfColumn();
    TableColumn<Usuario, String> colunaEmail = createEmailColumn();
    TableColumn<Usuario, String> colunaTelefone = createPhoneColumn();
    TableColumn<Usuario, Object> colunaOpcao = createOptionColumn();

    this.usuariosTableView.getColumns().add(colunaId);
    this.usuariosTableView.getColumns().add(colunaNome);
    this.usuariosTableView.getColumns().add(colunaCpf);
    this.usuariosTableView.getColumns().add(colunaEmail);
    this.usuariosTableView.getColumns().add(colunaTelefone);
    this.usuariosTableView.getColumns().add(colunaOpcao);
  }

  private TableColumn<Usuario, Long> createIdColumn() {
    TableColumn<Usuario, Long> colunaId = new TableColumn<>("ID");
    colunaId.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getId()).asObject());
    colunaId.setStyle("-fx-alignment: CENTER;");
    colunaId.setPrefWidth(63.0);

    return colunaId;
  }

  private TableColumn<Usuario, String> createNameColumn() {
    TableColumn<Usuario, String> colunaNome = new TableColumn<>("Nome");
    colunaNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
    colunaNome.setStyle("-fx-alignment: CENTER;");
    colunaNome.setPrefWidth(180.0);

    return colunaNome;
  }

  private TableColumn<Usuario, String> createCpfColumn() {
    TableColumn<Usuario, String> colunaCpf = new TableColumn<>("CPF");
    colunaCpf.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCpf()));
    colunaCpf.setStyle("-fx-alignment: CENTER;");
    colunaCpf.setPrefWidth(180.0);

    return colunaCpf;
  }

  private TableColumn<Usuario, String> createEmailColumn() {
    TableColumn<Usuario, String> colunaEmail = new TableColumn<>("Email");
    colunaEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
    colunaEmail.setStyle("-fx-alignment: CENTER;");
    colunaEmail.setPrefWidth(280.0);
    return colunaEmail;
  }

  private TableColumn<Usuario, String> createPhoneColumn() {
    TableColumn<Usuario, String> colunaTelefone = new TableColumn<>("Telefone");
    colunaTelefone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefone()));
    colunaTelefone.setStyle("-fx-alignment: CENTER;");
    colunaTelefone.setPrefWidth(135.0);
    return colunaTelefone;
  }

  private TableColumn<Usuario, Object> createOptionColumn() {
    TableColumn<Usuario, Object> colunaOpcao = new TableColumn<>("Opções");
    colunaOpcao.setCellFactory(data -> new TableCell<Usuario, Object>() {
      private final ImageView imageView = new ImageView();

      @Override
      protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
        }
        else {
          Image image = new Image(getClass().getResource("/icons/delete.gif").toExternalForm());
          imageView.setImage(image);

          setOnMouseClicked(event -> {
            Usuario usuario = getTableRow().getItem();

            if (usuario != null) {
              Alert alert = AlertUtils.createDeleteAlert(usuario.getNome());

              alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                  usuarioService.delete(usuario.getId());
                  fillTableView();
                }
              });
            }
          });

          setGraphic(imageView);
        }
      }
    });

    colunaOpcao.setPrefWidth(63.0);
    colunaOpcao.setStyle("-fx-alignment: CENTER;");

    return colunaOpcao;
  }

  private void fillTableView() {
    List<Usuario> usuarios = usuarioService.findAll();
    ObservableList<Usuario> observableList = FXCollections.observableArrayList(usuarios);
    this.usuariosTableView.getItems().setAll(observableList);
  }

}