/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import javax.ejb.Remote;
/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBClienteTelefonoRemote {

     /**
     *
     * @param numero numero de telefono
     * @param prefijo prefijo o codigo de area del telefono
     * @param idcliente id de cliente
     * @return devuelve una cadena si se ejecuto correctamente el método o no
     */
    String addClienteTelefono(String numero, String prefijo, long idcliente);
}
