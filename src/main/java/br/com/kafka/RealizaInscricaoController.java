package br.com.kafka;

import br.com.kafka.entities.Evento;
import br.com.kafka.entities.Usuario;
import br.com.kafka.services.EventoService;
import br.com.kafka.services.UsuarioService;
import br.com.kafka.utils.AlertUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RealizaInscricaoController implements Initializable {

  @FXML
  private ComboBox<String> eventosComboBox;

  @FXML
  private ListView<String> usuariosSemInscricao;

  @FXML
  private ListView<String> usuariosInscritos;

  @Autowired
  private EventoService eventoService;

  @Autowired
  private UsuarioService usuarioService;

  private List<Evento> eventos;

  private List<Usuario> usuarios;

  private ObservableList<String> observableUsuariosInscritos;

  private ObservableList<String> observableUsuariosSemInscricao;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    observableUsuariosInscritos = FXCollections.observableArrayList();
    observableUsuariosSemInscricao = FXCollections.observableArrayList();

    eventos = eventoService.buscarEventosAgendados();
    usuarios = usuarioService.buscarTodosUsuarios();

    List<String> nomesEventos = eventos.stream().map(Evento::getNome).toList();
    eventosComboBox.setItems(FXCollections.observableArrayList(nomesEventos));

    eventosComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
      observableUsuariosInscritos = FXCollections.observableArrayList();
      observableUsuariosSemInscricao = FXCollections.observableArrayList();

      List<String> participantes = new ArrayList<>();

      Evento optEvento = eventos.stream()
        .filter(e -> e.getNome().equalsIgnoreCase(newValue))
        .findAny()
        .orElse(null);

      if (optEvento != null) {
        Evento evento = eventoService.buscarEventoPorId(optEvento.getId());
        participantes.addAll(evento.getParticipantes().stream().map(Usuario::getNome).toList());
        observableUsuariosInscritos.setAll(participantes);
      }

      List<String> list = usuarios.stream()
        .filter(usuario -> !participantes.contains(usuario.getNome()))
        .toList()
        .stream()
        .map(Usuario::getNome)
        .toList();

      observableUsuariosSemInscricao.addAll(list);

      usuariosInscritos.setItems(observableUsuariosInscritos);
      usuariosSemInscricao.setItems(observableUsuariosSemInscricao);
    });

    usuariosSemInscricao.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    usuariosInscritos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  @FXML
  protected void addButtonAction(ActionEvent event) {
    ObservableList<String> usuarios = usuariosSemInscricao.getSelectionModel().getSelectedItems();
    if (usuarios != null && !usuarios.isEmpty()) {
      observableUsuariosInscritos.addAll(usuarios);
      observableUsuariosSemInscricao.removeAll(usuarios);
    }
  }

  @FXML
  protected void removeButtonAction(ActionEvent event) {
    ObservableList<String> usuarios = usuariosInscritos.getSelectionModel().getSelectedItems();
    if (usuarios != null && !usuarios.isEmpty()) {
      observableUsuariosSemInscricao.addAll(usuarios);
      observableUsuariosInscritos.removeAll(usuarios);
    }
  }

  @FXML
  protected void finalizarInscricoes(ActionEvent event) {

    if (eventosComboBox.getSelectionModel().getSelectedItem() == null) {
      AlertUtils.showValidateAlert("É necessário selecionar um evento para inscrever os usuários.");
      return;
    }

    String selectedItem = eventosComboBox.getSelectionModel().getSelectedItem();

    Evento evento = eventos.stream()
      .filter(e -> e.getNome().equalsIgnoreCase(selectedItem))
      .findAny()
      .orElse(null);

    List<String> inscritos = usuariosInscritos.getItems().stream().toList();

    for (String item : inscritos) {
      Usuario usuario = usuarioService.buscarUsuarioPorNome(item);
      eventoService.realizarInscricaoUsuario(usuario.getId(), evento.getId());
    }

    List<String> naoInscritos = usuariosSemInscricao.getItems().stream().toList();

    for (String item : naoInscritos) {
      Usuario usuarip = usuarioService.buscarUsuarioPorNome(item);
      eventoService.cancelarInscricaoUsuario(usuarip.getId(), evento.getId());
    }

    AlertUtils.showSuccessAlert("Os participantes selecionados tiveram suas inscrições atualizadas com sucesso.");
  }

}
