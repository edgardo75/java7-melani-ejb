/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
/**
 *
 * @author Edgardo
 */
@Embeddable
public class PersonasdomiciliosPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "IDDOMICILIO", nullable = false)
    private Long iddomicilio;

    /**
     *
     */
    @Basic(optional = false)
    @Column(name = "ID_PERSONA",precision = 16)
    protected Long idPersona;

    /**
     *
     */
    public PersonasdomiciliosPK(){}

    /**
     *
     * @param idDomicilio
     * @param idPersona
     */
    public PersonasdomiciliosPK(Long idDomicilio,Long idPersona){
        this.iddomicilio=idDomicilio;
        this.idPersona = idPersona;
    }

    /**
     *
     * @return
     */
    public Long getId() {
        return iddomicilio;
    }

    /**
     *
     * @param idDomicilio
     */
    public void setId(Long idDomicilio) {
        this.iddomicilio = idDomicilio;
    }

    /**
     *
     * @return
     */
    public Long getIdPersona() {
        return idPersona;
    }

    /**
     *
     * @param idPersona
     */
    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (long) iddomicilio;
        hash += (long) idPersona;
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonasdomiciliosPK)) {
            return false;
        }
        PersonasdomiciliosPK other = (PersonasdomiciliosPK) object;
        if (!Objects.equals(this.iddomicilio, other.iddomicilio)) {
            return false;
        }
        if (!Objects.equals(this.idPersona, other.idPersona)) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "entity.PersonasdomiciliosPK[iddomicilio=" + iddomicilio + ", idPersona=" + idPersona + "]";
    }
}
