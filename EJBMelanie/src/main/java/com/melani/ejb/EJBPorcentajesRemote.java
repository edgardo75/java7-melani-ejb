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
public interface EJBPorcentajesRemote {

    /**
     *
     * @param descripcion
     * @param valor
     * @return
     */
    short agregarDatosPorcenta(String descripcion, double valor);
}
