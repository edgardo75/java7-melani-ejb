/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
/**
 *
 * @author Edgardo
 */
@Entity
@Table
public class HistPersonasTelefonos implements Serializable {
    @Id
    @TableGenerator(name="HistPerTelIdGen", table="ID_GEN_HPT",
    pkColumnName="FNAME",pkColumnValue="HistPersonasTelefonos", valueColumnName="FKEY",
    allocationSize=1)
    @GeneratedValue(generator="HistPerTelIdGen",strategy=GenerationType.TABLE)
    private Long idhistperdom;
    private Long numerotel;
    private Long prefijo;
    private Long idpersona;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechacambio;
    private Integer idusuario;

    /**
     *
     * @return
     */
    public Long getIdhistperdom() {
        return idhistperdom;
    }

    /**
     *
     * @param idhistperdom
     */
    public void setIdhistperdom(Long idhistperdom) {
        this.idhistperdom = idhistperdom;
    }

    /**
     *
     * @return
     */
    public Date getFechacambio() {
        return fechacambio;
    }

    /**
     *
     * @param fechacambio
     */
    public void setFechacambio(Date fechacambio) {
        this.fechacambio = fechacambio;
    }

    /**
     *
     * @return
     */
    public Long getIdpersona() {
        return idpersona;
    }

    /**
     *
     * @param idpersona
     */
    public void setIdpersona(Long idpersona) {
       this.idpersona = idpersona;
    }

    /**
     *
     * @return
     */
    public Integer getIdusuario() {
        return idusuario;
    }

    /**
     *
     * @param idusuario
     */
    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
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
    public void setNumerotel(Long numerotel) {
        this.numerotel = numerotel;
    }

    /**
     *
     * @return
     */
    public Long getPrefijo() {
        return prefijo;
    }

    /**
     *
     * @param prefijo
     */
    public void setPrefijo(Long prefijo) {
        this.prefijo = prefijo;
    }

    /**
     *
     * @return
     */
    public String toXML(){
        String item="<item>\n" +
                "<id>"+this.getIdhistperdom()+"</id>\n" +
                "<prefijo>"+this.getPrefijo()+"</prefijo>\n" +
                "<numerotel>"+this.getNumerotel()+"</numerotel>\n" +
                "<fecha>"+this.getFechacambio()+"</fecha>\n" +
                "<idusuario>"+this.getIdusuario()+"</idusuario>\n" +
               "</item>\n";
        return item;
    }
}