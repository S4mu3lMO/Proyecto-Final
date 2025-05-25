package co.edu.uniquindio.finalproyect.model;

public class Sala {
    private String idSala; // Identificador único de la sala (ej. "S001", "C-205")
    private String numeroSala; // Número o nombre visible de la sala
    private TipoSala tipo; // Tipo de sala (ej. CONSULTORIO, QUIROFANO, LABORATORIO, ESPERA)
    private int capacidad; // Número máximo de personas (si aplica)
    private boolean estaDisponible; // Estado actual de disponibilidad de la sala

    public Sala(String idSala, String numeroSala, TipoSala tipo, int capacidad, boolean estaDisponible) {
        this.idSala = idSala;
        this.numeroSala = numeroSala;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.estaDisponible = estaDisponible;
    }

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }

    public String getNumeroSala() {
        return numeroSala;
    }

    public void setNumeroSala(String numeroSala) {
        this.numeroSala = numeroSala;
    }

    public TipoSala getTipoSala() {
        return tipo;
    }

    public void setTipoSala(TipoSala tipo) {
        this.tipo = tipo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean isEstaDisponible() {
        return estaDisponible;
    }

    public void setEstaDisponible(boolean estaDisponible) {
        this.estaDisponible = estaDisponible;
    }
}
