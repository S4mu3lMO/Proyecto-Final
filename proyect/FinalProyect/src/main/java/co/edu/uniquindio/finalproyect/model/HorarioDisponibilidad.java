
package co.edu.uniquindio.finalproyect.model;

import java.time.LocalTime;
import java.time.DayOfWeek;

public class HorarioDisponibilidad {
    private String idHorario;
    private DayOfWeek diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public HorarioDisponibilidad(String idHorario, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        this.idHorario = idHorario;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }


    public String getIdHorario() { return idHorario; }
    public void setIdHorario(String idHorario) { this.idHorario = idHorario; }
    public DayOfWeek getDiaSemana() { return diaSemana; }
    public void setDiaSemana(DayOfWeek diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

}