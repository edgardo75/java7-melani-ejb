package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.apache.commons.lang3.StringEscapeUtils;

@Entity
@Table(name = "DETALLESPRESUPUESTO")
@NamedQueries({@NamedQuery(name = "Detallespresupuesto.findAll", query = "SELECT d FROM Detallespresupuesto d"),
@NamedQuery(name = "Detallespresupuesto.findByDescuento", query = "SELECT d FROM Detallespresupuesto d WHERE d.descuento = :descuento"),
@NamedQuery(name = "Detallespresupuesto.findByCantidad", query = "SELECT d FROM Detallespresupuesto d WHERE d.cantidad = :cantidad"),
@NamedQuery(name = "Detallespresupuesto.findBySubtotal", query = "SELECT d FROM Detallespresupuesto d WHERE d.subtotal = :subtotal"),
@NamedQuery(name = "Detallespresupuesto.findByFkProducto", query = "SELECT d FROM Detallespresupuesto d WHERE d.detallespresupuestoPK.fkProducto = :fkProducto"),
@NamedQuery(name = "Detallespresupuesto.findByIdDpFk", query = "SELECT d FROM Detallespresupuesto d WHERE d.detallespresupuestoPK.idDpFk = :idDpFk"),
@NamedQuery(name = "Detallespresupuesto.findByPrecio", query = "SELECT d FROM Detallespresupuesto d WHERE d.precio = :precio"),
@NamedQuery(name = "Detallespresupuesto.findByPrecioDesc", query = "SELECT d FROM Detallespresupuesto d WHERE d.precioDesc = :precioDesc")})
public class Detallespresupuesto implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected DetallespresupuestoPK detallespresupuestoPK;
    @Column(name = "SUBTOTAL",precision=15,scale=3)
    private BigDecimal subtotal;
    @Column(name = "DESCUENTO",precision=15,scale=2)
    private BigDecimal descuento;
    @Column(name = "PRECIO",precision=12,scale=2)
    private BigDecimal precio;
    @Column(name = "PRECIO_DESC",precision=12,scale=2)
    private BigDecimal precioDesc;
    @Column(name = "CANTIDAD")
    private Short cantidad;
    @JoinColumn(name = "ID_DP_FK", referencedColumnName = "ID_PRESUPUESTO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Presupuestos presupuestos;
    @JoinColumn(name = "FK_PRODUCTO", referencedColumnName = "SID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Productos productos;

    public Detallespresupuesto() {
    }

    public Detallespresupuesto(DetallespresupuestoPK detallespresupuestoPK) {
        this.detallespresupuestoPK = detallespresupuestoPK;
    }

    public Detallespresupuesto(int idDpFk, int fkProducto) {
        this.detallespresupuestoPK = new DetallespresupuestoPK(idDpFk, fkProducto);
    }

    public DetallespresupuestoPK getDetallespresupuestoPK() {
        return detallespresupuestoPK;
    }

    public void setDetallespresupuestoPK(DetallespresupuestoPK detallespresupuestoPK) {
        this.detallespresupuestoPK = detallespresupuestoPK;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPrecioDesc() {
        return precioDesc;
    }

    public void setPrecioDesc(BigDecimal precioDesc) {
        this.precioDesc = precioDesc;
    }

    public Short getCantidad() {
        return cantidad;
    }

    public void setCantidad(Short cantidad) {
        this.cantidad = cantidad;
    }

    public Presupuestos getPresupuestos() {
        return presupuestos;
    }

    public void setPresupuestos(Presupuestos presupuestos) {
        this.presupuestos = presupuestos;
    }

    public Productos getProductos() {
        return productos;
    }

    public void setProductos(Productos productos) {
        this.productos = productos;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallespresupuestoPK != null ? detallespresupuestoPK.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {        
        if (!(object instanceof Detallespresupuesto)) {
            return false;
        }
        Detallespresupuesto other = (Detallespresupuesto) object;
        return !((this.detallespresupuestoPK == null && other.detallespresupuestoPK != null) || (this.detallespresupuestoPK != null && !this.detallespresupuestoPK.equals(other.detallespresupuestoPK)));
    }
    @Override
    public String toString() {
        return "entity.Detallespresupuesto[detallespresupuestoPK=" + detallespresupuestoPK + "]";
    }

    public String toXML(){
        String xml= "<itemdetallepresupuesto>\n<idproducto>" + this.getProductos().getSid() + "</idproducto>\n" + "<descripcion_prod>" + StringEscapeUtils.escapeXml10(this.getProductos().getDescripcion()) + "</descripcion_prod>\n" + "<codigo_producto>" + this.getProductos().getCodproducto() + "</codigo_producto>\n" + "<idpresupuesto>" + this.getPresupuestos().getIdPresupuesto() + "</idpresupuesto>\n" + "<precio>" + this.getPrecio().toString() + "</precio>\n" + "<precio_desc>" + this.getPrecioDesc().toString() + "</precio_desc>\n" + "<descuento>" + this.getDescuento().toString() + "</descuento>\n" + "<subtotal>" + this.getSubtotal().toString() + "</subtotal>\n" + "<cantidad>" + this.getCantidad() + "</cantidad>\n" + "</itemdetallepresupuesto>\n";
        return xml;
    }
}