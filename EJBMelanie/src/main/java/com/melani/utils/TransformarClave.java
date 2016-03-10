package com.melani.utils;

public class TransformarClave {

    public TransformarClave(){}

    public String encriptData(String text){
        StringBuilder password = new StringBuilder(text);
        
        for (int i = 0; i < password.length(); i++) {
            int tmp;
            tmp = password.charAt(i);
            tmp *= 16;
            password.setCharAt(i,(char) tmp);
        }
    
        return password.toString();
    }

    public String decriptData(String text){
     
        StringBuilder password = new StringBuilder(text);
        
        for (int i = 0; i < password.length(); i++) {
            int tmp;
            tmp = password.charAt(i);
            tmp /= 16;
            password.setCharAt(i,(char) tmp);
        }
        return password.toString();
    }  
}