/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Collections;
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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.StringEscapeUtils;
/**
 * A Entity Productos
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name = "PRODUCTOS")
@NamedQueries({
    @NamedQuery(name = "Productos.findAll", query = "SELECT p FROM Productos p"),
    @NamedQuery(name = "Productos.findBySid", query = "SELECT p FROM Productos p WHERE p.sid = :sid"),
    @NamedQuery(name = "Productos.findByCantidadInicial", query = "SELECT p FROM Productos p WHERE p.cantidadInicial = :cantidadInicial"),
    @NamedQuery(name = "Productos.findByCantidadDisponible", query = "SELECT p FROM Productos p WHERE p.cantidadDisponible = :cantidadDisponible"),
    @NamedQuery(name = "Productos.findByPrecioUnitario", query = "SELECT p FROM Productos p WHERE p.precioUnitario = :precioUnitario"),
    @NamedQuery(name = "Productos.findByFecha", query = "SELECT p FROM Productos p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "Productos.findByDescripcion", query = "SELECT p FROM Productos p WHERE p.descripcion = :descripcion")})
public class Productos implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name="ProductoIdGen", table="ID_GEN_PROD",
    pkColumnName="FNAME",pkColumnValue="Productos" , valueColumnName="FKEY",
    allocationSize=1)
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="ProductoIdGen",strategy=GenerationType.TABLE)
    @Column(name = "SID")
    private Long sid;
    @Column(name = "DESCRIPCION",length=100,nullable=false,unique=true)
    private String descripcion;
    @Column(name="CODPRODUCTO",length=100,unique=true,nullable=true)
    private String codproducto;
    @Column(name = "PRECIOUNITARIO",scale=2,precision=12)
    private BigDecimal precioUnitario;
    @Column(name = "CANTIDADINICIAL",precision=10)
    private BigInteger cantidadInicial;
    @Column(name = "CANTIDADDISPONIBLE",precision=10)
    private BigInteger cantidadDisponible;
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Lob @Basic(fetch=FetchType.EAGER)
    @Column(name = "IMG")
    private byte[] img;
    @OneToMany(mappedBy = "productos",cascade={CascadeType.ALL})
    private List<ExistenciasProductos> existenciasProductoss;
    @OneToMany(mappedBy = "productos",cascade = CascadeType.ALL)
    private List<Detallesnotadepedido> detallesnotadepedidoList;
    @OneToMany( mappedBy = "productos",cascade = CascadeType.ALL)
    private List<Detallespresupuesto> detallepresupuestosList;
    @OneToMany(mappedBy = "productos",cascade = CascadeType.ALL)
    private List<ImagenesProductos>imagenesProductosList;

    /**
     *
     */
    public Productos(){}

    /**
     *
     * @param sid
     */
    public Productos(Long sid) {
        this.sid = sid;
    }

    /**
     *
     * @return
     */
    public Long getSid() {
        return sid;
    }

    /**
     *
     * @param sid
     */
    public void setSid(Long sid) {
        this.sid = sid;
    }

    /**
     *
     * @return
     */
    public BigInteger getCantidadDisponible() {
        return cantidadDisponible;
    }

    /**
     *
     * @param cantidadDisponible
     */
    public void setCantidadDisponible(BigInteger cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    /**
     *
     * @return
     */
    public BigInteger getCantidadInicial() {
        return cantidadInicial;
    }

    /**
     *
     * @param cantidadInicial
     */
    public void setCantidadInicial(BigInteger cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }

    /**
     *
     * @return
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     *
     * @param descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     *
     * @return
     */
    public List<ExistenciasProductos> getExistenciasProductoss() {
        return Collections.unmodifiableList(existenciasProductoss);
    }

    /**
     *
     * @return
     */
    public String getCodproducto() {
        return codproducto;
    }

    /**
     *
     * @param codproducto
     */
    public void setCodproducto(String codproducto) {
        this.codproducto = codproducto;
    }

    /**
     *
     * @param existenciasProductoss
     */
    public void setExistenciasProductoss(List<ExistenciasProductos> existenciasProductoss) {
        this.existenciasProductoss = existenciasProductoss;
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
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     *
     * @param precioUnitario
     */
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     *
     * @return
     */
    public byte[] getImg() {
        return img;
    }

    /**
     *
     * @param img
     */
    public void setImg(byte[] img) {
        this.img = img;
    }

    /**
     *
     * @return
     */
    public List<Detallespresupuesto> getDetallepresupuestosList() {
        return Collections.unmodifiableList(detallepresupuestosList);
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
    public List<ImagenesProductos> getImagenesProductosList() {
        return Collections.unmodifiableList(imagenesProductosList);
    }

    /**
     *
     * @param imagenesProductosList
     */
    public void setImagenesProductosList(List<ImagenesProductos> imagenesProductosList) {
        this.imagenesProductosList = imagenesProductosList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sid != null ? sid.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productos)) {
            return false;
        }
        Productos other = (Productos) object;
        if ((this.sid == null && other.sid != null) || (this.sid != null && !this.sid.equals(other.sid))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "com.melani.entity.Productos[id=" + sid + "]";
    }

    /**
     *
     * @return
     */
    public String toXML(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String item="<producto>\n" +
                "<id>" +this.getSid()+"</id>\n" +
                "<descripcion>"+StringEscapeUtils.escapeXml(this.getDescripcion())+"</descripcion>\n" +
                "<cantidadinicial>"+this.getCantidadInicial()+"</cantidadinicial>\n" +
                "<cantidaddisponible>"+this.getCantidadDisponible()+"</cantidaddisponible>\n" +
                "<fechacarga>"+sdf.format(this.getFecha())+"</fechacarga>\n" +
                "<preciovigente>"+this.getPrecioUnitario().toString()+"</preciovigente>"
                + "<img>"+this.getImagenesProductosList().size()+"</img>\n";
                item+="<existencias>\n";
                    if(this.getExistenciasProductoss().isEmpty()) {
                        item+="</existencias>\n";
        } else{
                        List<ExistenciasProductos>lista = this.getExistenciasProductoss();
            for (ExistenciasProductos existenciasProductos : lista) {
                item+=existenciasProductos.toXML();
            }
                        item+="</existencias>\n";
                    }
                    item+="<listImages>\n";
                    if(this.getImagenesProductosList().isEmpty()) {
                        item+="</listImages>\n";
        } else{
                        List<ImagenesProductos>lista = this.getImagenesProductosList();
                        for(ImagenesProductos i:lista){
                            item+="<pathImage>"+i.getPathImagenEnDisco()+"</pathImage>\n";
                        }
                                item+="</listImages>\n";
                    }
                    
        item+=        "</producto>";
    return item;
    }
}
