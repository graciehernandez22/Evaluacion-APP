package com.fjar.app_crudsqlite;

import java.util.Date;

public class DtoCategoria {
    private int idCategoria;
    private String nameCategoria;
    private int estadoCategoria;
    private Date fecha;

    public DtoCategoria() {

    }

    public DtoCategoria(int idCategoria, String nameCategoria, int estadoCategoria, Date fecha) {
        this.idCategoria = idCategoria;
        this.nameCategoria = nameCategoria;
        this.estadoCategoria = estadoCategoria;
        this.fecha = fecha;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNameCategoria() {
        return nameCategoria;
    }

    public void setNameCategoria(String nameCategoria) {
        this.nameCategoria = nameCategoria;
    }

    public int getEstadoCategoria() {
        return estadoCategoria;
    }

    public void setEstadoCategoria(int estadoCategoria) {
        this.estadoCategoria = estadoCategoria;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
