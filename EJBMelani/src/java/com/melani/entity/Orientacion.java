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
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * A Entity Orientacion
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name="ORIENTACION")
public class Orientacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id   
    @Column(name="ID_ORIENTACION",nullable=false,precision=10)
    private Long id;
    @Column(length=10,name="DESCRIPCION",nullable=false)
    private String descripcion;
    @OneToMany(mappedBy = "idorientacion",fetch=FetchType.LAZY)
    private List<Domicilios> domicilioss;

    /**
     *
     */
    public Orientacion(){}

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
        if (!(object instanceof Orientacion)) {
            return false;
        }
        Orientacion other = (Orientacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "com.melani.entity.Orientaciones[id=" + id + "]";
    }
}
