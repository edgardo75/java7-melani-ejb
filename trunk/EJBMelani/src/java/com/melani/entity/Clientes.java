/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
/**
 * A Entity Clientes
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@DiscriminatorValue("CLI")
public class Clientes extends Personas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(precision = 15,scale=3,name="TOTALCOMPRAS")
    private BigDecimal totalCompras;
    @Column(precision = 15,name = "TOTALPUNTOS")
    private BigInteger totalEnPuntos;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCarga;
    @OneToMany(mappedBy = "fkIdcliente",cascade = CascadeType.ALL)
    private List<Notadepedido> notadepedidoList;

    /**
     *
     */
    public Clientes(){}

    /**
     *
     * @return
     */
    public Date getFechaCarga() {
        return fechaCarga;
    }

    /**
     *
     * @param fechaCarga
     */
    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
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

    /**
     *
     * @return
     */
    public List<Notadepedido> getNotadepedidoList() {
        return Collections.unmodifiableList(notadepedidoList);
    }

    /**
     *
     * @param notadepedidoList
     */
    public void setNotadepedidoList(List<Notadepedido> notadepedidoList) {
        this.notadepedidoList = notadepedidoList;
    }

    /**
     *
     * @return
     */
    public String toXMLCLI(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder xml = new StringBuilder("<totalcompras>"+this.getTotalCompras()+"</totalcompras>\n").append("<totalpuntos>").append(this.getTotalEnPuntos()).append("</totalpuntos>\n");
                xml.append("<fechacarga>").append(sdf.format(this.getFechaCarga())).append("</fechacarga>\n");
                xml.append("<notapedidolist>\n");
                if(this.getNotadepedidoList().isEmpty()) {
                    xml.append("</notapedidolist>\n");
        } else{
                    List<Notadepedido>lista = this.getNotadepedidoList();
            for (Notadepedido notadepedido : lista) {
                xml.append(notadepedido.toXML());
            }
                    xml.append("</notapedidolist>\n");
                }
        return xml.toString();
    }
}





















