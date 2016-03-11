package com.melani.ejb;
import javax.ejb.Remote;
@Remote
public interface EJBClienteTelefonoRemote {
    String addClienteTelefono(String numero, String prefijo, long idcliente);
}
