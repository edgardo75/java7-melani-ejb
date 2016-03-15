package com.melani.entity;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name="TIPOSDOCUMENTO") 
@NamedQueries({
    @NamedQuery(name = "TiposDocumento.findAll", query = "SELECT t FROM TiposDocumento t"),
    @NamedQuery(name = "TiposDocumento.findById", query = "SELECT t FROM TiposDocumento t WHERE t.id = :id"),
    @NamedQuery(name = "TiposDocumento.findByDescripcion", query = "SELECT t FROM TiposDocumento t WHERE t.descripcion = :descripcion")})
public class TiposDocumento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id    
    @Column(name="ID",nullable=false,updatable=false)
    private Short id;
   
    @Column(length=20,name="DESCRIPCION")
    protected String descripcion;
    @OneToMany( mappedBy = "tipodocumento")
    private List<Personas> personasList;

    public TiposDocumento(){}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public List<Personas> getPersonasList() {
        return Collections.unmodifiableList(personasList);
    }

    public void setPersonasList(List<Personas> personasList) {
        this.personasList = personasList;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {       
        if (!(object instanceof TiposDocumento)) {
            return false;
        }
        TiposDocumento other = (TiposDocumento) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
    @Override
    public String toString() {
        return "com.melani.entity.TiposDocumento[id=" + id + "]";
    }
}