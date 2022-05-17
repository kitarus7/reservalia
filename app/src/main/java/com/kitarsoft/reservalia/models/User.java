package com.kitarsoft.reservalia.models;

public class User {

    private String id;
    private String email;
    private String password;
    private String phone;
    private boolean owner;

    public User(String id, String email, String password, String phone, boolean owner) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.owner = owner;
    }

    public User() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }
}
