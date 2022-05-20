package com.kitarsoft.reservalia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kitarsoft.reservalia.utils.PermissionUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Reservar_buscar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reservar_buscar extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY1 = "param1";
    private static final String KEY2 = "param2";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean permissionDenied = false;
    private GoogleMap map;

    public Reservar_buscar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reservar_buscar.
     */
    // TODO: Rename and change types and number of parameters
    public static Reservar_buscar newInstance(String param1, String param2) {
        Reservar_buscar fragment = new Reservar_buscar();
        Bundle args = new Bundle();
        args.putString(KEY1, param1);
        args.putString(KEY2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(KEY1);
            //Log.println(Log.INFO,"PAR1", mParam1);
            mParam2 = getArguments().getString(KEY2);
            //Log.println(Log.INFO,"PAR2", mParam2);
        }

        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);
        map.setOnMyLocationClickListener((GoogleMap.OnMyLocationClickListener) this);

        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }
}