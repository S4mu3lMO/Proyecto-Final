package co.edu.uniquindio.finalproyect.viewController.adminSubViews.forms; // O el paquete que uses

import co.edu.uniquindio.finalproyect.model.HorarioDisponibilidad;
import co.edu.uniquindio.finalproyect.model.Medico;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class MedicoHorariosFormController {

    @FXML private Label lblTituloHorariosMedico;
    @FXML private Label lblNombreMedicoHorarios;
    @FXML private TableView<HorarioDisponibilidad> tablaHorariosMedico;
    @FXML private TableColumn<HorarioDisponibilidad, DayOfWeek> colDiaSemanaHorario;
    @FXML private TableColumn<HorarioDisponibilidad, LocalTime> colHoraInicioHorario;
    @FXML private TableColumn<HorarioDisponibilidad, LocalTime> colHoraFinHorario;
    @FXML private ComboBox<DayOfWeek> cbDiaSemanaFormHorario;
    @FXML private TextField txtHoraInicioFormHorario;
    @FXML private TextField txtHoraFinFormHorario;
    @FXML private Button btnAgregarActualizarFormHorario;
    @FXML private Button btnEliminarFormHorario;
    @FXML private Button btnLimpiarCamposFormHorario;
    @FXML private Button btnCerrarDialogoHorarios;

    private Stage dialogStage;
    private SistemaHospitalario sistemaHospitalario;
    private Medico medicoSeleccionado;
    private ObservableList<HorarioDisponibilidad> listaObservableHorarios;
    private HorarioDisponibilidad horarioParaActualizar = null; // Para saber si estamos editando un horario existente

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        // Poblar ComboBox de días si no se hizo en FXML
        if(cbDiaSemanaFormHorario.getItems().isEmpty()){
            cbDiaSemanaFormHorario.setItems(FXCollections.observableArrayList(DayOfWeek.values()));
        }

        colDiaSemanaHorario.setCellValueFactory(new PropertyValueFactory<>("diaSemana"));
        colHoraInicioHorario.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colHoraFinHorario.setCellValueFactory(new PropertyValueFactory<>("horaFin"));

        // Formatear la visualización de LocalTime en la tabla
        colHoraInicioHorario.setCellFactory(column -> formatTimeCell());
        colHoraFinHorario.setCellFactory(column -> formatTimeCell());

        // Listener para cargar datos en el formulario al seleccionar un horario de la tabla
        tablaHorariosMedico.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                horarioParaActualizar = newSelection;
                cbDiaSemanaFormHorario.setValue(newSelection.getDiaSemana());
                txtHoraInicioFormHorario.setText(newSelection.getHoraInicio().format(timeFormatter));
                txtHoraFinFormHorario.setText(newSelection.getHoraFin().format(timeFormatter));
                btnAgregarActualizarFormHorario.setText("Actualizar Horario");
            } else {
                limpiarCamposDelFormulario();
            }
        });
    }

    private TableCell<HorarioDisponibilidad, LocalTime> formatTimeCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(LocalTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(timeFormatter));
                }
            }
        };
    }

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }

    public void setMedico(Medico medico) {
        this.medicoSeleccionado = medico;
        if (medico != null) {
            lblNombreMedicoHorarios.setText("Médico: Dr(a). " + medico.getNombre());
            // inicializarVistaHorarios(); // Llamado desde el controlador que abre este diálogo
        }
    }

    public void inicializarVistaHorarios() {
        if (medicoSeleccionado == null) {
            mostrarAlerta("Error", "Médico no especificado", "No se puede cargar horarios sin un médico seleccionado.", Alert.AlertType.ERROR);
            return;
        }
        // El método consultarHorariosDisponibilidad ya devuelve la lista del médico.
        listaObservableHorarios = FXCollections.observableArrayList(sistemaHospitalario.consultarHorariosDisponibilidad(medicoSeleccionado.getCedula()));
        tablaHorariosMedico.setItems(listaObservableHorarios);
        tablaHorariosMedico.refresh();
        limpiarCamposDelFormulario();
    }


    @FXML
    void handleAgregarActualizarFormHorario(ActionEvent event) {
        if (medicoSeleccionado == null) return;

        DayOfWeek dia = cbDiaSemanaFormHorario.getValue();
        String strHoraInicio = txtHoraInicioFormHorario.getText();
        String strHoraFin = txtHoraFinFormHorario.getText();

        if (dia == null || strHoraInicio.isEmpty() || strHoraFin.isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Datos Incompletos", "Todos los campos (Día, Hora Inicio, Hora Fin) son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        LocalTime horaInicio, horaFin;
        try {
            horaInicio = LocalTime.parse(strHoraInicio, timeFormatter);
            horaFin = LocalTime.parse(strHoraFin, timeFormatter);
        } catch (DateTimeParseException e) {
            mostrarAlerta("Formato Inválido", "Formato de Hora Incorrecto", "Use HH:mm para las horas (ej. 09:00, 14:30).", Alert.AlertType.ERROR);
            return;
        }

        if (horaFin.isBefore(horaInicio) || horaFin.equals(horaInicio)) {
            mostrarAlerta("Horas Inválidas", "Error en Horas", "La hora de fin debe ser posterior a la hora de inicio.", Alert.AlertType.WARNING);
            return;
        }

        boolean resultado;
        if (horarioParaActualizar != null) { // Actualizando un horario existente
            resultado = sistemaHospitalario.actualizarHorarioDisponibilidad(
                    medicoSeleccionado.getCedula(),
                    horarioParaActualizar.getIdHorario(), // Se necesita el ID del horario original
                    dia, horaInicio, horaFin
            ); //
        } else { // Agregando un nuevo horario
            resultado = sistemaHospitalario.registrarHorarioDisponibilidad(
                    medicoSeleccionado.getCedula(),
                    dia, horaInicio, horaFin
            ); //
        }

        if (resultado) {
            mostrarAlerta("Éxito", "Operación Exitosa", "El horario ha sido " + (horarioParaActualizar != null ? "actualizado." : "registrado."), Alert.AlertType.INFORMATION);
            inicializarVistaHorarios(); // Recarga la tabla y limpia el formulario
        } else {
            mostrarAlerta("Error", "Fallo en Operación", "No se pudo guardar/actualizar el horario. Verifique la consola o si ya existe un horario para ese ID (si aplica).", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleEliminarFormHorario(ActionEvent event) {
        if (medicoSeleccionado == null) return;
        HorarioDisponibilidad seleccionado = tablaHorariosMedico.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Sin Selección", "No hay horario seleccionado", "Seleccione un horario de la tabla para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("Eliminar Horario para Dr(a). " + medicoSeleccionado.getNombre());
        confirmacion.setContentText("¿Está seguro de que desea eliminar el horario para " + seleccionado.getDiaSemana() + " de " + seleccionado.getHoraInicio().format(timeFormatter) + " a " + seleccionado.getHoraFin().format(timeFormatter) + "?");
        Optional<ButtonType> result = confirmacion.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean eliminado = sistemaHospitalario.eliminarHorarioDisponibilidad(medicoSeleccionado.getCedula(), seleccionado.getIdHorario()); //
            if (eliminado) {
                mostrarAlerta("Éxito", "Horario Eliminado", "El horario ha sido eliminado.", Alert.AlertType.INFORMATION);
                inicializarVistaHorarios(); // Recarga la tabla y limpia el formulario
            } else {
                mostrarAlerta("Error", "Fallo al Eliminar", "No se pudo eliminar el horario. Verifique la consola.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleLimpiarCamposFormHorario(ActionEvent event) {
        limpiarCamposDelFormulario();
    }

    @FXML
    void handleCerrarDialogoHorarios(ActionEvent event) {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    private void limpiarCamposDelFormulario() {
        tablaHorariosMedico.getSelectionModel().clearSelection(); // Esto también dispara el listener y limpia
        cbDiaSemanaFormHorario.setValue(null);
        txtHoraInicioFormHorario.clear();
        txtHoraFinFormHorario.clear();
        horarioParaActualizar = null;
        btnAgregarActualizarFormHorario.setText("Agregar Horario");
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.initOwner(dialogStage); // Para que la alerta sea modal respecto al diálogo actual
        alert.showAndWait();
    }
}