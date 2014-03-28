/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * A Entity Telefonos
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name = "TELEFONOS")
@NamedQueries({
    @NamedQuery(name = "Telefonos.findAll", query = "SELECT t FROM Telefonos t"),
    @NamedQuery(name = "Telefonos.findByIdPrefijo", query = "SELECT t FROM Telefonos t WHERE t.telefonosPK.idPrefijo = :idPrefijo"),
    @NamedQuery(name = "Telefonos.findByNumero", query = "SELECT t FROM Telefonos t WHERE t.telefonosPK.numero = :numero")})
    public class Telefonos implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @EmbeddedId
    protected TelefonosPK telefonosPK;
    @JoinColumn(name = "ID_EMPRESATELEFONIA", referencedColumnName = "ID_EMP_TELEFONIA", nullable = false)
    @ManyToOne(cascade={CascadeType.ALL},optional = false,fetch=FetchType.LAZY)
    private EmpresaTelefonia idEmpresatelefonia;
    @JoinColumn(name = "ID_TIPOTELEFONO", referencedColumnName = "ID_TIPOTEL")
    @ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.LAZY)
    private Tipostelefono idTipotelefono;
     @OneToMany(cascade = CascadeType.ALL, mappedBy = "telefonos")
    private List<Personastelefonos> personastelefonosCollection;

    /**
     *
     */
    public Telefonos() {
    }

    /**
     *
     * @param telefonosPK
     */
    public Telefonos(TelefonosPK telefonosPK) {
        this.telefonosPK = telefonosPK;
    }

    /**
     *
     * @param numero
     * @param idPrefijo
     */
    public Telefonos(Integer numero, long idPrefijo) {
        this.telefonosPK = new TelefonosPK(numero, idPrefijo);
    }

    /**
     *
     * @return
     */
    public TelefonosPK getTelefonosPK() {
        return telefonosPK;
    }

    /**
     *
     * @param telefonosPK
     */
    public void setTelefonosPK(TelefonosPK telefonosPK) {
        this.telefonosPK = telefonosPK;
    }

    /**
     *
     * @return
     */
    public EmpresaTelefonia getIdEmpresatelefonia() {
        return idEmpresatelefonia;
    }

    /**
     *
     * @param idEmpresatelefonia
     */
    public void setIdEmpresatelefonia(EmpresaTelefonia idEmpresatelefonia) {
        this.idEmpresatelefonia = idEmpresatelefonia;
    }

    /**
     *
     * @return
     */
    public Tipostelefono getIdTipotelefono() {
        return idTipotelefono;
    }

    /**
     *
     * @param idTipotelefono
     */
    public void setIdTipotelefono(Tipostelefono idTipotelefono) {
        this.idTipotelefono = idTipotelefono;
    }

    /**
     *
     * @return
     */
    public List<Personastelefonos> getPersonastelefonosCollection() {
        return personastelefonosCollection;
    }

    /**
     *
     * @param personastelefonosCollection
     */
    public void setPersonastelefonosCollection(List<Personastelefonos> personastelefonosCollection) {
        this.personastelefonosCollection = personastelefonosCollection;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (telefonosPK != null ? telefonosPK.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Telefonos)) {
            return false;
        }
        Telefonos other = (Telefonos) object;
        if ((this.telefonosPK == null && other.telefonosPK != null) || (this.telefonosPK != null && !this.telefonosPK.equals(other.telefonosPK))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "com.tarjetadata.dao.entidades.Telefonos[telefonosPK=" + telefonosPK + "]";
    }

    /**
     *
     * @return
     */
    public String toXML() {
        String xml = "<telefono>\n" +
                        "<idempresatelefonia>" + this.getIdEmpresatelefonia().getidEmpTelefonia() + "</idempresatelefonia>\n" +
                            "<descripcion>" + this.getIdEmpresatelefonia().getNombre() + "</descripcion>\n"+
                            "<prefijo>" +this.getTelefonosPK().getIdPrefijo()+"</prefijo>\n"
                            + "<nrotelefono>"+this.getTelefonosPK().getNumero()+"</nrotelefono>\n" +
                            "<tipotelefono>"+this.getIdTipotelefono().getDescripcion()+"</tipotelefono>\n" +
                            "<idtipotel>"+this.getIdTipotelefono().getIdTipotel()+"</idtipotel>\n"+
                      "</telefono>\n";
        return xml;
    }
}
