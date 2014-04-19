/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
/**
 *
 * @author Edgardo
 * @version 1.0 Build 5600 Feb 20, 2013
 */
@Stateless(name="ejb/EJBClienteDomicilio")
public class EJBClienteDomicilio implements EJBClienteDomicilioRemote {
    private static final Logger logger = Logger.getLogger(EJBClienteDomicilio.class);
    @PersistenceContext
    private EntityManager em;
    @EJB
    EJBHistoricoPersonaDomicilioRemote ejbhistperdom;
/**
 *
 * @param idCliente id de cliente registrado en la tabla correspondiente en la base de datos
 * @param idDomicilio id interno en la base de datos
 * @param idUsuario id usuario que realiza la accion
 * @return devuelve un string indicando el exito de la operación o no
 */
    
    
    @Override
    public String addRelacionClienteDomicilio(long idCliente, long idDomicilio,int idUsuario) {
        String retorno = "NADA";
        try {
            //fecha actual
            if((idDomicilio>0) && (idCliente>0)&&idUsuario>=0){
                    GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
                    PersonasdomiciliosPK perpk = new PersonasdomiciliosPK(idDomicilio, idCliente);
                    PersonasDomicilios personadomici = renovarDomicilio(idDomicilio,idCliente,idUsuario);
                    logger.info("Persona domicilio renovado "+personadomici.getPersonasdomiciliosPK().getId()+" "+personadomici.getPersonasdomiciliosPK().getIdPersona());
                        PersonasDomicilios personadomicilio = new PersonasDomicilios();
                        personadomicilio.setDomicilioss(em.find(Domicilios.class, idDomicilio));
                        personadomicilio.setEstado("Habitable".toUpperCase());
                        personadomicilio.setPersonasdomiciliosPK(perpk);
                        personadomicilio.setFechaingresovivienda(calendario.getTime());
                        personadomicilio.setPersonas(em.find(Clientes.class, idCliente));
                        em.persist(personadomicilio);
                        em.flush();
                        retorno ="InyectóRelacion";
            }
        } catch (Exception e) {
            retorno ="Error";
            logger.error("Error en metodo addRelacionClienteDomicilio "+e);
        }finally{            
            return retorno;
        }
    }
    /**
     * 
     * @param idDomicilio id de domicilio
     * @param idCliente id de cliente
     * @param idUsuario id de usuario o vendedor que realizó la operación
     * @return devuelve el objeto PersonaDomicilio
     */
private PersonasDomicilios renovarDomicilio(long idDomicilio, long idCliente,int idUsuario) {
        PersonasDomicilios perdomi = null;
        try {
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
                                ejbhistperdom.addHistoricoPersonaDomicilio(Integer.valueOf(String.valueOf(personasDomicilios.getDomicilioss().getId())),Integer.valueOf(String.valueOf(personasDomicilios.getPersonas().getIdPersona())), idUsuario);
                                em.remove(personasDomicilios);
                            }
                    }
                        em.flush();
                        perdomi=personasDomicilios;
           }
         } catch (NumberFormatException e) {
             perdomi=null;
            logger.error("Error en metodo renovarDomicilio EJBClienteDomicilio "+e.getLocalizedMessage());
         }finally{            
            return perdomi;
         }
    }
}