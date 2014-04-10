/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
/**
 *
 * @author Edgardo
 */
@Entity
@Table(name="HISTORICODATOSCLIENTE")
public class HistoricoDatosClientes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @TableGenerator(name="HistCliIdGen", table="ID_GEN_HCL",
    pkColumnName="FNAME",pkColumnValue="HistoricoDatosClientes", valueColumnName="FKEY",
    allocationSize=1,initialValue=1)
    @GeneratedValue(generator="HistCliIdGen",strategy=GenerationType.TABLE)
    private Long id;
    @Column(name="ID_CLI",nullable=false)
    private Long idCliente;
    @Column(name="EMAIL",columnDefinition="VARCHAR(50)")
    private String email;
    @Column(name="OBSERVACIONES",columnDefinition="VARCHAR(32000)")
    private String Observaciones;
    @Column(name="IDGENERO")
    private short idgenero;
    @Column(name="NOMBRE",columnDefinition="VARCHAR(30)")
    private String nombre;
    @Column(name="APELLIDO",columnDefinition="VARCHAR(30)")
    private String apellido;
   @Column(precision = 15,scale=3,name="TOTALCOMPRAS")
    private BigDecimal totalCompras;
    @Column(precision = 15,name = "TOTALPUNTOS")
    private BigInteger totalEnPuntos;

    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getObservaciones() {
        return Observaciones;
    }

    /**
     *
     * @param Observaciones
     */
    public void setObservaciones(String Observaciones) {
        this.Observaciones = Observaciones;
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
    public Long getIdCliente() {
        return idCliente;
    }

    /**
     *
     * @param idCliente
     */
    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    /**
     *
     * @return
     */
    public short getIdgenero() {
        return idgenero;
    }

    /**
     *
     * @param idgenero
     */
    public void setIdgenero(short idgenero) {
        this.idgenero = idgenero;
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
    public BigDecimal getTotalCompras() {
        return totalCompras;
    }

    /**
     *
     * @param totalCompras
     */
    public void setTotalCompras(BigDecimal totalCompras) {
        this.totalCompras = totalCompras;
    }

    /**
     *
     * @return
     */
    public BigInteger getTotalEnPuntos() {
        return totalEnPuntos;
    }

    /**
     *
     * @param totalEnPuntos
     */
    public void setTotalEnPuntos(BigInteger totalEnPuntos) {
        this.totalEnPuntos = totalEnPuntos;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoricoDatosClientes)) {
            return false;
        }
        HistoricoDatosClientes other = (HistoricoDatosClientes) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
    @Override
    public String toString() {
        return "com.melani.entity.HistoricoDatosClientes[id=" + id + "]";
    }
}