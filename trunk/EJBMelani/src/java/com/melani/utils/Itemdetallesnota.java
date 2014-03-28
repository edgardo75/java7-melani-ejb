/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 *
 * @author Edgardo
 * @version 1.0 Build 5600 Feb 20, 2013
 */
@XStreamAlias("itemdetallesnota")
public class Itemdetallesnota {
        private int cantidad;
        private Double precio;
        private Double subtotal;
        private char entregado;
        private char cancelado;
        private char pendiente;
        private Double descuento;
        private long iva;
        private long id_nota;
        private long id_producto;
        private char anulado;
        private Double preciocondescuento;

    /**
     *
     * @return
     */
    public Double getPreciocondescuento() {
        return preciocondescuento;
    }
        /////////////////---------------------------------------------------------------------

    /**
     *
     * @return
     */
            public char getCancelado() {
            return cancelado;
        }

    /**
     *
     * @return
     */
    public int getCantidad() {
            return cantidad;
        }

    /**
     *
     * @return
     */
    public Double getDescuento() {
            return descuento;
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
    public char getAnulado() {
        return anulado;
    }

    /**
     *
     * @return
     */
    public long getId_nota() {
            return id_nota;
        }

    /**
     *
     * @return
     */
    public long getId_producto() {
            return id_producto;
        }

    /**
     *
     * @return
     */
    public long getIva() {
            return iva;
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
    public Double getPrecio() {
            return precio;
        }

    /**
     *
     * @return
     */
    public Double getSubtotal() {
            return subtotal;
        }
    //------------------------------------------------------------------------------------
}
