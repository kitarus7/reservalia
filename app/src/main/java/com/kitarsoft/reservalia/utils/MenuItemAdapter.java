package com.kitarsoft.reservalia.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.dao.MenuItemDao;
import com.kitarsoft.reservalia.models.BookingDto;
import com.kitarsoft.reservalia.models.MenuItem;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

    private RecyclerView menuRecyclerview;
    private RecyclerView.Adapter mAdapter;

    private List<MenuItem> menuModelList;
    private MenuItemDao menuItemDao = new MenuItemDao();
    private String documentName;


    public MenuItemAdapter(List<MenuItem> menuModelList, String documentName, RecyclerView recyclerview, RecyclerView.Adapter adapter) {
        this.menuModelList = menuModelList;
        this.documentName = documentName;
        this.menuRecyclerview = recyclerview;
        this.mAdapter = adapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nombre.setText(menuModelList.get(position).getNombre());
        holder.descripcion.setText(menuModelList.get(position).getDescripcion());
        holder.precio.setText(String.valueOf(menuModelList.get(position).getPrecio()));
    }

    @Override
    public int getItemCount() {
        return menuModelList.size();
    }

    //  Coge los valores del view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombre, descripcion, precio;

        public ViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.menuItem_Name);
            descripcion = (TextView) v.findViewById(R.id.menuItem_Description);
            precio = (TextView) v.findViewById(R.id.menuItem_Price);
        }
    }

}
