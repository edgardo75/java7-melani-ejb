/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.utils;
/**
 *
 * @author Edgardo™
 * @version 1.0 Build 5600 Feb 20, 2013
 */
public class DatosDomicilios {
    private long domicilioId;
    private String piso;
    private String manzana;
    private String entrecalleycalle;
    private String sector;
    private String torre;
    private String monoblock;
    private Barrioss barrios;
    private Calless calles;
    private String area;
    private Orientacions orientacion;
    private int numero;
    private int numdepto;
    private Localidadess localidad;
    private String det1ails_homes;
    ////////-------------GETTERS AND SETTERS----------------
    ////----------------------------------
    //Constructor

    /**
     *
     */
        public DatosDomicilios(){}
    /**
        * @return Este metodo retorna el id del Domicilio
    */
    public long getDomicilioId(){
        return domicilioId;
    }
    /**
        * @return Este metodo retorna el piso del domicilio
    */
    public String getPiso(){
        return piso;
    }    
    /**
    * @return Este metodo retorna detalles u observaciones del domicilio
    */
    public String getObservaciones() {
        return det1ails_homes;
    }
    /**
     * @param det1ails_homes
    */
    public void setObservaciones(String det1ails_homes) {
        this.det1ails_homes = det1ails_homes;
    }
    /**
        * @return Este metodo retorna el numero de departamento del domicilio
    */
    public Integer getNumDepto(){
        return numdepto;
    }
    /**
        * @return Este metodo retorna la manzana del domicilio
    */
    public String getManzana(){
        return manzana;
    }
    /**
        * @return Este metodo retorna el entre calles del domicilio
    */
    public String getEntrecalleycalle(){
        return entrecalleycalle;
    }
    /**
        * @return Este metodo retorna sector del domicilio
    */
    public String getSector(){
        return sector;
    }
    /**
        * @return Este metodo retorna la torre o edificio del domicilio
    */
    public String getTorre(){
        return torre;
    }
/**
        * @return Este metodo retorna el area del domicilio
    */
    public String getArea() {
        return area;
    }

    /**
     *
     * @param area
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     *
     * @param numdepto
     */
    public void setNumdepto(Integer numdepto) {
        this.numdepto = numdepto;
    }
    /**
        * @return Este metodo retorna el monoblock del domicilio
    */
    public String getMonoblock(){
        return monoblock;
    }
    /**
        * @return Este metodo retorna el número del domicilio
    */
    public int getNumero(){
        return numero;
    }
    ////----------------------------------

    /**
     *
     * @return
     */
        public Barrioss getBarrio(){
        return barrios;
    }

    /**
     *
     * @return
     */
    public Calless getCalle(){
        return calles;
    }

    /**
     *
     * @return
     */
    public Orientacions getOrientacion(){
        return orientacion;
    }
    /////////////////////--------------------------------------------------

    /**
     *
     * @return
     */
        public Localidadess getLocalidad(){
        return localidad;
    }
    ///----------  --- --------- ----- ----------------------   ------

    /**
     *
     * @param domicilioId
     */
        public void setDomicilioId(int domicilioId){
        this.domicilioId = domicilioId;
    }

    /**
     *
     * @param piso
     */
    public void setPiso(String piso){
        this.piso = piso;
    }

    /**
     *
     * @param manzana
     */
    public void setManzana(String manzana){
        this.manzana = manzana;
    }

    /**
     *
     * @param entrecalleycalle
     */
    public void setEntrecalleycalle(String entrecalleycalle){
        this.entrecalleycalle = entrecalleycalle;
   }

    /**
     *
     * @param sector
     */
    public void setSector(String sector){
        this.sector = sector;
   }

    /**
     *
     * @param torre
     */
    public void setTorre(String torre){
        this.torre = torre;
   }

    /**
     *
     * @param monoblock
     */
    public void setMonoblock(String monoblock){
        this.monoblock = monoblock;
   }
    /////////////////////--------------------------------------------------

    /**
     *
     */
        public class Barrioss{
        private int barrioId;

        /**
         *
         */
        public Barrioss(){}

        /**
         *
         * @return
         */
        public int getBarrioId(){
            return barrioId;
        }
    }

    /**
     *
     */
    public class Calless{

        /**
         *
         */
        public Calless(){}

        /**
         *
         * @param calleId
         */
        public Calless(int calleId) {
            this.calleId = calleId;
        }
        private int calleId;

        /**
         *
         * @return
         */
        public int getCalleId(){
            return calleId;
        }

        /**
         *
         * @param calleId
         */
        public void setCalleId(int calleId){
            this.calleId = calleId;
        }
    }

    /**
     *
     */
    public class Orientacions{
        private long idOrientacion;

        /**
         *
         */
        public Orientacions(){}

        /**
         *
         * @param idOrientacion
         */
        public Orientacions(long idOrientacion) {
            this.idOrientacion = idOrientacion;
        }

        /**
         *
         * @return
         */
        public long getOrientacion(){
            return idOrientacion;
        }

        /**
         *
         * @param idOrientacion
         */
        public void setOrientacion(long idOrientacion){
            this.idOrientacion = idOrientacion;
        }
    }
//----- --  --  --  -   --- -   --      -

    /**
     *
     */
        public class Localidadess{
        private long idlocalidad;
        private short idProvincia;
        private int codigoPostal;

        /**
         *
         */
        public Localidadess(){}

        /**
         *
         * @param idlocalidad
         * @param idProvincia
         * @param codigoPostal
         */
        public Localidadess(long idlocalidad, short idProvincia, int codigoPostal) {
            this.idlocalidad = idlocalidad;
            this.idProvincia = idProvincia;
            this.codigoPostal = codigoPostal;
        }

        /**
         *
         * @return
         */
        public int getCodigoPostal(){
                return codigoPostal;
        }

        /**
         *
         * @param codigopostal
         */
        public void setCodigoPostal(int codigopostal){
            this.codigoPostal = codigopostal;
        }

        /**
         *
         * @return
         */
        public long getIdLocalidad() {
            return idlocalidad;
        }

        /**
         *
         * @param idlocalidad
         */
        public void setIdLocalidad(long idlocalidad) {
            this.idlocalidad = idlocalidad;
        }

        /**
         *
         * @return
         */
        public short getIdProvincia() {
            return idProvincia;
        }

        /**
         *
         * @param idProvincia
         */
        public void setIdProvincia(short idProvincia) {
            this.idProvincia = idProvincia;
        }
    }
}
