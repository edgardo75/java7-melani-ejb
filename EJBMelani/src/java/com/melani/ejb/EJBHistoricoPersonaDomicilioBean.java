/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.HistPersonasDomicilios;
import java.util.GregorianCalendar;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBHistoricoPersonaDomicilioBean")
@WebService(serviceName="ServicesHistPD",name="HistPersoDominWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBHistoricoPersonaDomicilioBean implements EJBHistoricoPersonaDomicilioRemote {
    private static final Logger logger = Logger.getLogger(EJBHistoricoPersonaDomicilioBean.class);
    @PersistenceContext
    private EntityManager em;      
    /**
     * 
     * @param idDomicilio objeto domicilio representando los datos de uno en especial
     * @param idPersona numero que identifica una persona
     * @param idUsuario numero que identifica un usuario
     * @return 
     */
    @Override
    public long addOneHomePerson(Integer idDomicilio, Integer idPersona,Integer idUsuario) {
        long retorno =0;
        try {
            //agrega una persona al historico de personas domicilio para almacenar el domicilio antiguo luego de un cambio realizado
                    GregorianCalendar gc = new GregorianCalendar();
                    HistPersonasDomicilios histpd = new HistPersonasDomicilios();
                    histpd.setFechadecambio(gc.getTime());
                    histpd.setIdDomicilio(Long.valueOf(idDomicilio));
                    histpd.setIdPersona(Long.valueOf(idPersona));
                    histpd.setIdusuario(idUsuario);                   
                    em.persist(histpd);
                    retorno=histpd.getIdhistperdom();
        } catch (Exception e) {
            retorno = -4;
            logger.error("Error en metodo addOneHomePerson, EJBHistoricoPersonaDomicilioBean "+e.getMessage());
        }finally{
           return retorno;
        }
    }        
}
