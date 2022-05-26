package com.kitarsoft.reservalia.models;

public class MenuItem {
    private long id;
    private boolean menu;
    private String categorie;
    private String name;
    private String description;
    private String anotation;   //  Ex: €/unidad ó /Kg
    private float price;

    public MenuItem() {
    }

    public MenuItem(long id, boolean menu, String categorie, String name, String description, String anotation, float price) {
        this.id = id;
        this.menu = menu;
        this.categorie = categorie;
        this.name = name;
        this.description = description;
        this.anotation = anotation;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isMenu() {
        return menu;
    }

    public void setMenu(boolean menu) {
        this.menu = menu;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


}
