/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Calles;
import com.melani.utils.ProjectHelpers;
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
@Stateless(name="ejb/EJBCalles")
@SOAPBinding(style=SOAPBinding.Style.RPC)
@WebService(serviceName="ServicesCalles",name="CallesWs")
public class EJBCalles implements EJBCallesRemote {
   private static final Logger logger = Logger.getLogger(EJBCalles.class);
   @PersistenceContext
   private EntityManager em;
   
/**
 *
 * @param descripcion nombre propio de calle
 * @param idUsuario usuario que realiza la accion de carga de calle
 * @return retorna el id de la calle insertada
 */
   @Override
    public long addCalles(String descripcion,int idUsuario) {
        long retorno = 0;
        StringBuilder internalDescripcion = new StringBuilder();
        String out = null;
        try {
            out = new String(descripcion.getBytes("ISO-8859-1"), "UTF-8");
            internalDescripcion.append(out);
            System.out.println(out);                 
            //verifico que descripcion no s
            if(internalDescripcion.length()>0&& ProjectHelpers.DescripcionValidator.validate(internalDescripcion.toString())){
//                //paso a minúsculas las letras de la palabra
                    descripcion = descripcion.toLowerCase();
                    Query consulta = em.createQuery("SELECT c FROM Calles c WHERE LOWER(c.descripcion) LIKE LOWER(:descripcion)",Calles.class);
                    consulta.setParameter("descripcion", internalDescripcion.append("%").toString().toLowerCase());
                    
                    List<Calles> lista = consulta.getResultList();
                    
                       //inserto en la base de datos el nombre de calle
                        if (lista.isEmpty()) {
                            Calles calle = new Calles();
                            calle.setDescripcion(descripcion.toUpperCase());
                            em.persist(calle);
                            em.flush();
                            retorno = calle.getId();
                        } else {
                            retorno = -5;
                        }                    
            }else {
                retorno = -4;
            }
            
        } catch (UnsupportedEncodingException e) {
            retorno = -1;
            logger.error("Error en metodo addCalles "+e.getLocalizedMessage());
        } finally {    
            return retorno;
        }
    }
/**
 *
 * @return devuelve una lista de Calles de la entidad 
 */
   @Override
    public String searchAllCalles() {
        StringBuilder xml = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>\n"
                + "<Lista>\n");
        try {
            Query consulta =em.createQuery("SELECT c FROM Calles c Order by c.id");
            List<Calles>lista = consulta.getResultList();
            if(lista.isEmpty()) {
                xml.append("LA CONSULTA NO ARROJÓ RESULTADOS");
            } else{
                for (Calles calles : lista) {
                    xml.append(calles.toXML());
                }
             
            }      
        } catch (Exception e) {
            xml.append("Error");
            logger.error("Error en metodo searchallcalles "+ e.getLocalizedMessage());
            e.getMessage();
        } finally {           
               xml.append("</Lista>");               
            return xml.toString();
        }
    }
/**
 *
 * @return devuelve la cantidad de calles actualmente en la "vista" del contenedor
 */
   @Override
    public Object recorCountCalles() { 
        int retorno =0;
        try {
            Query consulta = em.createQuery("Select c From Calles c");
                retorno =consulta.getResultList().size();
        } catch (Exception e) {
            logger.error("Error en metodo recorCountCalles "+e.getLocalizedMessage());
        }finally{
            return retorno;
        }
    }
}
