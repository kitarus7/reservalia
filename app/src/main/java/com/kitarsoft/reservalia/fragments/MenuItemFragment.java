package com.kitarsoft.reservalia.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kitarsoft.reservalia.dao.BookingDao;
import com.kitarsoft.reservalia.models.Booking;
import com.kitarsoft.reservalia.models.BookingDto;
import com.kitarsoft.reservalia.models.MenuItem;
import com.kitarsoft.reservalia.utils.BookingAdapter;
import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.utils.MenuItemAdapter;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class MenuItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private String userId;
    private static final String USER_ID = "userId";

    private RecyclerView menuItemRecyclerview;
    private RecyclerView.Adapter mAdapter;

    private List<MenuItem>listaMenuItems;

    public MenuItemFragment() {}

    public MenuItemFragment(List<MenuItem>listaMenuItems) {
        this.listaMenuItems = listaMenuItems;
    }

    @SuppressWarnings("unused")
    public static MenuItemFragment newInstance(int columnCount) {
        MenuItemFragment fragment = new MenuItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
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
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        menuItemRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewMenuItem);
        menuItemRecyclerview.setHasFixedSize(true);
        menuItemRecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mAdapter = new MenuItemAdapter(listaMenuItems, userId, menuItemRecyclerview, mAdapter);
        menuItemRecyclerview.setAdapter(mAdapter);

//        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new MyMenuItemRecyclerViewAdapter(PlaceholderContent.getPlaceHolderItems(listaMenuItems)));
//        }
        return view;
    }
}