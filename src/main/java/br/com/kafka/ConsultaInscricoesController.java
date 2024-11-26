package br.com.kafka;

import br.com.kafka.entities.Evento;
import br.com.kafka.entities.Usuario;
import br.com.kafka.services.EventoService;
import br.com.kafka.services.UsuarioService;
import br.com.kafka.utils.AlertUtils;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsultaInscricoesController implements Initializable {

  @FXML
  private TableView<Evento> eventosTableView;

  @FXML
  private TextField nomeTextField;

  @FXML
  private ComboBox<String> usuariosComboBox;

  @FXML
  private ListView<String> usuariosListView;

  @Autowired
  private EventoService eventoService;

  @Autowired
  private UsuarioService usuarioService;

  private List<Usuario> usuarios = new ArrayList<>();

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    createTableColumns();
    usuariosListView.setPlaceholder(new Label("Nenhum usuário encontrado"));

    nomeTextField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        if (newValue != null && !newValue.isEmpty()) {
          usuarios = usuarioService.buscarUsuarioPorNomeLike(newValue);

          List<String> nomesUsuarios = usuarios
            .stream()
            .map(Usuario::getNome)
            .toList();

          if (!usuarios.isEmpty()) {
            usuariosListView.setItems(FXCollections.observableArrayList(nomesUsuarios));
            eventosTableView.setPlaceholder(new Label("Não há conteúdo na tabela."));
            return;
          }
        }

        usuariosListView.setItems(FXCollections.observableArrayList());
        usuariosListView.setPlaceholder(new Label("Nenhum usuário encontrado"));
      }
    });

    usuariosListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
        if (newValue != null && !newValue.isEmpty()) {
          Usuario u = usuarios.stream()
            .filter(usuario -> usuario.getNome().equalsIgnoreCase(newValue))
            .findAny()
            .orElse(null);

          List<Evento> eventos = eventoService.buscarEventosUsuario(u.getId());

          if (eventos.isEmpty()) {
            eventosTableView.getItems().setAll(FXCollections.observableArrayList());
            eventosTableView.setPlaceholder(new Label("%s não está inscrito em nenhum evento.".formatted(u.getNome())));
            return;
          }

          ObservableList<Evento> observableList = FXCollections.observableArrayList(eventos);
          eventosTableView.getItems().setAll(observableList);
        }
        else {
          eventosTableView.getItems().setAll(FXCollections.observableArrayList());
          eventosTableView.setPlaceholder(new Label("Não há conteúdo na tabela."));
        }
      }
    });
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
    colunaId.setPrefWidth(60.0);

    return colunaId;
  }

  private TableColumn<Evento, String> createNomeColumn() {
    TableColumn<Evento, String> colunaNome = new TableColumn<>("Nome");
    colunaNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
    colunaNome.setStyle("-fx-alignment: CENTER;");
    colunaNome.setPrefWidth(350.0);

    return colunaNome;
  }

  private TableColumn<Evento, String> createDataColumn() {
    TableColumn<Evento, String> colunaData = new TableColumn<>("Data");
    colunaData.setCellValueFactory(data ->
      new SimpleStringProperty(data.getValue().getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
    colunaData.setStyle("-fx-alignment: CENTER;");
    colunaData.setPrefWidth(200.0);

    return colunaData;
  }

  private TableColumn<Evento, String> createDuracaoColumn() {
    TableColumn<Evento, String> colunaDuracao = new TableColumn<>("Duração");
    colunaDuracao.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDuracao().toString()));
    colunaDuracao.setStyle("-fx-alignment: CENTER;");
    colunaDuracao.setPrefWidth(140.0);
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
          Image image = new Image(getClass().getResource("/icons/close.gif").toExternalForm());
          imageView.setImage(image);

          setOnMouseClicked(event -> {
            Evento evento = getTableRow().getItem();

            if (evento != null) {
              Alert alert = AlertUtils.createDeleteEventAlert(evento.getNome());

              alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                  eventoService.apagarEvento(evento.getId());
//                  fillTableView();
                }
              });
            }
          });

          setGraphic(imageView);
        }
      }
    });

    colunaOpcao.setPrefWidth(80.0);
    colunaOpcao.setStyle("-fx-alignment: CENTER;");

    return colunaOpcao;
  }

}