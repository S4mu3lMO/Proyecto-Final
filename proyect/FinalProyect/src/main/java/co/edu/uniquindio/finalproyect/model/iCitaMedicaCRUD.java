package co.edu.uniquindio.finalproyect.model;

public interface iCitaMedicaCRUD {
    boolean agregarCitaMedica(CitaMedica citaMedica,Paciente paciente);
    boolean actualizarCitaMedica(CitaMedica citaMedica,Paciente paciente);
    boolean eliminarCitaMedica(CitaMedica citaMedica);
    String mostrarCitaMedica(CitaMedica citaMedica);
}
