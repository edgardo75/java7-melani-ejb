/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * A Entity EmpresaTelefonia
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name = "EMPRESATELEFONIA")
@NamedQueries({
    @NamedQuery(name = "EmpresaTelefonia.findAll", query = "SELECT e FROM EmpresaTelefonia e"),
    @NamedQuery(name = "EmpresaTelefonia.findByIdEmpTelefonia", query = "SELECT e FROM EmpresaTelefonia e WHERE e.idEmpTelefonia = :idEmpTelefonia"),
    @NamedQuery(name = "EmpresaTelefonia.findByNombre", query = "SELECT e FROM EmpresaTelefonia e WHERE e.nombre = :nombre")})
public class EmpresaTelefonia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_EMP_TELEFONIA", nullable = false,updatable=false)
    private Short idEmpTelefonia;
    @Column(name = "NOMBRE", length = 30,unique=true)
    private String nombre;
    @OneToMany( mappedBy = "idEmpresatelefonia",fetch=FetchType.LAZY)
    private List<Telefonos> telefonosList;

    /**
     *
     */
    public EmpresaTelefonia() {
    }

    /**
     *
     * @param idEmpTelefonia
     */
    public EmpresaTelefonia(Short idEmpTelefonia) {
        this.idEmpTelefonia = idEmpTelefonia;
    }

    /**
     *
     * @return
     */
    public Short getidEmpTelefonia() {
        return idEmpTelefonia;
    }

    /**
     *
     * @param idEmpTelefonia
     */
    public void setidEmpTelefonia(Short idEmpTelefonia) {
        this.idEmpTelefonia = idEmpTelefonia;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @return
     */
    public List<Telefonos> getTelefonosList() {
        return Collections.unmodifiableList(telefonosList);
    }

    /**
     *
     * @param telefonosList
     */
    public void setTelefonosList(List<Telefonos> telefonosList) {
        this.telefonosList = telefonosList;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpTelefonia != null ? idEmpTelefonia.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpresaTelefonia)) {
            return false;
        }
        EmpresaTelefonia other = (EmpresaTelefonia) object;
        return (this.idEmpTelefonia != null || other.idEmpTelefonia == null) && (this.idEmpTelefonia == null || this.idEmpTelefonia.equals(other.idEmpTelefonia));
    }

    /**
     *
     * @return
     */
    public String toXML(){
        StringBuilder item = new StringBuilder("<item>\n");
                            item.append("<id>").append(this.getidEmpTelefonia()).append("</id>\n");
                            item.append("<nombre>").append(this.getNombre()).append("</nombre>\n");
                       item.append("</item>\n");
         return item.toString();
    }
}
