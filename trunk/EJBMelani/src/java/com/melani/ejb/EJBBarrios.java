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
import org.apache.log4j.Level;
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
        StringBuilder internalDescripcion =new StringBuilder();
        String out = null;
        try {            
            //convierto string a su correspondiente encoding
            out = new String(descripcion.getBytes("ISO-8859-1"), "UTF-8");
            internalDescripcion.append(out);        
            //metodo que agrega un nombre de barrio
           //variable estatica para indicar el nivel de error
            logger.setLevel(Level.ERROR);
            
            
           
            if(internalDescripcion.length()>0){
                  
            //------------------------------------------------------------------------------------------------
                    
                    Query consulta =  em.createNamedQuery("SELECT b FROM Barrios b WHERE LOWER(b.descripcion) LIKE LOWER(?1)",Barrios.class);
                    consulta.setParameter("1",internalDescripcion.append("%").toString().toLowerCase());
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
            logger.error("Error en metodo addBarrio "+e);
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
        
        StringBuilder xml = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>\n"
                + "<Lista>\n");        
        try {
        
            //Consulta jpql
            Query consulta = em.createNamedQuery("Barrios.findAll");
            List<Barrios>lista = consulta.getResultList();
                if(lista.isEmpty()) {
                    xml.append("LA CONSULTA NO ARROJÓ RESULTADOS");
            } else{
                    for (Barrios barrios : lista) {
                        xml.append(barrios.toXML());
                    }                    
                }                         
        //*********************************************************************
            xml.append("</Lista>");          
           
        } catch (Exception e) {                        
            logger.error("Error en metodo searchAllBarrios "+e.getLocalizedMessage());
        } finally {           
            return xml.toString();
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
/**
 *
 * @param indiceInicio indica la pagina en al cual se establece la consulta paginada
 * @param numeroItems indica los numeros de registros que retorna cada consulta paginada
 * @return devuelve una lista de barrios instanciados
 */

//   @Override
//   public String obtenrItemsPaginados(int indiceInicio, int numeroItems) {
//        StringBuilder xml = new StringBuilder("<?xml version = '1.0' encoding = 'UTF-8'?>\n" +
//                "<Lista>\n");
//        int indice = 0;
//        int nroItems=0;
//        try {
//            indice=indiceInicio;
//            nroItems=numeroItems;
//            
//            Query consulta = em.createQuery("SELECT b FROM Barrios b", Barrios.class);
//            consulta.setMaxResults(nroItems);
//             consulta.setFirstResult(indice*nroItems);
//            
//            
//            List<Barrios>lista = consulta.getResultList();
//            
//            for (Barrios barrios : lista) {
//                xml.append(barrios.toXML());
//            }
//            xml.append("</Lista>\n");       
//            
//        } catch (Exception e) {
//            logger.error("Error al obtenerItemsPaginados EJBbarrios "+e.getLocalizedMessage());
//        }finally{
//            return xml.toString();
//        }
//}
/**
 *
 * @param startindex indice de pagina
 * @param numitems cantidad de registro por pagina
 * @return devuelve la lista de barrios instanciados
 */
  
//   @Override
//   public Barrios[] barrios_Paging(int startindex, int numitems) {
//         Barrios[]fBarrios=null;       
//        try {   
//            
//            Query consulta = em.createQuery("SELECT b FROM Barrios b");
//                consulta.setMaxResults(numitems);
//                consulta.setFirstResult(startindex*numitems);
//                List<Barrios>lista = consulta.getResultList();
//            if(lista.size()>0){
//                try {
//                    int len = lista.size();
//                    fBarrios = new Barrios[len];
//                    lista.toArray(fBarrios);
//                } catch (Exception ee) {
//                   ee.getMessage();
//                }
//            }          
//        } catch (Exception e) {
//            e.getMessage();
//            logger.warn(e.getLocalizedMessage());
//        }finally{                
//             return fBarrios;
//        }
//    }   
}
