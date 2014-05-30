/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Localidades;
import com.melani.entity.Provincias;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBLocalidades")
@WebService(serviceName="ServicesLocalidades",name="LocalidadesWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBLocalidades implements EJBLocalidadesRemote {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EJBLocalidades.class);
     @PersistenceContext(unitName="EJBMelaniPU2")
     private EntityManager em;  
     /**
      * 
      * @param idProvincia representa un identificador de una provincia en la base de datos
      * @return devuleve un listado de las provincias argentinas en con estructura xml
      */
    @Override
    public String searchLocXProvincia(short idProvincia) {
        StringBuilder xml = new StringBuilder("<Lista>\n");
        Query jpql = null;
        try {
            
            jpql = em.createQuery("SELECT l FROM Localidades l WHERE l.provincias.idProvincia = :idProvincia and l.latitud <> null and l.longitud <> null");
            jpql.setParameter("idProvincia",idProvincia);
            List<Localidades>localidad = jpql.getResultList();
            for (Localidades localidades : localidad) {
                xml.append(localidades.toXML());
            }
        } catch (Exception e) {            
            logger.error("error en metodo searchLocXProvincia "+e.getMessage());
        }finally{
           return xml.append("</Lista>\n").toString();
        }
    }
/**
 * 
 * @param descripcion el nombre de la localidad
 * @param idProvincia el id de la provincia
 * @param codigopostal numero postal de la localidad en la provincia
 * @return devuelve el id de la localidad almancenada con éxito en la base de datos, caso contrario numero negativo, se produjo un error al 
 * ejecutar el método, se validó mal la descripcion de la localidad, si es cero no paso nada.
 */
    @Override
    public long addLocalidadCompleto(String descripcion, short idProvincia, int codigopostal) {
        long retorno = 0;
        StringBuilder internalDescripcion = new StringBuilder();
        String out = null;
        try {
            out = new String(descripcion.getBytes("ISO-8859-1"), "UTF-8");
            internalDescripcion.append(out);
            
            
            if(internalDescripcion.length()>0){
                    
                    internalDescripcion.append("%");
                    Query consulta = em.createQuery("SELECT l FROM Localidades l WHERE l.descripcion LIKE :descripcion and l.codigopostal = :codigopostal and " +
                            " l.provincias.idProvincia = :idProvincia");
                    consulta.setParameter("descripcion",descripcion.toString().toLowerCase());
                    consulta.setParameter("codigopostal", codigopostal);
                    consulta.setParameter("idProvincia", idProvincia);
                            List<Localidades> lista = consulta.getResultList();
                            if (lista.isEmpty()) {
                                Localidades depto = new Localidades();
                                depto.setDescripcion(descripcion.toUpperCase());
                                depto.setProvincias(em.find(Provincias.class, idProvincia));
                                depto.setCodigopostal(codigopostal);
                                depto.setLatitud("0");
                                depto.setLongitud("0");
                                
                                em.persist(depto);
                                em.flush();
                                retorno = depto.getIdLocalidad();
                            } else {
                                retorno = -6;
                            }
            }else {
                retorno = -7;
            }
            
        } catch (UnsupportedEncodingException e) {
            retorno =-1;
            logger.error("Error en metodo addLocalidades " + e.getLocalizedMessage());
       }finally{
            
            return retorno;
        }
    }
    //--------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------
    /**
     * 
     * @param idProvincia representa el identificador de la provincia
     * @return devuelve un listado de las localidades de la provincia correspondiente
     */
    @Override
    public String searchAllLocalidadesByIdProvincia(Short idProvincia) {
         StringBuilder resultado = new StringBuilder("<Lista>\n");
         try {
             Query consulta = em.createQuery("SELECT l FROM Localidades l WHERE l.provincias.idProvincia = :idProvincia and l.latitud is not null and l.longitud is not null order by l.descripcion asc");
             consulta.setParameter("idProvincia", idProvincia);
             List<Localidades>lista = consulta.getResultList();
             if(lista.isEmpty()) {
                 resultado.append("NO HAY LOCALIDADES CARGADAS en ").append(em.find(Provincias.class, idProvincia).getProvincia());
             } else{
                 for (Localidades localidades : lista) {
                     resultado.append(localidades.toXML());
                 }
            
             }
        } catch (Exception e) {
            logger.error("Error en metodo searchAllLocalidadesByIdProvincia "+e.getLocalizedMessage());
            resultado.append("<error>Se produjo un error</error>");
        }finally{
              resultado.append("</Lista>\n");              
            return resultado.toString();
        }
    }

    @Override
    public short addLatitudLongitud(long idProvincia, long idLocalidad, String latitud, String longitud) {
        Localidades localidad = em.find(Localidades.class, idLocalidad);
        localidad.setLatitud(latitud);
        localidad.setLongitud(longitud);
        em.flush();
        short retorno = Short.valueOf(String.valueOf(localidad.getIdLocalidad()));
        return retorno;
    }
    
}