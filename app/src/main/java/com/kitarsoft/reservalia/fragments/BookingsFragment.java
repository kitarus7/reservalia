package com.kitarsoft.reservalia.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.dao.BookingDao;
import com.kitarsoft.reservalia.models.Booking;
import com.kitarsoft.reservalia.models.BookingDto;
import com.kitarsoft.reservalia.utils.BookingAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookingsFragment extends Fragment {

    private String userId;
    private static final String USER_ID = "userId";

    private ImageButton btnDelete;
    private RecyclerView bookingsRecyclerview;
    private RecyclerView.Adapter mAdapter;

    private BookingDao bookingDao = new BookingDao();
    private List<BookingDto> listaReservasDto = new ArrayList<>();
    private BookingDto bookingDto = new BookingDto();

    public BookingsFragment() {
        // Required empty public constructor
    }

    public static BookingsFragment newInstance(String param1, String param2) {
        BookingsFragment fragment = new BookingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            userId = getArguments().getString(USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookingsRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewResGest);
        bookingsRecyclerview.setHasFixedSize(true);
        bookingsRecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));

        bookingDao.getBookings(userId, null, null, new BookingDao.ReadBookings() {
            @Override
            public void onCallback(List<Booking> results){
                listaReservasDto.clear();
                for(Booking booking : results){
                    listaReservasDto.add(new BookingDto(booking));
                }
                mAdapter = new BookingAdapter(listaReservasDto, userId, bookingsRecyclerview, mAdapter);
                bookingsRecyclerview.setAdapter(mAdapter);
            }
        });
    }
}