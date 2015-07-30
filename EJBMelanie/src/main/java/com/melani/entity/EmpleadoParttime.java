/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
/**
 * A Entity EmpleadoParttime
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@DiscriminatorValue("EMPPARTTIME")
@NamedQueries({@NamedQuery(name = "EmpleadoParttime.deleteById",
        query = "DELETE FROM EmpleadoParttime e WHERE e.idPersona = :idPersona")})
public class EmpleadoParttime extends Empleados implements Serializable{
    @Column(name="SALARIO_POR_HORA",precision=10,scale=2)
    private BigDecimal salarioporhora;

    /**
     *
     * @return
     */
    public BigDecimal getSalarioporhora() {
        return salarioporhora;
    }

    /**
     *
     * @param salarioporhora
     */
    public void setSalarioporhora(BigDecimal salarioporhora) {
        this.salarioporhora = salarioporhora;
    }

    /**
     *
     * @return
     */
    @Override
    public String toXML(){
        String xml = "<salarioporhora>" + this.getSalarioporhora() + "</salarioporhora>\n";
        return xml;
    }
}
