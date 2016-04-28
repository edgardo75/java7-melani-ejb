package com.melani.ejb;
import com.melani.entity.Clientes;
import com.melani.entity.Domicilios;
import com.melani.entity.PersonasDomicilios;
import com.melani.entity.PersonasdomiciliosPK;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

@Stateless(name="ejb/EJBClienteDomicilio")
public class EJBClienteDomicilio implements EJBClienteDomicilioRemote {
    private static final Logger LOGGER  = Logger.getLogger(EJBClienteDomicilio.class);
    @PersistenceContext
    private EntityManager em;
    @EJB
    EJBHistoricoPersonaDomicilioRemote ejbhistperdom;

    @Override
    public String addRelacionClienteDomicilio(long idCliente, long idDomicilio,int idUsuario) {
        String retorno = "NADA";
        
            if((idDomicilio>0) && (idCliente>0)&&idUsuario>=0){
                    GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
                    PersonasdomiciliosPK perpk = new PersonasdomiciliosPK(idDomicilio, idCliente);
                    PersonasDomicilios personadomici = renovarDomicilio(idDomicilio,idCliente,idUsuario);                    
                        PersonasDomicilios personadomicilio = new PersonasDomicilios();
                        personadomicilio.setDomicilioss(em.find(Domicilios.class, idDomicilio));
                        personadomicilio.setEstado("Habitable".toUpperCase());
                        personadomicilio.setPersonasdomiciliosPK(perpk);
                        personadomicilio.setFechaingresovivienda(calendario.getTime());
                        personadomicilio.setPersonas(em.find(Clientes.class, idCliente));
                        em.persist(personadomicilio);                        
                        retorno ="InyectóRelacion";
            }
        return retorno;        
    }
  
private PersonasDomicilios renovarDomicilio(long idDomicilio, long idCliente,int idUsuario) {
        PersonasDomicilios perdomi;        
           perdomi = em.find(PersonasDomicilios.class, new PersonasdomiciliosPK(idDomicilio, idCliente));
           if(perdomi!=null){
                    Query consulta = em.createQuery("SELECT p FROM PersonasDomicilios p WHERE p.personasdomiciliosPK.idPersona = :idPersona");
                    consulta.setParameter("idPersona", idCliente);
                    List<PersonasDomicilios>lista= consulta.getResultList();
                    PersonasDomicilios personasDomicilios = null;
                                if(!lista.isEmpty()){
                                        for (Iterator<PersonasDomicilios> it = lista.iterator(); it.hasNext();) {
                                             personasDomicilios = it.next();
                                             //agrego el domicilio relacion al historico antes de remover la relacion para que quede constancia en la base de datos del cambio
                                            ejbhistperdom.addHistoricoPersonaDomicilio(personasDomicilios.getDomicilioss()
                                            .getId().intValue(),personasDomicilios.getPersonas().getIdPersona().intValue(), idUsuario);
                                            em.remove(personasDomicilios);
                                        }
                                }
                        em.flush();
                        perdomi=personasDomicilios;
           }
            return perdomi;
    }
}