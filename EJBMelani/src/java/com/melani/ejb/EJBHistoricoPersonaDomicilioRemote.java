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
public interface EJBHistoricoPersonaDomicilioRemote {

    /**
     *
     * @param idDomicilio
     * @param idPersona
     * @param idUsuario
     * @return
     */
    long addHistoricoPersonaDomicilio(Integer idDomicilio, Integer idPersona,Integer idUsuario);   
}
