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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class MedicoNotificacionesController implements MedicoSubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Medico medicoLogueado;

    @FXML private ListView<String> listViewNotificaciones; // Mostraremos strings formateados
    @FXML private Button btnRefrescarNotificaciones;

    private ObservableList<String> notificacionesObsList;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Override
    public void setMainApp(App mainApp) { this.mainApp = mainApp; }
    @Override
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }
    @Override
    public void setMedicoLogueado(Medico medico) { this.medicoLogueado = medico; }

    @Override
    public void inicializarDatosSubVista() {
        if (medicoLogueado == null) return;
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
            notificacionesObsList.setAll("No se pudo cargar notificaciones (error de sesión).");
            return;
        }
        notificacionesObsList.clear();

        // Simulación de notificaciones: El médico tiene una lista de sus citas.
        // Podríamos iterar sobre las citas del médico y verificar si alguna tuvo un cambio reciente
        // o si está próxima. En un sistema real, las notificaciones se generarían por eventos.
        // Aquí, para simular, mostraremos las citas pendientes y confirmadas del médico.
        // La clase Medico implementa iNotificacionCita, lo que significa que tiene un método notificarCambioCita.
        // Este método actualmente imprime en consola.
        // Para una "bandeja", necesitaríamos almacenar estas notificaciones en algún lugar o
        // generar "notificaciones" basadas en el estado actual de las citas.

        // Ejemplo: Mostrar citas futuras del médico.
        // El método consultarCitasFuturasDeMedico devuelve las citas pendientes o confirmadas.
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
                                cita.getPaciente().getNombre(),
                                cita.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE),
                                cita.getHora().format(timeFormatter),
                                cita.getSala().getNumeroSala(),
                                cita.getMotivo()
                        );
                        notificacionesObsList.add(notificacion);
                    });
        }


        if (notificacionesObsList.isEmpty()) {
            notificacionesObsList.add("No tienes notificaciones nuevas o citas programadas.");
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