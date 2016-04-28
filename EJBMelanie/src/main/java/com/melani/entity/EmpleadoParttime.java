package com.melani.entity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("EMPPARTTIME")
@NamedQueries({@NamedQuery(name = "EmpleadoParttime.deleteById",
        query = "DELETE FROM EmpleadoParttime e WHERE e.idPersona = :idPersona")})
public class EmpleadoParttime extends Empleados implements Serializable{
    @Column(name="SALARIO_POR_HORA",precision=10,scale=2)
    private double salarioporhora;

    public double getSalarioporhora() {
        return salarioporhora;
    }

    public void setSalarioporhora(double salarioporhora) {
        this.salarioporhora = salarioporhora;
    }

    @Override
    public String toXML(){
        String xml = "<salarioporhora>" + this.getSalarioporhora() + "</salarioporhora>\n";
        return xml;
    }
}
