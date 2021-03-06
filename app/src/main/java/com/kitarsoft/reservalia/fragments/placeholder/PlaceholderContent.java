package com.kitarsoft.reservalia.fragments.placeholder;

import com.kitarsoft.reservalia.models.Menu;
import com.kitarsoft.reservalia.models.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<PlaceholderItem> ITEMS = new ArrayList<PlaceholderItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderItem>();

    private static Menu menu = new Menu();

//    static{
//        // private void dummyMenuData(){
//        dummyMenu.setName("Menú de prueba");
//        dummyMenu.setMenuDays("Lunes - Viernes");
//        dummyMenu.setAnotations("Viernes noche no hay menú");
//        dummyMenu.setMenuPrice(16.5f);
//
//        List<MenuItem> dummyMenuItems = new ArrayList<MenuItem>();
//
//        for(int i = 0; i < 10; i++){
//            dummyMenuItems.add(new MenuItem("id"+i,false, "Categorie "+i, "Item "+i, "Dummy item", "It's only a dummy menú item", 8.75f));
//        }
//        dummyMenu.setMenuItems(dummyMenuItems);
//        // }
//    }

    public static List<PlaceholderItem> getPlaceHolderItems(List<MenuItem>listaMenuItems){
        menu.setMenuItems(listaMenuItems);
        for (int i = 0; i < listaMenuItems.size(); i++) {
            addItem(createPlaceholderItem(i));
        }
        return ITEMS;
    }

//    static {
//        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createPlaceholderItem(i));
//        }
//    }

    private static void addItem(PlaceholderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.itemName, item);
    }

    private static PlaceholderItem createPlaceholderItem(int position) {
        return new PlaceholderItem(
                menu.getName(),
                menu.getMenuItems().get(position).getCategoria(),
                menu.getMenuItems().get(position).getNombre(),
                menu.getMenuItems().get(position).getDescripcion(),
                String.valueOf(menu.getMenuItems().get(position).getPrecio())
        );
    }

    public static class PlaceholderItem {
        public final String menuName;
        public final String category;
        public final String itemName;
        public final String itemDescription;
        public final String itemPrice;

        public PlaceholderItem(String menuName, String category, String itemName, String itemDescription, String itemPrice) {
            this.menuName = menuName;
            this.category = category;
            this.itemName = itemName;
            this.itemDescription = itemDescription;
            this.itemPrice = itemPrice;
        }

        @Override
        public String toString() {
            return menuName;
        }
    }
}