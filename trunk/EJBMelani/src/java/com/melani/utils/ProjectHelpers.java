/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;

/**
 *
 * @author win7
 */
public class ProjectHelpers {
    private static final Logger logger = Logger.getLogger(ProjectHelpers.class);
    private static final SecretKeySpec key = new SecretKeySpec("MyKey".getBytes(), "Blowfish");
    private static  Pattern pattern;
    private static  Matcher matcher;

    private ProjectHelpers() {
    }

    public static class ClaveSeguridad{

        /**
         *
         * @param frase
         * @return
         */
        public static String encriptar(String frase){
                            String encryptedRet="No Encrypted";
                            try {            
                                Cipher cipher = Cipher.getInstance("Blowfish");            
                                // initialize for encrypting
                                cipher.init(Cipher.ENCRYPT_MODE, key);            
                                byte[]encrypted=cipher.doFinal(frase.getBytes());            
                                encryptedRet=new String(encrypted);            
                            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
                                logger.error("Error en metodo al encriptar frase "+e.getLocalizedMessage());
                            }finally{
                               return encryptedRet;
                            }    
                }

        /**
         *
         * @param fraseEncriptada
         * @return
         */
        public static String decriptar(String fraseEncriptada){
                                String decriptedRet="No decrypted";
                                try {
                                    Cipher cipher = Cipher.getInstance("Blowfish");
                                     // initialize for decrypting
                                    cipher.init(Cipher.DECRYPT_MODE, key); 
                                    byte[] encryptedData=fraseEncriptada.getBytes();       
                                    byte[] decrypted = cipher.doFinal(encryptedData);
                                    String afterEncryption = new String(decrypted);
                                    decriptedRet=afterEncryption;
                                } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                                    logger.error("Error en metodo al desencriptar frase "+e.getLocalizedMessage());
                                }finally{
                                   return decriptedRet;
                                }
                 }   

        private ClaveSeguridad() {
        }
            }

    public static class NombreUsuarioValidator{

                        private static final String NAMEUSER_PATTERN="(?=^.{1,20}$)^([\\w\\.^\\-.][\\s]?)([\\w\\-\\s]*)([\\w]+$?)+$";

        /**
         *
         * @param nombreUsuario
         * @return
         */
        public static boolean validate(String nombreUsuario){              
                             pattern=Pattern.compile(NAMEUSER_PATTERN);
                             matcher = pattern.matcher(nombreUsuario);                  
                           return matcher.matches();
                        }  

        private NombreUsuarioValidator() {
        }

            }

    public static class PasswordValidator{
                    private static final  String PASSWORD_PATTERN="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,20})";

        /**
         *
         * @param password
         * @return
         */
        public static boolean validate(String password){
                             pattern=Pattern.compile(PASSWORD_PATTERN);
                             matcher = pattern.matcher(password);                  
                           return matcher.matches();
                    }

        private PasswordValidator() {
        }
            
            
            }

    public static class NumeroDocumentoValidator{
                private static final  String NUMBER_PATTERN="(?=^.{1,10}$)\\d+$";

        /**
         *
         * @param numerodocumento
         * @return
         */
        public static boolean validate(String numerodocumento){
                                 pattern=Pattern.compile(NUMBER_PATTERN);
                                matcher = pattern.matcher(numerodocumento);                  
                        return matcher.matches();
                }

        private NumeroDocumentoValidator() {
        }
            
            }

    public static class NombreyApellidoValidator {
                    private static final String NOMBREYAPELLIDO_PATTERN="(?=^.{1,30}$)[[A-Z][a-z]\\p{IsLatin}]* ?[[a-zA-Z]\\p{IsLatin}]* ?[[a-zA-Z]\\p{IsLatin}]+$";

        /**
         *
         * @param nombreyapellido
         * @return
         */
        public static boolean validate(final String nombreyapellido){   
                              pattern=Pattern.compile(NOMBREYAPELLIDO_PATTERN);
                        matcher = pattern.matcher(nombreyapellido);                  
                        return matcher.matches();
                    }

        private NombreyApellidoValidator() {
        }
            }

    public static class EmailValidator {
               private static final  String EMAIL_PATTERN="^[\\w\\-\\+\\*]+[\\w\\S]@(\\w+\\.)+[\\w]{2,4}$";

        /**
         *
         * @param email
         * @return
         */
        public static boolean validate(String email){
                   pattern=Pattern.compile(EMAIL_PATTERN);
                            matcher = pattern.matcher(email);                  
                        return matcher.matches();
               }

        private EmailValidator() {
        }
           }

    public static class DescripcionValidator {
                private static final  String DESCRIPCION_PATTERN = "(?=^.{3,100}$)^([\\w\\.\\p{IsLatin}][\\s]?)+$";
   
        /**
         *
         * @param descripcion
         * @return
         */
        public static  boolean validate(final String descripcion){   
                 pattern=Pattern.compile(DESCRIPCION_PATTERN);
		  matcher = pattern.matcher(descripcion);                  
		  return matcher.matches();
            }

        
           
    }
    public static class TelefonoValidator{
        private static final String TELEFONO_PATTERN ="^(4|15)(\\d){6,}+$";
        private static final String PREFIJO_PATTERN ="^(\\d){1,8}+$";
        
        public static boolean validateTelefono(final String telefeno){
             pattern=Pattern.compile(TELEFONO_PATTERN);
	     matcher = pattern.matcher(telefeno);                  
          return matcher.matches();
        }
        public static boolean validatePrefijo(final String prefijo){
             pattern=Pattern.compile(PREFIJO_PATTERN);
	     matcher = pattern.matcher(prefijo);                  
          return matcher.matches();
        }
    
    }
}
