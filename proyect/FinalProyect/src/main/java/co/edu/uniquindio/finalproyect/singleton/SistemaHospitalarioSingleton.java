package co.edu.uniquindio.finalproyect.singleton;

import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Administrativo; // Ejemplo de import para inicialización
import co.edu.uniquindio.finalproyect.model.TipoUsuario; // Ejemplo de import para inicialización
import co.edu.uniquindio.finalproyect.model.Sexo; // Ejemplo de import para inicialización
import co.edu.uniquindio.finalproyect.model.RolAdministrativo; // Ejemplo de import para inicialización

/**
 * Clase Singleton para asegurar una única instancia de SistemaHospitalario
 * en toda la aplicación.
 */
public class SistemaHospitalarioSingleton {

    // 1. Instancia estática de la clase Singleton
    private static SistemaHospitalarioSingleton instance;

    // 2. Instancia de la clase principal de la lógica de negocio (tu "cerebro")
    private SistemaHospitalario sistemaHospitalario;

    // Opcional: Para almacenar el usuario que ha iniciado sesión
    private Administrativo usuarioLogueado; // O Usuario, según tu jerarquía

    // 3. Constructor privado para evitar la instanciación externa
    private SistemaHospitalarioSingleton() {
        // Inicializa sistemaHospitalario aquí, o en un método aparte como inicializarSistema()
        // Es una buena práctica inicializarlo aquí si no requiere lógica compleja al inicio
        // sistemaHospitalario = new SistemaHospitalario("Hospital Central", "123456789-0");
    }

    // 4. Método estático público para obtener la única instancia del Singleton
    public static SistemaHospitalarioSingleton getInstance() {
        if (instance == null) {
            instance = new SistemaHospitalarioSingleton();
        }
        return instance;
    }

    // 5. Método para obtener la instancia de SistemaHospitalario
    public SistemaHospitalario getSistemaHospitalario() {
        return sistemaHospitalario;
    }

    // 6. Método para inicializar o cargar datos del sistema (opcional pero muy útil)
    // Este método se llamaría una única vez al inicio de tu aplicación (ej. en App.start())
    public void inicializarSistema() {
        if (sistemaHospitalario == null) {
            sistemaHospitalario = new SistemaHospitalario("Hospital UniQuindio", "900123456-7");

            // Aquí puedes:
            // a) Cargar datos de persistencia (archivos, base de datos)
            // b) Crear datos de prueba para desarrollo

            // Ejemplo de datos de prueba:
            // Asegúrate de que tu clase SistemaHospitalario tenga métodos para agregar estos usuarios
            // y que los constructores de tus clases de modelo sean correctos.

            // Ejemplo de Administrativo
            sistemaHospitalario.registrarAdministrativo(
                    new Administrativo("Administrador", "1000000000", Sexo.MASCULINO, 35,
                            "admin", "123", TipoUsuario.ADMINISTRADOR,
                            "Gerente de TI", RolAdministrativo.SUPER_ADMIN)
            );

            // Ejemplo de Medico
            // sistemaHospitalario.registrarMedico(
            //     new Medico("Dr. Juan Pérez", "1000000001", Sexo.MASCULINO, 45,
            //                "jperez", "password123", TipoUsuario.MEDICO,
            //                "Cardiología", "MD12345", new LinkedList<>())
            // );

            // Ejemplo de Paciente
            // sistemaHospitalario.registrarPaciente(
            //     new Paciente("María López", "1000000002", Sexo.FEMENINO, 30,
            //                  "maria.l", "pass456", TipoUsuario.PACIENTE,
            //                  "ABC12345")
            // );

            System.out.println("Sistema Hospitalario inicializado y listo para usar.");
        }
    }

    // Opcional: Métodos para manejar el usuario logueado
    public Administrativo getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Administrativo usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    public void cerrarSesion() {
        this.usuarioLogueado = null;
    }
}