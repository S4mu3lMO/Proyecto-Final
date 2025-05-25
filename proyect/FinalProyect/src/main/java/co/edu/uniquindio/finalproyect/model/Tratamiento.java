package co.edu.uniquindio.proyectofinal.model;

import java.time.LocalDate;
import java.util.LinkedList;

public class Tratamiento {
    private String idTratamiento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin; // Puede ser nula si es un tratamiento en curso
    private String descripcion;
    private LinkedList<Medicamento> listMedicamentos; // Podría ser List<Medicamento> si creas la clase Medicamento
    private String dosisFrecuencia; // Ej: "1 pastilla cada 8 horas"
    private Medico medicoPrescriptor; // Referencia al objeto Medico

    public Tratamiento(String idTratamiento, LocalDate fechaInicio, LocalDate fechaFin, String descripcion, LinkedList<Medicamento> listMedicamentos, String dosisFrecuencia, Medico medicoPrescriptor) {
        this.idTratamiento = idTratamiento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.listMedicamentos = listMedicamentos;
        this.dosisFrecuencia = dosisFrecuencia;
        this.medicoPrescriptor = medicoPrescriptor;
    }

    public String getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(String idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LinkedList<Medicamento> getListMedicamentos() {
        return listMedicamentos;
    }

    public void setListMedicamentos(LinkedList<Medicamento> listMedicamentos) {
        this.listMedicamentos = listMedicamentos;
    }

    public String getDosisFrecuencia() {
        return dosisFrecuencia;
    }

    public void setDosisFrecuencia(String dosisFrecuencia) {
        this.dosisFrecuencia = dosisFrecuencia;
    }

    public Medico getMedicoPrescriptor() {
        return medicoPrescriptor;
    }

    public void setMedicoPrescriptor(Medico medicoPrescriptor) {
        this.medicoPrescriptor = medicoPrescriptor;
    }
}

