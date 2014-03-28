/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang3.StringEscapeUtils;
/**
 * A Entity Localidades
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name = "LOCALIDADES")
@NamedQueries({
    @NamedQuery(name = "Localidades.findAll", query = "SELECT l FROM Localidades l"),
    @NamedQuery(name = "Localidades.findByIdLocalidad", query = "SELECT l FROM Localidades l WHERE l.idLocalidad = :idLocalidad"),
    @NamedQuery(name = "Localidades.findByDescripcion", query = "SELECT l FROM Localidades l WHERE l.descripcion = :descripcion"),
    @NamedQuery(name = "Localidades.findByCodigopostal", query = "SELECT l FROM Localidades l WHERE l.codigopostal = :codigopostal")})
public class Localidades implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name="LocalidadIdGen", table="ID_GEN_LOC",
    pkColumnName="FNAME",pkColumnValue="Localidades" , valueColumnName="FKEY",
    allocationSize=1)
    @Id    
    @GeneratedValue(generator="LocalidadIdGen",strategy=GenerationType.TABLE)
    @Column(name = "ID_LOCALIDAD")
    private Long idLocalidad;
    @Column(name = "CODIGOPOSTAL")
    private Integer codigopostal;
    @Column(name = "DESCRIPCION",length=100)
    private String descripcion;    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localidades", fetch = FetchType.LAZY)
    private List<Domicilios> domiciliosList;
    @JoinColumn(name = "ID_PROVINCIA", referencedColumnName = "ID_PROVINCIA")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Provincias provincias;

    /**
     *
     */
    public Localidades() {
    }

    /**
     *
     * @param idLocalidad
     */
    public Localidades(Long idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    /**
     *
     * @return
     */
    public Long getIdLocalidad() {
        return idLocalidad;
    }

    /**
     *
     * @param idLocalidad
     */
    public void setIdLocalidad(Long idLocalidad) {
        this.idLocalidad = idLocalidad;
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
    public Integer getCodigopostal() {
        return codigopostal;
    }

    /**
     *
     * @param codigopostal
     */
    public void setCodigopostal(Integer codigopostal) {
        this.codigopostal = codigopostal;
    }

    /**
     *
     * @return
     */
    public List<Domicilios> getDomiciliosList() {
        return Collections.unmodifiableList(domiciliosList);
    }

    /**
     *
     * @param domiciliosList
     */
    public void setDomiciliosList(List<Domicilios> domiciliosList) {
        this.domiciliosList = domiciliosList;
    }

    /**
     *
     * @return
     */
    public Provincias getProvincias() {
        return provincias;
    }

    /**
     *
     * @param provincias
     */
    public void setProvincias(Provincias provincias) {
        this.provincias = provincias;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLocalidad != null ? idLocalidad.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Localidades)) {
            return false;
        }
        Localidades other = (Localidades) object;
        if ((this.idLocalidad == null && other.idLocalidad != null) || (this.idLocalidad != null && !this.idLocalidad.equals(other.idLocalidad))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "entity.Localidades[idLocalidad=" + idLocalidad + "]";
    }

    /**
     *
     * @return
     */
    public String toXML(){
        String item="<localidades>\n"
                + "<id>"+this.getIdLocalidad()+"</id>\n"
                + "<descripcion>"+StringEscapeUtils.escapeXml(this.getDescripcion())+"</descripcion>\n"
                + "<codigopostal>"+this.getCodigopostal()+"</codigopostal>\n"
                + "</localidades>\n";
        return item;
    }
}
