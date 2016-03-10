package com.melani.utils;

public class DatosCliente {
    private String apellido;// variable interna atribute apellido
    private String nombre;// variable interna atribute nombre
    private String email;//variable interna atribute email
    private String observaciones;//variable interna atribute observaciones
    private Integer nrodocu;//variable interna atribute nrodocu
    private Short idtipodocu;//variable interna atribute idtipodocu
    private float totalcompras;//variable interna atribute totalcompras
    private int totalpuntos;//variable interna atribute totalpuntos
    private long idcliente;//variable interna atribute idcliente
    private Generos genero;//variable interna atribute genero
   
        public DatosCliente(){}
 
    public String getApellido() {
        return apellido;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
  
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
   
    public String getEmail() {
        return email;
    }
   
    public void setEmail(String email) {
        this.email = email;
    }

    public Short getIdtipodocu() {
        return idtipodocu;
    }

    public void setIdtipodocu(Short idtipodocu) {
        this.idtipodocu = idtipodocu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNrodocu() {
        return nrodocu;
    }

    public void setNrodocu(Integer nrodocu) {
        this.nrodocu = nrodocu;
    }

    public float getTotalcompras() {
        return totalcompras;
    }

    public void setTotalcompras(float totalcompras) {
        this.totalcompras = totalcompras;
    }

    public int getTotalpuntos() {
        return totalpuntos;
    }

    public void setTotalpuntos(int totalpuntos) {
        this.totalpuntos = totalpuntos;
    }

    public long getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(long idcliente) {
        this.idcliente = idcliente;
    }
 
    public Generos getGeneros() {
        return genero;
    }

    public void setGeneros(Generos genero) {
        this.genero = genero;
    }

    public class Generos{
    private short idgenero;

        public Generos(){
    }

        public Generos(short idgenero) {
            this.idgenero = idgenero;
        }
    
        public short getIdgenero() {
            return idgenero;
        }

        public void setIdgenero(short idgenero) {
            this.idgenero = idgenero;
        }        
    }
}