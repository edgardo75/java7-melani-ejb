package com.melani.ejb;
import javax.ejb.Remote;
@Remote
public interface EJBPorcentajesRemote {
    short agregarDatosPorcenta(String descripcion, double valor);
}
