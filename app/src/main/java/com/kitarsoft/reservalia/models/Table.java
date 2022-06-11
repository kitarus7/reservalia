package com.kitarsoft.reservalia.models;

public class Table {
    private String id;
    private long tamaño;
    private boolean terraza;

    public Table() {
    }

    public Table(String id, long tamaño, boolean terraza) {
        this.id = id;
        this.tamaño = tamaño;
        this.terraza = terraza;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTamaño() {
        return tamaño;
    }

    public void setTamaño(long tamaño) {
        this.tamaño = tamaño;
    }

    public boolean isTerraza() {
        return terraza;
    }

    public void setTerraza(boolean terraza) {
        this.terraza = terraza;
    }

}
