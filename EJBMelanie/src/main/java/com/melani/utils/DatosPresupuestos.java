package com.melani.utils;

public class DatosPresupuestos {
    private int idpresupuesto;
    private String observaciones;
    private int id_usuario_expidio;
    private String fvalidez;
    private String fpresupuesto;
    private Double total;    
    private Double totalapagar;
    private String nombre;
    private String apellido;
    private Double iva;
    private Double porc_descuento_total;
    private DetallesPresupuesto detallepresupuesto;
    private Double recargototal;
    private Double porcentajerecargo;
    private Double descuentoresto;

    public String getApellido() {
        return apellido;
    }

    public Double getPorc_descuento_total() {
        return porc_descuento_total;
    }

    public DetallesPresupuesto getDetallepresupuesto() {
        return detallepresupuesto;
    }

    public Double getIva() {
        return iva;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFpresupuesto() {
        return fpresupuesto;
    }

    public String getFvalidez() {
        return fvalidez;
    }

    public int getId_usuario_expidio() {
        return id_usuario_expidio;
    }

    public int getIdpresupuesto() {
        return idpresupuesto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Double getTotal() {
        return total;
    }

    public DetallesPresupuesto getDetallesPresupuesto(){
        return detallepresupuesto;
    }

    public Double getTotalapagar() {
        return totalapagar;
    }

    public Double getDescuentoresto() {
        return descuentoresto;
    }

    public Double getPorcentajerecargo() {
        return porcentajerecargo;
    }

    public Double getRecargototal() {
        return recargototal;
    }
}