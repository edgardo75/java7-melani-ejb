/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.utils;
/**
 *
 * @author Edgardo
 * @version 1.0 Build 5600 Feb 20, 2013
 */
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

    /**
     *
     * @return
     */
    public String getApellido() {
        return apellido;
    }

    /**
     *
     * @return
     */
    public Double getPorc_descuento_total() {
        return porc_descuento_total;
    }

    /**
     *
     * @return
     */
    public DetallesPresupuesto getDetallepresupuesto() {
        return detallepresupuesto;
    }

    /**
     *
     * @return
     */
    public Double getIva() {
        return iva;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @return
     */
    public String getFpresupuesto() {
        return fpresupuesto;
    }

    /**
     *
     * @return
     */
    public String getFvalidez() {
        return fvalidez;
    }

    /**
     *
     * @return
     */
    public int getId_usuario_expidio() {
        return id_usuario_expidio;
    }

    /**
     *
     * @return
     */
    public int getIdpresupuesto() {
        return idpresupuesto;
    }

    /**
     *
     * @return
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     *
     * @return
     */
    public Double getTotal() {
        return total;
    }

    /**
     *
     * @return
     */
    public DetallesPresupuesto getDetallesPresupuesto(){
        return detallepresupuesto;
    }

    /**
     *
     * @return
     */
    public Double getTotalapagar() {
        return totalapagar;
    }

    /**
     *
     * @return
     */
    public Double getDescuentoresto() {
        return descuentoresto;
    }

    /**
     *
     * @return
     */
    public Double getPorcentajerecargo() {
        return porcentajerecargo;
    }

    /**
     *
     * @return
     */
    public Double getRecargototal() {
        return recargototal;
    }
}
