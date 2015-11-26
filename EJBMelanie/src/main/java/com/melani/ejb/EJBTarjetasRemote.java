package com.melani.ejb;
import javax.ejb.Remote;

@Remote
public interface EJBTarjetasRemote {
    String searchAllTarjetasCreditoDebito();
}
