package co.edu.uniquindio.finalproyect.model;

public class Administrativo extends Usuario implements iCitaMedicaCRUD{
    private String cargo; // Ej: "Jefe de Sistemas", "Gerente de Operaciones"
    private RolAdministrativo rol; // Un enum para especificar el nivel de rol administrativo

    public Administrativo(String nombre, String cedula, Sexo sexo, int edad, String nombreUsuario, String contrasena, TipoUsuario tipoUsuario, String cargo, RolAdministrativo rol) {
        super(nombre, cedula, sexo, edad, nombreUsuario, contrasena, tipoUsuario);
        this.cargo = cargo;
        this.rol = rol;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public RolAdministrativo getRol() {
        return rol;
    }

    public void setRol(RolAdministrativo rol) {
        this.rol = rol;
    }
}
