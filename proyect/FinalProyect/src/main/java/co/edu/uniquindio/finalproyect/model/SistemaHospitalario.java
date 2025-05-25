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


    public boolean registrarPaciente(Paciente paciente){
        for(Paciente p : listPacientes){
            if(p.getCedula().equals(paciente.getCedula())){
                System.out.println("Error: Paciente con cédula " + paciente.getCedula() + " ya existe.");
                return false;
            }
        }
        paciente.setTipoUsuario(TipoUsuario.PACIENTE);
        String idHistorial = UUID.randomUUID().toString();
        HistorialMedico nuevoHistorial = new HistorialMedico(idHistorial, paciente, new LinkedList<>(), new LinkedList<>());
        paciente.setHistorialMedico(nuevoHistorial);
        listPacientes.add(paciente);
        listUsuarios.add(paciente);
        System.out.println("Paciente " + paciente.getNombre() + " registrado exitosamente.");
        return true;
    }


    public boolean actualizarPaciente(Paciente paciente){
        for(Paciente p : listPacientes){
            if(p.getCedula().equals(paciente.getCedula())){
                p.setNombre(paciente.getNombre());
                p.setSexo(paciente.getSexo());
                p.setEdad(paciente.getEdad());
                p.setNombreUsuario(paciente.getNombreUsuario());
                p.setContrasena(paciente.getContrasena());
                p.setNumeroSeguroSocial(paciente.getNumeroSeguroSocial());
                System.out.println("Datos del paciente " + paciente.getNombre() + " actualizados exitosamente.");
                return true;
            }
        }
        System.out.println("Error: Paciente con cédula " + paciente.getCedula() + " no encontrado para actualizar.");
        return false;
    }
    public Paciente buscarPacientePorCedula(String cedula) {
        for (Paciente p : listPacientes) {
            if (p.getCedula().equals(cedula)) {
                return p;
            }
        }
        return null;
    }


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
        medico.getListCitasMedicas().add(nuevaCita); //Añadir la cita al horario del médico

        System.out.println("Cita médica solicitada exitosamente. ID de cita: " + idCita);
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

    private boolean medicoTieneCita(Medico medico, LocalDate fecha, LocalTime hora) {
        for (CitaMedica c : medico.getListCitasMedicas()) {
            if (c.getFecha().equals(fecha) && c.getHora().equals(hora)) {
                return true;
            }
        }
        return false;
    }


    public Sala buscarSalaDisponible(LocalDate fecha, LocalTime hora, TipoSala tipoSala) {
        for (Sala s : listSalas) {
            if (s.getTipoSala() == tipoSala && s.isEstaDisponible()) {
                boolean salaOcupadaPorOtraCita = false;
                for (CitaMedica citaExistente : listCitasMedicas) {
                    if (citaExistente.getSala() != null &&
                            citaExistente.getSala().equals(s) && //
                            citaExistente.getFecha().equals(fecha) &&
                            citaExistente.getHora().equals(hora) &&
                            (citaExistente.getEstadoCita() == EstadoCita.PENDIENTE ||
                                    citaExistente.getEstadoCita() == EstadoCita.CONFIRMADA)) {
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


    public HistorialMedico consultarHistorialMedico(String cedulaPaciente) {
        Paciente paciente = buscarPacientePorCedula(cedulaPaciente);
        if (paciente == null) {
            System.out.println("Error: Paciente con cédula " + cedulaPaciente + " no encontrado. No se puede consultar el historial.");
            return null;
        }
        System.out.println("Historial médico del paciente " + paciente.getNombre() + " consultado exitosamente.");
        return paciente.getHistorialMedico();
    }


    public String notificarCitaMedica(String idCita) {
        CitaMedica cita = null;
        for (CitaMedica c : listCitasMedicas) {
            if (c.getIdCita().equals(idCita)) {
                cita = c;
                break;
            }
        }

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
                                        String descripcionTratamiento, LinkedList<Medicamento> listMedicamentos,
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
        LinkedList<Medicamento> medicamentosDelTratamiento = new LinkedList<>();
        Tratamiento nuevoTratamiento = new Tratamiento(idTratamiento, fechaInicio, fechaFin,
                descripcionTratamiento, medicamentosDelTratamiento, dosisFrecuencia, medicoPrescriptor);
        historial.agregarTratamiento(nuevoTratamiento);

        System.out.println("Tratamiento registrado exitosamente para el paciente " + paciente.getNombre() + " por el Dr(a). " + medicoPrescriptor.getNombre() + ". ID Tratamiento: " + idTratamiento);
        return true;
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

        EstadoCita estadoAnterior = cita.getEstadoCita();
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


}


