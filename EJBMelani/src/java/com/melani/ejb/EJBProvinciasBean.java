/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Provincias;
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
@Stateless(name="ejb/EJBProvinciasBean")
@WebService(serviceName="ServiceProvincias",name="ProvinciasWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBProvinciasBean implements EJBProvinciasRemote {
org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EJBProvinciasBean.class);
@PersistenceContext
private EntityManager em;
/**
 * 
 * @return devuelve un listado de todas las provincias con estructura xml
 */
@Override
    public String searchAllProvincias() {
        StringBuilder xml = new StringBuilder("<Lista>\n");
        try {
            Query consulta = em.createNamedQuery("Provincias.findAll");
            List<Provincias> lista = consulta.getResultList();
            if(lista.isEmpty()) {
                xml.append("NO HAY PROVINCIAS CARGADAS");
            } else{
                for (Provincias provincias : lista) {
                    xml.append(provincias.toXML());
                }
                
            }
        } catch (Exception e) {
            logger.error("error en metodo searchallPRovincias"+e.getMessage());
        }finally{
            
            xml.append("</Lista>\n");
        return xml.toString();
        }
    }
}
