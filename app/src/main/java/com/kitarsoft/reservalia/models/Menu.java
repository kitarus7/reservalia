package com.kitarsoft.reservalia.models;

import java.util.List;

public class Menu {
    private String name;
    private List<MenuItem> menuItems;
    private float menuPrice;
    private String menuDays;
    private String anotations;

    public Menu() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public float getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(float menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getMenuDays() {
        return menuDays;
    }

    public void setMenuDays(String menuDays) {
        this.menuDays = menuDays;
    }

    public String getAnotations() {
        return anotations;
    }

    public void setAnotations(String anotations) {
        this.anotations = anotations;
    }
}
