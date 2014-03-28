/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 *
 * @author Edgardo Alvarez™
 * @version 1.0 Build 5600 Feb 20, 2013
 */
@XStreamAlias("telefono")
public class DatosTelefonos {
    //---------------------------------------
    private String numero;
    private String prefijo;
    private EmpresaTelefonia idEmpresaTelefonia;
    private int idcliente;
    private TiposTelefonos tipoTelefono;

    /**
     *
     */
    public DatosTelefonos(){}

    /**
     *
     * @return
     */
    public EmpresaTelefonia getIdEmpresaTelefonia() {
        return idEmpresaTelefonia;
    }

    /**
     *
     * @return
     */
    public int getIdcliente() {
        return idcliente;
    }

    /**
     *
     * @return
     */
    public TiposTelefonos getTipoTelefono() {
        return tipoTelefono;
    }
    //-/--------------------------------------

    /**
     *
     * @return
     */
        public String getNumero(){
        return numero;
    }

    /**
     *
     * @return
     */
    public String getPrefijo(){
        return prefijo;
    }

    /**
     *
     * @return
     */
    public int getIdCliente(){
        return idcliente;
    }

    /**
     *
     */
    public class EmpresaTelefonia{
        private short idempresatelefonia;

        /**
         *
         * @return
         */
        public short getIdempresatelefonia() {
            return idempresatelefonia;
        }
    }

    /**
     *
     */
    public class TiposTelefonos{
        private short idtipotel;

        /**
         *
         * @return
         */
        public short getTipoTelefono() {
        return idtipotel;
    }
    }
}
