package com.melani.ejb;
import javax.ejb.Remote;

@Remote
public interface EJBEmpleadosRemote {
    public long addEmpleadoFullTime(String xmlEmpleado);
    String selectAllEmpleados();
    int deshabilitarEmpleado(int idEmpleado, int idEmpleadoDesabilito);
    int habilitarEmpleado(int idEmpleado, int idEmpleadohabilito);
    String checkPassEmployee(long idEmpleado,String pass);
}
