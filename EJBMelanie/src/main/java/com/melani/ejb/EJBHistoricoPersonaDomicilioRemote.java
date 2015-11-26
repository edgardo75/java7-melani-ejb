package com.melani.ejb;
import javax.ejb.Remote;

@Remote
public interface EJBHistoricoPersonaDomicilioRemote {
    long addHistoricoPersonaDomicilio(Integer idDomicilio, Integer idPersona,Integer idUsuario);   
}
