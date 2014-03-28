/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.StringEscapeUtils;
/**
 *
 * @author Edgardo
 */
@Entity
@Table(name="PRESUPUESTOS")
@NamedQueries({@NamedQuery(name = "Presupuestos.findAll",
query = "SELECT p FROM Presupuestos p"), @NamedQuery(name = "Presupuestos.findByIdPresupuesto",query = "SELECT p FROM Presupuestos p WHERE p.idPresupuesto = :idPresupuesto"), @NamedQuery(name = "Presupuestos.findByObservaciones",
        query = "SELECT p FROM Presupuestos p WHERE p.observaciones = :observaciones"), @NamedQuery(name = "Presupuestos.findByIdUsuarioExpidioPresupuesto",
        query = "SELECT p FROM Presupuestos p WHERE p.idUsuarioFk = :idUsuarioFk"), @NamedQuery(name = "Presupuestos.findByValidez",
        query = "SELECT p FROM Presupuestos p WHERE p.validez = :validez"), @NamedQuery(name = "Presupuestos.findByFechapresupuesto",
        query = "SELECT p FROM Presupuestos p WHERE p.fechapresupuesto = :fechapresupuesto"), @NamedQuery(name = "Presupuestos.findByTotalapagar",
        query = "SELECT p FROM Presupuestos p WHERE p.totalapagar = :totalapagar"), @NamedQuery(name = "Presupuestos.findByIva",
        query = "SELECT p FROM Presupuestos p WHERE p.iva = :iva"), @NamedQuery(name = "Presupuestos.findByNombre",
        query = "SELECT p FROM Presupuestos p WHERE p.nombre = :nombre"), @NamedQuery(name = "Presupuestos.findByApellido",
        query = "SELECT p FROM Presupuestos p WHERE p.apellido = :apellido"), @NamedQuery(name = "Presupuestos.findByTotal",
        query = "SELECT p FROM Presupuestos p WHERE p.total = :total"), @NamedQuery(name = "Presupuestos.findByPorcDescTotal", query = "SELECT p FROM Presupuestos p WHERE p.porcetajedescuentoTOTAL = :porcetajedescuentoTOTAL"), @NamedQuery(name = "Presupuestos.findByRecargototal",
        query = "SELECT p FROM Presupuestos p WHERE p.recargototal = :recargototal"), @NamedQuery(name = "Presupuestos.findByPorcentajerecargo",
        query = "SELECT p FROM Presupuestos p WHERE p.porcentajerecargo = :porcentajerecargo"), @NamedQuery(name = "Presupuestos.findByDescuentoresto",
        query = "SELECT p FROM Presupuestos p WHERE p.descuentoresto = :descuentoresto")})
public class Presupuestos implements Serializable {
    private static final long serialVersionUID = 1L;
  @TableGenerator(name="PresupuestosIdGen", table="ID_GEN_PRE",
    pkColumnName="FNAME",pkColumnValue="Presupuestos" , valueColumnName="FKEY",
    allocationSize=1)
    @Id
    @GeneratedValue(generator="PresupuestosIdGen",strategy=GenerationType.TABLE)
    @Basic(optional = false)
    @Column(name = "ID_PRESUPUESTO")
    private Integer idPresupuesto;
    @Column(name = "FECHAPRESUPUESTO")
    @Temporal(TemporalType.DATE)
    private Date fechapresupuesto;
    @Column(name = "VALIDEZ")
    @Temporal(TemporalType.DATE)
    private Date validez;
    @Column(name = "TOTAL",precision=15,scale=3)
    private BigDecimal total;
    @Column(name = "OBSERVACIONES",length=5_000)
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "ID_USUARIO_EXPIDIO_PRESUPUESTO")
    private int idUsuarioFk;    
    @Column(name="TOTALAPAGAR",precision=15,scale=3)
    private BigDecimal totalapagar;
    @Column(name = "IVA",precision=15,scale=3)
    private BigDecimal iva;
    @Column(name = "NOMBRE",length=40)
    private String nombre;
    @Column(name = "APELLIDO",length=20)
    private String apellido;
    @Column(name="PORC_DESC_TOTAL",precision=15,scale=2)
    private BigDecimal porcetajedescuentoTOTAL;
    @Column(name = "RECARGOTOTAL",precision=15,scale=2)
    private BigDecimal recargototal;
    @Column(name = "PORCENTAJERECARGO",precision=15,scale=2)
    private BigDecimal porcentajerecargo;
     @Column(name = "DESCUENTORESTO",precision=15,scale=3)
    private BigDecimal descuentoresto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "presupuestos",fetch=FetchType.LAZY)
    private List<Detallespresupuesto> detallepresupuestosList;

    /**
     *
     */
    public Presupuestos() {
    }

    /**
     *
     * @param idPresupuesto
     */
    public Presupuestos(Integer idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    /**
     *
     * @param idPresupuesto
     * @param idUsuarioFk
     */
    public Presupuestos(Integer idPresupuesto, int idUsuarioFk) {
        this.idPresupuesto = idPresupuesto;
        this.idUsuarioFk = idUsuarioFk;
    }

    /**
     *
     * @return
     */
    public Integer getIdPresupuesto() {
        return idPresupuesto;
    }

    /**
     *
     * @param idPresupuesto
     */
    public void setIdPresupuesto(Integer idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    /**
     *
     * @return
     */
    public Date getFechapresupuesto() {
        return fechapresupuesto;
    }

    /**
     *
     * @param fechapresupuesto
     */
    public void setFechapresupuesto(Date fechapresupuesto) {
        this.fechapresupuesto = fechapresupuesto;
    }

    /**
     *
     * @return
     */
    public Date getValidez() {
        return validez;
    }

    /**
     *
     * @param validez
     */
    public void setValidez(Date validez) {
        this.validez = validez;
    }

    /**
     *
     * @return
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     *
     * @param total
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
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
     *
     * @return
     */
    public int getIdUsuarioFk() {
        return idUsuarioFk;
    }

    /**
     *
     * @param idUsuarioFk
     */
    public void setIdUsuarioFk(int idUsuarioFk) {
        this.idUsuarioFk = idUsuarioFk;
    }

    /**
     *
     * @return
     */
    public List<Detallespresupuesto> getDetallepresupuestosList() {
        return detallepresupuestosList;
    }

    /**
     *
     * @param detallepresupuestosList
     */
    public void setDetallepresupuestosList(List<Detallespresupuesto> detallepresupuestosList) {
        this.detallepresupuestosList = detallepresupuestosList;
    }

    /**
     *
     * @return
     */
    public BigDecimal getTotalapagar() {
        return totalapagar;
    }

    /**
     *
     * @param totalapagar
     */
    public void setTotalapagar(BigDecimal totalapagar) {
        this.totalapagar = totalapagar;
    }

    /**
     *
     * @return
     */
    public String getApellido() {
        return apellido;
    }

    /**
     *
     * @param apellido
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     *
     * @return
     */
    public BigDecimal getIva() {
        return iva;
    }

    /**
     *
     * @param iva
     */
    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @return
     */
    public BigDecimal getPorcetajedescuentoTOTAL() {
        return porcetajedescuentoTOTAL;
    }

    /**
     *
     * @param porcetajedescuento
     */
    public void setPorcetajedescuentoTOTAL(BigDecimal porcetajedescuento) {
        this.porcetajedescuentoTOTAL = porcetajedescuento;
    }

    /**
     *
     * @return
     */
    public BigDecimal getDescuentoresto() {
        return descuentoresto;
    }

    /**
     *
     * @param descuentoresto
     */
    public void setDescuentoresto(BigDecimal descuentoresto) {
        this.descuentoresto = descuentoresto;
    }

    /**
     *
     * @return
     */
    public BigDecimal getPorcentajerecargo() {
        return porcentajerecargo;
    }

    /**
     *
     * @param porcentajerecargo
     */
    public void setPorcentajerecargo(BigDecimal porcentajerecargo) {
        this.porcentajerecargo = porcentajerecargo;
    }

    /**
     *
     * @return
     */
    public BigDecimal getRecargototal() {
        return recargototal;
    }

    /**
     *
     * @param recargototal
     */
    public void setRecargototal(BigDecimal recargototal) {
        this.recargototal = recargototal;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPresupuesto != null ? idPresupuesto.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Presupuestos)) {
            return false;
        }
        Presupuestos other = (Presupuestos) object;
        return (this.idPresupuesto != null || other.idPresupuesto == null) && (this.idPresupuesto == null || this.idPresupuesto.equals(other.idPresupuesto));
    }
    @Override
    public String toString() {
        return "entidades.Presupuestos[idPresupuesto=" + idPresupuesto + "]";
    }

    /**
     *
     * @return
     */
    public String toXML(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String xml ="<Item>\n" +
                "<id>" +this.getIdPresupuesto()+ "</id>\n" +
                "<nombre>" +this.getNombre()+ "</nombre>\n"+
                "<apellido>" +this.getApellido()+ "</apellido>\n"+                
                "<observaciones>"+StringEscapeUtils.escapeXml(this.getObservaciones())+"</observaciones>\n"+
                "<totalapagar>" +this.getTotalapagar().toString()+ "</totalapagar>\n"+
                "<usuarioexpidio>" +this.getIdUsuarioFk()+ "</usuarioexpidio>\n"+
                "<iva>" +this.getIva().toString()+ "</iva>\n"+
                "<total>" +this.getTotal().toString()+ "</total>\n"+
                "<fechapresupuesto>" +sdf.format(this.getFechapresupuesto())+ "</fechapresupuesto>\n"+
                "<fechavalidez>" +sdf.format(this.getValidez())+ "</fechavalidez>\n" +
                "<porcentajedescuentototal>" +this.getPorcetajedescuentoTOTAL().toString()+ "</porcentajedescuentototal>\n" +
                "<descuentoresto>"+this.getDescuentoresto().toString()+"</descuentoresto>\n" +
                "<recargototal>"+this.getRecargototal().toString()+"</recargototal>\n" +
                "<porcentajerecargo>"+this.getPorcentajerecargo().toString()+"</porcentajerecargo>\n"+
                "<Detallepresupuesto>\n" ;
                if(this.getDetallepresupuestosList().isEmpty()) {
                    xml+=   "</Detallepresupuesto>\n";
        } else{
                    List<Detallespresupuesto>lista = this.getDetallepresupuestosList();
                        for (Detallespresupuesto detallespresupuesto : lista) {
                            xml+=detallespresupuesto.toXML();
                        }
                }
            xml+=   "</Detallepresupuesto>\n"+
                "</Item>\n";
    return xml;
    }
}
