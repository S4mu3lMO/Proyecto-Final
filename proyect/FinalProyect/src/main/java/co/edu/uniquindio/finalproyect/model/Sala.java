package co.edu.uniquindio.finalproyect.model;

import java.util.UUID;

public class Sala {
    private String idSala;
    private String numeroSala;
    private TipoSala tipoSala;
    private int capacidad;
    private boolean estaDisponible;

    public Sala(String numeroSala, TipoSala tipoSala, int capacidad) {
        this.idSala = UUID.randomUUID().toString();
        this.numeroSala = numeroSala;
        this.tipoSala = tipoSala;
        this.capacidad = capacidad;
        this.estaDisponible = true;
    }

    public Sala(String idSala, String numeroSala, TipoSala tipoSala, int capacidad, boolean estaDisponible) {
        this.idSala = idSala;
        this.numeroSala = numeroSala;
        this.tipoSala = tipoSala;
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
        return tipoSala;
    }

    public void setTipoSala(TipoSala tipoSala) {
        this.tipoSala = tipoSala;
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