package com.melani.utils;

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

    public long getIdproducto() {
        return idproducto;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public void setIdproducto(long idproducto) {
        this.idproducto = idproducto;
    }

    public int getCantidaddisponible() {
        return cantidaddisponible;
    }

    public void setCantidaddisponible(int cantidaddisponible) {
        this.cantidaddisponible = cantidaddisponible;
    }

    public int getCantidadinicial() {
        return cantidadinicial;
    }

    public void setCantidadinicial(int cantidadinicial) {
        this.cantidadinicial = cantidadinicial;
    }

    public String getCodproducto() {
        return codproducto;
    }

    public void setCodproducto(String codproducto) {
        this.codproducto = codproducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(String fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public double getPreciounitario() {
        return preciounitario;
    }

    public void setPreciounitario(double preciounitario) {
        this.preciounitario = preciounitario;
    }

   
    
}