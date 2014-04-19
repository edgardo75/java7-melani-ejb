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
public interface EJBNotaPedidoRemote {

    /**
     *
     * @param xmlNotaPedido
     * @return
     */
    long agregarNotaPedido(String xmlNotaPedido);

    /**
     *
     * @param idnta
     * @return
     */
    String selectUnaNota(long idnta);

    /**
     *
     * @param idnota
     * @param saldo
     * @param idusuario
     * @return
     */
   // long modificarSaldoNota(long idnota, double saldo, int idusuario);

    /**
     *
     * @param idnota
     * @param idusuariocancelo
     * @param estado
     * @return
     */
    long cancelarNotaPedido(long idnota,int idusuariocancelo,int estado);

    /**
     *
     * @param idnota
     * @param idusuario
     * @param estado
     * @return
     */
    long entregarNotaPedido(long idnota, int idusuario,int estado);

    /**
     *
     * @param fecha1
     * @param fecha2
     * @param idvendedor
     * @return
     */
    String selectNotaEntreFechas(String fecha1, String fecha2,int idvendedor);

    /**
     *
     * @return
     */
    int getRecorCountNotas();

    /**
     *
     * @return
     */
    String selectAllNotas();

    /**
     *
     * @param index
     * @param recordCount
     * @return
     */
    String verNotasPedidoPaginadas(int index, int recordCount);

    /**
     *
     * @param idnota
     * @param idusuario
     * @param estado
     * @return
     */
    long anularNotaPedido(long idnota, long idusuario, int estado);

    /**
     *
     * @param xmlnotapedidomodificada
     * @return
     */
    long actualizarNotaPedido(String xmlnotapedidomodificada);

    /**
     *
     * @param fecha1
     * @param fecha2
     * @param idvendedor
     * @return
     */
    String selecNotaEntreFechasEntrega(String fecha1, String fecha2, int idvendedor);

    /**
     *
     * @param idnota
     * @param idEmpleado
     * @return
     */
    int eliminarNotaDePedido(long idnota, long idEmpleado);

    /**
     *
     * @return
     */
    String calcularVentasMensualesHastaFechaYAnoActual();
}
