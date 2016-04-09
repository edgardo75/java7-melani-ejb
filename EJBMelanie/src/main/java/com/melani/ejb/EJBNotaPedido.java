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
    @EJB
    EJBEntradasSalidaDiariasRemote ejbentradasSalidas;        
    DatosNotaPedido notadepedido;    
    volatile Double totalCompras;    
    private DatosNotaPedido xstreamNotaPedido(String xmlNotapedido){
            XStream xestream = new XStream(new StaxDriver());
                xestream.alias("notapedido", DatosNotaPedido.class);
                xestream.alias("personas", DatosNotaPedido.Personas.class);
                xestream.alias("tarjetacredito", DatosNotaPedido.TarjetaCredito.class);
                xestream.alias("porcentajes", DatosNotaPedido.Porcentajes.class);
                xestream.alias("itemdetallesnota", Itemdetallesnota.class);
                xestream.alias("detallesnotapedido", DetallesNotaPedido.class);
                xestream.addImplicitCollection(DetallesNotaPedido.class, "list");
            return notadepedido = (DatosNotaPedido) xestream.fromXML(ProjectHelpers.parsearCaracteresEspecialesXML(xmlNotapedido));
    }    
    @Override
    public long agregarNotaPedido(String xmlNotaPedido) {
        long retorno;                    
            if(!xmlNotaPedido.isEmpty()){            
                retorno = almacenarNotaPedido(xstreamNotaPedido(xmlNotaPedido));            
            }else{
                retorno = -5;
            }       
            return retorno;        
    }
    private long almacenarNotaPedido(DatosNotaPedido datosNota){
        long retorno;    
            
                    Clientes cliente = em.find(Clientes.class, datosNota.getPersonas().getId());
                            Notadepedido notape = new Notadepedido();
                            almacenarNotaVoid(notape,datosNota);
                                
                                if(datosNota.getEnefectivoAnticipo()==1&&datosNota.getAnticipo()>0){
                                    System.out.println("SE A VERIFICAR ENTRADA CAJA EFE "+datosNota.getEnefectivo()+" "+datosNota.getAnticipo());
                                    verificarAnticipoParaInsertarComoEntradaCaja(datosNota,notape);
                                    
                                }else{
                                    if(datosNota.getNumerodecupon().length()>0){
                                        System.out.println("SE A VERIFICAR ENTRADA CAJA TARJETA "+datosNota.getEnefectivo()+" "+datosNota.getAnticipo());
                                        verificarNumeroDeCuponParaInsertarComoEntradaCaja(datosNota,notape);
                                    }
                                }
                                         switch(datosNota.getStockfuturo()){
                                             case 0:{            
                                                    almacenarDetalleNotaConControlStock(datosNota,notape);
                                                    almacenarHistorico(datosNota,notape);            
                                                    }
                                             break;
                                             default :
                                             {            
                                                almacenarDetalleNota(datosNota,notape);
                                                almacenarHistorico(datosNota,notape);            
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
                                                    cliente.setFechaCarga(GregorianCalendar.getInstance().getTime());                                           
                                                em.persist(cliente);        
                                                
                                                retorno = notape.getId();                                          
            return retorno;        
    }
    
    private long almacenarDetalleNota(DatosNotaPedido datosNota, Notadepedido notape) {
        long retorno;        
            List<Itemdetallesnota>lista = datosNota.getDetallesnotapedido().getDetallesnota();    
            System.out.println("TAMAÑO DEL ARRAY "+lista.size()+" "+notape.getId()+" ");
            
            lista.stream().forEach((itemdetallesnota) -> {
                Productos productos = em.find(Productos.class,itemdetallesnota.getId_producto());
                DetallesnotadepedidoPK detallespk = new DetallesnotadepedidoPK(notape.getId(), itemdetallesnota.getId_producto());
                Detallesnotadepedido detalles = new Detallesnotadepedido();
                almacenarDetallesNota(productos,itemdetallesnota,notape,detallespk,detalles);
        });
                
                    unirRelacion(notape);
                            retorno = notape.getId();        
            return retorno;        
    }
    
    private long almacenarDetalleNotaConControlStock(DatosNotaPedido notadepedido, Notadepedido notape) {
        long retorno;
        int stockDisponible;        
            List<Itemdetallesnota>lista = notadepedido.getDetallesnotapedido().getDetallesnota();
                    for (Itemdetallesnota itemdetallesnota : lista) {                        
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
        
                    GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());                    
                                    Historiconotapedido historico = new Historiconotapedido();                                    
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
                        Query queryFindByIdProducto = em.createNamedQuery("Historiconotapedido.findByFkIdNotaPedido");
                                queryFindByIdProducto.setParameter("idnota", notape.getId());
                                notape.setHistoriconotapedidoList(queryFindByIdProducto.getResultList());                   
                                em.persist(notape);                    
                    //resultado = historico.getIdhistorico();
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
    public long cancelarNotaPedido(String datosXML,int estado) {
        long result;
            DatosNotaPedido getDatosXML = xstreamNotaPedido(datosXML);
                              GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
                              SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                              Notadepedido nota = em.find(Notadepedido.class,getDatosXML.getIdnota());
                              
            if(nota.getCancelado()=='1'&&estado==1){
                return 0;
            }else{
                
                if(getDatosXML.getSaldo()==0){
                        Empleados empleado = em.find(Empleados.class, getDatosXML.getUsuario_cancelo_nota());
                                    if(estado==1){   
                                            if(getDatosXML.getEnefectivo()=='1'&&getDatosXML.getAnticipo()>0){
                                                verificarAnticipoParaInsertarComoEntradaCaja(getDatosXML, nota);
                                            }else{
                                                if(getDatosXML.getNumerodecupon().length()>0&&getDatosXML.getEnefectivo()==0){
                                                    verificarNumeroDeCuponParaInsertarComoEntradaCaja(notadepedido, nota);
                                                }
                                            }
                                        nota.setCancelado('1');
                                        nota.setIdusuariocancelo(getDatosXML.getUsuario_cancelo_nota());
                                        nota.setFecancelado(gc.getTime());
                                        nota.setUltimaActualizacion(em.find(Empleados.class, getDatosXML.getUsuario_cancelo_nota()).getNameuser()+" "+sdf.format(gc.getTime()));
                                    }else{
                                        nota.setCancelado('0');
                                        nota.setIdusuariocancelo(0L);
                                                try {
                                                    nota.setFecancelado(sdf.parse("01/01/1900"));
                                                } catch (ParseException ex) {
                                                    java.util.logging.Logger.getLogger(EJBNotaPedido.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                        nota.setUltimaActualizacion(em.find(Empleados.class, getDatosXML.getUsuario_cancelo_nota()).getNameuser()+" "+sdf.format(gc.getTime()));
                                    }
                                            List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();
                                            lista.stream().forEach((detallesnotadepedido) -> {
                                                detallesnotadepedido.setCancelado((char)estado);
                                            });
                                                            Historiconotapedido historico = new Historiconotapedido();
                                                                        if(estado==1){
                                                                            historico.setAccion("Cancelada por "+empleado.getNameuser());
                                                                            historico.setCancelado('1');
                                                                            historico.setIdusuariocancelo(getDatosXML.getUsuario_cancelo_nota());
                                                                        }else{
                                                                            historico.setAccion("No cancelada");
                                                                            historico.setCancelado('0');
                                                                            historico.setIdusuariocancelo(0L);
                                                                        }
                                                                       procesarHistorico(historico,gc,nota);
                                                                 em.persist(historico);
                                                            long notaID = procesaListNotaHistorico(nota);                                                            
                                                            result = notaID;      
                    }else{
                        result =-3;
                    }
            }
                
            return result;        
    }    
    private long procesaListNotaHistorico(Notadepedido nota) {        
                            Query queryFindHistorico = em.createNamedQuery("Historiconotapedido.findByFkIdNotaPedido");
                            queryFindHistorico.setParameter("idnota", nota.getId());                            
                            nota.setHistoriconotapedidoList(queryFindHistorico.getResultList());                                                  
                            em.persist(nota);                            
            return nota.getId();       
    }
    
    @Override
    public long entregarNotaPedido(String datosXML, int estado) {
        long result;
        char pendiente = '0';
        
        DatosNotaPedido datosXMLNota = xstreamNotaPedido(datosXML);
                GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                SimpleDateFormat sdfH = new SimpleDateFormat("dd/MM/yyyy");
                Notadepedido nota = em.find(Notadepedido.class, datosXMLNota.getIdnota());
                        if(nota.getEntregado()=='1'&& estado==1){
                            return 0;
                        }else{
                            Empleados empleado = em.find(Empleados.class, datosXMLNota.getUsuario_entregado());
                                    if(estado==1){                                        
                                        nota.setEntregado('1');
                                        nota.setFechaentrega(gc.getTime());
                                        nota.setPendiente(pendiente);
                                        
                                        nota.setIdusuarioEntregado(datosXMLNota.getUsuario_entregado());
                                        nota.setUltimaActualizacion(em.find(Empleados.class, datosXMLNota.getUsuario_entregado())+" "+sdf.format(gc.getTime()));
                                    }else{
                                            try {
                                                pendiente ='1';
                                                nota.setEntregado('0');
                                                nota.setFechaentrega(sdfH.parse(ResourceBundle.getBundle("config").getString("FECHA_DEFAULT")));
                                                nota.setPendiente(pendiente);
                                                nota.setIdusuarioEntregado(0L);
                                                nota.setUltimaActualizacion(em.find(Empleados.class, datosXMLNota.getUsuario_entregado())+" "+sdf.format(gc.getTime()));
                                            } catch (ParseException ex) {
                                                java.util.logging.Logger.getLogger(EJBNotaPedido.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                    }
                                    List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();
                                for (Detallesnotadepedido detallesnotadepedido : lista) {
                                    detallesnotadepedido.setEntregado((char)estado);
                                    detallesnotadepedido.setPendiente(pendiente);
                                }
                                em.persist(nota);            
                                    Historiconotapedido historico = new Historiconotapedido();
                                            if(estado==1){
                                                    historico.setAccion("Entregado por "+empleado.getNameuser());
                                                    historico.setEntregado('1');
                                                    historico.setPendiente('0');                                             
                                            }else{
                                                    historico.setAccion("No entregada");
                                                    historico.setEntregado('0');
                                                    historico.setPendiente('1');                                                       
                                            }                        
                                            procesarHistorico(historico,gc,nota);                                        
                                            em.persist(historico);
                                                long notaID = procesaListNotaHistorico(nota);
                                           result = notaID;
                        }
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
            Query queryFindNota =em.createQuery("Select n From Notadepedido n ORDER BY n.id DESC, "
                    + "n.fechadecompra DESC,n.fkIdcliente.idPersona",Notadepedido.class);            
            result = queryFindNota.getResultList();            
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
        String result = "<Lista>\n";
        Query queryFindNota = em.createNamedQuery("Notadepedido.searchAllOrderDesc",Notadepedido.class);
             queryFindNota.setMaxResults(recordCount);
             queryFindNota.setFirstResult(index*recordCount);
            List<Notadepedido>lista = queryFindNota.getResultList();            
            if(!lista.isEmpty()) {
                StringBuilder processNote = new StringBuilder(10);
                lista.stream().forEach((notape) -> {
                    processNote.append(devolverNotaProcesadaSB(notape));
                });
                result+=processNote;
            } else{            
                result+="LA CONSULTA NO ARROJÓ RESULTADOS!!!";
            }
            return result+="</Lista>";        
    }
    @Override
    public long anularNotaPedido(String datosXML, int estado) {
        long result;
            DatosNotaPedido datosNotaXML = xstreamNotaPedido(datosXML);
                GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");           
                Notadepedido nota = em.find(Notadepedido.class,datosNotaXML.getIdnota());
                if(nota.getAnulado()=='1'&&estado==1){
                    return 0;
                }else{
                Empleados empleado = em.find(Empleados.class,datosNotaXML.getId_usuario_anulado());
                        if(estado==1){        
                            nota.setFechaAnulado(gc.getTime());
                            nota.setIdusuarioAnulado(datosNotaXML.getId_usuario_anulado());
                            nota.setAnulado('1');
                            nota.setUltimaActualizacion(em.find(Empleados.class, datosNotaXML.getId_usuario_anulado()).getNameuser()+" "+sdf.format(gc.getTime()));
                        }else{
                             nota.setAnulado('0');
                                    try {
                                        nota.setFechaAnulado(sdf.parse("01/01/1900"));
                                    } catch (ParseException ex) {
                                        java.util.logging.Logger.getLogger(EJBNotaPedido.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                             nota.setIdusuarioAnulado(0L);
                             nota.setUltimaActualizacion(em.find(Empleados.class, datosNotaXML.getId_usuario_anulado()).getNameuser()+" "+sdf.format(gc.getTime()));
                        }
                List<Detallesnotadepedido>lista = nota.getDetallesnotadepedidoList();
                lista.stream().forEach((detallesnotadepedido) -> {
                    detallesnotadepedido.setAnulado((char)estado);
            });
                Historiconotapedido historico = new Historiconotapedido();
                        if(estado==1){
                                historico.setAccion("Nota anulada "+empleado.getNameuser());
                                historico.setAnulado('1');
                                historico.setIdusuarioanulo(datosNotaXML.getId_usuario_anulado());
                        }else{
                                historico.setAccion("Nota no anulada");
                                historico.setAnulado('0');
                                historico.setIdusuarioanulo(0L);
                        }
                                procesarHistorico(historico,gc,nota);                                
                                em.persist(historico);               
                            long notaID = procesaListNotaHistorico(nota);                            
                       result = notaID;   
                }
            return result;        
    }

    @Override
    public long actualizarNotaPedido(String xmlnotapedidomodificada) {
        DatosNotaPedido datosnotapedido;
        long retorno;       
            if(!xmlnotapedidomodificada.isEmpty()){
                datosnotapedido=xstreamNotaPedido(xmlnotapedidomodificada);
                if(datosnotapedido.getIdnota()>0){
                    Notadepedido nota = em.find(Notadepedido.class, datosnotapedido.getIdnota());
                    
                    if(nota.getAnulado()==1){
                        retorno =-10;
                    }else{
                        if(nota.getEntregado()==1){
                            retorno =-11;
                        }else{
                            if(nota.getCancelado()==1){
                                retorno = -12;
                            
                            }else{
                                if((datosnotapedido.getEntregado()==1&&datosnotapedido.getCancelado()==1)&&nota.getCancelado()=='0'){
                                    retorno = -13;
                                }else{
                                    
                                    retorno = procesarNotaaActualizar(datosnotapedido,nota);
                                }
                            }
                            
                        }
                    }
                    
                            
                }else {
                    retorno =-2;
                }
            }else{
                retorno = -5;
            }       
            return retorno;        
}
    private long procesarNotaaActualizar(DatosNotaPedido datosnotapedido, Notadepedido nota) {
        long result;       
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
                                                        nota.setAnticipo(BigDecimal.valueOf(datosnotapedido.getAnticipo()));                                                        
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
                                                   procesarControlCaja(datosnotapedido,nota);
                                                   
                                                List<Itemdetallesnota>lista = datosnotapedido.getDetallesnotapedido().getDetallesnota();
                                                Query deletsql = em.createNamedQuery("Detallesnotadepedido.deleteById");
                                                deletsql.setParameter("idNota", nota.getId());
                                                int retorno = deletsql.executeUpdate();                                                
                                                Query queryDetailsOrders1 = em.createNamedQuery("Detallesnotadepedido.findByFkIdnota");
                                                queryDetailsOrders1.setParameter("fkIdnota", nota.getId());                                                
                                                 Productos productos;
                                                 Itemdetallesnota itemdetallesnota;
                                                    for (Iterator<Itemdetallesnota> it = lista.iterator(); it.hasNext();) {
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
                    Query jpasql = em.createQuery("SELECT n FROM Notadepedido n WHERE n.fechaentrega BETWEEN ?1 AND ?2 AND n.entregado = "
                            + "?3 AND n.pendiente = ?4 ORDER BY n.fechadecompra,n.horacompra,n.fkIdcliente.apellido DESC ",Notadepedido.class);
                    //Query jpasql = em.createQuery("SELECT n FROM Notadepedido n WHERE n.fechaentrega "
                      //      + "BETWEEN ?1 AND ?2 AND n.entregado = ?3 and n.pendiente = ?4 ORDER BY n.horacompra,n. desc",Notadepedido.class);
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
                   Query queryFindNota=em.createQuery("SELECT n FROM Notadepedido n WHERE n.id = :id");
            queryFindNota.setParameter("id", idnota);            
            if(!queryFindNota.getResultList().isEmpty()){
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
        String xml = "";
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
                                                                String periodo = 
                                                                        "<fechainicio>" + fecha1 + "</fechainicio>\n" + 
                                                                        "<fechafinal>" + fecha2 + "</fechafinal>\n";                                                                
                                                                 sb.replace(sb.indexOf("</numerocupon>")+14, sb.indexOf("<observaciones>")
                                                                         , "\n"+periodo); 
                                                                      
      return sb.toString();
    }

    private void almacenarDetallesNota(Productos productos, Itemdetallesnota itemdetallesnota, Notadepedido notape, DetallesnotadepedidoPK detallespk, Detallesnotadepedido detalles) {
                                detalles.setAnulado(itemdetallesnota.getAnulado());
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

    private void procesarHistorico(Historiconotapedido historico, GregorianCalendar gc, Notadepedido nota) {
        SimpleDateFormat sdfH = new SimpleDateFormat("hh:mm:ss");
                                        historico.setAnticipo(nota.getAnticipo());                                
                                        historico.setFecharegistro(gc.getTime());
                                        historico.setFkidnotapedido(nota);
                                             try {
                                                historico.setHoraregistro(sdfH.parse(sdfH.format(gc.getTime())));
                                             } catch (ParseException ex){
                                                java.util.logging.Logger.getLogger(EJBNotaPedido.class.getName()).log(Level.SEVERE, null, ex);
                                             }
                                        historico.setPendiente(nota.getPendiente());
                                        historico.setEntregado(nota.getEntregado());
                                        historico.setIdusuarioanulo(nota.getIdusuarioAnulado());
                                        historico.setIdusuarioentrega(nota.getIdusuarioEntregado());
                                        historico.setIdusuarioexpidio(nota.getIdUsuarioExpidioNota());
                                        historico.setObservaciones(nota.getObservaciones());
                                        historico.setPorcentajeaplicado(Short.valueOf("0"));
                                        historico.setSaldo(nota.getSaldo());
                                        historico.setTotal(nota.getTotal());
                                        historico.setTotalapagar(nota.getMontototalapagar());
                                        historico.setRecargo(nota.getPorcrecargo());
                                        historico.setPorcrecargo(BigDecimal.ZERO);
                                        historico.setPorcentajedesc(nota.getPorcdesctotal());
                                        historico.setDescuento(nota.getDescuentoNota());
                                        historico.setAnulado(nota.getAnulado());
    }

    private void almacenarNotaVoid(Notadepedido notape, DatosNotaPedido datosNotaPedido) {
        GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                               
                                notape.setAnulado(datosNotaPedido.getAnulado());                                
                                notape.setEnefectivo(datosNotaPedido.getEnefectivo());
                                notape.setPendiente(datosNotaPedido.getPendiente());
                                notape.setEntregado(datosNotaPedido.getEntregado());
                                notape.setFkIdcliente(em.find(Personas.class, datosNotaPedido.getPersonas().getId()));
                                notape.setFkidporcentajenotaId(em.find(Porcentajes.class, datosNotaPedido.getPorcentajes().getId_porcentaje()));
                                notape.setIdTarjetaFk(em.find(TarjetasCreditoDebito.class, datosNotaPedido.getTarjetacredito().getId_tarjeta()));
                                notape.setIdUsuarioExpidioNota(datosNotaPedido.getUsuario_expidio_nota());
                                notape.setIdusuarioAnulado(datosNotaPedido.getId_usuario_anulado());
                                notape.setIdusuarioEntregado(datosNotaPedido.getUsuario_entregado());
                                notape.setMontoiva(BigDecimal.valueOf(datosNotaPedido.getMontoiva()));
                                notape.setNumerodecupon(datosNotaPedido.getNumerodecupon());
                                notape.setAnticipo(BigDecimal.valueOf(datosNotaPedido.getAnticipo()));
                                if(datosNotaPedido.getObservaciones().length()>0){
                                    notape.setObservaciones(datosNotaPedido.getObservaciones());
                                }else{
                                    notape.setObservaciones("");
                                }
                                notape.setRecargo(BigDecimal.valueOf(datosNotaPedido.getRecargo()));
                                notape.setSaldo(BigDecimal.valueOf(datosNotaPedido.getSaldo()));
                                notape.setStockfuturo(datosNotaPedido.getStockfuturo());
                                notape.setTotal(BigDecimal.valueOf(datosNotaPedido.getMontototal()));                              
                                notape.setHoracompra(gc.getTime());                              
                                notape.setFechadecompra(gc.getTime());                              
                                    try {
                                        notape.setFechaentrega(sdf.parse(datosNotaPedido.getFechaentrega()));
                                    } catch (ParseException e1) {
                                        java.util.logging.Logger.getLogger(EJBNotaPedido.class.getName()).log(Level.SEVERE, null, e1);
                                    }                              
                                notape.setCancelado(datosNotaPedido.getCancelado());
                                notape.setDescuentonota(BigDecimal.valueOf(datosNotaPedido.getDescuentonota()));                                
                                notape.setDescuentoPesos(BigDecimal.valueOf(datosNotaPedido.getDescuentopesos()));                              
                                notape.setUltimaActualizacion(ResourceBundle.getBundle("config").getString("FECHA_DEFAULT"));
                                notape.setIdusuariocancelo(datosNotaPedido.getUsuario_cancelo_nota());
                                notape.setMontototalapagar(BigDecimal.valueOf(datosNotaPedido.getMontototalapagar()));
                                notape.setPorcdesctotal(BigDecimal.valueOf(datosNotaPedido.getPorc_descuento_total()));
                                notape.setPorcrecargo(BigDecimal.valueOf(datosNotaPedido.getPorcentajerecargo()));                                
                                if(datosNotaPedido.getCancelado()=='1') {
                                    notape.setFecancelado(gc.getTime());
                                }else{
                                        try {
                                            notape.setFecancelado(sdf.parse(ResourceBundle.getBundle("config").getString("FECHA_DEFAULT")));
                                        } catch (ParseException e) {
                                            java.util.logging.Logger.getLogger(EJBNotaPedido.class.getName()).log(Level.SEVERE, null, e);
                                        }
                                }
                                if(datosNotaPedido.getAnulado()=='1') {
                                    notape.setFechaAnulado(gc.getTime());
                                }else{
                                        try {
                                            notape.setFechaAnulado(sdf.parse(ResourceBundle.getBundle("config").getString("FECHA_DEFAULT")));
                                        } catch (ParseException e2) {
                                            java.util.logging.Logger.getLogger(EJBNotaPedido.class.getName()).log(Level.SEVERE, null, e2);
                                        }
                                }
                                if(datosNotaPedido.getEntregado()==1){
                                    notape.setFechaentrega(gc.getTime());
                                }else{
                                        try {
                                            notape.setFechaentrega(sdf.parse(datosNotaPedido.getFechaentrega()));
                                        } catch (ParseException e3) {
                                            java.util.logging.Logger.getLogger(EJBNotaPedido.class.getName()).log(Level.SEVERE, null, e3);
                                        }
                                }
                                em.persist(notape);
    }
    private String iterateOverListNote(List<Notadepedido> lista) {
        StringBuilder xmlLoop = new StringBuilder(10);
        lista.stream().forEach((notadepedido1) -> {
            xmlLoop.append(notadepedido1.toXML());
        });
        return xmlLoop.toString();
    }    
    
     private void verificarNumeroDeCuponParaInsertarComoEntradaCaja(DatosNotaPedido datosnotapedido,Notadepedido notape){ 
         if(notape.getEntregado()=='1'||notape.getCancelado()=='1'){
                   ejbentradasSalidas.calculosPorNumerodeCupon(datosnotapedido.getMontototal(),notape);
         }else{
                if(datosnotapedido.getAnticipo()>0&&datosnotapedido.getEnefectivo()=='1'){
                    ejbentradasSalidas.calculosPorAnticipoNotaPedido(datosnotapedido.getAnticipo(), notape);
                }else{
                    if(datosnotapedido.getAnticipo()>0&&datosnotapedido.getEnefectivo()=='0'){
                        ejbentradasSalidas.calculosPorNumerodeCupon(datosnotapedido.getAnticipo(), notape);
                    }
                }
         }
    }

    private void verificarAnticipoParaInsertarComoEntradaCaja(DatosNotaPedido datos,Notadepedido notape) {
            System.out.println("verifico la entrada");
                ejbentradasSalidas.calculosPorAnticipoNotaPedido(datos.getAnticipo(),notape);
        
    }
    private void procesarControlCaja(DatosNotaPedido datosnotapedido, Notadepedido nota) {
        System.out.println("ENTRANDO A PROCESAR CONTROL CAJA");
        
                if(datosnotapedido.getEntregado()=='1'&&datosnotapedido.getCancelado()=='1'&&datosnotapedido.getEnefectivo()==1){
                        
                        System.out.println("ME FUI POR EL EFECTIVO");
                        verificarAnticipoParaInsertarComoEntradaCaja(datosnotapedido,nota);
                }else{
                       if(datosnotapedido.getEnefectivo()==1){
                           System.out.println("ME FUI POR LAS TARJETAS");
                           verificarNumeroDeCuponParaInsertarComoEntradaCaja(datosnotapedido,nota);
                       }else{
                           System.out.println("ME FUI POR LAS ENTRADAS");
                           if(datosnotapedido.getNumerodecupon().length()>0){
                                    verificarAnticipoParaInsertarComoEntradaCaja(datosnotapedido,nota);
                           }
                       } 
                }
        }
    
}