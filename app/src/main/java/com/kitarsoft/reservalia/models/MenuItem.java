package com.kitarsoft.reservalia.models;

public class MenuItem {
    private String menuId;
    private boolean menu;
    private String categoria;
    private String nombre;
    private String descripcion;
    private String observaciones;   //  Ex: €/unidad ó /Kg
    private double precio;

    public MenuItem() {
    }

    public MenuItem(String menuId, boolean menu, String categoria, String nombre, String descripcion, String observaciones, double precio) {
        this.menuId = menuId;
        this.menu = menu;
        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.precio = precio;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isMenu() {
        return menu;
    }

    public void setMenu(boolean menu) {
        this.menu = menu;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }


}
