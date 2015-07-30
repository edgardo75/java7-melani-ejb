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
public class DatosNotaPedido {
    private long idnota;
    private Personas personas;
    private long vendedor;
    private double montototal;
    private double anticipoacum;
    private double anticipo;
    private double saldo;
    private char entregado;
    private long usuario_expidio_nota;
    private int stockfuturo;
    private long usuario_entregado;
    private char anulado;
    private long usuario_cancelo_nota;
    private char cancelado;
    private String observaciones;
    private char pendiente;
    private double montoiva;
    private double recargo;
    private long id_usuario_anulado;
    private String numerodecupon;
    private char enefectivo;
    private Double montototalapagar;
    private Double porc_descuento_total;    
    private Double porcentajerecargo;
    private TarjetaCredito tarjetacredito;
    private Porcentajes porcentaje;
    private DetallesNotaPedido detallesnotapedido;
    private Double descuentonota;
    private Double descuentopesos;
    private String fechaentrega;
    private String fechaanulada;
    private String fechacancelada;
    private String fechacompra;
    private String fecancel;
    private String horacompra;

    /**
     *
     * @return
     */
    public long getIdnota() {
        return idnota;
    }

    /**
     *
     * @param idnota
     */
    public void setIdnota(long idnota) {
        this.idnota = idnota;
    }

    /**
     *
     * @return
     */
    public long getIdVendedor() {
        return vendedor;
    }

    /**
     *
     * @return
     */
    public String getFechacompra() {
        return fechacompra;
    }

    /**
     *
     * @param vendedor
     */
    public void setVendedor(int vendedor) {
        this.vendedor = vendedor;
    }

    /**
     *
     * @return
     */
    public double getAnticipoacum() {
        return anticipoacum;
    }

    /**
     *
     * @param anticipoacum
     */
    public void setAnticipoacum(double anticipoacum) {
        this.anticipoacum = anticipoacum;
    }

    /**
     *
     * @return
     */
    public long getUsuario_cancelo_nota() {
        return usuario_cancelo_nota;
    }

    /**
     *
     * @param usuario_cancelo_nota
     */
    public void setUsuario_cancelo_nota(int usuario_cancelo_nota) {
        this.usuario_cancelo_nota = usuario_cancelo_nota;
    }

    /**
     *
     * @return
     */
    public Double getDescuentonota() {
        return descuentonota;
    }

    /**
     *
     * @return
     */
    
    public String getHoracompra() {
        return horacompra;
    }

    /**
     *
     * @return
     */
    
    public Double getDescuentopesos() {
        return descuentopesos;
    }

    /**
     *
     * @return
     */
    public String getFechaanulada() {
        return fechaanulada;
    }

    /**
     *
     * @return
     */
    
    public String getFecancel() {
        return fecancel;
    }
    /**
     *
     * @return
     */
    
    public String getFechacancelada() {
        return fechacancelada;
    }

    /**
     *
     * @return
     */
    public String getFechaentrega() {
        return fechaentrega;
    }

    /**
     *
     * @return
     */
    
    public Porcentajes getPorcentaje() {
        return porcentaje;
    }

    /**
     *
     * @return
     */
    public DetallesNotaPedido getDetallesnotapedido() {
        return detallesnotapedido;
    }

    /**
     *
     * @return
     */
    public Double getMontototalapagar() {
        return montototalapagar;
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
    public Double getPorcentajerecargo() {
        return porcentajerecargo;
    }
    //------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */
        public double getAnticipo() {
        return anticipo;
    }

    /**
     *
     * @return
     */
    public char getAnulado() {
        return anulado;
    }

    /**
     *
     * @return
     */
    public char getEnefectivo() {
        return enefectivo;
    }

    /**
     *
     * @return
     */
    public char getEntregado() {
        return entregado;
    }

    /**
     *
     * @return
     */
    public char getCancelado() {
        return cancelado;
    }

    /**
     *
     * @param cancelado
     */
    public void setCancelado(char cancelado) {
        this.cancelado = cancelado;
    }

    /**
     *
     * @return
     */
    public long getId_usuario_anulado() {
        return id_usuario_anulado;
    }

    /**
     *
     * @return
     */
    public double getMontoiva() {
        return montoiva;
    }

    /**
     *
     * @return
     */
    public double getMontototal() {
        return montototal;
    }

    /**
     *
     * @return
     */
    public String getNumerodecupon() {
        return numerodecupon;
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
    public int getControlastock() {
        return stockfuturo;
    }

    /**
     *
     * @return
     */
    public char getPendiente() {
        return pendiente;
    }

    /**
     *
     * @return
     */
    public double getRecargo() {
        return recargo;
    }

    /**
     *
     * @return
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     *
     * @return
     */
    public long getUsuario_entregado() {
        return usuario_entregado;
    }

    /**
     *
     * @return
     */
    public long getUsuario_expidio_nota() {
        return usuario_expidio_nota;
    }
    //-----------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */
        public Personas getPersonas() {
        return personas;
    }
   //------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */
        public Porcentajes getPorcentajes() {
        return porcentaje;
    }

    /**
     *
     * @return
     */
    public TarjetaCredito getTarjetacredito() {
        return tarjetacredito;
    }
    //-----------------------------------------------------------------------------------------

    /**
     *
     * @return
     */
        public int getStockfuturo() {
        return stockfuturo;
    }

    /**
     *
     * @param stockfuturo
     */
    public void setStockfuturo(int stockfuturo) {
        this.stockfuturo = stockfuturo;
    }
    //-----------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------

       public class Personas{
       private long id;
       private int numerodocu;

        /**
         *
         * @return
         */
        public long getId() {
            return id;
        }

        /**
         *
         * @param id
         */
        public void setId(long id) {
            this.id = id;
        }

        /**
         *
         * @return
         */
        public int getNumerodocu() {
            return numerodocu;
        }

        /**
         *
         * @param numerodocu
         */
        public void setNumerodocu(int numerodocu) {
            this.numerodocu = numerodocu;
        }
   }

        public class TarjetaCredito{
        private int id_tarjeta;

        /**
         *
         * @return
         */
        public int getId_tarjeta() {
            return id_tarjeta;
        }
    }

        public class Porcentajes{
        private short id_porcentaje;

        /**
         *
         * @return
         */
        public short getId_porcentaje() {
            return id_porcentaje;
        }
     }
}
