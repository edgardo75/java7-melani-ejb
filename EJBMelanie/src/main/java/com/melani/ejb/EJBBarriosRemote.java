package com.melani.ejb;
import javax.ejb.Remote;

@Remote
public interface EJBBarriosRemote {
    long addBarrio(String descripcion,int idUsuario);
    String searchAllBarrios();
    int recordCountBarrios();
}
