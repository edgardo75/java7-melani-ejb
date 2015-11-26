package com.melani.ejb;
import com.melani.utils.DatosTelefonos;
import javax.ejb.Remote;

@Remote
public interface EJBTelefonosRemote {
    long addTelefonos(DatosTelefonos datosTel);
}