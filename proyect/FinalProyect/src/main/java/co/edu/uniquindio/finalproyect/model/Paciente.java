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
        if (cita != null && this.equals(cita.getPaciente())) {
            System.out.println("--- NOTIFICACIÓN para Paciente " + this.getNombre() + " ---");
            System.out.println("Su cita con Dr(a). " + cita.getMedico().getNombre() +
                    " (" + cita.getFecha() + " " + cita.getHora() + ") ha sido " + mensaje + ".");
            System.out.println("Estado actual: " + cita.getEstadoCita());
            System.out.println("----------------------------------------");
        }
    }
}





