package com.melani.ejb;
import com.melani.entity.TarjetasCreditoDebito;
import java.util.List;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name="ejb/EJBTarjetasBean")
@WebService(serviceName="ServiceTarjetas",name="TartjetasWs")
public class EJBTarjetasBean implements EJBTarjetasRemote {    
    @PersistenceContext
    private EntityManager em;
   
    @Override
    public String searchAllTarjetasCreditoDebito() {
        String xml = "<Lista>\n";      
            Query consulta = em.createQuery("SELECT t FROM TarjetasCreditoDebito t Order by t.idtarjeta");
            List<TarjetasCreditoDebito>lista = consulta.getResultList();
            if(lista.size()>0){
                StringBuilder xmlTarjetas = new StringBuilder(32);
                for (TarjetasCreditoDebito tarjetasCreditoDebito : lista) {
                    xmlTarjetas.append(tarjetasCreditoDebito.toXML());
                }                
                xml+=xmlTarjetas;
            }else {
                xml+="no hay tarjetas";
            }
            xml+="</Lista>\n";
            return xml;      
    }
}