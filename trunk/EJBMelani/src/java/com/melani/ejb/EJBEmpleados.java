/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.EmpleadoParttime;
import com.melani.entity.Empleados;
import com.melani.entity.FullTimeEmpleado;
import com.melani.entity.Generos;
import com.melani.entity.Notadepedido;
import com.melani.entity.Tiposdocumento;
import com.melani.utils.DatosEmpleado;
import com.melani.utils.ProjectHelpers;
import com.thoughtworks.xstream.XStream;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBEmpleados")
@WebService(serviceName="ServicesEmpleados",name="EmpleadosWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
/*
Esta clase es la encargada de administrar la logica de negocio con todo lo relacionado a los Empleados
*/
public class EJBEmpleados implements EJBEmpleadosRemote {
     private static final Logger logger = Logger.getLogger(EJBEmpleados.class);
    @PersistenceContext(unitName="EJBMelaniPU2")
    private EntityManager em;
    
    
    /**
     * 
     * @param xmlEmpleado datos del empleado recibidos con estructura xml
     * @return retorna un numero, si todo va bien retorna el id del empleado almacenado en la base de datos, caso contrario un error o por que no se completó la operacion
     */
    
    
     @Override
    public long addEmpleadoFullTime(String xmlEmpleado){
        long retorno = 0;
        try { 
                    //llamo a metodo interno para convertir a objeto los datos del empleado
                    DatosEmpleado datosEmpleado = datosEmpleadosObject(xmlEmpleado);
                      retorno = procesarDatosEmpleadoAdd(datosEmpleado);
                   } catch (NumberFormatException e) {
                    retorno = -1;
                    logger.error("Error en metodo addEmpleadoFullTime "+e.getLocalizedMessage());
                }  catch (Exception e) {
                    retorno = -1;
                        logger.error("Error en metodo addEmpleadoFullTime "+e.getLocalizedMessage());
                }
            finally{
                return retorno;
            }
    }
/**
 * 
 * @param retorno idica el numero o codigo a evaluar por el método
 * @return devuelve un número, el resultado de ejecutar una operación o metodo
 */
    
    private long numeroaRetornar(long retorno) {
         switch((int)retorno){
            case 1:{retorno=-2;break;}//nro de documento encotrado
            case 2:{retorno=-4;break;}//email detectado
            case 3:{retorno=-6;break;}//nombre usuario detectado                                           
            case -3:{retorno=-5;break;}//error en el metodo
         }         
         return retorno;
    
    }

    /**
     *
     * @param xmlEmpleado
     * @return
     */
    public long addEmpleadoParttime(String xmlEmpleado){
    
        long retorno =0;
        try {
            
             DatosEmpleado datosEmpleado = datosEmpleadosObject(xmlEmpleado);
                       
                
                
                
                retorno = procesarDatosEmpleadoAdd(datosEmpleado);
            
            

        } catch (Exception e) {
            retorno = -1;
            logger.error("Error en metodo addEmpleadoParttime "+e.getLocalizedMessage());
        }finally{
            
            return retorno;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String selectAllEmpleados() {
       String xml = "<?xml version='1.0' encoding='utf-8'?>\n"
               + "<Lista>\n";
        try {
            Query consulta = em.createQuery("SELECT e FROM Empleados e order by e.idPersona desc");            
            List<Empleados>lista = consulta.getResultList();
            if(lista.size()>0){
                for (Empleados empleados : lista) {                    
                    xml+="<item>\n";
                    xml+="<id>" +empleados.getIdPersona()+"</id>\n"+
                            "<nombre>"+StringEscapeUtils.escapeXml(empleados.getNombre())+"</nombre>\n" +
                            "<apellido>"+StringEscapeUtils.escapeXml(empleados.getApellido())+"</apellido>\n" +
                            "<genero>"+empleados.getGeneros().getIdGenero()+"</genero>\n" +
                            "<tipodocu>"+empleados.getTipodocumento().getId()+"</tipodocu>\n" +
                            "<documento>"+empleados.getNrodocumento()+"</documento>\n" +
                            "<observaciones>"+StringEscapeUtils.escapeXml(empleados.getObservaciones())+"</observaciones>\n"+
                            "<email>"+empleados.getEmail()+"</email>\n" ;
                    xml+= obtenerEmpleado(empleados);
                    xml+=empleados.toXMLEmpleado();
                    xml+="<clave>"+ProjectHelpers.ClaveSeguridad.decriptar(empleados.getPassword())+"</clave>";
                            
                    xml+="</item>\n";
                }
            }else {
                xml+="<result>Lista Vacia</result>\n";
            }
        } catch (Exception e) {
            logger.error("error al obtener lista de empleados "+e.getMessage());
            xml+="<error>"+e.getLocalizedMessage()+"</error>\n";
        }finally{
            
            return xml+="</Lista>\n";
        }
    }

    /**
     *
     * @param emp
     * @return
     */
    protected String obtenerEmpleado(Empleados emp){
        String xml=null;
        
        if(emp.getEmptype().equals("FULLTIME")){
                        Query sqlFullTimeEmp = em.createQuery("Select f From FullTimeEmpleado f Where f.idPersona = :idpersona");
                        sqlFullTimeEmp.setParameter("idpersona", emp.getIdPersona());
                        List<FullTimeEmpleado>list=sqlFullTimeEmp.getResultList();
            for (FullTimeEmpleado fullTimeEmpleado : list) {
                xml=fullTimeEmpleado.toXML();
            }

        }else{
                        Query sqlParTTimeEmp = em.createQuery("Select e From EmpleadoParttime e Where e.idPersona = :idpersona");
                        sqlParTTimeEmp.setParameter("idpersona", emp.getIdPersona());
                        List<EmpleadoParttime>list=sqlParTTimeEmp.getResultList();
            for (EmpleadoParttime empleadoParttime : list) {
                xml =empleadoParttime.toXML();
            }

        }
        return xml;
    }

    /**
     *
     * @param idEmpleado
     * @param idEmpleadoDesabilito
     * @return
     */
     @Override
    public int deshabilitarEmpleado(int idEmpleado, int idEmpleadoDesabilito) {
        int retorno =0;
        try {
            GregorianCalendar gc = new GregorianCalendar();
            StringBuilder sb =new StringBuilder();
                Empleados empleadoDesabilitado = em.find(Empleados.class, idEmpleado);
                Empleados empleadoDesabilito = em.find(Empleados.class, idEmpleadoDesabilito);
                empleadoDesabilitado.setEstado((short)0);
                    sb.append(empleadoDesabilito.getNombre());
                    sb.append(" ");
                    sb.append(empleadoDesabilito.getApellido());
                    logger.info("EMPLEADO DESHABILITADO EL DIA DE LA FECHA "+gc.getTime()+", EL EMPLEADO QUE REALIZÓ LA OPERACION FUE "+sb.toString());
             retorno=Integer.valueOf(String.valueOf(empleadoDesabilitado.getIdPersona()));
        } catch (NumberFormatException e) {
            retorno=-1;
            logger.error("Error en metodo deshabilitarEmpleado "+ e.getLocalizedMessage());
        }finally{
            
            return retorno;
        }
    }
    private long buscarPersonaEmailAndNameUser(int numerodocu,String email,String nameuser) {
        long retorno =0;
        try {
            if(numerodocu>0){
                Query consulta = em.createQuery("SELECT p FROM Personas p WHERE p.nrodocumento = :nrodocumento");
                    consulta.setParameter("nrodocumento", numerodocu);
                    if(consulta.getResultList().size()==1){
                        retorno=1;
                        return retorno;
                    }
            }
            if(email.contains("@")){
                Query sqlemail = em.createQuery("SELECT p FROM Personas p WHERE p.email = :email");
                    sqlemail.setParameter("email", email.toLowerCase());
                    if(sqlemail.getResultList().size()==1) {
                        retorno=2;
                }
            }
            if(nameuser.length()>0){
                Query sqlusername=em.createQuery("SELECT e FROM Empleados e WHERE LOWER(e.nameuser) = LOWER(:nameuser)");
                    sqlusername.setParameter("nameuser", nameuser.toLowerCase());
                    if(sqlusername.getResultList().size()==1) {
                        retorno=3;
                }
            }
            
            
        } catch (Exception e) {
            retorno =-3;
            logger.error("Error en metodo buscar persona and email "+ e.getLocalizedMessage());
        }finally{
            
            return retorno;
        }
    }

    /**
     *
     * @param idEmpleado
     * @param idEmpleadohabilito
     * @return
     */
     @Override
    public int habilitarEmpleado(int idEmpleado, int idEmpleadohabilito) {
            int retorno =0;
        try {
            GregorianCalendar gc = new GregorianCalendar();
            StringBuilder sb =new StringBuilder();
                Empleados empleadoHabilitado = em.find(Empleados.class, idEmpleado);
                Empleados empleadohabilito = em.find(Empleados.class, idEmpleadohabilito);
                empleadoHabilitado.setEstado((short)1);
                    sb.append(empleadohabilito.getNombre());
                    sb.append(" ");
                    sb.append(empleadohabilito.getApellido());
                    logger.info("EMPLEADO HABILITADO EL DIA DE LA FECHA "+gc.getTime()+", EL EMPLEADO QUE REALIZÓ LA OPERACION FUE "+sb.toString());
             retorno=Integer.valueOf(String.valueOf(empleadohabilito.getIdPersona()));
        } catch (NumberFormatException e) {
            retorno=-1;
            logger.error("Error en metodo deshabilitarEmpleado "+ e.getLocalizedMessage());
        }finally{
            
            return retorno;
        }
    }

    /**
     *
     * @param xmlEmpleado
     * @return
     */
    public long actualizarEmpleado(String xmlEmpleado){
   
        long retorno = 0;
        int retEmpleadoEmptype = 0;
        int retEmployee = 0;
        try {           
            DatosEmpleado empleado = datosEmpleadosObject(xmlEmpleado);
            
           retorno=validateData(empleado);//retorno el resultado de validar ambas cosas
        retorno = valorRetornadoAlBuscarEmailyNombreUsuario(retorno, empleado.getNumeroDocumento(),empleado.getEmail(),empleado.getNombreUsuario());
        
                

                 if(retorno>0){               

                              //selecciono la persona con el tipo de empleado a buscar
                                  Query sqlEmpleadoEmptype =em.createQuery("Select e From Empleados e Where e.idPersona = :idpersona and e.emptype like :emptype");
                                      sqlEmpleadoEmptype.setParameter("idpersona", empleado.getId());
                                      sqlEmpleadoEmptype.setParameter("emptype", empleado.getTipoEmpleado());

                                      //el resultado de la consulta de empleado
                                      retEmpleadoEmptype=sqlEmpleadoEmptype.getResultList().size();


                                          Query slqEmployee=em.createQuery("Select e From Empleados e Where e.idPersona = :idpersona");
                                              slqEmployee.setParameter("idpersona", empleado.getId());
                                      retEmployee=slqEmployee.getResultList().size();


                                          Query sqlParttimeEmployee=em.createQuery("Select e From EmpleadoParttime e Where e.idPersona = :idpersona");
                                                          sqlParttimeEmployee.setParameter("idpersona", empleado.getId());

                                          Query sqlFullTimeEmployee=em.createQuery("Select f From FullTimeEmpleado f Where f.idPersona = :idpersona");
                                                          sqlFullTimeEmployee.setParameter("idpersona", empleado.getId());


                                  if(retEmployee==1){



                                          Query sqlemail = em.createQuery("SELECT p FROM Personas p WHERE p.email = :email");
                                              sqlemail.setParameter("email", empleado.getEmail().toLowerCase());
                                          Query sqlusername=em.createQuery("SELECT e FROM Empleados e WHERE e.nameuser = :nameuser");
                                          sqlusername.setParameter("nameuser", empleado.getNombreUsuario());



                                                  if(empleado.getTipoEmpleado().equals("FULLTIME")&&retEmpleadoEmptype==0){

                                                          Query deletePartTimeEmployee=em.createNativeQuery("DELETE FROM EMPLEADOPARTTIME e WHERE e.ID_PERSONA = idEmpleado");
                                                          deletePartTimeEmployee.setParameter("idEmpleado", empleado.getId());
                                                              deletePartTimeEmployee.executeUpdate();

                                                          Query deleteEntityEmployee=em.createNativeQuery("DELETE FROM EMPLEADOS e WHERE e.ID_PERSONA = idEmpleado");
                                                          deleteEntityEmployee.setParameter("idEmpleado", empleado.getId());
                                                              deleteEntityEmployee.executeUpdate();

                                                          Query deleteEntityPersona=em.createNativeQuery("DELETE FROM PERSONAS p WHERE p.ID_PERSONA = idEmpleado");
                                                          deleteEntityPersona.setParameter("idEmpleado", empleado.getId());
                                                              deleteEntityPersona.executeUpdate();

                                                          em.flush();

//                         
                                                          long nuevoEmployee = addEmpleadoFullTime(xmlEmpleado);
                                                          actualizarReferenciasConNotasdePedido(nuevoEmployee,empleado.getId());
                                                          retorno=nuevoEmployee;

                                                  }else{
                                                      if(empleado.getTipoEmpleado().equals("PARTTIME")&&retEmpleadoEmptype==0){

                                                                  Query deletePartTimeEmployee=em.createNativeQuery("DELETE FROM FULLTIMEEMPLEADO e WHERE e.ID_PERSONA = idEmpleado");
                                                                  deletePartTimeEmployee.setParameter("idEmpleado", empleado.getId());
                                                                  deletePartTimeEmployee.executeUpdate();
                                                                  Query deleteEntityEmployee=em.createNativeQuery("DELETE FROM EMPLEADOS e WHERE e.ID_PERSONA = idEmpleado");
                                                                  deleteEntityEmployee.setParameter("idEmpleado", empleado.getId());
                                                                  deleteEntityEmployee.executeUpdate();
                                                                  Query deleteEntityPersona=em.createNativeQuery("DELETE FROM PERSONAS p WHERE p.ID_PERSONA = idEmpleado");
                                                                  deleteEntityPersona.setParameter("idEmpleado", empleado.getId());
                                                                  deleteEntityPersona.executeUpdate();
                                                                  em.flush();
                                                          long nuevoEmployee = addEmpleadoParttime(xmlEmpleado);
        
                                                          actualizarReferenciasConNotasdePedido(nuevoEmployee, empleado.getId());
                                                          retorno = nuevoEmployee;

                                                      }else{

                                                              if(empleado.getTipoEmpleado().equals("FULLTIME")){
                                                                      FullTimeEmpleado fulltimeEmploy=em.find(FullTimeEmpleado.class, empleado.getId());

                                                                                          fulltimeEmploy.setApellido(empleado.getApellido());

                                                                                          if(sqlemail.getResultList().isEmpty()&&empleado.getEmail().contains("@")) {
                                                                                              fulltimeEmploy.setEmail(empleado.getEmail());
                                                                      } else {
                                                                                              fulltimeEmploy.setEmail("");
                                                                      }

                                                                                          fulltimeEmploy.setGeneros(em.find(Generos.class, empleado.getIdGenero()));

                                                                                          if(sqlusername.getResultList().isEmpty()) {
                                                                                              fulltimeEmploy.setNameuser(empleado.getNombreUsuario());
                                                                      }

                                                                                          fulltimeEmploy.setNombre(empleado.getNombre());

                                                                                          fulltimeEmploy.setSalario(BigDecimal.valueOf(Long.valueOf(empleado.getSalario())));

                                                                                          fulltimeEmploy.setObservaciones(empleado.getObservaciones());
                                                                                          
                                                                                        logger.info("pass a encriptar"+empleado.getPassword());

                                                                                          fulltimeEmploy.setPassword(ProjectHelpers.ClaveSeguridad.encriptar(StringEscapeUtils.escapeXml(empleado.getPassword())));                                                               

                                                                                          fulltimeEmploy.setTipodocumento(em.find(Tiposdocumento.class, empleado.getIdTipoDocumento()));
                                                                                          
                                                                                          em.persist(fulltimeEmploy);
                                                                                           logger.info("passs  encriptado "+fulltimeEmploy.getPassword());
                                                                                          retorno = fulltimeEmploy.getIdPersona();


                                                              }else{

                                                                          EmpleadoParttime empleadoPartime=em.find(EmpleadoParttime.class, empleado.getId());

                                                                                          empleadoPartime.setApellido(empleado.getApellido());

                                                                                          if(sqlemail.getResultList().isEmpty()&&empleado.getEmail().contains("@")) {
                                                                                              empleadoPartime.setEmail(empleado.getEmail());
                                                                          } else {
                                                                                              empleadoPartime.setEmail("");
                                                                          }


                                                                                          empleadoPartime.setGeneros(em.find(Generos.class, empleado.getIdGenero()));


                                                                                          if(sqlusername.getResultList().isEmpty()) {
                                                                                              empleadoPartime.setNameuser(empleado.getNombreUsuario());
                                                                          }


                                                                                          empleadoPartime.setNombre(empleado.getNombre());

                                                                                          empleadoPartime.setSalarioporhora(BigDecimal.valueOf(Long.valueOf(empleado.getSalarioxhora())));

                                                                                          empleadoPartime.setObservaciones(empleado.getObservaciones());

                                                                                          logger.info("passs a encriptar "+empleado.getPassword());
                                                                                          empleadoPartime.setPassword(ProjectHelpers.ClaveSeguridad.encriptar(StringEscapeUtils.escapeXml(empleado.getPassword())));                                                           
                                                                                          

                                                                                          empleadoPartime.setTipodocumento(em.find(Tiposdocumento.class, empleado.getIdTipoDocumento()));

                                                                                          em.persist(empleadoPartime);
                                                                                          logger.info("passs  encriptado "+empleadoPartime.getPassword());
                                                                                      retorno = empleadoPartime.getIdPersona();
                                                              }
                                                      }
                                                  }

                                              em.flush();

                                  




            
            
 
            
                                        }  //end if
                 }
                                  
        } catch (NumberFormatException e) {
            retorno = -1;
            logger.error("Error en metodo actualizarEmpleado en EJBEmpleados "+e.getLocalizedMessage());
       
        }finally{
            
            return retorno;
        }
    }

    private void actualizarReferenciasConNotasdePedido(long nuevoEmployee, int idEmpleado) {
        try {
            Query sql = em.createNativeQuery("SELECT * FROM NOTADEPEDIDO", Notadepedido.class);
            List<Notadepedido>lista = sql.getResultList();
            for(Notadepedido n:lista){
                if(n.getIdUsuarioExpidioNota()==idEmpleado) {
                    n.setIdUsuarioExpidioNota(Integer.valueOf(String.valueOf(nuevoEmployee)));
                }

                     if(n.getIdusuarioAnulado()==idEmpleado) {
                         n.setIdusuarioAnulado(Integer.valueOf(String.valueOf(nuevoEmployee)));
                }

                         if(n.getIdusuarioEntregado()==idEmpleado) {
                             n.setIdusuarioEntregado(Integer.valueOf(String.valueOf(nuevoEmployee)));
                }

                             if(n.getIdusuariocancelo()==idEmpleado) {
                                 n.setIdusuariocancelo(Integer.valueOf(String.valueOf(nuevoEmployee)));
                }
            }
            em.flush();
            logger.info("TODAS LAS REFERENCIAS FUERON ACTUALIZADAS PARA EL EMPLEADO "+nuevoEmployee);
        } catch (NumberFormatException e) {
            logger.error("error al actulizar referencias de nota de pedidos de empleado "+e.getLocalizedMessage());
        }
    }
   
    /**
     *
     * @return
     */
    public String desencriptarClavesYNombresUsuario(){
    Empleados emp=em.find(Empleados.class, 38);
        
    String clave = ProjectHelpers.ClaveSeguridad.decriptar(emp.getPassword());
    
    return clave;
    }

    private long procesarDatosEmpleadoAdd(DatosEmpleado empleado) {
        
        long retorno=0;
        try {
            
        
              
                retorno=validateData(empleado);//retorno el resultado de validar ambas cosas
                retorno =valorRetornadoAlBuscarEmailyNombreUsuario(retorno,empleado.getNumeroDocumento(),empleado.getEmail(),empleado.getNombreUsuario());  
                    GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
                                   if(retorno==0){
                                       if(empleado.getTipoEmpleado().equals("FULLTIME")) {
                                           retorno=addFullTimeEmpleado(empleado,gc);
                                       } else {
                                           retorno=addPartTimeEmpleado(empleado,gc);
                                       }
                                    
                                   }else {
                                       retorno=numeroaRetornar(retorno);//retorno de buscarPersonaEmailNameUSer
                }
            
      
      

        } catch (Exception e) {
            logger.error("Error en metodo procesarDatosEmpleadoAdd "+e.getLocalizedMessage());
        }finally{
         return retorno;
        }
    }

    private long addFullTimeEmpleado(DatosEmpleado empleado,GregorianCalendar gc) {
        long retorno =0;
        try {
                 FullTimeEmpleado empfulltime = new FullTimeEmpleado();
                                         empfulltime.setApellido(empleado.getApellido().toUpperCase());
                                         empfulltime.setEmail(empleado.getEmail().toLowerCase());
                                         empfulltime.setNombre(empleado.getNombre().toUpperCase());
                                         empfulltime.setNameuser(StringEscapeUtils.escapeXml(empleado.getNombreUsuario()));
                                       
                                         try {


                                                empfulltime.setPassword(ProjectHelpers.ClaveSeguridad.encriptar(StringEscapeUtils.escapeXml(empleado.getPassword())));


                                       } catch (Exception e) {
                                           retorno=-10;
                                           logger.error(e.getLocalizedMessage());
                                       }

                                         empfulltime.setGeneros(em.find(Generos.class, empleado.getIdGenero()));
                                         empfulltime.setEstado((short)1);
                                         empfulltime.setFechacarga(gc.getTime());
                                         empfulltime.setEmptype("FULLTIME");
                                         empfulltime.setObservaciones(empleado.getObservaciones());
                                         empfulltime.setTipodocumento(em.find(Tiposdocumento.class, empleado.getIdTipoDocumento()));
                                         empfulltime.setNrodocumento(empleado.getNumeroDocumento());
                                         empfulltime.setSalario(BigDecimal.valueOf(Float.valueOf(empleado.getSalario())));
                                         em.persist(empfulltime);
                                         em.flush();
                                       retorno = empfulltime.getIdPersona();
            
        } catch (NumberFormatException e) {
            logger.error("Error en metodo addFullTimeEmpleado "+e.getLocalizedMessage());
        }finally{
             return retorno;
        }
        
       
         
    }

    private long addPartTimeEmpleado(DatosEmpleado empleado, GregorianCalendar gc) {
        long retorno =0;
        try {
              EmpleadoParttime empparttime = new EmpleadoParttime();
                                                    empparttime.setApellido(empleado.getApellido().toUpperCase());
                                                    empparttime.setNameuser(StringEscapeUtils.escapeXml(empleado.getNombreUsuario()));
                                                    
                                                    
                                                    empparttime.setNombre(empleado.getNombre().toUpperCase());
                                                    empparttime.setFechacarga(gc.getTime());
                                                    try {


                                                            empparttime.setPassword(ProjectHelpers.ClaveSeguridad.encriptar(StringEscapeUtils.escapeXml(empleado.getPassword())));

                                                    } catch (Exception e) {
                                                        retorno=-10;
                                                        logger.error(e.getLocalizedMessage());
                                                    }

                                                    empparttime.setEmptype("PARTTIME");
                                                    empparttime.setTipodocumento(em.find(Tiposdocumento.class, empleado.getIdTipoDocumento()));
                                                    empparttime.setNrodocumento(empleado.getNumeroDocumento());
                                                    empparttime.setGeneros(em.find(Generos.class, empleado.getIdGenero()));
                                                    empparttime.setEstado((short)1);
                                                    empparttime.setEmail(empleado.getEmail());
                                                    empparttime.setSalarioporhora(BigDecimal.valueOf(Float.valueOf(empleado.getSalarioxhora())));
                                                    em.persist(empparttime);
                                                    em.flush();
                                                    retorno = empparttime.getIdPersona();
            
        } catch (NumberFormatException e) {
            logger.error("error en metodo addPartTimeEmpleado "+e.getLocalizedMessage());
        }finally{
           return retorno;
        }
    }

     
    private long validateData(DatosEmpleado empleado) {
        long retorno=0;
        try {
            String numeroDocumento = String.valueOf(empleado.getNumeroDocumento());
            
            if(ProjectHelpers.NumeroDocumentoValidator.validate(numeroDocumento)){
                        
                        if(!empleado.getNombre().isEmpty()&&ProjectHelpers.NombreyApellidoValidator.validate(empleado.getNombre())){
                            if(!empleado.getApellido().isEmpty()&&ProjectHelpers.NombreyApellidoValidator.validate(empleado.getApellido())){
                                    if(empleado.getPassword().equals(empleado.getPasswordre())){
                                                
                                                  
                                              if(!ProjectHelpers.NombreUsuarioValidator.validate(StringEscapeUtils.escapeXml(empleado.getNombreUsuario()))) {
                                                  retorno=-12;
                                              }

                                              if(!ProjectHelpers.PasswordValidator.validate(StringEscapeUtils.escapeXml(empleado.getPassword()))&&empleado.getPassword().equals(empleado.getPasswordre())) {
                                                  retorno=-11;
                                              }

                                    }else {
                                        retorno=-13;
                                    }
                            }else {
                                retorno =-15;
                            }
                    }else {
                            retorno = -14;
                        }
            }else {
                retorno =-16;
            }
            
            
        } catch (Exception e) {
            logger.error("Error en metodo validateData "+e.getLocalizedMessage());
        }finally{
        return retorno;
        }
    }
/**
 * 
 * @param xmlEmpleado datos del empleado con estructura xml
 * @return retorna un objeto del tipo DatosEmpleado
 */
    private DatosEmpleado datosEmpleadosObject(String xmlEmpleado) {
        DatosEmpleado datosEmpleado = null;
        try {
            //Objeto xstrem encargado de convertir un XML en su correspondiente Objeto
             XStream xstream = new XStream();
            
            xstream.alias("Empleado", DatosEmpleado.class);   
            
            datosEmpleado =(DatosEmpleado) xstream.fromXML(xmlEmpleado);
            
        } catch (Exception e) {
            logger.error("error en metodo datosEmpleadosObject "+e.getLocalizedMessage());
        }finally{
             return datosEmpleado;            
        }
    }

    private long valorRetornadoAlBuscarEmailyNombreUsuario(long indice,Integer numeroDocumento, String email, String nombreUsuario) {
        long retorno =0;
        try {
             switch((int)indice){
            case -11:break;//si no valida el password
            case -12:break;    //si no valida el nombre de usuario
            case -13:break;    
            case -14:break;//SI NO valida el nombre del empleado
            case -15:break; //si no valida el apellido
            case -16:break;//so mp valida el numero de documento    
            default:{
                retorno = buscarPersonaEmailAndNameUser(numeroDocumento,email,nombreUsuario);
              
            
            }
        }
            
            
        } catch (Exception e) {
            retorno =-1;
            logger.error("error en metodo valorRetornadoAlBuscarEmailyNombreUsuario ");
        }finally{
            return retorno;
        }
    }
         
       
    
    
}