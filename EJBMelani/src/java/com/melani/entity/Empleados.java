/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
/**
 *A Entity Empleados
 * @version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@DiscriminatorValue("EMP")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="emptype",discriminatorType=DiscriminatorType.STRING) 
@NamedQueries({@NamedQuery(name = "Empleados.deleteById",query = "DELETE FROM Empleados e WHERE e.idPersona = :idPersona")})
public abstract class Empleados extends Personas {

    /**
     *
     */
    @Column(name="PASSWORD",nullable=false)
    protected String password;

    /**
     *
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Date fechacarga;

    /**
     *
     */
    @Column(name = "emptype",length = 10)
    protected String emptype;
     @Column(name="NAME_USER",unique=true,nullable = false) 
     private String nameuser;

    /**
     *
     */
    @Column(name="ESTADO")
     protected Short estado;
     
    /**
     *
     */
    public Empleados(){}

    /**
     *
     * @return
     */
    public Date getFechacarga() {
        return fechacarga;
    }

    /**
     *
     * @param fechacarga
     */
    public void setFechacarga(Date fechacarga) {
        this.fechacarga = fechacarga;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getEmptype() {
        return emptype;
    }

    /**
     *
     * @param emptype
     */
    public void setEmptype(String emptype) {
        this.emptype = emptype;
    }

    /**
     *
     * @return
     */
    public String getNameuser() {
        return nameuser;
    }

    /**
     *
     * @param nameuser
     */
    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    /**
     *
     * @return
     */
    public Short getEstado() {
        return estado;
    }

    /**
     *
     * @param estado
     */
    public void setEstado(Short estado) {
        this.estado = estado;
    }

    /**
     *
     * @return
     */
    public String toXMLEmpleado(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder item = new StringBuilder("<nameuser>"+this.getNameuser()+"</nameuser>");
                 item.append("<emptype>").append(this.getEmptype()).append("</emptype>\n");
                item.append("<estado>").append(this.getEstado()).append("</estado>\n");
                item.append("<fechacarga>").append(sdf.format(this.getFechacarga())).append("</fechacarga>\n");
        return item.toString();
    }
}
