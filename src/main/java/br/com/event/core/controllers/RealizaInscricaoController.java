package br.com.event.core.controllers;

import br.com.event.core.entities.Evento;
import br.com.event.core.entities.EventosUsuario;
import br.com.event.core.entities.Usuario;
import br.com.event.core.exceptions.ServiceException;
import br.com.event.core.services.EventoService;
import br.com.event.core.services.EventosUsuarioService;
import br.com.event.core.services.UsuarioService;
import br.com.event.core.utils.AlertUtils;
import br.com.event.core.utils.IconUtils;
import br.com.event.core.utils.MaskUtils;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
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

  @FXML
  private Button saveButton;

  @Autowired
  private EventoService eventoService;

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private EventosUsuarioService eventosUsuarioService;

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

    List<String> nomesEventos = eventos.stream()
      .map(MaskUtils::applyInfoEventMask)
      .toList();

    eventosComboBox.setItems(FXCollections.observableArrayList(nomesEventos));
    eventosComboBox.valueProperty().addListener(this::eventosComboBoxAction);

    usuariosSemInscricao.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    usuariosInscritos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    configSaveButton();
  }

  @FXML
  protected void addButtonAction() {
    ObservableList<String> usuarios = usuariosSemInscricao.getSelectionModel().getSelectedItems();
    if (usuarios != null && !usuarios.isEmpty()) {
      observableUsuariosInscritos.addAll(usuarios);
      observableUsuariosSemInscricao.removeAll(usuarios);
    }
  }

  @FXML
  protected void removeButtonAction() {
    ObservableList<String> usuarios = usuariosInscritos.getSelectionModel().getSelectedItems();
    if (usuarios != null && !usuarios.isEmpty()) {
      observableUsuariosSemInscricao.addAll(usuarios);
      observableUsuariosInscritos.removeAll(usuarios);
    }
  }

  @FXML
  protected void finalizarInscricoes() {
    if (eventosComboBox.getSelectionModel().getSelectedItem() == null) {
      AlertUtils.showValidateAlert("É necessário selecionar um evento para inscrever os usuários.");
      return;
    }

    String selectedItem = eventosComboBox.getSelectionModel().getSelectedItem();

    Optional<Evento> eventoEncontrado = eventos.stream()
      .filter(e -> e.getNome().equalsIgnoreCase(MaskUtils.removeInfoEventMask(selectedItem)))
      .findAny();

    if (eventoEncontrado.isPresent()) {
      Evento evento = eventoEncontrado.get();

      List<String> inscritos = usuariosInscritos.getItems().stream().toList();

      for (String item : inscritos) {
        item = MaskUtils.removeInfoUserMask(item);
        Usuario usuario = usuarioService.buscarUsuarioPorNome(item);

        try {
          eventosUsuarioService.realizarInscricao(usuario.getId(), evento.getId());
        }
        catch (ServiceException e) {
          AlertUtils.showWarningAlert(e.getMessage());
          return;
        }
        catch (Exception e) {
          log.error(e.getMessage(), e);
          AlertUtils.showErrorAlert("Ocorreu um erro durante o processamento.");
          return;
        }
      }

      List<String> naoInscritos = usuariosSemInscricao.getItems().stream().toList();

      for (String item : naoInscritos) {
        item = MaskUtils.removeInfoUserMask(item);
        Usuario usuario = usuarioService.buscarUsuarioPorNome(item);

        try {
          eventosUsuarioService.cancelarInscricao(usuario.getId(), evento.getId());
        }
        catch (ServiceException e) {
          AlertUtils.showWarningAlert(e.getMessage());
          return;
        }
        catch (Exception e) {
          log.error(e.getMessage(), e);
          AlertUtils.showErrorAlert("Ocorreu um erro durante o processamento.");
          return;
        }
      }

      usuariosSemInscricao.getItems().clear();
      usuariosInscritos.getItems().clear();

      eventosComboBox.getSelectionModel().clearSelection();
      eventosComboBox.setPromptText("Selecione o evento");

      AlertUtils.showSuccessAlert("Inscrições atualizadas com sucesso.");
    }
  }

  private void eventosComboBoxAction(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    observableUsuariosInscritos = FXCollections.observableArrayList();
    observableUsuariosSemInscricao = FXCollections.observableArrayList();

    Optional<Evento> eventoEncontrado = eventos.stream()
      .filter(e -> e.getNome().equalsIgnoreCase(MaskUtils.removeInfoEventMask(newValue)))
      .findAny();

    if (eventoEncontrado.isPresent()) {
      Evento e = eventoEncontrado.get();

      List<EventosUsuario> eventosUsuarios = eventosUsuarioService.buscarTodosEventosUsuarioPorEventoId(
        e.getId());

      List<String> participantes = eventosUsuarios.stream()
        .map(EventosUsuario::getUsuario)
        .toList()
        .stream()
        .map(MaskUtils::applyInfoUserMask)
        .toList();

      observableUsuariosInscritos.setAll(participantes);

      List<String> nomesParticipantes = usuarios.stream()
        .filter(usuario -> !participantes.contains(MaskUtils.applyInfoUserMask(usuario)))
        .toList()
        .stream()
        .map(MaskUtils::applyInfoUserMask)
        .toList();

      observableUsuariosSemInscricao.addAll(nomesParticipantes);

      usuariosInscritos.setItems(observableUsuariosInscritos);
      usuariosSemInscricao.setItems(observableUsuariosSemInscricao);
    }
  }

  private void configSaveButton() {
    saveButton.getStyleClass().add("edit-button");

    ImageView icon = IconUtils.getIcon("/icons/save.png", 25, 25);

    saveButton.setGraphic(icon);
    saveButton.setGraphicTextGap(7.5);
    saveButton.setContentDisplay(ContentDisplay.RIGHT);
  }
}
