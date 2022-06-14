package com.kitarsoft.reservalia.models;

import com.google.firebase.Timestamp;

public class Booking {
    private String id;
    private String userId;
    private String mesaId;
    private String nombre;
    private String apellidos;
    private String telefono;
    private Timestamp fecha_reserva;
    private long comensales;
    private boolean terraza;

    public Booking() {
    }

    public Booking(String id, String userId, String mesaId, String nombre, String apellidos, String telefono, Timestamp fecha_reserva, long comensales, boolean terraza) {
        this.id = id;
        this.userId = userId;
        this.mesaId = mesaId;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.fecha_reserva = fecha_reserva;
        this.comensales = comensales;
        this.terraza = terraza;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Timestamp getFecha_reserva() {
        return fecha_reserva;
    }

    public void setFecha_reserva(Timestamp fecha_reserva) {
        this.fecha_reserva = fecha_reserva;
    }

    public long getComensales() {
        return comensales;
    }

    public void setComensales(long comensales) {
        this.comensales = comensales;
    }

    public boolean isTerraza() {
        return terraza;
    }

    public void setTerraza(boolean terraza) {
        this.terraza = terraza;
    }

    public String getMesaId() {
        return mesaId;
    }

    public void setMesaId(String mesaId) {
        this.mesaId = mesaId;
    }
}
