package co.edu.uniquindio.finalproyect.model;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HistorialMedico {
    private String idHistorial; // Podría ser el mismo ID del paciente o uno único
    private Paciente pacienteAsociado; // Asociación con el paciente
    private LinkedList<Diagnostico> listDiagnosticos;
    private LinkedList<Tratamiento> listTratamientos;
    // private List<DocumentoMedico> documentosAdjuntos; // Para funcionalidad extendida
    // private List<Alergia> alergias; // Para más detalle


    public HistorialMedico(String idHistorial, Paciente pacienteAsociado, LinkedList<Diagnostico> listDiagnosticos, LinkedList<Tratamiento> listTratamientos) {
        this.idHistorial = idHistorial;
        this.pacienteAsociado = pacienteAsociado;
        this.listDiagnosticos = listDiagnosticos;
        this.listTratamientos = listTratamientos;
    }

    public String getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(String idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Paciente getPacienteAsociado() {
        return pacienteAsociado;
    }

    public void setPacienteAsociado(Paciente pacienteAsociado) {
        this.pacienteAsociado = pacienteAsociado;
    }

    public LinkedList<Diagnostico> getListDiagnosticos() {
        return listDiagnosticos;
    }

    public void setListDiagnosticos(LinkedList<Diagnostico> listDiagnosticos) {
        this.listDiagnosticos = listDiagnosticos;
    }

    public LinkedList<Tratamiento> getListTratamientos() {
        return listTratamientos;
    }

    public void setListTratamientos(LinkedList<Tratamiento> listTratamientos) {
        this.listTratamientos = listTratamientos;
    }

    // Métodos para agregar información al historial
    public boolean agregarDiagnostico(Diagnostico diagnostico) {
        if (diagnostico != null) {
            this.listDiagnosticos.add(diagnostico);
            return true;
        }
        return false;
    }

    public boolean agregarTratamiento(Tratamiento tratamiento) {
        if (tratamiento != null) {
            this.listTratamientos.add(tratamiento);
            return true;
        }
        return false;
    }

    // Otros métodos de lógica de negocio para el historial
    // public Diagnostico buscarDiagnosticoPorFecha(String fecha) { ... }
}
