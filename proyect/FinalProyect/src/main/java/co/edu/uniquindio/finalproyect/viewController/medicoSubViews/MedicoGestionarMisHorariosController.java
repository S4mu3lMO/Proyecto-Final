package co.edu.uniquindio.finalproyect.viewController.medicoSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.HorarioDisponibilidad;
import co.edu.uniquindio.finalproyect.model.Medico;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class MedicoGestionarMisHorariosController implements MedicoSubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Medico medicoLogueado;

    @FXML private TableView<HorarioDisponibilidad> tablaMisHorarios;
    @FXML private TableColumn<HorarioDisponibilidad, DayOfWeek> colDiaSemana;
    @FXML private TableColumn<HorarioDisponibilidad, LocalTime> colHoraInicio;
    @FXML private TableColumn<HorarioDisponibilidad, LocalTime> colHoraFin;
    @FXML private ComboBox<DayOfWeek> cbDiaSemanaHorario;
    @FXML private TextField txtHoraInicioHorario;
    @FXML private TextField txtHoraFinHorario;
    @FXML private Button btnAgregarHorarioMedico;
    @FXML private Button btnEliminarHorarioMedico;
    @FXML private Button btnLimpiarCamposHorario;

    private ObservableList<HorarioDisponibilidad> misHorariosObsList;
    private HorarioDisponibilidad horarioSeleccionadoParaEdicion = null; // Para saber si estamos editando

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void setMainApp(App mainApp) { this.mainApp = mainApp; }
    @Override
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }
    @Override
    public void setMedicoLogueado(Medico medico) { this.medicoLogueado = medico; }

    @Override
    public void inicializarDatosSubVista() {
        if (medicoLogueado == null) return;

        // Configurar ComboBox de días si no se hizo en FXML
        if (cbDiaSemanaHorario.getItems().isEmpty()) {
            cbDiaSemanaHorario.setItems(FXCollections.observableArrayList(DayOfWeek.values()));
        }

        colDiaSemana.setCellValueFactory(new PropertyValueFactory<>("diaSemana"));
        colHoraInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colHoraFin.setCellValueFactory(new PropertyValueFactory<>("horaFin"));

        // Formatear la visualización de LocalTime en la tabla
        colHoraInicio.setCellFactory(column -> formatTimeCell());
        colHoraFin.setCellFactory(column -> formatTimeCell());


        cargarMisHorarios();

        // Listener para cargar campos al seleccionar un horario de la tabla
        tablaMisHorarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                horarioSeleccionadoParaEdicion = newSelection;
                cbDiaSemanaHorario.setValue(newSelection.getDiaSemana());
                txtHoraInicioHorario.setText(newSelection.getHoraInicio().format(timeFormatter));
                txtHoraFinHorario.setText(newSelection.getHoraFin().format(timeFormatter));
                btnAgregarHorarioMedico.setText("Actualizar Horario Seleccionado");
            } else {
                limpiarCamposFormularioHorario();
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


    private void cargarMisHorarios() {
        // El médico tiene su propia lista de horarios.
        // El método consultarHorariosDisponibilidad(cedulaMedico) ya accede a esta.
        misHorariosObsList = FXCollections.observableArrayList(sistemaHospitalario.consultarHorariosDisponibilidad(medicoLogueado.getCedula()));
        tablaMisHorarios.setItems(misHorariosObsList);
        tablaMisHorarios.refresh();
    }

    @FXML
    void handleAgregarHorarioMedico() {
        DayOfWeek dia = cbDiaSemanaHorario.getValue();
        String strHoraInicio = txtHoraInicioHorario.getText();
        String strHoraFin = txtHoraFinHorario.getText();

        if (dia == null || strHoraInicio.isEmpty() || strHoraFin.isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Datos Incompletos", "Todos los campos son obligatorios.", Alert.AlertType.WARNING);
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
        if (horarioSeleccionadoParaEdicion != null) { // Actualizando
            resultado = sistemaHospitalario.actualizarHorarioDisponibilidad(
                    medicoLogueado.getCedula(),
                    horarioSeleccionadoParaEdicion.getIdHorario(), // Se necesita el ID del horario original
                    dia, horaInicio, horaFin
            ); //
        } else { // Agregando nuevo
            resultado = sistemaHospitalario.registrarHorarioDisponibilidad(
                    medicoLogueado.getCedula(),
                    dia, horaInicio, horaFin
            ); //
        }


        if (resultado) {
            mostrarAlerta("Éxito", "Operación Exitosa", "El horario ha sido " + (horarioSeleccionadoParaEdicion != null ? "actualizado." : "registrado."), Alert.AlertType.INFORMATION);
            cargarMisHorarios();
            limpiarCamposFormularioHorario();
        } else {
            mostrarAlerta("Error", "Fallo en Operación", "No se pudo guardar/actualizar el horario. Verifique la consola o si ya existe un horario similar.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleEliminarHorarioMedico() {
        HorarioDisponibilidad seleccionado = tablaMisHorarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Sin Selección", "No hay horario seleccionado", "Seleccione un horario de la tabla para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("Eliminar Horario");
        confirmacion.setContentText("¿Está seguro de que desea eliminar el horario para " + seleccionado.getDiaSemana() + " de " + seleccionado.getHoraInicio() + " a " + seleccionado.getHoraFin() + "?");
        Optional<ButtonType> result = confirmacion.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean eliminado = sistemaHospitalario.eliminarHorarioDisponibilidad(medicoLogueado.getCedula(), seleccionado.getIdHorario()); //
            if (eliminado) {
                mostrarAlerta("Éxito", "Horario Eliminado", "El horario ha sido eliminado.", Alert.AlertType.INFORMATION);
                cargarMisHorarios();
                limpiarCamposFormularioHorario();
            } else {
                mostrarAlerta("Error", "Fallo al Eliminar", "No se pudo eliminar el horario.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleLimpiarCamposHorario() {
        limpiarCamposFormularioHorario();
    }

    private void limpiarCamposFormularioHorario() {
        tablaMisHorarios.getSelectionModel().clearSelection();
        cbDiaSemanaHorario.setValue(null);
        txtHoraInicioHorario.clear();
        txtHoraFinHorario.clear();
        horarioSeleccionadoParaEdicion = null;
        btnAgregarHorarioMedico.setText("Agregar Horario");
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}