/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.ExistenciasProductos;
import com.melani.entity.ImagenesProductos;
import com.melani.entity.Productos;
import com.melani.utils.DatosProductos;
import com.thoughtworks.xstream.XStream;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBProductos")
@WebService(serviceName="ServiceProductos",name="ProductosWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBProductos implements EJBProductosRemote {
    private static final Logger logger = Logger.getLogger(EJBProductos.class);
    static final String PATH_IMAGENES=System.getProperty("user.dir")+File.separator+"var"+File.separator+"webapp"+File.separator+"upload"+File.separator;
    @PersistenceContext(unitName="EJBMelaniPU2")    
    private EntityManager em;    
    //--------------------------------------------------------------------------------------------------
    
//-----------------------------------------------------------------------------------------------------

    /**
     *
     * @param idproducto
     * @param cantidad
     * @param precio
     * @param idusuario
     * @return
     */
        @Override
    public long addExistenciasProducto(long idproducto, int cantidad,float precio,int idusuario) {
        long retorno = 0;
        try {
                GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
                    Productos producto =em.find(Productos.class, idproducto);
                    producto.setCantidadDisponible(BigInteger.valueOf(producto.getCantidadDisponible().intValue()+cantidad));
                        ExistenciasProductos existencias = new ExistenciasProductos();
                            existencias.setCantidadactual(cantidad);
                            existencias.setCantidadinicial(0);
                            existencias.setIdUsuario(idusuario);
                            existencias.setFechaagregado(gc.getTime());
                            existencias.setProductos(em.find(Productos.class, producto.getSid()));
                    if(precio!=0){
                        producto.setPrecioUnitario(BigDecimal.valueOf(precio));
                        existencias.setPreciounitario(BigDecimal.valueOf(precio));
                    }else {
                        existencias.setPreciounitario(BigDecimal.valueOf(0));
                }
            Query consulta = em.createQuery("SELECT e FROM ExistenciasProductos e WHERE e.productos.sid = :sid");
            consulta.setParameter("sid", producto.getSid());
                        List<ExistenciasProductos>lista = consulta.getResultList();
                    producto.setExistenciasProductoss(lista);
                    retorno = producto.getSid();
                    em.merge(producto);
                    em.persist(existencias);
        } catch (Exception e) {
            retorno = -1;
            logger.error("Error en metodo addExistenciasProducto, ejbproductos "+e);
        }finally{
            
            return retorno;
        }
    }

    /**
     *
     * @param idProducto
     * @return
     */
    @Override
    public String leerImagenBaseDatos(long idProducto) {
        String result="NADA";
        ByteArrayInputStream is = null;
        FileOutputStream fos = null;
        try {
            String pathActual = System.getProperty("user.dir") + File.separatorChar + "Imagen" + File.separatorChar;
                Productos producto = em.find(Productos.class, idProducto);
                    File file = new File(pathActual+"faro.jpg");
                    fos = new FileOutputStream(file);
//                    //byte[] buffer= producto.getImg();
//                    is = new ByteArrayInputStream(buffer);
//            //----------------------------------------------------------------------------------
//                    while (is.read(buffer) > 0) {
//                      fos.write(buffer);
//                    }
//            //----------------------------------------------------------------------------------
                    result = "LEIDO";
        } catch (IOException e) {
            result = "ERROR";
            e.getMessage();
        }finally{
           try{
               if(fos!=null) {
                   fos.close();
               }
               if(is!=null) {
                   is.close();
               }
           }catch(IOException e){
                logger.error("Error leyendo imagen leerImagenBaseDatos, en ejbproductos "+e);
            }finally{
                
                return result;
           }
        }
    }

    /**
     *
     * @param xmlProducto
     * @return
     */
    @Override
    public String addProducto(String xmlProducto) {
        String retorno = "0L";
        Productos producto = null;
        long idproduct;
        try {
        idproduct =agregarProducto(producto, xmlProducto);
        if(idproduct>0){
            retorno=searchAllProductos();
        }else{
            retorno="<Lista>\n" +
                    "<producto>\n" +
                    "<id>"+idproduct+"</id>\n" +
                    "</producto>\n"+
                    "</Lista>\n";
        }
        } catch (Exception e) {
            logger.error("Error en metodo addProducto "+e);
        }finally{
            return retorno;
        }
    }
    private long agregarProducto(Productos producto, String xmlProducto) {
        long retorno = 0L;
        String descripcion=null;
        String codigo =null;
        try {
            //-----------------------------------------------------------------------
             XStream xstream = new XStream();
                xstream.alias("producto", DatosProductos.class);
                DatosProductos datosprod = (DatosProductos) xstream.fromXML(xmlProducto);
            //-----------------------------------------------------------------------
                GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
            //-----------------------------------------------------------------------
                descripcion=datosprod.getDescripcion();
                codigo =datosprod.getCodproducto();
                if(!descripcion.isEmpty()){
                    if(codigo.length()>0){
            //-----------------------------------------------------------------------    
                producto =em.find(Productos.class, datosprod.getIdproducto());
                Query consulta1 = em.createQuery("SELECT p FROM Productos p WHERE LOWER(p.codproducto) LIKE LOWER(:codigoproducto)");
                consulta1.setParameter("codigoproducto", codigo.concat("%"));
                Query consulta = em.createQuery("SELECT p FROM Productos p WHERE LOWER(p.descripcion) LIKE LOWER(:descripcion)");
                consulta.setParameter("descripcion", descripcion.concat("%"));
                
                
                    if(consulta1.getResultList().isEmpty()){
                        if(consulta.getResultList().isEmpty()){
                            if(producto==null){
                            //----------------------------Producto Nuevo-------------------------------------------
                                    producto = new Productos();
                                    producto.setCantidadDisponible(BigInteger.valueOf(datosprod.getCantidaddisponible()));
                                    producto.setCantidadInicial(BigInteger.valueOf(datosprod.getCantidadinicial()));
                                    producto.setPrecioUnitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                    producto.setDescripcion(datosprod.getDescripcion().toUpperCase());
                                    producto.setFecha(calendario.getTime());                                    
                                    producto.setCodproducto(datosprod.getCodproducto().toUpperCase());
                                    em.persist(producto);
                                        ExistenciasProductos existencias = new ExistenciasProductos();
                                        existencias.setCantidadactual(Integer.valueOf(datosprod.getCantidaddisponible()));
                                        existencias.setCantidadinicial(datosprod.getCantidadinicial());
                                        existencias.setFechaagregado(calendario.getTime());
                                        existencias.setPreciounitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                        existencias.setIdUsuario(datosprod.getIdusuario());
                                        existencias.setProductos(em.find(Productos.class, producto.getSid()));
                                        em.persist(existencias);
                                    retorno = existencias(producto);
                        //-----------------------------------------------------------------------
                                        }else{
                                                        if(producto.getCantidadDisponible().intValue()!=datosprod.getCantidaddisponible()&&producto.getPrecioUnitario()!=BigDecimal.valueOf(datosprod.getPreciounitario())){
                                            //--------------------------------Actualizo Producto Los CamposNecesarios-------------------------------
                                                                    producto.setCantidadDisponible(BigInteger.valueOf(producto.getCantidadDisponible().intValue()+datosprod.getCantidaddisponible()));
                                                                    producto.setPrecioUnitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                                                    producto.setDescripcion(datosprod.getDescripcion().toUpperCase());
                                                                    producto.setCodproducto(datosprod.getCodproducto());
                                                                    em.merge(producto);
                                            //---------------------------------------------------------------------------------
                                                                    ExistenciasProductos existencias = new ExistenciasProductos();
                                                                    existencias.setCantidadactual(datosprod.getCantidaddisponible());
                                                                    existencias.setCantidadinicial(0);
                                                                    existencias.setFechaagregado(calendario.getTime());
                                                                    existencias.setPreciounitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                                                    existencias.setProductos(em.find(Productos.class, producto.getSid()));
                                                                    em.persist(existencias);
                                            //---------------------------------------------------------------------------------
                                                                retorno = existencias(producto);
                                            //---------------------------------------------------------------------------------
                                                        }else {
                                                            retorno = producto.getSid();
                                                        }
                                        }
                                }else {
                            retorno = -5;
                        }
                              }else {
                        retorno = -6;
                }
                    }else {
                        retorno=-8;
                    }
                }else {
                    retorno=-7;
             }
                
        } catch (Exception e) {
            retorno =-2;
            logger.error("Error en metodo agregarProducto, ejbProducto "+e);
        }finally{
            
            return retorno;
        }
    }
    private long existencias(Productos producto) {
        long retorno = 0L;
        try {
            List<ExistenciasProductos>lista = em.createQuery("SELECT e FROM ExistenciasProductos e WHERE e.productos.sid = :sid")
                .setParameter("sid", producto.getSid()).getResultList();
                producto.setExistenciasProductoss(lista);
                em.merge(producto);
                retorno = producto.getSid();
        } catch (Exception e) {
            retorno =-1;
            logger.error("Error en metodo existencias ejbProductos "+e);
        }finally{
            
            return retorno;
        }
    }

    /**
     *
     * @param idproducto
     * @return
     */
    @Override
    public String selectoneproducto(long idproducto) {
        String result = "NADA";
        try {
            Productos producto = em.find(Productos.class, idproducto);
            result = producto.toXML();
        } catch (Exception e) {
            result = "ERROR";
            logger.error("Error en metodo selectoneproducto "+e);
        }finally{
            
            return result;
        }
    }

    /**
     *
     * @param producto
     * @return
     */
    @Override
    @SuppressWarnings("null")
    public Productos agregarProductos(Productos producto) {
        
        try {
            GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
                Productos produ = em.find(Productos.class, producto.getSid());
                    if(produ!=null){
                        produ.setFecha(calendario.getTime());
                        em.persist(producto);
                    }else{                        
                          produ.setFecha(calendario.getTime());
                          em.merge(producto);
                    }
        } catch (Exception e) {
            logger.error("Error en metodo addProductos "+e);
        }finally{
            
            return producto;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String searchAllProductos() {
        String xml = "NADA";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //Query query = em.createNativeQuery("SELECT p.sid,p.DESCRIPCION,p.codproducto,p.PRECIOUNITARIO,p.CANTIDADINICIAL,p.CANTIDADDISPONIBLE,p.img" +
              //      ",p.FECHA FROM PRODUCTOS p Order by p.sid", Productos.class);
            Query query = em.createNamedQuery("Productos.findAll");
            List<Productos> lista = query.getResultList();
                    if(lista.isEmpty()) {
                        xml="LA CONSULTA NO ARROJÓ RESULTADOS";
            } else{
                        Iterator iter = lista.iterator();
                        xml="<Lista>\n";
                        while(iter.hasNext()){
                            Productos prod = (Productos) iter.next();
                            xml+="<producto>\n"
                                    + "<id>"+prod.getSid()+"</id>\n"
                                    + "<idproduct>"+prod.getCodproducto()+"</idproduct>\n"
                                    + "<descripcion>"+prod.getDescripcion()+"</descripcion>\n"
                                    + "<cantidadDisponible>"+prod.getCantidadDisponible()+"</cantidadDisponible>\n"
                                    + "<cantidadInicial>"+prod.getCantidadInicial()+"</cantidadInicial>\n"
                                    + "<fecha>"+sdf.format(prod.getFecha())+"</fecha>\n" 
                                    +"<precio>"+prod.getPrecioUnitario()+"</precio>\n"
                                    + "<img>"+prod.getImagenesProductosList().size()+"</img>\n";
                                    xml+="</producto>\n";
                                   
                        //List<ExistenciasProductos>lista1=prod.getExistenciasProductoss();
                            
                        }                        
                        xml+="</Lista>\n";
                    }
        }catch (Exception e) {
            logger.error("Error al buscar todos los producto EJBProducto", e);
        }finally{
            
            return xml;
        }
    }

    /**
     *
     * @param idProducto
     * @param cantidad
     * @param idUsuario
     * @return
     */
    @Override
    public int controlStockProducto(long idProducto, int cantidad, int idUsuario) {
        int resultado = 0;
        try {
                        GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
                        Productos producto = em.find(Productos.class, idProducto);
                        producto.setCantidadDisponible(producto.getCantidadDisponible().subtract(BigInteger.valueOf(cantidad)));
                        //-----------------------------------------------------------------------------------------------------
                            ExistenciasProductos existencias = new ExistenciasProductos();
                                    existencias.setCantidadactual(-cantidad);
                                    existencias.setCantidadinicial(0);
                                    existencias.setFechaagregado(gc.getTime());
                                    existencias.setIdUsuario(idUsuario);
                                    existencias.setPreciounitario(producto.getPrecioUnitario());
                                    existencias.setProductos(em.find(Productos.class, idProducto));
                                    em.persist(existencias);
                               Query consulta = em.createQuery("SELECT e FROM ExistenciasProductos e WHERE e.productos.sid = :idproducto");
                               consulta.setParameter("idproducto", producto.getSid());
                               List<ExistenciasProductos>lista = consulta.getResultList();
                                   producto.setExistenciasProductoss(lista);
                               em.persist(producto);
                                    resultado = producto.getCantidadDisponible().intValue();
        } catch (Exception e) {
            logger.error("Error en metodo controlStockProducto "+ e.getLocalizedMessage());
            resultado = -1;
        } finally {
            
            return resultado;
        }
    }

    /**
     *
     * @param xmlProducto
     * @return
     */
    @Override
    public String actualizarProducto(String xmlProducto) {
          String retorno = "0L";
        Productos producto = null;
        long idproduct;
        try {
        idproduct =updateProducto(producto, xmlProducto);
            retorno="<Lista>\n" +
                    "<producto>\n" +
                    "<id>"+idproduct+"</id>\n" +
                    "</producto>\n"+
                    "</Lista>\n";
        } catch (Exception e) {
            logger.error("Error en metodo addProducto "+e);
        }finally{
            return retorno;
        }
    }
    private long updateProducto(Productos producto, String xmlProducto) {
         long retorno = 0L;
        try {
            //-----------------------------------------------------------------------
             XStream xstream = new XStream();
                xstream.alias("producto", DatosProductos.class);
                DatosProductos datosprod = (DatosProductos) xstream.fromXML(xmlProducto);
            //-----------------------------------------------------------------------
                GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
            //-----------------------------------------------------------------------
                if(datosprod.getIdproducto()>0) {
                    producto =em.find(Productos.class, datosprod.getIdproducto());
             }
                        
                //--------------------------------Actualizo Producto Los CamposNecesarios-------------------------------
                                        producto.setCantidadDisponible(BigInteger.valueOf(producto.getCantidadDisponible().intValue()+datosprod.getCantidaddisponible()));
                                        producto.setPrecioUnitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                        em.persist(producto);
                                        em.flush();
                //---------------------------------------------------------------------------------
                                        ExistenciasProductos existencias = new ExistenciasProductos();
                                        existencias.setCantidadactual(datosprod.getCantidaddisponible());
                                        existencias.setCantidadinicial(0);
                                        existencias.setFechaagregado(calendario.getTime());
                                        existencias.setPreciounitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                        existencias.setProductos(em.find(Productos.class, producto.getSid()));
                                        existencias.setIdUsuario(datosprod.getIdusuario());
                                        em.persist(existencias);
                                        em.flush();
                //---------------------------------------------------------------------------------
                                    retorno = existencias(producto);
                //---------------------------------------------------------------------------------
                                retorno = producto.getSid();
        } catch (Exception e) {
            retorno =-2;
            logger.error("Error en metodo updateProducto, ejbProducto "+e);
        }finally{
            
            return retorno;
        }
    }   
    /**
     * 
     * @param id_producto numero producto a guardar con imagen
     * @param longitudImagen tamaño de la imagen en bytes
     * @param nameImage nombre de la imagen
     * @param magnitud tamaño de la imagen en disco
     * @return devuelve el identificador si es mayor a cero tuvo exito la operación, si no es menor hubo error, si es cero no paso nada
     */
    @Override
    public int grabarImagen(long id_producto, byte[] longitudImagen,String nameImage,String magnitud) {
        int retorno = 0;
        String extension=nameImage.substring(nameImage.indexOf(".")+1, nameImage.length());
        String nameImg=nameImage.substring(0, nameImage.indexOf("."));
        try {
            
            Productos producto = em.find(Productos.class, id_producto);
            
            
                ByteArrayInputStream bis = new ByteArrayInputStream(longitudImagen);
                
                Iterator<?> readers = ImageIO.getImageReadersByFormatName(extension);
                

                        //ImageIO is a class containing static methods for locating ImageReaders
                        //and ImageWriters, and performing simple encoding and decoding. 

                        ImageReader reader = (ImageReader) readers.next();
                        Object source = bis; 
                        ImageInputStream iis = ImageIO.createImageInputStream(source); 
                        reader.setInput(iis, true);
                        ImageReadParam param = reader.getDefaultReadParam();

                        Image image = reader.read(0, param);
                        
                        //got an image file

                        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
                        
                        //bufferedImage is the RenderedImage to be written

                        Graphics2D g2 = bufferedImage.createGraphics();
                        
                        g2.drawImage(image, null, null);

                        File imageFile = new File(PATH_IMAGENES+nameImage);       
                        
                        //-----------------------------------------------------------Escribir path de la imagen en disco
                        ImageIO.write(bufferedImage, extension, imageFile);
                        
                        
               //-------------------------------------------------------------------------------------------------------Escribir en Base de Datos
                      retorno= grabarPathImagenEnBaseDeDatos(producto,imageFile.getPath(),extension,magnitud,nameImg);       
        } catch (IOException e) {
            retorno=-1;
            logger.error("Error al Almacenar Imagen en Base de Datos "+e.getLocalizedMessage());
        }finally{
            
            return retorno;
        }
    }

    /**
     *
     * @param idProducto
     * @return
     */
    @Override
    public byte[] obtenerImagenProducto(long idProducto) {
        byte[] retorno = null;
        try {
            
            Query consulta = em.createQuery("SELECT i FROM ImagenesProductos i WHERE i.productos.sid = :sid");
            
            
            consulta.setParameter("sid", idProducto);
            
            List<ImagenesProductos>lista = consulta.getResultList();
            
                    String pathImage = null;
                    for(ImagenesProductos imagen:lista){
                           pathImage=PATH_IMAGENES+imagen.getNombreImagen()+"."+imagen.getExtension();             
                    }
            
                    //Creo objeto file
                    File file = new File(pathImage);

                    //Objengo los bytes, tamaño del archivo o imagen
                    FileInputStream fis = new FileInputStream(file);

                    //Obtengo un output stream luego creo el buffer
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    byte [] buffer = new byte[1_024];
                    //itero llenando el buffer para ser transferido o retornado como bytearray
                    for(int readNum;(readNum=fis.read(buffer))!=-1;){
                        bos.write(buffer,0,readNum);
                    }
                    retorno = bos.toByteArray();
        } catch (IOException e) {            
            logger.error("Error en metodo obtenerImagenProducto en EJBProductos "+e.getLocalizedMessage());
        }finally{
            
            return retorno;
        }
    }
//---------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------------
    /**
     * 
     * @param producto objeto que representa una instancia de Productos
     * @param path ruta a la imagen en el directorio de usuario del servidor
     * @param extension tipo de imagen a almacenar
     * @param magnitud tamaño de la imagen en disco
     * @param nameImage nombre de la imagen
     * @return devuelve el numero de imagen generado correcto mayor a cero, cero no paso nada, menor a cero hubo un error
     */

    private int grabarPathImagenEnBaseDeDatos(Productos producto, String path, String extension, String magnitud, String nameImage) {
        int retorno=0;
        try {
            //graba las características de la imagen en la base de datos
            
            ImagenesProductos imgProd =new ImagenesProductos();
            
                    imgProd.setExtension(extension);
            
                    imgProd.setMagnitud(magnitud);
            
                    imgProd.setNombreImagen(nameImage);
            
                    imgProd.setPathImagenEnDisco(path);
            
                    imgProd.setProductos(producto);
            
                    em.persist(imgProd);
            
                    //Enlazando las imagenes de un producto
                Query consulta = em.createQuery("Select i From ImagenesProductos i Where i.productos.sid = :idProducto");
                    consulta.setParameter("idProducto", producto.getSid());
               List<ImagenesProductos>lista = consulta.getResultList();
               
               producto.setImagenesProductosList(lista);
               
               em.merge(producto);
            
            retorno=Integer.valueOf(String.valueOf(producto.getSid()));
            
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            retorno=-1;
        }finally{
            
            return retorno;
        }
    }
}
