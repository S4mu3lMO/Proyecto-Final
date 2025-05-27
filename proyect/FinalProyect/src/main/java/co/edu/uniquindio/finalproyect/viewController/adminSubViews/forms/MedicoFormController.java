package co.edu.uniquindio.finalproyect.viewController.adminSubViews.forms;

import co.edu.uniquindio.finalproyect.model.CitaMedica;
import co.edu.uniquindio.finalproyect.model.Medico;
import co.edu.uniquindio.finalproyect.model.Sexo;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.TipoUsuario;
import co.edu.uniquindio.finalproyect.viewController.adminSubViews.AdminMedicosMainController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.LinkedList;

public class MedicoFormController {

    @FXML private Label lblTituloFormMedico;
    @FXML private TextField txtCedulaMedico;
    @FXML private TextField txtNombreMedico;
    @FXML private TextField txtEdadMedico;
    @FXML private ComboBox<Sexo> cbSexoMedico;
    @FXML private TextField txtUsuarioMedico;
    @FXML private PasswordField txtContrasenaMedico;
    @FXML private TextField txtEspecialidadMedico;
    @FXML private TextField txtLicenciaMedico;
    @FXML private Button btnGuardarMedico;
    @FXML private Button btnCancelarMedico;

    private Stage dialogStage;
    private SistemaHospitalario sistemaHospitalario;
    private Medico medicoOriginal;
    private AdminMedicosMainController adminMedicosMainController;
    private boolean esNuevo = true;

    @FXML
    void initialize() {
        cbSexoMedico.setItems(FXCollections.observableArrayList(Sexo.values())); //
    }

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }
    public void setAdminMedicosMainController(AdminMedicosMainController controller) { this.adminMedicosMainController = controller; }

    public void setMedicoParaEdicion(Medico medico) {
        this.medicoOriginal = medico;
        if (medico != null) {
            esNuevo = false;
            lblTituloFormMedico.setText("Actualizar Médico");
            txtCedulaMedico.setText(medico.getCedula());
            txtCedulaMedico.setEditable(false);
            txtNombreMedico.setText(medico.getNombre());
            txtEdadMedico.setText(String.valueOf(medico.getEdad()));
            cbSexoMedico.setValue(medico.getSexo());
            txtUsuarioMedico.setText(medico.getNombreUsuario());
            txtEspecialidadMedico.setText(medico.getEspecialidad());
            txtLicenciaMedico.setText(medico.getNumeroLicenciaMedica());
        } else {
            esNuevo = true;
            lblTituloFormMedico.setText("Registrar Nuevo Médico");
            txtCedulaMedico.setEditable(true);
        }
    }

    @FXML
    void handleGuardarMedico(ActionEvent event) {
        if (validarCamposMedico()) {
            String cedula = txtCedulaMedico.getText();
            String nombre = txtNombreMedico.getText();
            int edad = Integer.parseInt(txtEdadMedico.getText());
            Sexo sexo = cbSexoMedico.getValue();
            String nombreUsuario = txtUsuarioMedico.getText();
            String contrasena = txtContrasenaMedico.getText();
            String especialidad = txtEspecialidadMedico.getText();
            String numLicencia = txtLicenciaMedico.getText();

            if (esNuevo && (contrasena == null || contrasena.trim().isEmpty())) {
                mostrarAlerta("Error de Validación", "Contraseña Requerida", "La contraseña es obligatoria para nuevos médicos.", Alert.AlertType.ERROR);
                return;
            }

            Medico medicoEditado = new Medico(nombre, cedula, sexo, edad, nombreUsuario, contrasena, TipoUsuario.MEDICO, especialidad, numLicencia, new LinkedList<CitaMedica>());

            if (!esNuevo && (contrasena == null || contrasena.trim().isEmpty()) && medicoOriginal != null) {
                medicoEditado.setContrasena(medicoOriginal.getContrasena());
            }

            boolean resultado;
            if (esNuevo) {
                resultado = sistemaHospitalario.registrarMedico(medicoEditado); //
            } else {
                if(medicoOriginal != null) {
                    medicoEditado.setListCitasMedicas(medicoOriginal.getListCitasMedicas());
                    medicoEditado.setListHorariosDisponibilidad(medicoOriginal.getListHorariosDisponibilidad());
                }
                resultado = sistemaHospitalario.actualizarMedico(medicoEditado); //
            }

            if (resultado) {
                mostrarAlerta("Éxito", "Operación Exitosa", "El médico ha sido " + (esNuevo ? "registrado" : "actualizado") + " correctamente.", Alert.AlertType.INFORMATION);
                if (adminMedicosMainController != null) {
                    adminMedicosMainController.refrescarTablaMedicos();
                }
                dialogStage.close();
            } else {
                mostrarAlerta("Error", "Operación Fallida", "No se pudo " + (esNuevo ? "registrar" : "actualizar") + " el médico. Verifique si la cédula o usuario ya existen.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleCancelarMedico(ActionEvent event) {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    private boolean validarCamposMedico() {
        String mensajeError = "";
        if (txtCedulaMedico.getText() == null || txtCedulaMedico.getText().trim().isEmpty()) mensajeError += "La cédula es obligatoria.\n";
        if (txtNombreMedico.getText() == null || txtNombreMedico.getText().trim().isEmpty()) mensajeError += "El nombre es obligatorio.\n";
        if (txtEdadMedico.getText() == null || txtEdadMedico.getText().trim().isEmpty()) {
            mensajeError += "La edad es obligatoria.\n";
        } else {
            try {
                int edad = Integer.parseInt(txtEdadMedico.getText().trim());
                if (edad <= 0) mensajeError += "La edad debe ser un número positivo.\n";
            } catch (NumberFormatException e) {
                mensajeError += "La edad debe ser un número válido.\n";
            }
        }
        if (cbSexoMedico.getValue() == null) mensajeError += "El sexo es obligatorio.\n";
        if (txtUsuarioMedico.getText() == null || txtUsuarioMedico.getText().trim().isEmpty()) mensajeError += "El nombre de usuario es obligatorio.\n";
        if (esNuevo && (txtContrasenaMedico.getText() == null || txtContrasenaMedico.getText().trim().isEmpty())) {
            mensajeError += "La contraseña es obligatoria para nuevos médicos.\n";
        }
        if (txtEspecialidadMedico.getText() == null || txtEspecialidadMedico.getText().trim().isEmpty()) mensajeError += "La especialidad es obligatoria.\n";
        if (txtLicenciaMedico.getText() == null || txtLicenciaMedico.getText().trim().isEmpty()) mensajeError += "El número de licencia es obligatorio.\n";


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