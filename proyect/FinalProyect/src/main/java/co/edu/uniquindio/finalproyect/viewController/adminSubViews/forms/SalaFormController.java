package co.edu.uniquindio.finalproyect.viewController.adminSubViews.forms;

import co.edu.uniquindio.finalproyect.model.Sala;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.TipoSala;
import co.edu.uniquindio.finalproyect.viewController.adminSubViews.AdminSalasMainController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SalaFormController {

    @FXML private Label lblTituloFormSala;
    @FXML private TextField txtNumeroSala;
    @FXML private ComboBox<TipoSala> cbTipoSala;
    @FXML private TextField txtCapacidadSala;
    @FXML private CheckBox checkEstaDisponible;
    @FXML private Button btnGuardarSala;
    @FXML private Button btnCancelarSala;

    private Stage dialogStage;
    private SistemaHospitalario sistemaHospitalario;
    private Sala salaOriginal;
    private AdminSalasMainController adminSalasMainController;
    private boolean esNueva = true;

    @FXML
    void initialize() {
        cbTipoSala.setItems(FXCollections.observableArrayList(TipoSala.values()));
    }

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }
    public void setAdminSalasMainController(AdminSalasMainController controller) { this.adminSalasMainController = controller; }

    public void setSalaParaEdicion(Sala sala) {
        this.salaOriginal = sala;
        if (sala != null) {
            esNueva = false;
            lblTituloFormSala.setText("Actualizar Sala");
            txtNumeroSala.setText(sala.getNumeroSala());
            cbTipoSala.setValue(sala.getTipoSala());
            txtCapacidadSala.setText(String.valueOf(sala.getCapacidad()));
            checkEstaDisponible.setSelected(sala.isEstaDisponible());
        } else {
            esNueva = true;
            lblTituloFormSala.setText("Registrar Nueva Sala");
            txtNumeroSala.clear();
            cbTipoSala.getSelectionModel().clearSelection();
            txtCapacidadSala.clear();
            checkEstaDisponible.setSelected(true);
        }
    }

    @FXML
    void handleGuardarSala(ActionEvent event) {
        if (validarCamposSala()) {
            String numeroSala = txtNumeroSala.getText().trim();
            TipoSala tipoSala = cbTipoSala.getValue();
            int capacidad = Integer.parseInt(txtCapacidadSala.getText().trim());
            boolean disponible = checkEstaDisponible.isSelected();

            Sala salaEditada;
            if (esNueva) {
                salaEditada = new Sala(numeroSala, tipoSala, capacidad);
                salaEditada.setEstaDisponible(disponible);
            } else {
                salaEditada = new Sala(salaOriginal.getIdSala(), numeroSala, tipoSala, capacidad, disponible);
            }

            boolean resultado;
            if (esNueva) {
                resultado = sistemaHospitalario.agregarSala(salaEditada);
            } else {
                resultado = sistemaHospitalario.actualizarSala(salaEditada);
            }

            if (resultado) {
                mostrarAlerta("Éxito", "Operación Exitosa", "La sala ha sido " + (esNueva ? "registrada" : "actualizada") + " correctamente.", Alert.AlertType.INFORMATION);
                if (adminSalasMainController != null) {
                    adminSalasMainController.refrescarTablaSalas();
                }
                dialogStage.close();
            } else {
                mostrarAlerta("Error", "Operación Fallida", "No se pudo " + (esNueva ? "registrar" : "actualizar") + " la sala. Verifique si el número de sala ya existe o si el ID es incorrecto para la actualización.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleCancelarSala(ActionEvent event) {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    private boolean validarCamposSala() {
        String mensajeError = "";
        if (txtNumeroSala.getText() == null || txtNumeroSala.getText().trim().isEmpty()) {
            mensajeError += "El número de sala es obligatorio.\n";
        }
        if (cbTipoSala.getValue() == null) {
            mensajeError += "El tipo de sala es obligatorio.\n";
        }
        if (txtCapacidadSala.getText() == null || txtCapacidadSala.getText().trim().isEmpty()) {
            mensajeError += "La capacidad es obligatoria.\n";
        } else {
            try {
                int capacidad = Integer.parseInt(txtCapacidadSala.getText().trim());
                if (capacidad <= 0) {
                    mensajeError += "La capacidad debe ser un número positivo.\n";
                }
            } catch (NumberFormatException e) {
                mensajeError += "La capacidad debe ser un número válido.\n";
            }
        }

        if (mensajeError.isEmpty()) {
            return true;
        } else {
            mostrarAlerta("Campos Inválidos", "Por favor, corrija los siguientes errores:", mensajeError, Alert.AlertType.ERROR);
            return false;
        }
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.initOwner(dialogStage);
        alert.showAndWait();
    }
}