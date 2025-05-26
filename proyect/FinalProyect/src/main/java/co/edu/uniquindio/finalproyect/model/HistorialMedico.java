package co.edu.uniquindio.finalproyect.model;

import java.util.LinkedList;

public class HistorialMedico {
    private String idHistorial;
    private Paciente pacienteAsociado;
    private LinkedList<Diagnostico> listDiagnosticos;
    private LinkedList<Tratamiento> listTratamientos;

    public HistorialMedico(String idHistorial, Paciente pacienteAsociado, LinkedList<Diagnostico> listDiagnosticos, LinkedList<Tratamiento> listTratamientos) {
        this.idHistorial = idHistorial;
        this.pacienteAsociado = pacienteAsociado;
        this.listDiagnosticos = new LinkedList<>();
        this.listTratamientos = new LinkedList<>();
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

}
