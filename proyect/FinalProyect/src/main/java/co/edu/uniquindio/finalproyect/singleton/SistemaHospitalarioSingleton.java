package co.edu.uniquindio.finalproyect.singleton;

import co.edu.uniquindio.finalproyect.model.*; // Importa todas tus clases del modelo
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Clase Singleton para asegurar una única instancia de SistemaHospitalario
 * en toda la aplicación y para inicializarla con datos de prueba.
 */
public class SistemaHospitalarioSingleton {

    private static SistemaHospitalarioSingleton instance;
    private SistemaHospitalario sistemaHospitalario;

    // Opcional: Para almacenar el usuario que ha iniciado sesión globalmente si se necesita.
    // private Usuario usuarioLogueadoGlobal;

    private SistemaHospitalarioSingleton() {
        // El sistema se inicializa en inicializarSistema() para asegurar que se haga una sola vez.
    }

    public static SistemaHospitalarioSingleton getInstance() {
        if (instance == null) {
            instance = new SistemaHospitalarioSingleton();
        }
        return instance;
    }

    public SistemaHospitalario getSistemaHospitalario() {
        // Asegurarse de que el sistema esté inicializado antes de devolverlo.
        if (sistemaHospitalario == null) {
            System.out.println("ADVERTENCIA: Se está accediendo a SistemaHospitalario antes de inicializarlo. Se intentará inicializar ahora.");
            inicializarSistema();
        }
        return sistemaHospitalario;
    }

    /**
     * Inicializa la instancia de SistemaHospitalario y la puebla con datos quemados.
     * Este método debe ser llamado una única vez al inicio de la aplicación (ej. en App.start()).
     */
    public void inicializarSistema() {
        if (sistemaHospitalario == null) {
            System.out.println("Inicializando SistemaHospitalarioSingleton...");
            sistemaHospitalario = new SistemaHospitalario("Hospital UniSalud Central", "900.123.456-7");

            // --- Crear Medicamentos de Ejemplo ---
            Medicamento ibuprofeno = new Medicamento(UUID.randomUUID().toString().substring(0,8), "Ibuprofeno", "Tableta 400mg", 400, "Genfar", false);
            Medicamento amoxicilina = new Medicamento(UUID.randomUUID().toString().substring(0,8), "Amoxicilina", "Cápsula 500mg", 500, "MK", true);
            Medicamento paracetamol = new Medicamento(UUID.randomUUID().toString().substring(0,8), "Paracetamol", "Tableta 500mg", 500, "La Santé", false);
            Medicamento losartan = new Medicamento(UUID.randomUUID().toString().substring(0,8), "Losartán", "Tableta 50mg", 50, "Coaspharma", true);

            sistemaHospitalario.getListMedicamentos().addAll(Arrays.asList(ibuprofeno, amoxicilina, paracetamol, losartan));

            // --- Crear Salas de Ejemplo ---
            Sala consultorio101 = new Sala("C-101", TipoSala.CONSULTORIO, 1);
            Sala consultorio102 = new Sala("C-102", TipoSala.CONSULTORIO, 1);
            Sala quirofanoA = new Sala("Q-A", TipoSala.QUIROFANO, 5);
            Sala laboratorioB = new Sala("L-B", TipoSala.LABORATORIO, 3);
            Sala esperaPrincipal = new Sala("ESP-01", TipoSala.ESPERA, 20);

            sistemaHospitalario.agregarSala(consultorio101);
            sistemaHospitalario.agregarSala(consultorio102);
            sistemaHospitalario.agregarSala(quirofanoA);
            sistemaHospitalario.agregarSala(laboratorioB);
            sistemaHospitalario.agregarSala(esperaPrincipal);

            // --- Crear Usuarios de Ejemplo ---

            // 1. Administrador
            Administrativo admin = new Administrativo("Alicia Administradora", "1000000000", Sexo.FEMENINO, 38,
                    "admin", "admin123", TipoUsuario.ADMINISTRADOR,
                    "Jefe de Admisiones", RolAdministrativo.SUPER_ADMIN);
            sistemaHospitalario.registrarAdministrativo(admin);

            // 2. Médicos
            Medico medico1 = new Medico("Carlos Cárdenas", "2000000001", Sexo.MASCULINO, 45,
                    "ccardenas", "med123", TipoUsuario.MEDICO,
                    "Cardiología", "MD-CAR-001", new LinkedList<>());
            sistemaHospitalario.registrarMedico(medico1);
            sistemaHospitalario.registrarHorarioDisponibilidad(medico1.getCedula(), DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 0));
            sistemaHospitalario.registrarHorarioDisponibilidad(medico1.getCedula(), DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(17, 0));
            sistemaHospitalario.registrarHorarioDisponibilidad(medico1.getCedula(), DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(13, 0));


            Medico medico2 = new Medico("Laura López", "2000000002", Sexo.FEMENINO, 39,
                    "llopez", "med456", TipoUsuario.MEDICO,
                    "Pediatría", "MD-PED-002", new LinkedList<>());
            sistemaHospitalario.registrarMedico(medico2);
            sistemaHospitalario.registrarHorarioDisponibilidad(medico2.getCedula(), DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(16, 0));
            sistemaHospitalario.registrarHorarioDisponibilidad(medico2.getCedula(), DayOfWeek.THURSDAY, LocalTime.of(8, 30), LocalTime.of(12, 30));
            sistemaHospitalario.registrarHorarioDisponibilidad(medico2.getCedula(), DayOfWeek.FRIDAY, LocalTime.of(14, 0), LocalTime.of(18, 0));


            // 3. Pacientes
            Paciente paciente1 = new Paciente("Juan Rodríguez", "3000000001", Sexo.MASCULINO, 30,
                    "jrodriguez", "pac123", TipoUsuario.PACIENTE,
                    "EPS001-12345", null); // El historial se crea en registrarPaciente si es null
            sistemaHospitalario.registrarPaciente(paciente1);

            Paciente paciente2 = new Paciente("Ana Vélez", "3000000002", Sexo.FEMENINO, 25,
                    "avelez", "pac456", TipoUsuario.PACIENTE,
                    "EPS002-67890", null);
            sistemaHospitalario.registrarPaciente(paciente2);

            // --- Crear Citas Médicas de Ejemplo ---
            // Cita para Juan Rodríguez con Dr. Carlos Cárdenas (Cardiología)
            sistemaHospitalario.solicitarCitaMedica(
                    paciente1.getCedula(), medico1.getCedula(),
                    LocalDate.now().plusDays(3).with(DayOfWeek.MONDAY), // Próximo lunes
                    LocalTime.of(9, 0),
                    "Chequeo general, dolor en el pecho."
            );

            // Cita para Ana Vélez con Dra. Laura López (Pediatría) - Asumamos que Ana lleva a un niño
            sistemaHospitalario.solicitarCitaMedica(
                    paciente2.getCedula(), medico2.getCedula(),
                    LocalDate.now().plusDays(5).with(DayOfWeek.TUESDAY), // Próximo martes
                    LocalTime.of(11, 0),
                    "Control de crecimiento niño 2 años."
            );

            // Cita pasada y finalizada para Juan Rodríguez
            String idCitaPasada = UUID.randomUUID().toString();
            CitaMedica citaPasada = new CitaMedica(idCitaPasada, paciente1, medico1,
                    LocalDate.now().minusDays(15), LocalTime.of(10,0),
                    consultorio101, "Dolor de cabeza persistente", EstadoCita.FINALIZADA);
            sistemaHospitalario.getListCitasMedicas().add(citaPasada);
            medico1.agregarListCitasMedicas(citaPasada); // Asegurar que el médico también la tenga

            // --- Añadir Diagnósticos y Tratamientos de Ejemplo al Historial de Juan Rodríguez ---
            if (paciente1.getHistorialMedico() != null) {
                // Diagnóstico para la cita pasada
                sistemaHospitalario.registrarDiagnostico(
                        medico1.getCedula(), paciente1.getCedula(),
                        "Migraña tensional debido a estrés. Se recomienda reposo y analgésicos.",
                        LocalDate.now().minusDays(15) // Fecha del diagnóstico (misma que la cita pasada)
                );

                // Tratamiento para la migraña
                LinkedList<Medicamento> medsMigrana = new LinkedList<>();
                medsMigrana.add(ibuprofeno);
                medsMigrana.add(paracetamol);
                sistemaHospitalario.registrarTratamiento(
                        medico1.getCedula(), paciente1.getCedula(),
                        LocalDate.now().minusDays(15), // Inicio del tratamiento
                        LocalDate.now().minusDays(10), // Fin del tratamiento
                        "Reposo relativo. Tomar Ibuprofeno 400mg cada 8 horas por 3 días si hay dolor. Paracetamol 500mg SOS.",
                        medsMigrana,
                        "Ibuprofeno: 1 tableta c/8h por 3 días. Paracetamol: 1 tableta si es necesario, max 4 al día."
                );
            }

            // --- Añadir un diagnóstico para Ana Vélez (o su hijo)
            if (paciente2.getHistorialMedico() != null) {
                sistemaHospitalario.registrarDiagnostico(
                        medico2.getCedula(), paciente2.getCedula(),
                        "Niño con desarrollo normal para su edad. Leve resfriado común.",
                        LocalDate.now().minusDays(5) // Fecha de un chequeo anterior
                );
                LinkedList<Medicamento> medsResfriado = new LinkedList<>();
                medsResfriado.add(paracetamol); // Dosis pediátrica (esto es solo un ejemplo)
                sistemaHospitalario.registrarTratamiento(
                        medico2.getCedula(), paciente2.getCedula(),
                        LocalDate.now().minusDays(5), LocalDate.now().minusDays(2),
                        "Hidratación abundante. Paracetamol en gotas según peso si hay fiebre.",
                        medsResfriado,
                        "Paracetamol: según peso y edad, cada 6-8 horas SOS."
                );
            }


            System.out.println("Sistema Hospitalario inicializado y poblado con datos de prueba.");
        } else {
            System.out.println("Sistema Hospitalario ya había sido inicializado.");
        }
    }

    // Opcional: Métodos para manejar el usuario logueado globalmente
    /*
    public Usuario getUsuarioLogueadoGlobal() {
        return usuarioLogueadoGlobal;
    }

    public void setUsuarioLogueadoGlobal(Usuario usuarioLogueadoGlobal) {
        this.usuarioLogueadoGlobal = usuarioLogueadoGlobal;
    }

    public void cerrarSesionGlobal() {
        this.usuarioLogueadoGlobal = null;
    }
    */
}
