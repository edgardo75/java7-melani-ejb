package com.melani.ejb;
import javax.ejb.Remote;

@Remote
public interface EJBNotaPedidoRemote {

    long agregarNotaPedido(String xmlNotaPedido);
    String selectUnaNota(long idnta);
    long cancelarNotaPedido(String datosXML,int estado);
    long entregarNotaPedido(String datosXML,int estado);
    String selectNotaEntreFechasCompra(String desde, String hasta);
    int getRecorCountNotas();
    String selectAllNotas();
    String verNotasPedidoPaginadas(int index, int recordCount);
    long anularNotaPedido(String datosXML, int estado);
    long actualizarNotaPedido(String xmlnotapedidomodificada);
    String selectNotaEntreFechasEntrega(String fecha1, String fecha2);
    int eliminarNotaDePedido(long idnota, long idEmpleado);
    String calcularVentasMensualesHastaFechaYAnoActual();
}
