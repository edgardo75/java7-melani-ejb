package com.melani.ejb;
import com.melani.entity.HistPersonasDomicilios;
import java.util.GregorianCalendar;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name="ejb/EJBHistoricoPersonaDomicilioBean")
@WebService(serviceName="ServicesHistPD",name="HistPersoDominWs")
public class EJBHistoricoPersonaDomicilioBean implements EJBHistoricoPersonaDomicilioRemote {    
    @PersistenceContext
    private EntityManager em;
    @Override
    public long addHistoricoPersonaDomicilio(Integer idDomicilio, Integer idPersona,Integer idUsuario) {
        long retorno;              
                    GregorianCalendar gc = new GregorianCalendar();
                    HistPersonasDomicilios histpd = new HistPersonasDomicilios();
                    histpd.setFechadecambio(gc.getTime());
                    histpd.setIdDomicilio(Long.valueOf(idDomicilio));
                    histpd.setIdPersona(Long.valueOf(idPersona));
                    histpd.setIdusuario(idUsuario);                   
                    em.persist(histpd);
                    retorno=histpd.getIdhistperdom();        
           return retorno;        
    }        
}
