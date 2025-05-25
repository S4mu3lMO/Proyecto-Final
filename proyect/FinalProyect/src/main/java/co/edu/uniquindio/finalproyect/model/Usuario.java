package co.edu.uniquindio.finalproyect.model;

import java.util.LinkedList;

public abstract class Usuario extends Persona {
    protected String nombreUsuario;
    protected String contrasena;
    protected TipoUsuario tipoUsuario;

    public Usuario(String nombre, String cedula, Sexo sexo, int edad, String nombreUsuario, String contrasena, TipoUsuario tipoUsuario) {
        super(nombre, cedula, sexo, edad);
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.tipoUsuario = tipoUsuario;

    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
