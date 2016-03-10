
package com.melani.entity;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DetallespresupuestoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_DP_FK")
    private int idDpFk;
    @Basic(optional = false)
    @Column(name = "FK_PRODUCTO")
    private int fkProducto;

    public DetallespresupuestoPK() {
    }

    public DetallespresupuestoPK(int idDpFk, int fkProducto) {
        this.idDpFk = idDpFk;
        this.fkProducto = fkProducto;
    }

    public int getIdDpFk() {
        return idDpFk;
    }

    public void setIdDpFk(int idDpFk) {
        this.idDpFk = idDpFk;
    }

    public int getFkProducto() {
        return fkProducto;
    }

    public void setFkProducto(int fkProducto) {
        this.fkProducto = fkProducto;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += idDpFk;
        hash += fkProducto;
        return hash;
    }
    @Override
    public boolean equals(Object object) {        
        if (!(object instanceof DetallespresupuestoPK)) {
            return false;
        }
        DetallespresupuestoPK other = (DetallespresupuestoPK) object;
        if (this.idDpFk != other.idDpFk) {
            return false;
        }
        return this.fkProducto == other.fkProducto;
    }
    @Override
    public String toString() {
        return "entity.DetallespresupuestoPK[idDpFk=" + idDpFk + ", fkProducto=" + fkProducto + "]";
    }
}