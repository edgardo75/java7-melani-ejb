/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Detallespresupuesto;
import com.melani.entity.DetallespresupuestoPK;
import com.melani.entity.Presupuestos;
import com.melani.entity.Productos;
import com.melani.utils.DatosPresupuestos;
import com.melani.utils.DetallesPresupuesto;
import com.melani.utils.ItemDetallesPresupuesto;
import com.melani.utils.ProjectHelpers;
import com.thoughtworks.xstream.XStream;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;


/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBPresupuestosBean")
@WebService(serviceName="ServicesPresupuestos",name="PresupuestoWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBPresupuestosBean implements EJBPresupuestosRemote {
    private static final Logger logger = Logger.getLogger(EJBPresupuestosBean.class);
    @PersistenceContext
    private EntityManager em;  

    /**
     *
     * @param xmlPresupuesto
     * @return
     */
    @Override
    @SuppressWarnings("FinallyDiscardsException")
    public long addBudgets(String xmlPresupuesto) {
        long retorno =0L;
        try {
            XStream xstream = new XStream();
                xstream.alias("presupuesto", DatosPresupuestos.class);
                xstream.alias("detallepresupuesto", DetallesPresupuesto.class);
                xstream.alias("itemdetallespresupuesto", ItemDetallesPresupuesto.class);
                xstream.addImplicitCollection(DetallesPresupuesto.class, "lista");
            DatosPresupuestos datospresupuesto = (DatosPresupuestos) xstream.fromXML(ProjectHelpers.parsearCaracteresEspecialesXML(xmlPresupuesto));
            retorno = almacenarPresupuesto(datospresupuesto);
        }catch(NullPointerException npe)    {
            retorno =-4;
            logger.error("Error en metodo addBuget en ejbpresupuestos "+npe.getMessage());
        }catch(com.thoughtworks.xstream.io.StreamException ex)    {
            retorno =-3;
            logger.error("Error en el armado del xml en metodo addBudgets "+ex.getMessage());
        } catch (Exception e) {
            retorno =-1;
            logger.error("Error en metodo addBudgets en EJBPresupuestosBean "+e.getMessage());
        }finally{
            return retorno;
        }
    }
    private long almacenarPresupuesto(DatosPresupuestos datospresupuesto) {
        long result = 0L;
        try {
            GregorianCalendar gc = new GregorianCalendar();
            GregorianCalendar fvalidez = new GregorianCalendar();
            fvalidez.add(GregorianCalendar.DATE,20);
                    Presupuestos presupuesto = new Presupuestos();
                            presupuesto.setTotalapagar(BigDecimal.valueOf(datospresupuesto.getTotalapagar()));
                            presupuesto.setFechapresupuesto(gc.getTime());
                            presupuesto.setApellido(datospresupuesto.getApellido().toUpperCase());
                            presupuesto.setNombre(datospresupuesto.getNombre().toUpperCase());
                            presupuesto.setIva(BigDecimal.valueOf(datospresupuesto.getIva()));
                            presupuesto.setValidez(fvalidez.getTime());
                            presupuesto.setPorcetajedescuentoTOTAL(BigDecimal.valueOf(datospresupuesto.getPorc_descuento_total()));
                            presupuesto.setIdUsuarioFk(datospresupuesto.getId_usuario_expidio());
                            presupuesto.setObservaciones(datospresupuesto.getObservaciones());
                            presupuesto.setTotal(BigDecimal.valueOf(datospresupuesto.getTotal()));
                            presupuesto.setRecargototal(BigDecimal.valueOf(datospresupuesto.getRecargototal()));
                            presupuesto.setPorcentajerecargo(BigDecimal.valueOf(datospresupuesto.getPorcentajerecargo()));
                            presupuesto.setDescuentoresto(BigDecimal.valueOf(datospresupuesto.getDescuentoresto()));
                            presupuesto.setPorcetajedescuentoTOTAL(BigDecimal.valueOf(datospresupuesto.getPorc_descuento_total()));
                            em.persist(presupuesto);
                            
            //++++++++++++++++++++++++++Alamcenar Detalle del Presupuesto+++++++++++++++++++++++++++
            //**************************************************************************************
                List<ItemDetallesPresupuesto>lista = datospresupuesto.getDetallesPresupuesto().getLista();
            for (ItemDetallesPresupuesto itemDetallesPresupuesto : lista) {
                        Productos producto = em.find(Productos.class, Long.valueOf(itemDetallesPresupuesto.getFk_id_producto()));
                        DetallespresupuestoPK detallePresupuestoPK = new DetallespresupuestoPK(presupuesto.getIdPresupuesto(), 
                                itemDetallesPresupuesto.getFk_id_producto());
                        Detallespresupuesto detallePresupuesto = new Detallespresupuesto(detallePresupuestoPK);
                        detallePresupuesto.setCantidad(itemDetallesPresupuesto.getCantidad());
                        detallePresupuesto.setDescuento(BigDecimal.valueOf(itemDetallesPresupuesto.getDescuento()));
                        detallePresupuesto.setPrecioDesc(BigDecimal.valueOf(itemDetallesPresupuesto.getPrecio_desc()));
                        detallePresupuesto.setPrecio(BigDecimal.valueOf(itemDetallesPresupuesto.getPrecio()));
                        detallePresupuesto.setDetallespresupuestoPK(detallePresupuestoPK);
                        detallePresupuesto.setPresupuestos(em.find(Presupuestos.class, presupuesto.getIdPresupuesto()));
                        detallePresupuesto.setProductos(producto);
                        detallePresupuesto.setSubtotal(BigDecimal.valueOf(itemDetallesPresupuesto.getSubtotal()));
                     em.persist(detallePresupuesto);
                            Query consultaListProdPres = em.createQuery("SELECT d FROM Detallespresupuesto d WHERE "
                                    + "d.detallespresupuestoPK.fkProducto = :fkProducto");
                            consultaListProdPres.setParameter("fkProducto", itemDetallesPresupuesto.getFk_id_producto());
                            producto.setDetallepresupuestosList(consultaListProdPres.getResultList());
            }
                                Query consulta1 = em.createQuery("SELECT d FROM Detallespresupuesto "
                                        + "d WHERE d.detallespresupuestoPK.idDpFk = :idDpFk");
                                consulta1.setParameter("idDpFk", presupuesto.getIdPresupuesto());
                                presupuesto.setDetallepresupuestosList(consulta1.getResultList());
                                em.merge(presupuesto);
                        result=Long.valueOf(presupuesto.getIdPresupuesto());
        } catch (Exception e) {
            result=-2;
            logger.error("Error en método almacenarPresupuesto EJBPresupuesto "+e.getMessage());
        }finally{
            
            return result;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String selectAllPresupuestosJPA() {
        StringBuilder xmlpresupuesto = new StringBuilder(4);
                xmlpresupuesto.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n").append("<Lista>\n");
        try {
            Query consulta  = em.createNamedQuery("Presupuesto.findPresupuestoOrderByFechaIdPresupesto");
            List<Presupuestos>lista = consulta.getResultList();
            if(lista.isEmpty()) {
                xmlpresupuesto.append("LA CONSULTA NO ARROJÓ RESULTADOS!!!");
            } else{
                for (Presupuestos presupuestos : lista) {
                    xmlpresupuesto.append(presupuestos.toXML());
                }
            }
        } catch (Exception e) {
            
            logger.error("Error en metodo selectAllPresupuestoJPA "+e.getMessage());
        }finally{
            xmlpresupuesto.append("</Lista>\n");                    
            return xmlpresupuesto.toString();
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Integer getRecordCount() {
        int retorno =0;
        try {
            Query presupuesto = em.createNamedQuery("Presupuestos.findAll");
            retorno =presupuesto.getResultList().size();
        } catch (Exception e) {
            retorno=-1;
            logger.error("Error en getRecordCount en EJBPresupuestosBean ");
        }finally{
            
            return retorno;
        }
    }

    /**
     *
     * @return
     */
//    @Override
//    public String selectPresupuestoOfTheDay(){
//        String presupuesto="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
//                + "<Lista>\n";
//        try {
//            Query consulta = em.createQuery("SELECT p FROM Presupuestos p WHERE p.fechapresupuesto = CURRENT_DATE");            
//            List<Presupuestos>lista = consulta.getResultList();
//            if(lista.isEmpty()) {
//                presupuesto+="LA CONSULTA NO ARROJÓ RESULTADOS!!!";
//            } else{
//                for (Presupuestos presupuestoss : lista) {
//                    presupuesto+=presupuestoss.toXML();
//                }
//            }
//            presupuesto+="</Lista>\n";
//        } catch (Exception e) {
//            presupuesto="Error";
//            logger.error("Error en selectPresupuestoPaging en EJBPresupuestosBean");
//        }finally{
//            
//         return presupuesto;
//        }
//    }

    /**
     *
     * @param index
     * @param record
     * @return
     */
    @Override
    public String verPresupuestosPaginados(Integer index, Integer record) {
        String result ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<Lista>\n";
        try {           
            Query paging = em.createQuery("Select p From Presupuestos p Order by p.fechapresupuesto desc,p.idPresupuesto desc",Presupuestos.class);
            paging.setMaxResults(record);
            paging.setFirstResult(index*record);
            
            List<Presupuestos>lista = paging.getResultList();
            
            if(lista.isEmpty()) {
                result+="LA CONSULTA NO ARROJÓ RESULTADOS!!!";
            } else{
                
                for (Presupuestos presupuestos : lista) {
                  
                    result+=presupuestos.toXML();
                }
                
            }
            
        } catch (Exception e) {
            result="Error";
            logger.error("Error en metodo verPresupuestos en EBPresupuestosBean ");
        }finally{
            result +="</Lista>\n";
            
            return result;
        }
    }

    /**
     *
     * @param idpresupuesto
     * @return
     */
    @Override
    public String searchOneBudget(int idpresupuesto) {
        StringBuilder result = new StringBuilder(32);
                result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<Lista>\n");
        try {
            Query paging =em.createNamedQuery("Presupuesto.findIdPresupuestoOrderByFechaIdPresupesto");            
            paging.setParameter("1", idpresupuesto);
            List<Presupuestos>lista = paging.getResultList();
            if(lista.isEmpty()) {
                result.append("LA CONSULTA NO ARROJÓ RESULTADOS!!!");
            } else{
                for (Presupuestos presupuestos : lista) {
                    result.append(presupuestos.toXML());
                }
                
            }
        } catch (Exception e) {
            
            logger.error("Error en metodo verPresupuestos en EBPresupuestosBean ");
        }finally{
            result.append("</Lista>\n");
            
            return result.toString();
        }
    }
    //-----------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------

//    /**
//     *
//     * @param xmlPresupuesto
//     * @return
//     */
//    protected StringBuilder parsearCaracteresEspecialesXML(String xmlPresupuesto){
//    String xml = "No paso Nada";
//    StringBuilder sb = null;
//    try {
//        sb = new StringBuilder(xmlPresupuesto);
//        
//        
//            xml=StringEscapeUtils.escapeXml10(xmlPresupuesto.substring(xmlPresupuesto.indexOf("es>")+3,xmlPresupuesto.indexOf("</ob")));
//            
//            sb.replace(sb.indexOf("es>")+3, sb.indexOf("</ob"), xml);
//        
//    } catch (Exception e) {
//        xml = "Error";
//        logger.error("Error en metodo parsearCaracteresEspecialesXML "+e.getLocalizedMessage());
//    }finally{
//    return sb;
//    }
//}

    /**
     *
     * @param idPresupuesto
     * @return
     */
    @Override
    public String ShowReportPresupuesto(Integer idPresupuesto) {
        StringBuilder xml = new StringBuilder(50);
                xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<Lista>\n");      
        try {

            Query consulta = em.createQuery("SELECT p FROM Presupuestos p WHERE p.idPresupuesto = :idPresupuesto");
            consulta.setParameter("idPresupuesto", idPresupuesto);
            
            List<Presupuestos>lista = consulta.getResultList();
            if(!lista.isEmpty()){
                    for (Presupuestos presupuestos : lista) {
                        xml.append(presupuestos.toXML());
                    }        
            }else {
                xml.append("<result>La consulta no arrojó resultados</result>");
            }
            
        } catch (Exception e) {
            logger.error("Error en metodo ShowReportPresupuesto ");
        }finally{
            
        return xml.append("</Lista>\n").toString();
        }
    }  

    /**
     *
     * @param first
     * @param last
     * @return
     */
    public String ShowReportVerPresupuesto(Long first, Long last) {
        
        
        StringBuilder xml = new StringBuilder(4);
                xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<Lista>\n");
        try {
            long uno=first;
            long dos=last;
            String sql =   "Select p FROM Presupuestos p Where p.idPresupuesto BETWEEN :uno and :dos order by p.fechapresupuesto desc ,p.idPresupuesto desc";
            
             //----------------------------------------------------------------------------
            Query consulta = em.createQuery(sql);
            consulta.setParameter("uno", uno);
            consulta.setParameter("dos", dos);
            List<Presupuestos>lista = consulta.getResultList();
           
            if(!lista.isEmpty()){
                for (Presupuestos presupuestos : lista) {
                    xml.append(presupuestos.toXML());
                }
            }else {
                xml.append("<result>La consulta no arrojó resultados</result>");
            }
           
        } catch (Exception e) {
            logger.error("Error en metodo ShowReportVerPresupuesto ");
            xml.append("<result>Error en metodo ShowReportVerPresupuesto \n").append(e.getLocalizedMessage()).append("</result>");
        }finally{            
            return xml.append("</Lista>").toString();
        }
    }
}
