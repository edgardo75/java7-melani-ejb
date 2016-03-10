
package com.melani.entity;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DetallesnotadepedidoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "FK_IDNOTA")
    private long fkIdnota;
    @Basic(optional = false)
    @Column(name = "FK_IDPRODUCTO")
    private long fkIdproducto;

    public DetallesnotadepedidoPK() {
    }

    public DetallesnotadepedidoPK(long fkIdnota, long fkIdproducto) {
        this.fkIdnota = fkIdnota;
        this.fkIdproducto = fkIdproducto;
    }

    public long getFkIdnota() {
        return fkIdnota;
    }

    public void setFkIdnota(long fkIdnota) {
        this.fkIdnota = fkIdnota;
    }

    public long getFkIdproducto() {
        return fkIdproducto;
    }

    public void setFkIdproducto(int fkIdproducto) {
        this.fkIdproducto = fkIdproducto;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) fkIdnota;
        hash += (int) fkIdproducto;
        return hash;
    }
    @Override
    public boolean equals(Object object) {        
        if (!(object instanceof DetallesnotadepedidoPK)) {
            return false;
        }
        DetallesnotadepedidoPK other = (DetallesnotadepedidoPK) object;
        if (this.fkIdnota != other.fkIdnota) {
            return false;
        }
        return this.fkIdproducto == other.fkIdproducto;
    }
    @Override
    public String toString() {
        return "entity.DetallesnotadepedidoPK[fkIdnota=" + fkIdnota + ", fkIdproducto=" + fkIdproducto + "]";
    }
}