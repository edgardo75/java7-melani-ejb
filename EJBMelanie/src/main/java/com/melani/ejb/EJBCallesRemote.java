package com.melani.ejb;
import javax.ejb.Remote;
@Remote
public interface EJBCallesRemote {
    long addCalles(String descripcion,int idUsuario);
    String searchAllCalles();
    int recorCountCalles();
}
