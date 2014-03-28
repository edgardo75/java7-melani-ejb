/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Clientes;
import javax.ejb.Remote;
/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBClientesRemote {

    /**
     *
     * @param xmlaParsear
     * @return
     */
    String parsearCaracteresEspecialesXML1(String xmlaParsear);

    /**
     *
     * @param idCliente
     * @return
     */
    String obtenerCliente(long idCliente);

    /**
     *
     * @param idTipo
     * @param nrodDocu
     * @return
     */
    String obtenerClienteXTipoAndNumeroDocu(short idTipo, int nrodDocu);

    /**
     *
     * @param clientes
     * @return
     */
    long addClientes(Clientes clientes);

    /**
     *
     * @param cliente
     * @return
     */
    long updateCliente(Clientes cliente);

    /**
     *
     * @param docNumber
     * @return
     */
    String getCustomerDocNumber(Integer docNumber);

    /**
     *
     * @param name
     * @param lastname
     * @return
     */
    String searchClientForNameAndLastName(String name,String lastname);

    /**
     *
     * @param datospersonalescliente
     * @return
     */
    String addClienteDatosPersonales(String datospersonalescliente);

   
}
