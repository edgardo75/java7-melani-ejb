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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang3.StringEscapeUtils;
/**
 * A Entity Calles
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name="CALLES")
public class Calles implements Serializable {
    private static final long serialVersionUID = 1L;   
    @TableGenerator(name="CalleIdGen", table="ID_GEN_CALLE",
    pkColumnName="FNAME",pkColumnValue="Calles" , valueColumnName="FKEY",
    allocationSize=1)
    @Id
    @GeneratedValue(generator="CalleIdGen",strategy=GenerationType.TABLE)
    @Column(name="ID_CALLE")
    private Long id;
    @Column(length = 100,name="DESCRIPCION",nullable = false,unique=true)
    private String descripcion;
    @OneToMany(mappedBy = "idcalle",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Domicilios> domicilioss;

    /**
     *
     */
    public Calles(){}

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
    public List<Domicilios> getDomicilioss() {
        return Collections.unmodifiableList(domicilioss);
    }

    /**
     *
     * @param domicilioss
     */
    public void setDomicilioss(List<Domicilios> domicilioss) {
        this.domicilioss = domicilioss;
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
        if (!(object instanceof Calles)) {
            return false;
        }
        Calles other = (Calles) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
    @Override
    public String toString() {
        return "com.melani.entity.Calles[id=" + id + "]";
    }

    /**
     *
     * @return
     */
    public String toXML(){
        StringBuilder item = new StringBuilder("<item>\n");
                        item.append("<id>").append(this.getId()).append("</id>\n");
                        item.append("<nombre>").append(StringEscapeUtils.escapeXml10(this.getDescripcion())).append("</nombre>\n");
                      item.append("</item>\n");
        return item.toString();
    }
}
