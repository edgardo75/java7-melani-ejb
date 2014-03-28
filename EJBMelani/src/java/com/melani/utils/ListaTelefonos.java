/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.List;
/**
 *
 * @author Edgardo
 * @version 1.0 Build 5600 Feb 20, 2013
 */
@XStreamAlias("listaTelefonos")
public class ListaTelefonos {
    private List<DatosTelefonos>list;

    /**
     *
     */
    public ListaTelefonos(){}

    /**
     *
     * @return
     */
    public List<DatosTelefonos> getList() {
        return list;
    }
}
