/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang3.StringEscapeUtils;
/**
 *A Entity Provincias
 * @version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name = "PROVINCIAS")
@NamedQueries({
    @NamedQuery(name = "Provincias.findAll", query = "SELECT p FROM Provincias p"),
    @NamedQuery(name = "Provincias.findByIdProvincia", query = "SELECT p FROM Provincias p WHERE p.idProvincia = :idProvincia"),
    @NamedQuery(name = "Provincias.findByProvincia", query = "SELECT p FROM Provincias p WHERE p.provincia = :provincia"),
    @NamedQuery(name = "Provincias.findByCodigo", query = "SELECT p FROM Provincias p WHERE p.codigo = :codigo")})
public class Provincias implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PROVINCIA")
    private Short idProvincia;
    @Column(name = "PROVINCIA",length=30)
    private String provincia;
    @Column(name = "CODIGO")
    private Character codigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provincias", fetch = FetchType.LAZY)
    private List<Localidades> localidadesList;

    /**
     *
     */
    public Provincias() {
    }

    /**
     *
     * @param idProvincia
     */
    public Provincias(Short idProvincia) {
        this.idProvincia = idProvincia;
    }

    /**
     *
     * @return
     */
    public Short getIdProvincia() {
        return idProvincia;
    }

    /**
     *
     * @param idProvincia
     */
    public void setIdProvincia(Short idProvincia) {
        this.idProvincia = idProvincia;
    }

    /**
     *
     * @return
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     *
     * @param provincia
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     *
     * @return
     */
    public Character getCodigo() {
        return codigo;
    }

    /**
     *
     * @param codigo
     */
    public void setCodigo(Character codigo) {
        this.codigo = codigo;
    }

    /**
     *
     * @return
     */
    public List<Localidades> getLocalidadesList() {
        return Collections.unmodifiableList(localidadesList);
    }

    /**
     *
     * @param localidadesList
     */
    public void setLocalidadesList(List<Localidades> localidadesList) {
        this.localidadesList = localidadesList;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProvincia != null ? idProvincia.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Provincias)) {
            return false;
        }
        Provincias other = (Provincias) object;
        if ((this.idProvincia == null && other.idProvincia != null) || (this.idProvincia != null && !this.idProvincia.equals(other.idProvincia))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "entity.Provincias[idProvincia=" + idProvincia + "]";
    }

    /**
     *
     * @return
     */
    public String toXML(){
    String xml = "<provincia>\n" +
            "<id>"+this.getIdProvincia()+"</id>\n" +
            "<descripcion>"+StringEscapeUtils.escapeXml(this.getProvincia())+"</descripcion>\n" +
            "<codigo>"+this.getCodigo()+"</codigo>\n" +
            "</provincia>\n";
    return xml;
    }
}
