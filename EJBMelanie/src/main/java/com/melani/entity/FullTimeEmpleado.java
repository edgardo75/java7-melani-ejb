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
/**
 * A Entity FullTimeEmpleado
 *@version
 * @author Edgardo Alvarez
 */
@Entity
@DiscriminatorValue("EMPFULLTIME")
public class FullTimeEmpleado extends Empleados implements Serializable{
    @Column(name="SALARIO",precision = 10,scale=2)
    private BigDecimal salario;

    /**
     *
     * @return
     */
    public BigDecimal getSalario() {
        return salario;
    }

    /**
     *
     * @param salario
     */
    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    /**
     *
     * @return
     */
    @Override
    public String toXML(){
        StringBuilder xml = new StringBuilder("<salario>" ).append(this.getSalario()).append("</salario>\n");
        return xml.toString();
    }
}
