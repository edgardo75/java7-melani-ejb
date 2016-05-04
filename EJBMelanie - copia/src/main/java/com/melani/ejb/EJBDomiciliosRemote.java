package com.melani.ejb;
import com.melani.utils.DatosDomicilios;
import javax.ejb.Remote;

@Remote
public interface EJBDomiciliosRemote {
    long addDomicilios(DatosDomicilios xmlDomicilios);
    long addDomicilio(String xmlDomicilio);
}
