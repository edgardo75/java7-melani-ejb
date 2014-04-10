/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 *
 * @author Edgardo
 */
@Entity
@Table(name = "PORCENTAJES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Porcentajes.findAll", query = "SELECT p FROM Porcentajes p"),
    @NamedQuery(name = "Porcentajes.findByIdPorcentajes", query = "SELECT p FROM Porcentajes p WHERE p.idPorcentajes = :idPorcentajes"),
    @NamedQuery(name = "Porcentajes.findByDescripcion", query = "SELECT p FROM Porcentajes p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Porcentajes.findByValor", query = "SELECT p FROM Porcentajes p WHERE p.valor = :valor")})
public class Porcentajes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
     @GeneratedValue(generator="PorcentajesIdGen",strategy=GenerationType.TABLE)
        @TableGenerator(name="PorcentajesIdGen", table="ID_GEN_PORCENTAJES",
            pkColumnName="ID_POR", valueColumnName="ID_VAL_POR",
            pkColumnValue="Porcentajes")
    @Basic(optional = false)
    @Column(name = "ID_PORCENTAJES")
    private Short idPorcentajes;
    @Column(name = "DESCRIPCION",length=100,unique=true)
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VALOR",precision=10,scale=2)
    private Double valor;
    @OneToMany(mappedBy = "fkidporcentajenotaId")
    private List<Notadepedido> notadepedidoList;

    /**
     *
     */
    public Porcentajes() {
    }

    /**
     *
     * @param idPorcentajes
     */
    public Porcentajes(Short idPorcentajes) {
        this.idPorcentajes = idPorcentajes;
    }

    /**
     *
     * @return
     */
    public Short getIdPorcentajes() {
        return idPorcentajes;
    }

    /**
     *
     * @param idPorcentajes
     */
    public void setIdPorcentajes(Short idPorcentajes) {
        this.idPorcentajes = idPorcentajes;
    }

    /**
     *
     * @return
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     *
     * @param descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     *
     * @return
     */
    public Double getValor() {
        return valor;
    }

    /**
     *
     * @param valor
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     *
     * @return
     */
    @XmlTransient
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
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPorcentajes != null ? idPorcentajes.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Porcentajes)) {
            return false;
        }
        Porcentajes other = (Porcentajes) object;
        return (this.idPorcentajes != null || other.idPorcentajes == null) && (this.idPorcentajes == null || this.idPorcentajes.equals(other.idPorcentajes));
    }
    @Override
    public String toString() {
        return "com.melani.entity.Porcentajes[ idPorcentajes=" + idPorcentajes + " ]";
    }
}
