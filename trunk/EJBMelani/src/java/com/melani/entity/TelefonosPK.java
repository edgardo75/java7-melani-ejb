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
public class TelefonosPK implements Serializable {    
    @Basic(optional = false)
    @Column(name = "NUMERO", nullable = false)
    private long numero;
    @Basic(optional = false)
    @Column(name = "ID_PREFIJO", nullable = false)
    private long idPrefijo;

    /**
     *
     */
    public TelefonosPK() {
    }

    /**
     *
     * @param numero
     * @param idPrefijo
     */
    public TelefonosPK(long numero, long idPrefijo) {
        this.numero = numero;
        this.idPrefijo = idPrefijo;
    }

    /**
     *
     * @return
     */
    public Long getNumero() {
        return numero;
    }

    /**
     *
     * @param numero
     */
    public void setNumero(long numero) {
        this.numero = numero;
    }

    /**
     *
     * @return
     */
    public long getIdPrefijo() {
        return idPrefijo;
    }

    /**
     *
     * @param idPrefijo
     */
    public void setIdPrefijo(long idPrefijo) {
        this.idPrefijo = idPrefijo;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int)numero;
        hash += (int) idPrefijo;
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TelefonosPK)) {
            return false;
        }
        TelefonosPK other = (TelefonosPK) object;
        if (this.numero != other.idPrefijo) {
            return false;
        }
           if(this.numero != other.numero) {
               return false;
        }
        if (this.idPrefijo != other.idPrefijo) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "com.tarjetadata.dao.entidades.TelefonosPK[numero=" + numero + ", idPrefijo=" + idPrefijo + "]";
    }
}
