package com.melani.ejb;
import com.melani.entity.Calles;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
@Stateless(name="ejb/EJBCalles")
@WebService(serviceName="ServicesCalles",name="CallesWs")
public class EJBCalles implements EJBCallesRemote {  
   @PersistenceContext
   private EntityManager em;

   @Override
    public long addCalles(String descripcion,int idUsuario) {
        long retorno;
        String internalDescripcion = "";        
                    try {
                        internalDescripcion = new String(descripcion.getBytes("ISO-8859-1"), "UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        java.util.logging.Logger.getLogger(EJBCalles.class.getName()).log(Level.SEVERE, null, ex);
                    }    
                        if(internalDescripcion.length()>0){            
                                descripcion = descripcion.toLowerCase();
                                Query consulta = em.createQuery("SELECT c FROM Calles c WHERE LOWER(c.descripcion) LIKE LOWER(:descripcion)",Calles.class);
                                consulta.setParameter("descripcion", internalDescripcion+"%".toLowerCase());                    
                                List<Calles> lista = consulta.getResultList();                                           
                                                if (lista.isEmpty()) {
                                                    Calles calle = new Calles();
                                                    calle.setDescripcion(descripcion.toUpperCase());
                                                    em.persist(calle);

                                                    retorno = calle.getId();
                                                } else {
                                                    retorno = -5;
                                                }                    
                        }else {
                            retorno = -4;
                        }
        return retorno;        
    }

   @Override
    public String searchAllCalles() {
        String xml = "<?xml version='1.0' encoding='UTF-8'?>\n"
                + "<Lista>\n";        
            Query consulta =em.createQuery("SELECT c FROM Calles c Order by c.id");
            List<Calles>lista = consulta.getResultList();
                if(lista.isEmpty()) {
                    xml+="LA CONSULTA NO ARROJÓ RESULTADOS";
                } else{
                        StringBuilder xmlLoop = new StringBuilder(32);
                        for (Calles calles : lista) {
                            xmlLoop.append(calles.toXML());
                    }
                   
                        xml+=xmlLoop;
                }      
         xml+="</Lista>";               
       return xml;
    }
 
    @Override
    public int recorCountCalles() { 
        Query consulta = em.createQuery("Select c From Calles c");              
       return consulta.getResultList().size();
    }
}
