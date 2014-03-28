/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author win7
 */
@Entity
@Table(name = "IMAGENESPRODUCTOS")
public class ImagenesProductos implements Serializable{
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="ImagenProductoIdGen",strategy=GenerationType.IDENTITY)
    @Column(name = "ID_IMAGE")   
    private Long id_image;
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
     * @param id_image
     * @param productos
     * @param pathImagenEnDisco
     * @param extension
     * @param magnitud
     */
    public ImagenesProductos(Long id_image, Productos productos, String pathImagenEnDisco, String extension, String magnitud) {
        this.id_image = id_image;
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
        return id_image;
    }

    /**
     *
     * @param id_image
     */
    public void setId_image(Long id_image) {
        this.id_image = id_image;
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
        hash = 71 * hash + (this.id_image != null ? this.id_image.hashCode() : 0);
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
        if (this.id_image != other.id_image && (this.id_image == null || !this.id_image.equals(other.id_image))) {
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
        if ((this.magnitud == null) ? (other.magnitud != null) : !this.magnitud.equals(other.magnitud)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "ImagenesProductos{" + "id_image=" + id_image + ", productos=" + productos + ", pathImagenEnDisco=" + pathImagenEnDisco + ", nombreImagen=" + nombreImagen + ", extension=" + extension + ", magnitud=" + magnitud + '}';
    }
    
    
    
    
      
}
