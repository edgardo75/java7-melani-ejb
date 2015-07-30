/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author win7
 */
@Entity
@Table(name = "IMAGENESPRODUCTOS")
@NamedQueries({@NamedQuery(name = "ImagenesProductos.findById",query = "SELECT i FROM ImagenesProductos i WHERE i.productos.sid = :sid"),
@NamedQuery(name = "ImagenesProductos.findByIdProduct", query = "Select i From ImagenesProductos i Where i.productos.sid = :idProducto")})
public class ImagenesProductos implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id    
    @Column(name = "ID")
    @GeneratedValue(generator="ImagenProductoIdGen",strategy=GenerationType.IDENTITY)          
    private Long id;
    @ManyToOne(optional = false)
    private Productos productos;
    @Column(name = "PATHIMAGEN")
    private String pathImagenEnDisco;
    @Column(name="NOMBRE_IMAGEN")
    private String nombreImagen;
    @Column(name = "EXTENSION")
    private String extension;
    @Column(name = "MAGNITUD")
    private String magnitud;

    /**
     *
     */
    public ImagenesProductos() {
    }  

    /**
     *
     * @param id
     * @param productos
     * @param pathImagenEnDisco
     * @param extension
     * @param magnitud
     */
    public ImagenesProductos(Long id, Productos productos, String pathImagenEnDisco, String extension, String magnitud) {
        this.id = id;
        this.productos = productos;
        this.pathImagenEnDisco = pathImagenEnDisco;
        this.extension = extension;
        this.magnitud = magnitud;
    }
    
    /**
     *
     * @return
     */
    public Long getId_image() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId_image(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Productos getProductos() {
        return productos;
    }

    /**
     *
     * @param productos
     */
    public void setProductos(Productos productos) {
        this.productos = productos;
    }

    /**
     *
     * @return
     */
    public String getPathImagenEnDisco() {
        return pathImagenEnDisco;
    }

    /**
     *
     * @param pathImagenEnDisco
     */
    public void setPathImagenEnDisco(String pathImagenEnDisco) {
        this.pathImagenEnDisco = pathImagenEnDisco;
    }

    /**
     *
     * @return
     */
    public String getNombreImagen() {
        return nombreImagen;
    }

    /**
     *
     * @param nombreImagen
     */
    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    /**
     *
     * @return
     */
    public String getExtension() {
        return extension;
    }

    /**
     *
     * @param extension
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     *
     * @return
     */
    public String getMagnitud() {
        return magnitud;
    }

    /**
     *
     * @param magnitud
     */
    public void setMagnitud(String magnitud) {
        this.magnitud = magnitud;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 71 * hash + (this.productos != null ? this.productos.hashCode() : 0);
        hash = 71 * hash + (this.pathImagenEnDisco != null ? this.pathImagenEnDisco.hashCode() : 0);
        hash = 71 * hash + (this.nombreImagen != null ? this.nombreImagen.hashCode() : 0);
        hash = 71 * hash + (this.extension != null ? this.extension.hashCode() : 0);
        hash = 71 * hash + (this.magnitud != null ? this.magnitud.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImagenesProductos other = (ImagenesProductos) obj;
        if (!Objects.equals(this.id, other.id) && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.productos != other.productos && (this.productos == null || !this.productos.equals(other.productos))) {
            return false;
        }
        if ((this.pathImagenEnDisco == null) ? (other.pathImagenEnDisco != null) : !this.pathImagenEnDisco.equals(other.pathImagenEnDisco)) {
            return false;
        }
        if ((this.nombreImagen == null) ? (other.nombreImagen != null) : !this.nombreImagen.equals(other.nombreImagen)) {
            return false;
        }
        if ((this.extension == null) ? (other.extension != null) : !this.extension.equals(other.extension)) {
            return false;
        }
        return !((this.magnitud == null) ? (other.magnitud != null) : !this.magnitud.equals(other.magnitud));
    }

    
    @Override
    public String toString() {
        return "ImagenesProductos{" + "id=" + id + ", productos=" + productos + ", pathImagenEnDisco=" + pathImagenEnDisco + ", nombreImagen=" + nombreImagen + ", extension=" + extension + ", magnitud=" + magnitud + '}';
    }
    
    
    
    
      
}
