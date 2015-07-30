/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.utils.DatosTelefonos;
import javax.ejb.Remote;
/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBTelefonosRemote {

    /**
     *
     * @param datosTel
     * @return
     */
    long addTelefonos(DatosTelefonos datosTel);
}
