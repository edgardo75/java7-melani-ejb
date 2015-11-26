package com.melani.ejb;
import com.melani.entity.Porcentajes;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;

@Stateless(name="ejb/EJBPorcentajesBean")
@WebService(serviceName="ServicePorcentajes",name="PorcentajesWs")
public class EJBPorcentajesBean implements EJBPorcentajesRemote {
    private static final Logger LOGGER = Logger.getLogger(EJBPorcentajesBean.class);
    @PersistenceContext
    private EntityManager em;   
    @Override
    public short agregarDatosPorcenta(String descripcion, double valor) {
        short retorno;        
            Porcentajes porcentaje = new Porcentajes();
            porcentaje.setDescripcion(descripcion.toUpperCase());
            porcentaje.setValor(valor);
            em.persist(porcentaje);
            retorno = porcentaje.getIdPorcentajes();        
            return retorno;        
    }
}