/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * A Entity PersonasDomicilios
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name = "PERSONASDOMICILIOS")
@NamedQueries({
    @NamedQuery(name = "PersonasDomicilios.findAll", query = "SELECT p FROM PersonasDomicilios p"),
    @NamedQuery(name = "PersonasDomicilios.findByFechaingresovivienda", query = "SELECT p FROM PersonasDomicilios p WHERE p.fechaingresovivienda = :fechaingresovivienda"),
    @NamedQuery(name = "PersonasDomicilios.findByEstado", query = "SELECT p FROM PersonasDomicilios p WHERE p.estado = :estado"),
    @NamedQuery(name = "PersonasDomicilios.findByIddomicilio", query = "SELECT p FROM PersonasDomicilios p WHERE p.personasdomiciliosPK.iddomicilio = :iddomicilio"),
    @NamedQuery(name = "PersonasDomicilios.findByIdPersona", query = "SELECT p FROM PersonasDomicilios p WHERE p.personasdomiciliosPK.idPersona = :idPersona")})
public class PersonasDomicilios implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @EmbeddedId
    protected PersonasdomiciliosPK personasdomiciliosPK;
     @Column(name = "ESTADO", length = 20)
    private String estado;
    @Column(name = "FECHAINGRESOVIVIENDA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaingresovivienda;
    @JoinColumn(name="ID_PERSONA",referencedColumnName="ID_PERSONA",insertable=false,updatable=false)
    @ManyToOne(cascade={CascadeType.PERSIST},fetch=FetchType.EAGER,optional = false)
    private Personas personas;
    @JoinColumn(name = "IDDOMICILIO", referencedColumnName = "ID_DOMICILIO",insertable = false, updatable = false)
    @ManyToOne(cascade={CascadeType.PERSIST},optional=false,fetch=FetchType.EAGER)
    private Domicilios domicilioss;

    /**
     *
     */
    public PersonasDomicilios() {
    }

    /**
     *
     * @param personasdomiciliosPK
     */
    public PersonasDomicilios(PersonasdomiciliosPK personasdomiciliosPK) {
        this.personasdomiciliosPK = personasdomiciliosPK;
    }

    /**
     *
     * @param id
     * @param idPersona
     */
    public PersonasDomicilios(Long id,Long idPersona){
        this.personasdomiciliosPK = new PersonasdomiciliosPK(id,idPersona);
    }

    /**
     *
     * @return
     */
    public Domicilios getDomicilioss() {
        return domicilioss;
    }

    /**
     *
     * @param domicilioss
     */
    public void setDomicilioss(Domicilios domicilioss) {
        this.domicilioss = domicilioss;
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
    public Date getFechaingresovivienda() {
        return fechaingresovivienda;
    }

    /**
     *
     * @param fechaingresovivienda
     */
    public void setFechaingresovivienda(Date fechaingresovivienda) {
        this.fechaingresovivienda = fechaingresovivienda;
    }

    /**
     *
     * @return
     */
    public Personas getPersonas() {
        return personas;
    }

    /**
     *
     * @param personas
     */
    public void setPersonas(Personas personas) {
        this.personas = personas;
    }

    /**
     *
     * @return
     */
    public PersonasdomiciliosPK getPersonasdomiciliosPK() {
        return personasdomiciliosPK;
    }

    /**
     *
     * @param personasdomiciliosPK
     */
    public void setPersonasdomiciliosPK(PersonasdomiciliosPK personasdomiciliosPK) {
        this.personasdomiciliosPK = personasdomiciliosPK;
    }

    /**
     *
     * @return
     */
    public String toXML(){
        String item=domicilioss.toXML();
    return item;
}
}
