package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("EMPFULLTIME")
public class FullTimeEmpleado extends Empleados implements Serializable{
    @Column(name="SALARIO",precision = 10,scale=2)
    private BigDecimal salario;

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }
    @Override
    public String toXML(){
        String xml = "<salario>" + this.getSalario() + "</salario>\n";
        return xml;
    }
}
