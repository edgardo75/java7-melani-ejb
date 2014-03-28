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
@XStreamAlias("itemdetallespresupuesto")
public class ItemDetallesPresupuesto {
    private Double descuento;
    private Short cantidad;
    private Integer fk_id_producto;
    private Integer fk_id_presupuesto;
    private Double subtotal;
    private Double precio_desc;
    private Double precio;

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
    public Short getCantidad() {
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
    public Integer getFk_id_presupuesto() {
        return fk_id_presupuesto;
    }

    /**
     *
     * @return
     */
    public Integer getFk_id_producto() {
        return fk_id_producto;
    }

    /**
     *
     * @return
     */
    public Double getPrecio_desc() {
        return precio_desc;
    }

    /**
     *
     * @return
     */
    public Double getSubtotal() {
        return subtotal;
    }
}
