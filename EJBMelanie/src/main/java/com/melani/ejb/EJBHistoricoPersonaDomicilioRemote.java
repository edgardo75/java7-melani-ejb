package com.melani.ejb;
import javax.ejb.Remote;

@Remote
public interface EJBHistoricoPersonaDomicilioRemote {
    long addHistoricoPersonaDomicilio(int idDomicilio,int idPersona,int idUsuario);   
}
