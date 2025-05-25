package co.edu.uniquindio.proyectofinal.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaMedica {
    private String idCita; // Identificador único de la cita
    private Paciente paciente; // Objeto Paciente asociado a esta cita
    private Medico medico; // Objeto Medico asociado a esta cita
    private LocalDate fecha; // Fecha de la cita (ej. 2025-05-21)
    private LocalTime hora; // Hora de la cita (ej. 10:00)
    private Sala sala; // Objeto Sala donde se realizará la consulta
    private String motivo; // Breve descripción del motivo de la cita
    private EstadoCita estado; // Estado actual de la cita (PENDIENTE, CONFIRMADA, CANCELADA, FINALIZADA)

    public CitaMedica(String idCita, Paciente paciente, Medico medico, LocalDate fecha, LocalTime hora, Sala sala, String motivo, EstadoCita estado) {
        this.idCita = idCita;
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
        this.hora = hora;
        this.sala = sala;
        this.motivo = motivo;
        this.estado = estado;
    }

    public String getIdCita() {
        return idCita;
    }

    public void setIdCita(String idCita) {
        this.idCita = idCita;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }
}
