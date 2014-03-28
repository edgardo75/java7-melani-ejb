/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Productos;
import javax.ejb.Remote;
/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBProductosRemote {
    
    /**
     *
     * @param idproducto
     * @param cantidad
     * @param precio
     * @param idusuario
     * @return
     */
    long addExistenciasProducto(int idproducto, int cantidad,float precio,int idusuario);

    /**
     *
     * @param idProducto
     * @return
     */
    String leerImagenBaseDatos(int idProducto);

    /**
     *
     * @param xmlProducto
     * @return
     */
    String addProducto(String xmlProducto);

    /**
     *
     * @param idproducto
     * @return
     */
    String selectoneproducto(long idproducto);

    /**
     *
     * @param producto
     * @return
     */
    Productos agregarProductos(Productos producto);

    /**
     *
     * @return
     */
    String searchAllProductos();

    /**
     *
     * @param idProducto
     * @param cantidad
     * @param idUsuario
     * @return
     */
    int controlStockProducto(long idProducto, int cantidad, int idUsuario);
    
    /**
     *
     * @param xmlProducto
     * @return
     */
    String actualizarProducto(String xmlProducto);

    /**
     *
     * @param id_producto
     * @param longitudImagen
     * @param nameImage
     * @param magnitud
     * @return
     */
    int grabarImagen(int id_producto, byte[] longitudImagen,String nameImage,String magnitud);

    /**
     *
     * @param idProducto
     * @return
     */
    byte[] obtenerImagenProducto(int idProducto);
}
