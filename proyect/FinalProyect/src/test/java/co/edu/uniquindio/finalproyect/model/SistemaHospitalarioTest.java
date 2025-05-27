package co.edu.uniquindio.finalproyect.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List; // Importado para List
import java.util.UUID; // Importado para UUID
import java.util.logging.Logger;

/**
 * Pruebas unitarias para la clase SistemaHospitalario.
 */
public class SistemaHospitalarioTest {

    private static final Logger LOG = Logger.getLogger(SistemaHospitalarioTest.class.getName());
    private SistemaHospitalario sistemaHospitalario;
    private Paciente pacienteGlobal;
    private Medico medicoGlobal;
    private Sala salaGlobal;
    private CitaMedica citaGlobal;

    @BeforeEach
    void setUp() {
        sistemaHospitalario = new SistemaHospitalario("Hospital Test UQ", "123456789-0");
        pacienteGlobal = new Paciente("Paciente Global", "1000", Sexo.MASCULINO, 30, "pacglob", "pass", TipoUsuario.PACIENTE, "EPSGLOB", null);
        sistemaHospitalario.registrarPaciente(pacienteGlobal);
        medicoGlobal = new Medico("Medico Global", "2000", Sexo.FEMENINO, 40, "medglob", "pass", TipoUsuario.MEDICO, "General", "LICGLOB", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medicoGlobal);
        sistemaHospitalario.registrarHorarioDisponibilidad(medicoGlobal.getCedula(), LocalDate.now().plusDays(1).getDayOfWeek(), LocalTime.of(9,0), LocalTime.of(12,0));
        salaGlobal = new Sala( "CONSULTORIO_GLOBAL", TipoSala.CONSULTORIO, 1);
        sistemaHospitalario.agregarSala(salaGlobal);

        if (sistemaHospitalario.solicitarCitaMedica(pacienteGlobal.getCedula(), medicoGlobal.getCedula(), LocalDate.now().plusDays(1), LocalTime.of(10, 0), "Chequeo Rutinario")) {
            if (!sistemaHospitalario.getListCitasMedicas().isEmpty()) {
                citaGlobal = sistemaHospitalario.getListCitasMedicas().stream()
                        .filter(c -> c.getPaciente().equals(pacienteGlobal) && c.getMedico().equals(medicoGlobal))
                        .findFirst().orElse(null);
            }
        }
    }

    // Pruebas para la gestión de Pacientes

    @Test
    void testRegistrarPacienteExitoso() {
        Paciente paciente = new Paciente("Juan Perez", "1094000111", Sexo.MASCULINO, 30, "jperez", "pass123", TipoUsuario.PACIENTE, "EPS001", null);
        assertTrue(sistemaHospitalario.registrarPaciente(paciente), "El paciente debería registrarse exitosamente."); //
        assertNotNull(sistemaHospitalario.buscarPacientePorCedula("1094000111"), "El paciente debería existir después del registro."); //
        assertNotNull(paciente.getHistorialMedico(), "El historial médico debería crearse si es nulo."); //
    }

    @Test
    void testRegistrarPacienteCedulaExistente() {
        Paciente paciente1 = new Paciente("Juan Perez", "1094000111", Sexo.MASCULINO, 30, "jperez", "pass123", TipoUsuario.PACIENTE, "EPS001", null);
        sistemaHospitalario.registrarPaciente(paciente1); //
        Paciente paciente2 = new Paciente("Ana Gomez", "1094000111", Sexo.FEMENINO, 25, "agomez", "pass456", TipoUsuario.PACIENTE, "EPS002", null);
        assertFalse(sistemaHospitalario.registrarPaciente(paciente2), "No debería registrarse un paciente con cédula existente.");
    }

    @Test
    void testActualizarPacienteExitoso() {
        Paciente paciente = new Paciente("Carlos Ruiz", "1094000222", Sexo.MASCULINO, 40, "cruiz", "pass789", TipoUsuario.PACIENTE, "EPS003", null);
        sistemaHospitalario.registrarPaciente(paciente);
        paciente.setNombre("Carlos Andres Ruiz");
        paciente.setEdad(41);
        assertTrue(sistemaHospitalario.actualizarPaciente(paciente), "El paciente debería actualizarse exitosamente.");
        assertEquals("Carlos Andres Ruiz", sistemaHospitalario.buscarPacientePorCedula("1094000222").getNombre());
    }

    @Test
    void testActualizarPacienteNoExistente() {
        Paciente paciente = new Paciente("Laura Pausini", "1094000333", Sexo.FEMENINO, 45, "lpausini", "italia", TipoUsuario.PACIENTE, "EPS004", null);
        assertFalse(sistemaHospitalario.actualizarPaciente(paciente), "No debería actualizarse un paciente que no existe.");
    }

    @Test
    void testEliminarPacienteExitoso() {
        Paciente paciente = new Paciente("Pedro Paramo", "1094000444", Sexo.MASCULINO, 50, "pparamo", "mexico", TipoUsuario.PACIENTE, "EPS005", null);
        sistemaHospitalario.registrarPaciente(paciente); //
        assertTrue(sistemaHospitalario.eliminarPaciente("1094000444"), "El paciente debería eliminarse exitosamente.");
        assertNull(sistemaHospitalario.buscarPacientePorCedula("1094000444"), "El paciente no debería existir después de ser eliminado.");
    }

    @Test
    void testEliminarPacienteNoExistente() {
        assertFalse(sistemaHospitalario.eliminarPaciente("1094000555"), "No debería eliminarse un paciente que no existe.");
    }


    // Pruebas para la gestión de Médicos
    @Test
    void testRegistrarMedicoExitoso() {
        Medico medico = new Medico("Lucia Diaz", "2094000111", Sexo.FEMENINO, 35, "ldiaz", "medpass1", TipoUsuario.MEDICO, "Cardiología", "LIC001", new LinkedList<>());
        assertTrue(sistemaHospitalario.registrarMedico(medico), "El médico debería registrarse exitosamente."); //
        assertNotNull(sistemaHospitalario.buscarMedicoPorCedula("2094000111"), "El médico debería existir después del registro."); //
    }

    @Test
    void testRegistrarMedicoCedulaExistente() {
        Medico medico1 = new Medico("Lucia Diaz", "2094000111", Sexo.FEMENINO, 35, "ldiaz", "medpass1", TipoUsuario.MEDICO, "Cardiología", "LIC001", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medico1); //
        Medico medico2 = new Medico("Mario Casas", "2094000111", Sexo.MASCULINO, 42, "mcasas", "medpass2", TipoUsuario.MEDICO, "Pediatría", "LIC002", new LinkedList<>());
        assertFalse(sistemaHospitalario.registrarMedico(medico2), "No debería registrarse un médico con cédula existente."); //
    }

    @Test
    void testActualizarMedicoExitoso() {
        Medico medico = new Medico("Sofia Vergara", "2094000222", Sexo.FEMENINO, 48, "svergara", "medpass3", TipoUsuario.MEDICO, "Dermatología", "LIC003", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medico); //
        medico.setEspecialidad("Dermatología Estética");
        assertTrue(sistemaHospitalario.actualizarMedico(medico), "El médico debería actualizarse exitosamente."); //
        assertEquals("Dermatología Estética", sistemaHospitalario.buscarMedicoPorCedula("2094000222").getEspecialidad()); //
    }

    @Test
    void testActualizarMedicoNoExistente() {
        Medico medico = new Medico("Ricardo Arjona", "2094000333", Sexo.MASCULINO, 55, "rarjona", "medpass4", TipoUsuario.MEDICO, "Medicina General", "LIC004", new LinkedList<>());
        assertFalse(sistemaHospitalario.actualizarMedico(medico), "No debería actualizarse un médico que no existe."); //
    }

    @Test
    void testEliminarMedicoExitoso() {
        Medico medico = new Medico("Roberto Gomez", "2094000444", Sexo.MASCULINO, 60, "rgomez", "medpass5", TipoUsuario.MEDICO, "Psiquiatría", "LIC005", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medico); //
        assertTrue(sistemaHospitalario.eliminarMedico("2094000444"), "El médico debería eliminarse exitosamente."); //
        assertNull(sistemaHospitalario.buscarMedicoPorCedula("2094000444"), "El médico no debería existir después de ser eliminado."); //
    }

    // Pruebas para la gestión de Salas
    @Test
    void testAgregarSalaExitosa() {
        Sala sala = new Sala("CONS001", TipoSala.CONSULTORIO, 1);
        assertTrue(sistemaHospitalario.agregarSala(sala), "La sala debería agregarse exitosamente."); //
        assertNotNull(sistemaHospitalario.buscarSalaPorNumero("CONS001"), "La sala debería existir después de agregarla."); //
    }

    @Test
    void testAgregarSalaConIdExistente() {
        Sala sala1 = new Sala("idUnicoSala", "CONS001", TipoSala.CONSULTORIO, 1, true);
        sistemaHospitalario.agregarSala(sala1); //
        Sala sala2 = new Sala("idUnicoSala", "CONS002", TipoSala.LABORATORIO, 5, true);
        assertFalse(sistemaHospitalario.agregarSala(sala2), "No debería agregarse una sala con ID de sala existente."); //
    }

    @Test
    void testAgregarSalaConNumeroExistente() {
        Sala sala1 = new Sala("CONS003", TipoSala.CONSULTORIO, 1);
        sistemaHospitalario.agregarSala(sala1); //
        Sala sala2 = new Sala("CONS003", TipoSala.QUIROFANO, 3); // Mismo número
        assertFalse(sistemaHospitalario.agregarSala(sala2), "No debería agregarse una sala con número de sala existente."); //
    }


    @Test
    void testActualizarSalaExitosa() {
        Sala sala = new Sala("idOriginal", "SALA001", TipoSala.ESPERA, 10, true);
        sistemaHospitalario.agregarSala(sala); //
        Sala salaActualizada = new Sala("idOriginal", "SALA-ESP-01", TipoSala.ESPERA, 15, true);
        assertTrue(sistemaHospitalario.actualizarSala(salaActualizada), "La sala debería actualizarse exitosamente."); //
        assertEquals(15, sistemaHospitalario.buscarSalaPorId("idOriginal").getCapacidad()); //
    }

    @Test
    void testActualizarSalaNoExistente() {
        Sala sala = new Sala("SALA999", TipoSala.LABORATORIO, 4); // Este ID no existe
        assertFalse(sistemaHospitalario.actualizarSala(sala), "No debería actualizarse una sala que no existe."); //
    }

    @Test
    void testEliminarSalaExitosa() {
        Sala sala = new Sala("SALA_A_BORRAR", TipoSala.CONSULTORIO, 1);
        sistemaHospitalario.agregarSala(sala); //
        assertTrue(sistemaHospitalario.eliminarSala(sala.getIdSala()), "La sala debería eliminarse exitosamente."); //
        assertNull(sistemaHospitalario.buscarSalaPorId(sala.getIdSala()), "La sala no debería existir después de ser eliminada."); //
    }

    @Test
    void testEliminarSalaConCitasFuturas() {
        assertNotNull(citaGlobal, "La cita global debe estar configurada para esta prueba.");
        Sala salaDeCita = citaGlobal.getSala();
        assertNotNull(salaDeCita, "La sala de la cita global no puede ser nula.");

        assertFalse(sistemaHospitalario.eliminarSala(salaDeCita.getIdSala()), "No debería eliminarse una sala con citas futuras activas."); //
    }


    // Pruebas para Citas Médicas

    @Test
    void testSolicitarCitaMedicaExitosa() {
        Paciente paciente = new Paciente("Laura Mora", "1095000111", Sexo.FEMENINO, 28, "lmora", "citapass1", TipoUsuario.PACIENTE, "EPSX01", null);
        sistemaHospitalario.registrarPaciente(paciente); //

        Medico medico = new Medico("David Bisbal", "2095000111", Sexo.MASCULINO, 40, "dbisbal", "citamed1", TipoUsuario.MEDICO, "Otorrinolaringología", "LICX01", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medico); //
        sistemaHospitalario.registrarHorarioDisponibilidad(medico.getCedula(), LocalDate.now().plusDays(3).with(DayOfWeek.MONDAY).getDayOfWeek(), LocalTime.of(8, 0), LocalTime.of(12, 0)); //

        Sala sala = new Sala("OTORRINO-1", TipoSala.CONSULTORIO, 1);
        sistemaHospitalario.agregarSala(sala); //

        assertTrue(sistemaHospitalario.solicitarCitaMedica(paciente.getCedula(), medico.getCedula(), LocalDate.now().plusDays(3).with(DayOfWeek.MONDAY), LocalTime.of(9, 0), "Dolor de oido"), "La cita debería solicitarse exitosamente."); //
        assertEquals(2, sistemaHospitalario.getListCitasMedicas().size()); // 1 de setUp + 1 aquí
    }

    @Test
    void testSolicitarCitaMedicoNoDisponible() {
        Paciente paciente = new Paciente("Laura Mora", "1095000111", Sexo.FEMENINO, 28, "lmora", "citapass1", TipoUsuario.PACIENTE, "EPSX01", null);
        sistemaHospitalario.registrarPaciente(paciente); //
        Medico medico = new Medico("David Bisbal", "2095000111", Sexo.MASCULINO, 40, "dbisbal", "citamed1", TipoUsuario.MEDICO, "Otorrinolaringología", "LICX01", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medico); //
        Sala sala = new Sala("OTORRINO-1", TipoSala.CONSULTORIO, 1);
        sistemaHospitalario.agregarSala(sala); //

        assertFalse(sistemaHospitalario.solicitarCitaMedica(paciente.getCedula(), medico.getCedula(), LocalDate.now().plusDays(1).with(DayOfWeek.TUESDAY), LocalTime.of(9, 0), "Dolor de oido"), "No debería solicitarse cita si el médico no tiene horario general."); //
    }

    @Test
    void testSolicitarCitaSinSalasDisponibles() {
        Paciente paciente = new Paciente("Laura Mora", "1095000111", Sexo.FEMENINO, 28, "lmora", "citapass1", TipoUsuario.PACIENTE, "EPSX01", null);
        sistemaHospitalario.registrarPaciente(paciente); //

        Medico medico = new Medico("David Bisbal", "2095000111", Sexo.MASCULINO, 40, "dbisbal", "citamed1", TipoUsuario.MEDICO, "Otorrinolaringología", "LICX01", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medico); //
        sistemaHospitalario.registrarHorarioDisponibilidad(medico.getCedula(), LocalDate.now().plusDays(2).with(DayOfWeek.WEDNESDAY).getDayOfWeek(), LocalTime.of(14, 0), LocalTime.of(18, 0)); //

        if (citaGlobal != null && citaGlobal.getSala().equals(salaGlobal) &&
                citaGlobal.getFecha().equals(LocalDate.now().plusDays(2).with(DayOfWeek.WEDNESDAY)) &&
                citaGlobal.getHora().equals(LocalTime.of(15,0))) {
        }


        assertFalse(sistemaHospitalario.solicitarCitaMedica(paciente.getCedula(), medico.getCedula(), LocalDate.now().plusDays(2).with(DayOfWeek.WEDNESDAY), LocalTime.of(15, 0), "Revisión"), "No debería solicitarse cita si no hay salas disponibles."); //
    }

    @Test
    void testCancelarCitaMedicaExitosa() {
        assertNotNull(citaGlobal, "La cita global debe existir para ser cancelada.");
        assertTrue(sistemaHospitalario.cancelarCitaMedica(citaGlobal.getIdCita()), "La cita debería cancelarse exitosamente."); //
        assertEquals(EstadoCita.CANCELADA, citaGlobal.getEstadoCita()); //
    }

    @Test
    void testCancelarCitaNoExistente() {
        assertFalse(sistemaHospitalario.cancelarCitaMedica("ID_INEXISTENTE"), "No debería cancelarse una cita que no existe."); //
    }


    // Pruebas para Validación de Credenciales

    @Test
    void testValidarCredencialesPacienteExitoso() {
        Paciente paciente = new Paciente("Usuario Prueba", "777777777", Sexo.MASCULINO, 30, "usuariotest", "test123", TipoUsuario.PACIENTE, "EPS777", null);
        sistemaHospitalario.registrarPaciente(paciente); //
        Usuario usuarioLogueado = sistemaHospitalario.validarCredenciales("usuariotest", "test123"); //
        assertNotNull(usuarioLogueado, "El usuario debería loguearse exitosamente.");
        assertEquals(TipoUsuario.PACIENTE, usuarioLogueado.getTipoUsuario()); //
    }

    @Test
    void testValidarCredencialesMedicoExitoso() {
        Medico medico = new Medico("Doctor Log", "888888888", Sexo.FEMENINO, 45, "doclog", "logpass", TipoUsuario.MEDICO, "Neurología", "LIC888", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medico); //
        Usuario usuarioLogueado = sistemaHospitalario.validarCredenciales("doclog", "logpass"); //
        assertNotNull(usuarioLogueado, "El médico debería loguearse exitosamente.");
        assertEquals(TipoUsuario.MEDICO, usuarioLogueado.getTipoUsuario()); //
    }

    @Test
    void testValidarCredencialesAdministrativoExitoso() {
        Administrativo admin = new Administrativo("Admin Acceso", "999999999", Sexo.MASCULINO, 50, "adminacc", "accadmin", TipoUsuario.ADMINISTRADOR, "Gerente", RolAdministrativo.SUPER_ADMIN);
        sistemaHospitalario.registrarAdministrativo(admin); //
        Usuario usuarioLogueado = sistemaHospitalario.validarCredenciales("adminacc", "accadmin"); //
        assertNotNull(usuarioLogueado, "El administrativo debería loguearse exitosamente.");
        assertEquals(TipoUsuario.ADMINISTRADOR, usuarioLogueado.getTipoUsuario()); //
    }

    @Test
    void testValidarCredencialesUsuarioIncorrecto() {
        assertNull(sistemaHospitalario.validarCredenciales("noexiste", "nopass"), "No debería loguearse con usuario incorrecto."); //
    }

    @Test
    void testValidarCredencialesContrasenaIncorrecta() {
        Paciente paciente = new Paciente("Usuario Pass", "666666666", Sexo.FEMENINO, 22, "userpass", "correcta", TipoUsuario.PACIENTE, "EPS666", null);
        sistemaHospitalario.registrarPaciente(paciente); //
        assertNull(sistemaHospitalario.validarCredenciales("userpass", "incorrecta"), "No debería loguearse con contraseña incorrecta."); //
    }

    // Pruebas para Reportes (Ejemplos básicos)
    @Test
    void testGenerarReporteCitasMedicasVacio() {
        String reporte = sistemaHospitalario.generarReporteCitasMedicas(LocalDate.now().minusDays(5), LocalDate.now().minusDays(4), null, null); // Rango sin citas
        assertTrue(reporte.contains("No se encontraron citas que cumplan los criterios."), "El reporte debería indicar que no hay citas."); //
    }

    @Test
    void testGenerarReporteCitasMedicasConDatos() {
        assertNotNull(citaGlobal, "La cita global debe existir para este test.");
        String reporte = sistemaHospitalario.generarReporteCitasMedicas(LocalDate.now(), LocalDate.now().plusDays(2), null, null); //
        assertFalse(reporte.contains("No se encontraron citas que cumplan los criterios."), "El reporte no debería estar vacío."); //
        assertTrue(reporte.contains("Paciente: " + pacienteGlobal.getNombre()), "El reporte debería contener información de la cita global."); //
        assertTrue(reporte.contains("Total de Citas Encontradas: 1"), "El resumen del reporte debería mostrar 1 cita (la global)."); //
    }

    @Test
    void testObtenerMedicosDisponibles() {
        Medico medico1 = new Medico("Ana Disponible", "7001", Sexo.FEMENINO, 34, "anad", "pass", TipoUsuario.MEDICO, "Cardiología", "LIC7001", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medico1); //
        sistemaHospitalario.registrarHorarioDisponibilidad(medico1.getCedula(), DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)); //

        Medico medico2 = new Medico("Luis Ocupado", "7002", Sexo.MASCULINO, 45, "luiso", "pass", TipoUsuario.MEDICO, "Cardiología", "LIC7002", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medico2); //
        sistemaHospitalario.registrarHorarioDisponibilidad(medico2.getCedula(), DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)); //

        Paciente pacienteParaOcupar = new Paciente("Carlos Paciente", "7003", Sexo.MASCULINO, 50, "carlosp", "pass", TipoUsuario.PACIENTE, "EPS7003", null);
        sistemaHospitalario.registrarPaciente(pacienteParaOcupar); //
        Sala salaCardio = new Sala( "CARDIO-10", TipoSala.CONSULTORIO, 1);
        sistemaHospitalario.agregarSala(salaCardio); //
        sistemaHospitalario.solicitarCitaMedica(pacienteParaOcupar.getCedula(), medico2.getCedula(), LocalDate.now().with(DayOfWeek.MONDAY), LocalTime.of(10, 30), "Test Ocupacion"); //


        LinkedList<Medico> disponiblesCardio = sistemaHospitalario.obtenerMedicosDisponibles(LocalDate.now().with(DayOfWeek.MONDAY), LocalTime.of(10, 30), "Cardiología"); //
        assertEquals(1, disponiblesCardio.size(), "Debería haber 1 médico cardiólogo disponible (Ana).");
        assertEquals("Ana Disponible", disponiblesCardio.getFirst().getNombre());

        LinkedList<Medico> disponiblesGeneral = sistemaHospitalario.obtenerMedicosDisponibles(LocalDate.now().with(DayOfWeek.MONDAY), LocalTime.of(8, 0), "Medicina General"); //
        assertTrue(disponiblesGeneral.isEmpty(), "No deberían haber médicos de Medicina General disponibles si no se registraron para ese horario/especialidad.");
    }

    @Test
    void testRegistrarHorarioYConsultar() {
        Medico medico = new Medico("Horario Test", "445566", Sexo.MASCULINO, 40, "horariot", "test", TipoUsuario.MEDICO, "General", "LICHT", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medico); //

        assertTrue(sistemaHospitalario.registrarHorarioDisponibilidad(medico.getCedula(), DayOfWeek.FRIDAY, LocalTime.of(14, 0), LocalTime.of(18, 0))); //
        LinkedList<HorarioDisponibilidad> horarios = sistemaHospitalario.consultarHorariosDisponibilidad(medico.getCedula()); //
        assertNotNull(horarios);
        assertEquals(1, horarios.size());
        assertEquals(DayOfWeek.FRIDAY, horarios.getFirst().getDiaSemana()); //
    }

    @Test
    void testActualizarHorario() {
        Medico medico = new Medico("Horario Upd", "778899", Sexo.FEMENINO, 33, "horariou", "testupd", TipoUsuario.MEDICO, "Pediatría", "LIC HU", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medico); //
        sistemaHospitalario.registrarHorarioDisponibilidad(medico.getCedula(), DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(12, 0)); //
        HorarioDisponibilidad horarioOriginal = sistemaHospitalario.consultarHorariosDisponibilidad(medico.getCedula()).getFirst(); //

        assertTrue(sistemaHospitalario.actualizarHorarioDisponibilidad(medico.getCedula(), horarioOriginal.getIdHorario(), DayOfWeek.TUESDAY, LocalTime.of(9,0), LocalTime.of(13,0))); //
        HorarioDisponibilidad horarioActualizado = sistemaHospitalario.consultarHorariosDisponibilidad(medico.getCedula()).getFirst(); //
        assertEquals(LocalTime.of(9,0), horarioActualizado.getHoraInicio()); //
    }

    @Test
    void testEliminarHorario() {
        Medico medico = new Medico("Horario Del", "112233", Sexo.MASCULINO, 50, "horariod", "testdel", TipoUsuario.MEDICO, "Ortopedia", "LIC HD", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medico); //
        sistemaHospitalario.registrarHorarioDisponibilidad(medico.getCedula(), DayOfWeek.WEDNESDAY, LocalTime.of(10,0), LocalTime.of(16,0)); //
        HorarioDisponibilidad horario = sistemaHospitalario.consultarHorariosDisponibilidad(medico.getCedula()).getFirst(); //

        assertTrue(sistemaHospitalario.eliminarHorarioDisponibilidad(medico.getCedula(), horario.getIdHorario())); //
        assertTrue(sistemaHospitalario.consultarHorariosDisponibilidad(medico.getCedula()).isEmpty()); //
    }

    @Test
    void testRegistrarDiagnosticoYTratamiento() {
        assertTrue(sistemaHospitalario.registrarDiagnostico(medicoGlobal.getCedula(), pacienteGlobal.getCedula(), "Gripe Común", LocalDate.now())); //
        HistorialMedico historial = sistemaHospitalario.consultarHistorialMedico(pacienteGlobal.getCedula()); //
        assertNotNull(historial);
        assertEquals(1, historial.getListDiagnosticos().size()); //

        Medicamento paracetamol = new Medicamento("MED001", "Paracetamol", "Tableta 500mg", 500, "Genfar", false); //
        LinkedList<Medicamento> medicamentos = new LinkedList<>();
        medicamentos.add(paracetamol);

        assertTrue(sistemaHospitalario.registrarTratamiento(medicoGlobal.getCedula(), pacienteGlobal.getCedula(), LocalDate.now(), LocalDate.now().plusDays(5), "Reposo y acetaminofén", medicamentos, "1 tableta cada 8 horas")); //
        assertEquals(1, historial.getListTratamientos().size()); //
    }

    //  Nuevas Pruebas para Métodos Restantes

    @Test
    void testRegistrarAdministrativoExitoso() {
        Administrativo admin = new Administrativo("Laura Admin", "3001", Sexo.FEMENINO, 30, "ladmin", "adminpass", TipoUsuario.ADMINISTRADOR, "Coord. General", RolAdministrativo.SUPER_ADMIN);
        assertTrue(sistemaHospitalario.registrarAdministrativo(admin)); //
        assertNotNull(sistemaHospitalario.buscarAdministrativoPorCedula("3001")); //
    }

    @Test
    void testRegistrarAdministrativoCedulaExistente() {
        Administrativo admin1 = new Administrativo("Laura Admin", "3001", Sexo.FEMENINO, 30, "ladmin", "adminpass", TipoUsuario.ADMINISTRADOR, "Coord. General", RolAdministrativo.SUPER_ADMIN);
        sistemaHospitalario.registrarAdministrativo(admin1); //
        Administrativo admin2 = new Administrativo("Pedro Admin", "3001", Sexo.MASCULINO, 40, "padmin", "adminpass2", TipoUsuario.ADMINISTRADOR, "Gestor", RolAdministrativo.GESTOR_PERSONAL);
        assertFalse(sistemaHospitalario.registrarAdministrativo(admin2)); //
    }

    @Test
    void testBuscarUsuarioPorCedula() {
        assertNotNull(sistemaHospitalario.buscarUsuarioPorCedula(pacienteGlobal.getCedula()), "Debería encontrar el paciente global."); //
        assertNotNull(sistemaHospitalario.buscarUsuarioPorCedula(medicoGlobal.getCedula()), "Debería encontrar el médico global."); //
        Administrativo admin = new Administrativo("Test Admin User", "3002", Sexo.MASCULINO, 35, "testadmin", "pass", TipoUsuario.ADMINISTRADOR, "Tester", RolAdministrativo.GESTOR_INFRASTRUCTURA);
        sistemaHospitalario.registrarAdministrativo(admin); //
        assertNotNull(sistemaHospitalario.buscarUsuarioPorCedula("3002"), "Debería encontrar el administrativo registrado."); //
        assertNull(sistemaHospitalario.buscarUsuarioPorCedula("CédulaInexistente"), "No debería encontrar un usuario inexistente."); //
    }

    @Test
    void testBuscarCitaMedicaPorId() {
        assertNotNull(citaGlobal, "La cita global debe estar inicializada.");
        CitaMedica encontrada = sistemaHospitalario.buscarCitaMedicaPorId(citaGlobal.getIdCita()); //
        assertNotNull(encontrada);
        assertEquals(citaGlobal.getIdCita(), encontrada.getIdCita()); //
        assertNull(sistemaHospitalario.buscarCitaMedicaPorId("ID_FALSO")); //
    }

    @Test
    void testActualizarEstadoCita() {
        assertNotNull(citaGlobal, "La cita global debe estar inicializada.");
        assertTrue(sistemaHospitalario.actualizarEstadoCita(citaGlobal.getIdCita(), EstadoCita.CONFIRMADA)); //
        assertEquals(EstadoCita.CONFIRMADA, citaGlobal.getEstadoCita()); //

        assertTrue(sistemaHospitalario.actualizarEstadoCita(citaGlobal.getIdCita(), EstadoCita.FINALIZADA)); //
        assertEquals(EstadoCita.FINALIZADA, citaGlobal.getEstadoCita()); //

        assertFalse(sistemaHospitalario.actualizarEstadoCita("ID_FALSO", EstadoCita.CANCELADA)); //
    }

    @Test
    void testNotificarCitaMedica() {
        assertNotNull(citaGlobal, "La cita global debe estar inicializada.");
        String notificacion = sistemaHospitalario.notificarCitaMedica(citaGlobal.getIdCita()); //
        assertNotNull(notificacion);
        assertTrue(notificacion.contains(pacienteGlobal.getNombre())); //
        assertTrue(notificacion.contains(medicoGlobal.getNombre())); //
        assertTrue(notificacion.contains(citaGlobal.getEstadoCita().toString())); //

        String notificacionError = sistemaHospitalario.notificarCitaMedica("ID_FALSO"); //
        assertTrue(notificacionError.contains("Error: Cita con ID ID_FALSO no encontrada.")); //
    }

    @Test
    void testObtenerHistorialPacienteParaMedicoConPermiso() {
        assertNotNull(citaGlobal, "La cita global debe existir para esta prueba.");
        HistorialMedico historial = sistemaHospitalario.obtenerHistorialPacienteParaMedico(medicoGlobal.getCedula(), pacienteGlobal.getCedula()); //
        assertNotNull(historial);
        assertEquals(pacienteGlobal.getHistorialMedico().getIdHistorial(), historial.getIdHistorial()); //
    }

    @Test
    void testObtenerHistorialPacienteParaMedicoSinPermiso() {
        Medico otroMedico = new Medico("Otro Medico", "2001", Sexo.MASCULINO, 45, "otromed", "pass", TipoUsuario.MEDICO, "Pediatra", "LICOTRO", new LinkedList<>());
        sistemaHospitalario.registrarMedico(otroMedico); //
        HistorialMedico historial = sistemaHospitalario.obtenerHistorialPacienteParaMedico(otroMedico.getCedula(), pacienteGlobal.getCedula()); //
        assertNull(historial, "No debería obtener historial si el médico no ha atendido al paciente.");
    }

    @Test
    void testBuscarSalaDisponible() {
        LocalDate fechaBusqueda = LocalDate.now().plusDays(5);
        LocalTime horaBusqueda = LocalTime.of(14, 0);

        Sala disponible = sistemaHospitalario.buscarSalaDisponible(fechaBusqueda, horaBusqueda, TipoSala.CONSULTORIO); //
        assertNotNull(disponible);
        assertEquals(TipoSala.CONSULTORIO, disponible.getTipoSala()); //
        assertTrue(disponible.isEstaDisponible()); //

        sistemaHospitalario.solicitarCitaMedica(pacienteGlobal.getCedula(), medicoGlobal.getCedula(), fechaBusqueda, horaBusqueda, "Ocupar Sala"); //

        Sala deberiaSerNula = sistemaHospitalario.buscarSalaDisponible(fechaBusqueda, horaBusqueda, TipoSala.CONSULTORIO); //
        if (sistemaHospitalario.getListSalas().stream().filter(s -> s.getTipoSala() == TipoSala.CONSULTORIO).count() == 1) {
            assertNull(deberiaSerNula, "No debería encontrar sala si la única está ocupada.");
        }

        Sala noHayTipo = sistemaHospitalario.buscarSalaDisponible(fechaBusqueda, horaBusqueda, TipoSala.QUIROFANO); //
        assertNull(noHayTipo, "No debería encontrar sala si no hay de ese tipo (asumiendo que no se registró ninguna de QUIROFANO).");
    }

    @Test
    void testAsignarPacienteAMedicoDisponible() {
        Paciente nuevoPaciente = new Paciente("Paciente Asignar", "1001", Sexo.FEMENINO, 22, "pasignar", "pass", TipoUsuario.PACIENTE, "EPSASG", null);
        sistemaHospitalario.registrarPaciente(nuevoPaciente); //

        LocalDate fechaAsignacion = LocalDate.now().plusDays(1);
        LocalTime horaAsignacion = LocalTime.of(11, 0);

        if (medicoGlobal.getListHorariosDisponibilidad().stream().noneMatch(h -> h.getDiaSemana() == fechaAsignacion.getDayOfWeek() &&
                !horaAsignacion.isBefore(h.getHoraInicio()) && horaAsignacion.isBefore(h.getHoraFin()))) {
            sistemaHospitalario.registrarHorarioDisponibilidad(medicoGlobal.getCedula(), fechaAsignacion.getDayOfWeek(), horaAsignacion.minusHours(1), horaAsignacion.plusHours(1)); //
        }


        CitaMedica asignada = sistemaHospitalario.asignarPacienteAMedicoDisponible(nuevoPaciente.getCedula(), medicoGlobal.getEspecialidad(), fechaAsignacion, horaAsignacion, "Asignación Directa"); //
        assertNotNull(asignada);
        assertEquals(nuevoPaciente.getCedula(), asignada.getPaciente().getCedula()); //
        assertEquals(medicoGlobal.getCedula(), asignada.getMedico().getCedula()); //
        assertEquals(fechaAsignacion, asignada.getFecha()); //
        assertEquals(horaAsignacion, asignada.getHora()); //
    }

    @Test
    void testAsignarPacienteAMedicoNoDisponible() {
        Paciente nuevoPaciente = new Paciente("Paciente No Asignar", "1002", Sexo.MASCULINO, 50, "pnoasignar", "pass", TipoUsuario.PACIENTE, "EPSNOASG", null);
        sistemaHospitalario.registrarPaciente(nuevoPaciente); //

        CitaMedica noAsignada = sistemaHospitalario.asignarPacienteAMedicoDisponible(nuevoPaciente.getCedula(), medicoGlobal.getEspecialidad(), citaGlobal.getFecha(), citaGlobal.getHora(), "Intento Fallido"); //
        assertNull(noAsignada, "No debería asignarse si el médico ya está ocupado.");
    }


    @Test
    void testConsultarCitasFuturasDeMedico() {
        assertNotNull(citaGlobal, "Cita global debe existir.");
        LinkedList<CitaMedica> futuras = sistemaHospitalario.consultarCitasFuturasDeMedico(medicoGlobal.getCedula()); //
        assertNotNull(futuras);
        assertFalse(futuras.isEmpty());
        assertTrue(futuras.stream().anyMatch(c -> c.getIdCita().equals(citaGlobal.getIdCita()))); //

        sistemaHospitalario.cancelarCitaMedica(citaGlobal.getIdCita()); //
        futuras = sistemaHospitalario.consultarCitasFuturasDeMedico(medicoGlobal.getCedula()); //
        assertTrue(futuras.stream().noneMatch(c -> c.getIdCita().equals(citaGlobal.getIdCita()) && c.getEstadoCita() != EstadoCita.CANCELADA)); //
    }

    @Test
    void testConsultarCitasPorEstado() {
        assertNotNull(citaGlobal, "Cita global debe existir.");
        citaGlobal.setEstadoCita(EstadoCita.PENDIENTE);
        LinkedList<CitaMedica> pendientes = sistemaHospitalario.consultarCitasPorEstado(EstadoCita.PENDIENTE); //
        assertNotNull(pendientes);
        assertTrue(pendientes.stream().anyMatch(c -> c.getIdCita().equals(citaGlobal.getIdCita()))); //

        sistemaHospitalario.actualizarEstadoCita(citaGlobal.getIdCita(), EstadoCita.CONFIRMADA); //
        LinkedList<CitaMedica> confirmadas = sistemaHospitalario.consultarCitasPorEstado(EstadoCita.CONFIRMADA); //
        assertTrue(confirmadas.stream().anyMatch(c -> c.getIdCita().equals(citaGlobal.getIdCita()))); //
        assertTrue(sistemaHospitalario.consultarCitasPorEstado(EstadoCita.PENDIENTE).stream().noneMatch(c -> c.getIdCita().equals(citaGlobal.getIdCita()))); //
    }

    @Test
    void testObtenerMedicosPorEspecialidad() {
        Medico cardiologo1 = new Medico("Cardio Uno", "CRD01", Sexo.MASCULINO, 50, "card1", "p", TipoUsuario.MEDICO, "Cardiología", "LICCRD1", null);
        Medico cardiologo2 = new Medico("Cardio Dos", "CRD02", Sexo.FEMENINO, 40, "card2", "p", TipoUsuario.MEDICO, "Cardiología", "LICCRD2", null);
        Medico pediatra = new Medico("Pedia Uno", "PED01", Sexo.MASCULINO, 35, "ped1", "p", TipoUsuario.MEDICO, "Pediatría", "LICPED1", null);
        sistemaHospitalario.registrarMedico(cardiologo1); //
        sistemaHospitalario.registrarMedico(cardiologo2); //
        sistemaHospitalario.registrarMedico(pediatra); //

        LinkedList<Medico> cardiologos = sistemaHospitalario.obtenerMedicosPorEspecialidad("Cardiología"); //
        assertEquals(2, cardiologos.size());
        assertTrue(cardiologos.stream().allMatch(m -> m.getEspecialidad().equals("Cardiología"))); //

        LinkedList<Medico> neurologos = sistemaHospitalario.obtenerMedicosPorEspecialidad("Neurología"); //
        assertTrue(neurologos.isEmpty());
    }

    @Test
    void testGenerarReporteOcupacionSalas() {
        assertNotNull(citaGlobal, "Cita global debe existir.");
        LocalDate fechaReporte = citaGlobal.getFecha(); //
        String reporte = sistemaHospitalario.generarReporteOcupacionSalas(fechaReporte, 30); //

        assertNotNull(reporte);
        assertTrue(reporte.contains("REPORTE DE OCUPACIÓN DE SALAS (CONSULTORIOS)")); //
        assertTrue(reporte.contains("Sala " + salaGlobal.getNumeroSala())); //
        assertTrue(reporte.contains("Citas Agendadas: 1"), "Debería mostrar 1 cita para la sala global en esa fecha."); //

        String reporteSinCitas = sistemaHospitalario.generarReporteOcupacionSalas(LocalDate.now().minusDays(10), 30); //
        assertTrue(reporteSinCitas.contains("Citas Agendadas: 0"), "Debería mostrar 0 citas para una fecha sin actividad."); //

        SistemaHospitalario shSinConsultorios = new SistemaHospitalario("Test Sin Salas", "000");
        String reporteSinConsult = shSinConsultorios.generarReporteOcupacionSalas(LocalDate.now(), 30); //
        assertTrue(reporteSinConsult.contains("No hay salas de tipo CONSULTORIO registradas"), "Reporte debe indicar falta de consultorios."); //
    }

}