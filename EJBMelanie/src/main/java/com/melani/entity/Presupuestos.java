package com.melani.entity;
import java.io.Serializable;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
        query = "SELECT p FROM Presupuestos p WHERE p.descuentoresto = :descuentoresto"),
        @NamedQuery(name = "Presupuesto.findPresupuestoOrderByFechaIdPresupesto",query = "SELECT p FROM Presupuestos p ORDER BY p.fechapresupuesto DESC, p.idPresupuesto DESC"),
        @NamedQuery(name = "Presupuesto.findIdPresupuestoOrderByFechaIdPresupesto",query = "SELECT p FROM Presupuestos p WHERE p.idPresupuesto = ?1 ORDER BY p.fechapresupuesto DESC, p.idPresupuesto DESC")})
public class Presupuestos implements Serializable {
    private static final long serialVersionUID = 1L;
  @TableGenerator(name="PresupuestosIdGen", table="ID_GEN_PRE",
    pkColumnName="FNAME",pkColumnValue="Presupuestos" , valueColumnName="FKEY",
    allocationSize=1)
    @Id
    @GeneratedValue(generator="PresupuestosIdGen",strategy=GenerationType.TABLE)
    @Basic(optional = false)
    @Column(name = "ID_PRESUPUESTO")
    private int idPresupuesto;
    @Column(name = "FECHAPRESUPUESTO")
    @Temporal(TemporalType.DATE)
    private Date fechapresupuesto;
    @Column(name = "VALIDEZ")
    @Temporal(TemporalType.DATE)
    private Date validez;
    @Column(name = "TOTAL",precision=15,scale=3)
    private double total;
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "OBSERVACIONES",length=5_000)
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "ID_USUARIO_EXPIDIO_PRESUPUESTO")
    private int idUsuarioFk;    
    @Column(name="TOTALAPAGAR",precision=15,scale=3)
    private double totalapagar;
    @Column(name = "IVA",precision=15,scale=3)
    private double iva;
    @Column(name = "NOMBRE",length=40)
    private String nombre;
    @Column(name = "APELLIDO",length=20)
    private String apellido;
    @Column(name="PORC_DESC_TOTAL",precision=15,scale=2)
    private double porcetajedescuentoTOTAL;
    @Column(name = "RECARGOTOTAL",precision=15,scale=2)
    private double recargototal;
    @Column(name = "PORCENTAJERECARGO",precision=15,scale=2)
    private double porcentajerecargo;
     @Column(name = "DESCUENTORESTO",precision=15,scale=3)
    private double descuentoresto;
    @OneToMany(orphanRemoval = true, mappedBy = "presupuestos",cascade = CascadeType.REMOVE)
    private List<Detallespresupuesto> detallepresupuestosList;

    public Presupuestos() {
    }

    public Presupuestos(int idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public Presupuestos(int idPresupuesto, int idUsuarioFk) {
        this.idPresupuesto = idPresupuesto;
        this.idUsuarioFk = idUsuarioFk;
    }

    public int getIdPresupuesto() {
        return idPresupuesto;
    }

    public void setIdPresupuesto(int idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public Date getFechapresupuesto() {
        return fechapresupuesto;
    }

    public void setFechapresupuesto(Date fechapresupuesto) {
        this.fechapresupuesto = fechapresupuesto;
    }

    public Date getValidez() {
        return validez;
    }

    public void setValidez(Date validez) {
        this.validez = validez;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getIdUsuarioFk() {
        return idUsuarioFk;
    }

    public void setIdUsuarioFk(int idUsuarioFk) {
        this.idUsuarioFk = idUsuarioFk;
    }

    public List<Detallespresupuesto> getDetallepresupuestosList() {
        return Collections.unmodifiableList(detallepresupuestosList);
    }

    public void setDetallepresupuestosList(List<Detallespresupuesto> detallepresupuestosList) {
        this.detallepresupuestosList = detallepresupuestosList;
    }

    public double getTotalapagar() {
        return totalapagar;
    }

    public void setTotalapagar(double totalapagar) {
        this.totalapagar = totalapagar;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPorcetajedescuentoTOTAL() {
        return porcetajedescuentoTOTAL;
    }

    public void setPorcetajedescuentoTOTAL(double porcetajedescuento) {
        this.porcetajedescuentoTOTAL = porcetajedescuento;
    }

    public double getDescuentoresto() {
        return descuentoresto;
    }

    public void setDescuentoresto(double descuentoresto) {
        this.descuentoresto = descuentoresto;
    }

    public double getPorcentajerecargo() {
        return porcentajerecargo;
    }


    public void setPorcentajerecargo(double porcentajerecargo) {
        this.porcentajerecargo = porcentajerecargo;
    }

    public double getRecargototal() {
        return recargototal;
    }

    public void setRecargototal(double recargototal) {
        this.recargototal = recargototal;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.idPresupuesto;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Presupuestos other = (Presupuestos) obj;
        if (this.idPresupuesto != other.idPresupuesto) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "entidades.Presupuestos[idPresupuesto=" + idPresupuesto + "]";
    }

    public String toXML(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder xml = new StringBuilder("<Item>\n").append("<id>").append(this.getIdPresupuesto()).append("</id>\n");
                xml.append("<nombre>").append(this.getNombre()).append("</nombre>\n");
                xml.append("<apellido>").append(this.getApellido()).append("</apellido>\n");                
                xml.append("<observaciones>").append(this.getObservaciones()).append("</observaciones>\n");
                xml.append("<totalapagar>").append(this.getTotalapagar()).append("</totalapagar>\n");
                xml.append("<usuarioexpidio>").append(this.getIdUsuarioFk()).append("</usuarioexpidio>\n");
                xml.append("<iva>").append(this.getIva()).append("</iva>\n");
                xml.append("<total>").append(this.getTotal()).append("</total>\n");
                xml.append("<fechapresupuesto>").append(sdf.format(this.getFechapresupuesto())).append("</fechapresupuesto>\n");
                xml.append("<fechavalidez>").append(sdf.format(this.getValidez())).append("</fechavalidez>\n" );
                xml.append("<porcentajedescuentototal>").append(this.getPorcetajedescuentoTOTAL()).append("</porcentajedescuentototal>\n");
                xml.append("<descuentoresto>").append(this.getDescuentoresto()).append("</descuentoresto>\n");
                xml.append("<recargototal>").append(this.getRecargototal()).append("</recargototal>\n");
                xml.append("<porcentajerecargo>").append(this.getPorcentajerecargo()).append("</porcentajerecargo>\n");
                xml.append("<Detallepresupuesto>\n" );
                if(this.getDetallepresupuestosList().isEmpty()) {
                    xml.append("</Detallepresupuesto>\n");
        } else{
                    List<Detallespresupuesto>lista = this.getDetallepresupuestosList();
                        for (Detallespresupuesto detallespresupuesto : lista) {
                            xml.append(detallespresupuesto.toXML());
                        }
                }
            xml.append("</Detallepresupuesto>\n").append("</Item>\n");
    return xml.toString();
    }
}
