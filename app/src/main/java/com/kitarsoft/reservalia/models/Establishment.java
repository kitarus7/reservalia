package com.kitarsoft.reservalia.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Establishment {
    private String userId;
    private String nombre;
    private String telefono;
    private float puntuacion;
    private float precio;
    private GeoPoint posicion;

    public Establishment() {
    }

    public Establishment(String userId, String nombre, String telefono, float puntuacion, float precio, GeoPoint posicion) {
        this.userId = userId;
        this.nombre = nombre;
        this.telefono = telefono;
        this.puntuacion = puntuacion;
        this.precio = precio;
        this.posicion = posicion;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public GeoPoint getPosicion() {
        return posicion;
    }

    public void setPosicion(GeoPoint posicion) {
        this.posicion = posicion;
    }

}
