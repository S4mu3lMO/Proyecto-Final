package co.edu.uniquindio.proyectofinal.model;

import java.time.LocalDate; // Para manejar fechas de forma moderna
import java.util.ArrayList;
import java.util.List;

// --- Clase Diagnostico ---
public class Diagnostico {
    private String idDiagnostico;
    private LocalDate fecha;
    private String descripcion;
    private Medico medicoResponsable; // Referencia al objeto Medico

    public Diagnostico(String idDiagnostico, LocalDate fecha, String descripcion, Medico medicoResponsable) {
        this.idDiagnostico = idDiagnostico;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.medicoResponsable = medicoResponsable;
    }

    // Getters y Setters
    public String getIdDiagnostico() { return idDiagnostico; }
    public LocalDate getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public Medico getMedicoResponsable() { return medicoResponsable; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    // ... otros setters
}