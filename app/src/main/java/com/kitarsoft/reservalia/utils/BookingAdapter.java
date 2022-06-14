package com.kitarsoft.reservalia.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.dao.BookingDao;
import com.kitarsoft.reservalia.models.BookingDto;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private RecyclerView bookingsRecyclerview;
    private RecyclerView.Adapter mAdapter;

    private List<BookingDto> bookingModelList;
    private BookingDao bookingDao = new BookingDao();
    private String documentName;


    public BookingAdapter(List<BookingDto> bookingModelList, String documentName, RecyclerView recyclerview, RecyclerView.Adapter adapter) {
        this.bookingModelList = bookingModelList;
        this.documentName = documentName;
        this.bookingsRecyclerview = recyclerview;
        this.mAdapter = adapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserva_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nombre.setText(bookingModelList.get(position).getNombre());
        holder.telefono.setText(bookingModelList.get(position).getTelefono());
        holder.comensales.setText(bookingModelList.get(position).getComensales());
        holder.mesa.setText(bookingModelList.get(position).getMesa());
        holder.fecha.setText(bookingModelList.get(position).getFecha());
        holder.getButton().setOnClickListener(view1 -> borrarReserva(position));
    }

    private void borrarReserva(int position){
        bookingDao.delete(documentName, bookingModelList.get(position).getIdReserva());
        bookingModelList.remove(position);
        bookingsRecyclerview.removeViewAt(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, bookingModelList.size());
    }

    @Override
    public int getItemCount() {
        return bookingModelList.size();
    }

    //  Coge los valores del view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombre, telefono, comensales, mesa, fecha;
        private ImageButton btnDelete;

        public ViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.txtNombreResGest);
            telefono = (TextView) v.findViewById(R.id.txtTelefonoResGest);
            comensales = (TextView) v.findViewById(R.id.txtComensalesResGest);
            mesa = (TextView) v.findViewById(R.id.txtMesaResGest);
            fecha = (TextView) v.findViewById(R.id.txtFechaResGest);
            btnDelete = (ImageButton) v.findViewById(R.id.btnDelResGest);
        }

        public ImageButton getButton(){
            return btnDelete;
        }
    }

}
