package com.melani.utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.Collections;
import java.util.List;

@XStreamAlias("listaTelefonos")
public class ListaTelefonos {
    private List<DatosTelefonos>list;

    public ListaTelefonos(){}
    public List<DatosTelefonos> getList() {
        return Collections.unmodifiableList(list);
    }
}
