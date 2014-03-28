/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Localidades;
import com.melani.entity.Provincias;
import com.melani.utils.ProjectHelpers;
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
        String xml ="<Lista>\n";
        Query jpql = null;
        try {
            
            jpql = em.createQuery("SELECT l FROM Localidades l WHERE l.provincias.idProvincia = :idProvincia");
            jpql.setParameter("idProvincia",idProvincia);
            List<Localidades>localidad = jpql.getResultList();
            
            for (Localidades localidades : localidad) {
                xml += localidades.toXML();
            }
        } catch (Exception e) {
            xml+="<error>Error</error>";
            logger.error("error en metodo serachlocxprovincia "+e.getMessage());
        }finally{
            
            return xml+="</Lista>\n";
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
        try {
            descripcion = descripcion.toUpperCase();
            
            if(ProjectHelpers.DescripcionValidator.validate(descripcion)&&!descripcion.isEmpty()){
                    StringBuilder sb = new StringBuilder();
                    sb.append(descripcion);
                    sb.append("%");
                    Query consulta = em.createQuery("SELECT l FROM Localidades l WHERE l.descripcion LIKE :descripcion and l.codigopostal = :codigopostal and " +
                            " l.provincias.idProvincia = :idProvincia");
                    consulta.setParameter("descripcion",descripcion.toString());
                    consulta.setParameter("codigopostal", codigopostal);
                    consulta.setParameter("idProvincia", idProvincia);
                    List<Localidades> lista = consulta.getResultList();
                    if (lista.isEmpty()) {
                        Localidades depto = new Localidades();
                        depto.setDescripcion(descripcion.toUpperCase());
                        depto.setProvincias(em.find(Provincias.class, idProvincia));
                        depto.setCodigopostal(codigopostal);
                        em.persist(depto);
                        em.flush();
                        retorno = depto.getIdLocalidad();
                    } else {
                        retorno = -6;
                    }
            }else {
                retorno = -7;
            }
            
        } catch (Exception e) {
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
    public String searchAllLocalidadesbyidprov(Short idProvincia) {
         String resultado = "<Lista>\n";
         try {
             Query consulta = em.createQuery("SELECT l FROM Localidades l WHERE l.provincias.idProvincia = :idProvincia order by l.descripcion asc");
             consulta.setParameter("idProvincia", idProvincia);
             List<Localidades>lista = consulta.getResultList();
             if(lista.isEmpty()) {
                 resultado="NO HAY LOCALIDADES CARGADAS en "+em.find(Provincias.class, idProvincia).getProvincia();
             } else{
                 for (Localidades localidades : lista) {
                     resultado+=localidades.toXML();
                 }
            
             }
        } catch (Exception e) {
            logger.error("Error en metodo searchAllLocalidadesbyidprov "+e.getLocalizedMessage());
        }finally{
              resultado+="</Lista>\n";
              
        return resultado;
        }
    }
}