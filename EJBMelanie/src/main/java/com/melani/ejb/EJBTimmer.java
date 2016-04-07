package com.melani.ejb;
import com.melani.utils.ProjectHelpers;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
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
import org.apache.log4j.Logger;

@Stateless
@WebService
public class EJBTimmer {
    @PersistenceContext(unitName = "EJBMelaniPU2")
    EntityManager em;
    private static final Logger LOGGER = Logger.getLogger(EJBTimmer.class);
    @Schedule(persistent = false,timezone = "America/Argentina/San_Juan",second = "50",hour = "11",minute = "16")
    private void ventasDiarias(){ 
        final String miCorreo = "micorreo@gmail.com"; 
        final String miContrasena = "*****";
        //final String servidorSMTP = "smtp.gmail.com";
        final String servidorSMTP = "smtp.live.com";        
        //final String puertoEnvio = "465";
        final String puertoEnvio = "587";
        String mailReceptor = null;
        String asunto = null;
        String cuerpo = null;
        int ventasDia = 0;
                    Properties props = new Properties();
                    //props.put("mail.smtp.user", miCorreo);
                    props.put("mail.smtp.host", servidorSMTP);
                    props.put("mail.smtp.port", puertoEnvio);
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.transport.protocol","smtp");
                    props.put("mail.smtp.auth", "true");                  
                    try {
                        GregorianCalendar gc = new GregorianCalendar(TimeZone.getDefault()); 
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Query consulta = em.createQuery("SELECT SUM(n.montototalapagar) FROM Notadepedido n WHERE n.fechadecompra = CURRENT_DATE");
                   Session session = Session.getInstance(props, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication(){
                         return new PasswordAuthentication("edgardoalvarez@outlook.com", ProjectHelpers.ClaveSeguridad.decriptar("ÅMK˜RÉ 3€\"ÌçD‰"));
                        }
                    });
                    final MimeMessage message = new MimeMessage(session);
//                    String result = null;
//                    if( consulta.getResultList().toString() == null){
//                        result+="No se registraron ventas!!!";
//                    }else{
//                        result+=consulta.getResultList().toString().replace("[", "").replace("]", "");                    }                          
                    message.setFrom(new InternetAddress("edgardoalvarez@outlook.com"));
                    message.addRecipient(Message.RecipientType.TO,new InternetAddress("edgardoalvarez@outlook.com"));                    
                    Multipart multipart = new MimeMultipart("alternative");
                        MimeBodyPart textPart = new MimeBodyPart();
                        String textContext ;
                        String resultParser = consulta.getResultList().toString().replace("[", "").replace("]","");
                        if(resultParser.equals("null")){
                             textContext="Ventas del Día "+sdf.format(gc.getTime())+" $ 0";
                        }else
                            textContext="Ventas del Día "+sdf.format(gc.getTime())+" $ "+resultParser;                        
                        message.setSubject(textContext);                        
                        textPart.setText(textContext);
                        MimeBodyPart htmlPart = new MimeBodyPart();
                        String htmlContext = "<html>"+"<h1>Hi</h1>"+
                                "<p>Hola viejo te envio este email de notificacion de testeo para saber ventas diarias esto "
                                + "es por email tambien puede ser via movil decime si no está <strong>cool</strong>!!!, "
                                + "saludos</p>"+"<p>"+textContext+"</p>"+"</html>";
                        htmlPart.setContent(htmlContext, "text/html");
                        multipart.addBodyPart(textPart);
                        multipart.addBodyPart(htmlPart);
                        message.setContent(multipart);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {                                                                        
                                    Transport.send(message);                                    
                                } catch (MessagingException ex) {
                                    java.util.logging.Logger.getLogger(EJBTimmer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }).start();
                    } catch (MessagingException ex) {
                        LOGGER.error("Error en EJBTimmer");
                    }
    }    
    public String obtenerIPAddress() {       
        return null;
    }
}
