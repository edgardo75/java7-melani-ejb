package com.melani.ejb;
import com.melani.entity.Barrios;
import java.io.UnsupportedEncodingException;
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
 * @version 1.0 Build 5600 Feb 20, 2013
 */
@Stateless(name="ejb/EJBBarrios")
@WebService(serviceName="ServiceBarrios",name="BarriosWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBBarrios implements EJBBarriosRemote {
   private static final Logger logger = Logger.getLogger(EJBBarrios.class);
   @PersistenceContext
   private EntityManager em;
   
   /**
    * 
    * @param descripcion nombre propio del barrio
    * @param idUsuario operario que raliza la accion
    * @return retorna el id del barrio 
    */
   

   @Override
    public long addBarrio(String descripcion,int idUsuario) {
        long retorno = 0;
        String internalDescripcion;
        String out;
        try {            
            //convierto string a su correspondiente encoding para evitar errores de parseo
            internalDescripcion = new String(descripcion.getBytes("ISO-8859-1"), "UTF-8");
            
            //metodo que agrega un nombre de barrio
           //variable estatica para indicar el nivel de error
            
            
            
           
            if(internalDescripcion.length()>0){
                  
            //------------------------------------------------------------------------------------------------
                    
                    Query consulta =  em.createQuery("SELECT b FROM Barrios b WHERE LOWER(b.descripcion) LIKE LOWER(?1)",Barrios.class);
                    consulta.setParameter("1",internalDescripcion+"%".toLowerCase());
                    List<Barrios> lista = consulta.getResultList();
                    
                    //------------------------------------------------------------------------------------------------
                    //inserto en la base de datos el nombre de barrio
                          if (lista.isEmpty()) {
                                Barrios barrio = new Barrios();
                                barrio.setDescripcion(descripcion.toUpperCase());
                                
                                em.persist(barrio);
                                

                                retorno = barrio.getId();
                            } else {
                                retorno = -4;
                            }
                    //------------------------------------------------------------------------------------------------
            }else {
                retorno=-2;
            }
        } catch (UnsupportedEncodingException e) {
            retorno = -1;
            logger.error("Error en metodo addBarrio "+e.getLocalizedMessage());
        } finally {           
            
            return retorno;
        }
    }

/**
 *
 * @return devuelve la lista de barrios con estructura xml
 */

   
   @Override
    public String searchAllBarrios() {
        
        String xml = "<?xml version='1.0' encoding='ISO-8859-1'?>\n"
                + "<Lista>\n";        
        try {
        
            //Consulta jpql
            Query consulta = em.createNamedQuery("Barrios.findAll");
            List<Barrios>lista = consulta.getResultList();
                if(lista.isEmpty()) {
                    xml+="LA CONSULTA NO ARROJÓ RESULTADOS";
            } else{
                    StringBuilder xmlLoop = new StringBuilder(10);
                    for (Barrios barrios : lista) {
                        xmlLoop.append(barrios.toXML());
                    } 
                    xml+=xmlLoop;
                }                         
        //*********************************************************************
            
           
        } catch (UnsupportedEncodingException e) {                        
            logger.error("Error en metodo searchAllBarrios ");
        } finally {           
            xml+="</Lista>";          
            return xml;
        }
    }

/**
 *
 * @return devuelve la cantidad actual de barrios almacenados
 */

   @Override
    public int recordCountBarrios() {
       int retorno =0;      
       try {
                      //consulta jpql
                       Query consulta = em.createQuery("SELECT b FROM Barrios b");
                       retorno = consulta.getResultList().size();
            } catch (Exception e) {
                retorno = -1;
                logger.error("Error al Obtener la cantidad de registros de la tabla barrios", e);
            }finally{                
              return retorno;
            }
    }



 
}
