/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
/**
 * A Entity Persoastelefonos
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name = "PERSONASTELEFONOS")
@NamedQueries({
    @NamedQuery(name = "Personastelefonos.findAll", query = "SELECT p FROM Personastelefonos p"),
    @NamedQuery(name = "Personastelefonos.findByDetalles", query = "SELECT p FROM Personastelefonos p WHERE p.detalles = :detalles"),
    @NamedQuery(name = "Personastelefonos.findByEstado", query = "SELECT p FROM Personastelefonos p WHERE p.estado = :estado"),
    @NamedQuery(name = "Personastelefonos.findByPrefijo", query = "SELECT p FROM Personastelefonos p WHERE p.personastelefonosPK.prefijo = :prefijo"),
    @NamedQuery(name = "Personastelefonos.findByNumerotel", query = "SELECT p FROM Personastelefonos p WHERE p.personastelefonosPK.numerotel = :numerotel"),
    @NamedQuery(name = "Personastelefonos.findByIdPersona", query = "SELECT p FROM Personastelefonos p WHERE p.personastelefonosPK.idPersona = :idPersona"),
    @NamedQuery(name = "Personastelefonos.addRelationship",query = "SELECT p FROM Personastelefonos p WHERE p.personastelefonosPK.prefijo = :prefijo and " +
                            "p.personastelefonosPK.numerotel = :numerotel and p.personastelefonosPK.idPersona = :idPersona")})
public class Personastelefonos implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @EmbeddedId
    protected PersonastelefonosPK personastelefonosPK;
    @Column(name = "ESTADO", length = 50)
    private String estado;
    @Column(name = "DETALLES", length = 60)
    private String detalles;
    @JoinColumn(name = "ID_PERSONA", referencedColumnName = "ID_PERSONA", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false,fetch=FetchType.LAZY)
    private Personas idPersona;
    @JoinColumns({
        @JoinColumn(name = "NUMEROTEL", referencedColumnName = "NUMERO", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "PREFIJO", referencedColumnName = "ID_PREFIJO", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false,fetch=FetchType.LAZY)
    private Telefonos telefonos;

    /**
     *
     */
    public Personastelefonos() {
    }

    /**
     *
     * @param personastelefonosPK
     */
    public Personastelefonos(PersonastelefonosPK personastelefonosPK) {
        this.personastelefonosPK = personastelefonosPK;
    }

    /**
     *
     * @param numerotel
     * @param prefijo
     * @param idPersona
     */
    public Personastelefonos(int numerotel, long prefijo, long idPersona) {
        this.personastelefonosPK = new PersonastelefonosPK(numerotel, prefijo, idPersona);
    }

    /**
     *
     * @return
     */
    public PersonastelefonosPK getPersonastelefonosPK() {
        return personastelefonosPK;
    }

    /**
     *
     * @param personastelefonosPK
     */
    public void setPersonastelefonosPK(PersonastelefonosPK personastelefonosPK) {
        this.personastelefonosPK = personastelefonosPK;
    }

    /**
     *
     * @return
     */
    public String getEstado() {
        return estado;
    }

    /**
     *
     * @param estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     *
     * @return
     */
    public String getDetalles() {
        return detalles;
    }

    /**
     *
     * @param detalles
     */
    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    /**
     *
     * @return
     */
    public Personas getIdPersona() {
        return idPersona;
    }

    /**
     *
     * @param idPersona
     */
    public void setIdPersona(Personas idPersona) {
        this.idPersona = idPersona;
    }

    /**
     *
     * @return
     */
    public Telefonos getTelefonos() {
        return telefonos;
    }

    /**
     *
     * @param telefonos
     */
    public void setTelefonos(Telefonos telefonos) {
        this.telefonos = telefonos;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personastelefonosPK != null ? personastelefonosPK.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personastelefonos)) {
            return false;
        }
        Personastelefonos other = (Personastelefonos) object;
        return (this.personastelefonosPK != null || other.personastelefonosPK == null) && (this.personastelefonosPK == null || this.personastelefonosPK.equals(other.personastelefonosPK));
    }
    @Override
    public String toString() {
        return "com.tarjetadata.dao.ejb.Personas.Personastelefonos[PersonastelefonosPK=" + personastelefonosPK + "]";
    }

    /**
     *
     * @return
     */
    public String toXML(){
            String item=telefonos.toXML();
                return item;
    }
}
