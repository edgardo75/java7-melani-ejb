
package com.melani.utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.Collections;
import java.util.List;

@XStreamAlias("detallesnotapedido")
    public class DetallesNotaPedido{
    @XStreamImplicit
    private List<Itemdetallesnota> list;

    public List<Itemdetallesnota> getDetallesnota() {
        return Collections.unmodifiableList(list);
    }

    public void setDetallesnota(List<Itemdetallesnota> list) {
        this.list = list;
    }
    }
