package co.edu.uniquindio.proyectofinal.model;

public class Medicamento {
    private String idMedicamento;
    private String nombreComercial;
    private String presentacion; // Ej: "Tabletas", "Jarabe", "Inyección"
    private double dosisMiligramos; // Dosis por unidad
    private String fabricante;
    private boolean requiereReceta;

    public Medicamento(String idMedicamento, String nombreComercial, String presentacion, double dosisMiligramos, String fabricante, boolean requiereReceta) {
        this.idMedicamento = idMedicamento;
        this.nombreComercial = nombreComercial;
        this.presentacion = presentacion;
        this.dosisMiligramos = dosisMiligramos;
        this.fabricante = fabricante;
        this.requiereReceta = requiereReceta;
    }

    public String getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(String idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public double getDosisMiligramos() {
        return dosisMiligramos;
    }

    public void setDosisMiligramos(double dosisMiligramos) {
        this.dosisMiligramos = dosisMiligramos;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public boolean isRequiereReceta() {
        return requiereReceta;
    }

    public void setRequiereReceta(boolean requiereReceta) {
        this.requiereReceta = requiereReceta;
    }
}
