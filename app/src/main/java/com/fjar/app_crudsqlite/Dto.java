package com.fjar.app_crudsqlite;

import java.io.Serializable;

public class Dto implements Serializable {
    private int Codigo;
    private String Descripcion;
    private double precio;
    private int idCategoria;

    public Dto() {

    }

    public Dto(int codigo, String descripcion, double precio, int categoria) {
        this.Codigo = codigo;
        this.Descripcion = descripcion;
        this.precio = precio;
        this.idCategoria = categoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getCodigo() {
        return Codigo;
    }

    public void setCodigo(int codigo) {
        this.Codigo = codigo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {

        this.Descripcion = descripcion;
    }

    public double getPrecio() {

        return precio;
    }

    public void setPrecio(double precio) {

        this.precio = precio;
    }
}
