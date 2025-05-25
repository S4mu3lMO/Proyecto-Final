package co.edu.uniquindio.proyectofinal.model;

import java.util.ArrayList;
import java.util.LinkedList;


public class Medico extends Usuario {
    private String especialidad;
    private String numeroLicenciaMedica;
    private LinkedList<CitaMedica> horarioConsultas; // Para administrar sus citas y disponibilidad
    // Podrías añadir un Map<LocalDate, List<String>> para manejar horarios específicos


    public Medico(String nombre, String cedula, Sexo sexo, int edad, String nombreUsuario, String contrasena, TipoUsuario tipoUsuario, String especialidad, String numeroLicenciaMedica, LinkedList<CitaMedica> horarioConsultas) {
        super(nombre, cedula, sexo, edad, nombreUsuario, contrasena, tipoUsuario);
        this.especialidad = especialidad;
        this.numeroLicenciaMedica = numeroLicenciaMedica;
        this.horarioConsultas = horarioConsultas;
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

    public LinkedList<CitaMedica> getHorarioConsultas() {
        return horarioConsultas;
    }


    // No se suele tener un setter directo para Listas, es mejor tener métodos para añadir/eliminar
    public void agregarCitaAHorario(CitaMedica cita) {
        if (cita != null && !this.horarioConsultas.contains(cita)) {
            this.horarioConsultas.add(cita);
        }
    }

    public void removerCitaDeHorario(CitaMedica cita) {
        this.horarioConsultas.remove(cita);
    }
}


