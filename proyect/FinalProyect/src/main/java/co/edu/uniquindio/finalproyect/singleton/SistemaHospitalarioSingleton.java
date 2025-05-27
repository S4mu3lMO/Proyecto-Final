package co.edu.uniquindio.finalproyect.singleton;

import co.edu.uniquindio.finalproyect.model.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

public class SistemaHospitalarioSingleton {

    private static SistemaHospitalarioSingleton instance;
    private SistemaHospitalario sistemaHospitalario;

    private SistemaHospitalarioSingleton() {

    }

    public static SistemaHospitalarioSingleton getInstance() {
        if (instance == null) {
            instance = new SistemaHospitalarioSingleton();
        }
        return instance;
    }

    public SistemaHospitalario getSistemaHospitalario() {
        if (sistemaHospitalario == null) {
            System.out.println("ADVERTENCIA: Se está accediendo a SistemaHospitalario antes de inicializarlo. Se intentará inicializar ahora.");
            inicializarSistema();
        }
        return sistemaHospitalario;
    }

    public void inicializarSistema() {
        if (sistemaHospitalario != null) {
            System.out.println("INFO: Sistema Hospitalario ya había sido inicializado.");
            return;
        }

        System.out.println("Inicializando SistemaHospitalarioSingleton con datos de prueba ampliados...");
        sistemaHospitalario = new SistemaHospitalario("UQ", "900.555.123-0");


        Medicamento ibuprofeno = new Medicamento(UUID.randomUUID().toString().substring(0,8), "Ibuprofeno", "Tableta 600mg", 600, "Genfar", false);
        Medicamento amoxicilina = new Medicamento(UUID.randomUUID().toString().substring(0,8), "Amoxicilina Forte", "Suspensión 250mg/5ml", 250, "MK", true);
        Medicamento paracetamol = new Medicamento(UUID.randomUUID().toString().substring(0,8), "Paracetamol", "Gotas 100mg/ml", 100, "La Santé", false);
        Medicamento losartan = new Medicamento(UUID.randomUUID().toString().substring(0,8), "Losartán Potásico", "Tableta 100mg", 100, "Coaspharma", true);
        Medicamento omeprazol = new Medicamento(UUID.randomUUID().toString().substring(0,8), "Omeprazol", "Cápsula 20mg", 20, "Procaps", true);
        Medicamento loratadina = new Medicamento(UUID.randomUUID().toString().substring(0,8), "Loratadina", "Jarabe 5mg/5ml", 5, "Bayer", false);
        Medicamento metformina = new Medicamento(UUID.randomUUID().toString().substring(0,8), "Metformina", "Tableta 850mg", 850, "Sanofi", true);
        Medicamento salbutamol = new Medicamento(UUID.randomUUID().toString().substring(0,8), "Salbutamol Inhalador", "Inhalador 100mcg/dosis", 100, "GSK", true);

        sistemaHospitalario.getListMedicamentos().addAll(Arrays.asList(ibuprofeno, amoxicilina, paracetamol, losartan, omeprazol, loratadina, metformina, salbutamol));


        Sala consultorio101 = new Sala("C-101", TipoSala.CONSULTORIO, 1);
        Sala consultorio102 = new Sala("C-102", TipoSala.CONSULTORIO, 1);
        Sala consultorio201 = new Sala("C-201", TipoSala.CONSULTORIO, 1);
        Sala consultorio202 = new Sala("C-202", TipoSala.CONSULTORIO, 1);
        Sala consultorio301 = new Sala("C-301", TipoSala.CONSULTORIO, 1);
        Sala quirofanoA = new Sala("Q-A", TipoSala.QUIROFANO, 5);
        Sala quirofanoB = new Sala("Q-B", TipoSala.QUIROFANO, 4);
        Sala laboratorioB = new Sala("L-B", TipoSala.LABORATORIO, 3);
        Sala laboratorioC = new Sala("L-C", TipoSala.LABORATORIO, 5);
        Sala esperaPrincipal = new Sala("ESP-01", TipoSala.ESPERA, 20);
        Sala esperaUrgencias = new Sala("ESP-Urg", TipoSala.ESPERA, 15);


        sistemaHospitalario.agregarSala(consultorio101);
        sistemaHospitalario.agregarSala(consultorio102);
        sistemaHospitalario.agregarSala(consultorio201);
        sistemaHospitalario.agregarSala(consultorio202);
        sistemaHospitalario.agregarSala(consultorio301);
        sistemaHospitalario.agregarSala(quirofanoA);
        sistemaHospitalario.agregarSala(quirofanoB);
        sistemaHospitalario.agregarSala(laboratorioB);
        sistemaHospitalario.agregarSala(laboratorioC);
        sistemaHospitalario.agregarSala(esperaPrincipal);
        sistemaHospitalario.agregarSala(esperaUrgencias);




        // Administradores (5)
        Administrativo admin1 = new Administrativo("Alicia Administradora", "1000000000", Sexo.FEMENINO, 38, "admin", "admin123", TipoUsuario.ADMINISTRADOR, "Jefe de Admisiones", RolAdministrativo.SUPER_ADMIN);
        Administrativo admin2 = new Administrativo("Bernardo Botero", "1000000001", Sexo.MASCULINO, 45, "bbo", "ber456", TipoUsuario.ADMINISTRADOR, "Gestor de Personal Médico", RolAdministrativo.GESTOR_PERSONAL);
        Administrativo admin3 = new Administrativo("Carolina Cruz", "1000000002", Sexo.FEMENINO, 32, "ccr", "car789", TipoUsuario.ADMINISTRADOR, "Coordinadora de Salas", RolAdministrativo.GESTOR_INFRASTRUCTURA);
        Administrativo admin4 = new Administrativo("David Duarte", "1000000003", Sexo.MASCULINO, 50, "ddu", "dav101", TipoUsuario.ADMINISTRADOR, "Analista de Reportes", RolAdministrativo.GESTOR_PERSONAL);
        Administrativo admin5 = new Administrativo("Elena Estrada", "1000000004", Sexo.FEMENINO, 29, "ees", "ele202", TipoUsuario.ADMINISTRADOR, "Soporte TI", RolAdministrativo.RECURSOS_HUMANOS);
        sistemaHospitalario.registrarAdministrativo(admin1);
        sistemaHospitalario.registrarAdministrativo(admin2);
        sistemaHospitalario.registrarAdministrativo(admin3);
        sistemaHospitalario.registrarAdministrativo(admin4);
        sistemaHospitalario.registrarAdministrativo(admin5);

        // Médicos (5)
        Medico medicoCardiologo = new Medico("Carlos Cárdenas", "2000000001", Sexo.MASCULINO, 45, "ccardenas", "med123", TipoUsuario.MEDICO, "Cardiología", "MD-CAR-001", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medicoCardiologo);
        sistemaHospitalario.registrarHorarioDisponibilidad(medicoCardiologo.getCedula(), DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 30));
        sistemaHospitalario.registrarHorarioDisponibilidad(medicoCardiologo.getCedula(), DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(18, 0));

        Medico medicoPediatra = new Medico("Laura López", "2000000002", Sexo.FEMENINO, 39, "llopez", "med456", TipoUsuario.MEDICO, "Pediatría", "MD-PED-002", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medicoPediatra);
        sistemaHospitalario.registrarHorarioDisponibilidad(medicoPediatra.getCedula(), DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
        sistemaHospitalario.registrarHorarioDisponibilidad(medicoPediatra.getCedula(), DayOfWeek.THURSDAY, LocalTime.of(8, 0), LocalTime.of(13, 0));

        Medico medicoGeneral = new Medico("Mario Moreno", "2000000003", Sexo.MASCULINO, 52, "mmoreno", "med789", TipoUsuario.MEDICO, "Medicina General", "MD-GEN-003", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medicoGeneral);
        sistemaHospitalario.registrarHorarioDisponibilidad(medicoGeneral.getCedula(), DayOfWeek.FRIDAY, LocalTime.of(7,0), LocalTime.of(15,0));
        sistemaHospitalario.registrarHorarioDisponibilidad(medicoGeneral.getCedula(), DayOfWeek.SATURDAY, LocalTime.of(8,0), LocalTime.of(12,0)); // Horario de Sábado

        Medico medicoDermatologa = new Medico("Diana Durán", "2000000004", Sexo.FEMENINO, 42, "dduran", "derm12", TipoUsuario.MEDICO, "Dermatología", "MD-DER-004", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medicoDermatologa);
        sistemaHospitalario.registrarHorarioDisponibilidad(medicoDermatologa.getCedula(), DayOfWeek.MONDAY, LocalTime.of(10,0), LocalTime.of(18,0));
        sistemaHospitalario.registrarHorarioDisponibilidad(medicoDermatologa.getCedula(), DayOfWeek.THURSDAY, LocalTime.of(14,0), LocalTime.of(19,0)); // Horario tarde

        Medico medicoOrtopedista = new Medico("Oscar Osorio", "2000000005", Sexo.MASCULINO, 48, "oosorio", "orto34", TipoUsuario.MEDICO, "Ortopedia", "MD-ORT-005", new LinkedList<>());
        sistemaHospitalario.registrarMedico(medicoOrtopedista);
        sistemaHospitalario.registrarHorarioDisponibilidad(medicoOrtopedista.getCedula(), DayOfWeek.TUESDAY, LocalTime.of(7,30), LocalTime.of(12,30));
        sistemaHospitalario.registrarHorarioDisponibilidad(medicoOrtopedista.getCedula(), DayOfWeek.WEDNESDAY, LocalTime.of(8,0), LocalTime.of(16,0));


        // Pacientes (5)
        Paciente pacienteJuan = new Paciente("Juan Rodríguez", "3000000001", Sexo.MASCULINO, 30, "jrodriguez", "pac123", TipoUsuario.PACIENTE, "EPS001-JR", null);
        sistemaHospitalario.registrarPaciente(pacienteJuan);

        Paciente pacienteAna = new Paciente("Ana Vélez", "3000000002", Sexo.FEMENINO, 25, "avelez", "pac456", TipoUsuario.PACIENTE, "EPS002-AV", null);
        sistemaHospitalario.registrarPaciente(pacienteAna);

        Paciente pacienteLuis = new Paciente("Luis Arias", "3000000003", Sexo.MASCULINO, 42, "larias", "pac789", TipoUsuario.PACIENTE, "EPS003-LA", null);
        sistemaHospitalario.registrarPaciente(pacienteLuis);

        Paciente pacienteSofia = new Paciente("Sofía Soto", "3000000004", Sexo.FEMENINO, 22, "ssoto", "sofi12", TipoUsuario.PACIENTE, "EPS004-SS", null);
        sistemaHospitalario.registrarPaciente(pacienteSofia);

        Paciente pacientePedro = new Paciente("Pedro Páez", "3000000005", Sexo.MASCULINO, 55, "ppaez", "pedro34", TipoUsuario.PACIENTE, "EPS005-PP", null);
        sistemaHospitalario.registrarPaciente(pacientePedro);


        sistemaHospitalario.solicitarCitaMedica(pacienteJuan.getCedula(), medicoCardiologo.getCedula(), LocalDate.now().plusDays(7), LocalTime.of(9, 30), "Revisión Cardiológica Anual");
        sistemaHospitalario.solicitarCitaMedica(pacienteAna.getCedula(), medicoPediatra.getCedula(), LocalDate.now().plusDays(3), LocalTime.of(14, 0), "Vacunación y control pediátrico");
        sistemaHospitalario.solicitarCitaMedica(pacienteLuis.getCedula(), medicoGeneral.getCedula(), LocalDate.now().plusDays(5), LocalTime.of(10, 0), "Consulta por resfriado fuerte");
        sistemaHospitalario.solicitarCitaMedica(pacienteSofia.getCedula(), medicoDermatologa.getCedula(), LocalDate.now().plusDays(10), LocalTime.of(15,0), "Revisión de lunares");
        sistemaHospitalario.solicitarCitaMedica(pacientePedro.getCedula(), medicoOrtopedista.getCedula(), LocalDate.now().plusDays(12), LocalTime.of(8,30), "Dolor de rodilla persistente");


        CitaMedica citaCancelada = new CitaMedica(UUID.randomUUID().toString().substring(0,10), pacienteJuan, medicoGeneral, LocalDate.now().plusDays(2), LocalTime.of(11,0), consultorio201, "Consulta general", EstadoCita.PENDIENTE);
        sistemaHospitalario.getListCitasMedicas().add(citaCancelada);
        if(medicoGeneral.getListCitasMedicas()!=null) medicoGeneral.getListCitasMedicas().add(citaCancelada);
        sistemaHospitalario.cancelarCitaMedica(citaCancelada.getIdCita());


        CitaMedica citaPasadaJuan = new CitaMedica(UUID.randomUUID().toString().substring(0,10), pacienteJuan, medicoCardiologo, LocalDate.now().minusDays(30), LocalTime.of(10,0), consultorio101, "Dolor de cabeza y mareos", EstadoCita.FINALIZADA);
        sistemaHospitalario.getListCitasMedicas().add(citaPasadaJuan);
        if(medicoCardiologo.getListCitasMedicas() != null) medicoCardiologo.getListCitasMedicas().add(citaPasadaJuan);


        CitaMedica citaPasadaAna = new CitaMedica(UUID.randomUUID().toString().substring(0,10), pacienteAna, medicoPediatra, LocalDate.now().minusDays(45), LocalTime.of(11,30), consultorio102, "Fiebre alta niño", EstadoCita.FINALIZADA);
        sistemaHospitalario.getListCitasMedicas().add(citaPasadaAna);
        if(medicoPediatra.getListCitasMedicas() != null) medicoPediatra.getListCitasMedicas().add(citaPasadaAna);



        if (pacienteJuan.getHistorialMedico() != null) {
            sistemaHospitalario.registrarDiagnostico(medicoCardiologo.getCedula(), pacienteJuan.getCedula(), "Episodios de vértigo postural benigno. Hipertensión arterial Etapa 1.", LocalDate.now().minusDays(30));
            LinkedList<Medicamento> medsJuan = new LinkedList<>(Arrays.asList(losartan, paracetamol));
            sistemaHospitalario.registrarTratamiento(medicoCardiologo.getCedula(), pacienteJuan.getCedula(), LocalDate.now().minusDays(30), LocalDate.now().plusMonths(3), "Control de presión arterial con Losartán. Ejercicios de reposicionamiento para vértigo. Paracetamol SOS para dolor.", medsJuan, "Losartán 50mg OD. Paracetamol 500mg c/8h PRN dolor.");
        }

        if (pacienteAna.getHistorialMedico() != null) {
            sistemaHospitalario.registrarDiagnostico(medicoPediatra.getCedula(), pacienteAna.getCedula(), "Otitis media aguda en oído derecho. Se descarta complicación.", LocalDate.now().minusDays(45));
            LinkedList<Medicamento> medsAna = new LinkedList<>(Arrays.asList(amoxicilina, ibuprofeno));
            sistemaHospitalario.registrarTratamiento(medicoPediatra.getCedula(), pacienteAna.getCedula(), LocalDate.now().minusDays(45), LocalDate.now().minusDays(45).plusDays(7), "Tratamiento antibiótico y analgésico.", medsAna, "Amoxicilina 250mg/5ml - 5ml cada 8 horas por 7 días. Ibuprofeno suspensión según peso para fiebre/dolor.");
        }

        if (pacienteLuis.getHistorialMedico() != null) {
            sistemaHospitalario.registrarDiagnostico(medicoGeneral.getCedula(), pacienteLuis.getCedula(), "Gripe común con congestión nasal severa.", LocalDate.now().minusDays(2));
            LinkedList<Medicamento> medsLuis = new LinkedList<>(Arrays.asList(paracetamol, loratadina));
            sistemaHospitalario.registrarTratamiento(medicoGeneral.getCedula(), pacienteLuis.getCedula(), LocalDate.now().minusDays(2), LocalDate.now().plusDays(3), "Reposo, hidratación, sintomáticos.", medsLuis, "Paracetamol 500mg c/6h. Loratadina 10mg OD.");
        }

        System.out.println("Sistema Hospitalario inicializado y poblado con datos de prueba ampliados.");
    }


}
