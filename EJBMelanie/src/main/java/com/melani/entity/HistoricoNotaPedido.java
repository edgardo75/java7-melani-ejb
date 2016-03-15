package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
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
import org.apache.commons.lang3.StringEscapeUtils; 
@Entity
@Table(name = "HISTORICONOTAPEDIDO", catalog = "", schema = "")
@NamedQueries({@NamedQuery(name = "HistoricoNotaPedido.findAll", query = "SELECT h FROM HistoricoNotaPedido h"),
@NamedQuery(name = "HistoricoNotaPedido.findByIdhistorico", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.id = :id"),
@NamedQuery(name = "HistoricoNotaPedido.findByAnticipo", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.anticipo = :anticipo"),
@NamedQuery(name = "HistoricoNotaPedido.findByIdusuariocancelo", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.idusuariocancelo = :idusuariocancelo"),
@NamedQuery(name = "HistoricoNotaPedido.findByPendiente", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.pendiente = :pendiente"),
@NamedQuery(name = "HistoricoNotaPedido.findByTotal", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.total = :total"),
@NamedQuery(name = "HistoricoNotaPedido.findByIdusuarioanulo", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.idusuarioanulo = :idusuarioanulo"),
@NamedQuery(name = "HistoricoNotaPedido.findBySaldo", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.saldo = :saldo"),
@NamedQuery(name = "HistoricoNotaPedido.findByHoraregistro", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.horaregistro = :horaregistro"),
@NamedQuery(name = "HistoricoNotaPedido.findByFecharegistro", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.fecharegistro = :fecharegistro"),
@NamedQuery(name = "HistoricoNotaPedido.findByAccion", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.accion = :accion"),
@NamedQuery(name = "HistoricoNotaPedido.findByPorcentajeaplicado", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.porcentajeaplicado = :porcentajeaplicado"),
@NamedQuery(name = "HistoricoNotaPedido.findByDescuento", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.descuento = :descuento"),
@NamedQuery(name = "HistoricoNotaPedido.findByIdusuarioexpidio", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.idusuarioexpidio = :idusuarioexpidio"),
@NamedQuery(name = "HistoricoNotaPedido.findByTotalapagar", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.totalapagar = :totalapagar"),
@NamedQuery(name = "HistoricoNotaPedido.findByObservaciones", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.observaciones = :observaciones"),
@NamedQuery(name = "HistoricoNotaPedido.findByRecargo", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.recargo = :recargo"),
@NamedQuery(name = "HistoricoNotaPedido.findByIdusuarioentrega", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.idusuarioentrega = :idusuarioentrega"),
@NamedQuery(name = "HistoricoNotaPedido.findByPorcrecargo", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.porcrecargo = :porcrecargo"),
@NamedQuery(name = "HistoricoNotaPedido.findByEntregado", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.entregado = :entregado"),
@NamedQuery(name = "HistoricoNotaPedido.findByCancelado", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.cancelado = :cancelado"),
@NamedQuery(name = "HistoricoNotaPedido.findByAnulado", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.anulado = :anulado"),
@NamedQuery(name = "HistoricoNotaPedido.findByPorcdesc", query = "SELECT h FROM HistoricoNotaPedido h WHERE h.porcdesc = :porcdesc"),
@NamedQuery(name = "HistoricoNotaPedido.findByFkIdNotaPedido",query = "SELECT h FROM HistoricoNotaPedido h WHERE h.fkidnotapedido.id = :idnota")})
public class HistoricoNotaPedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false,fetch = FetchType.LAZY)
    @GeneratedValue(strategy=GenerationType.TABLE,generator="HistoricoNotaPedidoIdGen")
    @TableGenerator(name="HistoricoNotaPedidoIdGen", table="ID_GEN_HIST_NOTAP",
    pkColumnName="FNAME",pkColumnValue="HistoricoNotaPedido" , valueColumnName="FKEY",
    allocationSize=1)
    @Column(name = "IDHISTORICO")    
    private Long id;
    @Column(name = "ANTICIPO",precision=15,scale=3)
    private BigDecimal anticipo;
    @Column(name = "IDUSUARIOCANCELO")
    private Long idusuariocancelo;
    @Column(name = "PENDIENTE",length=1)
    private Character pendiente;
    @Column(name = "TOTAL",precision=15,scale=3)
    private BigDecimal total;
    @Column(name = "IDUSUARIOANULO")
    private Long idusuarioanulo;
    @Column(name = "SALDO",precision=15,scale=3)
    private BigDecimal saldo;
    @Column(name = "HORAREGISTRO")
    @Temporal(TemporalType.TIME)
    private Date horaregistro;
    @Column(name = "FECHAREGISTRO")
    @Temporal(TemporalType.DATE)
    private Date fecharegistro;
    @Column(name = "ACCION",length=100)
    private String accion;
    @Column(name = "PORCENTAJEAPLICADO")
    private Short porcentajeaplicado;
    @Column(name = "DESCUENTO",precision=15,scale=3)
    private BigDecimal descuento;
    @Column(name = "IDUSUARIOEXPIDIO")
    private Long idusuarioexpidio;
    @Column(name = "TOTALAPAGAR",precision=15,scale=3)
    private BigDecimal totalapagar;
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "OBSERVACIONES",length=32_000)
    private String observaciones;
    @Column(name = "RECARGO",precision=15,scale=3)
    private BigDecimal recargo;
    @Column(name = "IDUSUARIOENTREGA")
    private Long idusuarioentrega;
    @Column(name = "PORCRECARGO",precision=15,scale=3)
    private BigDecimal porcrecargo;
    @Column(name = "ENTREGADO",length=1)
    private Character entregado;
    @Column(name = "CANCELADO",length=1)
    private Character cancelado;
    @Column(name = "ANULADO",length=1)
    private Character anulado;
    @Column(name = "PORCDESC",precision=15,scale=3)
    private BigDecimal porcdesc;
    @JoinColumn(name="FKIDNOTAPEDIDO_ID",referencedColumnName="ID")
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private Notadepedido fkidnotapedido;

    public HistoricoNotaPedido() {
    }

    public HistoricoNotaPedido(long id) {
        this.id = id;
    }

    public Notadepedido getFkidnotapedido() {
        return fkidnotapedido;
    }

    public void setFkidnotapedido(Notadepedido fkidnotapedido) {
        this.fkidnotapedido = fkidnotapedido;
    }

    public BigDecimal getPorcentajedesc() {
        return porcdesc;
    }

    public void setPorcentajedesc(BigDecimal porcentajedesc) {
        this.porcdesc = porcentajedesc;
    }

    public Short getPorcentajeaplicado() {
        return porcentajeaplicado;
    }

    public void setPorcentajeaplicado(Short porcentajeaplicado) {
        this.porcentajeaplicado = porcentajeaplicado;
    }

    public Character getPendiente() {
        return pendiente;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }   

    
    public void setPendiente(Character pendiente) {
        this.pendiente = pendiente;
    }

    public Character getEntregado() {
        return entregado;
    }

    public void setEntregado(Character entregado) {
        this.entregado = entregado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getIdusuarioexpidio() {
        return idusuarioexpidio;
    }

    public void setIdusuarioexpidio(long idusuarioexpidio) {
        this.idusuarioexpidio = idusuarioexpidio;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Long getIdusuarioentrega() {
        return idusuarioentrega;
    }

    public void setIdusuarioentrega(long idusuarioentrega) {
        this.idusuarioentrega = idusuarioentrega;
    }

    public Date getFecharegistro() {
        return fecharegistro;
    }

    public Long getIdusuariocancelo() {
        return idusuariocancelo;
    }

    public void setIdusuariocancelo(long idusuariocancelo) {
        this.idusuariocancelo = idusuariocancelo;
    }    

    public void setFecharegistro(Date fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getAnticipo() {
        return anticipo;
    }

    public Date getHoraregistro() {
        return horaregistro;
    }

    public void setHoraregistro(Date horaregistro) {
        this.horaregistro = horaregistro;
    }

    public void setAnticipo(BigDecimal anticipo) {
        this.anticipo = anticipo;
    }

    public Long getIdhistorico() {
        return id;
    }

    public void setIdhistorico(long id) {
        this.id = id;
    }

    public Long getIdusuarioanulo() {
        return idusuarioanulo;
    }

    public void setIdusuarioanulo(Long idusuarioanulo) {
        this.idusuarioanulo = idusuarioanulo;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPorcrecargo() {
        return porcrecargo;
    }

    public void setPorcrecargo(BigDecimal porcrecargo) {
        this.porcrecargo = porcrecargo;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getTotalapagar() {
        return totalapagar;
    }

    public void setTotalapagar(BigDecimal totalapagar) {
        this.totalapagar = totalapagar;
    }

    public Character getCancelado() {
        return cancelado;
    }

    public void setCancelado(Character cancelado) {
        this.cancelado = cancelado;
    }

    public Character getAnulado() {
        return anulado;
    }

    public void setAnulado(Character anulado) {
        this.anulado = anulado;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {      
        if (!(object instanceof HistoricoNotaPedido)) {
            return false;
        }
        HistoricoNotaPedido other = (HistoricoNotaPedido) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    @Override
    public String toString() {
        return "com.melani.entidades.HistoricoNotaPedido[id=" + id + "]";
    }

    public String toXML(){        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fereg = "";
        if(this.getFecharegistro()!=null) {
            fereg =sdf.format(this.getFecharegistro());
        }
        String hourreg = "";
        if(this.getHoraregistro()!=null) {
            hourreg=sdf.format(this.getHoraregistro());
        }       
            StringBuilder item = new StringBuilder(10);
        try {
              item.append("<item>\n").append("<id>").append(this.getIdhistorico()).append("</id>\n").append("<anticipo>").append(this.getAnticipo().toString()).append("</anticipo>\n");
                           item.append("<entregado>").append(this.getEntregado().toString()).append("</entregado>\n");
                           item.append("<fecharegistro>").append(fereg).append("</fecharegistro>\n");
                           item.append("<horaregistro>").append(hourreg).append("</horaregistro>\n");
                          item.append("<cancelado>").append(this.getCancelado().toString()).append("</cancelado>\n");
                          item.append("<anulado>").append(this.getAnulado().toString()).append("</anulado>\n");
                           item.append("<idnota>").append(this.getFkidnotapedido().getId()).append("</idnota>\n");
                           item.append("<iduseranulo>").append(this.getIdusuarioanulo().toString()).append("</iduseranulo>\n");
                           item.append("<iduserentrega>").append(this.getIdusuarioentrega().toString()).append("</iduserentrega>\n");
                           item.append("<iduserexpidio>").append(this.getIdusuarioexpidio().toString()).append("</iduserexpidio>\n");
                           item.append("<idusuariocancelo>").append(this.getIdusuariocancelo().toString())
                           .append("</idusuariocancelo>\n" + "<recargo>").append(this.getRecargo().toString())
                           .append("</recargo>\n" + "<totalapagar>").append(this.getTotalapagar().toString())
                                   .append("</totalapagar>\n").append("<porcrecargo>")
                                   .append(this.getPorcrecargo().toString()).append("</porcrecargo>\n")
                                   .append("<porcentajedescuento>").append(this.getPorcentajedesc().toString())
                                   .append("</porcentajedescuento>\n" ).append("<descuento>")
                                   .append(this.getDescuento().toString()).append("</descuento>\n" )
                                   .append("<accion>").append(StringEscapeUtils.escapeXml10(this.getAccion()))
                                   .append("</accion>\n" ).append("<saldo>").append(this.getSaldo().toString())
                                   .append("</saldo>\n" ).append("<total>").append(this.getTotal().toString()).append("</total>\n");
                   item.append("</item>\n");
        } catch (Exception e) {
            e.getMessage();
        }
        return item.toString();        
    }
}