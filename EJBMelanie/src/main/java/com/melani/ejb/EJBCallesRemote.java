/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import javax.ejb.Remote;
/**
 * Interfac remota EJBCalles
 * @author Edgardo
 */
@Remote
public interface EJBCallesRemote {

    /**
     *
     * @param descripcion nombre de calle 
     * @param idUsuario id de usuario que realiza la operación
     * @return id de calle insertado, si es cero o menor que cero la operación no se realizó correctamente
     */
    long addCalles(String descripcion,int idUsuario);

    /**
     *
     * @return lista de calles
     */
    String searchAllCalles();

    /**
     *
     * @return cantidad de registro en la base de datos
     */
    int recorCountCalles();
}
