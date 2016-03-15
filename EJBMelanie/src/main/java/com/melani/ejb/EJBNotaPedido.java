package com.melani.ejb;
import com.melani.entity.Clientes;
import com.melani.entity.Detallesnotadepedido;
import com.melani.entity.DetallesnotadepedidoPK;
import com.melani.entity.Empleados;
import com.melani.entity.HistoricoNotaPedido;
import com.melani.entity.Notadepedido;
import com.melani.entity.Personas;
import com.melani.entity.Porcentajes;
import com.melani.entity.Productos;
import com.melani.entity.TarjetasCreditoDebito;
import com.melani.utils.DatosNotaPedido;
import com.melani.utils.DetallesNotaPedido;
import com.melani.utils.ItemDetallesNota;
import com.melani.utils.ProjectHelpers;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.apache.log4j.Logger;
@Stateless(name="ejb/EJBNotaPedido")
@WebService(serviceName="ServiceNotaPedido",name="NotaPedidoWs")
public class EJBNotaPedido implements EJBNotaPedidoRemote {
    private static final Logger LOGGER = Logger.getLogger(EJBNotaPedido.class);   
    @PersistenceContext()    
    private EntityManager em;
    @EJB
      EJBProductosRemote producto;
    @EJB
    EJBPresupuestosRemote ejbpresupuesto;    
    DatosNotaPedido notadepedido;    
    volatile Double totalCompras;    
    private DatosNotaPedido xestreaNotapedido(String xmlNotapedido){
            XStream xestream = new XStream(new StaxDriver());
                xestream.alias("notapedido", DatosNotaPedido.class);
                xestream.alias("personas", DatosNotaPedido.Personas.class);
                xestream.alias("tarjetacredito", DatosNotaPedido.TarjetaCredito.class);
                xestream.alias("porcentajes", DatosNotaPedido.Porcentajes.class);
                xestream.alias("itemdetallesnota", ItemDetallesNota.class);
                xestream.alias("detallesnotapedido", DetallesNotaPedido.class);
                xestream.addImplicitCollection(DetallesNotaPedido.class, "list");
            return notadepedido = (DatosNotaPedido) xestream.fromXML(ProjectHelpers.parsearCaracteresEspecialesXML(xmlNotapedido));
    }
    
    @Override
    public long agregarNotaPedido(String xmlNotaPedido) {
        long retorno;                    
            if(!xmlNotaPedido.isEmpty()){
            
                retorno = almacenarNotaPedido(xestreaNotapedido(xmlNotaPedido));
            
            }else{
                retorno = -5;
            }       
            return retorno;        
    }
    private long almacenarNotaPedido(DatosNotaPedido notadepedido){
        long retorno;       
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Clientes cliente = em.find(Clientes.class, notadepedido.getPersonas().getId());
                            Notadepedido notape = new Notadepedido();                            
                                    
                                        almacenarNotaVoid(notape,notadepedido,sdf,gc);
                                    
                                em.persist(notape);                                                             
                                         switch(notadepedido.getStockfuturo()){
                                             case 0:{            
                                                    almacenarDetalleNotaConControlStock(notadepedido,notape);
                                                    almacenarHistorico(notadepedido,notape);            
                                                    }
                                             break;
                                             default :
                                             {            
                                                almacenarDetalleNota(notadepedido,notape);
                                                almacenarHistorico(notadepedido,notape);            
                                                    }
                                         }
                                         Query queryFindByIdProducto =em.createNamedQuery("Notadepedido.findClientFk");
                                            queryFindByIdProducto.setParameter("id", cliente.getIdPersona());
                                            List<Notadepedido>lista=queryFindByIdProducto.getResultList();
                                                cliente.setNotadepedidoList(lista);
                                                    totalCompras = cliente.getTotalCompras().doubleValue()+notape.getMontototalapagar().doubleValue();
                                                    int totalPuntos = cliente.getTotalEnPuntos().intValue()+totalCompras.intValue();
                                                    cliente.setTotalCompras(BigDecimal.valueOf(totalCompras));
                                                    cliente.setTotalEnPuntos(BigInteger.valueOf(totalPuntos));
                                                    cliente.setFechaCarga(gc.getTime());                                           
                                                em.persist(cliente);                       
                                                retorno = notape.getId();                                          
            return retorno;        
    }
    
    private long almacenarDetalleNota(DatosNotaPedido notadepedido, Notadepedido notape) {
        long retorno;        
            List<ItemDetallesNota>lista = notadepedido.getDetallesnotapedido().getDetallesnota();            
                    for (ItemDetallesNota itemdetallesnota : lista) {
                            Productos productos = em.find(Productos.class,itemdetallesnota.getId_producto());
                            DetallesnotadepedidoPK detallespk = new DetallesnotadepedidoPK(notape.getId(), itemdetallesnota.getId_producto());
                            Detallesnotadepedido detalles = new Detallesnotadepedido();                            
                            almacenarDetallesNota(productos,itemdetallesnota,notape,detallespk,detalles);
                    }                    
                    unirRelacion(notape);
                            retorno = notape.getId();        
            return retorno;        
    }
    
    private long almacenarDetalleNotaConControlStock(DatosNotaPedido notadepedido, Notadepedido notape) {
        long retorno;
        int stockDisponible;        
            List<ItemDetallesNota>lista = notadepedido.getDetallesnotapedido().getDetallesnota();
                    for (ItemDetallesNota itemdetallesnota : lista) {                        
                                Productos productos = em.find(Productos.class,itemdetallesnota.getId_producto());                                
                                DetallesnotadepedidoPK detallespk = new DetallesnotadepedidoPK(notape.getId(), itemdetallesnota.getId_producto());
                                Detallesnotadepedido detalles = new Detallesnotadepedido();                                
                                almacenarDetallesNota(productos,itemdetallesnota,notape,detallespk,detalles);                                                                
                                stockDisponible=producto.controlStockProducto(itemdetallesnota.getId_producto(), itemdetallesnota.getCantidad(), notadepedido.getUsuario_expidio_nota());
                            if(stockDisponible<=50 &&stockDisponible>=0 ) {
                                LOGGER.info("El stock Disponible para el producto "+" está bajando a nivel máximo debe actualizar o agregar mas productos");
                                } else{
                                if (stockDisponible<0) {
                                    LOGGER.info("Ocurrió un Error o hay faltante de stock, valor devuelto por la función "+stockDisponible+" el producto es "+productos.getDescripcion()+" su stock disponible "+productos.getCantidadDisponible().intValue());
                                    }
                            }
                    }
                    unirRelacion(notape);                               
                    retorno = notape.getId();        
            return retorno;
        
    }
    private void almacenarHistorico(DatosNotaPedido notadepedido,Notadepedido notape){
        long resultado;
                    GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());                    
                                    HistoricoNotaPedido historico = new HistoricoNotaPedido();
                                    historico.setAnticipo(BigDecimal.valueOf(notadepedido.getAnticipo()));
                                    historico.setEntregado(notadepedido.getEntregado());
                                    historico.setFecharegistro(gc.getTime());
                                    historico.setFkidnotapedido(em.find(Notadepedido.class, notape.getId()));
                                        if(notadepedido.getObservaciones().length()>0){
                                            historico.setObservaciones(notadepedido.getObservaciones());
                                        }else{
                                            historico.setObservaciones("");
                                        }
                                    historico.setPendiente(notadepedido.getPendiente());
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
                        Query queryFindByIdProducto = em.createNamedQuery("HistoricoNotaPedido.findByFkIdNotaPedido");
                                queryFindByIdProducto.setParameter("idnota", notape.getId());
                                notape.setHistoriconotapedidoList(queryFindByIdProducto.getResultList());                   
                                em.persist(notape);                    
                    resultado = historico.getIdhistorico();
        //return resultado;
    }

    @Override
    public String selectUnaNota(long idnta) {
        String xml = "<Lista>\n";        
            Notadepedido nota = em.find(Notadepedido.class, idnta);
            if(nota != null) {               
                xml+=devolverNotaProcesadaSB(nota);
            } else {
                xml+="<nota>nota no encontrada</nota>";
            }      
            return xml+="</Lista>";    
    }
    
    @Override
    public long cancelarNotaPedido(long idnota,long idusuariocancelo,int estado) {
        long result = 0L;
        char cancelado ='0';
                              GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
                       SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            Notadepedido nota = em.find(Notadepedido.class,idnota);
            Empleados empleado = em.find(Empleados.class, idusuariocancelo);
            if(estado==1){
                cancelado ='1';
                nota.setCancelado(cancelado);
                nota.setIdusuariocancelo(idusuariocancelo);
                nota.setFecancelado(gc.getTime());
                nota.setUltimaActualizacion(em.find(Empleados.class, idusuariocancelo)+" "+sdf.format(gc.getTime()));
            }else{
                nota.setCancelado(cancelado);
                nota.setIdusuariocancelo(0L);
                nota.setFecancelado(null);
                nota.setUltimaActualizacion(em.find(Empleados.class, idusuariocancelo)+" "+sdf.format(gc.getTime()));
            }
            List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();
            for (Detallesnotadepedido detallesnotadepedido : lista) {
                detallesnotadepedido.setCancelado(cancelado);
            }
                            HistoricoNotaPedido historico = new HistoricoNotaPedido();
                            if(estado==1){
                                historico.setAccion("Cancelada por "+empleado.getNameuser());
                                historico.setCancelado(cancelado);
                                historico.setIdusuariocancelo(idusuariocancelo);
                            }else{
                                historico.setAccion("No cancelada por "+empleado.getNameuser());
                                historico.setCancelado(cancelado);
                                historico.setIdusuariocancelo(0L);
                            }
                                       procesarHistorico(historico,gc,nota);
                                 em.persist(historico);
                            long notaID = procesaListNotaHistorico(nota);
                            result = notaID;       
            return result;        
    }
    
    private long procesaListNotaHistorico(Notadepedido nota) {        
                            Query queryFindByIdProducto = em.createNamedQuery("Historiconotapedido.findByFkidnotapedido");
                             queryFindByIdProducto.setParameter("idnota", nota.getId());
                                    nota.setHistoriconotapedidoList(queryFindByIdProducto.getResultList());                      
                                 em.persist(nota);
            return nota.getId();       
    }
    
    @Override
    public long entregarNotaPedido(long idnota, long idusuarioentrega, int estado) {
        long result;
        char pendiente = '0';
        char entregado ='0';
        
                GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                Notadepedido nota = em.find(Notadepedido.class, idnota);
                Empleados empleado = em.find(Empleados.class, idusuarioentrega);
                if(estado==1){
                    entregado ='1';
                    nota.setEntregado(entregado);
                    nota.setFechaentrega(gc.getTime());
                    nota.setPendiente(pendiente);
                    nota.setIdusuarioEntregado(idusuarioentrega);
                    nota.setUltimaActualizacion(em.find(Empleados.class, idusuarioentrega)+" "+sdf.format(gc.getTime()));
                }else{
                    pendiente ='1';
                     nota.setEntregado(entregado);
                     nota.setFechaentrega(null);
                     nota.setPendiente(pendiente);
                     nota.setIdusuarioEntregado(0L);
                     nota.setUltimaActualizacion(em.find(Empleados.class, idusuarioentrega)+" "+sdf.format(gc.getTime()));
                }
                List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();
            for (Detallesnotadepedido detallesnotadepedido : lista) {
                detallesnotadepedido.setEntregado(entregado);
                detallesnotadepedido.setPendiente(pendiente);
            }
            em.persist(nota);            
                HistoricoNotaPedido historico = new HistoricoNotaPedido();
                        if(estado==1){
                                historico.setAccion("Entregado por "+empleado.getNameuser());
                                historico.setEntregado('1');
                                historico.setPendiente('0');
                        }else{
                                historico.setAccion("No entregada por "+empleado.getNameuser());
                                historico.setEntregado('0');
                                historico.setPendiente('1');
                        }                        
                        procesarHistorico(historico,gc,nota);                                        
                                 em.persist(historico);
                            long notaID = procesaListNotaHistorico(nota);
                       result = notaID;
           return result;        
    }
    
    @Override
    public String selectNotaEntreFechasCompra(String desde, String hasta,long idvendedor) {
        String xml ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"+"<Lista>\n";
        List<Notadepedido>lista;
       
            
            String resultProcFechas=chequearFechas(desde,hasta);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            if(resultProcFechas.equals("TODO OK")){    
            try {
                Query jpasql=em.createQuery("SELECT n FROM Notadepedido n WHERE  "
                        + "n.fechadecompra BETWEEN ?1 and ?2 AND n.entregado = ?3 AND n.pendiente = ?4 ORDER BY n.id desc",Notadepedido.class);                
                jpasql.setParameter("1", sdf.parse(desde),TemporalType.TIMESTAMP);
                jpasql.setParameter("2", sdf.parse(hasta),TemporalType.TIMESTAMP);
                jpasql.setParameter("3", '0');
                jpasql.setParameter("4", '1');
                lista= jpasql.getResultList();
                if(lista.size()>0){
                    xml+=iterateOverListNote(lista);
                    xml+=agregarDatosAlxml(xml,desde,hasta);
                }else {
                    xml+="<result>lista vacia</result>\n";
                }
            } catch (ParseException ex) {
                LOGGER.error("Error al parsear fechas");
            }
            }else {
                xml+=resultProcFechas;
            }   
        return xml+="</Lista>\n";        
    }

    @Override
    public int getRecorCountNotas() {
        int retorno;        
            Query notas = em.createNamedQuery("Notadepedido.findAll");
            retorno =notas.getResultList().size();          
            return retorno;        
    }
    
    @Override
    public String selectAllNotas() {
        String lista = "\"<Lista>\\n\"";
        List<Notadepedido>result;        
            Query queryFindByIdProducto =em.createQuery("Select n From Notadepedido n ORDER BY n.id DESC, "
                    + "n.fechadecompra DESC,n.fkIdcliente.idPersona",Notadepedido.class);            
            result = queryFindByIdProducto.getResultList();            
            if(result.isEmpty()) {
                lista+="LA CONSULTA NO ARROJÓ RESULTADOS";
            } else{
                for (Notadepedido notape : result) {
                    lista+=devolverNotaProcesadaSB(notape);
                }
            }                                 
                return lista+="</Lista>";        
    }
    private StringBuilder devolverNotaProcesadaSB(Notadepedido nota) {
                long idusuarioexpidio;
                String usuarioexpidio;
                long idusuarioanulonota;
                String usuarioanulonota;
                long idusuarioentregonota;
                String usuarioentregonota;
                long idusuariocancelonota;
                String usuariocancelonota;
                StringBuilder sb;
                    sb=new StringBuilder();
                    sb.append(nota.toXML());           
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
            return sb;        
    }

    @Override    
    public String verNotasPedidoPaginadas(int index, int recordCount) {
        String result = "\"<Lista>\\n\"";
        Query queryFindByIdProducto = em.createNamedQuery("Notadepedido.searchAllOrderDesc",Notadepedido.class);
             queryFindByIdProducto.setMaxResults(recordCount);
             queryFindByIdProducto.setFirstResult(index*recordCount);
            List<Notadepedido>lista = queryFindByIdProducto.getResultList();            
            if(lista.isEmpty()) {
                result+="LA CONSULTA NO ARROJÓ RESULTADOS!!!";
            } else{
                    StringBuilder processNote = new StringBuilder(10);
                for (Notadepedido notape : lista) {   
                    
                    processNote.append(devolverNotaProcesadaSB(notape));
                }
                result+=processNote;
            }
            result+="</Lista>";            
        return result;        
    }

    @Override
    public long anularNotaPedido(long idnota, long idusuario, int estado) {
        long result;
        char anulada ='0';        
                GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");           
                Notadepedido nota = em.find(Notadepedido.class,idnota);
                Empleados empleado = em.find(Empleados.class,idusuario);
                if(estado==1){
                    anulada ='1';
                    nota.setFechaAnulado(gc.getTime());
                    nota.setIdusuarioAnulado(idusuario);
                    nota.setAnulado(anulada);
                    nota.setUltimaActualizacion(em.find(Empleados.class, idusuario)+" "+sdf.format(gc.getTime()));
                }else{
                     nota.setAnulado(anulada);
                     nota.setFechaAnulado(null);
                     nota.setIdusuarioAnulado(0L);
                     nota.setUltimaActualizacion(em.find(Empleados.class, idusuario)+" "+sdf.format(gc.getTime()));
                }
                List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();
            for (Detallesnotadepedido detallesnotadepedido : lista) {
                detallesnotadepedido.setAnulado(anulada);
            }
                HistoricoNotaPedido historico = new HistoricoNotaPedido();
                        if(estado==1){
                                historico.setAccion("Nota anulada "+empleado.getNameuser());
                                historico.setAnulado(anulada);
                                historico.setIdusuarioanulo(idusuario);
                        }else{
                                historico.setAccion("NOTa no anulada por "+empleado.getNameuser());
                                historico.setAnulado(anulada);
                                historico.setIdusuarioanulo(0L);
                        }
                                procesarHistorico(historico,gc,nota);                                
                                em.persist(historico);               
                            long notaID = procesaListNotaHistorico(nota);                            
                       result = notaID;        
            return result;        
    }

    @Override
    public long actualizarNotaPedido(String xmlnotapedidomodificada) {
        DatosNotaPedido datosnotapedido;
        long retorno;       
            if(!xmlnotapedidomodificada.isEmpty()){
                datosnotapedido=xestreaNotapedido(xmlnotapedidomodificada);
                if(datosnotapedido.getIdnota()>0){
                    Notadepedido nota = em.find(Notadepedido.class, datosnotapedido.getIdnota());
                    retorno = procesarNotaaActualizar(datosnotapedido,nota);
                }else {
                    retorno =-2;
                }
            }else{
                retorno = -5;
            }       
            return retorno;        
}
    private long procesarNotaaActualizar(DatosNotaPedido datosnotapedido, Notadepedido nota) {
        long result =-3;       
            GregorianCalendar gc = new GregorianCalendar();            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                                            Clientes cliente =em.find(Clientes.class, datosnotapedido.getPersonas().getId());
                                                Double montotoalapagar = nota.getMontototalapagar().doubleValue();
                                                Double restomontoapagar = cliente.getTotalCompras().doubleValue()- montotoalapagar;
                                                cliente.setTotalCompras(BigDecimal.valueOf(restomontoapagar+datosnotapedido.getMontototalapagar()));
                                                em.merge(cliente);
                                                        nota.setAnulado(datosnotapedido.getAnulado());
                                                        nota.setEnefectivo(datosnotapedido.getEnefectivo());
                                                        nota.setEntregado(datosnotapedido.getEntregado());
                                                        nota.setFkIdcliente(cliente);
                                                        nota.setFkidporcentajenotaId(em.find(Porcentajes.class, 
                                                                datosnotapedido.getPorcentajes().getId_porcentaje()));
                                                        nota.setIdTarjetaFk(em.find(TarjetasCreditoDebito.class, 
                                                                datosnotapedido.getTarjetacredito().getId_tarjeta()));
                                                        nota.setIdUsuarioExpidioNota(datosnotapedido.getUsuario_expidio_nota());
                                                        nota.setIdusuarioAnulado(datosnotapedido.getId_usuario_anulado());
                                                        nota.setIdusuarioEntregado(datosnotapedido.getUsuario_entregado());
                                                        nota.setMontoiva(BigDecimal.valueOf(datosnotapedido.getMontoiva()));
                                                        nota.setNumerodecupon(datosnotapedido.getNumerodecupon());
                                                        if(datosnotapedido.getObservaciones().length()>0){
                                                            nota.setObservaciones(datosnotapedido.getObservaciones());
                                                        }else{
                                                            nota.setObservaciones("");
                                                        }
                                                        nota.setUltimaActualizacion(em.find(Empleados.class, datosnotapedido.getIdVendedor()).getNameuser()+" "+sdf.format(gc.getTime()));
                                                        nota.setPendiente(datosnotapedido.getPendiente());
                                                        nota.setRecargo(BigDecimal.valueOf(datosnotapedido.getRecargo()));
                                                        nota.setSaldo(BigDecimal.valueOf(datosnotapedido.getSaldo()));
                                                        nota.setStockfuturo(datosnotapedido.getStockfuturo());
                                                        nota.setTotal(BigDecimal.valueOf(datosnotapedido.getMontototal()));
                                                                try {
                                                                    nota.setFechadecompra(DateFormat.getDateInstance().parse(datosnotapedido.getFechacompra()));
                                                                    nota.setFechaentrega(DateFormat.getDateInstance().parse(datosnotapedido.getFechaentrega()));
                                                                } catch (ParseException ex) {
                                                                    LOGGER.error("Error al parsear fechas al almacenar notas de pedido");
                                                                }                                                        
                                                        nota.setCancelado(datosnotapedido.getCancelado());
                                                        nota.setDescuentonota(BigDecimal.valueOf(datosnotapedido.getDescuentonota()));
                                                        nota.setDescuentoPesos(BigDecimal.valueOf(datosnotapedido.getDescuentopesos()));
                                                        nota.setIdusuariocancelo(datosnotapedido.getUsuario_cancelo_nota());                                                        
                                                            try {
                                                                nota.setMontototalapagar(BigDecimal.valueOf(datosnotapedido.getMontototalapagar()));
                                                            } catch (Exception ex) {
                                                                LOGGER.error("Error al seteal el montotoal a pagar ");
                                                            }
                                                            nota.setPorcdesctotal(BigDecimal.valueOf(datosnotapedido.getPorc_descuento_total()));
                                                            nota.setPorcrecargo(BigDecimal.valueOf(datosnotapedido.getPorcentajerecargo()));
                                                                    if(datosnotapedido.getCancelado()=='1') {
                                                                        nota.setFecancelado(gc.getTime());
                                                                    }
                                                                    if(datosnotapedido.getAnulado()=='1') {
                                                                        nota.setFechaAnulado(gc.getTime());
                                                                   }                                                
                                                   em.persist(nota);  
                                                List<ItemDetallesNota>lista = datosnotapedido.getDetallesnotapedido().getDetallesnota();
                                                Query deletsql = em.createNamedQuery("Detallesnotadepedido.deleteById");
                                                deletsql.setParameter("idNota", nota.getId());
                                                int retorno = deletsql.executeUpdate();                                                
                                                Query queryDetailsOrders1 = em.createNamedQuery("Detallesnotadepedido.findByFkIdnota");
                                                queryDetailsOrders1.setParameter("fkIdnota", nota.getId());                                                
                                                 Productos productos;
                                                 ItemDetallesNota itemdetallesnota;
                                                    for (Iterator<ItemDetallesNota> it = lista.iterator(); it.hasNext();) {
                                                         itemdetallesnota= it.next();
                                                         productos =em.find(Productos.class, itemdetallesnota.getId_producto());
                                                            DetallesnotadepedidoPK pkdetalle = new DetallesnotadepedidoPK(itemdetallesnota.getId_nota()
                                                                                                        ,itemdetallesnota.getId_producto());
                                                                                Detallesnotadepedido detallesnotadepedido = new Detallesnotadepedido();
                                                                                
                                                           almacenarDetallesNota(productos, itemdetallesnota,nota, pkdetalle, detallesnotadepedido);                     
                                                            
                                                     }//end for
                                            List<Detallesnotadepedido> queryDetailsOrdersFindIdOrder= em.createNamedQuery(
                                                    "Detallesnotadepedido.findByFkIdnota")
                                               .setParameter("fkIdnota", nota.getId()).getResultList();
                                               nota.setDetallesnotadepedidoList(queryDetailsOrdersFindIdOrder);
                                               em.merge(nota);
                                if(datosnotapedido.getAnticipoacum()!=nota.getAnticipo().doubleValue()){
            try {
                nota.setAnticipo(BigDecimal.valueOf(datosnotapedido.getAnticipoacum()));
                almacenarHistorico(datosnotapedido,nota);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(EJBNotaPedido.class.getName()).log(Level.SEVERE, null, ex);
            }
                                }   
                  result=nota.getId();               
            return result;        
    }
    
    @Override
    public String selecNotaEntreFechasEntrega(String fecha1, String fecha2) {
        String xml = "<Lista>\n";
        String pepito;
        String resultchequearFechas=chequearFechas(fecha1, fecha2);            
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");            
                if(resultchequearFechas.equals("TODO OK")){
                    
                    Query jpasql = em.createQuery("SELECT n FROM Notadepedido n WHERE n.fechaentrega "
                        + "BETWEEN ?1 AND ?2 AND n.entregado = ?3 and n.pendiente = ?4 ORDER BY n.horacompra,n.id desc",Notadepedido.class);
                        try {
                    
                            jpasql.setParameter("1", sdf.parse(fecha1),TemporalType.TIMESTAMP);
                            jpasql.setParameter("2", sdf.parse(fecha2),TemporalType.TIMESTAMP);
                        } catch (ParseException ex) {
                            LOGGER.error("Error al parsear fechas en selectnotaentrefechasentrega");
                        }                
                                 jpasql.setParameter("3", '0');
                                 jpasql.setParameter("4", '1');
                        List<Notadepedido>listaDeNotasEntreFechasEntrega;              
                               listaDeNotasEntreFechasEntrega= jpasql.getResultList();
                                
                                    if(listaDeNotasEntreFechasEntrega.size()>0){                                       
                                        pepito=iterateOverListNote(listaDeNotasEntreFechasEntrega);
                                        
                                        return xml+=agregarDatosAlxml(pepito, fecha1, fecha2)+"</Lista>";
                                        
                                    }else {
                                        xml+="<result>lista vacia</result>\n";
                                    }
                }else {
                    
                    xml+=resultchequearFechas;
                }
                
                
                return  "";          
    }
    
    @Override
    public int eliminarNotaDePedido(long idnota, long idEmpleado) {
        int idRetorno=0;
                   Query queryFindByIdProducto=em.createQuery("SELECT n FROM Notadepedido n WHERE n.id = :id");
            queryFindByIdProducto.setParameter("id", idnota);            
            if(!queryFindByIdProducto.getResultList().isEmpty()){
                Notadepedido nota =em.find(Notadepedido.class, idnota);
                em.remove(nota);                
                idRetorno=1;
                GregorianCalendar gc = new GregorianCalendar();
                LOGGER.info(new StringBuilder("El Empleado ")
                        .append(em.find(Empleados.class, idEmpleado))
                        .append(" elimino la nota de pedido N° ")
                        .append(idnota).append(" el dia ")
                        .append(gc.getTime()).toString());
            }else {
                idRetorno=-2;
            }   
            return idRetorno;        
    }

    @Override
    @SuppressWarnings("FinallyDiscardsException")
    public String calcularVentasMensualesHastaFechaYAnoActual() {
        String xml = "<Lista>\n";        
            GregorianCalendar gc = new GregorianCalendar();            
            String year = new SimpleDateFormat("YYYY").format(gc.getTime());       
           int month=0;
           StringBuilder xmlLoop = new StringBuilder(10);           
            for (int i = 0; i < 12; i++) {
                        xmlLoop.setLength(0);
                            month++;
                            String ventaMensual;                            
                            Query queryTotalProductMonth;
                            queryTotalProductMonth = em.createQuery("SELECT SUM(n.montototalapagar) FROM Notadepedido n WHERE SQL('EXTRACT(MONTH FROM ?)',n.fechadecompra) = ?1 AND SQL('EXTRACT(YEAR FROM ?)',n.fechadecompra) = ?2");
                            queryTotalProductMonth.setParameter("1", month);
                            queryTotalProductMonth.setParameter("2", year);                            
                            ventaMensual = String.valueOf(queryTotalProductMonth.getSingleResult());                            
                            xmlLoop.append("<Item>\n");
                            switch(i){
                                case 0:xmlLoop.append("<month>ENE</month>\n");
                                    break;
                                case 1:xmlLoop.append("<month>FEB</month>\n");    
                                    break;
                                case 2:xmlLoop.append("<month>MAR</month>\n");       
                                    break;
                                case 3:xmlLoop.append("<month>ABR</month>\n");       
                                    break;
                                case 4:xmlLoop.append("<month>MAY</month>\n");       
                                    break;
                                case 5:xmlLoop.append("<month>JUN</month>\n");       
                                    break;
                                case 6:xmlLoop.append("<month>JUL</month>\n");       
                                    break;
                                case 7:xmlLoop.append("<month>AGO</month>\n");       
                                    break;
                                case 8:xmlLoop.append("<month>SEP</month>\n");       
                                    break;
                                case 9:xmlLoop.append("<month>OCT</month>\n");       
                                    break;    
                                case 10:xmlLoop.append("<month>NOV</month>\n");       
                                    break;        
                                case 11:xmlLoop.append("<month>DIC</month>\n");       
                                            
                            }
                            xml+=xmlLoop;
                                    
                                  if(!"null".equals(ventaMensual)) {
                                      xml+="<totalMonthlySales>"+ventaMensual+"</totalMonthlySales>\n";
                                    } else {
                                      xml+="<totalMonthlySales>0</totalMonthlySales>\n";
                                    }
                            xml+="</Item>\n";                
            }
            xml+="</Lista>";               
            return xml;        
    }

    private String chequearFechas(String fecha1, String fecha2) {
        String xml = null;
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
                                        LOGGER.error("Error en parseo de fechas ");
                                    }
                        }
                    }
                }
            }       
            return xml;        
    }

    private String agregarDatosAlxml(String xml,String fecha1,String fecha2) {        
         fecha1=fecha1.substring(3, 5)+"/"+fecha1.substring(0, 2)+"/"+fecha1.substring(6, 10);         
                                                                fecha2=fecha2.substring(3, 5)+"/"+fecha2.substring(0, 2)+"/"+fecha2.substring(6, 10);                                                                
                                                                StringBuilder sb =new StringBuilder(xml);                                                                
                                                                String periodoqueryFindByIdProductodo = 
                                                                        "<fechainicio>" + fecha1 + "</fechainicio>\n" + 
                                                                        "<fechafinal>" + fecha2 + "</fechafinal>\n";                                                                
                                                                 sb.replace(sb.indexOf("</numerocupon>")+14, sb.indexOf("<observaciones>")
                                                                         , "\n"+periodoqueryFindByIdProductodo); 
                                                                      
      return sb.toString();
    }

    private void almacenarDetallesNota(Productos productos, ItemDetallesNota itemdetallesnota, Notadepedido notape, DetallesnotadepedidoPK detallespk, Detallesnotadepedido detalles) {
                                detalles.setCancelado(itemdetallesnota.getCancelado());
                                detalles.setCantidad(itemdetallesnota.getCantidad());
                                detalles.setDescuento(BigDecimal.valueOf(itemdetallesnota.getDescuento()));
                                detalles.setPreciocondescuento(BigDecimal.valueOf(itemdetallesnota.getPreciocondescuento()));
                                detalles.setEntregado(itemdetallesnota.getEntregado());
                                detalles.setIva(BigDecimal.valueOf(itemdetallesnota.getIva()));
                                detalles.setNotadepedido(notape);
                                detalles.setPendiente(itemdetallesnota.getPendiente());
                                detalles.setPrecio(BigDecimal.valueOf(itemdetallesnota.getPrecio()));
                                detalles.setProductos(productos);
                                detalles.setSubtotal(BigDecimal.valueOf(itemdetallesnota.getSubtotal()));
                                detalles.setDetallesnotadepedidoPK(detallespk);                        
                                em.persist(detalles);
                                    Query queryFindByIdProducto = em.createNamedQuery("Detallesnotadepedido.findByFkIdproducto");
                                        queryFindByIdProducto.setParameter("fkIdproducto", itemdetallesnota.getId_producto());
                                        productos.setDetallesnotadepedidoList(queryFindByIdProducto.getResultList());
    }

    private void unirRelacion(Notadepedido notape) {
        Query queryDetailsOrders = em.createNamedQuery("Detallesnotadepedido.findByFkIdnota");
                                        queryDetailsOrders.setParameter("fkIdnota", notape.getId());
                                        notape.setDetallesnotadepedidoList(queryDetailsOrders.getResultList());
                                        em.merge(notape);
    }

    private void procesarHistorico(HistoricoNotaPedido historico, GregorianCalendar gc, Notadepedido nota) {
                                        historico.setAnticipo(BigDecimal.ZERO);                                
                                        historico.setFecharegistro(gc.getTime());
                                        historico.setFkidnotapedido(nota);
                                        historico.setHoraregistro(gc.getTime());
                                        historico.setPendiente('0');
                                        historico.setEntregado('0');
                                        historico.setIdusuarioanulo(0L);
                                        historico.setIdusuarioentrega(0L);
                                        historico.setIdusuarioexpidio(0L);
                                        historico.setObservaciones("");
                                        historico.setPorcentajeaplicado(Short.valueOf("0"));
                                        historico.setSaldo(BigDecimal.ZERO);
                                        historico.setTotal(BigDecimal.ZERO);
                                        historico.setTotalapagar(BigDecimal.ZERO);
                                        historico.setRecargo(BigDecimal.ZERO);
                                        historico.setPorcrecargo(BigDecimal.ZERO);
                                        historico.setPorcentajedesc(BigDecimal.ZERO);
                                        historico.setDescuento(BigDecimal.ZERO);
                                        historico.setAnulado('0');
    }

    private void almacenarNotaVoid(Notadepedido notape, DatosNotaPedido notadepedido, SimpleDateFormat sdf, GregorianCalendar gc) {
        try {
            notape.setAnticipo(BigDecimal.valueOf(notadepedido.getAnticipo()));
            notape.setAnulado(notadepedido.getAnulado());
            notape.setEnefectivo(notadepedido.getEnefectivo());
            notape.setPendiente(notadepedido.getPendiente());
            notape.setEntregado(notadepedido.getEntregado());
            notape.setFkIdcliente(em.find(Personas.class, notadepedido.getPersonas().getId()));
            notape.setFkidporcentajenotaId(em.find(Porcentajes.class, notadepedido.getPorcentajes().getId_porcentaje()));
            notape.setIdTarjetaFk(em.find(TarjetasCreditoDebito.class, notadepedido.getTarjetacredito().getId_tarjeta()));
            notape.setIdUsuarioExpidioNota(notadepedido.getUsuario_expidio_nota());
            notape.setIdusuarioAnulado(notadepedido.getId_usuario_anulado());
            notape.setIdusuarioEntregado(notadepedido.getUsuario_entregado());
            notape.setMontoiva(BigDecimal.valueOf(notadepedido.getMontoiva()));
            notape.setNumerodecupon(notadepedido.getNumerodecupon());
            if(notadepedido.getObservaciones().length()>0){
                notape.setObservaciones(notadepedido.getObservaciones());
            }else{
                notape.setObservaciones("");
            }
            notape.setRecargo(BigDecimal.valueOf(notadepedido.getRecargo()));
            notape.setSaldo(BigDecimal.valueOf(notadepedido.getSaldo()));
            notape.setStockfuturo(notadepedido.getStockfuturo());
            notape.setTotal(BigDecimal.valueOf(notadepedido.getMontototal()));
            notape.setHoracompra(gc.getTime());
            notape.setFechadecompra(gc.getTime());
            notape.setFechaentrega(sdf.parse(notadepedido.getFechaentrega()));
            notape.setCancelado(notadepedido.getCancelado());
            notape.setDescuentonota(BigDecimal.valueOf(notadepedido.getDescuentonota()));
            notape.setDescuentoPesos(BigDecimal.valueOf(notadepedido.getDescuentopesos()));
            notape.setUltimaActualizacion(ResourceBundle.getBundle("config").getString("FECHA_DEFAULT"));
            notape.setIdusuariocancelo(notadepedido.getUsuario_cancelo_nota());
            
            notape.setMontototalapagar(BigDecimal.valueOf(notadepedido.getMontototalapagar()));
            
            notape.setPorcdesctotal(BigDecimal.valueOf(notadepedido.getPorc_descuento_total()));
            notape.setPorcrecargo(BigDecimal.valueOf(notadepedido.getPorcentajerecargo()));
                        if(notadepedido.getCancelado()=='1') {
                            notape.setFecancelado(gc.getTime());
                        }else{
                            notape.setFecancelado(sdf.parse(ResourceBundle.getBundle("config").getString("FECHA_DEFAULT")));

                        }
            if(notadepedido.getAnulado()=='1') {
                notape.setFechaAnulado(gc.getTime());
            }else{
                notape.setFechaAnulado(sdf.parse(ResourceBundle.getBundle("config").getString("FECHA_DEFAULT")));
            }
        } catch (ParseException ex) {
            LOGGER.error("eRROR con la fecha");
        }
    }
    private String iterateOverListNote(List<Notadepedido> lista) {
        StringBuilder xmlLoop = new StringBuilder(10);
                for (Notadepedido notadepedido1 : lista) {
                    xmlLoop.append(notadepedido1.toXML());
                                   }
        return xmlLoop.toString();
    }    
}