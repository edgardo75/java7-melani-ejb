/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author Edgardo
 * @version 1.0 Build 5600 Feb 20, 2013
 */
@XStreamAlias("detallepresupuesto")
public class DetallesPresupuesto {
    @XStreamImplicit
    private List<ItemDetallesPresupuesto>lista;

    /**
     *
     * @return
     */
    public List<ItemDetallesPresupuesto> getLista() {
        return Collections.unmodifiableList(lista);
    }
}
