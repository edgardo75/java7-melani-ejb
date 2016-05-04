package com.melani.utils;

public class ClienteDomicilioTelefono {
    private DatosCliente item;
    private Integer idusuario;
    private DatosDomicilios Domicilio;
    private ListaTelefonos listaTelefonos;
   
    public ClienteDomicilioTelefono(){}
    public DatosCliente getCliente() {
        return item;
    }
    public Integer getIdusuario() {
        return idusuario;
    }
    public DatosCliente getItem() {
        return item;
    }
    public ListaTelefonos getListaTelefonos() {
        return listaTelefonos;
    }
    public DatosDomicilios getDomicilio() {
        return Domicilio;
    }
}
