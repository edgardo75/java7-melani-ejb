/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.utils;
/**
 *Clase DatosCliente Utilizada para Instanciar mediante libreria Xstream, con
 * el objetivo de Producir una salida de un Objeto Cliente
 * @version 1.0 Build 5600 Feb 20, 2013
 * @author Edgardo Alvarez
 */
public class DatosCliente {
    private String apellido;// variable interna atribute apellido
    private String nombre;// variable interna atribute nombre
    private String email;//variable interna atribute email
    private String observaciones;//variable interna atribute observaciones
    private Integer nrodocu;//variable interna atribute nrodocu
    private Short idtipodocu;//variable interna atribute idtipodocu
    private float totalcompras;//variable interna atribute totalcompras
    private int totalpuntos;//variable interna atribute totalpuntos
    private long idcliente;//variable interna atribute idcliente
    private Generos genero;//variable interna atribute genero
   //Constructor

    /**
     *
     */
        public DatosCliente(){}
 /**
     
     * @return   Este metodo devuelve el apellido de un cliente
     */
    public String getApellido() {
        return apellido;
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
     * @param observaciones
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
/**
     * @param apellido
 */    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    /**
     * @return Este metodo devuelve el email del cliente
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
/**
 * @return Este metodo devuelve el id del Tipo de Documento
 */
    public Short getIdtipodocu() {
        return idtipodocu;
    }
/**
     * @param idtipodocu
 */
    public void setIdtipodocu(Short idtipodocu) {
        this.idtipodocu = idtipodocu;
    }
/**
 * @return Este metodo devuelve el nombre del cliente
 */
    public String getNombre() {
        return nombre;
    }
/**
     * @param nombre
 */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
/**
 * @return Este metodo devuelve el numero de documento
 */
    public Integer getNrodocu() {
        return nrodocu;
    }
/**
     * @param nrodocu
 */
    public void setNrodocu(Integer nrodocu) {
        this.nrodocu = nrodocu;
    }
/**
 * @return Este metodo devuelve el total de compras
 */
    public float getTotalcompras() {
        return totalcompras;
    }
/**
     * @param totalcompras
 */
    public void setTotalcompras(float totalcompras) {
        this.totalcompras = totalcompras;
    }
/**
 * @return Este metodo devuelve el total de puntos del cliente
 */
    public int getTotalpuntos() {
        return totalpuntos;
    }
/**
     * @param totalpuntos
 */
    public void setTotalpuntos(int totalpuntos) {
        this.totalpuntos = totalpuntos;
    }
/*
 * Este metodo devuelve el id de cliente
 */

    /**
     *
     * @return
     */
    
    public long getIdcliente() {
        return idcliente;
    }
/**
     * @param idcliente
 */
    public void setIdcliente(long idcliente) {
        this.idcliente = idcliente;
    }
   /**
 * @return Este metodo trata a la clase Genero
 */
    public Generos getGeneros() {
        return genero;
    }
/**
     * @param genero
 */
    public void setGeneros(Generos genero) {
        this.genero = genero;
    }
//Clase anónima Generos

    /**
     *
     */
    public class Generos{
    private short idgenero;

        /**
         *
         */
        public Generos(){
    }

        /**
         *
         * @param idgenero
         */
        public Generos(short idgenero) {
            this.idgenero = idgenero;
        }

        /**
         *
         * @return
         */
        public short getIdgenero() {
            return idgenero;
        }

        /**
         *
         * @param idgenero
         */
        public void setIdgenero(short idgenero) {
            this.idgenero = idgenero;
        }
    }
}
