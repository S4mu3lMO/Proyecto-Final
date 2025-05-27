package co.edu.uniquindio.finalproyect.viewController.pacienteSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.CitaMedica;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Collectors;

public class PacienteNotificacionesController implements PacienteSubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Paciente pacienteLogueado;

    @FXML private ListView<String> listViewMisNotificaciones;
    @FXML private Button btnRefrescarNotificacionesPaciente;

    private ObservableList<String> misNotificacionesObsList;

    private final DateTimeFormatter fechaFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private final DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");


    @Override
    public void setMainApp(App mainApp) { this.mainApp = mainApp; }

    @Override
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }

    @Override
    public void setPacienteLogueado(Paciente paciente) { this.pacienteLogueado = paciente; }

    @Override
    public void inicializarDatosSubVistaPaciente() {
        if (pacienteLogueado == null) {
            mostrarAlerta("Error", "Paciente no identificado.", "No se pudieron cargar las notificaciones.", Alert.AlertType.ERROR);
            return;
        }
        misNotificacionesObsList = FXCollections.observableArrayList();
        listViewMisNotificaciones.setItems(misNotificacionesObsList);
        cargarMisNotificaciones();
    }

    @FXML
    public void initialize() {
    }

    @FXML
    void handleRefrescarNotificacionesPaciente(ActionEvent event) {
        cargarMisNotificaciones();
    }

    private void cargarMisNotificaciones() {
        if (pacienteLogueado == null || sistemaHospitalario == null) {
            if (misNotificacionesObsList == null) {
                misNotificacionesObsList = FXCollections.observableArrayList();
                listViewMisNotificaciones.setItems(misNotificacionesObsList);
            }
            misNotificacionesObsList.setAll("No se pudo cargar notificaciones (error de sesión).");
            return;
        }
        misNotificacionesObsList.clear();


        if (sistemaHospitalario.getListCitasMedicas() != null) {
            sistemaHospitalario.getListCitasMedicas().stream()
                    .filter(cita -> cita.getPaciente().getCedula().equals(pacienteLogueado.getCedula()))
                    .sorted(Comparator.comparing(CitaMedica::getFecha).thenComparing(CitaMedica::getHora).reversed())
                    .forEach(cita -> {
                        String mensajeBase = String.format("Cita con Dr(a). %s (%s) el %s a las %s (Sala: %s)",
                                cita.getMedico().getNombre(),
                                cita.getMedico().getEspecialidad(),
                                cita.getFecha().format(fechaFormatter),
                                cita.getHora().format(horaFormatter),
                                cita.getSala().getNumeroSala()
                        );
                        String notificacion = "";
                        switch (cita.getEstadoCita()) {
                            case PENDIENTE:
                                notificacion = "[PENDIENTE] " + mensajeBase + ". Motivo: " + cita.getMotivo();
                                break;
                            case CONFIRMADA:
                                notificacion = "[CONFIRMADA] " + mensajeBase + ". Su cita está confirmada.";
                                break;
                            case CANCELADA:
                                notificacion = "[CANCELADA] " + mensajeBase + " ha sido cancelada.";
                                break;
                            case FINALIZADA:

                                break;
                            default:
                                break;
                        }
                        if (!notificacion.isEmpty()) {
                            misNotificacionesObsList.add(notificacion);
                        }
                    });
        }

        if (misNotificacionesObsList.isEmpty()) {
            misNotificacionesObsList.add("No tienes notificaciones.");
        }
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Notificaciones");
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        if (mainApp != null && mainApp.getPrimaryStage() != null) {
            alert.initOwner(mainApp.getPrimaryStage());
        }
        alert.showAndWait();
    }
}
