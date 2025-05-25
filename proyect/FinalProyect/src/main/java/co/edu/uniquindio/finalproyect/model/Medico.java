package co.edu.uniquindio.finalproyect.model;

import java.util.ArrayList;
import java.util.LinkedList;


public class Medico extends Usuario implements iCitaMedicaCRUD {
    private String especialidad;
    private String numeroLicenciaMedica;
    private LinkedList<CitaMedica> listCitasMedicas; // Para administrar sus citas y disponibilidad
    // Podrías añadir un Map<LocalDate, List<String>> para manejar horarios específicos


    public Medico(String nombre, String cedula, Sexo sexo, int edad, String nombreUsuario, String contrasena, TipoUsuario tipoUsuario, String especialidad, String numeroLicenciaMedica, LinkedList<CitaMedica> horarioConsultas) {
        super(nombre, cedula, sexo, edad, nombreUsuario, contrasena, tipoUsuario);
        this.especialidad = especialidad;
        this.numeroLicenciaMedica = numeroLicenciaMedica;
        this.listCitasMedicas = new LinkedList<>();
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


    // No se suele tener un setter directo para Listas, es mejor tener métodos para añadir/eliminar
    public void agregarListCitasMedicas(CitaMedica cita) {
        if (cita != null && !this.listCitasMedicas.contains(cita)) {
            this.listCitasMedicas.add(cita);
        }
    }

    public void removerListCitasMedicas(CitaMedica cita) {
        this.listCitasMedicas.remove(cita);
    }
}


