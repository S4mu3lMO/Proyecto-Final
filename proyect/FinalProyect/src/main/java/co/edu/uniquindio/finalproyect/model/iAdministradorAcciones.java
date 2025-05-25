package co.edu.uniquindio.finalproyect.model;

public interface iAdministradorAcciones {
    boolean registrarPaciente(Paciente paciente);
    boolean actualizarPaciente(Paciente paciente);
    boolean eliminarPaciente(Paciente paciente);
    boolean registrarMedico(Medico medico);
    boolean actualizarMedico(Medico medico);
    boolean eliminarMedico(Medico medico);
    boolean agregarSala(Sala sala);
    boolean eliminarSala(Sala sala);
    boolean actualizarSala(Sala sala);
    String mostrarSala(Sala sala);
    boolean agregarCitaMedica(CitaMedica citaMedica,Paciente paciente);
    boolean actualizarCitaMedica(CitaMedica citaMedica,Paciente paciente);
    boolean eliminarCitaMedica(CitaMedica citaMedica);
    String mostrarCitaMedica(CitaMedica citaMedica);
    boolean monitoreoDoctoresDsiponibles(Medico medico);
    boolean asignarPacientesMedicoDisponible(Medico medico);

}
