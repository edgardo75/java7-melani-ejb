package com.melani.entity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("EMPFULLTIME")
public class FullTimeEmpleado extends Empleados implements Serializable{
    @Column(name="SALARIO",precision = 10,scale=2)
    private double salario;

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
    @Override
    public String toXML(){
        String xml = "<salario>" + this.getSalario() + "</salario>\n";
        return xml;
    }
}
