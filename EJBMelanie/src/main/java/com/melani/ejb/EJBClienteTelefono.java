package com.melani.ejb;
import com.melani.entity.Clientes;
import com.melani.entity.Personastelefonos;
import com.melani.entity.PersonastelefonosPK;
import com.melani.entity.Telefonos;
import com.melani.entity.TelefonosPK;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name="ejb/EJBClienteTelefono")
public class EJBClienteTelefono implements EJBClienteTelefonoRemote {    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public String addClienteTelefono(String numero, String prefijo, long idcliente) {
        String retorno = "NADA";
       
            if(!numero.isEmpty()&&!prefijo.isEmpty()&&idcliente>0){
                    PersonastelefonosPK persotelpk = new PersonastelefonosPK(Long.valueOf(numero), Long.valueOf(prefijo), idcliente);
                    TelefonosPK telepk = new TelefonosPK(Long.valueOf(numero), Long.valueOf(prefijo));
                    Query consulta = em.createNamedQuery("Personastelefonos.addRelationship");
                        consulta.setParameter("prefijo", Long.valueOf(prefijo.trim()));
                        consulta.setParameter("numerotel", Long.valueOf(numero.trim()));
                        consulta.setParameter("idPersona", idcliente);
                                if(consulta.getResultList().size()==1) {
                                    retorno ="RelacionTelefonoExistente";
                                } else{
                                    Personastelefonos personatel = new Personastelefonos();
                                        personatel.setDetalles("TelefonoCliente".toUpperCase());
                                        personatel.setEstado("Activo".toUpperCase());
                                        personatel.setIdPersona(em.find(Clientes.class, idcliente));
                                        personatel.setPersonastelefonosPK(persotelpk);
                                        personatel.setTelefonos(em.find(Telefonos.class, telepk));
                                        em.persist(personatel);
                                        retorno = "InyectoRelacionClienteTelefono";
                                }
            }
            return retorno;
    }
}
