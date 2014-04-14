/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Barrios;
import com.melani.entity.Calles;
import com.melani.entity.Domicilios;
import com.melani.entity.Localidades;
import com.melani.entity.Orientacion;
import com.melani.utils.DatosDomicilios;
import com.thoughtworks.xstream.XStream;
import java.util.List;
import java.util.Locale;
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
@Stateless(name="ejb/EJBDomicilios")
@WebService(serviceName="ServicesDomicilios",name="DomiciliosWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBDomicilios implements EJBDomiciliosRemote {
    private static final Logger logger = Logger.getLogger(EJBDomicilios.class);
    @PersistenceContext
    private EntityManager em; 
    private long idDomicilio=0;    
    
    /**
     * 
     * @param datosDomici objeto que representa información del domicilio de la persona
     * @return el método devuelve un numero con el resultado de la operacion si es negativo no se realizó con éxito la operación.
     */
    @Override
 public long addDomicilios(DatosDomicilios datosDomici) {
        long retorno =0;
        try {
            //------------------------------------------------------------------
           //------------------------------------------------------------------
             idDomicilio= existe(datosDomici);
            switch((int)idDomicilio){
                    case 0:{                    
                            retorno = procesarAddDomicilio(datosDomici);                            
                            
                        break;
                    }                   
                    case -1:{
                           retorno=-1;
                    break;
                    }
                default:{                   
                        retorno = actualizarDomicilio(datosDomici,idDomicilio);  
                        
                    break;
                }
            }
        } catch (Exception e) {
            retorno =-2;
            logger.error("Error en Metodo addDomicilios "+e);
        }finally{
            return retorno;
        }
    }
    //------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------
 
 /**
  * 
  * @param domiciXML objeto que representa información del domicilio de la persona
  * @return el método devuelve un numero con el resultado de la operacion si es negativo no se realizó con éxito la operación.
  */
 
    private long procesarAddDomicilio(DatosDomicilios domiciXML) {
        long retorno = 0;
        Barrios barrios = null;
        Calles calles = null;
        Orientacion orientacion = null;        
        try {          
                Domicilios domicilioss = new Domicilios();          
                domicilioss.setPiso(domiciXML.getPiso());
                
            if (domiciXML.getEntrecalleycalle().length()>0) {
                domicilioss.setEntrecalleycalle(domiciXML.getEntrecalleycalle().toUpperCase(Locale.getDefault()));
                } else {
                domicilioss.setEntrecalleycalle("NO INGRESADO");
                }
            
                            domicilioss.setSector(domiciXML.getSector().toUpperCase(Locale.getDefault()));
                            domicilioss.setMonoblock(domiciXML.getMonoblock().toUpperCase(Locale.getDefault()));          
                            barrios = em.find(Barrios.class, domiciXML.getBarrio().getBarrioId());
                            calles = em.find(Calles.class, domiciXML.getCalle().getCalleId());
                            orientacion = em.find(Orientacion.class, domiciXML.getOrientacion().getOrientacion());       
                            domicilioss.setLocalidades(em.find(Localidades.class, domiciXML.getLocalidad().getIdLocalidad()));
                            domicilioss.setBarrios(barrios);
                            domicilioss.setCalles(calles);
                            domicilioss.setOrientacion(orientacion);
                            domicilioss.setNumero(domiciXML.getNumero());              
                            domicilioss.setNumdepto(domiciXML.getNumDepto());              
                            domicilioss.setArea(domiciXML.getArea().toUpperCase(Locale.getDefault()));
                            domicilioss.setManzana(domiciXML.getManzana().toUpperCase(Locale.getDefault()));
                            domicilioss.setPiso(domiciXML.getPiso().toUpperCase(Locale.getDefault()));
                            domicilioss.setTorre(domiciXML.getTorre().toUpperCase(Locale.getDefault()));
                            
                            
                    if(domiciXML.getObservaciones().length()>0){
                        domicilioss.setObservaciones(domiciXML.getObservaciones().toUpperCase(Locale.getDefault()));
                    }else{
                        domicilioss.setObservaciones("NO INGRESADO");
                    }
            em.persist(domicilioss);
            em.flush();
          retorno = domicilioss.getId();
        } catch (Exception xst) {
            logger.error("Error en metodo procesarAddDomicilio  "+xst.getLocalizedMessage());
            retorno = -2;
        }  finally {    
            
            return retorno;
        }
    }
/**
 * 
 * @param domiciXML objeto que representa información del domicilio de la persona
 * @return un valor si el domicilio existe si es cero no existe el domicilio, si es un numero entero si existe y finalmente si es negativo el numero
 * hubo un error al ejecutar el algoritmo.
 */
    private long existe(DatosDomicilios domiciXML) {
        long retorno =0;  
        String entrecalle="";
        String manzana,piso="";
        int numeroDomicilio,numdepto =0;
        long barrioN,calleN = 0;
        String area,torre,sector,monoblock="";
        long orientacion,localidadN=0L;
        try {
            entrecalle=domiciXML.getEntrecalleycalle();
            manzana=domiciXML.getManzana();
            numeroDomicilio=domiciXML.getNumero();
            area=domiciXML.getArea();
            torre=domiciXML.getTorre();
            piso=domiciXML.getPiso();
            sector=domiciXML.getSector();
            monoblock=domiciXML.getMonoblock();
            numdepto=domiciXML.getNumDepto();
            barrioN=domiciXML.getBarrio().getBarrioId();
            calleN=domiciXML.getCalle().getCalleId();
            orientacion=domiciXML.getOrientacion().getOrientacion();
            localidadN=domiciXML.getLocalidad().getIdLocalidad();
            

            
            
            Query consulta = em.createQuery("SELECT d FROM Domicilios d WHERE d.entrecalleycalle like ?1 and d.manzana like ?2 and d.numero = ?3 and d.area like ?4 and d.torre like ?5 and d.piso like ?6 and d.sector like ?7 and d.monoblock like ?8 and d.numdepto = ?9 and d.idbarrio.id = ?10 and d.idcalle.id = ?11 and d.idorientacion.id = ?12 and d.localidades.idLocalidad = ?13",Domicilios.class);
                    consulta.setParameter("1", entrecalle);
                    consulta.setParameter("2", manzana);
                    consulta.setParameter("3", numeroDomicilio);
                    consulta.setParameter("4", area);
                    consulta.setParameter("5", torre);
                    consulta.setParameter("6", piso);
                    consulta.setParameter("7", sector);
                    consulta.setParameter("8", monoblock);
                    consulta.setParameter("9", numdepto);
                    consulta.setParameter("10", barrioN);
                    consulta.setParameter("11", calleN);
                    consulta.setParameter("12", orientacion);
                    consulta.setParameter("13", localidadN);            
            if(consulta.getResultList().isEmpty()) {
                retorno =0;
            } else{
                List<Domicilios>lista = consulta.getResultList();
                for (Domicilios domicilios : lista) {
                    retorno = domicilios.getId();
                }
            }  
            
        } catch (Exception e) {
            retorno=-1;
            logger.error("Error en metodo existe de EJBDomicilio "+e);
        }finally{
            return retorno;
        }
    }
   //----------------------------------------------------------------------------------------
    /**
     * 
     * @param xmlDomicilio objeto datos del domicilio
     * @return un numero si se agregó el domicilio o actualizó correctamente, si es negativo hubo algún error.
     */
    @Override
    public long addDomicilio(String xmlDomicilio) {
       long retorno =0;
        try {
            //------------------------------------------------------------------
            //Utiliza libreria para transformar objeto a string y viceversa
            XStream xstream = new XStream();
            xstream.alias("Domicilio",DatosDomicilios.class);
            DatosDomicilios domiciXML = (DatosDomicilios) xstream.fromXML(xmlDomicilio);
            //------------------------------------------------------------------
           
           
                            long idDomicilio1 = existe(domiciXML);
                            switch((int)idDomicilio1){
                                    case 1:{
                                        retorno=actualizarDomicilio(domiciXML,idDomicilio1);                        
                                        break;
                                    }                    
                                    case -1:{
                                        retorno=-1;
                                        break;
                                    }
                                default:{
                                    retorno = procesarAddDomicilio(domiciXML);                   
                                }
                            }
             
            
        } catch (Exception e) {
            retorno =-2;
            logger.error("Error en Metodo addDomicilio "+e);
        }finally{
            return retorno;
        }
    }
    /**
     * 
     * @param domiciXML objeto domicilio
     * @param iddomicilio numero de domicilio
     * @return devuelve numero, si es entero mayor que cero la operecion se realizó con éxito 
     * si es menor a cero hubo algún error, si es cero no pasó nada
     */
private long actualizarDomicilio(DatosDomicilios domiciXML,long iddomicilio) {
        long retorno = 0L;
        try {
            //busco una entidad domicilio
                Domicilios domicilio = em.find(Domicilios.class, iddomicilio);
                    domicilio.setArea(domiciXML.getArea());
                    domicilio.setBarrios(em.find(Barrios.class, domiciXML.getBarrio().getBarrioId()));
                    domicilio.setCalles(em.find(Calles.class, domiciXML.getCalle().getCalleId()));
            if(domiciXML.getObservaciones()!=null) {
                domicilio.setObservaciones(domiciXML.getObservaciones().toUpperCase());
                }            
                        domicilio.setLocalidades(em.find(Localidades.class, domiciXML.getLocalidad().getIdLocalidad()));
                        domicilio.setManzana(domiciXML.getManzana());
                        domicilio.setMonoblock(domiciXML.getMonoblock());
                        domicilio.setNumdepto(domiciXML.getNumDepto());
                        domicilio.setNumero(domiciXML.getNumero());
                        domicilio.setOrientacion(em.find(Orientacion.class, domiciXML.getOrientacion().getOrientacion()));
                        domicilio.setPiso(domiciXML.getPiso());
                        domicilio.setSector(domiciXML.getSector());
                        domicilio.setEntrecalleycalle(domiciXML.getEntrecalleycalle());                        
                        domicilio.setTorre(domiciXML.getTorre());
                      em.merge(domicilio);
                retorno = domicilio.getId();
        } catch (Exception e) {
            retorno = -3;
            logger.error("Error en metodo actualizarDomicilio, EJBDomicilio "+e);
        }finally{
            
            return retorno;
        }
    }

    
}