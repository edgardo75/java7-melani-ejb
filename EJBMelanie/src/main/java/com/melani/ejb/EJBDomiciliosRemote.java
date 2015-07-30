/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.utils.DatosDomicilios;
import javax.ejb.Remote;
/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBDomiciliosRemote {

    /**
     *
     * @param xmlDomicilios
     * @return
     */
    long addDomicilios(DatosDomicilios xmlDomicilios);

    /**
     *
     * @param xmlDomicilio
     * @return
     */
    long addDomicilio(String xmlDomicilio);
}
