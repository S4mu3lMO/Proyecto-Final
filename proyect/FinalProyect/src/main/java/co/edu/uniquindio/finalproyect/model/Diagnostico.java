package co.edu.uniquindio.finalproyect.model;

import java.time.LocalDate; // Para manejar fechas de forma moderna
import java.util.ArrayList;
import java.util.List;

public class Diagnostico {
    private String idDiagnostico;
    private LocalDate fecha;
    private String descripcion;
    private Medico medicoResponsable;

    public Diagnostico(String idDiagnostico, LocalDate fecha, String descripcion, Medico medicoResponsable) {
        this.idDiagnostico = idDiagnostico;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.medicoResponsable = medicoResponsable;
    }


    public String getIdDiagnostico() {
        return idDiagnostico;
    }

    public void setIdDiagnostico(String idDiagnostico) {
        this.idDiagnostico = idDiagnostico;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Medico getMedicoResponsable() {
        return medicoResponsable;
    }

    public void setMedicoResponsable(Medico medicoResponsable) {
        this.medicoResponsable = medicoResponsable;
    }
}