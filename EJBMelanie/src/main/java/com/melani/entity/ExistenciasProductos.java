package com.melani.entity;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "EXISTENCIASPRODUCTOS")
@NamedQueries({
    @NamedQuery(name = "ExistenciasProductos.findAll", query = "SELECT e FROM ExistenciasProductos e"),
    @NamedQuery(name = "ExistenciasProductos.findByCantidadactual", query = "SELECT e FROM ExistenciasProductos e WHERE e.cantidadactual = :cantidadactual"),
    @NamedQuery(name = "ExistenciasProductos.findByFechaagregado", query = "SELECT e FROM ExistenciasProductos e WHERE e.fechaagregado = :fechaagregado"),
    @NamedQuery(name = "ExistenciasProductos.findByIdExistencias", query = "SELECT e FROM ExistenciasProductos e WHERE e.idExistencias = :idExistencias"),
    @NamedQuery(name = "ExistenciasProductos.findByPreciounitario", query = "SELECT e FROM ExistenciasProductos e WHERE e.preciounitario = :preciounitario")})
public class ExistenciasProductos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "CANTIDADACTUAL")
    private int cantidadactual;
    @Column(name = "FECHAAGREGADO")
    @Temporal(TemporalType.DATE)
    private Date fechaagregado;
    @Column(name = "CANTIDADINICIAL")
    private int cantidadinicial;
    @Column(name = "PRECIOUNITARIO",precision=12,scale=2)
    private double preciounitario;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.TABLE,generator="ExistenciasIdGen")
    @TableGenerator(name="ExistenciasIdGen", table="ID_GEN_EXISTENCIAS",
    pkColumnName="FNAME",pkColumnValue="ExistenciasProductos", valueColumnName="FKEY",
    allocationSize=1)
    @Column(name = "ID_EXISTENCIAS")
    private Long idExistencias;
    @Column(name="ID_USUARIO")
    private Long idUsuario;
    @JoinColumn(name = "PRODUCTOS_SID", referencedColumnName = "SID")
    @ManyToOne(optional = false)
    private Productos productos;

    public ExistenciasProductos() {
    }

    public ExistenciasProductos(long idExistencias) {
        this.idExistencias = idExistencias;
    }

    public int getCantidadactual() {
        return cantidadactual;
    }

    public void setCantidadactual(int cantidadactual) {
        this.cantidadactual = cantidadactual;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaagregado() {
        return fechaagregado;
    }

    public void setFechaagregado(Date fechaagregado) {
        this.fechaagregado = fechaagregado;
    }

    public Long getIdExistencias() {
        return idExistencias;
    }

    public void setIdExistencias(Long idExistencias) {
        this.idExistencias = idExistencias;
    }

    public Productos getProductos() {
        return productos;
    }

    public void setProductos(Productos productos) {
        this.productos = productos;
    }

    public int getCantidadinicial() {
        return cantidadinicial;
    }

    public void setCantidadinicial(int cantidadinicial) {
        this.cantidadinicial = cantidadinicial;
    }

    public double getPreciounitario() {
        return preciounitario;
    }

    public void setPreciounitario(double preciounitario) {
        this.preciounitario = preciounitario;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idExistencias != null ? idExistencias.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {        
        if (!(object instanceof ExistenciasProductos)) {
            return false;
        }
        ExistenciasProductos other = (ExistenciasProductos) object;
        return (this.idExistencias != null || other.idExistencias == null) && (this.idExistencias == null || this.idExistencias.equals(other.idExistencias));
    }
    @Override
    public String toString() {
        return "entity.ExistenciasProductos[idExistencias=" + idExistencias + "]";
    }

    public String toXML(){
        StringBuilder item = new StringBuilder(32);
        item.append("<item>\n<id_existencias>").append(this.getIdExistencias())
                .append("</id_existencias>\n").append("<idproducto>").append(this.getProductos().getSid())
                .append("</idproducto>\n").append("<cantidadactual>").append(this.getCantidadactual())
                .append("</cantidadactual>\n").append("<cantidadinicial>").append(this.getCantidadinicial())
                .append("</cantidadinicial>\n").append("<fecha>")
                .append(DateFormat.getDateInstance().format(this.getFechaagregado()))
                .append("</fecha>\n").append("<precio>").append(this.getPreciounitario())
                .append("</precio>\n").append("<id_usuario>")
                .append(this.getIdUsuario()).append("</id_usuario>\n").append("</item>\n");
    return item.toString();
    }
}
