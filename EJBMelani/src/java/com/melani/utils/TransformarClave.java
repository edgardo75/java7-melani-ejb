/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.utils;

/**
 *
 * @author win7
 */
public class TransformarClave {
    
    /**
     *
     */
    public TransformarClave(){}

    /**
     *
     * @param text
     * @return
     */
    public String encriptData(String text){
        StringBuilder password = new StringBuilder(text);
        
        for (int i = 0; i < password.length(); i++) {
            int tmp= 0;
            tmp = password.charAt(i);
            tmp *= 16;
            password.setCharAt(i,(char) tmp);
        }
    
        return password.toString();
    }

    /**
     *
     * @param text
     * @return
     */
    public String decriptData(String text){
     
        StringBuilder password = new StringBuilder(text);
        
        for (int i = 0; i < password.length(); i++) {
            int tmp= 0;
            tmp = password.charAt(i);
            tmp /= 16;
            password.setCharAt(i,(char) tmp);
        }
        return password.toString();
    }
    
    
}
