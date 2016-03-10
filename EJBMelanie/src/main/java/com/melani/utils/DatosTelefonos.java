package com.melani.utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("telefono")
public class DatosTelefonos {    
    private String numero;
    private String prefijo;
    private EmpresaTelefonia idEmpresaTelefonia;
    private int idcliente;
    private TiposTelefonos tipoTelefono;

    public DatosTelefonos(){}

    public EmpresaTelefonia getIdEmpresaTelefonia() {
        return idEmpresaTelefonia;
    }

    public int getIdcliente() {
        return idcliente;
    }

    public TiposTelefonos getTipoTelefono() {
        return tipoTelefono;
    }
    
        public String getNumero(){
        return numero;
    }

    public String getPrefijo(){
        return prefijo;
    }

    public int getIdCliente(){
        return idcliente;
    }

    public class EmpresaTelefonia{
        private short idempresatelefonia;

        public short getIdempresatelefonia() {
            return idempresatelefonia;
        }
    }

    public class TiposTelefonos{
        private short idtipotel;

        public short getTipoTelefono() {
        return idtipotel;
    }
    }
}
