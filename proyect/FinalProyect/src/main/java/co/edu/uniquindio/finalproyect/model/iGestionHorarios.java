package co.edu.uniquindio.finalproyect.model;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.LinkedList;

public interface iGestionHorarios {


    boolean agregarHorarioDisponibilidad(HorarioDisponibilidad horario);

    HorarioDisponibilidad buscarHorarioDisponibilidad(String idHorario);

    boolean actualizarHorarioDisponibilidad(HorarioDisponibilidad horarioActualizado);

    boolean eliminarHorarioDisponibilidad(String idHorario);

    LinkedList<HorarioDisponibilidad> getListHorariosDisponibilidad();
}