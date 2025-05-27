package co.edu.uniquindio.finalproyect.viewController.medicoSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.CitaMedica;
import co.edu.uniquindio.finalproyect.model.Medico;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
// import javafx.scene.control.ListCell; // No se usa directamente si formateamos el String
import javafx.scene.control.ListView;
import javafx.stage.Stage; // Para initOwner en Alerta

import java.time.format.DateTimeFormatter;
// import java.util.stream.Collectors; // No se usa en este controlador

public class MedicoNotificacionesController implements MedicoSubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Medico medicoLogueado;

    @FXML private ListView<String> listViewNotificaciones; // Mostraremos strings formateados
    @FXML private Button btnRefrescarNotificaciones;

    private ObservableList<String> notificacionesObsList;
    // Formateadores específicos para fecha y hora para mayor claridad
    private final DateTimeFormatter fechaFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private final DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");


    @Override
    public void setMainApp(App mainApp) { this.mainApp = mainApp; }
    @Override
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }
    @Override
    public void setMedicoLogueado(Medico medico) { this.medicoLogueado = medico; }

    @Override
    public void inicializarDatosSubVista() {
        if (medicoLogueado == null) {
            // Opcional: mostrar alerta si el médico no está logueado
            mostrarAlerta("Error de Sesión", "No se pudo identificar al médico.", "Por favor, inicie sesión nuevamente.", Alert.AlertType.ERROR);
            return;
        }
        notificacionesObsList = FXCollections.observableArrayList();
        listViewNotificaciones.setItems(notificacionesObsList);
        cargarNotificaciones();
    }

    @FXML
    void handleRefrescarNotificaciones() {
        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        if (medicoLogueado == null || sistemaHospitalario == null) {
            if (notificacionesObsList == null) { // Asegurarse que la lista esté inicializada
                notificacionesObsList = FXCollections.observableArrayList();
                listViewNotificaciones.setItems(notificacionesObsList);
            }
            notificacionesObsList.setAll("No se pudo cargar notificaciones (error de sesión).");
            return;
        }
        notificacionesObsList.clear();

        // La lógica de notificaciones aquí es una simulación mostrando las citas del médico.
        // Un sistema real tendría un mecanismo de eventos para generar y almacenar notificaciones.
        if (medicoLogueado.getListCitasMedicas() != null && !medicoLogueado.getListCitasMedicas().isEmpty()) {
            medicoLogueado.getListCitasMedicas().stream()
                    .sorted((c1, c2) -> { // Ordenar por fecha y hora
                        int dateCompare = c1.getFecha().compareTo(c2.getFecha());
                        if (dateCompare == 0) {
                            return c1.getHora().compareTo(c2.getHora());
                        }
                        return dateCompare;
                    })
                    .forEach(cita -> {
                        String notificacion = String.format("Cita (%s): Paciente %s, Fecha: %s %s, Sala: %s. Motivo: %s",
                                cita.getEstadoCita(),
                                (cita.getPaciente() != null ? cita.getPaciente().getNombre() : "N/A"),
                                cita.getFecha().format(this.fechaFormatter), // Usar el formateador de fecha
                                cita.getHora().format(this.horaFormatter),   // Usar el formateador de hora
                                (cita.getSala() != null ? cita.getSala().getNumeroSala() : "N/A"),
                                cita.getMotivo()
                        );
                        notificacionesObsList.add(notificacion);
                    });
        }


        if (notificacionesObsList.isEmpty()) {
            notificacionesObsList.add("No tienes notificaciones nuevas o citas programadas.");
        }
    }

    // Método de alerta privado y autónomo para esta clase
    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        // Hacer la alerta modal a la ventana principal de la aplicación si es posible
        if (mainApp != null && mainApp.getPrimaryStage() != null) {
            alert.initOwner(mainApp.getPrimaryStage());
        }
        alert.showAndWait();
    }
}
