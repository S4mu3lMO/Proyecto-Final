package co.edu.uniquindio.finalproyect.viewController.pacienteSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.Sexo;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.TipoUsuario;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PacienteDatosPersonalesController implements PacienteSubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Paciente pacienteLogueado;

    @FXML private TextField txtCedulaPaciente;
    @FXML private TextField txtNombrePaciente;
    @FXML private TextField txtEdadPaciente;
    @FXML private ComboBox<Sexo> cbSexoPaciente;
    @FXML private TextField txtNumSeguroPaciente;
    @FXML private TextField txtNombreUsuarioPaciente;
    @FXML private PasswordField pwdNuevaContrasena;
    @FXML private PasswordField pwdConfirmarContrasena;
    @FXML private Button btnActualizarMisDatos;

    @Override
    public void setMainApp(App mainApp) { this.mainApp = mainApp; }

    @Override
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }

    @Override
    public void setPacienteLogueado(Paciente paciente) { this.pacienteLogueado = paciente; }

    @Override
    public void inicializarDatosSubVistaPaciente() {
        if (pacienteLogueado == null) {
            mostrarAlerta("Error", "Paciente no identificado", "No se pudieron cargar los datos del paciente.", Alert.AlertType.ERROR);
            btnActualizarMisDatos.setDisable(true);
            return;
        }
        btnActualizarMisDatos.setDisable(false);
        cargarDatosPaciente();
    }

    @FXML
    public void initialize() {
        cbSexoPaciente.setItems(FXCollections.observableArrayList(Sexo.values()));
    }

    private void cargarDatosPaciente() {
        if (pacienteLogueado != null) {
            txtCedulaPaciente.setText(pacienteLogueado.getCedula());
            txtNombrePaciente.setText(pacienteLogueado.getNombre());
            txtEdadPaciente.setText(String.valueOf(pacienteLogueado.getEdad()));
            cbSexoPaciente.setValue(pacienteLogueado.getSexo());
            txtNumSeguroPaciente.setText(pacienteLogueado.getNumeroSeguroSocial());
            txtNombreUsuarioPaciente.setText(pacienteLogueado.getNombreUsuario());
            pwdNuevaContrasena.clear();
            pwdConfirmarContrasena.clear();
        }
    }

    @FXML
    void handleActualizarMisDatos(ActionEvent event) {
        if (pacienteLogueado == null) {
            mostrarAlerta("Error", "Operación no permitida", "No hay un paciente logueado para actualizar.", Alert.AlertType.ERROR);
            return;
        }

        if (!validarCampos()) {
            return;
        }

        String nombre = txtNombrePaciente.getText().trim();
        int edad;
        try {
            edad = Integer.parseInt(txtEdadPaciente.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "Edad inválida", "La edad debe ser un número.", Alert.AlertType.ERROR);
            return;
        }
        Sexo sexo = cbSexoPaciente.getValue();
        String nombreUsuario = txtNombreUsuarioPaciente.getText().trim();
        String nuevaContrasena = pwdNuevaContrasena.getText();

        Paciente pacienteActualizado = new Paciente(
                nombre,
                pacienteLogueado.getCedula(),
                sexo,
                edad,
                nombreUsuario,
                nuevaContrasena.isEmpty() ? pacienteLogueado.getContrasena() : nuevaContrasena,
                pacienteLogueado.getTipoUsuario(),
                pacienteLogueado.getNumeroSeguroSocial(),
                pacienteLogueado.getHistorialMedico()
        );

        boolean exito = sistemaHospitalario.actualizarPaciente(pacienteActualizado);

        if (exito) {
            this.pacienteLogueado = pacienteActualizado;

            mostrarAlerta("Éxito", "Datos Actualizados", "Sus datos personales han sido actualizados correctamente.", Alert.AlertType.INFORMATION);
            pwdNuevaContrasena.clear();
            pwdConfirmarContrasena.clear();
        } else {
            mostrarAlerta("Error", "Actualización Fallida", "No se pudieron actualizar sus datos. Intente más tarde o contacte a soporte.", Alert.AlertType.ERROR);
            cargarDatosPaciente();
        }
    }

    private boolean validarCampos() {
        String nombre = txtNombrePaciente.getText();
        String edadStr = txtEdadPaciente.getText();
        Sexo sexo = cbSexoPaciente.getValue();
        String nombreUsuario = txtNombreUsuarioPaciente.getText();
        String nuevaContrasena = pwdNuevaContrasena.getText();
        String confirmarContrasena = pwdConfirmarContrasena.getText();

        String errores = "";

        if (nombre == null || nombre.trim().isEmpty()) {
            errores += "El nombre no puede estar vacío.\n";
        }
        if (edadStr == null || edadStr.trim().isEmpty()) {
            errores += "La edad no puede estar vacía.\n";
        } else {
            try {
                int edad = Integer.parseInt(edadStr.trim());
                if (edad <= 0 || edad > 120) { // Rango de edad razonable
                    errores += "La edad ingresada no es válida.\n";
                }
            } catch (NumberFormatException e) {
                errores += "La edad debe ser un número válido.\n";
            }
        }
        if (sexo == null) {
            errores += "Debe seleccionar un sexo.\n";
        }
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            errores += "El nombre de usuario no puede estar vacío.\n";
        }

        if (!nuevaContrasena.isEmpty() || !confirmarContrasena.isEmpty()) {
            if (!nuevaContrasena.equals(confirmarContrasena)) {
                errores += "Las contraseñas no coinciden.\n";
            }
            if (nuevaContrasena.length() < 4 && !nuevaContrasena.isEmpty()) {
                errores += "La nueva contraseña debe tener al menos 4 caracteres.\n";
            }
        }


        if (errores.isEmpty()) {
            return true;
        } else {
            mostrarAlerta("Errores de Validación", "Por favor, corrija los siguientes errores:", errores, Alert.AlertType.ERROR);
            return false;
        }
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        if (mainApp != null && mainApp.getPrimaryStage() != null) {
            alert.initOwner(mainApp.getPrimaryStage());
        }
        alert.showAndWait();
    }
}
