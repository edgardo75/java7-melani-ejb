package com.melani.utils;

import com.melani.entity.ImagenesProductos;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.log4j.Logger;

public class Imagen {
    private static final Logger LOGGER = Logger.getLogger(Imagen.class);    
    private static final String PATH_IMAGENES = System.getProperty("user.dir")+File.separator+"var"+File.separator+"webapp"+File.separator+"upload"+File.separator;        
  public Imagen(){}  
  
  public String[] procesarImagen(byte[] longitudImagen,String nameImage,String magnitud) {
        String arrayImageProperties[] = null;
        try {            
                String extension=nameImage.substring(nameImage.indexOf(".")+1, nameImage.length());       
                            String nameImg=nameImage.substring(0, nameImage.indexOf("."));                            
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
                arrayImageProperties = new String[]{imageFile.getPath(),extension,magnitud,nameImg};
                   
        } catch (IOException ex) {
            LOGGER.error("ERROR AL PROCESAR LA IMAGEN "+ex.getMessage());
        }       
        return arrayImageProperties;
    }
    
    public byte[] obtenerImagenByteArray(List<ImagenesProductos> listaDeImagenesProducto){
        byte[]retorno = new byte[5];
        String pathImage = "";        
            if(listaDeImagenesProducto.size()>0){  
                    for(ImagenesProductos imagen:listaDeImagenesProducto){
                           pathImage=PATH_IMAGENES+imagen.getNombreImagen()+"."+imagen.getExtension();             
                    }                                    
                                    File file = new File(pathImage);
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
                                                          LOGGER.error("Error al procesar imagen por defecto "+ex.getMessage());
                                                   }
                                           }
            }    
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
    public String pathImagenes(){
        return PATH_IMAGENES;
    }
}