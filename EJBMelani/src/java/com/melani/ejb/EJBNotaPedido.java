/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Clientes;
import com.melani.entity.Detallesnotadepedido;
import com.melani.entity.DetallesnotadepedidoPK;
import com.melani.entity.Empleados;
import com.melani.entity.Historiconotapedido;
import com.melani.entity.Notadepedido;
import com.melani.entity.Personas;
import com.melani.entity.Porcentajes;
import com.melani.entity.Productos;
import com.melani.entity.TarjetasCreditoDebito;
import com.melani.utils.DatosNotaPedido;
import com.melani.utils.DetallesNotaPedido;
import com.melani.utils.Itemdetallesnota;
import com.thoughtworks.xstream.XStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBNotaPedido")
@WebService(serviceName="ServiceNotaPedido",name="NotaPedidoWs")
public class EJBNotaPedido implements EJBNotaPedidoRemote {
    private static final Logger logger = Logger.getLogger(EJBNotaPedido.class);
    @PersistenceContext
    private EntityManager em;
    @EJB
      EJBProductosRemote producto;
    @EJB
    EJBPresupuestosRemote ejbpresupuesto;
    DatosNotaPedido notadepedido;
    
    private DatosNotaPedido xestreaNotapedido(String xmlNotapedido){
            XStream xestream = new XStream();
            xestream.alias("notapedido", DatosNotaPedido.class);
            xestream.alias("personas", DatosNotaPedido.Personas.class);
            xestream.alias("tarjetacredito", DatosNotaPedido.TarjetaCredito.class);
            xestream.alias("porcentajes", DatosNotaPedido.Porcentajes.class);
            xestream.alias("itemdetallesnota", Itemdetallesnota.class);
            xestream.alias("detallesnotapedido", DetallesNotaPedido.class);
            xestream.addImplicitCollection(DetallesNotaPedido.class, "list");
            return notadepedido = (DatosNotaPedido) xestream.fromXML(parsearCaracteresEspecialesXML(xmlNotapedido).toString());
    }

    /**
     *
     * @param xmlNotaPedido
     * @return
     */
    @Override
    public long agregarNotaPedido(String xmlNotaPedido) {
        long retorno =0L;
        try {
             //---------------------------------------------------------------------------------
            //---------------------------------------------------------------------------------
             retorno = almacenarNotaPedido(xestreaNotapedido(xmlNotaPedido));
        } catch (Exception e) {
            logger.error("Error en metodo agregarNotaPedido, verifique", e);
            retorno = -1;
        }finally{
            return retorno;
        }
    }
    private long almacenarNotaPedido(DatosNotaPedido notadepedido) {
        long retorno =0L;
        try {
            //---------------------------------------------------------------------------------
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            //---------------------------------------------------------------------------------
                            Clientes cliente = em.find(Clientes.class, notadepedido.getPersonas().getId());
                            Notadepedido notape = new Notadepedido();
                                            notape.setAnticipo(BigDecimal.valueOf(notadepedido.getAnticipo()));
                                            notape.setAnulado(notadepedido.getAnulado());
                                            notape.setEnefectivo(notadepedido.getEnefectivo());
                                            notape.setPendiente(Character.valueOf(notadepedido.getPendiente()));
                                            notape.setEntregado(Character.valueOf(notadepedido.getEntregado()));
                                            notape.setFkIdcliente(em.find(Personas.class, notadepedido.getPersonas().getId()));
                                            notape.setFkidporcentajenotaId(em.find(Porcentajes.class, notadepedido.getPorcentajes().getId_porcentaje()));                
                                            notape.setIdTarjetaFk(em.find(TarjetasCreditoDebito.class, notadepedido.getTarjetacredito().getId_tarjeta()));
                                            notape.setIdUsuarioExpidioNota(notadepedido.getUsuario_expidio_nota());
                                            notape.setIdusuarioAnulado(notadepedido.getId_usuario_anulado());
                                            notape.setIdusuarioEntregado(notadepedido.getUsuario_entregado());
                                            notape.setMontoiva(BigDecimal.valueOf(notadepedido.getMontoiva()));
                                            notape.setNumerodecupon(notadepedido.getNumerodecupon());
                                            notape.setObservaciones(notadepedido.getObservaciones());
                                            notape.setRecargo(BigDecimal.valueOf(notadepedido.getRecargo()));
                                            notape.setSaldo(BigDecimal.valueOf(notadepedido.getSaldo()));
                                            notape.setStockfuturo(notadepedido.getStockfuturo());
                                            notape.setTotal(BigDecimal.valueOf(notadepedido.getMontototal()));
                                            notape.setHoracompra(gc.getTime());
                                            notape.setFechadecompra(gc.getTime());                                            
                                            notape.setFechaentrega(sdf.parse(notadepedido.getFechaentrega()));                                 
                                            notape.setCancelado(Character.valueOf(notadepedido.getCancelado()));
                                            notape.setDescuentonota(BigDecimal.valueOf(notadepedido.getDescuentonota()));
                                            notape.setDescuentoPesos(BigDecimal.valueOf(notadepedido.getDescuentopesos()));
                                            notape.setIdusuariocancelo(notadepedido.getUsuario_cancelo_nota());
                                            try {
                                                notape.setMontototalapagar(BigDecimal.valueOf(notadepedido.getMontototalapagar()));
                                            } catch (Exception e) {
                                                logger.error("Error en metodo almacenar nota, monto total a pagar "+e.getLocalizedMessage());
                                            }
                                            notape.setPorcdesctotal(BigDecimal.valueOf(notadepedido.getPorc_descuento_total()));
                                            notape.setPorcrecargo(BigDecimal.valueOf(notadepedido.getPorcentajerecargo()));
                                            if(notadepedido.getCancelado()=='1') {
                                                notape.setFecancelado(gc.getTime());
                                            }
                                            if(notadepedido.getAnulado()=='1') {
                                                notape.setFechaAnulado(gc.getTime());
                                            }
                                            em.persist(notape);
                            /*
                             * trato la lista de productos de la nota de pedido, a continuaciÃ³n*/
                                  long historico =0;
                                         switch(notadepedido.getStockfuturo()){
                                             case 0:{
                                                 retorno = almacenarDetalleNotaConControlStock(notadepedido,notape);
                                                             /*Almacenar el historico en el mÃ©todo para que quede bien registrada la operacion*/
                                                           historico =  almacenarHistorico(notadepedido,notape);
                                                    }
                                             break;
                                             default :
                                                    {
                                                        retorno = almacenardetallenota(notadepedido,notape);
                                                      /*Almacenar el historico en el método para que quede bien registrada la operacion*/
                                                       historico =  almacenarHistorico(notadepedido,notape);
                                                    }
                                         }
                                         Query consulta =em.createQuery("SELECT n FROM Notadepedido n WHERE n.fkIdcliente.idPersona = :id");
                                         consulta.setParameter("id", cliente.getIdPersona());
                                         List<Notadepedido>lista=consulta.getResultList();
                                         cliente.setNotadepedidoList(lista);
                                         Double totalCompras = cliente.getTotalCompras().doubleValue()+notape.getMontototalapagar().doubleValue();
                                         int totalPuntos = cliente.getTotalEnPuntos().intValue()+totalCompras.intValue();
                                           cliente.setTotalCompras(BigDecimal.valueOf(totalCompras));
                                           cliente.setTotalEnPuntos(BigInteger.valueOf(totalPuntos));
                                           cliente.setFechaCarga(gc.getTime());                                           
                                         em.persist(cliente);
                                         em.flush();
                                 if(historico<0) {
                                     retorno = historico;
                                 } else{
                                      retorno = notape.getId();
                                             
            }
        } catch (ParseException e) {
            logger.error("Error en metodo almacenarNotaPedido, EJBNotaPedido "+e.getLocalizedMessage());
            retorno =-2;
        }finally{
            
            return retorno;
        }
    }
    private long almacenardetallenota(DatosNotaPedido notadepedido, Notadepedido notape) {
        long retorno =0;
        try {
            /* a continuacion "desmenuzo" todo el detalle de la nota de pedido para
             * asi de esa manera poder amancenarla en los lugares correspondiente como lo es en detalles
             * de pedido de la nota y sus ligaduras con la nota de pedido propiamente dicha y los productos
             * para mantener la persistencia de las lista de manera real*/
            List<Itemdetallesnota>lista = notadepedido.getDetallesnotapedido().getDetallesnota();
                    for (Itemdetallesnota itemdetallesnota : lista) {
                            Productos productos = em.find(Productos.class,itemdetallesnota.getId_producto());
                            DetallesnotadepedidoPK detallespk = new DetallesnotadepedidoPK(notape.getId(), itemdetallesnota.getId_producto());
                            Detallesnotadepedido detalles = new Detallesnotadepedido();
                            detalles.setCancelado(Character.valueOf(itemdetallesnota.getCancelado()));
                            detalles.setCantidad(itemdetallesnota.getCantidad());
                            detalles.setDescuento(BigDecimal.valueOf(itemdetallesnota.getDescuento()));
                            detalles.setEntregado(Character.valueOf(itemdetallesnota.getEntregado()));
                            detalles.setIva(BigDecimal.valueOf(itemdetallesnota.getIva()));
                            detalles.setNotadepedido(notape);
                            detalles.setPendiente(Character.valueOf(itemdetallesnota.getPendiente()));
                            detalles.setPrecio(BigDecimal.valueOf(itemdetallesnota.getPrecio()));
                            detalles.setProductos(productos);
                            detalles.setSubtotal(BigDecimal.valueOf(itemdetallesnota.getSubtotal()));
                            detalles.setDetallesnotadepedidoPK(detallespk);
                            detalles.setAnulado(Character.valueOf(itemdetallesnota.getAnulado()));
                            detalles.setPreciocondescuento(BigDecimal.valueOf(itemdetallesnota.getPreciocondescuento()));
                            em.persist(detalles);
                            Query consulta = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdproducto = :fkIdproducto");
                            consulta.setParameter("fkIdproducto", itemdetallesnota.getId_producto());
                            productos.setDetallesnotadepedidoList(consulta.getResultList());
                    }
            Query consulta1 = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdnota = :fkIdnota");
            consulta1.setParameter("fkIdnota", notape.getId());
            notape.setDetallesnotadepedidoList(consulta1.getResultList());
            em.merge(notape);
            retorno = notape.getId();
        } catch (Exception e) {
            logger.error("Error en metdodo almacenardetallenota "+e.getLocalizedMessage());
            retorno=-3;
        }finally{
            
            return retorno;
        }
    }
    private long almacenarDetalleNotaConControlStock(DatosNotaPedido notadepedido, Notadepedido notape) {
        long retorno =0;
        int stockDisponible=0;
        try{
/* Idem metodo almacenar nota con la inclusion del metodo para controlar el stock llamando
 a metodo remoto de EJBPRoductosRemote, perdon por no usar reusabilidad de codigo fuente, no se me ocurria otra alternativa
 */
            List<Itemdetallesnota>lista = notadepedido.getDetallesnotapedido().getDetallesnota();
                    for (Itemdetallesnota itemdetallesnota : lista) {
                                Productos productos = em.find(Productos.class,itemdetallesnota.getId_producto());
                                DetallesnotadepedidoPK detallespk = new DetallesnotadepedidoPK(notape.getId(), itemdetallesnota.getId_producto());
                                Detallesnotadepedido detalles = new Detallesnotadepedido();
                                detalles.setCancelado(Character.valueOf(itemdetallesnota.getCancelado()));
                                detalles.setCantidad(itemdetallesnota.getCantidad());
                                detalles.setDescuento(BigDecimal.valueOf(itemdetallesnota.getDescuento()));
                                detalles.setPreciocondescuento(BigDecimal.valueOf(itemdetallesnota.getPreciocondescuento()));
                                detalles.setEntregado(Character.valueOf(itemdetallesnota.getEntregado()));
                                detalles.setIva(BigDecimal.valueOf(itemdetallesnota.getIva()));
                                detalles.setNotadepedido(notape);
                                detalles.setPendiente(Character.valueOf(itemdetallesnota.getPendiente()));
                                detalles.setPrecio(BigDecimal.valueOf(itemdetallesnota.getPrecio()));
                                detalles.setProductos(productos);
                                detalles.setSubtotal(BigDecimal.valueOf(itemdetallesnota.getSubtotal()));
                                detalles.setDetallesnotadepedidoPK(detallespk);
                                em.persist(detalles);
                                Query consulta = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdproducto = :fkIdproducto");
                                consulta.setParameter("fkIdproducto", itemdetallesnota.getId_producto());
                                productos.setDetallesnotadepedidoList(consulta.getResultList());
                                stockDisponible=producto.controlStockProducto(itemdetallesnota.getId_producto(), itemdetallesnota.getCantidad(), notadepedido.getUsuario_expidio_nota());
                            if(stockDisponible<=50 &&stockDisponible>=0 ) {
                                logger.info("El stock Disponible para el producto "+productos.getDescripcion()+" estÃ¡ bajando a nivel mÃ­nimo debe actualizar o agregar mas productos");
                                } else{
                                if (stockDisponible<0) {
                                    logger.info("Ocurrió un Error o hay faltante de stock, valor devuelto por la funciÃ³n "+stockDisponible+" el producto es "+productos.getDescripcion()+" su stock disponible "+productos.getCantidadDisponible().intValue());
                                    }
                            }
                    }
                                Query consulta1 = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdnota = :fkIdnota");
                                consulta1.setParameter("fkIdnota", notape.getId());
                                notape.setDetallesnotadepedidoList(consulta1.getResultList());
                                em.merge(notape);
                                retorno = notape.getId();
        }catch(Exception e){
            logger.error("Error en metodo almacenarDetalleNotaConControlStock "+e.getLocalizedMessage());
            retorno =-1;
        }finally{
            
            return retorno;
        }
    }
    private long almacenarHistorico(DatosNotaPedido notadepedido,Notadepedido notape) {
        long resultado =0L;
                    GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
                    Historiconotapedido historico = new Historiconotapedido();
                    historico.setAnticipo(BigDecimal.valueOf(notadepedido.getAnticipo()));
                    historico.setEntregado(Character.valueOf(notadepedido.getEntregado()));
                    historico.setFecharegistro(gc.getTime());
                    historico.setFkidnotapedido(em.find(Notadepedido.class, notape.getId()));
                    historico.setObservaciones(notadepedido.getObservaciones());
                    historico.setPendiente(Character.valueOf(notadepedido.getPendiente()));
                    historico.setPorcentajeaplicado(notadepedido.getPorcentajes().getId_porcentaje());
                    historico.setSaldo(BigDecimal.valueOf(notadepedido.getSaldo()));
                    historico.setTotal(BigDecimal.valueOf(notadepedido.getMontototal()));
                    historico.setIdusuarioanulo(notadepedido.getId_usuario_anulado());
                    historico.setIdusuariocancelo(notadepedido.getUsuario_cancelo_nota());
                    historico.setIdusuarioentrega(notadepedido.getUsuario_entregado());
                    historico.setPorcentajedesc(BigDecimal.valueOf(notadepedido.getPorc_descuento_total()));
                    historico.setIdusuarioexpidio(notadepedido.getUsuario_expidio_nota());
                    historico.setPorcrecargo(BigDecimal.valueOf(notadepedido.getPorcentajerecargo()));
                    historico.setTotalapagar(BigDecimal.valueOf(notadepedido.getMontototalapagar()));
                    historico.setDescuento(BigDecimal.valueOf(notadepedido.getDescuentonota()));
                    historico.setRecargo(BigDecimal.valueOf(notadepedido.getRecargo()));
                    historico.setHoraregistro(gc.getTime());
                    historico.setCancelado(notadepedido.getCancelado());
                    historico.setAnulado(notadepedido.getAnulado());
                    historico.setAccion("Historico Almacenado con exito nota de pedido N "+notape.getId());
                    em.persist(historico);
                    try {
                        Query consulta = em.createQuery("SELECT h FROM Historiconotapedido h WHERE h.fkidnotapedido.id = :idnota");
                        consulta.setParameter("idnota", notape.getId());
                        notape.setHistoriconotapedidoList(consulta.getResultList());
                    } catch (Exception e) {
                        logger.error("Error en la consulta historiconotadepedido, metodo almacenar historico "+ e.getLocalizedMessage());
                        resultado = -2;
                    }
                    em.persist(notape);
                    em.flush();
                    resultado = historico.getIdhistorico();
        return resultado;
    }

    /**
     *
     * @param idnta
     * @return
     */
    @Override
    public String selectUnaNota(long idnta) {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<Lista>\n";
        try {
            //Si encuentro la nota de pedido devuelve el resultado si no mensaje de nota no encontrada
            Notadepedido nota = em.find(Notadepedido.class, idnta);
            if(nota != null) {               
                xml+=devolverNotaProcesadaSB(nota);
            } else {
                xml+="<nota>nota no encontrada</nota>";
            }
            //xml+=nota.toXML();
        } catch (Exception e) {
            logger.error("Error en metodo selectUnaNota "+e.getLocalizedMessage());
            xml += "Error no paso nada";
        }finally{
            
            return xml+="</Lista>";
        }
    }

    /**
     *
     * @param idnota
     * @param saldo
     * @param idusuario
     * @return
     */
    @Override
    public long modificarSaldoNota(long idnota, double saldo, int idusuario) {
        long result =0L;
        try {
            //--------------------------------------------------------------------------
            GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
            //--------------------------------------------------------------------------
                        Notadepedido nota = em.find(Notadepedido.class, idnota);
                    //------------------------------------------------------------------
                        nota.setSaldo(nota.getSaldo().subtract(BigDecimal.valueOf(saldo)));
                    //------------------------------------------------------------------
                        Historiconotapedido historico = new Historiconotapedido();
                    //------------------------------------------------------------------
                        historico.setAnticipo(BigDecimal.valueOf(saldo));
                        historico.setEntregado(Character.valueOf(nota.getEntregado()));
                        historico.setFecharegistro(gc.getTime());
                        historico.setFkidnotapedido(nota);
                        historico.setHoraregistro(gc.getTime());
                        historico.setIdusuarioanulo(nota.getIdusuarioAnulado());
                        historico.setIdusuarioentrega(nota.getIdusuarioEntregado());
                        historico.setIdusuarioexpidio(idusuario);
                        historico.setIdusuariocancelo(0);
                        historico.setPendiente(Character.valueOf(nota.getPendiente()));
                        historico.setPorcentajeaplicado(nota.getFkidporcentajenotaId().getIdPorcentajes());
                        historico.setSaldo(nota.getSaldo());
                        historico.setTotal(nota.getTotal());
               //----------------------------------------------------------------------------------
                        long notaID = procesaListNotaHistorico(nota,historico);
               //----------------------------------------------------------------------------------
                    em.flush();
                result = notaID;
        } catch (Exception e) {
            logger.error("Error en metodo modificarSaldoNota "+e.getLocalizedMessage());
            result = -1;
        }finally{
            
            return result;
        }
    }

    /**
     *
     * @param idnota
     * @param idusuariocancelo
     * @param estado
     * @return
     */
    @Override
    public long cancelarNotaPedido(long idnota,int idusuariocancelo,int estado) {
        long result = 0L;
        char cancelado ='0';
        try {
            //--------------------------------------------------------------------------
                       GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
            //--------------------------------------------------------------------------
            //--------------------------------------------------------------------------
            Notadepedido nota = em.find(Notadepedido.class,idnota);
            Empleados empleado = em.find(Empleados.class, idusuariocancelo);
            if(estado==1){
                cancelado ='1';
                nota.setCancelado(Character.valueOf(cancelado));
                nota.setIdusuariocancelo(idusuariocancelo);
                nota.setFecancelado(gc.getTime());
            }else{
                nota.setCancelado(Character.valueOf(cancelado));
                nota.setIdusuariocancelo(idusuariocancelo);
                nota.setFecancelado(null);
            }
            //--------------------------------------------------------------------------
            List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();
            for (Detallesnotadepedido detallesnotadepedido : lista) {
                detallesnotadepedido.setCancelado(Character.valueOf(cancelado));
            }
            //--------------------------------------------------------------------------
                            Historiconotapedido historico = new Historiconotapedido();
                            if(estado==1){
                                historico.setAccion("Cancelada por"+empleado.getNameuser());
                                historico.setCancelado(Character.valueOf(cancelado));
                            }else{
                                historico.setAccion("No cancelada por"+empleado.getNameuser());
                                historico.setCancelado(Character.valueOf(cancelado));
                            }
                                historico.setAnticipo(BigDecimal.ZERO);                                
                                historico.setFecharegistro(gc.getTime());
                                historico.setFkidnotapedido(nota);
                                historico.setHoraregistro(gc.getTime());
                                historico.setPendiente(Character.valueOf('0'));
                                historico.setEntregado(Character.valueOf('0'));
                                historico.setIdusuarioanulo(0);
                                historico.setIdusuarioentrega(0);
                                historico.setIdusuarioexpidio(0);
                                historico.setIdusuariocancelo(idusuariocancelo);
                                historico.setPorcentajeaplicado(Short.valueOf("0"));
                                historico.setSaldo(BigDecimal.ZERO);
                                historico.setTotal(BigDecimal.ZERO);
                                historico.setTotalapagar(BigDecimal.ZERO);
                                historico.setRecargo(BigDecimal.ZERO);
                                historico.setPorcrecargo(BigDecimal.ZERO);
                                historico.setPorcentajedesc(BigDecimal.ZERO);
                                historico.setDescuento(BigDecimal.ZERO);
                                historico.setAnulado('0');
                                 em.persist(historico);
          //----------------------------------------------------------------------------------
                            long notaID = procesaListNotaHistorico(nota,historico);
          //----------------------------------------------------------------------------------
            em.flush();
                result = notaID;
        } catch (NumberFormatException e) {
            logger.error("Error en metodo cancelaNotaPedido "+ e.getLocalizedMessage());
            result = -1;
        }finally{
            
            return result;
        }
    }
    private long procesaListNotaHistorico(Notadepedido nota, Historiconotapedido historico) {
        long result = 0L;
        try {
            //-----------------------------------------------------------------
                     try {
                            Query consulta = em.createQuery("SELECT h FROM Historiconotapedido h WHERE h.fkidnotapedido.id = :idnota");
                             consulta.setParameter("idnota", nota.getId());
                           nota.setHistoriconotapedidoList(consulta.getResultList());
                      } catch (Exception e) {
                          logger.error("Error en la consulta historiconotadepedido, metodo almacenar historico "+ e.getLocalizedMessage());
                          result = -2;
                      }
                     //-----------------------------------------------------------------
                        em.persist(nota);
                 result = nota.getId();
        } catch (Exception e) {
            logger.error("Error en metodo procesaListNotaHistorico "+ e.getLocalizedMessage());
            result = -1;
        }finally{
            
            return result;
        }
    }

    /**
     *
     * @param idnota
     * @param idusuarioentrega
     * @param estado
     * @return
     */
    @Override
    public long entregarNotaPedido(long idnota, int idusuarioentrega, int estado) {
        long result = 0L;
        char pendiente = '0';
        char entregado ='0';
        try {
            //--------------------------------------------------------------------
                GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
            //--------------------------------------------------------------------
                Notadepedido nota = em.find(Notadepedido.class, idnota);
                Empleados empleado = em.find(Empleados.class, idusuarioentrega);
                if(estado==1){
                    entregado ='1';
                    nota.setEntregado(Character.valueOf(entregado));
                    nota.setFechaentrega(gc.getTime());
                    nota.setPendiente(Character.valueOf(pendiente));
                    nota.setIdusuarioEntregado(idusuarioentrega);
                }else{
                    pendiente ='1';
                     nota.setEntregado(Character.valueOf(entregado));
                     nota.setPendiente(pendiente);
                     nota.setIdusuarioEntregado(idusuarioentrega);
                }
                List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();
            for (Detallesnotadepedido detallesnotadepedido : lista) {
                detallesnotadepedido.setEntregado(Character.valueOf(entregado));
                detallesnotadepedido.setPendiente(Character.valueOf(pendiente));
            }
                Historiconotapedido historico = new Historiconotapedido();
                        if(estado==1){
                                historico.setAccion("Entregado por"+empleado.getNameuser());
                                historico.setEntregado('1');
                                historico.setPendiente('0');
                        }else{
                                historico.setAccion("No entregada por "+empleado.getNameuser());
                                historico.setEntregado('0');
                                historico.setPendiente('1');
                        }
                                historico.setAnticipo(BigDecimal.ZERO);
                                historico.setFecharegistro(gc.getTime());
                                historico.setFkidnotapedido(nota);
                                historico.setHoraregistro(gc.getTime());
                                historico.setIdusuarioanulo(0);
                                historico.setIdusuarioentrega(0);
                                historico.setIdusuarioexpidio(0);
                                historico.setIdusuariocancelo(0);
                                historico.setPorcentajeaplicado(Short.valueOf("0"));
                                historico.setSaldo(BigDecimal.ZERO);
                                historico.setTotal(BigDecimal.ZERO);
                                historico.setTotalapagar(BigDecimal.ZERO);
                                historico.setRecargo(BigDecimal.ZERO);
                                historico.setPorcrecargo(BigDecimal.ZERO);
                                historico.setPorcentajedesc(BigDecimal.ZERO);
                                historico.setDescuento(BigDecimal.ZERO);
                                historico.setAnulado('0');
                                historico.setCancelado('0');
                                 em.persist(historico);
                //----------------------------------------------------------------------------------
                            long notaID = procesaListNotaHistorico(nota,historico);
          //----------------------------------------------------------------------------------
                            em.flush();
                       result = notaID;
        } catch (NumberFormatException e) {
            logger.error("Error en metodo entregarNotaPedido "+e.getLocalizedMessage());
            result = -1;
        }finally{
            
            return result;
        }
    }
//-----------------------------------------------------------------------------------------------

    /**
     *
     * @param xmlNota
     * @return
     */
    public StringBuilder parsearCaracteresEspecialesXML(String xmlNota){
    String xml = "No paso Nada";
    StringBuilder sb=null;
    try {
        sb=new StringBuilder(xmlNota);
            xml=StringEscapeUtils.escapeXml10(xmlNota.substring(xmlNota.indexOf("es>")+3,xmlNota.indexOf("</ob")));
            sb.replace(sb.indexOf("es>")+3, sb.indexOf("</ob"), xml);
    } catch (Exception e) {
        xml = "Error";
        logger.error("Error en metodo parsearCaracteresEspecialesXML "+e.getLocalizedMessage());
    }finally{
        return sb;
    }
}

    /**
     *
     * @param fecha1
     * @param fecha2
     * @param idvendedor
     * @return
     */
    @Override
    public String selectNotaEntreFechas(String fecha1, String fecha2,int idvendedor) {
        String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<Lista>\n";
        List<Notadepedido>lista = null;
        try {
            
            String resultProcFechas=chequeoFechas(fecha1,fecha2);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            if(resultProcFechas.equals("TODO OK")){    
                Query jpasql=em.createQuery("SELECT n FROM Notadepedido n WHERE  n.fechadecompra BETWEEN ?1 and ?2 AND n.entregado = ?3 AND n.pendiente = ?4 ORDER BY n.id desc",Notadepedido.class);                
                jpasql.setParameter("1", sdf.parse(fecha1),TemporalType.TIMESTAMP);
                jpasql.setParameter("2", sdf.parse(fecha2),TemporalType.TIMESTAMP);
                jpasql.setParameter("3", '0');
                jpasql.setParameter("4", '1');
                   
                                                    lista= jpasql.getResultList();
                                                    if(lista.size()>0){
                                                            for (Notadepedido notadepedido1 : lista) {
                                                                xml+=notadepedido1.toXML();
                                                            }
                                                               
                                                          xml=agregarDatosAlxml(xml,fecha1,fecha2);
                                                    }else {
                                                        xml+="<result>lista vacia</result>\n";
                    }
            }else {
                xml+=resultProcFechas;
            }
            
            
            
        } catch(ParseException e){
            xml+="<error>Error</error>\n";
            logger.error("Error en metodo selectnotaEntreFechas",e.fillInStackTrace());
        }finally{
                
                return xml+="</Lista>\n";
        }
    }

    /**
     *
     * @return
     */
    @Override
    public int getRecorCountNotas() {
        int retorno =0;
        try {
            Query notas = em.createNamedQuery("Notadepedido.findAll");
            retorno =notas.getResultList().size();
        } catch (Exception e) {
            retorno =-1;
            logger.error("Error en metodo getRecorCountNotas "+e.getLocalizedMessage());
        }finally{
            
            return retorno;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String selectAllNotas() {
        String lista = "<Lista>\n";
        List<Notadepedido>result=null;
        try {
            em.flush();
            
            Query consulta =em.createQuery("Select n From Notadepedido n ORDER BY n.id DESC, n.fechadecompra DESC,n.fkIdcliente.idPersona",Notadepedido.class);
            
            result = consulta.getResultList();
            
            if(result.isEmpty()) {
                lista="LA CONSULTA NO ARROJÓ RESULTADOS";
            } else{
                for (Notadepedido notape : result) {
                    lista+=devolverNotaProcesadaSB(notape);
                }
            }
           
        } catch (Exception e) {
            lista +="ERROR EN METODO selectAllNotas";
            logger.error("Error en metodo selecAllNotas "+e.getMessage());
        }finally{                                
                return lista+="</Lista>";
        }
    }
    private StringBuilder devolverNotaProcesadaSB(Notadepedido nota) {
                long idusuarioexpidio=0;
                String usuarioexpidio ="";
                long idusuarioanulonota=0;
                String usuarioanulonota ="";
                long idusuarioentregonota=0;
                String usuarioentregonota ="";
                long idusuariocancelonota =0;
                String usuariocancelonota="";
                StringBuilder sb=null;
        try {
                
                try {
                    sb=new StringBuilder(nota.toXML());
                } catch (Exception e) {
                    logger.error("error al convertir stringbuilder "+e.getLocalizedMessage());
                }
                
               
            if(nota.getIdUsuarioExpidioNota()>0){
                
                 idusuarioexpidio = (long) nota.getIdUsuarioExpidioNota();
                
                Empleados persona = em.find(Empleados.class, idusuarioexpidio);
                
                usuarioexpidio=persona.getNombre();
                
                sb.replace(sb.indexOf("dionota>")+8, sb.indexOf("</usuarioex"), usuarioexpidio);
                
            }
                    if(nota.getIdusuarioAnulado()>0){
              
                        idusuarioanulonota = nota.getIdusuarioAnulado();
                        
                        Empleados persona = em.find(Empleados.class, idusuarioanulonota);
                        
                        usuarioanulonota=persona.getNombre();
                        
                        sb.replace(sb.indexOf("anulonota>")+10, sb.indexOf("</usuarioanul"), usuarioanulonota.toUpperCase());
                        
                    }
                            if(nota.getIdusuarioEntregado()>0){
                          
                                idusuarioentregonota=nota.getIdusuarioEntregado();
                                
                                Empleados persona = em.find(Empleados.class, idusuarioentregonota);
                                
                                usuarioentregonota=persona.getNombre();
                                
                                sb.replace(sb.indexOf("oentregonota>")+13, sb.indexOf("</usuarioent"), usuarioentregonota.toUpperCase());
                                
                            }
                                if(nota.getIdusuariocancelo()>0){
                            
                                        idusuariocancelonota=nota.getIdusuariocancelo();
                                       
                                        Empleados persona = em.find(Empleados.class, idusuariocancelonota);
                                       
                                        usuariocancelonota=persona.getNombre();
                                        
                                        sb.replace(sb.indexOf("ocancelonota>")+13, sb.indexOf("</usuariocan"), usuariocancelonota.toUpperCase());
                                       
                                }
        } catch (Exception e) {
            
            logger.error("Error en metodo devolverNotaProcesadaSB "+e.getLocalizedMessage());
        }finally{
            
            return sb;
        }
    }

    /**
     *
     * @param index
     * @param recordCount
     * @return
     */
    @Override
    
    public String verNotasPedidoPaginadas(int index, int recordCount) {
        String result="<Lista>\n";
        try {
            
            //Query consulta = em.createNativeQuery("SELECT FIRST "+recordCount+" SKIP ("+index+"*"+recordCount+") * FROM NOTADEPEDIDO n ORDER BY n.ID DESC, n.FECHADECOMPRA DESC",Notadepedido.class);
            Query consulta = em.createQuery("SELECT n FROM Notadepedido n ORDER BY n.id DESC, n.fechadecompra DESC",Notadepedido.class);
             consulta.setMaxResults(recordCount);
             consulta.setFirstResult(index*recordCount);
            List<Notadepedido>lista = consulta.getResultList();
            
            if(lista.isEmpty()) {
                result+="LA CONSULTA NO ARROJÓ RESULTADOS!!!";
            } else{
                for (Notadepedido notape : lista) {                    
                    result+=devolverNotaProcesadaSB(notape).toString();
                }
                
            }
            
        } catch (Exception e) {
            result += "Error en metodo vernotaspedidopaginadas";
            logger.error("Error en metodo vernotasPedidoPaginadas "+e.getLocalizedMessage());
        }finally{
            result+="</Lista>";
            
        return result;
        }
    }

    /**
     *
     * @param idnota
     * @param idusuario
     * @param estado
     * @return
     */
    @Override
    public long anularNotaPedido(long idnota, long idusuario, int estado) {
        long result = 0L;
        char anulada ='0';
        try {
            //--------------------------------------------------------------------
                GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
            //--------------------------------------------------------------------
                Notadepedido nota = em.find(Notadepedido.class,idnota);
                Empleados empleado = em.find(Empleados.class,idusuario);
                if(estado==1){
                    anulada ='1';
                    nota.setFechaAnulado(gc.getTime());
                    nota.setIdusuarioAnulado((int)idusuario);
                    nota.setAnulado(Character.valueOf(anulada));
                }else{
                     nota.setAnulado(anulada);
                     nota.setFechaAnulado(null);
                     nota.setIdusuarioAnulado((int)idusuario);
                }
                List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();
            for (Detallesnotadepedido detallesnotadepedido : lista) {
                detallesnotadepedido.setAnulado(Character.valueOf(anulada));
            }
                Historiconotapedido historico = new Historiconotapedido();
                        if(estado==1){
                                historico.setAccion("Nota anulada"+empleado.getNameuser());
                                historico.setAnulado(anulada);
                        }else{
                                historico.setAccion("NOTa no anulada por "+empleado.getNameuser());
                                historico.setAnulado(anulada);
                        }
                                historico.setIdusuarioanulo((int)idusuario);
                                historico.setAnticipo(BigDecimal.ZERO);
                                historico.setFecharegistro(gc.getTime());
                                historico.setFkidnotapedido(nota);
                                historico.setHoraregistro(gc.getTime());
                                historico.setIdusuarioentrega(0);
                                historico.setIdusuarioexpidio(0);
                                historico.setIdusuariocancelo(0);
                                historico.setPorcentajeaplicado(Short.valueOf("0"));
                                historico.setSaldo(BigDecimal.ZERO);
                                historico.setTotal(BigDecimal.ZERO);
                                historico.setTotalapagar(BigDecimal.ZERO);
                                historico.setRecargo(BigDecimal.ZERO);
                                historico.setPorcrecargo(BigDecimal.ZERO);
                                historico.setPendiente('0');
                                historico.setEntregado('0');
                                historico.setDescuento(BigDecimal.ZERO);
                                historico.setCancelado('0');
                                historico.setPorcentajedesc(BigDecimal.ZERO);
                                em.persist(historico);
                //----------------------------------------------------------------------------------
                            long notaID = procesaListNotaHistorico(nota,historico);
          //----------------------------------------------------------------------------------
                            em.flush();
                       result = notaID;
        } catch (NumberFormatException e) {
            logger.error("Error en metodo anularNotaPedido "+e.getLocalizedMessage());
            result = -1;
        }finally{
            
            return result;
        }
    }

    /**
     *
     * @param xmlnotapedidomodificada
     * @return
     */
    @Override
    public long actualizarNotaPedido(String xmlnotapedidomodificada) {
        DatosNotaPedido datosnotapedido;
        long retorno =0L;
        try {
            datosnotapedido=xestreaNotapedido(xmlnotapedidomodificada);
            if(datosnotapedido.getIdnota()>0){
                Notadepedido nota = em.find(Notadepedido.class, datosnotapedido.getIdnota());
                retorno = procesarNotaaActualizar(datosnotapedido,nota);
            }else {
                retorno =-2;
            }
        } catch (Exception e) {
            retorno =-1;
            logger.error("Error en metodo actualizarNotapedido"+ e.getLocalizedMessage());
        }finally{
            
            return retorno;
        }
    }
    private long procesarNotaaActualizar(DatosNotaPedido datosnotapedido, Notadepedido nota) {
        long result =-3;
        try {
              //---------------------------------------------------------------------------------
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //---------------------------------------------------------------------------------
                                            nota.setAnulado(datosnotapedido.getAnulado());
                                            nota.setEnefectivo(datosnotapedido.getEnefectivo());
                                            nota.setEntregado(Character.valueOf(datosnotapedido.getEntregado()));
                                            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                            Clientes cliente =em.find(Clientes.class, datosnotapedido.getPersonas().getId());
                                                Double montotoalapagar = nota.getMontototalapagar().doubleValue();
                                                Double restomontoapagar = cliente.getTotalCompras().doubleValue()-montotoalapagar;
                                                cliente.setTotalCompras(BigDecimal.valueOf(restomontoapagar+datosnotapedido.getMontototalapagar()));
                                                em.merge(cliente);
                                            //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                            nota.setFkIdcliente(cliente);
                                            nota.setFkidporcentajenotaId(em.find(Porcentajes.class, datosnotapedido.getPorcentajes().getId_porcentaje()));
                                            nota.setIdTarjetaFk(em.find(TarjetasCreditoDebito.class, datosnotapedido.getTarjetacredito().getId_tarjeta()));
                                            nota.setIdUsuarioExpidioNota(datosnotapedido.getUsuario_expidio_nota());
                                            nota.setIdusuarioAnulado(datosnotapedido.getId_usuario_anulado());
                                            nota.setIdusuarioEntregado(datosnotapedido.getUsuario_entregado());
                                            nota.setMontoiva(BigDecimal.valueOf(datosnotapedido.getMontoiva()));
                                            nota.setNumerodecupon(datosnotapedido.getNumerodecupon());
                                            nota.setObservaciones(datosnotapedido.getObservaciones());
                                            nota.setPendiente(Character.valueOf(datosnotapedido.getPendiente()));
                                            nota.setRecargo(BigDecimal.valueOf(datosnotapedido.getRecargo()));
                                            nota.setSaldo(BigDecimal.valueOf(datosnotapedido.getSaldo()));
                                            nota.setStockfuturo(datosnotapedido.getStockfuturo());
                                            nota.setTotal(BigDecimal.valueOf(datosnotapedido.getMontototal()));
                                            nota.setFechadecompra(sdf.parse(datosnotapedido.getFechacompra()));
                                            nota.setFechaentrega(sdf.parse(datosnotapedido.getFechaentrega()));
                                            nota.setCancelado(Character.valueOf(datosnotapedido.getCancelado()));
                                            nota.setDescuentonota(BigDecimal.valueOf(datosnotapedido.getDescuentonota()));
                                            nota.setDescuentoPesos(BigDecimal.valueOf(datosnotapedido.getDescuentopesos()));
                                            nota.setIdusuariocancelo(datosnotapedido.getUsuario_cancelo_nota());
                                            try {
                                                nota.setMontototalapagar(BigDecimal.valueOf(datosnotapedido.getMontototalapagar()));
                                            } catch (Exception ex) {
                                                logger.error("Error al seteal el montotoal a pagar "+ex.getLocalizedMessage());
                                            }
                                            nota.setPorcdesctotal(BigDecimal.valueOf(datosnotapedido.getPorc_descuento_total()));
                                            nota.setPorcrecargo(BigDecimal.valueOf(datosnotapedido.getPorcentajerecargo()));
                                            if(datosnotapedido.getCancelado()=='1') {
                                                nota.setFecancelado(gc.getTime());
            }
                                            if(datosnotapedido.getAnulado()=='1') {
                                                nota.setFechaAnulado(gc.getTime());
            }
                                        backupLogListDetalleNotaAntesdeBorrar(nota);
                                  //-------------------DETALLE---------------------------------------------------
                                                List<Itemdetallesnota>lista = datosnotapedido.getDetallesnotapedido().getDetallesnota();
                                                //----------------------------------------------------------------------------------
                                                //Query deletesql = em.createNativeQuery("DELETE FROM DETALLESNOTADEPEDIDO d WHERE d.FK_IDNOTA = "+nota.getId());
                                                Query deletsql = em.createNamedQuery("Detallesnotadepedido.deleteById");
                                                deletsql.setParameter("idNota", nota.getId());
                                                int retorno = deletsql.executeUpdate();
                                                
                                                Query consulta11 = em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE  d.detallesnotadepedidoPK.fkIdnota = :fkIdnota");
                                                consulta11.setParameter("fkIdnota", nota.getId());
                                                em.flush();
                                                 Productos productos=null;
                                                 Itemdetallesnota itemdetallesnota=null;
                                                    for (Iterator<Itemdetallesnota> it = lista.iterator(); it.hasNext();) {
                                                         itemdetallesnota= it.next();
                                                            DetallesnotadepedidoPK pkdetalle = new DetallesnotadepedidoPK(itemdetallesnota.getId_nota(),itemdetallesnota.getId_producto());
                                                                                Detallesnotadepedido detallesnotadepedido = new Detallesnotadepedido();
                                                                                detallesnotadepedido.setAnulado(itemdetallesnota.getAnulado());
                                                                                detallesnotadepedido.setCancelado(itemdetallesnota.getCancelado());
                                                                                detallesnotadepedido.setCantidad(itemdetallesnota.getCantidad());
                                                                                productos =em.find(Productos.class, itemdetallesnota.getId_producto());
                                                                                detallesnotadepedido.setDescuento(BigDecimal.valueOf(itemdetallesnota.getDescuento()));
                                                                                detallesnotadepedido.setDetallesnotadepedidoPK(pkdetalle);
                                                                                detallesnotadepedido.setEntregado(Character.valueOf(itemdetallesnota.getEntregado()));
                                                                                detallesnotadepedido.setIva(BigDecimal.valueOf(itemdetallesnota.getIva()));
                                                                                detallesnotadepedido.setNotadepedido(em.find(Notadepedido.class, itemdetallesnota.getId_nota()));
                                                                                detallesnotadepedido.setPendiente(Character.valueOf(itemdetallesnota.getPendiente()));
                                                                                detallesnotadepedido.setPrecio(BigDecimal.valueOf(itemdetallesnota.getPrecio()));
                                                                                detallesnotadepedido.setPreciocondescuento(BigDecimal.valueOf(itemdetallesnota.getPreciocondescuento()));
                                                                                detallesnotadepedido.setProductos(productos);
                                                                                detallesnotadepedido.setSubtotal(BigDecimal.valueOf(itemdetallesnota.getSubtotal()));
                                                                                em.persist(detallesnotadepedido);
                                                                                em.flush();
                                                           ///+++++++++++++++++++++++++++++++++++++++++++++++++++
                                                  List<Detallesnotadepedido> listDNP= em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdproducto = :fkIdproducto")
                                                            .setParameter("fkIdproducto", itemdetallesnota.getId_producto()).getResultList();
                                                            productos.setDetallesnotadepedidoList(listDNP);
                                                            em.merge(productos);
                                                            em.flush();
                                                  ///*****************************************************+
                                                        }
            List<Detallesnotadepedido> listDNP= em.createQuery("SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdnota = :fkIdnota")
               .setParameter("fkIdnota", nota.getId()).getResultList();
               nota.setDetallesnotadepedidoList(listDNP);
              if(datosnotapedido.getAnticipoacum()!=nota.getAnticipo().doubleValue()){
                  nota.setAnticipo(BigDecimal.valueOf(datosnotapedido.getAnticipoacum()));
                  result =  almacenarHistorico(datosnotapedido,nota);
              }
              em.persist(nota);
              em.flush();
                  result=nota.getId();
                                  //-----------------------------------------------------------------------------
        } catch (ParseException e) {
            result=-4;
            logger.error("Error en metodo procesarNotaaActualizar "+e.getLocalizedMessage());
        }finally{
            
            return result;
        }
    }

    /**
     *
     * @param fecha1
     * @param fecha2
     * @param idvendedor
     * @return
     */
    @Override
    public String selecNotaEntreFechasEntrega(String fecha1, String fecha2, int idvendedor) {
        String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<Lista>\n";
        List<Notadepedido>lista = null;
        try {
            
            String resultChequeoFechas=chequeoFechas(fecha1, fecha2);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            if(resultChequeoFechas.equals("TODO OK")){
                Query jpasql = em.createQuery("SELECT n FROM Notadepedido n WHERE n.fechaentrega BETWEEN ?1 AND ?2 AND n.entregado = ?3 and n.pendiente = ?4 ORDER BY n.horacompra,n.id desc",Notadepedido.class);
                jpasql.setParameter("1", sdf.parse(fecha1),TemporalType.TIMESTAMP);
                jpasql.setParameter("2", sdf.parse(fecha2),TemporalType.TIMESTAMP);
                jpasql.setParameter("3", '0');
                jpasql.setParameter("4", '1');
                        //Query jpasql=em.createNativeQuery("SELECT * FROM NOTADEPEDIDO n WHERE CAST(n.FECHAENTREGA as date)  between CAST('"+fecha1+"' as DATE) and cast('"+fecha2+"' as date) and n.entregado=0 and n.pendiente=1 order by n.fechaentrega,n.horacompra,n.id desc", Notadepedido.class);
                            lista= jpasql.getResultList();
                            if(lista.size()>0){
                                
                                try {
                                    for (Notadepedido notadepedido1 : lista) {
                                        xml+=notadepedido1.toXML();
                                   }
                                } catch (Exception e) {
                                }
                                    
                               
                                 xml=agregarDatosAlxml(xml, fecha1, fecha2);
                            }else {
                                xml+="<result>lista vacia</result>\n";
                        }
            }else {
                xml+=resultChequeoFechas;
            }
                         
        } catch (ParseException e) {
            xml+="<error>Error</error>\n";
            logger.error("Error en metodo selectnotaEntreFechasEntrega "+e.getLocalizedMessage());
        }finally{                
                return  xml+="</Lista>\n";  
        }
    }
    private void backupLogListDetalleNotaAntesdeBorrar(Notadepedido nota) {
        try {
           logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
           logger.info("Creando registro de items nota a borrar");
           logger.info(" Nota N° "+nota.getId());
           List<Detallesnotadepedido>lista=nota.getDetallesnotadepedidoList();
            for (Iterator<Detallesnotadepedido> it = lista.iterator(); it.hasNext();) {
                logger.info("------------------------------------------------------");
                Detallesnotadepedido detallesnotadepedido = it.next();
                logger.info("id nota "+detallesnotadepedido.getNotadepedido().getId());
                logger.info("anulado "+detallesnotadepedido.getAnulado());
                logger.info("cancelado "+detallesnotadepedido.getCancelado());
                logger.info("cantidad "+detallesnotadepedido.getCantidad());
                logger.info("descuento "+detallesnotadepedido.getDescuento());
                logger.info("entregado "+detallesnotadepedido.getEntregado());
                logger.info("iva "+detallesnotadepedido.getIva());
                logger.info("pendiente "+detallesnotadepedido.getPendiente());
                logger.info("precio "+detallesnotadepedido.getPrecio());
                logger.info("preciodescuento "+detallesnotadepedido.getPreciocondescuento());
                logger.info("idproducto "+detallesnotadepedido.getProductos().getSid());
                logger.info("subtotal "+detallesnotadepedido.getSubtotal());
                logger.info("------------------------------------------------------");
            }
            logger.info("fin del registro");
           logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        } catch (Exception e) {
            logger.error("Error en metodo backupLogListDetalleNotaAntesdeBorrar");
        }
    }

    /**
     *
     * @param idnota
     * @param idEmpleado
     * @return
     */
    @Override
    public int eliminarNotaDePedido(long idnota, long idEmpleado) {
        int idRetorno=0;
        try {
           
            Query consulta=em.createNamedQuery("Notadepedido.findById");
            consulta.setParameter("id", idnota);
            
            if(!consulta.getResultList().isEmpty()){
                Notadepedido nota =em.find(Notadepedido.class, idnota);
                em.remove(nota);
                em.flush();
                idRetorno=1;
                GregorianCalendar gc = new GregorianCalendar();
                logger.info("El Empleado "+em.find(Empleados.class, idEmpleado)+" elimino la nota de pedido N° "+idnota+" el dia "+gc.getTime());
            }else {
                idRetorno=-2;
            }

        } catch (Exception e) {
            idRetorno=-1;
            logger.error("Error en metodo eliminarNotaDePedido "+e.getLocalizedMessage());
        }finally{
            
            return idRetorno;
        }
    }

    /**
     *
     * @return
     */
    @Override
    @SuppressWarnings("FinallyDiscardsException")
    public String calcularVentasMensualesHastaFechaYAnoActual() {
        String xml="<Lista>\n";
        try {
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("MM");
            String year = new SimpleDateFormat("YYYY").format(gc.getTime());
            
            
            
            
           int month=0;
           
            for (int i = 0; i < Integer.valueOf(sdf.format(gc.getTime())); i++) {
                            month++;
                            String ventaMensual="0";
                            Query consulta = em.createQuery("SELECT SUM(n.montototalapagar) FROM Notadepedido n WHERE SQL('EXTRACT(MONTH FROM ?)',n.fechadecompra) = ?1 AND SQL('EXTRACT(YEAR FROM ?)',n.fechadecompra) = ?2");
                            consulta.setParameter("1", month);
                            consulta.setParameter("2", year);
                            //Query consulta = em.createNativeQuery("SELECT SUM(n.MONTOTOTALAPAGAR) FROM NOTADEPEDIDO n WHERE EXTRACT(MONTH FROM n.FECHADECOMPRA)="+month+" AND EXTRACT(YEAR FROM n.FECHADECOMPRA)="+year+"");
                            

                                ventaMensual=consulta.getResultList().toString().replace("[", "").replace("]", "");
                                //ventaMensual=ventaMensual.substring(ventaMensual.indexOf("[")+1, ventaMensual.length()-1).toLowerCase();
                                

                            xml+="<Item>\n";
                            switch(i){
                                case 0:xml+="<month>ENE</month>\n";
                                    break;
                                case 1:xml+="<month>FEB</month>\n";    
                                    break;
                                case 2:xml+="<month>MAR</month>\n";       
                                    break;
                                case 3:xml+="<month>ABR</month>\n";       
                                    break;
                                case 4:xml+="<month>MAY</month>\n";       
                                    break;
                                case 5:xml+="<month>JUN</month>\n";       
                                    break;
                                case 6:xml+="<month>JUL</month>\n";       
                                    break;
                                case 7:xml+="<month>AGO</month>\n";       
                                    break;
                                case 8:xml+="<month>SEP</month>\n";       
                                    break;
                                case 9:xml+="<month>OCT</month>\n";       
                                    break;    
                                case 10:xml+="<month>NOV</month>\n";       
                                    break;        
                                case 11:xml+="<month>DIC</month>\n";       
                                            
                            }
                                    
                                  if(!"null".equals(ventaMensual)) {
                                      xml+= "<totalMonthlySales>"+ventaMensual+"</totalMonthlySales>\n";
                            } else {
                                      xml+= "<totalMonthlySales>0</totalMonthlySales>\n";
                            }
                                    xml+= "</Item>\n";
                
            }
            xml+="</Lista>";
            
        } catch (NumberFormatException e) {
            logger.error("Error en metodo calcularVentasMensualesHastaFechaYAnoActual"+e.getLocalizedMessage());
        }finally{
            
            return xml;
        }
    }

    private String chequeoFechas(String fecha1, String fecha2) {
        String xml="";
    
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            if(fecha1 == null) {
                xml="<error>Error fecha vacia</error>\n";
            } else {
                if(fecha2==null) {
                    xml="<error>Error fecha vacia</error>\n";
                } else{
                    if(fecha1.trim().length()!=sdf.toPattern().length()) {
                        xml="<error>Error fecha1 con patron desconocido o incorrecto</error>\n";
                    } else{
                        if(fecha2.trim().length()!=sdf.toPattern().length()) {
                            xml="<error>Error fecha2 con patron desconocido o incorrecto</error>\n";
                        } else{
                                sdf.setLenient(false);
                                    try {
                                        sdf.parse(fecha1.trim());
                                        sdf.parse(fecha2.trim());
                                        

                                           if(sdf.parse(fecha1).compareTo(sdf.parse(fecha2))<=0) {
                                              xml="TODO OK";
                                           } else {                                   
                                              xml="<result>rango no correcto de fechas elegido</result>\n";
                                            }


                                        
                                    } catch (ParseException e) {
                                        xml="<error>Error en parse de fechas</error>\n";                            
                                        logger.error("Error en parseo de fechas "+e.getLocalizedMessage());
                                    }
                        }
                    }
                }
            }
        } catch (Exception e) {
                xml="<error>Error en metodo chequeo fechas</error>";
                logger.error("Error en metodo cheque de fechas "+e.getLocalizedMessage());
                
        }finally{
            return xml;
        }
    }

    private String agregarDatosAlxml(String xml,String fecha1,String fecha2) {
        
         fecha1=fecha1.substring(3, 5)+"/"+fecha1.substring(0, 2)+"/"+fecha1.substring(6, 10);
         
                                                                fecha2=fecha2.substring(3, 5)+"/"+fecha2.substring(0, 2)+"/"+fecha2.substring(6, 10);
                                                                
                                                                StringBuilder sb =new StringBuilder(xml);
                                                                
                                                                String periodoconsultado = "<fechainicio>"+fecha1+"</fechainicio>\n" +
                                                                        
                                                                        "<fechafinal>"+fecha2+"</fechafinal>\n";
                                                                
                                                                 sb.replace(sb.indexOf("</numerocupon>")+14, sb.indexOf("<observaciones>"), "\n"+periodoconsultado);
                                                                 
                              return sb.toString();
    }
    
}
