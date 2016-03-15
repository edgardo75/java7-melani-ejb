package com.melani.ejb;
import com.melani.entity.Productos;
import javax.ejb.Remote;

@Remote
public interface EJBProductosRemote {   
    //long addExistenciasProducto(long idproducto, int cantidad,float precio,int idusuario);
    String addProducto(String xmlProducto);
    String selectOneProducto(long idproducto);
    Productos agregarProductos(Productos producto);
    String searchAllProductos();
    int controlStockProducto(long idProducto, int cantidad, long idUsuario);
    String actualizarProducto(String xmlProducto);
    int grabarImagen(long id_producto, byte[] longitudImagen,String nameImage,String magnitud);
    byte[] obtenerImagenProducto(long idProducto);
}