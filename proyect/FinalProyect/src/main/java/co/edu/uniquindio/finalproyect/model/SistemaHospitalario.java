package co.edu.uniquindio.finalproyect.model;

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

    public SistemaHospitalario(String nombre, String nit) {
        this.nombre = nombre;
        this.nit = nit;
        this.listPacientes = new LinkedList<>();
        this.listMedicos = new LinkedList<>();
        this.listAdministrativos = new LinkedList<>();
        this.listUsuarios = new LinkedList<>();
        this.listCitasMedicas = new LinkedList<>();
        this.listSalas = new LinkedList<>();
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

    public Usuario buscarUsuarioPorNombreUsuario(String nombreUsuario) {
        for (Usuario u : listUsuarios) {
            if (u.getNombreUsuario().equals(nombreUsuario)) {
                return u;
            }
        }
        return null;
    }
    // --- Funcionalidad de Pacientes: Solicitar Cita Médica ---

    /**
     * Solicita una cita médica para un paciente con un médico en una fecha y hora específicas.
     * Realiza validaciones de existencia de paciente y médico, y disponibilidad de médico y sala.
     *
     * @param cedulaPaciente Cédula del paciente que solicita la cita.
     * @param cedulaMedico   Cédula del médico con quien se solicita la cita.
     * @param fechaCita      Fecha de la cita.
     * @param horaCita       Hora de la cita.
     * @param motivoCita     Motivo de la consulta.
     * @return true si la cita fue solicitada y registrada exitosamente, false en caso contrario (paciente/médico no existe, médico no disponible, sala no disponible).
     */
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

        // Marcar la sala como ocupada si tu modelo de Sala lo soporta
        // Actualmente, Sala tiene estaDisponible, pero no un horario detallado.
        // Podrías necesitar una clase o un mapa en ClinicaUq para gestionar la ocupación de salas por fecha/hora.
        // Por ahora, asumimos que 'buscarSalaDisponible' ya implica que no hay conflictos.

        System.out.println("Cita médica solicitada exitosamente. ID de cita: " + idCita); // Mensaje para la vista
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
        // En un sistema real, la disponibilidad de la sala se verificaría contra
        // todas las citas ya agendadas que usan esa sala en esa fecha/hora.
        // Aquí, como 'Sala' no tiene un horario detallado, tendremos que simularlo.
        // Para simplificar, asumiremos que si una sala no está en uso por otra cita
        // en listCitasMedicas para esa fecha/hora, está disponible.

        for (Sala s : listSalas) {
            if (s.getTipoSala() == tipoSala && s.isEstaDisponible()) {
                boolean salaOcupadaPorOtraCita = false;
                for (CitaMedica citaExistente : listCitasMedicas) {
                    // Si ya hay una cita existente que usa esta sala en la misma fecha y hora
                    if (citaExistente.getSala() != null &&
                            citaExistente.getSala().equals(s) && // Compara objetos Sala
                            citaExistente.getFecha().equals(fecha) &&
                            citaExistente.getHora().equals(hora) &&
                            (citaExistente.getEstado() == EstadoCita.PENDIENTE ||
                                    citaExistente.getEstado() == EstadoCita.CONFIRMADA)) { // Solo si la cita está activa
                        salaOcupadaPorOtraCita = true;
                        break;
                    }
                }
                if (!salaOcupadaPorOtraCita) {
                    // Esta sala está disponible en la fecha y hora solicitadas
                    return s;
                }
            }
        }
        return null; // No se encontró sala disponible
    }

    // --- NUEVO MÉTODO: Consultar Historial Médico ---

    /**
     * Consulta y devuelve el historial médico de un paciente específico.
     *
     * @param cedulaPaciente La cédula del paciente cuyo historial se desea consultar.
     * @return El objeto HistorialMedico del paciente si se encuentra, o null si el paciente no existe.
     */
    public HistorialMedico consultarHistorialMedico(String cedulaPaciente) {
        // 1. Buscar al paciente por su cédula usando el método auxiliar
        Paciente paciente = buscarPacientePorCedula(cedulaPaciente);

        // 2. Si el paciente no se encuentra, se imprime un error y se retorna null
        if (paciente == null) {
            System.out.println("Error: Paciente con cédula " + cedulaPaciente + " no encontrado. No se puede consultar el historial.");
            return null;
        }

        // 3. Si el paciente existe, se retorna su historial médico
        // En el método registrarPaciente, nos aseguramos de que cada paciente nuevo tenga un HistorialMedico asignado.
        System.out.println("Historial médico del paciente " + paciente.getNombre() + " consultado exitosamente.");
        return paciente.getHistorialMedico();
    }


    public String notificarCitaMedica(String idCita) {
        CitaMedica cita = null;
        // Buscar la cita por su ID
        for (CitaMedica c : listCitasMedicas) {
            if (c.getIdCita().equals(idCita)) {
                cita = c;
                break;
            }
        }

        // Si la cita no se encuentra, retornar un mensaje de error
        if (cita == null) {
            return "Error: Cita con ID " + idCita + " no encontrada. No se puede generar la notificación.";
        }

        // Construir el mensaje de notificación con los detalles de la cita
        StringBuilder notificacion = new StringBuilder();
        notificacion.append("--- Notificación de Cita Médica ---\n");
        notificacion.append("Paciente: ").append(cita.getPaciente().getNombre()).append(" (Cédula: ").append(cita.getPaciente().getCedula()).append(")\n");
        notificacion.append("Médico: Dr(a). ").append(cita.getMedico().getNombre()).append(" (Especialidad: ").append(cita.getMedico().getEspecialidad()).append(")\n");
        notificacion.append("Fecha: ").append(cita.getFecha()).append("\n");
        notificacion.append("Hora: ").append(cita.getHora()).append("\n");
        notificacion.append("Sala: ").append(cita.getSala().getNumeroSala()).append(" (ID: ").append(cita.getSala().getIdSala()).append(")\n");
        notificacion.append("Motivo: ").append(cita.getMotivo()).append("\n");
        notificacion.append("Estado: ").append(cita.getEstado()).append("\n");
        notificacion.append("--- Fin de Notificación ---");

        System.out.println("Notificación generada para la cita ID: " + idCita);
        return notificacion.toString();
    }

}

