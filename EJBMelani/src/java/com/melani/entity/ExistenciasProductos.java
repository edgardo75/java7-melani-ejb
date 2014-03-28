/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
/**
 * A Entity ExistenciasProductos
 *@version 1.0
 * @author Edgardo Alvarez
 */
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
    private Integer cantidadactual;
    @Column(name = "FECHAAGREGADO")
    @Temporal(TemporalType.DATE)
    private Date fechaagregado;
    @Column(name = "CANTIDADINICIAL")
    private Integer cantidadinicial;
    @Column(name = "PRECIOUNITARIO",precision=12,scale=2)
    private BigDecimal preciounitario;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.TABLE,generator="ExistenciasIdGen")
    @TableGenerator(name="ExistenciasIdGen", table="ID_GEN_EXISTENCIAS",
    pkColumnName="FNAME",pkColumnValue="ExistenciasProductos", valueColumnName="FKEY",
    allocationSize=1)
    @Column(name = "ID_EXISTENCIAS")
    private Integer idExistencias;
    @Column(name="ID_USUARIO")
    private Integer idUsuario;
    @JoinColumn(name = "PRODUCTOS_SID", referencedColumnName = "SID")
    @ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.LAZY,optional = false)
    private Productos productos;

    /**
     *
     */
    public ExistenciasProductos() {
    }

    /**
     *
     * @param idExistencias
     */
    public ExistenciasProductos(Integer idExistencias) {
        this.idExistencias = idExistencias;
    }

    /**
     *
     * @return
     */
    public Integer getCantidadactual() {
        return cantidadactual;
    }

    /**
     *
     * @param cantidadactual
     */
    public void setCantidadactual(Integer cantidadactual) {
        this.cantidadactual = cantidadactual;
    }

    /**
     *
     * @return
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     *
     * @param idUsuario
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     *
     * @return
     */
    public Date getFechaagregado() {
        return fechaagregado;
    }

    /**
     *
     * @param fechaagregado
     */
    public void setFechaagregado(Date fechaagregado) {
        this.fechaagregado = fechaagregado;
    }

    /**
     *
     * @return
     */
    public Integer getIdExistencias() {
        return idExistencias;
    }

    /**
     *
     * @param idExistencias
     */
    public void setIdExistencias(Integer idExistencias) {
        this.idExistencias = idExistencias;
    }

    /**
     *
     * @return
     */
    public Productos getProductos() {
        return productos;
    }

    /**
     *
     * @param productos
     */
    public void setProductos(Productos productos) {
        this.productos = productos;
    }

    /**
     *
     * @return
     */
    public Integer getCantidadinicial() {
        return cantidadinicial;
    }

    /**
     *
     * @param cantidadinicial
     */
    public void setCantidadinicial(Integer cantidadinicial) {
        this.cantidadinicial = cantidadinicial;
    }

    /**
     *
     * @return
     */
    public BigDecimal getPreciounitario() {
        return preciounitario;
    }

    /**
     *
     * @param preciounitario
     */
    public void setPreciounitario(BigDecimal preciounitario) {
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExistenciasProductos)) {
            return false;
        }
        ExistenciasProductos other = (ExistenciasProductos) object;
        if ((this.idExistencias == null && other.idExistencias != null) || (this.idExistencias != null && !this.idExistencias.equals(other.idExistencias))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "entity.ExistenciasProductos[idExistencias=" + idExistencias + "]";
    }

    /**
     *
     * @return
     */
    public String toXML(){
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String item = "<item>\n" +
                "<id_existencias>" +this.getIdExistencias()+"</id_existencias>\n"+
                "<idproducto>" +this.getProductos().getSid()+"</idproducto>\n"+
                "<cantidadactual>" +this.getCantidadactual()+"</cantidadactual>\n"+
                "<cantidadinicial>" +this.getCantidadinicial()+"</cantidadinicial>\n"+
                "<fecha>" +sdf.format(this.getFechaagregado())+"</fecha>\n"+
                "<precio>" +this.getPreciounitario().toString()+"</precio>\n" +
                "<id_usuario>"+this.getIdUsuario()+"</id_usuario>\n"+
                "</item>\n";
    return item;
    }
}
