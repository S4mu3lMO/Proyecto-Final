package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Usuario; // Asegúrate de importar tu clase Usuario
import co.edu.uniquindio.finalproyect.singleton.SistemaHospitalarioSingleton;
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

    private App mainApp; // Referencia a la clase App para cambiar de vista
    private SistemaHospitalario sistemaHospitalario; // Referencia a tu modelo de negocio

    // Este método es llamado por la clase App para inyectar la referencia
    public void setApp(App mainApp) {
        this.mainApp = mainApp;
        // Obtener la instancia de tu SistemaHospitalario desde el Singleton
        this.sistemaHospitalario = SistemaHospitalarioSingleton.getInstance().getSistemaHospitalario();
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String nombreUsuario = txtUsuario.getText();
        String contrasena = pwdContrasena.getText();

        // **Lógica de Autenticación**
        // Llama al método de tu SistemaHospitalario para verificar las credenciales
        Usuario usuarioLogueado = sistemaHospitalario.validarCredenciales(nombreUsuario, contrasena);

        if (usuarioLogueado != null) {
            // Login exitoso
            System.out.println("¡Login exitoso! Usuario: " + usuarioLogueado.getNombreUsuario());
            // Puedes almacenar el usuario logueado en el Singleton si lo necesitas en otras partes
            // SistemaHospitalarioSingleton.getInstance().setUsuarioLogueado(usuarioLogueado);

            if (mainApp != null) {
                mainApp.mostrarMainView(); // Navega a la vista principal
            }
        } else {
            // Login fallido
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Autenticación");
            alert.setHeaderText("Credenciales inválidas");
            alert.setContentText("El nombre de usuario o la contraseña son incorrectos.");
            alert.showAndWait();
        }
    }

    // Otros métodos de inicialización o manejo de eventos si los necesitas
    @FXML
    void initialize() {
        // Este método se llama automáticamente después de que todos los @FXML son inyectados
        // No es el lugar para inicializar `sistemaHospitalario` directamente si se inyecta con `setApp`.
        // La inicialización de `sistemaHospitalario` debería ocurrir después de `setApp`.
    }
}
