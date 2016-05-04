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

@Stateless(name="ejb/EJBTelefonos")
public class EJBTelefonos implements EJBTelefonosRemote {    
    @PersistenceContext
    private EntityManager em;    
    @Override
    public long addTelefonos(DatosTelefonos datosTel) {
        long retorno;        
                    TelefonosPK telepk = new TelefonosPK(Long.valueOf(datosTel.getNumero().trim()),Long.valueOf(datosTel.getPrefijo().trim()));                          
                            Query consulta = em.createNamedQuery("Telefonos.addByCodeAndNumber");
                                    consulta.setParameter("idPrefijo", Long.valueOf(datosTel.getPrefijo().trim()));
                                    consulta.setParameter("numero", Long.valueOf(datosTel.getNumero().trim()));
                                            if(consulta.getResultList().size()==1) {
                                                retorno = 1;
                                            } else{
                                                    Telefonos telefono = new Telefonos();
                                                    telefono.setIdEmpresatelefonia(em.find(EmpresaTelefonia.class, 
                                                            datosTel.getIdEmpresaTelefonia().getIdempresatelefonia()));
                                                    telefono.setIdTipotelefono(em.find(Tipostelefono.class, 
                                                            datosTel.getTipoTelefono().getTipoTelefono()));
                                                    telefono.setTelefonosPK(telepk);
                                                    em.persist(telefono);                                                
                                                    retorno = 2;             
                                                }
            return retorno;                                      
    }
}