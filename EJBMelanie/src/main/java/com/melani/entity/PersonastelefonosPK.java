    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
/**
 *
 * @author Admin
 */
@Embeddable
public class PersonastelefonosPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "NUMEROTEL", nullable = false)
    private long numerotel;
    @Basic(optional = false)
    @Column(name = "PREFIJO", nullable = false)
    private long prefijo;
    @Basic(optional = false)
    @Column(name = "ID_PERSONA", nullable = false)
    private long idPersona;

    /**
     *
     */
    public PersonastelefonosPK() {
    }

    /**
     *
     * @param numerotel
     * @param prefijo
     * @param idPersona
     */
    public PersonastelefonosPK(long numerotel, long prefijo, long idPersona) {
        this.numerotel = numerotel;
        this.prefijo = prefijo;
        this.idPersona = idPersona;
    }

    /**
     *
     * @return
     */
    public Long getNumerotel() {
        return numerotel;
    }

    /**
     *
     * @param numerotel
     */
    public void setNumerotel(Integer numerotel) {
        this.numerotel = numerotel;
    }

    /**
     *
     * @return
     */
    public long getPrefijo() {
        return prefijo;
    }

    /**
     *
     * @param prefijo
     */
    public void setPrefijo(long prefijo) {
        this.prefijo = prefijo;
    }

    /**
     *
     * @return
     */
    public Long getIdClienteyapa() {
        return idPersona;
    }

    /**
     *
     * @param idPersona
     */
    public void setIdClienteyapa(Long idPersona) {
        this.idPersona = idPersona;
    }
     @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) prefijo;
        hash += (int) numerotel;
        hash += (int) idPersona;
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonastelefonosPK)) {
            return false;
        }
        PersonastelefonosPK other = (PersonastelefonosPK) object;
        if (this.prefijo != other.prefijo) {
            return false;
        }
        if (this.numerotel != other.numerotel) {
            return false;
        }
        return this.idPersona == other.idPersona;
    }
    @Override
    public String toString() {
        return "entity.PersonastelefonosPK[prefijo=" + prefijo + ", numerotel=" + numerotel + ", idPersona=" + idPersona + "]";
    }
}
