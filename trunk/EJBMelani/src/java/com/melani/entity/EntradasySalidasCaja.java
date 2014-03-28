/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author Edgardo
 */
@Entity(name="EntradasySalidasCaja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EntradasySalidasCaja.findAll", query = "SELECT e FROM EntradasySalidasCaja e"),
    @NamedQuery(name = "EntradasySalidasCaja.findById", query = "SELECT e FROM EntradasySalidasCaja e WHERE e.id = :id"),
    @NamedQuery(name = "EntradasySalidasCaja.findByDetalles", query = "SELECT e FROM EntradasySalidasCaja e WHERE e.detalles = :detalles"),
    @NamedQuery(name = "EntradasySalidasCaja.findByFecha", query = "SELECT e FROM EntradasySalidasCaja e WHERE e.fecha = :fecha"),
    @NamedQuery(name = "EntradasySalidasCaja.findByEntradas", query = "SELECT e FROM EntradasySalidasCaja e WHERE e.entradas = :entradas"),
    @NamedQuery(name = "EntradasySalidasCaja.findBySalidas", query = "SELECT e FROM EntradasySalidasCaja e WHERE e.salidas = :salidas"),
    @NamedQuery(name = "EntradasySalidasCaja.findByIdUsuario", query = "SELECT e FROM EntradasySalidasCaja e WHERE e.idUsuario = :idUsuario"),
    @NamedQuery(name = "EntradasySalidasCaja.findByNumerocupon", query = "SELECT e FROM EntradasySalidasCaja e WHERE e.numerocupon = :numerocupon"),
    @NamedQuery(name = "EntradasySalidasCaja.findByEnefectivo", query = "SELECT e FROM EntradasySalidasCaja e WHERE e.enefectivo = :enefectivo"),
    @NamedQuery(name = "EntradasySalidasCaja.findByHora", query = "SELECT e FROM EntradasySalidasCaja e WHERE e.hora = :hora")})
public class EntradasySalidasCaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE,generator="EntradasySalidasCajaIdGen")
    @TableGenerator(name="EntradasySalidasCajaIdGen", table="ID_GEN_ENTRADASYSALIDASCAJA",
    pkColumnName="FNAME",pkColumnValue="EntradasySalidasCaja", valueColumnName="FKEY",
    allocationSize=1)
    private Integer id;
    @Column(name = "DETALLES")
    private String detalles;
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "ENTRADAS")
    private Long entradas;
    @Column(name = "SALIDAS")
    private Long salidas;
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;
    @Column(name = "NUMEROCUPON")
    private String numerocupon;
    @Column(name = "ENEFECTIVO")
    private Character enefectivo;
    @Column(name = "HORA")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @JoinColumn(name = "ID_TARJETA_FK", referencedColumnName = "IDTARJETA")
    @ManyToOne(optional = false)
    private TarjetasCreditoDebito idTarjetaFk;

    /**
     *
     */
    public EntradasySalidasCaja() {
    }

    /**
     *
     * @param id
     */
    public EntradasySalidasCaja(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getDetalles() {
        return detalles;
    }

    /**
     *
     * @param detalles
     */
    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    /**
     *
     * @return
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     *
     * @param fecha
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     *
     * @return
     */
    public Long getEntradas() {
        return entradas;
    }

    /**
     *
     * @param entradas
     */
    public void setEntradas(Long entradas) {
        this.entradas = entradas;
    }

    /**
     *
     * @return
     */
    public Long getSalidas() {
        return salidas;
    }

    /**
     *
     * @param salidas
     */
    public void setSalidas(Long salidas) {
        this.salidas = salidas;
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
    public String getNumerocupon() {
        return numerocupon;
    }

    /**
     *
     * @param numerocupon
     */
    public void setNumerocupon(String numerocupon) {
        this.numerocupon = numerocupon;
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
    public Date getHora() {
        return hora;
    }

    /**
     *
     * @param hora
     */
    public void setHora(Date hora) {
        this.hora = hora;
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
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntradasySalidasCaja)) {
            return false;
        }
        EntradasySalidasCaja other = (EntradasySalidasCaja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "com.melani.entity.EntradasySalidasCaja[ id=" + id + " ]";
    }

    /**
     *
     * @return
     */
    public String toXML(){
    SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfh = new SimpleDateFormat("HH:mm:ss");
    String item="<item>\n"
            + "<id>"+this.getId()+"</id>\n"
            + "<detalles>"+this.getDetalles()+"</detalles>\n"
            + "<numerocupon>"+this.getNumerocupon()+"</numerocupon>\n"
            + "<enefectivo>"+this.getEnefectivo()+"</enefectivo>\n"
            + "<entradas>"+this.getEntradas()+"</entradas>\n"
            + "<fecha>"+sdf.format(this.getFecha())+"</fecha>\n"
            + "<hora>"+sdfh.format(this.getHora())+"</hora>\n"
            + "<idtarjeta>"+this.getIdTarjetaFk().getIdtarjeta()+"</idtarjeta>\n"
            + "<idusuario>"+this.getIdUsuario()+"</idusuario>\n"
            + "<salidas>"+this.getSalidas()+"</salidas>\n"
            + "</item>\n";
return item;
}    
}
