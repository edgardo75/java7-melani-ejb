
package com.melani.utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.Collections;
import java.util.List;

@XStreamAlias("detallepresupuesto")
public class DetallesPresupuesto {
    @XStreamImplicit
    private List<ItemDetallesPresupuesto>lista;

    public List<ItemDetallesPresupuesto> getLista() {
        return Collections.unmodifiableList(lista);
    }
}
