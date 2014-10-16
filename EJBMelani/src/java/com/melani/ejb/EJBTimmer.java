/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;



import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimeZone;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author win7
 */
@Stateless
@WebService
public class EJBTimmer {

    @PersistenceContext(unitName = "EJBMelaniPU2")
    EntityManager em;
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EJBTimmer.class);
    @Schedule(persistent = false,timezone = "America/Argentina/San_Juan",second = "50",hour = "10",minute = "17")
    private void ventasDiarias(){ 
        final String miCorreo = "micorreo@gmail.com"; 
        final String miContrasena = "*****";
        final String servidorSMTP = "smtp.gmail.com";
        final String puertoEnvio = "465";
        String mailReceptor = null;
        String asunto = null;
        String cuerpo = null;
        int ventasDia = 0;
    
    
                Properties props = new Properties();
                    props.put("mail.smtp.user", miCorreo);
                    props.put("mail.smtp.host", servidorSMTP);
                    props.put("mail.smtp.port", puertoEnvio);
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.socketFactory.port", puertoEnvio);
                    props.put("mail.smtp.socketFactory.class",
                            "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.socketFactory.fallback", "false");

                    //SecurityManager security = System.getSecurityManager();


                    try {
                        GregorianCalendar gc = new GregorianCalendar(TimeZone.getDefault()); 
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                        Query consulta = em.createQuery("SELECT SUM(n.montototalapagar) FROM Notadepedido n WHERE n.fechadecompra = CURRENT_DATE");

                   Session session = Session.getInstance(props, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication(){
                         return new PasswordAuthentication("edgardo75@gmail.com", "dunis6648");
                        }
                    });



                    MimeMessage message = new MimeMessage(session);
                    StringBuilder result = new StringBuilder();
                    if( consulta.getResultList().toString() == null)
                        result.append("No se registraron ventas!!!");
                    else
                        result.append(consulta.getResultList().toString().replace("[", "").replace("]", ""));

                    message.setFrom(new InternetAddress("edgardo75@gmail.com"));
                    message.addRecipient(Message.RecipientType.TO,new InternetAddress("edgardo75@gmail.com"));
                    










                        Multipart multipart = new MimeMultipart("alternative");

                        MimeBodyPart textPart = new MimeBodyPart();
                        String textContext=null;
                        String resultParser = consulta.getResultList().toString().replace("[", "").replace("]","");
                        if(resultParser.equals("null")){
                             textContext = "Ventas del Día "+sdf.format(gc.getTime())+" $ 0";
                        }else
                            textContext = "Ventas del Día "+sdf.format(gc.getTime())+" $ "+resultParser;
                        
                        message.setSubject(textContext);
                        
                        textPart.setText(textContext);

                        MimeBodyPart htmlPart = new MimeBodyPart();
                        String htmlContext = "<html>"
                                                + "<h1>Hi</h1>"
                                                + "<p>Hola viejo te envio este email de notificacion de testeo para saber ventas diarias esto es por email tambien puede ser via movil decime si no está <strong>cool</strong>!!!, saludos</p>"
                                                + "<p>"+textContext+"</p>"
                                            + "</html>";
                        htmlPart.setContent(htmlContext, "text/html");

                        multipart.addBodyPart(textPart);
                        multipart.addBodyPart(htmlPart);
                        message.setContent(multipart);





                        Transport.send(message);





                    } catch (MessagingException ex) {
                        logger.error("Error en EJBTimmer");
                    }
    }

    /**
     * Web service operation
     * @return 
     */
    public String obtenerIPAddress() {
        //TODO write your implementation code here:
        return null;
    }

}