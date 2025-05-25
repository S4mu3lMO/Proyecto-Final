package co.edu.uniquindio.finalproyect.model;

public interface iPacienteAcciones {
    boolean solicitudCitaMedica(CitaMedica citaMedica);
    boolean cancelacionCitaMedica(CitaMedica citaMedica);
    boolean consultaHistorialMedico(HistorialMedico historialMedico);
    boolean notificacionCitaMedica(CitaMedica citaMedica);
}
