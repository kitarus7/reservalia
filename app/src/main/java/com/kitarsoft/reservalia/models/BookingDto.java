package com.kitarsoft.reservalia.models;

import com.google.firebase.Timestamp;
import com.kitarsoft.reservalia.utils.Utils;

public class BookingDto {
    private String idReserva;
    private String nombre;
    private String telefono;
    private String mesa;
    private String fecha;
    private String comensales;

    public BookingDto() {
    }

    public BookingDto(Booking booking) {
        this.idReserva = booking.getId();
        this.nombre = booking.getNombre() + " " + booking.getApellidos();
        this.telefono = booking.getTelefono();
        this.mesa = booking.getMesaId();
        this.fecha = Utils.getDateFormated(booking.getFecha_reserva());
        this.comensales = String.valueOf(booking.getComensales());
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
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

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getComensales() {
        return comensales;
    }

    public void setComensales(String comensales) {
        this.comensales = comensales;
    }
}
