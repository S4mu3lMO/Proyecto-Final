package co.edu.uniquindio.proyectofinal.model;

public class Paciente extends Usuario {
    private String numeroSeguroSocial;
    private HistorialMedico historialMedico;

    public Paciente(String nombre, String cedula, Sexo sexo, int edad, String nombreUsuario, String contrasena, TipoUsuario tipoUsuario, String numeroSeguroSocial, HistorialMedico historialMedico) {
        super(nombre, cedula, sexo, edad, nombreUsuario, contrasena, tipoUsuario);
        this.numeroSeguroSocial = numeroSeguroSocial;
        this.historialMedico = historialMedico;
    }

    public String getNumeroSeguroSocial() {
        return numeroSeguroSocial;
    }

    public void setNumeroSeguroSocial(String numeroSeguroSocial) {
        this.numeroSeguroSocial = numeroSeguroSocial;
    }

    public HistorialMedico getHistorialMedico() {
        return historialMedico;
    }

    public void setHistorialMedico(HistorialMedico historialMedico) {
        this.historialMedico = historialMedico;
    }
}
