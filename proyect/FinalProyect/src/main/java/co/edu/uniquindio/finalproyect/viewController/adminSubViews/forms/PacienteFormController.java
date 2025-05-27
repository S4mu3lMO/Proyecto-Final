package co.edu.uniquindio.finalproyect.viewController.adminSubViews.forms; // Nueva subcarpeta

import co.edu.uniquindio.finalproyect.model.HistorialMedico;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.Sexo;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.TipoUsuario;
import co.edu.uniquindio.finalproyect.viewController.adminSubViews.AdminPacientesMainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.LinkedList;

public class PacienteFormController {

    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEdad;
    @FXML private ComboBox<Sexo> cbSexo;
    @FXML private TextField txtNombreUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private TextField txtNumSeguroSocial;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private Stage dialogStage;
    private SistemaHospitalario sistemaHospitalario;
    private Paciente pacienteOriginal;
    private AdminPacientesMainController adminPacientesMainController;
    private boolean esNuevo = true;

    @FXML
    void initialize() {
        cbSexo.getItems().setAll(Sexo.values());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSistemaHospitalario(SistemaHospitalario sistema) {
        this.sistemaHospitalario = sistema;
    }

    public void setAdminPacientesMainController(AdminPacientesMainController controller) {
        this.adminPacientesMainController = controller;
    }

    public void setPacienteParaEdicion(Paciente paciente) {
        this.pacienteOriginal = paciente;
        if (paciente != null) {
            esNuevo = false;
            txtCedula.setText(paciente.getCedula());
            txtCedula.setEditable(false);
            txtNombre.setText(paciente.getNombre());
            txtEdad.setText(String.valueOf(paciente.getEdad()));
            cbSexo.setValue(paciente.getSexo());
            txtNombreUsuario.setText(paciente.getNombreUsuario());
            txtNumSeguroSocial.setText(paciente.getNumeroSeguroSocial());
        } else {
            esNuevo = true;
            txtCedula.setEditable(true);
        }
    }

    @FXML
    void handleGuardar(ActionEvent event) {
        if (validarCampos()) {
            String cedula = txtCedula.getText();
            String nombre = txtNombre.getText();
            int edad = 0;
            try {
                edad = Integer.parseInt(txtEdad.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Error de Formato", "Edad inválida", "La edad debe ser un número.", Alert.AlertType.ERROR);
                return;
            }
            Sexo sexo = cbSexo.getValue();
            String nombreUsuario = txtNombreUsuario.getText();
            String contrasena = txtContrasena.getText();
            String numSeguro = txtNumSeguroSocial.getText();

            if (esNuevo && contrasena.isEmpty()) {
                mostrarAlerta("Error de Validación", "Contraseña Requerida", "La contraseña es obligatoria para nuevos usuarios.", Alert.AlertType.ERROR);
                return;
            }


            Paciente pacienteEditado = new Paciente(nombre, cedula, sexo, edad, nombreUsuario, contrasena, TipoUsuario.PACIENTE, numSeguro, null);
            if (!esNuevo && contrasena.isEmpty() && pacienteOriginal != null) {
                pacienteEditado.setContrasena(pacienteOriginal.getContrasena());
            }


            boolean resultado;
            if (esNuevo) {
                resultado = sistemaHospitalario.registrarPaciente(pacienteEditado);
            } else {
                pacienteEditado.setHistorialMedico(pacienteOriginal.getHistorialMedico());
                resultado = sistemaHospitalario.actualizarPaciente(pacienteEditado);
            }

            if (resultado) {
                mostrarAlerta("Éxito", "Operación Exitosa", "El paciente ha sido " + (esNuevo ? "registrado" : "actualizado") + " correctamente.", Alert.AlertType.INFORMATION);
                if (adminPacientesMainController != null) {
                    adminPacientesMainController.refrescarTablaPacientes();
                }
                dialogStage.close();
            } else {
                mostrarAlerta("Error", "Operación Fallida", "No se pudo " + (esNuevo ? "registrar" : "actualizar") + " el paciente.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        dialogStage.close();
    }

    private boolean validarCampos() {
        String mensajeError = "";
        if (txtCedula.getText() == null || txtCedula.getText().isEmpty()) mensajeError += "La cédula es obligatoria.\n";
        if (txtNombre.getText() == null || txtNombre.getText().isEmpty()) mensajeError += "El nombre es obligatorio.\n";
        if (txtEdad.getText() == null || txtEdad.getText().isEmpty()) mensajeError += "La edad es obligatoria.\n";
        else {
            try {
                Integer.parseInt(txtEdad.getText());
            } catch (NumberFormatException e) {
                mensajeError += "La edad debe ser un número válido.\n";
            }
        }
        if (cbSexo.getValue() == null) mensajeError += "El sexo es obligatorio.\n";
        if (txtNombreUsuario.getText() == null || txtNombreUsuario.getText().isEmpty()) mensajeError += "El nombre de usuario es obligatorio.\n";
        if (esNuevo && (txtContrasena.getText() == null || txtContrasena.getText().isEmpty())) {
            mensajeError += "La contraseña es obligatoria para nuevos usuarios.\n";
        }
        if (txtNumSeguroSocial.getText() == null || txtNumSeguroSocial.getText().isEmpty()) mensajeError += "El número de seguro social es obligatorio.\n";


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
        alert.showAndWait();
    }
}