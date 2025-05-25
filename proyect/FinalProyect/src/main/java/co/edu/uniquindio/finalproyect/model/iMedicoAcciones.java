package co.edu.uniquindio.finalproyect.model;

public interface iMedicoAcciones {
    boolean historialMedicoPaciente(HistorialMedico historialMedico,Paciente paciente);
    boolean registroDiagnosticoPaciente(Diagnostico diagnostico,Paciente paciente);
    boolean registroTratamientoPaciente(Tratamiento tratamiento,Paciente paciente);
    boolean agregarCitaMedica(CitaMedica citaMedica,Paciente paciente);
    boolean actualizarCitaMedica(CitaMedica citaMedica,Paciente paciente);
    boolean eliminarCitaMedica(CitaMedica citaMedica);
    String mostrarCitaMedica(CitaMedica citaMedica);
    boolean notificacionCitaMedica(CitaMedica citaMedica);

}
