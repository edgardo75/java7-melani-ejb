/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Clientes;
import com.melani.entity.Domicilios;
import com.melani.entity.Generos;
import com.melani.entity.HistoricoDatosClientes;
import com.melani.entity.Personas;
import com.melani.entity.PersonasDomicilios;
import com.melani.entity.Personastelefonos;
import com.melani.entity.Telefonos;
import com.melani.entity.TelefonosPK;
import com.melani.entity.Tiposdocumento;
import com.melani.utils.ClienteDomicilioTelefono;
import com.melani.utils.DatosCliente;
import com.melani.utils.DatosDomicilios;
import com.melani.utils.DatosTelefonos;
import com.melani.utils.ListaTelefonos;
import com.melani.utils.ProjectHelpers;
import com.thoughtworks.xstream.XStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
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
@Stateless(name="ejb/EJBClientes")
@WebService(serviceName="ServiceClientes",name="ClientesWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBClientes implements EJBClientesRemote {
    private static final Logger logger = Logger.getLogger(EJBClientes.class);
    @PersistenceContext(unitName="EJBMelaniPU2")
    private EntityManager em;
    @EJB
    EJBDomiciliosRemote ejbdomici;
    @EJB
    EJBTelefonosRemote ejbtele;
    @EJB
    EJBClienteDomicilioRemote ejbclidom;
    @EJB
    EJBClienteTelefonoRemote ejbclitel;
    private long chequear__email_numDoc=0L;
    //----------------------------------------------------------------------------    
//--------------------------------------------------------------------------------
    /**
     *
     * @param xmlClienteDomicilioTelefono cadena con todos los datos del cliente, domicilio y telefono
     * @return devuelve un número si es mayor a cero la operación se ejecuto correctamente
     */
        public long addCliente(String xmlClienteDomicilioTelefono) {
        long retorno =0L;
        Personas persona = null;
        try {
            //----------------------------------------------------------------------------------------
            //libreria Xstream que transforma un Objeto a XML o viceversa          
            ClienteDomicilioTelefono getAllDatos = parsear_a_objetos(xmlClienteDomicilioTelefono);
             DatosCliente datosClientePersonales = getAllDatos.getCliente();
           //------------------------------------------------------------------------------------------
             //valido el nombre y el apellido 
             
             if(ProjectHelpers.NombreyApellidoValidator.validate(datosClientePersonales.getNombre())){
             
                 if(ProjectHelpers.NombreyApellidoValidator.validate(datosClientePersonales.getApellido())){
             
                    String numeroDocu = String.valueOf(datosClientePersonales.getNrodocu());
             
                     if(ProjectHelpers.NumeroDocumentoValidator.validate(numeroDocu)){
                        ///++++++++++++++++++++++++++++++++++++la persona si existene en la base de datos
             
                         long idPersona = existePersona(datosClientePersonales.getNrodocu());
                         
                                if(idPersona>0) {
                                    persona = em.find(Personas.class, idPersona);
                                }
             
                                    //Verifico que venga un email para ver si existe en la base de datos
                                    if(!datosClientePersonales.getEmail().isEmpty()){
             
                                        //verifico que venga validado correctamente el email junto con el numero de documento y si tambien lo encontro o no en la base de datos
                                           chequear__email_numDoc = chequearEmail(datosClientePersonales.getEmail(),datosClientePersonales.getNrodocu());
                                           
                                           switch((int)chequear__email_numDoc){              
                                               
                                               case -7:{logger.error("Error en metodo chequear email");
                                               retorno = chequear__email_numDoc;
                                               break;
                                               }
                                               case -8:{logger.error("Email encontrado en metodo chequearEmail");
                                               retorno = chequear__email_numDoc;
                                                break;
                                               }
                                               case -11:{logger.error("Email no válido");
                                               retorno = chequear__email_numDoc;
                                                break;
                                               }

                                               default:{
                                                   //*****************************************************************************++++
                                           

                                                       if(persona!=null){
                                           
                                                           retorno = buscarPersonaSiEsCliente(persona, datosClientePersonales, getAllDatos, xmlClienteDomicilioTelefono);
                                                       }else{
                                                          
                                                           retorno = agregarTodosLosDatosCliente(getAllDatos,datosClientePersonales,xmlClienteDomicilioTelefono);
                                                       }
                                                       //**********************************************************************************
                                                      //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                                      break;
                                                  }
                                           }
                                    }else{
                                       if(persona!=null) {
                                           retorno = buscarPersonaSiEsCliente(persona, datosClientePersonales, getAllDatos, xmlClienteDomicilioTelefono);
                                       } else {
                                           retorno = agregarTodosLosDatosCliente(getAllDatos,datosClientePersonales,xmlClienteDomicilioTelefono);
                                       }
                                    }
                     }else{
                          retorno = -14;
                        logger.error("Numero de documento no válido");
                     }
                 }else{
                     retorno = -13;
                    logger.error("Apellido no válido");
                 }
             }else{
                 retorno = -12;
                 logger.error("Nombre no válido");
             }
        } catch (Exception e) {
            retorno = -1;
            
            logger.error("Error en Metodo addCliente, EJBClientes, verifique "+e);
        }finally{            
            return retorno;
        }
    }
        /**
         * 
         * @param persona objeto persona entidad
         * @param datosClientePersonales objeto cliente utilidad
         * @param todosDatos objeto cliente, domicilio, telefono utilidad
         * @param xmlClienteDomicilioTelefono datos completo de cliente recibidos
         * @return devuelve resultado de la operación mayor a cero hubo exito, menor, ocurrio un error
         *  cero no paso nada
         */
    private long buscarPersonaSiEsCliente(Personas persona,DatosCliente datosClientePersonales,ClienteDomicilioTelefono todosDatos,String xmlClienteDomicilioTelefono){
        long retorno=0;
        try {
           //verifico si la persona es un cliente usando el discriminador de columnas de la clase Personas                           
           if(persona.getPertype().equals("CLI")) {
               retorno=actualizarDatos(todosDatos,datosClientePersonales,xmlClienteDomicilioTelefono,persona.getIdPersona());
           } else {
               retorno=-9;
           }
        } catch (Exception e) {
            logger.error("Error en metodo buscarPersonaSiEsCliente "+e.getLocalizedMessage());
        }finally{
            return retorno; 
        }
    }
    /**
     * 
     * @param nrodocu numero de documento de la persona
     * @return devuelve el id de la persona
     */
//---------------------------------------------------------------------------------------------
    private long existePersona(int nrodocu) {
        long retorno =0;
        try {
            Query consulta = em.createNamedQuery("Personas.findByNrodocumento");
                consulta.setParameter("nrodocumento", nrodocu);
                        List<Personas>lista = consulta.getResultList();
                        if(lista.size()==1)      {
                                for (Personas personas : lista) {
                                    retorno=personas.getIdPersona();
                                }
                        }
        } catch (Exception e) {
            retorno = -1;
            logger.error("Error al Buscar Cliente, metodo existePersona, EJBCliente "+e.getLocalizedMessage());
        }finally{                
                return retorno;
        }
    }
    /**
     * 
     * @param todosDatos objeto cliente, domicilio, telefono
     * @param datosClientePersonales objeto cliente
     * @param xmlClienteDomicilioTelefono datos de cliente completo 
     * @return devuelve el id del cliente
     */
    private long agregarTodosLosDatosCliente(ClienteDomicilioTelefono todosDatos, DatosCliente datosClientePersonales,String xmlClienteDomicilioTelefono) {
        long retorno =0;        
        try {
            GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
            //-----------------------------------------------------------------------------------
            
          //------------------------------------------------Proceso el Cliente -------------------------------
                    Clientes cliente = new Clientes();                
                                cliente.setApellido(datosClientePersonales.getApellido().toUpperCase());                
                                cliente.setEmail(datosClientePersonales.getEmail());                
                                cliente.setFechaCarga(calendario.getTime());                
                                cliente.setGeneros(em.find(Generos.class, datosClientePersonales.getGeneros().getIdgenero()));                
                                cliente.setNombre(datosClientePersonales.getNombre().toUpperCase());                  
                                cliente.setNrodocumento(datosClientePersonales.getNrodocu());                
                                cliente.setObservaciones(datosClientePersonales.getObservaciones().toUpperCase());                
                                cliente.setTipodocumento(em.find(Tiposdocumento.class, datosClientePersonales.getIdtipodocu()));                
                                cliente.setTotalCompras(BigDecimal.valueOf(datosClientePersonales.getTotalcompras()));                
                                cliente.setTotalEnPuntos(BigInteger.valueOf(datosClientePersonales.getTotalpuntos()));                
                                em.persist(cliente);
                                em.flush();
                                retorno = guardarDomicilioyTelefonoCliente(xmlClienteDomicilioTelefono, cliente, todosDatos);
            /*//--------------------------------------------------------------------------------------------------*/
        } catch (Exception e) {
            retorno =-1;
            logger.error("Error en metodo agregarTodosLosDatosCliente, EJBClientes "+e.getMessage());
        }finally{            
            return retorno;
        }
    }

    /**
     *
     * @param idCliente id de cliente
     * @return devuelve los datos del cliente en formato string
     */
    @Override
    public String obtenerCliente(long idCliente) {
        StringBuilder cli = new StringBuilder("<Lista>\n");
        try {
            Clientes cliente = em.find(Clientes.class, idCliente);
            if(cliente!=null){
                cli.append("<item>\n");
                cli.append(cliente.toXML());
                cli.append(cliente.toXMLCLI());
                cli.append("</item>\n");
            }else {
                cli.append("<cliente>NO ENCONTRADO</cliente>");
            }
        } catch (Exception e) {
            logger.error("Error al obtener un cliente EJBCliente", e);
        }finally{
            
            return cli.append("</Lista>\n").toString();
        }
    }
    /**
     * 
     * @param todosDatos objeto completo de cliente, domicilio, telefono
     * @param datosClientePersonales objeto datos personales de cliente
     * @param xmlClienteDomicilioTelefono datos completo de cliente
     * @param idcliente id de cliente
     * @return devuelve el id de cliente si todo se realizó correctamente
     */
private long actualizarDatos(ClienteDomicilioTelefono todosDatos, DatosCliente datosClientePersonales,String xmlClienteDomicilioTelefono,long idcliente) {
        long retorno = 0;
        try {
            //**********************************************************************************
           //**********************************************************************************   
            
            Clientes cliente = em.find(Clientes.class, idcliente);
            if(cliente!=null){
                              //almacena los datos del cliente en HistoricoDatosClientes
                                HistoricoDatosClientes historicoClient = new HistoricoDatosClientes();
                                        historicoClient.setApellido(cliente.getApellido().toUpperCase());                
                                        historicoClient.setIdCliente(idcliente);
                                        historicoClient.setIdgenero(cliente.getGeneros().getIdGenero());
                                        historicoClient.setNombre(cliente.getNombre().toUpperCase());    
                                        historicoClient.setObservaciones(cliente.getObservaciones());
                                        

                                cliente.setObservaciones(datosClientePersonales.getObservaciones());            
                                cliente.setApellido(datosClientePersonales.getApellido().toUpperCase());            
                                cliente.setNombre(datosClientePersonales.getNombre().toUpperCase());      
                                cliente.setGeneros(em.find(Generos.class, datosClientePersonales.getGeneros().getIdgenero()));
                                 if(chequear__email_numDoc != -5){
                                    
                                            historicoClient.setEmail(cliente.getEmail());
                                            cliente.setEmail(datosClientePersonales.getEmail());
                                 }         
                                 //acumulo la cantidad de pesos comprados por un cliente
                                 double acumTotalCompras = 0;
                              if(cliente.getTotalCompras().compareTo(BigDecimal.valueOf(Double.valueOf(String.valueOf(datosClientePersonales.getTotalcompras()))))!=0){   
                                    double totalCompras = Double.valueOf(String.valueOf(cliente.getTotalCompras()));
                                    acumTotalCompras = totalCompras+Double.parseDouble(String.valueOf(datosClientePersonales.getTotalcompras()));
                                    cliente.setTotalCompras(BigDecimal.valueOf(Double.valueOf(String.valueOf(acumTotalCompras)))); 
                              }     
                              //acumulo los puntos
                              int acumTotalPuntos = 0;
                              if(cliente.getTotalEnPuntos().compareTo(BigInteger.valueOf(datosClientePersonales.getTotalpuntos()))!=0){
                                int totalPuntos = Integer.valueOf(String.valueOf(cliente.getTotalEnPuntos()));
                                acumTotalPuntos = totalPuntos+datosClientePersonales.getTotalpuntos();
                                cliente.setTotalEnPuntos(BigInteger.valueOf(acumTotalPuntos));
                              }
                                //actualizo la fecha de ultima carga
                                cliente.setFechaCarga(new GregorianCalendar().getTime());
                            historicoClient.setTotalCompras(BigDecimal.valueOf(Double.valueOf(acumTotalCompras)));
                            historicoClient.setTotalEnPuntos(BigInteger.valueOf(acumTotalPuntos));
                            /*
                             * Actualizo domicilio
                             */

                            retorno = guardarDomicilioyTelefonoCliente(xmlClienteDomicilioTelefono,cliente,todosDatos);              
                               //-------------------------------------------------------------------------------------------------
                                em.merge(cliente);
                                em.persist(historicoClient);                
            }
        } catch (NumberFormatException e) {
            retorno=-6;
            logger.error("Error en metodo actualizarDatos, ejbClientes "+e);
        }finally{            
            return retorno;
        }
    }

    /**
     *
     * @param idTipo id de tipo de documento
     * @param nrodDocu numero de documento
     * @return devuelve los datos del cliente en formato cadena
     */
    @Override
    public String obtenerClienteXTipoAndNumeroDocu(short idTipo, int nrodDocu) {
        String result = "<Lista>\n";
        try {    
            
            long idPersona = existePersona(nrodDocu);    
                    switch((int)idPersona){
                        case 0:{
                             result+="<result>NO PASO NADA</result>\n";                        
                             break;
                        }
                        case -1:{

                             result+="<result>ERROR EN METODO EXISTE</result>\n";                        
                             break;
                        }
                        default:{
                            Clientes cliente = em.find(Clientes.class, idPersona);           
                                    if(cliente != null){

                                        result+="<item>\n";
                                        result+=cliente.toXML();
                                        result+="</item>\n";
                                    }else {
                                        result+="<cliente>NO ES UN CLIENTE</cliente>\n";
                                    }                                         
                        }
            }
            
                        
        } catch (Exception e) { 
            result="<result>ERROR EN METODO obtenerClienteXTipoAndNumeroDocu</result>";
            logger.error("Error en metodo obtenerClienteXTipoAndNumeroDocu en EJBClientes "+e.getLocalizedMessage());
        }finally{               
            return result+="</Lista>\n";
        }
    }

    /**
     *
     * @param cliente objeto cliente
     * @return
     */
//    @Override
//    public long addClientes(Clientes cliente) {
//        long resultCode = 0;
//        try {
//            
//                            //-----------------------------------------------------------------------------------
//                            GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
//                            //-----------------------------------------------------------------------------------
//                            //--------------------------------------------------------------------------
//                            if(cliente == null)
//                            {
//                                logger.info("Objeto Cliente es invalido");
//                                resultCode = -1;
//                                throw new IllegalArgumentException("Objeto Cliente es invalido");
//
//                            }
//                            
//                            //--------------------------------------------------------------------------
//                            Query consulta = em.createQuery("SELECT c FROM Clientes c WHERE c.nrodocumento = :nrodocumento");
//                            consulta.setParameter("nrodocumento",cliente.getNrodocumento());
//                            //--------------------------------------------------------------------------
//                            if(consulta.getResultList().size()==1){
//                                 logger.info("Clientes existente, numero de documento");
//                                 resultCode=-3;
//                                 throw new RuntimeException("Entrada Cliente existente");
//                            }
//                            //-------------------------------------------------------------------------
//                            String nombre = cliente.getNombre();
//                            String apellido = cliente.getApellido();
//                            short idtipo = cliente.getTipodocumento().getId();
//                            int nrodocu = cliente.getNrodocumento();
//                            String email = cliente.getEmail();
//                            String observaciones = cliente.getObservaciones();
//                            //------------------------------------------------------------------------
//                            Clientes cli = new Clientes();
//                            cli.setApellido(apellido.toUpperCase());
//                            cli.setNombre(nombre.toUpperCase());
//                            cli.setEmail(email);
//                            cli.setObservaciones(observaciones);
//                            cli.setNrodocumento(nrodocu);
//                            cli.setTipodocumento(em.find(Tiposdocumento.class, idtipo));
//                            cli.setFechaCarga(calendario.getTime());
//                            cli.setTotalCompras(BigDecimal.ZERO);
//                            cli.setTotalEnPuntos(BigInteger.ZERO);
//                            em.persist(cli);
//                            resultCode = cli.getIdPersona();
//                            
//                //---------------------------------------------------------------------------------------------------
//        } catch (RuntimeException e) {
//            logger.error("Ocurrió un error al insertar un Objeto Cliente, verifique");
//            resultCode = -2;
//        }finally{            
//            return resultCode;
//        }
//    }

    /**
     *
     * @param cliente objeto cliente
     * @return devuelve el id del cliente
     */
//    @Override
//    public long updateCliente(Clientes cliente) {
//        long resultCode =0L;
//        try {
//            //--------------------------------------------------------
//            if(cliente==null){
//                logger.info("Error Objeto Cliente Invalido al actualizar");
//                resultCode=-2;
//                throw new IllegalArgumentException("Objeto Cliente invalido al actualizar");
//            }
//            //--------------------------------------------------------
//            String apellido = cliente.getApellido();
//            String nombre = cliente.getNombre();
//            String email = cliente.getEmail();
//            String observaciones = cliente.getObservaciones();
//            BigDecimal totalCompras = cliente.getTotalCompras();
//            BigInteger puntos = cliente.getTotalEnPuntos();
//            short idTipo = cliente.getTipodocumento().getId();
//            int nroDocu = cliente.getNrodocumento();
//            long clientId = cliente.getIdPersona();
//            //--------------------------------------------------------
//                    if(clientId <= 0 ||
//                            nombre == null ||
//                            apellido == null)
//                        {
//                        logger.info("Error Objeto Cliente Invalido al actualizar");
//                        resultCode=-3;
//                            throw new IllegalArgumentException("Objeto Cliente invalido al actualizar");
//                        }
//                       //---------------------------------------------------------
//                        Clientes cli = em.find(Clientes.class, clientId);
//                        cli.setApellido(apellido.toUpperCase());
//                        cli.setEmail(email);
//                        cli.setNombre(nombre.toUpperCase());
//                        cli.setObservaciones(observaciones);
//                        cli.setTotalCompras(totalCompras);
//                        cli.setTotalEnPuntos(puntos);
//                        cli.setTipodocumento(em.find(Tiposdocumento.class, idTipo));
//                        cli.setNrodocumento(nroDocu);
//                        //--------------------------------------------------------
//                            resultCode = cli.getIdPersona();
//                        //--------------------------------------------------------
//        } catch (IllegalArgumentException e) {
//            logger.error("Error en metodo updateCliente, verifique ",e);
//            resultCode =-1;
//        }finally{
//            
//            return resultCode;
//        }
//    }
    /**
     * 
     * @param xmlClienteDomicilioTelefono datos del cliente completo
     * @param cliente objeto cliente
     * @param todosDatos objeto completo cliente, domicilio, telefono
     * @return  devuelve el id del cliente si el método se realizo correctamente
     */
private long guardarDomicilioyTelefonoCliente(String xmlClienteDomicilioTelefono,Clientes cliente, ClienteDomicilioTelefono todosDatos) {
        long retorno=0;
        long idDomicilio=0;
        try {
           if(xmlClienteDomicilioTelefono.contains("<Domicilio>")){
                        idDomicilio = ejbdomici.addDomicilios(todosDatos.getDomicilio());
                  //-------ADD RELACION--------------------------------------------------------------------------
                                String result=null;            
                                //---------------------------------------------------------------------------------------------
                               switch((int)idDomicilio){
                                   case -1:{logger.error("Error No se pudo agregar domicilio Verifique!!!");
                                   retorno = -1;
                                   break;}
                                   case -2:{logger.error("Error No se pudo agregar domicilio Verifique!!!");
                                   retorno = -2;
                                   break;}
                                   case 0:{logger.error("Error no se pudo agregar domicilio Verifique!!!");
                                   retorno = 0;
                                   break;}
                                   case -3:{logger.error("Error en metodo actualizar domicilio Verifique!!!");
                                   retorno = -3;
                                   break;}
                                   default:{
                                       String consulta="SELECT p FROM PersonasDomicilios p WHERE p.personasdomiciliosPK.idPersona = :idPersona" +
                                               " and p.personasdomiciliosPK.iddomicilio = :iddomicilio";
                                       Query sqlPD = em.createQuery(consulta);
                                       sqlPD.setParameter("idPersona", cliente.getIdPersona());
                                       sqlPD.setParameter("iddomicilio", idDomicilio);            
                                       switch(sqlPD.getResultList().size()){
                                           case 0:{
                                            result= ejbclidom.addRelacionClienteDomicilio(cliente.getIdPersona(), idDomicilio,todosDatos.getIdusuario());
                                            break;
                                           }                                           
                                           case 1:{
                                               result="Relacion Encontrada PD";
                                               break;
                                           }
                                       }
                                       break;
                                   }
                               }
                               //----------------------------------------------------------------------------------------------
                                if(result.contains("InyectóRelacion")){            
                                    //---------------------------Agregamos la Relacion con Persona Cliente y Domicilios-----------
                                    List<PersonasDomicilios>listaPD = em.createQuery("SELECT p FROM PersonasDomicilios p WHERE " +
                                            "p.personasdomiciliosPK.idPersona = :idPersona").setParameter("idPersona", cliente.getIdPersona()).getResultList();                               
                                    //-----------------------------------------------------------------------------
                                       cliente.setPersonasDomicilioss(listaPD);
                                        Domicilios domici = em.find(Domicilios.class, idDomicilio);
                                        domici.setPersonasDomicilioss(listaPD);
                                    //-----------------------------------------------------------------------------
                                    em.persist(domici);
                                    em.persist(cliente);
                                }
                }
               //-------------------------------------------------------------------------------------------------
                //---------------------------------------------------------------------------------------------
                    if(xmlClienteDomicilioTelefono.contains("<telefono>")){            
                           if(todosDatos.getListaTelefonos().getList().size()>0){            
                               Iterator iter = todosDatos.getListaTelefonos().getList().iterator();
                               //------------------------------------------------------------------------------
                               String resultTC="";
                               DatosTelefonos datosTel=null;
                               while (iter.hasNext())
                               {    
                                   
                                    datosTel = (DatosTelefonos) iter.next();     
                                    //validar numero de telefono
                                    if(ProjectHelpers.TelefonoValidator.validateTelefono(datosTel.getNumero())&&ProjectHelpers.TelefonoValidator.validatePrefijo(datosTel.getPrefijo())){
                                                long rettelefono = ejbtele.addTelefonos(datosTel);
                                                if(rettelefono==2){
                                                    resultTC = ejbclitel.addClienteTelefono(datosTel.getNumero(), datosTel.getPrefijo(), cliente.getIdPersona());
                                                }else{
                                                    if(rettelefono==1){                                                  
                                                         resultTC = ejbclitel.addClienteTelefono(datosTel.getNumero(), datosTel.getPrefijo(), cliente.getIdPersona());                                                    
                                                    }
                                                }
                                    }else{
                                        logger.error("NUMERO DE TELEFONO O PREFIJO NO VÁLIDO NO ALMACENADO "+datosTel.getNumero()+" "+datosTel.getPrefijo());
                                        return retorno =-15;
                                    }
                               }
                               ///+++++++++++++++++++++++++++++++++++++++++++++++++++++++++Relaciono las Entidades****************************+++++
                               //------------------------------------------------------------------------------
                                       Query clitele = em.createNamedQuery("Personastelefonos.findByIdPersona");
                                       clitele.setParameter("idPersona", cliente.getIdPersona());
                                       List<Personastelefonos>listaTel = clitele.getResultList();
                                       cliente.setPersonastelefonoss(listaTel);
                                        Telefonos telef=null;
                               for (Personastelefonos personastelefonos : listaTel) {
                                   telef = em.find(Telefonos.class, new TelefonosPK(personastelefonos.getPersonastelefonosPK().getNumerotel(), personastelefonos.getPersonastelefonosPK().getPrefijo()));
                                   telef.setPersonastelefonosCollection(listaTel);
                               }
                                           em.persist(telef);
                                           em.persist(cliente);
                               //*****************************************************************************************************************
                      }                         
                    }
                    em.flush();
                retorno = cliente.getIdPersona();
            //---------------------------------------------------------------------------------------------
        } catch (Exception e) {
            retorno =-3;
            logger.error("ERROR EN METODO GUARDAR DOMICILIO Y TELEFONO CLIENTE "+e.getMessage());
        }finally{
            
           return retorno;
        }
    }

    /**
     *
     * @param docNumber numero de documento
     * @return datos de cliente
     */
    @Override
    public String getCustomerDocNumber(Integer docNumber) {
        String xml="";
        try {
            Query jsql=em.createNamedQuery("Personas.searchByNroDocuAndPertype");
            jsql.setParameter("nrodocumento", docNumber);
            jsql.setParameter("pertype", "CLI");
            List<Clientes>lista = jsql.getResultList();
           switch(lista.size()){
               case 0:xml="Cliente no encontrado";
               break;
               case 1:{
                for (Clientes cliente : lista) {
                    xml+="<item>\n"+
                            "<id>"+cliente.getIdPersona()+"</id>\n"+
                            "<apellido>"+cliente.getApellido()+"</apellido>\n"+
                            "<nombre>"+cliente.getNombre()+"</nombre>\n"+
                            "<idtipodocu>"+cliente.getTipodocumento().getId()+"</idtipodocu>\n"+
                            "<nrodocu>"+cliente.getNrodocumento()+"</nrodocu>\n";
                    xml+="</item>\n";
                }
               }
           }
        } catch (Exception e) {
            logger.error("Error en metodo getCustomerDocNumber "+e.getMessage());
            xml="Error";
        }finally{       
            
            return xml;
        }
    }
    //metodo que chequea el email en base de datos, agregado tambien validador

    /**
     *
     * @param email correo electronico
     * @param nrodocu numero de documento
     * @return devuelve un número deacuerdo a los datos entregados al método
     */
    public long chequearEmail(String email,Integer nrodocu) {
        long retorno = -6;
        try {
            if(!email.isEmpty()&&nrodocu>0){
                if(ProjectHelpers.EmailValidator.validate(email)){
                    Query sqlemail = em.createNamedQuery("Personas.searchByEmailAndNroDocu");
                                sqlemail.setParameter("email", email.toLowerCase());
                                sqlemail.setParameter("nrodocumento", nrodocu);
                        if(sqlemail.getResultList().size()==1) {
                            retorno = -5;
                    } else{
                            sqlemail = em.createNamedQuery("Personas.findByEmail");
                                sqlemail.setParameter("email", email.toLowerCase());
                                if(sqlemail.getResultList().size()==1) {
                                    retorno =-8;
                        }
                        }
                }else{
                    logger.error("Email no válido");
                    retorno =-11;
                }
            }
        } catch (Exception e) {
            retorno = -7;
            logger.error("Error en metodo chequear email en ejbClientes "+e);
        }finally{            
            return retorno;
        }
    }

    /**
     *
     * @param name nombre de cliente
     * @param lastname apellido de cliente
     * @return devuelve datos de cliente buscado
     */
    @Override
    public String searchClientForNameAndLastName(String name,String lastname) {
        String xml = "<Lista>\n";
        try {
            StringBuilder sbname = new StringBuilder();
                sbname.append(name);
                sbname.append("%");
                    StringBuilder sblastname = new StringBuilder();
                        sblastname.append(lastname);
                        sblastname.append("%");
                    String sql = "SELECT p FROM Personas p WHERE p.nombre LIKE :nombre and p.apellido LIKE :apellido";
                            Query consulta = em.createQuery(sql);
                            consulta.setParameter("nombre", sbname.toString().toUpperCase());
                            consulta.setParameter("apellido", sblastname.toString().toUpperCase());
                            List<Personas>lista = consulta.getResultList();
                                for (Personas personas : lista) {
                                    xml+="<item>\n"
                                            + "<id>"+personas.getIdPersona()+"</id>\n"
                                            + "<apellido>"+personas.getApellido()+"</apellido>\n"
                                            + "<nombre>"+personas.getNombre()+"</nombre>\n"
                                            + "<idtipodocu>"+personas.getTipodocumento().getId()+"</idtipodocu>\n"
                                            + "<nrodocu>"+personas.getNrodocumento()+"</nrodocu>\n" +
                                            "</item>\n";
                                }
        } catch (Exception e) {
            logger.error("Error en metodo searchClientForNameAndLastName "+e.getLocalizedMessage());
        }finally{
            
            return xml+="</Lista>\n";
        }
    }
    /**
     * 
     * @param xmlClienteDomicilioTelefono datos de cliente, domicilio, telefono
     * @return devuelve el objeto cliente, domicilio, telefono
     */
    private ClienteDomicilioTelefono parsear_a_objetos(String xmlClienteDomicilioTelefono){
        ClienteDomicilioTelefono datoscliente = null;
        //uso de la libreria Xstream para convertir de xml a objeto tambien valida los datos 
        try {
            
        
             XStream  xstream = new XStream();
             
        
               xstream.alias("ClienteDomicilioTelefono",ClienteDomicilioTelefono.class);
        
                    xstream.alias("item", DatosCliente.class);                      
        
                    //---------------------------------------------------------------------
                    if(xmlClienteDomicilioTelefono.contains("<Domicilio>")) {
                        xstream.alias("Domicilio", DatosDomicilios.class);
                    }
                    //---------------------------------------------------------------------
                    if(xmlClienteDomicilioTelefono.contains("<telefono>")){                   
                        xstream.alias("listaTelefonos", ListaTelefonos.class);
                        xstream.alias("telefono", DatosTelefonos.class);
                        xstream.addImplicitCollection(ListaTelefonos.class,"list");
                    }                      
        
                    
                   
                            datoscliente = (ClienteDomicilioTelefono) xstream.fromXML(parsearCaracteresEspecialesXML1(xmlClienteDomicilioTelefono));                                 
                   
          
         
        } catch (Exception e) {
         
         logger.error("Error en metodo parsear_a_objetos en EJBClientes "+e);
        }finally{
         
            return datoscliente;
        }
    }
    /**
     * 
     * @param datospersonalescliente cadena con los datos personales en formato 'xml'
     * @return devuelve una lista con los datos personales del cliente generado o encontrado, caso contrario devuelve la indicación de un error o en su defecto el resultado de una validación
     */
    @Override
    public String addClienteDatosPersonales(String datospersonalescliente) {
        long idcliente =0L;
        String xml="<Lista>\n";
        try {
            // convierto los datos personales a objetos
            ClienteDomicilioTelefono getAllDatos=parsear_a_objetos(datospersonalescliente);
            //trabajo con los datos personales de cliente
            DatosCliente getcliente = getAllDatos.getCliente();
           
           //valido nombre y apellido, además de numero de documento
           if(ProjectHelpers.NombreyApellidoValidator.validate(getcliente.getNombre())){
               
               if(ProjectHelpers.NombreyApellidoValidator.validate(getcliente.getApellido())){
                   
                   if(ProjectHelpers.NumeroDocumentoValidator.validate(String.valueOf(getcliente.getNrodocu()))){
                       //reviso el email y numero de documento si estan en la base de datos y pertenecen a este id de cliente u otro
                       chequear__email_numDoc = chequearEmail(getcliente.getEmail(),getcliente.getNrodocu());
                    
                            switch((int)chequear__email_numDoc){
                                     case -7:{logger.error("Error en metodo chequear email");
                                         xml+="<error>Error en metodo chequear email</error>\n";
                                         break;
                                      }
                                     case -8:{logger.error("Email encontrado en metodo chequearEmail");
                                         xml+="<info>Email encontrado en metodo chequearEmail<info>\n";
                                       break;
                                      }
                                      case -11:{logger.error("Email no válido");
                                                 xml+="<error>Email no válido<error>\n";
                                          break;
                                      }
                              default:{               
                                  //*****************************************************************************++++
                                         idcliente= existePersona(getcliente.getNrodocu());
                                             switch((int)idcliente){
                                                 case 0:{
                                                     //------agrego el cliente y todos sus datos desde cero                 
                                                         idcliente =agregarTodosLosDatosCliente(getAllDatos,getcliente,datospersonalescliente);                                              
                                                     }
                                                     break;                                        
                                                         case -1:{                 
                                                             logger.error("Fallo error al buscar cliente en metodo existe");
                                                          xml+="<error>Fallo error al buscar cliente en metodo existe</error>\n";
                                                         break;
                                                     }
                                                 default:{                 
                                                         idcliente = actualizarDatos(getAllDatos,getcliente,datospersonalescliente,idcliente);                                                        
                                                         break;
                                                     }
                                             }
                                     //**********************************************************************************
                             }
                            }//end switch
                   }else{
                             logger.error("El documento no es válido");
                         xml+="<error>El documento no es válido<error>\n";
                   }
                   
               
               }else{
                         logger.error("El apellido no es válido");
                         xml+="<error>El apellido no es válido<error>\n";
                 }
            }else{
               logger.error("El nombre no es válido");
               xml+="<error>El nombre no es válido<error>\n";
            }
           
            if(idcliente>0){
                       Clientes cliem = em.find(Clientes.class, idcliente);
                       xml+="<item>\n";
                       xml+=cliem.toXML();
                       xml+=cliem.toXMLCLI();
                       xml+="</item>\n";
             }
                               
        } catch (Exception e) {
            xml+="<error>se produjo un error</error>\n";
            logger.error("Error en metodo addclientepersonales en ejbcliente "+ e.getLocalizedMessage());
        }finally{
            xml+="</Lista>\n";
            return xml;
        }
    }

    /**
     *
     * @param xmlaParsear datos a parsear
     * @return devuelve los datos parseados de manera correcta
     */
    @Override
    public String parsearCaracteresEspecialesXML1(String xmlaParsear) {
        String xml = "No paso Nada";
        StringBuilder sb=null;
    try {        
        
        sb=new StringBuilder(xmlaParsear);
            if(xmlaParsear.indexOf("<item>")!=-1){
                xml=StringEscapeUtils.escapeXml10(xmlaParsear.substring(xmlaParsear.indexOf("nes>")+4,xmlaParsear.indexOf("</obse")));                
                sb.replace(sb.indexOf("nes>")+4, sb.indexOf("</obse"), xml);                
            }
           if(xmlaParsear.indexOf("<Domicilio>")!=-1){
                xml=StringEscapeUtils.escapeXml10(xmlaParsear.substring(xmlaParsear.indexOf("mes>")+4,xmlaParsear.indexOf("</det1")));                
                sb.replace(sb.indexOf("mes>")+4, sb.indexOf("</det1"), xml);
           }
           xml=sb.toString();
          
    } catch (Exception e) {
        xml = "Error";
        logger.error("Error en metodo parsearCaracteresEspecialesXML1 ",e);
    }finally{
        return xml;
    }
    }

   

    
}

















