package com.melani.ejb;
import com.melani.entity.ExistenciasProductos;
import com.melani.entity.ImagenesProductos;
import com.melani.entity.Productos;
import com.melani.utils.DatosProductos;
import com.melani.utils.Imagen;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
@Stateless(name="ejb/EJBProductos")
@WebService(serviceName="ServiceProductos",name="ProductosWs")
public class EJBProductos implements EJBProductosRemote {
    private static final Logger LOGGER = Logger.getLogger(EJBProductos.class);    
    @PersistenceContext(unitName="EJBMelaniPU2")    
    private EntityManager em; 
    private final Imagen imagen = new Imagen();   
//    @Override
//    public long addExistenciasProducto(long idproducto, int cantidad,float precio,int idusuario) {
//        long retorno;        
//                GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
//                    Productos producto =em.find(Productos.class, idproducto);
//                    producto.setCantidadDisponible(BigInteger.valueOf(producto.getCantidadDisponible().intValue()+cantidad));
//                        ExistenciasProductos existencias = new ExistenciasProductos();
//                            existencias.setCantidadactual(cantidad);
//                            existencias.setCantidadinicial(0);
//                            existencias.setIdUsuario(idusuario);
//                            existencias.setFechaagregado(gc.getTime());
//                            existencias.setProductos(em.find(Productos.class, producto.getSid()));
//                    if(precio!=0){
//                        producto.setPrecioUnitario(BigDecimal.valueOf(precio));
//                        existencias.setPreciounitario(BigDecimal.valueOf(precio));
//                    }else {
//                        existencias.setPreciounitario(BigDecimal.valueOf(0));
//                }
//                        Query obtenerLasExistenciasDelProducto = em.createQuery("SELECT e FROM ExistenciasProductos e WHERE e.productos.sid = :sid");
//                            obtenerLasExistenciasDelProducto.setParameter("sid", producto.getSid());
//                            List<ExistenciasProductos>lista = obtenerLasExistenciasDelProducto.getResultList();
//                                producto.setExistenciasProductoss(lista);
//                                retorno = producto.getSid();
//                    em.merge(producto);
//                    em.persist(existencias);        
//            return retorno;
//    }
    
    @Override
    public String addProducto(String xmlProducto) {
        String retorno = null;
        Productos producto = null;
        long idproduct;        
        idproduct =agregarProductoyProcesar(producto, xmlProducto);
        if(idproduct>0){
            retorno+=searchAllProductos();
        }else{
            retorno+="<Lista>\n"+"<producto>\n"+"<id>"+idproduct+"</id>\n";
                    retorno+="</producto>\n"+"</Lista>\n";
        }        
            return retorno;        
    }    
    private long agregarProductoyProcesar(Productos producto, String xmlProducto) {
        long retorno;
                XStream xstream = new XStream(new StaxDriver());
                xstream.alias("producto", DatosProductos.class);
                DatosProductos datosprod = (DatosProductos) xstream.fromXML(xmlProducto);                
                retorno = procesarProducto(producto,datosprod);                
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
        String xml;        
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
        int resultado;        
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
                                        Query obtenerExistenciasDeUnProducto = em.createQuery("SELECT e FROM ExistenciasProductos e WHERE e.productos.sid = :idproducto");
                                        obtenerExistenciasDeUnProducto.setParameter("idproducto", producto.getSid());
                                            List<ExistenciasProductos>lista = obtenerExistenciasDeUnProducto.getResultList();
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
         long retorno;        
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
        int retorno;
        Productos producto = em.find(Productos.class, id_producto);
        retorno= grabarPathImagenEnBaseDeDatos(producto,imagen.procesarImagen(longitudImagen, nameImage, magnitud));
        return retorno; 
    }
    @Override
    public byte[] obtenerImagenProducto(long idProducto) {        
            Query buscarImagenesDelProducto = em.createNamedQuery("ImagenesProductos.findById");
                buscarImagenesDelProducto.setParameter("sid", idProducto);
            List<ImagenesProductos>lista = buscarImagenesDelProducto.getResultList();
            return imagen.obtenerImagenByteArray(lista);   
    }
    private int grabarPathImagenEnBaseDeDatos(Productos producto, String[] procesarImagen) {
        int retorno;        
            ImagenesProductos imgProd =new ImagenesProductos();            
                    imgProd.setExtension(procesarImagen[1]);            
                    imgProd.setMagnitud(procesarImagen[2]);            
                    imgProd.setNombreImagen(procesarImagen[3]);            
                    imgProd.setPathImagenEnDisco(procesarImagen[0]);            
                    imgProd.setProductos(producto);            
                    em.persist(imgProd);  
                Query obtenerImagenPorIdProducto = em.createNamedQuery("ImagenesProductos.findByIdProduct");
                    obtenerImagenPorIdProducto.setParameter("idProducto", producto.getSid());
               List<ImagenesProductos>lista = obtenerImagenPorIdProducto.getResultList();               
               producto.setImagenesProductosList(lista);               
               em.merge(producto);            
            retorno=Integer.valueOf(String.valueOf(producto.getSid())); 
            return retorno;
    }
    public String actualizarPathImagenesEnBaseDeDatos(){       
            Query listaDeImagenes = em.createQuery("Select i FROM ImagenesProductos i");        
                List<ImagenesProductos> listado = listaDeImagenes.getResultList();        
            for(ImagenesProductos imagenesGuardadas:listado){        
                imagenesGuardadas.setPathImagenEnDisco(ResourceBundle.getBundle("config").getString("PATH_IMAGE")+imagenesGuardadas.getNombreImagen());
            }        
            em.flush();
        return "Echo";
    }
    private long procesarProducto(Productos producto,DatosProductos datosprod) {
        GregorianCalendar calendario = new GregorianCalendar(Locale.getDefault());        
        String descripcionDeProducto=datosprod.getDescripcion();
        String codigo =datosprod.getCodproducto();
        long retorno;
                if(!descripcionDeProducto.isEmpty()){
                    if(codigo.length()>0){            
                             producto =em.find(Productos.class, datosprod.getIdproducto());
                                        Query buscarProductoPorCodigoDeProducto = em.createQuery("SELECT p FROM Productos p WHERE LOWER(p.codproducto) LIKE LOWER(:codigoproducto)");
                                        buscarProductoPorCodigoDeProducto.setParameter("codigoproducto", codigo.concat("%"));
                                        Query obtenerProductoPorDescripcion = em.createQuery("SELECT p FROM Productos p WHERE LOWER(p.descripcion) LIKE LOWER(:descripcion)");
                                        obtenerProductoPorDescripcion.setParameter("descripcion", descripcionDeProducto.concat("%"));
                                            if(buscarProductoPorCodigoDeProducto.getResultList().isEmpty()){
                                                if(obtenerProductoPorDescripcion.getResultList().isEmpty()){
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
}