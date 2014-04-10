/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.EmpresaTelefonia;
import com.melani.entity.Telefonos;
import com.melani.entity.TelefonosPK;
import com.melani.entity.Tipostelefono;
import com.melani.utils.DatosTelefonos;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBTelefonos")
public class EJBTelefonos implements EJBTelefonosRemote {
    private static final Logger logger = Logger.getLogger(EJBTelefonos.class);
    @PersistenceContext
    private EntityManager em;
    /**
     * 
     * @param datosTel objeto que representa los datos del telefono
     * @return devuelve un numero si la operacion tuvo exito de agregar un telefono, cero no paso nada, negativo hubo un error.
     */
    @Override
    public long addTelefonos(DatosTelefonos datosTel) {
        long retorno =0;
        try {
                    //-----------------------------------------------------------------------------
                    //-----------------------------------------------------------------------------
                    TelefonosPK telepk = new TelefonosPK(Long.valueOf(datosTel.getNumero().trim()),Long.valueOf(datosTel.getPrefijo().trim()));
                            //-----------------------------------------------------------------------------
                            Query consulta = em.createQuery("SELECT t FROM Telefonos t WHERE t.telefonosPK.idPrefijo = :idPrefijo and " +
                                            "t.telefonosPK.numero = :numero");
                                    consulta.setParameter("idPrefijo", Long.valueOf(datosTel.getPrefijo().trim()));
                                    consulta.setParameter("numero", Long.valueOf(datosTel.getNumero().trim()));
                                            if(consulta.getResultList().size()==1) {
                                                retorno = 1;
                                            } else{
                                                Telefonos telefono = new Telefonos();
                                                telefono.setIdEmpresatelefonia(em.find(EmpresaTelefonia.class, datosTel.getIdEmpresaTelefonia().getIdempresatelefonia()));
                                                telefono.setIdTipotelefono(em.find(Tipostelefono.class, datosTel.getTipoTelefono().getTipoTelefono()));
                                                telefono.setTelefonosPK(telepk);
                                                em.persist(telefono);
                                                em.flush();
                                                retorno = 2;
                                            }
                    //--------------------------------------------------------------------------------------
        } catch (NumberFormatException e) {
            retorno = -1;
            logger.error("Error en metodo addTelefonos, EJBTelefonos "+e);
        }finally{
            
            return retorno;
        }
    }
}
