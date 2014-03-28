/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang3.StringEscapeUtils;
/**
 * A Entity Personas
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name="PERSONAS")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="pertype",discriminatorType=DiscriminatorType.STRING)
@NamedQueries({
    @NamedQuery(name = "Personas.findAll", query = "SELECT p FROM Personas p"),
    @NamedQuery(name = "Personas.findByIdPersona", query = "SELECT p FROM Personas p WHERE p.idPersona = :idPersona"),
    @NamedQuery(name = "Personas.findByPertype", query = "SELECT p FROM Personas p WHERE p.pertype = :pertype"),
    @NamedQuery(name = "Personas.findByObservaciones", query = "SELECT p FROM Personas p WHERE p.observaciones = :observaciones"),
    @NamedQuery(name = "Personas.findByNombre", query = "SELECT p FROM Personas p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Personas.findByNrodocumento", query = "SELECT p FROM Personas p WHERE p.nrodocumento = :nrodocumento"),
    @NamedQuery(name = "Personas.findByEmail", query = "SELECT p FROM Personas p WHERE p.email = :email"),
    @NamedQuery(name = "Personas.findByApellido", query = "SELECT p FROM Personas p WHERE p.apellido = :apellido")})
public abstract class Personas implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name="PersonaIdGen", table="ID_GEN_PER",
    pkColumnName="FNAME",pkColumnValue="Personas", valueColumnName="FKEY",
    allocationSize=1)
    @Id
    @GeneratedValue(generator="PersonaIdGen",strategy=GenerationType.TABLE)
    @Column(name = "ID_PERSONA")
    private Long idPersona;
    @Column(name = "NOMBRE",length=30)
    private String nombre;
    @Column(name = "APELLIDO",length=30)
    private String apellido;
    @Column(name = "EMAIL",length=50, unique=true)
    private String email;
    @Column(name = "pertype",length=3)
    private String pertype;
    @Column(name="OBSERVACIONES",columnDefinition="VARCHAR(32000)")
    private String observaciones;

    /**
     *
     */
    @Column(name = "NRODOCUMENTO",precision=19,unique=true,updatable=false,nullable=false)
    protected Integer nrodocumento;
    @JoinColumn(name="ID",referencedColumnName="ID",nullable=false,updatable=false)
    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    private Tiposdocumento tipodocumento;
    @OneToMany(mappedBy = "personas",fetch=FetchType.LAZY)
    private List<PersonasDomicilios> personasDomicilioss;
    @OneToMany(mappedBy = "idPersona",fetch=FetchType.LAZY)
    private List<Personastelefonos> personastelefonoss;
    @JoinColumn(name = "ID_GENERO", referencedColumnName = "ID_GENERO")
    @ManyToOne(fetch=FetchType.LAZY,optional = false)
    private Generos generos;        

    /**
     *
     */
    public Personas(){
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

    /**
     *
     * @return
     */
    public Integer getNrodocumento() {
        return nrodocumento;
    }

    /**
     *
     * @param nrodocumento
     */
    public void setNrodocumento(Integer nrodocumento) {
        this.nrodocumento = nrodocumento;
    }

    /**
     *
     * @return
     */
    public String getApellido() {
        return apellido;
    }

    /**
     *
     * @param apellido
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     *
     * @return
     */
    public String getPertype() {
        return pertype;
    }

    /**
     *
     * @return
     */
    public Tiposdocumento getTipodocumento() {
        return tipodocumento;
    }

    /**
     *
     * @param tipodocumento
     */
    public void setTipodocumento(Tiposdocumento tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    /**
     *
     * @param pertype
     */
    public void setPertype(String pertype) {
        this.pertype = pertype;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @return
     */
    public List<PersonasDomicilios> getPersonasDomicilioss() {
        return personasDomicilioss;
    }

    /**
     *
     * @param personasDomicilioss
     */
    public void setPersonasDomicilioss(List<PersonasDomicilios> personasDomicilioss) {
        this.personasDomicilioss = personasDomicilioss;
    }

    /**
     *
     * @return
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     *
     * @param observaciones
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     *
     * @return
     */
    public List<Personastelefonos> getPersonastelefonoss() {
        return personastelefonoss;
    }

    /**
     *
     * @param personastelefonoss
     */
    public void setPersonastelefonoss(List<Personastelefonos> personastelefonoss) {
        this.personastelefonoss = personastelefonoss;
    }

    /**
     *
     * @return
     */
    public Generos getGeneros() {
        return generos;
    }

    /**
     *
     * @param generos
     */
    public void setGeneros(Generos generos) {
        this.generos = generos;
    }
     @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPersona != null ? idPersona.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personas)) {
            return false;
        }
        Personas other = (Personas) object;
        if ((this.idPersona == null && other.idPersona != null) || (this.idPersona != null && !this.idPersona.equals(other.idPersona))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "entity.Personas[idPersona=" + idPersona + "]";
    }

    /**
     *
     * @return
     */
    public String toXML(){
        String item=null;
       
            
        
        item= "<id>"+this.getIdPersona()+"</id>\n"
                + "<apellido>"+this.getApellido()+"</apellido>\n"
                + "<nombre>"+this.getNombre()+"</nombre>\n"
                + "<idtipodocu>"+this.getTipodocumento().getId()+"</idtipodocu>\n"
                + "<nrodocu>"+this.getNrodocumento()+"</nrodocu>\n"
                +"<observaciones>"+StringEscapeUtils.escapeXml(this.getObservaciones())+"</observaciones>\n"
                + "<Genero>\n"
                + "<generoId>"+this.getGeneros().getIdGenero()+"</generoId>\n"
                + "<generoDescripcion>"+this.getGeneros().getDescripcion()+"</generoDescripcion>\n"
                + "</Genero>\n"
                + "<email>"+this.getEmail()+"</email>\n";
                item+="<personadomicilio>\n";
                    if(this.getPersonasDomicilioss().isEmpty()) {
                        item+="</personadomicilio>\n";
        } else{
                        List<PersonasDomicilios>lista = this.getPersonasDomicilioss();
                        for (Iterator<PersonasDomicilios> it = lista.iterator(); it.hasNext();) {
                            PersonasDomicilios personasDomicilios = it.next();
                            item+=personasDomicilios.toXML();
                        }
                        item+="</personadomicilio>\n";
                    }
                item+="<personatelefono>\n";
                    if(this.getPersonastelefonoss().isEmpty()) {
                        item+="</personatelefono>\n";
        } else{
                        List<Personastelefonos>lista=this.getPersonastelefonoss();
                        for (Iterator<Personastelefonos> it = lista.iterator(); it.hasNext();) {
                            Personastelefonos personastelefonos = it.next();
                            item+=personastelefonos.toXML();                            
                        }
                        item+="</personatelefono>\n";
                    }
               
         
         return item;
    }
}
