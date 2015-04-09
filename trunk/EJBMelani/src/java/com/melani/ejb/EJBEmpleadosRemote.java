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
public interface EJBEmpleadosRemote {
   
    /**
     *
     * @param xmlEmpleado
     * @return
     */
    public long addEmpleadoFullTime(String xmlEmpleado);

    /**
     *
     * @return
     */
    String selectAllEmpleados();

    /**
     *
     * @param idEmpleado
     * @param idEmpleadoDesabilito
     * @return
     */
    int deshabilitarEmpleado(int idEmpleado, int idEmpleadoDesabilito);

    /**
     *
     * @param idEmpleado
     * @param idEmpleadohabilito
     * @return
     */
    int habilitarEmpleado(int idEmpleado, int idEmpleadohabilito);

    boolean checkPassEmployee(long idEmpleado,String pass);
   
     
     
}
