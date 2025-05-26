package co.edu.uniquindio.finalproyect.model;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class Medico extends Usuario implements iNotificacionCita{
    private String especialidad;
    private String numeroLicenciaMedica;
    private LinkedList<CitaMedica> listCitasMedicas;
    private LinkedList<HorarioDisponibilidad> listHorariosDisponibilidad;


    public Medico(String nombre, String cedula, Sexo sexo, int edad, String nombreUsuario, String contrasena, TipoUsuario tipoUsuario, String especialidad, String numeroLicenciaMedica, LinkedList<CitaMedica> horarioConsultas) {
        super(nombre, cedula, sexo, edad, nombreUsuario, contrasena, tipoUsuario);
        this.especialidad = especialidad;
        this.numeroLicenciaMedica = numeroLicenciaMedica;
        this.listCitasMedicas = new LinkedList<>();
        this.listHorariosDisponibilidad = new LinkedList<>();
    }


    public void setListCitasMedicas(LinkedList<CitaMedica> listCitasMedicas) {
        this.listCitasMedicas = listCitasMedicas;
    }

    public void setListHorariosDisponibilidad(LinkedList<HorarioDisponibilidad> listHorariosDisponibilidad) {
        this.listHorariosDisponibilidad = listHorariosDisponibilidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNumeroLicenciaMedica() {
        return numeroLicenciaMedica;
    }

    public void setNumeroLicenciaMedica(String numeroLicenciaMedica) {
        this.numeroLicenciaMedica = numeroLicenciaMedica;
    }

    public LinkedList<CitaMedica> getListCitasMedicas() {
        return listCitasMedicas;
    }

    public void agregarListCitasMedicas(CitaMedica cita) {
        if (cita != null && !this.listCitasMedicas.contains(cita)) {
            this.listCitasMedicas.add(cita);
        }
    }

    public void removerListCitasMedicas(CitaMedica cita) {
        this.listCitasMedicas.remove(cita);
    }


    public boolean agregarHorarioDisponibilidad(HorarioDisponibilidad horario) {
        if (horario == null) {
            return false;
        }

        for (HorarioDisponibilidad h : listHorariosDisponibilidad) {
            if (h.getIdHorario().equals(horario.getIdHorario())) {
                System.out.println("Error: Ya existe un horario con ID " + horario.getIdHorario());
                return false;
            }
        }
        return this.listHorariosDisponibilidad.add(horario);
    }

    public HorarioDisponibilidad buscarHorarioDisponibilidad(String idHorario) {
        for (HorarioDisponibilidad h : listHorariosDisponibilidad) {
            if (h.getIdHorario().equals(idHorario)) {
                return h;
            }
        }
        return null;
    }


    public boolean actualizarHorarioDisponibilidad(HorarioDisponibilidad horarioActualizado) {
        if (horarioActualizado == null) {
            return false;
        }
        HorarioDisponibilidad horarioOriginal = buscarHorarioDisponibilidad(horarioActualizado.getIdHorario());
        if (horarioOriginal != null) {
            horarioOriginal.setDiaSemana(horarioActualizado.getDiaSemana());
            horarioOriginal.setHoraInicio(horarioActualizado.getHoraInicio());
            horarioOriginal.setHoraFin(horarioActualizado.getHoraFin());
            return true;
        }
        return false;
    }


    public boolean eliminarHorarioDisponibilidad(String idHorario) {
        HorarioDisponibilidad horarioAEliminar = buscarHorarioDisponibilidad(idHorario);
        if (horarioAEliminar != null) {
            return this.listHorariosDisponibilidad.remove(horarioAEliminar);
        }
        return false;
    }


    public LinkedList<HorarioDisponibilidad> getListHorariosDisponibilidad() {
        return listHorariosDisponibilidad;
    }


    @Override
    public void notificarCambioCita(CitaMedica cita, String mensaje) {
        if (cita != null) {
            System.out.println("--- NOTIFICACIÓN para Dr(a). " + this.getNombre() + " ---");
            System.out.println("Cita con paciente " + cita.getPaciente().getNombre() +
                    " (" + cita.getFecha() + " " + cita.getHora() + ") ha sido " + mensaje + ".");
            System.out.println("Estado actual: " + cita.getEstadoCita());
            System.out.println("----------------------------------------");
        }
    }


    public void actualizarCitaEnLista(CitaMedica citaActualizada) {
        if (citaActualizada == null) {
            return;
        }
        for (int i = 0; i < listCitasMedicas.size(); i++) {
            if (listCitasMedicas.get(i).getIdCita().equals(citaActualizada.getIdCita())) {
                listCitasMedicas.set(i, citaActualizada);
                System.out.println("Cita " + citaActualizada.getIdCita() + " actualizada en la lista del médico " + this.getNombre());
                return;
            }
        }
    }
}






