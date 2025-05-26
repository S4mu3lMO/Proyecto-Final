package co.edu.uniquindio.finalproyect.model;

public class Paciente extends Usuario implements iNotificacionCita{
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


    @Override
    public void notificarCambioCita(CitaMedica cita, String mensaje) {
        System.out.println("--- Notificación de Cita para " + this.getNombre() + " ---");
        System.out.println("Mensaje: " + mensaje);
        System.out.println("Detalles de la Cita ID: " + cita.getIdCita());
        System.out.println("Médico: Dr. " + cita.getMedico().getNombre());
        System.out.println("Fecha: " + cita.getFecha());
        System.out.println("Hora: " + cita.getHora());
        System.out.println("Sala: " + cita.getSala().getNumeroSala() + " (" + cita.getSala().getTipoSala() + ")");
        System.out.println("Estado Actual: " + cita.getEstadoCita());
        System.out.println("----------------------------------------");
    }
}





