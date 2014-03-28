/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang3.StringEscapeUtils;
/**
 * A Entity Domicilios
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name="DOMICILIOS")
@NamedQueries({
    @NamedQuery(name = "Domicilios.findAll", query = "SELECT d FROM Domicilios d"),
    @NamedQuery(name = "Domicilios.findByIdDomicilio", query = "SELECT d FROM Domicilios d WHERE d.id = :id"),
    @NamedQuery(name = "Domicilios.findByPiso", query = "SELECT d FROM Domicilios d WHERE d.piso = :piso"),
    @NamedQuery(name = "Domicilios.findByEntrecalleycalle", query = "SELECT d FROM Domicilios d WHERE d.entrecalleycalle = :entrecalleycalle"),
    @NamedQuery(name = "Domicilios.findByNumero", query = "SELECT d FROM Domicilios d WHERE d.numero = :numero"),
    @NamedQuery(name = "Domicilios.findByMonoblock", query = "SELECT d FROM Domicilios d WHERE d.monoblock = :monoblock"),
    @NamedQuery(name = "Domicilios.findByArea", query = "SELECT d FROM Domicilios d WHERE d.area = :area"),
    @NamedQuery(name = "Domicilios.findBySector", query = "SELECT d FROM Domicilios d WHERE d.sector = :sector"),
    @NamedQuery(name = "Domicilios.findByManzana", query = "SELECT d FROM Domicilios d WHERE d.manzana = :manzana"),
    @NamedQuery(name = "Domicilios.findByNumdepto", query = "SELECT d FROM Domicilios d WHERE d.numdepto = :numdepto"),
    @NamedQuery(name = "Domicilios.findByObservaciones", query = "SELECT d FROM Domicilios d WHERE d.observaciones = :obsrvaciones"),
    @NamedQuery(name = "Domicilios.findByTorre", query = "SELECT d FROM Domicilios d WHERE d.torre = :torre")})
public class Domicilios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name="DomiIdGen", table="ID_GEN_DOM",
    pkColumnName="FNAME",pkColumnValue="Domicilios" , valueColumnName="FKEY",
    allocationSize=1)
    @GeneratedValue(generator="DomiIdGen",strategy=GenerationType.TABLE)
    @Column(name="ID_DOMICILIO")
    private Long id;
    @Column(name="NUMERO",precision = 10)
    private int numero;
    @Column(name="SECTOR",columnDefinition="VARCHAR(10)")
    private String sector;
    @Column(name = "AREA",columnDefinition="VARCHAR(10)")
    private String area;
    @Column(name = "MONOBLOCK",columnDefinition="VARCHAR(10)")
    private String monoblock;
    @Column(name = "PISO",columnDefinition="VARCHAR(10)")
    private String piso;
    @Column(name = "NUMDEPTO")
    private Integer numdepto;
    @Column(name = "ENTRECALLEYCALLE",columnDefinition="VARCHAR(255) default 'DATO NO INGRESADO'")
    private String entrecalleycalle;
    @Column(name="MANZANA",columnDefinition="VARCHAR(10)")
    private String manzana;
    @Column(name="OBSERVACIONES",columnDefinition="VARCHAR(5000)")
    private String observaciones;
    @Column(name = "TORRE",columnDefinition="VARCHAR(10)")
    private String torre;        
    @JoinColumn(name="ID_BARRIO",referencedColumnName="ID_BARRIO",nullable=false,updatable=false)    
    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    private Barrios idbarrio;
    @JoinColumn(name="ID_CALLE",referencedColumnName="ID_CALLE",nullable=false,updatable=false)
    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    private Calles idcalle;
    @JoinColumn(name = "ID_ORIENTACION", referencedColumnName = "ID_ORIENTACION",nullable=false,updatable=false)
    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    private Orientacion idorientacion;
    @JoinColumn(name = "ID_LOCALIDAD", referencedColumnName = "ID_LOCALIDAD",nullable=false,updatable=false)
    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    private Localidades localidades;
    @OneToMany(mappedBy = "domicilioss",fetch=FetchType.LAZY)
    private List<PersonasDomicilios> personasDomicilioss;

    /**
     *
     */
    public Domicilios(){}

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
    public String getArea() {
        return area;
    }

    /**
     *
     * @param area
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     *
     * @return
     */
    public Barrios getBarrios() {
        return idbarrio;
    }

    /**
     *
     * @return
     */
    public String getTorre() {
        return torre;
    }

    /**
     *
     * @param torre
     */
    public void setTorre(String torre) {
        this.torre = torre;
    }

    /**
     *
     * @param idbarrio
     */
    public void setBarrios(Barrios idbarrio) {
        this.idbarrio = idbarrio;
    }

    /**
     *
     * @return
     */
    public Calles getCalles() {
        return idcalle;
    }

    /**
     *
     * @param idcalle
     */
    public void setCalles(Calles idcalle) {
        this.idcalle = idcalle;
    }

    /**
     *
     * @return
     */
    public Localidades getLocalidades() {
        return localidades;
    }

    /**
     *
     * @param localidades
     */
    public void setLocalidades(Localidades localidades) {
        this.localidades = localidades;
    }

    /**
     *
     * @return
     */
    public String getMonoblock() {
        return monoblock;
    }

    /**
     *
     * @param monoblock
     */
    public void setMonoblock(String monoblock) {
        this.monoblock = monoblock;
    }

    /**
     *
     * @return
     */
    public int getNumero() {
        return numero;
    }

    /**
     *
     * @param numero
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     *
     * @return
     */
    public Orientacion getOrientacion() {
        return idorientacion;
    }

    /**
     *
     * @param idorientacion
     */
    public void setOrientacion(Orientacion idorientacion) {
        this.idorientacion = idorientacion;
    }

    /**
     *
     * @return
     */
    public String getPiso() {
        return piso;
    }

    /**
     *
     * @return
     */
    public String getSector() {
        return sector;
    }

    /**
     *
     * @param sector
     */
    public void setSector(String sector) {
        this.sector = sector;
    }

    /**
     *
     * @return
     */
    public String getEntrecalleycalle() {
        return entrecalleycalle;
    }

    /**
     *
     * @param entrecalleycalle
     */
    public void setEntrecalleycalle(String entrecalleycalle) {
        this.entrecalleycalle = entrecalleycalle;
    }

    /**
     *
     * @return
     */
    public List<PersonasDomicilios> getPersonasDomicilioss() {
        return Collections.unmodifiableList(personasDomicilioss);
    }

    /**
     *
     * @param personasDomicilioss
     */
    public void setPersonasDomicilioss(List<PersonasDomicilios> personasDomicilioss) {
        this.personasDomicilioss = personasDomicilioss;
    }

    /**
     *
     * @return
     */
    public Barrios getIdbarrio() {
        return idbarrio;
    }

    /**
     *
     * @param idbarrio
     */
    public void setIdbarrio(Barrios idbarrio) {
        this.idbarrio = idbarrio;
    }

    /**
     *
     * @return
     */
    public Calles getIdcalle() {
        return idcalle;
    }

    /**
     *
     * @param idcalle
     */
    public void setIdcalle(Calles idcalle) {
        this.idcalle = idcalle;
    }

    /**
     *
     * @return
     */
    public Orientacion getIdorientacion() {
        return idorientacion;
    }

    /**
     *
     * @param idorientacion
     */
    public void setIdorientacion(Orientacion idorientacion) {
        this.idorientacion = idorientacion;
    }

    /**
     *
     * @return
     */
    public String getManzana() {
        return manzana;
    }

    /**
     *
     * @param manzana
     */
    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    /**
     *
     * @return
     */
    public Integer getNumdepto() {
        return numdepto;
    }

    /**
     *
     * @param numdepto
     */
    public void setNumdepto(Integer numdepto) {
        this.numdepto = numdepto;
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
     * @param piso
     */
    public void setPiso(String piso) {
        this.piso = piso;
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
        if (!(object instanceof Domicilios)) {
            return false;
        }
        Domicilios other = (Domicilios) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
    @Override
    public String toString() {
        return "com.melani.entity.Domicilios[id=" + id + "]";
    }

    /**
     *
     * @return
     */
    public String toXML(){
        String item="<domicilio>\n"
                + "<id>"+this.getId()+"</id>\n"
                    + "<Barrio>\n"
                        + "<idbarrio>"+this.getBarrios().getId()+"</idbarrio>\n"
                        + "<descbarrio>"+StringEscapeUtils.escapeXml(this.getBarrios().getDescripcion())+"</descbarrio>\n"
                    + "</Barrio>\n"
                + "<Calle>\n"
                    + "<idcalle>"+this.getCalles().getId()+"</idcalle>\n"
                    + "<desccalle>"+StringEscapeUtils.escapeXml(this.getCalles().getDescripcion())+"</desccalle>\n"
                + "</Calle>\n"
                + "<Orientacion>\n"
                    + "<idorientacion>"+this.getOrientacion().getId()+"</idorientacion>\n"
                    + "<descorientacion>"+this.getOrientacion().getDescripcion()+"</descorientacion>\n"
                + "</Orientacion>\n"
                + "<Localidad>\n"
                    + "<idlocalidad>"+this.getLocalidades().getIdLocalidad()+"</idlocalidad>\n"
                    + "<codigopostal>"+this.getLocalidades().getCodigopostal()+"</codigopostal>\n"
                    + "<idprovincia>"+this.getLocalidades().getProvincias().getIdProvincia()+"</idprovincia>\n"
                + "<desclocalidad>"+StringEscapeUtils.escapeXml(this.getLocalidades().getDescripcion())+"</desclocalidad>\n"
                + "</Localidad>\n"
                + "<area>"+StringEscapeUtils.escapeXml(this.getArea())+"</area>\n"
                + "<entrecalleycalle>"+StringEscapeUtils.escapeXml(this.getEntrecalleycalle())+"</entrecalleycalle>\n"
                + "<manzana>"+StringEscapeUtils.escapeXml(this.getManzana())+"</manzana>\n"
                + "<monoblock>"+StringEscapeUtils.escapeXml(this.getMonoblock())+"</monoblock>\n"
                + "<numdepto>"+this.getNumdepto()+"</numdepto>\n"
                + "<numero>"+this.getNumero()+"</numero>\n"
                + "<piso>"+StringEscapeUtils.escapeXml(this.getPiso())+"</piso>\n"
                + "<sector>"+StringEscapeUtils.escapeXml(this.getSector())+"</sector>\n" +
                "<torre>"+StringEscapeUtils.escapeXml(this.getTorre())+"</torre>\n"
                +"<observaciones>"+StringEscapeUtils.escapeXml(this.getObservaciones())+"</observaciones>\n"
                + "</domicilio>\n";
    return item;
    }
}
