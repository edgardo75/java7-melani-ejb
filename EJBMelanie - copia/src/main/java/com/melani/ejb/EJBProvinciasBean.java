package com.melani.ejb;
import com.melani.entity.Provincias;
import java.util.List;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name="ejb/EJBProvinciasBean")
@WebService(serviceName="ServiceProvincias",name="ProvinciasWs")
public class EJBProvinciasBean implements EJBProvinciasRemote {
@PersistenceContext
private EntityManager em;

@Override
    public String searchAllProvincias() {
        String xml ="<Lista>\n";        
            Query consulta = em.createNamedQuery("Provincias.findAll");
            List<Provincias> lista = consulta.getResultList();
                        if(lista.isEmpty()) {
                            xml+="NO HAY PROVINCIAS CARGADAS";
                        } else{
                            StringBuilder xmlProvincias = new StringBuilder(10);
                            for (Provincias provincias : lista) {
                                xmlProvincias.append(provincias.toXML());
                            }
                            xml+=xmlProvincias;
                        }       
            xml+="</Lista>\n";
        return xml;        
    }
}