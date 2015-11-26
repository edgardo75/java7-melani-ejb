package com.melani.ejb;
import javax.ejb.Remote;

@Remote
public interface EJBProvinciasRemote {
    String searchAllProvincias();
}
