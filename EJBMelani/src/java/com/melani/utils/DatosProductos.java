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
public class DatosProductos {
    private long idproducto;
    private String descripcion;
    private String codproducto;
    private double preciounitario;
    private int cantidadinicial;
    private int cantidaddisponible;
    private String fechaCarga;
    private int idusuario;
    private byte[] img;

    /**
     *
     * @return
     */
    public long getIdproducto() {
        return idproducto;
    }

    /**
     *
     * @return
     */
    public int getIdusuario() {
        return idusuario;
    }

    /**
     *
     * @param idusuario
     */
    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    /**
     *
     * @param idproducto
     */
    public void setIdproducto(long idproducto) {
        this.idproducto = idproducto;
    }

    /**
     *
     * @return
     */
    public int getCantidaddisponible() {
        return cantidaddisponible;
    }

    /**
     *
     * @param cantidaddisponible
     */
    public void setCantidaddisponible(int cantidaddisponible) {
        this.cantidaddisponible = cantidaddisponible;
    }

    /**
     *
     * @return
     */
    public int getCantidadinicial() {
        return cantidadinicial;
    }

    /**
     *
     * @param cantidadinicial
     */
    public void setCantidadinicial(int cantidadinicial) {
        this.cantidadinicial = cantidadinicial;
    }

    /**
     *
     * @return
     */
    public String getCodproducto() {
        return codproducto;
    }

    /**
     *
     * @param codproducto
     */
    public void setCodproducto(String codproducto) {
        this.codproducto = codproducto;
    }

    /**
     *
     * @return
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     *
     * @param descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     *
     * @return
     */
    public String getFechaCarga() {
        return fechaCarga;
    }

    /**
     *
     * @param fechaCarga
     */
    public void setFechaCarga(String fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    /**
     *
     * @return
     */
    public byte[] getImg() {
        return img;
    }

    /**
     *
     * @param img
     */
    public void setImg(byte[] img) {
        this.img = img;
    }

    /**
     *
     * @return
     */
    public double getPreciounitario() {
        return preciounitario;
    }

    /**
     *
     * @param preciounitario
     */
    public void setPreciounitario(double preciounitario) {
        this.preciounitario = preciounitario;
    }
}
