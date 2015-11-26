package com.melani.ejb;
import com.melani.entity.ExistenciasProductos;
import com.melani.entity.ImagenesProductos;
import com.melani.entity.Productos;
import com.melani.utils.DatosProductos;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
@Stateless(name="ejb/EJBProductos")
@WebService(serviceName="ServiceProductos",name="ProductosWs")
public class EJBProductos implements EJBProductosRemote {
    private static final Logger LOGGER = Logger.getLogger(EJBProductos.class);
    static final String PATH_IMAGENES = System.getProperty("user.dir")+File.separator+"var"+File.separator+"webapp"+File.separator+"upload"+File.separator;    
    @PersistenceContext(unitName="EJBMelaniPU2")    
    private EntityManager em;     
        
    @Override
    public long addExistenciasProducto(long idproducto, int cantidad,float precio,int idusuario) {
        long retorno;        
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
            return retorno;
    }
    
    @Override
    public String addProducto(String xmlProducto) {
        String retorno = null;
        Productos producto = null;
        long idproduct;        
        idproduct =agregarProducto(producto, xmlProducto);
        if(idproduct>0){
            retorno+=searchAllProductos();
        }else{
            retorno+="<Lista>\n"+"<producto>\n"+"<id>"+idproduct+"</id>\n";
                    retorno+="</producto>\n"+"</Lista>\n";
        }        
            return retorno;        
    }
    
    private long agregarProducto(Productos producto, String xmlProducto) {
        long retorno = 0L;
        String descripcion;
        String codigo;       
             XStream xstream = new XStream(new StaxDriver());
                xstream.alias("producto", DatosProductos.class);
                DatosProductos datosprod = (DatosProductos) xstream.fromXML(xmlProducto);            
                GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());            
            descripcion=datosprod.getDescripcion();
                codigo =datosprod.getCodproducto();
                if(!descripcion.isEmpty()){
                    if(codigo.length()>0){            
                producto =em.find(Productos.class, datosprod.getIdproducto());
                Query consulta1 = em.createQuery("SELECT p FROM Productos p WHERE LOWER(p.codproducto) LIKE LOWER(:codigoproducto)");
                consulta1.setParameter("codigoproducto", codigo.concat("%"));
                Query consulta = em.createQuery("SELECT p FROM Productos p WHERE LOWER(p.descripcion) LIKE LOWER(:descripcion)");
                consulta.setParameter("descripcion", descripcion.concat("%"));
                    if(consulta1.getResultList().isEmpty()){
                        if(consulta.getResultList().isEmpty()){
                            if(producto==null){                            
                                    producto = new Productos();
                                    producto.setCantidadDisponible(BigInteger.valueOf(datosprod.getCantidaddisponible()));
                                    producto.setCantidadInicial(BigInteger.valueOf(datosprod.getCantidadinicial()));
                                    producto.setPrecioUnitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                    producto.setDescripcion(datosprod.getDescripcion().toUpperCase());
                                    producto.setFecha(calendario.getTime());                                    
                                    producto.setCodproducto(datosprod.getCodproducto().toUpperCase());
                                    em.persist(producto);
                                        ExistenciasProductos existencias = new ExistenciasProductos();
                                        existencias.setCantidadactual(datosprod.getCantidaddisponible());
                                        existencias.setCantidadinicial(datosprod.getCantidadinicial());
                                        existencias.setFechaagregado(calendario.getTime());
                                        existencias.setPreciounitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                        existencias.setIdUsuario(datosprod.getIdusuario());
                                        existencias.setProductos(em.find(Productos.class, producto.getSid()));
                                        em.persist(existencias);
                                    retorno = existencias(producto);                        
                                        }else{
                                                        if(producto.getCantidadDisponible().intValue()!=datosprod.getCantidaddisponible()&&producto.getPrecioUnitario()!=BigDecimal.valueOf(datosprod.getPreciounitario())){                                            
                                                                    producto.setCantidadDisponible(BigInteger.valueOf(producto.getCantidadDisponible().intValue()+datosprod.getCantidaddisponible()));
                                                                    producto.setPrecioUnitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                                                    producto.setDescripcion(datosprod.getDescripcion().toUpperCase());
                                                                    producto.setCodproducto(datosprod.getCodproducto());
                                                                    em.merge(producto);
                                                                    ExistenciasProductos existencias = new ExistenciasProductos();
                                                                    existencias.setCantidadactual(datosprod.getCantidaddisponible());
                                                                    existencias.setCantidadinicial(0);
                                                                    existencias.setFechaagregado(calendario.getTime());
                                                                    existencias.setPreciounitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                                                    existencias.setProductos(em.find(Productos.class, producto.getSid()));
                                                                    em.persist(existencias);                                    
                                                                retorno = existencias(producto);
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
                return retorno;        
    }
    
    private long existencias(Productos producto) {
        long retorno;    
            List<ExistenciasProductos>lista = em.createQuery("SELECT e FROM ExistenciasProductos e WHERE e.productos.sid = :sid")
                .setParameter("sid", producto.getSid()).getResultList();
                producto.setExistenciasProductoss(lista);
                em.merge(producto);
                retorno = producto.getSid();                    
            return retorno;        
    }

    @Override
    public String selectOneProducto(long idproducto) {
        String result = "NADA";       
            Productos producto = em.find(Productos.class, idproducto);
            result+=producto.toXML();        
            return result;        
    }
    
    @Override
    @SuppressWarnings("null")
    public Productos agregarProductos(Productos producto) {        
            GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
                Productos produ = em.find(Productos.class, producto.getSid());
                    if(produ!=null){
                        produ.setFecha(calendario.getTime());
                        em.persist(producto);
                    }else{                        
                          produ.setFecha(calendario.getTime());
                          em.merge(producto);
                    }        
            return producto;        
    }

    @Override
    public String searchAllProductos() {
        String xml = "NADA";        
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");            
            Query query = em.createNamedQuery("Productos.findAll");
            List<Productos> lista = query.getResultList();
                    if(lista.isEmpty()) {
                        xml="LA CONSULTA NO ARROJÓ RESULTADOS";
            } else{
                        Iterator iter = lista.iterator();
                        xml="<Lista>\n";
                        StringBuilder xmlLoop= new StringBuilder(10);
                        while(iter.hasNext()){
                            Productos prod = (Productos) iter.next();
                            xmlLoop.append("<producto>\n");
                                    xmlLoop.append("<id>").append(prod.getSid()).append("</id>\n");
                                    xmlLoop.append("<idproduct>").append(prod.getCodproducto()).append("</idproduct>\n");
                                    xmlLoop.append("<descripcion>").append(prod.getDescripcion()).append("</descripcion>\n");
                                    xmlLoop.append("<cantidadDisponible>").append(prod.getCantidadDisponible()).append("</cantidadDisponible>\n");
                                    xmlLoop.append("<cantidadInicial>").append(prod.getCantidadInicial()).append("</cantidadInicial>\n");
                                    xmlLoop.append("<fecha>").append(sdf.format(prod.getFecha())).append("</fecha>\n");
                                    xmlLoop.append("<precio>").append(prod.getPrecioUnitario()).append("</precio>\n");
                                    xmlLoop.append("<img>").append(prod.getImagenesProductosList().size()).append("</img>\n");
                                    xmlLoop.append("</producto>\n");
                                   
                        
                            
                        } 
                        xml+=xmlLoop;
                        xml+="</Lista>\n";
                    }       
            return xml;        
    }

    @Override
    public int controlStockProducto(long idProducto, int cantidad, long idUsuario) {
        int resultado = 0;        
                        GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
                        Productos producto = em.find(Productos.class, idProducto);
                        producto.setCantidadDisponible(producto.getCantidadDisponible().subtract(BigInteger.valueOf(cantidad)));                       
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
            return resultado;
    }

    @Override
    public String actualizarProducto(String xmlProducto) {
          String retorno = null;
        Productos producto = null;
        LOGGER.info("PRODUCTOS ACTUALIZADOS "+updateProducto(producto, xmlProducto));
            retorno+="<Lista>\n";
                    retorno+="<producto>\n";
                    retorno+="<id>"+"</id>\n";
                    retorno+="</producto>\n";
                    retorno+="</Lista>\n";        
            return retorno;
    
    }
    private long updateProducto(Productos producto, String xmlProducto) {
         long retorno = 0L;        
             XStream xstream = new XStream();
                xstream.alias("producto", DatosProductos.class);
                DatosProductos datosprod = (DatosProductos) xstream.fromXML(xmlProducto);
                GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());
                if(datosprod.getIdproducto()>0) {
                    producto =em.find(Productos.class, datosprod.getIdproducto());
             }
                                        producto.setCantidadDisponible(BigInteger.valueOf(producto.getCantidadDisponible()
                                                .intValue()+datosprod.getCantidaddisponible()));
                                        producto.setPrecioUnitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                        em.persist(producto);
                                                ExistenciasProductos existencias = new ExistenciasProductos();
                                                existencias.setCantidadactual(datosprod.getCantidaddisponible());
                                                existencias.setCantidadinicial(0);
                                                existencias.setFechaagregado(calendario.getTime());
                                                existencias.setPreciounitario(BigDecimal.valueOf(datosprod.getPreciounitario()));
                                                existencias.setProductos(em.find(Productos.class, producto.getSid()));
                                                existencias.setIdUsuario(datosprod.getIdusuario());
                                                em.persist(existencias);                                                                        
                                                retorno = producto.getSid();       
            return retorno;
        
    } 
    
    @Override
    public int grabarImagen(long id_producto, byte[] longitudImagen,String nameImage,String magnitud) {
        int retorno = 0;
        try {
            
            String extension=nameImage.substring(nameImage.indexOf(".")+1, nameImage.length());       
            String nameImg=nameImage.substring(0, nameImage.indexOf("."));
            Productos producto = em.find(Productos.class, id_producto);
            ByteArrayInputStream bis = new ByteArrayInputStream(longitudImagen);
            Iterator<?> readers = ImageIO.getImageReadersByFormatName(extension);
            ImageReader reader = (ImageReader) readers.next();
            Object source = bis;
            ImageInputStream iis = ImageIO.createImageInputStream(source);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Image image = reader.read(0, param);
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bufferedImage.createGraphics();
            g2.drawImage(image, null, null);
            File imageFile = new File(PATH_IMAGENES+nameImage);
            ImageIO.write(bufferedImage, extension, imageFile);
            retorno= grabarPathImagenEnBaseDeDatos(producto,imageFile.getPath(),extension,magnitud,nameImg);
                   
        } catch (IOException ex) {
            LOGGER.error("ERROR AL PROCESAR LA IMAGEN "+ex.getMessage());
        }
        return retorno; 
    }

    @Override
    public byte[] obtenerImagenProducto(long idProducto) {
        byte[] retorno = new byte[5];              
            Query consulta = em.createNamedQuery("ImagenesProductos.findById");
                consulta.setParameter("sid", idProducto);
            List<ImagenesProductos>lista = consulta.getResultList();
            String pathImage = "";
            if(lista.size()>0){  
                    for(ImagenesProductos imagen:lista){
                           pathImage=PATH_IMAGENES+imagen.getNombreImagen()+"."+imagen.getExtension();             
                    }
                                    //Creo objeto file
                                    File file = new File(pathImage);
                                    //Objengo los bytes, tamaño del archivo o imagen
                                    if(file.exists()){
                                        try {
                                            retorno = procesarImagen(file);
                                        } catch (IOException ex) {
                                            LOGGER.error("Error en archivo de imagen");
                                        }
                                    }else{
                                            try {
                                                  retorno = procesarImagen(new File(PATH_IMAGENES+"android-logo-400x300.jpg"));
                                            } catch (IOException ex) {
                                                   LOGGER.error("Error al procesar imagen por defecto");
                                            }
                                    }
            }            
            return retorno;        
    }
    
    private int grabarPathImagenEnBaseDeDatos(Productos producto, String path, String extension, String magnitud, String nameImage) {
        int retorno;        
            ImagenesProductos imgProd =new ImagenesProductos();            
                    imgProd.setExtension(extension);            
                    imgProd.setMagnitud(magnitud);            
                    imgProd.setNombreImagen(nameImage);            
                    imgProd.setPathImagenEnDisco(path);            
                    imgProd.setProductos(producto);            
                    em.persist(imgProd);  
                Query consulta = em.createNamedQuery("ImagenesProductos.findByIdProduct");
                    consulta.setParameter("idProducto", producto.getSid());
               List<ImagenesProductos>lista = consulta.getResultList();               
               producto.setImagenesProductosList(lista);               
               em.merge(producto);            
            retorno=Integer.valueOf(String.valueOf(producto.getSid())); 
            return retorno;        
    }

    private byte[] procesarImagen(File file) throws FileNotFoundException, IOException {
        ByteArrayOutputStream bos;
        FileInputStream fis;
                        fis = new FileInputStream(file); 
                        bos = new ByteArrayOutputStream();
                        byte [] buffer = new byte[1_024];
                        for(int readNum;(readNum=fis.read(buffer))!=-1;){
                             bos.write(buffer,0,readNum);
                         }                                                 
        return bos.toByteArray();
    }
}