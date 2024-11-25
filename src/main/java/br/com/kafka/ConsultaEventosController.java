package br.com.kafka;

import br.com.kafka.entities.Evento;
import br.com.kafka.services.EventoService;
import br.com.kafka.utils.AlertUtils;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class ConsultaEventosController implements Initializable {

  @FXML
  private TableView<Evento> eventosTableView;

  @Autowired
  private EventoService eventoService;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    createTableColumns();
    fillTableView();
  }

  private void createTableColumns() {
    TableColumn<Evento, Long> colunaId = createIdColumn();
    TableColumn<Evento, String> colunaNome = createNomeColumn();
    TableColumn<Evento, String> colunaData = createDataColumn();
    TableColumn<Evento, String> colunaDuracao = createDuracaoColumn();
    TableColumn<Evento, String> colunaStatus = createStatusColumn();
    TableColumn<Evento, Object> colunaOpcao = createOptionColumn();

    this.eventosTableView.getColumns().add(colunaId);
    this.eventosTableView.getColumns().add(colunaNome);
    this.eventosTableView.getColumns().add(colunaData);
    this.eventosTableView.getColumns().add(colunaDuracao);
    this.eventosTableView.getColumns().add(colunaStatus);
    this.eventosTableView.getColumns().add(colunaOpcao);
  }


  private TableColumn<Evento, Long> createIdColumn() {
    TableColumn<Evento, Long> colunaId = new TableColumn<>("ID");
    colunaId.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getId()).asObject());
    colunaId.setStyle("-fx-alignment: CENTER;");
    colunaId.setPrefWidth(63.0);

    return colunaId;
  }

  private TableColumn<Evento, String> createNomeColumn() {
    TableColumn<Evento, String> colunaNome = new TableColumn<>("Nome");
    colunaNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
    colunaNome.setStyle("-fx-alignment: CENTER;");
    colunaNome.setPrefWidth(315.0);

    return colunaNome;
  }

  private TableColumn<Evento, String> createDataColumn() {
    TableColumn<Evento, String> colunaData = new TableColumn<>("Data");
    colunaData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getData().toString()));
    colunaData.setStyle("-fx-alignment: CENTER;");
    colunaData.setPrefWidth(200.0);

    return colunaData;
  }

  private TableColumn<Evento, String> createDuracaoColumn() {
    TableColumn<Evento, String> colunaDuracao = new TableColumn<>("Duração");
    colunaDuracao.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDuracao().toString()));
    colunaDuracao.setStyle("-fx-alignment: CENTER;");
    colunaDuracao.setPrefWidth(120.0);
    return colunaDuracao;
  }

  private TableColumn<Evento, String> createStatusColumn() {
    TableColumn<Evento, String> colunaTelefone = new TableColumn<>("Status");
    colunaTelefone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().getDescricao()));
    colunaTelefone.setStyle("-fx-alignment: CENTER;");
    colunaTelefone.setPrefWidth(140.0);
    return colunaTelefone;
  }

  private TableColumn<Evento, Object> createOptionColumn() {
    TableColumn<Evento, Object> colunaOpcao = new TableColumn<>("Opções");
    colunaOpcao.setCellFactory(data -> new TableCell<Evento, Object>() {
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
            Evento usuario = getTableRow().getItem();

            if (usuario != null) {
              Alert alert = AlertUtils.createDeleteAlert(usuario.getNome());

              alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                  eventoService.delete(usuario.getId());
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
    List<Evento> usuarios = eventoService.findAll();
    ObservableList<Evento> observableList = FXCollections.observableArrayList(usuarios);
    this.eventosTableView.getItems().setAll(observableList);
  }

}