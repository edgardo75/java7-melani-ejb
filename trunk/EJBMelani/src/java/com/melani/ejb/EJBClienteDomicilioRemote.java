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
public interface EJBClienteDomicilioRemote {

    /**
     *
     * @param idCliente id de cliente
     * @param idDomicilio id de domicilio
     * @param idUsuario ide de usuario o vendedor
     * @return
     */
    public String addRelacionClienteDomicilio(long idCliente, long idDomicilio,int idUsuario);
    

    
}
