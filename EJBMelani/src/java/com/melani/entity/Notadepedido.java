/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang3.StringEscapeUtils;
/**
 *
 * @author Edgardo
 */
@Entity
@Table(name = "NOTADEPEDIDO")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "Notadepedido.findAll",
query = "SELECT n FROM Notadepedido n"), @NamedQuery(name = "Notadepedido.findById",
        query = "SELECT n FROM Notadepedido n WHERE n.id = :id"), @NamedQuery(name = "Notadepedido.findByObservaciones",
        query = "SELECT n FROM Notadepedido n WHERE n.observaciones = :observaciones"), @NamedQuery(name = "Notadepedido.findByPendiente",
        query = "SELECT n FROM Notadepedido n WHERE n.pendiente = :pendiente"), @NamedQuery(name = "Notadepedido.findByAnticipo",
        query = "SELECT n FROM Notadepedido n WHERE n.anticipo = :anticipo"), @NamedQuery(name = "Notadepedido.findByMontoiva",
        query = "SELECT n FROM Notadepedido n WHERE n.montoiva = :montoiva"), @NamedQuery(name = "Notadepedido.findByHoracompra",
        query = "SELECT n FROM Notadepedido n WHERE n.horacompra = :horacompra"), @NamedQuery(name = "Notadepedido.findByRecargo",
        query = "SELECT n FROM Notadepedido n WHERE n.recargo = :recargo"), @NamedQuery(name = "Notadepedido.findByIdUsuarioExpidioNota",
        query = "SELECT n FROM Notadepedido n WHERE n.idUsuarioExpidioNota = :idUsuarioExpidioNota"), @NamedQuery(name = "Notadepedido.findByIdusuarioAnulado",
        query = "SELECT n FROM Notadepedido n WHERE n.idusuarioAnulado = :idusuarioAnulado"), @NamedQuery(name = "Notadepedido.findByFechaAnulado",
        query = "SELECT n FROM Notadepedido n WHERE n.fechaAnulado = :fechaAnulado"), @NamedQuery(name = "Notadepedido.findByTotal",
        query = "SELECT n FROM Notadepedido n WHERE n.total = :total"), @NamedQuery(name = "Notadepedido.findByAnulado",
        query = "SELECT n FROM Notadepedido n WHERE n.anulado = :anulado"), @NamedQuery(name = "Notadepedido.findByNumerodecupon",
        query = "SELECT n FROM Notadepedido n WHERE n.numerodecupon = :numerodecupon"), @NamedQuery(name = "Notadepedido.findByEnefectivo",
        query = "SELECT n FROM Notadepedido n WHERE n.enefectivo = :enefectivo"), @NamedQuery(name = "Notadepedido.findByEntregado",
        query = "SELECT n FROM Notadepedido n WHERE n.entregado = :entregado"), @NamedQuery(name = "Notadepedido.findByFechaentrega",
        query = "SELECT n FROM Notadepedido n WHERE n.fechaentrega = :fechaentrega"), @NamedQuery(name = "Notadepedido.findByIdusuarioEntregado",
        query = "SELECT n FROM Notadepedido n WHERE n.idusuarioEntregado = :idusuarioEntregado"), @NamedQuery(name = "Notadepedido.findByStockfuturo",
        query = "SELECT n FROM Notadepedido n WHERE n.stockfuturo = :stockfuturo"), @NamedQuery(name = "Notadepedido.findBySaldo",
        query = "SELECT n FROM Notadepedido n WHERE n.saldo = :saldo"), @NamedQuery(name = "Notadepedido.findByFechadecompra",
        query = "SELECT n FROM Notadepedido n WHERE n.fechadecompra = :fechadecompra"), @NamedQuery(name = "Notadepedido.findByCancelado",
        query = "SELECT n FROM Notadepedido n WHERE n.cancelado = :cancelado"), @NamedQuery(name = "Notadepedido.findByFecancelado",
        query = "SELECT n FROM Notadepedido n WHERE n.fecancelado = :fecancelado"), @NamedQuery(name = "Notadepedido.findByIdUsuarioCancelo",
        query = "SELECT n FROM Notadepedido n WHERE n.idUsuarioCancelo = :idUsuarioCancelo"), @NamedQuery(name = "Notadepedido.findByDescuentoNota",
        query = "SELECT n FROM Notadepedido n WHERE n.descuentoNota = :descuentoNota"), @NamedQuery(name = "Notadepedido.findByMontototalapagar",
        query = "SELECT n FROM Notadepedido n WHERE n.montototalapagar = :montototalapagar"), @NamedQuery(name = "Notadepedido.findByPorcdesctotal",
        query = "SELECT n FROM Notadepedido n WHERE n.porcdesctotal = :porcdesctotal"), @NamedQuery(name = "Notadepedido.findByPorcrecargo",
        query = "SELECT n FROM Notadepedido n WHERE n.porcrecargo = :porcrecargo"),@NamedQuery(name ="Notadepedido.deleteById",
        query = "DELETE FROM Notadepedido n WHERE n.id = :id"),@NamedQuery(name = "Notadepedido.searchAllOrderDesc",query = "SELECT n FROM Notadepedido n ORDER BY n.id DESC, n.fechadecompra DESC"),
@NamedQuery(name = "Notadepedido.findClientFk",query = "SELECT n FROM Notadepedido n WHERE n.fkIdcliente.idPersona = :id")})
public class Notadepedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id        
    @TableGenerator(name="NoPeIdGen", table="ID_GEN_NOTAP",
    pkColumnName="FNAME",pkColumnValue="Notadepedido" , valueColumnName="FKEY",
    allocationSize=1) 
    @GeneratedValue(strategy=GenerationType.TABLE,generator="NoPeIdGen")
    private Long id;
    @Column(name = "ANTICIPO",precision=15,scale=3)
    private BigDecimal anticipo;
    @Column(name = "SALDO",precision=15,scale=3)
    private BigDecimal saldo;
    @Column(name = "HORACOMPRA")
    @Temporal(TemporalType.TIME)
    private Date horacompra;
    @Column(name = "ENTREGADO")
    private Character entregado;
    @Column(name = "ID_USUARIO_EXPIDIO_NOTA")
    private Integer idUsuarioExpidioNota;
    @Column(name = "STOCKFUTURO")
    private Integer stockfuturo;
    @Column(name = "FECHA_ANULADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulado;
    @Column(name = "IDUSUARIO_ENTREGADO")
    private Integer idusuarioEntregado;
    @Column(name = "ANULADO")
    private Character anulado;
    @Column(name = "FECHADECOMPRA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadecompra;
    @Column(name = "OBSERVACIONES",length=32_000)
    private String observaciones;
    @Column(name = "PENDIENTE")
    private Character pendiente;
    @Column(name = "MONTOIVA",precision=12,scale=2)
    private BigDecimal montoiva;
    @Column(name = "RECARGO",precision=15,scale=3)
    private BigDecimal recargo;
    @Column(name = "IDUSUARIO_ANULADO")
    private Integer idusuarioAnulado;
    @Column(name = "TOTAL",precision=15,scale=3)
    private BigDecimal total;    
    @Column(name = "NUMERODECUPON",length=20)
    private String numerodecupon;
    @Column(name = "ENEFECTIVO")
    private Character enefectivo;
    @Column(name="FECHAENTREGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaentrega;
     @Column(name = "DESCUENTO_PESOS",precision=15,scale=3)
    private BigDecimal descuentoPesos;
    @OneToMany(mappedBy = "notadepedido",cascade = CascadeType.ALL)
    private List<Detallesnotadepedido> detallesnotadepedidoList;
    @OneToMany(mappedBy = "fkidnotapedido",cascade = CascadeType.ALL)
    private List<Historiconotapedido> historiconotapedidoList;
    @JoinColumn(name = "IDTARJETAFK_IDTARJETA", referencedColumnName = "IDTARJETA")
    @ManyToOne(optional = false)
    private TarjetasCreditoDebito idTarjetaFk;
    @JoinColumn(name = "FK_IDCLIENTE", referencedColumnName = "ID_PERSONA")
    @ManyToOne(optional = false)
    private Personas fkIdcliente;
    @JoinColumn(name = "FKIDPORCENTAJENOTA_ID", referencedColumnName = "ID_PORCENTAJES")
    @ManyToOne(optional = false)
    private Porcentajes fkidporcentajenotaId;
    @Column(name="CANCELADO",length=1)
    private Character cancelado;
    @Column(name="FECANCELADO")
    @Temporal(TemporalType.DATE)
    private Date fecancelado;
    @Column(name="ID_USUARIO_CANCELO")
    private Integer idUsuarioCancelo;
    @Column(name="DESCUENTO_NOTA",precision=15,scale=3)
    private BigDecimal descuentoNota;
    @Column(name = "MONTOTOTALAPAGAR",precision=15,scale=3)
    private BigDecimal montototalapagar;
    @Column(name = "PORCDESCTOTAL",precision=12,scale=2)
    private BigDecimal porcdesctotal;
    @Column(name = "PORCRECARGO",precision=12,scale=2)
    private BigDecimal porcrecargo;

    /**
     *
     */
    public Notadepedido() {
    }

    /**
     *
     * @param id
     */
    public Notadepedido(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public BigDecimal getAnticipo() {
        return anticipo;
    }

    /**
     *
     * @param anticipo
     */
    public void setAnticipo(BigDecimal anticipo) {
        this.anticipo = anticipo;
    }

    /**
     *
     * @return
     */
    public BigDecimal getSaldo() {
        return saldo;
    }

    /**
     *
     * @param saldo
     */
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    /**
     *
     * @return
     */
    public Date getHoracompra() {
        return horacompra;
    }

    /**
     *
     * @param horacompra
     */
    public void setHoracompra(Date horacompra) {
        this.horacompra = horacompra;
    }

    /**
     *
     * @return
     */
    public Character getEntregado() {
        return entregado;
    }

    /**
     *
     * @param entregado
     */
    public void setEntregado(Character entregado) {
        this.entregado = entregado;
    }

    /**
     *
     * @return
     */
    public Integer getIdUsuarioExpidioNota() {
        return idUsuarioExpidioNota;
    }

    /**
     *
     * @param idUsuarioExpidioNota
     */
    public void setIdUsuarioExpidioNota(Integer idUsuarioExpidioNota) {
        this.idUsuarioExpidioNota = idUsuarioExpidioNota;
    }

    /**
     *
     * @return
     */
    public Integer getStockfuturo() {
        return stockfuturo;
    }

    /**
     *
     * @param stockfuturo
     */
    public void setStockfuturo(Integer stockfuturo) {
        this.stockfuturo = stockfuturo;
    }

    /**
     *
     * @return
     */
    public Date getFechaAnulado() {
        return fechaAnulado;
    }

    /**
     *
     * @return
     */
    public BigDecimal getDescuentonota() {
        return descuentoNota;
    }

    /**
     *
     * @param descuentonota
     */
    public void setDescuentonota(BigDecimal descuentonota) {
        this.descuentoNota = descuentonota;
    }

    /**
     *
     * @return
     */
    public BigDecimal getDescuentoPesos() {
        return descuentoPesos;
    }

    /**
     *
     * @param descuentoPesos
     */
    public void setDescuentoPesos(BigDecimal descuentoPesos) {
        this.descuentoPesos = descuentoPesos;
    }

    /**
     *
     * @param fechaAnulado
     */
    public void setFechaAnulado(Date fechaAnulado) {
        this.fechaAnulado = fechaAnulado;
    }

    /**
     *
     * @return
     */
    public Integer getIdusuarioEntregado() {
        return idusuarioEntregado;
    }

    /**
     *
     * @param idusuarioEntregado
     */
    public void setIdusuarioEntregado(Integer idusuarioEntregado) {
        this.idusuarioEntregado = idusuarioEntregado;
    }

    /**
     *
     * @return
     */
    public Character getAnulado() {
        return anulado;
    }

    /**
     *
     * @param anulado
     */
    public void setAnulado(Character anulado) {
        this.anulado = anulado;
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
    public Character getPendiente() {
        return pendiente;
    }

    /**
     *
     * @param pendiente
     */
    public void setPendiente(Character pendiente) {
        this.pendiente = pendiente;
    }

    /**
     *
     * @return
     */
    public BigDecimal getMontoiva() {
        return montoiva;
    }

    /**
     *
     * @param montoiva
     */
    public void setMontoiva(BigDecimal montoiva) {
        this.montoiva = montoiva;
    }

    /**
     *
     * @return
     */
    public BigDecimal getRecargo() {
        return recargo;
    }

    /**
     *
     * @param recargo
     */
    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    /**
     *
     * @return
     */
    public Integer getIdusuarioAnulado() {
        return idusuarioAnulado;
    }

    /**
     *
     * @param idusuarioAnulado
     */
    public void setIdusuarioAnulado(Integer idusuarioAnulado) {
        this.idusuarioAnulado = idusuarioAnulado;
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
    public Porcentajes getFkidporcentajenotaId() {
        return fkidporcentajenotaId;
    }

    /**
     *
     * @param fkidporcentajenotaId
     */
    public void setFkidporcentajenotaId(Porcentajes fkidporcentajenotaId) {
        this.fkidporcentajenotaId = fkidporcentajenotaId;
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
     * @param numerodecupon
     */
    public void setNumerodecupon(String numerodecupon) {
        this.numerodecupon = numerodecupon;
    }

    /**
     *
     * @return
     */
    public Character getEnefectivo() {
        return enefectivo;
    }

    /**
     *
     * @param enefectivo
     */
    public void setEnefectivo(Character enefectivo) {
        this.enefectivo = enefectivo;
    }

    /**
     *
     * @return
     */
    @XmlTransient
    public List<Detallesnotadepedido> getDetallesnotadepedidoList() {
        return Collections.unmodifiableList(detallesnotadepedidoList);
    }

    /**
     *
     * @param detallesnotadepedidoList
     */
    public void setDetallesnotadepedidoList(List<Detallesnotadepedido> detallesnotadepedidoList) {
        this.detallesnotadepedidoList = detallesnotadepedidoList;
    }

    /**
     *
     * @return
     */
    public Date getFechaentrega() {
        return fechaentrega;
    }

    /**
     *
     * @param fechaentrega
     */
    public void setFechaentrega(Date fechaentrega) {
        this.fechaentrega = fechaentrega;
    }

    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    @XmlTransient
    public List<Historiconotapedido> getHistoriconotapedidoList() {
        return Collections.unmodifiableList(historiconotapedidoList);
    }

    /**
     *
     * @param historiconotapedidoList
     */
    public void setHistoriconotapedidoList(List<Historiconotapedido> historiconotapedidoList) {
        this.historiconotapedidoList = historiconotapedidoList;
    }

    /**
     *
     * @return
     */
    public TarjetasCreditoDebito getIdTarjetaFk() {
        return idTarjetaFk;
    }

    /**
     *
     * @param idTarjetaFk
     */
    public void setIdTarjetaFk(TarjetasCreditoDebito idTarjetaFk) {
        this.idTarjetaFk = idTarjetaFk;
    }

    /**
     *
     * @return
     */
    public Personas getFkIdcliente() {
        return fkIdcliente;
    }

    /**
     *
     * @param fkIdcliente
     */
    public void setFkIdcliente(Personas fkIdcliente) {
        this.fkIdcliente = fkIdcliente;
    }

    /**
     *
     * @return
     */
    public Date getFechadecompra() {
        return fechadecompra;
    }

    /**
     *
     * @param fechadecompra
     */
    public void setFechadecompra(Date fechadecompra) {
        this.fechadecompra = fechadecompra;
    }

    /**
     *
     * @return
     */
    public Character getCancelado() {
        return cancelado;
    }

    /**
     *
     * @param cancelado
     */
    public void setCancelado(Character cancelado) {
        this.cancelado = cancelado;
    }

    /**
     *
     * @return
     */
    public Date getFecancelado() {
        return fecancelado;
    }

    /**
     *
     * @param fecancelado
     */
    public void setFecancelado(Date fecancelado) {
        this.fecancelado = fecancelado;
    }

    /**
     *
     * @return
     */
    public Integer getIdusuariocancelo() {
        return idUsuarioCancelo;
    }

    /**
     *
     * @param idusuariocancelo
     */
    public void setIdusuariocancelo(Integer idusuariocancelo) {
        this.idUsuarioCancelo = idusuariocancelo;
    }

    /**
     *
     * @return
     */
    public BigDecimal getMontototalapagar() {
        return montototalapagar;
    }

    /**
     *
     * @param montototalapagar
     */
    public void setMontototalapagar(BigDecimal montototalapagar) {
        this.montototalapagar = montototalapagar;
    }

    /**
     *
     * @return
     */
    public BigDecimal getPorcdesctotal() {
        return porcdesctotal;
    }

    /**
     *
     * @param porcdesctotal
     */
    public void setPorcdesctotal(BigDecimal porcdesctotal) {
        this.porcdesctotal = porcdesctotal;
    }

    /**
     *
     * @return
     */
    public BigDecimal getPorcrecargo() {
        return porcrecargo;
    }

    /**
     *
     * @param porcrecargo
     */
    public void setPorcrecargo(BigDecimal porcrecargo) {
        this.porcrecargo = porcrecargo;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notadepedido)) {
            return false;
        }
        Notadepedido other = (Notadepedido) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
    @Override
    public String toString() {
        return "com.melani.entity.Notadepedido[ id=" + id + " ]";
    }

    /**
     *
     * @return
     */
    public String toXML(){
        //---------------------------------------------------------------------
         StringBuilder item =new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfh = new SimpleDateFormat("HH:mm:ss");
        //---------------------------------------------------------------------
        String fechanulado="";
        if (this.getFechaAnulado()!=null) {
            fechanulado=sdf.format(this.getFechaAnulado());
         }
        String feentrega="";
       if (this.getFechaentrega()!=null) {
           feentrega =sdf.format(this.getFechaentrega());
         }
        String fecompra = "";
       if(this.getFechadecompra()!=null) {
           fecompra =  sdf.format(this.getFechadecompra());
         }
        String hocompra = "";
        if (this.getHoracompra()!=null) {
            hocompra=sdfh.format(this.getHoracompra());
         }
        String fecancel="";
        if(this.getFecancelado()!=null) {
            fecancel=sdf.format(this.getFecancelado());
            //--------------------------------------------------------------------
         }        
            
            
                
                        item.append("<item>\n").append("<id>").append(this.getId()).append("</id>\n").append("<tarjetadecredito>").append(StringEscapeUtils.escapeXml10(this.getIdTarjetaFk().getDescripcion())).append("</tarjetadecredito>\n"); 
                            item.append("<idtarjeta>").append(this.getIdTarjetaFk().getIdtarjeta()).append("</idtarjeta>\n");
                            item.append("<numerocupon>").append(this.getNumerodecupon().toString()).append("</numerocupon>\n");
                           item.append("<observaciones>").append(StringEscapeUtils.escapeXml10(this.getObservaciones())).append("</observaciones>\n");
                            item.append("<anticipo>").append(this.getAnticipo().toPlainString()).append("</anticipo>\n");
                            item.append("<anulado>").append(this.getAnulado().toString()).append("</anulado>\n");
                            item.append("<cancelado>").append(this.getCancelado()).append("</cancelado>\n");
                            item.append("<efectivo>").append(this.getEnefectivo()).append("</efectivo>\n");
                            item.append("<entregado>").append(this.getEntregado()).append("</entregado>\n");                           
                            item.append("<descuentonota>").append(this.getDescuentonota().toPlainString()).append("</descuentonota>\n");
                            item.append("<descuentopesos>").append(this.getDescuentoPesos().toPlainString()).append("</descuentopesos>\n");
                            item.append("<fechaanulado>").append(fechanulado).append("</fechaanulado>\n"); 
                            item.append("<fecancel>").append(fecancel).append("</fecancel>\n");
                             item.append("<fechacompra>").append(fecompra).append("</fechacompra>\n");
                            item.append("<fechaentrega>").append(feentrega).append("</fechaentrega>\n");
                            item.append("<horacompra>").append(hocompra).append("</horacompra>\n");    
                            item.append("<stockfuturo>").append(this.getStockfuturo()).append("</stockfuturo>\n");
                            item.append("<cliente>\n" + "<id>").append(this.getFkIdcliente().getIdPersona()).append("</id>\n" + "<nrodocumento>").append(this.getFkIdcliente().getNrodocumento()).append("</nrodocumento>\n" + "<apellidoynombre>").append(StringEscapeUtils.escapeXml10(this.getFkIdcliente().getApellido()+" "+this.getFkIdcliente().getNombre())).append("</apellidoynombre>\n" + "</cliente>\n" + "<idporcentajes>").append(this.getFkidporcentajenotaId().getIdPorcentajes()).append("</idporcentajes>\n" + "<usuarioexpidionota>").append(this.getIdUsuarioExpidioNota()).append("</usuarioexpidionota>\n" + "<usuarioanulonota>").append(this.getIdusuarioAnulado()).append("</usuarioanulonota>\n" + "<usuarioentregonota>").append(this.getIdusuarioEntregado()).append("</usuarioentregonota>\n" + "<usuariocancelonota>").append(this.getIdusuariocancelo()).append("</usuariocancelonota>\n" + "<idusuarioexpidionota>").append(this.getIdUsuarioExpidioNota()).append("</idusuarioexpidionota>\n" + "<idusuarioanulonota>").append(this.getIdusuarioAnulado()).append("</idusuarioanulonota>\n" + "<idusuarioentregonota>").append(this.getIdusuarioEntregado()).append("</idusuarioentregonota>\n" + "<idusuariocancelonota>").append(this.getIdusuariocancelo()).append("</idusuariocancelonota>\n" + "<montoiva>").append(this.getMontoiva().toPlainString()).append("</montoiva>\n" + "<pendiente>").append(this.getPendiente()).append("</pendiente>\n" + "<recargo>").append(this.getRecargo().toPlainString()).append("</recargo>\n" + "<total>").append(this.getTotal().toPlainString()).append("</total>\n" + "<saldo>").append(this.getSaldo().toPlainString()).append("</saldo>\n" + "<montototalapagar>").append(this.getMontototalapagar().toPlainString()).append("</montototalapagar>\n" + "<porcentajedesctotal>").append(this.getPorcdesctotal().toPlainString()).append("</porcentajedesctotal>\n" + "<recargototal>").append(this.getRecargo().toPlainString()).append("</recargototal>\n" + "<porcrecargo>").append(this.getPorcrecargo().toPlainString()).append("</porcrecargo>\n");
                            item.append("<detallenota>\n");
                                if(this.getDetallesnotadepedidoList().isEmpty()) {
                                    item.append("</detallenota>\n");
                                } else{
                                    for (Detallesnotadepedido detallenota : this.getDetallesnotadepedidoList()) {
                                        item.append(detallenota.toXML());
                                    }
                                    item.append("</detallenota>\n");
                                }
                                item.append("<detallehistorico>\n");
                                if(this.getHistoriconotapedidoList().isEmpty()) {
                                    item.append("</detallehistorico>\n");
                                } else{
                                    for (Historiconotapedido historico : this.getHistoriconotapedidoList()) {
                                        item.append(historico.toXML());
                                    }
                                    item.append("</detallehistorico>\n");
                                }
                             item.append("</item>\n");     
              
                    return item.toString();
              
        
    }
}
