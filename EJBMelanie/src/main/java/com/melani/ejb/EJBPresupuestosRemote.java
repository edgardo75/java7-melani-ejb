package com.melani.ejb;
import javax.ejb.Remote;

@Remote
public interface EJBPresupuestosRemote {
    long addBudgets(String xmlPresupuesto);
    String selectAllPresupuestosJPA();
    Integer getRecordCount();
    String verPresupuestosPaginados(Integer index, Integer record);
    String searchOneBudget(int idpresupuesto);
    String ShowReportPresupuesto(Integer idPresupuesto);    
}