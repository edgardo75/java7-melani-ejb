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

    /**
     *
     * @param descripcion nombre del barrio
     * @param idUsuario id de usuario que realiza la operacion para que quede registrada
     * @return id de barrio insertado con exito en la base de datos
     */
    long addBarrio(String descripcion,int idUsuario);

    /**
     *
     * @return lista de barrios
     */
    String searchAllBarrios();

    /**
     *
     * @return cantidad de registros en la base de datos de la tabla barrios
     */
    int recordCountBarrios();

    /**
     *
     * @param indiceInicio indice de pagina para obtener los datos
     * @param numeroItems cantidad de elementos o registros por pagina
     * @return lista de barrios
     */
   // String obtenrItemsPaginados(int indiceInicio, int numeroItems);

    /**
     *
     * @param startindex indice de pagina para obtener los datos
     * @param numitems cantidad de elementos o registros por pagina
     * @return lista de barrios
     */
   // Barrios[] barrios_Paging(int startindex, int numitems);
}
