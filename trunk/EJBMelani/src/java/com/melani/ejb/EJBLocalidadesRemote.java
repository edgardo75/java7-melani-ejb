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
public interface EJBLocalidadesRemote {
   
    /**
     *
     * @param provincia
     * @return
     */
    String searchLocXProvincia(short provincia);

    /**
     *
     * @param descripcion
     * @param idprovincia
     * @param codigopostal
     * @return
     */
    long addLocalidadCompleto(String descripcion, short idprovincia, int codigopostal);

    /**
     *
     * @param idprovincia
     * @return
     */
    String searchAllLocalidadesbyidprov(Short idprovincia);
}
