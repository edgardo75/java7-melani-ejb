
package com.melani.utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.Collections;
import java.util.List;

@XStreamAlias("detallesnotapedido")
    public class DetallesNotaPedido{
    @XStreamImplicit
    private List<ItemDetallesNota> list;

    public List<ItemDetallesNota> getDetallesnota() {
        return Collections.unmodifiableList(list);
    }

    public void setDetallesnota(List<ItemDetallesNota> list) {
        this.list = list;
    }
    }
