/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import javax.ejb.Remote;
/**
 * interfaz remota EJBBarrios
 * @author Edgardo
 */
@Remote
public interface EJBBarriosRemote {
    long addBarrio(String descripcion,int idUsuario);
    String searchAllBarrios();
    int recordCountBarrios();
}
