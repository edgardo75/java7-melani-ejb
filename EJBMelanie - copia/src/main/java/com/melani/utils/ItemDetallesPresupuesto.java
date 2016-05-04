package com.melani.utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("itemdetallespresupuesto")
public class ItemDetallesPresupuesto {
    private Double descuento;
    private Short cantidad;
    private Integer fk_id_producto;
    private Integer fk_id_presupuesto;
    private Double subtotal;
    private Double precio_desc;
    private Double precio;

    public Double getPrecio() {
        return precio;
    }

    public Short getCantidad() {
        return cantidad;
    }

    public Double getDescuento() {
        return descuento;
    }

    public Integer getFk_id_presupuesto() {
        return fk_id_presupuesto;
    }

    public Integer getFk_id_producto() {
        return fk_id_producto;
    }

    public Double getPrecio_desc() {
        return precio_desc;
    }

    public Double getSubtotal() {
        return subtotal;
    }
}