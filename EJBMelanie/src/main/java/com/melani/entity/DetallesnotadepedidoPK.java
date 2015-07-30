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
 * @author Edgardo
 */
@Embeddable
public class DetallesnotadepedidoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "FK_IDNOTA")
    private long fkIdnota;
    @Basic(optional = false)
    @Column(name = "FK_IDPRODUCTO")
    private long fkIdproducto;

    /**
     *
     */
    public DetallesnotadepedidoPK() {
    }

    /**
     *
     * @param fkIdnota
     * @param fkIdproducto
     */
    public DetallesnotadepedidoPK(long fkIdnota, long fkIdproducto) {
        this.fkIdnota = fkIdnota;
        this.fkIdproducto = fkIdproducto;
    }

    /**
     *
     * @return
     */
    public long getFkIdnota() {
        return fkIdnota;
    }

    /**
     *
     * @param fkIdnota
     */
    public void setFkIdnota(long fkIdnota) {
        this.fkIdnota = fkIdnota;
    }

    /**
     *
     * @return
     */
    public long getFkIdproducto() {
        return fkIdproducto;
    }

    /**
     *
     * @param fkIdproducto
     */
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
        // TODO: Warning - this method won't work in the case the id fields are not set
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
