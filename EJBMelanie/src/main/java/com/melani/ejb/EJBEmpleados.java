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
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.math.BigDecimal;
import java.text.DateFormat;
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
                    logger.error("Error en metodo addEmpleadoFullTime "+e.getMessage());
                }  catch (Exception e) {
                    retorno = -1;
                        logger.error("Error en metodo addEmpleadoFullTime "+e.getMessage());
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
            logger.error("Error en metodo addEmpleadoParttime "+e.getMessage());
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
       String xml="<?xml version='1.0' encoding='utf-8'?>\n"+"<Lista>\n";
        try {
            Query consulta = em.createQuery("SELECT e FROM Empleados e order by e.idPersona desc");            
            List<Empleados>lista = consulta.getResultList();
            if(lista.size()>0){
                    StringBuilder xmlLoop = new StringBuilder(10);
                for (Empleados empleados : lista) {                    
                            xmlLoop.append("<item>\n");
                            xmlLoop.append("<id>").append(empleados.getIdPersona()).append("</id>\n")
                               .append("<nombre>").append(StringEscapeUtils.escapeXml10(empleados.getNombre())).append("</nombre>\n");
                            xmlLoop.append("<apellido>").append(StringEscapeUtils.escapeXml10(empleados.getApellido())).append("</apellido>\n");
                            xmlLoop.append("<genero>").append(empleados.getGeneros().getIdGenero()).append("</genero>\n");
                            xmlLoop.append("<tipodocu>").append(empleados.getTipodocumento().getId()).append("</tipodocu>\n");
                            xmlLoop.append("<documento>").append(empleados.getNrodocumento()).append("</documento>\n");
                            xmlLoop.append("<observaciones>").append(StringEscapeUtils.escapeXml10(empleados.getObservaciones()))
                               .append("</observaciones>\n");
                            xmlLoop.append("<email>").append(empleados.getEmail()).append("</email>\n");
                            xmlLoop.append(obtenerEmpleado(empleados));
                            xmlLoop.append(empleados.toXMLEmpleado());
                            xmlLoop.append("<clave>").append(ProjectHelpers.ClaveSeguridad.decriptar(empleados.getPassword())).append("</clave>");
                            xmlLoop.append("</item>\n");
                            
                }
                xml+=xmlLoop;
            }else {
                xml+="<result>Lista Vacia</result>\n";
            }
        } catch (Exception e) {
            logger.error("error al obtener lista de empleados "+e.getMessage());
            xml+="<error>"+e.getLocalizedMessage()+"</error>\n";
        }finally{
            return xml+"</Lista>\n";
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
            StringBuilder sb =new StringBuilder(32);
                Empleados empleadoDesabilitado = em.find(Empleados.class, idEmpleado);
                Empleados empleadoDesabilito = em.find(Empleados.class, idEmpleadoDesabilito);
                empleadoDesabilitado.setEstado((short)0);
                    sb.append(empleadoDesabilito.getNombre());
                    sb.append(" ");
                    sb.append(empleadoDesabilito.getApellido());
                    
             retorno=Integer.valueOf(String.valueOf(empleadoDesabilitado.getIdPersona()));
        } catch (NumberFormatException e) {
            retorno=-1;
            logger.error("Error en metodo deshabilitarEmpleado "+e.getMessage());
        }finally{
            return retorno;
        }
    }
    private long buscarEmpleadoEmailAndNameUser(int numerodocu,String email,String nameuser) {
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
            logger.error("Error en metodo buscar persona and email ");
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
            
                Empleados empleadoHabilitado = em.find(Empleados.class, idEmpleado);
                Empleados empleadohabilito = em.find(Empleados.class, idEmpleadohabilito);
                empleadoHabilitado.setEstado((short)1);
                    
                    
                    
             retorno=Integer.valueOf(String.valueOf(empleadohabilito.getIdPersona()));
             logger.warn(new StringBuilder("USUARIO HABILITÓ ").append(empleadoHabilitado.getNombre()).append(" A USUARIO NUEVO ").append(empleadohabilito.getNombre()).append(" EL DÍA ").append(DateFormat.getInstance().format(gc.getTime())));
        } catch (NumberFormatException e) {
            retorno=-1;
            logger.error("Error en metodo deshabilitarEmpleado "+e.getMessage());
        }finally{
            
            return retorno;
        }
    }

    /**
     *
     * @param xmlEmpleado
     * @return un numero indicando si se logro actualizar los valores del empleado ya sea cambios menores o cambio de empleado parttime a fulltime o viceversa
     */
    public long actualizarEmpleado(String xmlEmpleado){
   
        long retorno = 0;
        
        try {        
           
            DatosEmpleado empleado = datosEmpleadosObject(xmlEmpleado);//convierto a objeto
           
           retorno=validateData(empleado);//retorno el resultado de validar datos nombre y apellido, password
           
           if(retorno==0){
           
                        retorno = valorRetornadoAlBuscarEmailyNombreUsuario(retorno, empleado.getNumeroDocumento(),
                                empleado.getEmail(),empleado.getNombreUsuario());
                        
                            if(retorno>0){
                   
             
                                           retorno = processEmployee(empleado,xmlEmpleado);
                         }
           }
                                  
        } catch (NumberFormatException e) {
            retorno = -1;
            logger.error("Error en metodo actualizarEmpleado en EJBEmpleados "+e.getMessage());
       
        }finally{
            return retorno;
        }
    }

    private void actualizarReferenciasConNotasdePedido(long nuevoEmployee, int idEmpleado) {
        try {
            Query sql = em.createNamedQuery("Notadepedido.findAll", Notadepedido.class);
            List<Notadepedido>lista = sql.getResultList();
            for(Notadepedido n:lista){
                if(n.getIdUsuarioExpidioNota()==idEmpleado) {
                    n.setIdUsuarioExpidioNota(Long.valueOf(String.valueOf(nuevoEmployee)));
                }

                     if(n.getIdusuarioAnulado()==idEmpleado) {
                         n.setIdusuarioAnulado(Long.valueOf(String.valueOf(nuevoEmployee)));
                }

                         if(n.getIdusuarioEntregado()==idEmpleado) {
                             n.setIdusuarioEntregado(Long.valueOf(String.valueOf(nuevoEmployee)));
                }

                             if(n.getIdusuariocancelo()==idEmpleado) {
                                 n.setIdusuariocancelo(Long.valueOf(String.valueOf(nuevoEmployee)));
                }
            }
            em.flush();
            
        } catch (NumberFormatException e) {
            logger.error("error al actulizar referencias de nota de pedidos de empleado "+e.getMessage());
        }
    }
   
    /**
     *
     * @return
     */
//    public String desencriptarClavesYNombresUsuario(){
//    Empleados emp=em.find(Empleados.class, 38);        
//    String clave = ProjectHelpers.ClaveSeguridad.decriptar(emp.getPassword());
//    
//    return clave;
//    }

    private long procesarDatosEmpleadoAdd(DatosEmpleado empleado) {        
        long retorno=0;
        try {
              retorno=validateData(empleado);//retorno el resultado de validar ambas cosas nombre, apellido, password
                if(retorno==0){
                        retorno =valorRetornadoAlBuscarEmailyNombreUsuario(retorno,empleado.getNumeroDocumento(),empleado.getEmail(),empleado.getNombreUsuario());  
                            
                                           if(retorno==0){
                                                    if(empleado.getTipoEmpleado().equals("FULLTIME")) {
                                                        retorno=addFullTimeEmpleado(empleado);
                                                    } else {
                                                        retorno=addPartTimeEmpleado(empleado);
                                                    }
                                            }else {
                                                retorno=numeroaRetornar(retorno);//retorno de buscarPersonaEmailNameUSer
                        }
                }
            
      
      

        } catch (Exception e) {
            retorno =-1;
            logger.error("Error en metodo procesarDatosEmpleadoAdd "+e.getMessage());
        }finally{
         return retorno;
        }
    }

    private long addFullTimeEmpleado(DatosEmpleado empleado) {
        long retorno =0;
        try {
            GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
                 FullTimeEmpleado empfulltime = new FullTimeEmpleado();
                                         empfulltime.setApellido(empleado.getApellido().toUpperCase());
                                         if(!empleado.getEmail().isEmpty())
                                            empfulltime.setEmail(empleado.getEmail().toLowerCase());
                                         empfulltime.setNombre(empleado.getNombre().toUpperCase());
                                         empfulltime.setNameuser(StringEscapeUtils.escapeXml10(empleado.getNombreUsuario()));
                                       try {
                                            empfulltime.setPassword(ProjectHelpers.ClaveSeguridad.encriptar(StringEscapeUtils.escapeXml10(empleado.getPassword())));
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
                                         if(empleado.getSalario().length()>0){
                                            empfulltime.setSalario(BigDecimal.valueOf(Float.valueOf(empleado.getSalario())));
                                         }else{
                                            empfulltime.setSalario(BigDecimal.valueOf(Float.valueOf("0.0"))); 
                                         }
                                         em.persist(empfulltime);
                                         
                                       retorno = empfulltime.getIdPersona();
            
        } catch (NumberFormatException e) {
            logger.error("Error en metodo addFullTimeEmpleado "+e.getMessage());
        }finally{
             return retorno;
        }
    }

    private long addPartTimeEmpleado(DatosEmpleado empleado) {
        long retorno =0;
        try {
            GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
              EmpleadoParttime empparttime = new EmpleadoParttime();
                                                    empparttime.setApellido(empleado.getApellido().toUpperCase());
                                                    empparttime.setNameuser(StringEscapeUtils.escapeXml10(empleado.getNombreUsuario()));
                                                    
                                                    
                                                    empparttime.setNombre(empleado.getNombre().toUpperCase());
                                                    empparttime.setFechacarga(gc.getTime());
                                                    try {


                                                            empparttime.setPassword(ProjectHelpers.ClaveSeguridad.encriptar(StringEscapeUtils.escapeXml10(empleado.getPassword())));

                                                    } catch (Exception e) {
                                                        retorno=-10;
                                                        logger.error(e.getLocalizedMessage());
                                                    }

                                                    empparttime.setEmptype("PARTTIME");
                                                    empparttime.setTipodocumento(em.find(Tiposdocumento.class, empleado.getIdTipoDocumento()));
                                                    empparttime.setNrodocumento(empleado.getNumeroDocumento());
                                                    empparttime.setGeneros(em.find(Generos.class, empleado.getIdGenero()));
                                                    empparttime.setEstado((short)1);
                                                    if(!empleado.getEmail().isEmpty())
                                                         empparttime.setEmail(empleado.getEmail());
                                                    empparttime.setSalarioporhora(BigDecimal.valueOf(Float.valueOf(empleado.getSalarioxhora())));
                                                    em.persist(empparttime);
                                                    
                                                    retorno = empparttime.getIdPersona();
            
        } catch (NumberFormatException e) {
            logger.error("error en metodo addPartTimeEmpleado "+e.getMessage());
        }finally{
           return retorno;
        }
    }

     
    private long validateData(DatosEmpleado empleado) {
        long retorno=0;
        try {
            
            String numeroDocumento = String.valueOf(empleado.getNumeroDocumento());
            System.out.println("4");
            if(!ProjectHelpers.NumeroDocumentoValidator.validate(numeroDocumento)){
                retorno =-16;
            }else {
                System.out.println("3");
                if(!empleado.getNombre().isEmpty()&&ProjectHelpers.NombreyApellidoValidator.validate(empleado.getNombre())){
                    if(!empleado.getApellido().isEmpty()&&ProjectHelpers.NombreyApellidoValidator.validate(empleado.getApellido())){
                        if(empleado.getPassword().equals(empleado.getPasswordre())){
                            
                            
                            if(!ProjectHelpers.NombreUsuarioValidator.validate(StringEscapeUtils.escapeXml10(empleado.getNombreUsuario()))) {
                                retorno=-12;
                            }
                            
                            if(!ProjectHelpers.PasswordValidator.validate(StringEscapeUtils.escapeXml10(empleado.getPassword())) && (empleado.getPassword().equals(empleado.getPasswordre()))) {
                                retorno=-11;
                            }else{
                                
                                String encriptedText = ProjectHelpers.ClaveSeguridad.encriptar(empleado.getPassword());
                                
                                String decriptedText = ProjectHelpers.ClaveSeguridad.decriptar(encriptedText);
                                
                                if(!ProjectHelpers.PasswordValidator.validate(decriptedText)){
                                    retorno = -17;
                                }
                            }
                            
                        }else{
                            retorno =-13;
                        }
                        
                        
                    }else {
                        retorno =-15;
                    }
                }else {
                    retorno = -14;
                }
            }
            
            
        } catch (Exception e) {
            retorno =-1;
            
            logger.error("Error en metodo validateData "+e.getMessage());
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
            
             XStream xstream = new XStream(new StaxDriver());
            
            xstream.alias("Empleado", DatosEmpleado.class);   
            
            
                datosEmpleado =(DatosEmpleado) xstream.fromXML(xmlEmpleado);
            
                
            
        } catch (Exception e) {
            logger.error("error en metodo datosEmpleadosObject "+e.getMessage());
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
                retorno = buscarEmpleadoEmailAndNameUser(numeroDocumento,email,nombreUsuario);
              
            
            }
        }
            
            
        } catch (Exception e) {
            retorno =-1;
            logger.error("error en metodo valorRetornadoAlBuscarEmailyNombreUsuario "+e.getMessage());
        }finally{
            return retorno;
        }
    }

    @Override
    public String checkPassEmployee(long idEmpleado,String pass) {
        String passKey = "";
        String result = "";
        try {
            Query consulta = em.createNamedQuery("Empleados.chkpass");
            consulta.setParameter("1", idEmpleado);
            List<Empleados>empleado = consulta.getResultList();
            
            if(!empleado.isEmpty()){
                for (Empleados empleados : empleado) {
                     passKey = ProjectHelpers.ClaveSeguridad.decriptar(empleados.getPassword());
                    
                }
                result = String.valueOf(passKey.equals(pass));
            }else{
                result = "Empleado no encontrado";
            }
                
        } catch (Exception e) {
            logger.error("Error en metodo checkPassEmployee "+e.getLocalizedMessage());
        }finally{
            return  result;
        }
        
           
       
    }

    private void processEmployee(DatosEmpleado empleado) {
       
    }

    private long processEmployee(DatosEmpleado empleado, String xmlEmpleado) {
         int retEmpleadoEmptype = 0;
        int retEmployee = 0;
        long retorno = 0;
        //selecciono la persona con el tipo de empleado a buscar
                                               Query sqlEmpleadoEmptype =em.createQuery("Select e From Empleados e "
                                                       + "Where e.idPersona = :idpersona and e.emptype like :emptype");
                                                   sqlEmpleadoEmptype.setParameter("idpersona",(long) empleado.getId());
                                                   sqlEmpleadoEmptype.setParameter("emptype", empleado.getTipoEmpleado());

                                                   //el resultado de la consulta de empleado
                                                   retEmpleadoEmptype=sqlEmpleadoEmptype.getResultList().size();


                                                       Query slqEmployee=em.createQuery("Select e From Empleados e Where e.idPersona = :idpersona");
                                                           slqEmployee.setParameter("idpersona",(long) empleado.getId());
                                                   retEmployee=slqEmployee.getResultList().size();


                                                       Query sqlParttimeEmployee=em.createQuery("Select e From EmpleadoParttime e Where "
                                                               + "e.idPersona = :idpersona");
                                                                       sqlParttimeEmployee.setParameter("idpersona",(long) empleado.getId());

                                                       Query sqlFullTimeEmployee=em.createQuery("Select f From FullTimeEmpleado f Where "
                                                               + "f.idPersona = :idpersona");
                                                                       sqlFullTimeEmployee.setParameter("idpersona", (long)empleado.getId());


                     if(retEmployee==1){
                                          Query sqlemail = em.createQuery("SELECT p FROM Personas p WHERE p.email = :email");
                                                 sqlemail.setParameter("email", empleado.getEmail());
                                                    Query sqlusername=em.createQuery("SELECT e FROM Empleados e WHERE e.nameuser = :nameuser");
                                                       sqlusername.setParameter("nameuser", empleado.getNombreUsuario());



                                                               if(empleado.getTipoEmpleado().equals("FULLTIME")&&retEmpleadoEmptype==0){
                                                                    
                                                                       Query deletePartTimeEmployee=em.createNamedQuery("EmpleadoParttime.deleteById");
                                                                           deletePartTimeEmployee.setParameter("idPersona", (long)empleado.getId());
                                                                           deletePartTimeEmployee.executeUpdate();

                                                                       Query deleteEntityEmployee=em.createNamedQuery("Empleados.deleteById");
                                                                       deleteEntityEmployee.setParameter("idPersona",(long) empleado.getId());
                                                                           deleteEntityEmployee.executeUpdate();

                                                                       Query deleteEntityPersona=em.createNamedQuery("Personas.deleteById");
                                                                       deleteEntityPersona.setParameter("idPersona", (long)empleado.getId());
                                                                           deleteEntityPersona.executeUpdate();

                                                                       em.flush();

             //                         
                                                                       long nuevoEmployee = addEmpleadoFullTime(xmlEmpleado);
                                                                       actualizarReferenciasConNotasdePedido(nuevoEmployee,empleado.getId());
                                                                       retorno=nuevoEmployee;

                                                               }else{
                                                                   if(empleado.getTipoEmpleado().equals("PARTTIME")&&retEmpleadoEmptype==0){
             
                                                                               Query deletePartTimeEmployee=em.createNamedQuery("EmpleadoParttime.deleteById");
                                                                               deletePartTimeEmployee.setParameter("idPersona", (long)empleado.getId());
                                                                               deletePartTimeEmployee.executeUpdate();
                                                                               Query deleteEntityEmployee=em.createNamedQuery("Empleados.deleteById");
                                                                               deleteEntityEmployee.setParameter("idPersona",(long) empleado.getId());
                                                                               deleteEntityEmployee.executeUpdate();
                                                                               Query deleteEntityPersona=em.createNamedQuery("Personas.deleteById");
                                                                               deleteEntityPersona.setParameter("idPersona",(long) empleado.getId());
                                                                               deleteEntityPersona.executeUpdate();
                                                                               em.flush();
                                                                       long nuevoEmployee = addEmpleadoParttime(xmlEmpleado);

                                                                       actualizarReferenciasConNotasdePedido(nuevoEmployee, empleado.getId());
                                                                       retorno = nuevoEmployee;

                                                                   }else{
  
                                                                           if(empleado.getTipoEmpleado().equals("FULLTIME")){
                                                                                   FullTimeEmpleado fulltimeEmploy=em.find(FullTimeEmpleado.class,(long) empleado.getId());

                                                                                                       fulltimeEmploy.setApellido(empleado.getApellido());

                                                                                                       if(sqlemail.getResultList().isEmpty()&&empleado.getEmail().contains("@")) {
                                                                                                           fulltimeEmploy.setEmail(empleado.getEmail());
                                                                                                       }

                                                                                                       fulltimeEmploy.setGeneros(em.find(Generos.class, empleado.getIdGenero()));

                                                                                                       if(sqlusername.getResultList().isEmpty()) {
                                                                                                           fulltimeEmploy.setNameuser(empleado.getNombreUsuario());
                                                                                                       }

                                                                                                       fulltimeEmploy.setNombre(empleado.getNombre());

                                                                                                       fulltimeEmploy.setSalario(BigDecimal.valueOf(Long.valueOf(empleado.getSalario())));

                                                                                                       fulltimeEmploy.setObservaciones(empleado.getObservaciones());

                                                                                                     

                                                                                                       fulltimeEmploy.setPassword(ProjectHelpers.ClaveSeguridad.encriptar(StringEscapeUtils.escapeXml11(empleado.getPassword())));                                                               

                                                                                                       fulltimeEmploy.setTipodocumento(em.find(Tiposdocumento.class, empleado.getIdTipoDocumento()));

                                                                                                       em.persist(fulltimeEmploy);
                                                                                                     
                                                                                                       retorno = fulltimeEmploy.getIdPersona();


                                                                           }else{
                                                                                 
                                                                                 EmpleadoParttime empleadoPartime = null;
                                                                                 try {
                                                                                        empleadoPartime=em.find(EmpleadoParttime.class,(long) empleado.getId());
                                                                               } catch (Exception e) {
                                                                                       logger.error("Error al buscar empleado partime "+e.getMessage());
                                                                               }
                                                                                   
                                                                                       
                                                                                                       empleadoPartime.setApellido(empleado.getApellido());
                                                                                       
                                                                                                       if(sqlemail.getResultList().isEmpty()&&empleado.getEmail().contains("@")) {
                                                                                                           empleadoPartime.setEmail(empleado.getEmail());
                                                                                                       }
                                                                                                       

                                                                                                       empleadoPartime.setGeneros(em.find(Generos.class, empleado.getIdGenero()));
                                                                                                       

                                                                                                       if(sqlusername.getResultList().isEmpty()) {
                                                                                                           empleadoPartime.setNameuser(empleado.getNombreUsuario());
                                                                                                       }

                                                                                                       
                                                                                                       empleadoPartime.setNombre(empleado.getNombre());
                                                                                                       
                                                                                                       empleadoPartime.setSalarioporhora(BigDecimal.valueOf(Long.valueOf(empleado.getSalarioxhora())));
                                                                                                       
                                                                                                       empleadoPartime.setObservaciones(empleado.getObservaciones());
                                                                                                       
                                                                                                       
                                                                                                       
                                                                                                       empleadoPartime.setPassword(ProjectHelpers.ClaveSeguridad.encriptar(StringEscapeUtils.escapeXml10(empleado.getPassword())));                                                           

                                                                                                       
                                                                                                       empleadoPartime.setTipodocumento(em.find(Tiposdocumento.class, empleado.getIdTipoDocumento()));

                                                                                                       em.persist(empleadoPartime);
                                                                                                       
                                                                                                       
                                                                                                   retorno = empleadoPartime.getIdPersona();
                                                                           }
                                                                   }
                                                               }

                                                      em.flush();
                                                }  //end if
         return retorno;
                                               
    }
         
       
    
    
}
