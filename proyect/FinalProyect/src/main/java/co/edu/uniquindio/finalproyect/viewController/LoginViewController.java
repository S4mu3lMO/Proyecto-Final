package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Usuario;
import co.edu.uniquindio.finalproyect.singleton.SistemaHospitalarioSingleton; // Ya lo tenías
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField pwdContrasena;

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;

    public void setApp(App mainApp) {
        this.mainApp = mainApp;
        // Obtener la instancia de tu SistemaHospitalario
        this.sistemaHospitalario = mainApp.getSistemaHospitalario();
        if (this.sistemaHospitalario == null) { // Fallback por si acaso
            this.sistemaHospitalario = SistemaHospitalarioSingleton.getInstance().getSistemaHospitalario();
        }
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String nombreUsuario = txtUsuario.getText();
        String contrasena = pwdContrasena.getText();

        if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Error de Validación", "Campos vacíos", "Por favor, ingrese usuario y contraseña.");
            return;
        }

        Usuario usuarioLogueado = sistemaHospitalario.validarCredenciales(nombreUsuario, contrasena); //

        if (usuarioLogueado != null) {
            System.out.println("¡Login exitoso! Usuario: " + usuarioLogueado.getNombreUsuario() + " Tipo: " + usuarioLogueado.getTipoUsuario());
            // Guardar el usuario logueado en el Singleton para acceso global si es necesario
            // SistemaHospitalarioSingleton.getInstance().setUsuarioLogueado(usuarioLogueado); // Tú decides si esto es útil

            if (mainApp != null) {
                mainApp.mostrarDashboard(usuarioLogueado); // Navega al dashboard correspondiente
            }
        } else {
            mostrarAlerta("Error de Autenticación", "Credenciales inválidas", "El nombre de usuario o la contraseña son incorrectos.");
        }
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @FXML
    void initialize() {
        // Código de inicialización si es necesario
    }
}