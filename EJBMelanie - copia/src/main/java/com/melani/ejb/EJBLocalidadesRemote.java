package com.melani.ejb;
import javax.ejb.Remote;

@Remote
public interface EJBLocalidadesRemote {
    String searchLocXProvincia(short provincia);
    long addLocalidadCompleto(String descripcion, short idprovincia, int codigopostal);
    String searchAllLocalidadesByIdProvincia(Short idprovincia);
    short addLatitudLongitud(long idProvincia, long idLocalidad, String latitud, String longitud);
}
