
package com.melani.entity;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TelefonosPK implements Serializable {    
    @Basic(optional = false)
    @Column(name = "NUMERO", nullable = false)
    private long numero;
    @Basic(optional = false)
    @Column(name = "ID_PREFIJO", nullable = false)
    private long idPrefijo;

    public TelefonosPK() {
    }

    public TelefonosPK(long numero, long idPrefijo) {
        this.numero = numero;
        this.idPrefijo = idPrefijo;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public long getIdPrefijo() {
        return idPrefijo;
    }

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
        return this.idPrefijo == other.idPrefijo;
    }
    @Override
    public String toString() {
        return "com.tarjetadata.dao.entidades.TelefonosPK[numero=" + numero + ", idPrefijo=" + idPrefijo + "]";
    }
}