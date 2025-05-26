package co.edu.uniquindio.finalproyect.model;

public class Administrativo extends Usuario {
    private String cargo;
    private RolAdministrativo rolAdministrativo;

    public Administrativo(String nombre, String cedula, Sexo sexo, int edad, String nombreUsuario, String contrasena, TipoUsuario tipoUsuario, String cargo, RolAdministrativo rolAdministrativo) {
        super(nombre, cedula, sexo, edad, nombreUsuario, contrasena, tipoUsuario);
        this.cargo = cargo;
        this.rolAdministrativo = rolAdministrativo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public RolAdministrativo getRolAdministrativo() {
        return rolAdministrativo;
    }

    public void setRolAdministrativo(RolAdministrativo rolAdministrativo) {
        this.rolAdministrativo = rolAdministrativo;
    }
}
