package co.edu.uniquindio.finalproyect.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.UUID;

public class SistemaHospitalario {
    private String nombre;
    private String nit;
    private LinkedList<Paciente> listPacientes;
    private LinkedList<Usuario> listUsuarios;
    private LinkedList<Administrativo> listAdministrativos;
    private LinkedList<CitaMedica> listCitasMedicas;
    private LinkedList<Sala> listSalas;
    private LinkedList<Medico> listMedicos;
    private LinkedList<Medicamento> listMedicamentos;

    public SistemaHospitalario(String nombre, String nit) {
        this.nombre = nombre;
        this.nit = nit;
        this.listPacientes = new LinkedList<>();
        this.listMedicos = new LinkedList<>();
        this.listAdministrativos = new LinkedList<>();
        this.listUsuarios = new LinkedList<>();
        this.listCitasMedicas = new LinkedList<>();
        this.listSalas = new LinkedList<>();
        this.listMedicamentos = new LinkedList<>();
    }


    public LinkedList<Medicamento> getListMedicamentos() {
        return listMedicamentos;
    }

    public void setListMedicamentos(LinkedList<Medicamento> listMedicamentos) {
        this.listMedicamentos = listMedicamentos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public LinkedList<Paciente> getListPacientes() {
        return listPacientes;
    }

    public void setListPacientes(LinkedList<Paciente> listPacientes) {
        this.listPacientes = listPacientes;
    }

    public LinkedList<Usuario> getListUsuarios() {
        return listUsuarios;
    }

    public void setListUsuarios(LinkedList<Usuario> listUsuarios) {
        this.listUsuarios = listUsuarios;
    }

    public LinkedList<Administrativo> getListAdministrativos() {
        return listAdministrativos;
    }

    public void setListAdministrativos(LinkedList<Administrativo> listAdministrativos) {
        this.listAdministrativos = listAdministrativos;
    }

    public LinkedList<CitaMedica> getListCitasMedicas() {
        return listCitasMedicas;
    }

    public void setListCitasMedicas(LinkedList<CitaMedica> listCitasMedicas) {
        this.listCitasMedicas = listCitasMedicas;
    }

    public LinkedList<Sala> getListSalas() {
        return listSalas;
    }

    public void setListSalas(LinkedList<Sala> listSalas) {
        this.listSalas = listSalas;
    }

    public LinkedList<Medico> getListMedicos() {
        return listMedicos;
    }

    public void setListMedicos(LinkedList<Medico> listMedicos) {
        this.listMedicos = listMedicos;
    }

    // Métodos de Gestión de Usuarios

    public boolean registrarPaciente(Paciente paciente){
        // Verificar si la cédula ya existe en la lista general de usuarios (más robusto)
        if(buscarUsuarioPorCedula(paciente.getCedula()) != null){
            System.out.println("Error: Usuario (Paciente) con cédula " + paciente.getCedula() + " ya existe.");
            return false;
        }
        paciente.setTipoUsuario(TipoUsuario.PACIENTE);
        // Asegurarse de que el historial médico se cree si no viene en el objeto Paciente
        if (paciente.getHistorialMedico() == null) {
            String idHistorial = UUID.randomUUID().toString();
            HistorialMedico nuevoHistorial = new HistorialMedico(idHistorial, paciente, new LinkedList<>(), new LinkedList<>());
            paciente.setHistorialMedico(nuevoHistorial);
        }
        listPacientes.add(paciente);
        listUsuarios.add(paciente); // Añadir también a la lista general de usuarios
        System.out.println("Paciente " + paciente.getNombre() + " registrado exitosamente.");
        return true;
    }

    public boolean actualizarPaciente(Paciente paciente){
        Paciente pacienteExistente = buscarPacientePorCedula(paciente.getCedula());
        if(pacienteExistente == null){
            System.out.println("Error: Paciente con cédula " + paciente.getCedula() + " no encontrado para actualizar.");
            return false;
        }
        pacienteExistente.setNombre(paciente.getNombre());
        pacienteExistente.setSexo(paciente.getSexo());
        pacienteExistente.setEdad(paciente.getEdad());
        pacienteExistente.setNombreUsuario(paciente.getNombreUsuario());
        pacienteExistente.setContrasena(paciente.getContrasena());
        pacienteExistente.setNumeroSeguroSocial(paciente.getNumeroSeguroSocial());
        System.out.println("Datos del paciente " + paciente.getNombre() + " actualizados exitosamente.");
        return true;
    }

    public Paciente buscarPacientePorCedula(String cedula) {
        for (Paciente p : listPacientes) {
            if (p.getCedula().equals(cedula)) {
                return p;
            }
        }
        return null;
    }

    public boolean registrarMedico(Medico medico) {
        if (medico == null || buscarMedicoPorCedula(medico.getCedula()) != null) {
            System.out.println("Error al registrar médico: Cédula ya existe o médico es nulo.");
            return false;
        }
        medico.setTipoUsuario(TipoUsuario.MEDICO);
        this.listMedicos.add(medico);
        this.listUsuarios.add(medico);
        System.out.println("Médico " + medico.getNombre() + " registrado exitosamente.");
        return true;
    }

    public Medico buscarMedicoPorCedula(String cedula) {
        for (Medico m : listMedicos) {
            if (m.getCedula().equals(cedula)) {
                return m;
            }
        }
        return null;
    }

    public boolean registrarAdministrativo(Administrativo admin) {
        if (admin == null || buscarAdministrativoPorCedula(admin.getCedula()) != null) {
            System.out.println("Error al registrar administrativo: Cédula ya existe o administrativo es nulo.");
            return false;
        }
        admin.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
        this.listAdministrativos.add(admin);
        this.listUsuarios.add(admin);
        System.out.println("Administrativo " + admin.getNombre() + " registrado exitosamente.");
        return true;
    }

    public Administrativo buscarAdministrativoPorCedula(String cedula) {
        for (Administrativo a : listAdministrativos) {
            if (a.getCedula().equals(cedula)) {
                return a;
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorCedula(String cedula) {
        for (Usuario u : listUsuarios) {
            if (u.getCedula().equals(cedula)) {
                return u;
            }
        }
        return null;
    }


    // --- Métodos de Citas Médicas ---

    public boolean solicitarCitaMedica(String cedulaPaciente, String cedulaMedico,
                                       LocalDate fechaCita, LocalTime horaCita, String motivoCita) {

        Paciente paciente = buscarPacientePorCedula(cedulaPaciente);
        if (paciente == null) {
            System.out.println("Error: Paciente con cédula " + cedulaPaciente + " no encontrado.");
            return false;
        }

        Medico medico = buscarMedicoPorCedula(cedulaMedico);
        if (medico == null) {
            System.out.println("Error: Médico con cédula " + cedulaMedico + " no encontrado.");
            return false;
        }

        DayOfWeek diaSemanaCita = fechaCita.getDayOfWeek();
        boolean estaDisponibleEnHorarioGeneral = false;
        for (HorarioDisponibilidad horario : medico.getListHorariosDisponibilidad()) {
            if (horario.getDiaSemana() == diaSemanaCita &&
                    (horaCita.isAfter(horario.getHoraInicio()) || horaCita.equals(horario.getHoraInicio())) &&
                    (horaCita.isBefore(horario.getHoraFin()))) {
                estaDisponibleEnHorarioGeneral = true;
                break;
            }
        }
        if (!estaDisponibleEnHorarioGeneral) {
            System.out.println("Error en solicitud de cita: El médico " + medico.getNombre() + " no tiene disponibilidad general registrada para el " + diaSemanaCita + " a las " + horaCita + ".");
            return false;
        }

        if (medicoTieneCita(medico, fechaCita, horaCita)) {
            System.out.println("Error: El médico " + medico.getNombre() + " ya tiene una cita agendada para el " + fechaCita + " a las " + horaCita + ".");
            return false;
        }

        Sala salaDisponible = buscarSalaDisponible(fechaCita, horaCita, TipoSala.CONSULTORIO);
        if (salaDisponible == null) {
            System.out.println("Error: No hay salas de consultorio disponibles para el " + fechaCita + " a las " + horaCita + ".");
            return false;
        }

        String idCita = UUID.randomUUID().toString();
        CitaMedica nuevaCita = new CitaMedica(idCita, paciente, medico, fechaCita, horaCita,
                salaDisponible, motivoCita, EstadoCita.PENDIENTE);

        listCitasMedicas.add(nuevaCita);
        medico.agregarListCitasMedicas(nuevaCita);
        System.out.println("Cita médica solicitada exitosamente. ID de cita: " + idCita);
        return true;
    }

    private boolean medicoTieneCita(Medico medico, LocalDate fecha, LocalTime hora) {
        for (CitaMedica c : medico.getListCitasMedicas()) {
            if (c.getFecha().equals(fecha) && c.getHora().equals(hora) &&
                    (c.getEstadoCita() == EstadoCita.PENDIENTE || c.getEstadoCita() == EstadoCita.CONFIRMADA)) { // Usar getEstado()
                return true;
            }
        }
        return false;
    }

    public CitaMedica buscarCitaMedicaPorId(String idCita) {
        for (CitaMedica cita : listCitasMedicas) {
            if (cita.getIdCita().equals(idCita)) {
                return cita;
            }
        }
        return null;
    }

    public boolean actualizarEstadoCita(String idCita, EstadoCita nuevoEstado) {
        CitaMedica cita = buscarCitaMedicaPorId(idCita);
        if (cita == null) {
            System.out.println("Error: Cita con ID " + idCita + " no encontrada.");
            return false;
        }

        cita.setEstadoCita(nuevoEstado);

        Medico medicoAsociado = cita.getMedico();
        if (medicoAsociado != null) {
            medicoAsociado.actualizarCitaEnLista(cita);
            medicoAsociado.notificarCambioCita(cita, "actualizada a " + nuevoEstado.name());
        }

        Paciente pacienteAsociado = cita.getPaciente();
        if (pacienteAsociado != null) {
            System.out.println("Notificando al paciente " + pacienteAsociado.getNombre() + " sobre el cambio de estado de su cita.");
        }
        System.out.println("Estado de la cita " + idCita + " actualizado a " + nuevoEstado + ".");
        return true;
    }

    public String notificarCitaMedica(String idCita) {
        CitaMedica cita = buscarCitaMedicaPorId(idCita);

        if (cita == null) {
            return "Error: Cita con ID " + idCita + " no encontrada. No se puede generar la notificación.";
        }

        StringBuilder notificacion = new StringBuilder();
        notificacion.append("--- Notificación de Cita Médica ---\n");
        notificacion.append("Paciente: ").append(cita.getPaciente().getNombre()).append(" (Cédula: ").append(cita.getPaciente().getCedula()).append(")\n");
        notificacion.append("Médico: Dr(a). ").append(cita.getMedico().getNombre()).append(" (Especialidad: ").append(cita.getMedico().getEspecialidad()).append(")\n");
        notificacion.append("Fecha: ").append(cita.getFecha()).append("\n");
        notificacion.append("Hora: ").append(cita.getHora()).append("\n");
        notificacion.append("Sala: ").append(cita.getSala().getNumeroSala()).append(" (ID: ").append(cita.getSala().getIdSala()).append(")\n");
        notificacion.append("Motivo: ").append(cita.getMotivo()).append("\n");
        notificacion.append("Estado: ").append(cita.getEstadoCita()).append("\n");
        notificacion.append("--- Fin de Notificación ---");

        System.out.println("Notificación generada para la cita ID: " + idCita);
        return notificacion.toString();
    }


    public boolean cancelarCitaMedica(String idCita) {
        CitaMedica cita = buscarCitaMedicaPorId(idCita);

        if (cita == null) {
            System.out.println("Error al cancelar cita: Cita con ID " + idCita + " no encontrada.");
            return false;
        }

        if (cita.getEstadoCita() == EstadoCita.CANCELADA) {
            System.out.println("La cita con ID " + idCita + " ya está cancelada.");
            return false;
        }

        if (cita.getEstadoCita() == EstadoCita.FINALIZADA) {
            System.out.println("La cita con ID " + idCita + " ya ha finalizado y no puede ser cancelada.");
            return false;
        }

        cita.setEstadoCita(EstadoCita.CANCELADA);


        Medico medicoAsociado = cita.getMedico();
        if (medicoAsociado != null) {
            medicoAsociado.actualizarCitaEnLista(cita);
            medicoAsociado.notificarCambioCita(cita, "CANCELADA");
        }

        Paciente pacienteAsociado = cita.getPaciente();
        if (pacienteAsociado != null) {
            pacienteAsociado.notificarCambioCita(cita, "CANCELADA");
        }

        System.out.println("Cita con ID " + idCita + " ha sido cancelada exitosamente.");
        return true;
    }


    //Métodos de Historial Médico y Diagnóstico/Tratamiento

    public HistorialMedico consultarHistorialMedico(String cedulaPaciente) {
        Paciente paciente = buscarPacientePorCedula(cedulaPaciente);
        if (paciente == null) {
            System.out.println("Error: Paciente con cédula " + cedulaPaciente + " no encontrado. No se puede consultar el historial.");
            return null;
        }
        System.out.println("Historial médico del paciente " + paciente.getNombre() + " consultado exitosamente.");
        return paciente.getHistorialMedico();
    }

    public HistorialMedico obtenerHistorialPacienteParaMedico(String cedulaMedico, String cedulaPaciente) {
        Medico medico = buscarMedicoPorCedula(cedulaMedico);
        if (medico == null) {
            System.out.println("Acceso denegado: Médico con cédula " + cedulaMedico + " no encontrado.");
            return null;
        }

        Paciente paciente = buscarPacientePorCedula(cedulaPaciente);
        if (paciente == null) {
            System.out.println("Acceso denegado: Paciente con cédula " + cedulaPaciente + " no encontrado.");
            return null;
        }

        boolean pacienteAtendidoPorMedico = false;
        for (CitaMedica c : medico.getListCitasMedicas()) {
            if (c.getPaciente().getCedula().equals(cedulaPaciente)) {
                pacienteAtendidoPorMedico = true;
                break;
            }
        }

        if (!pacienteAtendidoPorMedico) {
            System.out.println("Acceso denegado: El Dr(a). " + medico.getNombre() + " no tiene relación con el paciente " + paciente.getNombre() + " (" + cedulaPaciente + ").");
            return null;
        }

        System.out.println("Acceso concedido: El Dr(a). " + medico.getNombre() + " ha accedido al historial de " + paciente.getNombre() + ".");
        return paciente.getHistorialMedico();
    }

    public boolean registrarDiagnostico(String cedulaMedico, String cedulaPaciente,
                                        String descripcionDiagnostico, LocalDate fechaDiagnostico) {
        Medico medicoResponsable = buscarMedicoPorCedula(cedulaMedico);
        if (medicoResponsable == null) {
            System.out.println("Error al registrar diagnóstico: Médico con cédula " + cedulaMedico + " no encontrado.");
            return false;
        }

        Paciente paciente = buscarPacientePorCedula(cedulaPaciente);
        if (paciente == null) {
            System.out.println("Error al registrar diagnóstico: Paciente con cédula " + cedulaPaciente + " no encontrado.");
            return false;
        }

        HistorialMedico historial = paciente.getHistorialMedico();
        if (historial == null) {
            System.out.println("Error: El paciente " + paciente.getNombre() + " no tiene un historial médico. Contacte a administración.");
            return false;
        }

        String idDiagnostico = UUID.randomUUID().toString();
        Diagnostico nuevoDiagnostico = new Diagnostico(idDiagnostico, fechaDiagnostico,
                descripcionDiagnostico, medicoResponsable);

        historial.agregarDiagnostico(nuevoDiagnostico);

        System.out.println("Diagnóstico registrado exitosamente para el paciente " + paciente.getNombre() + " por el Dr(a). " + medicoResponsable.getNombre() + ". ID Diagnóstico: " + idDiagnostico);
        return true;
    }

    public boolean registrarTratamiento(String cedulaMedico, String cedulaPaciente,
                                        LocalDate fechaInicio, LocalDate fechaFin,
                                        String descripcionTratamiento, LinkedList<Medicamento> listMedicamentosTratamiento, // Renombrado para claridad
                                        String dosisFrecuencia) {

        Medico medicoPrescriptor = buscarMedicoPorCedula(cedulaMedico);
        if (medicoPrescriptor == null) {
            System.out.println("Error al registrar tratamiento: Médico con cédula " + cedulaMedico + " no encontrado.");
            return false;
        }

        Paciente paciente = buscarPacientePorCedula(cedulaPaciente);
        if (paciente == null) {
            System.out.println("Error al registrar tratamiento: Paciente con cédula " + cedulaPaciente + " no encontrado.");
            return false;
        }

        HistorialMedico historial = paciente.getHistorialMedico();
        if (historial == null) {
            System.out.println("Error: El paciente " + paciente.getNombre() + " no tiene un historial médico. Contacte a administración.");
            return false;
        }

        String idTratamiento = UUID.randomUUID().toString();
        Tratamiento nuevoTratamiento = new Tratamiento(idTratamiento, fechaInicio, fechaFin,
                descripcionTratamiento, listMedicamentosTratamiento, dosisFrecuencia, medicoPrescriptor);
        historial.agregarTratamiento(nuevoTratamiento);

        System.out.println("Tratamiento registrado exitosamente para el paciente " + paciente.getNombre() + " por el Dr(a). " + medicoPrescriptor.getNombre() + ". ID Tratamiento: " + idTratamiento);
        return true;
    }


    // Métodos de Administración de Horarios de Médicos

    public boolean registrarHorarioDisponibilidad(String cedulaMedico, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        Medico medico = buscarMedicoPorCedula(cedulaMedico);
        if (medico == null) {
            System.out.println("Error al registrar horario: Médico con cédula " + cedulaMedico + " no encontrado.");
            return false;
        }
        String idHorario = UUID.randomUUID().toString();
        HorarioDisponibilidad nuevoHorario = new HorarioDisponibilidad(idHorario, diaSemana, horaInicio, horaFin);
        if (medico.agregarHorarioDisponibilidad(nuevoHorario)) {
            System.out.println("Horario de disponibilidad registrado exitosamente para el Dr(a). " + medico.getNombre() + ".");
            return true;
        }
        System.out.println("Fallo al registrar el horario para el Dr(a). " + medico.getNombre() + ". (Posiblemente ya existe un horario con el mismo ID)");
        return false;
    }

    public LinkedList<HorarioDisponibilidad> consultarHorariosDisponibilidad(String cedulaMedico) {
        Medico medico = buscarMedicoPorCedula(cedulaMedico);
        if (medico == null) {
            System.out.println("Error al consultar horarios: Médico con cédula " + cedulaMedico + " no encontrado.");
            return null;
        }
        System.out.println("Horarios de disponibilidad del Dr(a). " + medico.getNombre() + " consultados.");

        return medico.getListHorariosDisponibilidad();
    }

    public boolean actualizarHorarioDisponibilidad(String cedulaMedico, String idHorario, DayOfWeek nuevoDiaSemana, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin) {
        Medico medico = buscarMedicoPorCedula(cedulaMedico);
        if (medico == null) {
            System.out.println("Error al actualizar horario: Médico con cédula " + cedulaMedico + " no encontrado.");
            return false;
        }

        HorarioDisponibilidad horarioActualizado = new HorarioDisponibilidad(idHorario, nuevoDiaSemana, nuevaHoraInicio, nuevaHoraFin);
        if (medico.actualizarHorarioDisponibilidad(horarioActualizado)) {
            System.out.println("Horario con ID " + idHorario + " del Dr(a). " + medico.getNombre() + " actualizado exitosamente.");
            return true;
        }
        System.out.println("Fallo al actualizar el horario con ID " + idHorario + ". (Posiblemente no existe o datos inválidos)");
        return false;
    }

    public boolean eliminarHorarioDisponibilidad(String cedulaMedico, String idHorario) {
        Medico medico = buscarMedicoPorCedula(cedulaMedico);
        if (medico == null) {
            System.out.println("Error al eliminar horario: Médico con cédula " + cedulaMedico + " no encontrado.");
            return false;
        }
        if (medico.eliminarHorarioDisponibilidad(idHorario)) {
            System.out.println("Horario con ID " + idHorario + " del Dr(a). " + medico.getNombre() + " eliminado exitosamente.");
            return true;
        }
        System.out.println("Fallo al eliminar el horario con ID " + idHorario + ". (Posiblemente no existe)");
        return false;
    }

    // Métodos de Salas

    public boolean agregarSala(Sala sala) {
        if (sala == null || buscarSalaPorId(sala.getIdSala()) != null) {
            System.out.println("Error al agregar sala: Sala ya existe o es nula.");
            return false;
        }
        listSalas.add(sala);
        System.out.println("Sala " + sala.getNumeroSala() + " agregada.");
        return true;
    }

    public Sala buscarSalaPorId(String idSala) {
        for (Sala s : listSalas) {
            if (s.getIdSala().equals(idSala)) {
                return s;
            }
        }
        return null;
    }

    public Sala buscarSalaDisponible(LocalDate fecha, LocalTime hora, TipoSala tipoSala) {
        for (Sala s : listSalas) {
            if (s.getTipoSala() == tipoSala && s.isEstaDisponible()) { // Usar getTipo()
                boolean salaOcupadaPorOtraCita = false;
                for (CitaMedica citaExistente : listCitasMedicas) {
                    if (citaExistente.getSala() != null &&
                            citaExistente.getSala().equals(s) &&
                            citaExistente.getFecha().equals(fecha) &&
                            citaExistente.getHora().equals(hora) &&
                            (citaExistente.getEstadoCita() == EstadoCita.PENDIENTE || // Usar getEstado()
                                    citaExistente.getEstadoCita() == EstadoCita.CONFIRMADA)) { // Usar getEstado()
                        salaOcupadaPorOtraCita = true;
                        break;
                    }
                }
                if (!salaOcupadaPorOtraCita) {
                    return s;
                }
            }
        }
        return null;
    }
}

