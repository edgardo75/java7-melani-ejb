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
public interface EJBPresupuestosRemote {

    /**
     *
     * @param xmlPresupuesto
     * @return
     */
    long addBudgets(String xmlPresupuesto);

    /**
     *
     * @return
     */
    String selectAllPresupuestosJPA();

    /**
     *
     * @return
     */
    Integer getRecordCount();

    /**
     *
     * @return
     */
   // String selectPresupuestoOfTheDay();

    /**
     *
     * @param index
     * @param record
     * @return
     */
    String verPresupuestosPaginados(Integer index, Integer record);

    /**
     *
     * @param idpresupuesto
     * @return
     */
    String searchOneBudget(int idpresupuesto);

    /**
     *
     * @param idPresupuesto
     * @return
     */
    String ShowReportPresupuesto(Integer idPresupuesto);    
}
