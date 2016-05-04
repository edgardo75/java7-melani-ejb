package com.melani.ejb;
import javax.ejb.Remote;

@Remote
public interface EJBNotaPedidoRemote {

    long agregarNotaPedido(String xmlNotaPedido);
    String selectUnaNota(long idnta);
    long cancelarNotaPedido(String datosXML,int estado);
    long entregarNotaPedido(long idnota, long idusuario,int estado);
    String selectNotaEntreFechasCompra(String desde, String hasta,long idvendedor);
    int getRecorCountNotas();
    String selectAllNotas();
    String verNotasPedidoPaginadas(int index, int recordCount);
    long anularNotaPedido(long idnota, long idusuario, int estado);
    long actualizarNotaPedido(String xmlnotapedidomodificada);
    String selecNotaEntreFechasEntrega(String fecha1, String fecha2);
    int eliminarNotaDePedido(long idnota, long idEmpleado);
    String calcularVentasMensualesHastaFechaYAnoActual();
}
