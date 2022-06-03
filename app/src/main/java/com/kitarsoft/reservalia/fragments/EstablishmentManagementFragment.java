package com.kitarsoft.reservalia.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.activities.UserRegisterActivity;
import com.kitarsoft.reservalia.database.DBManager;
import com.kitarsoft.reservalia.models.Place;
import com.kitarsoft.reservalia.models.User;

public class EstablishmentManagementFragment extends Fragment {

    private final String COLLECTION = "Establishments";

    private Spinner spnNumeroMesa;
    private Button btnLocation;
    private ImageButton btnAddPlace, btnDelPlace, btnUpdPlace;
    private TextView txtNombreLocal, txtPosicionLocal, txtPuntuacionLocal, txtPrecioLocal;

    private Place place;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng position;

    public EstablishmentManagementFragment() {
        // Required empty public constructor
    }

    public static EstablishmentManagementFragment newInstance() {
        EstablishmentManagementFragment fragment = new EstablishmentManagementFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        place = new Place();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_establishment_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spnNumeroMesa = (Spinner) view.findViewById(R.id.spnNumeroMesa);
        String[] valores = {"-", "1","2","3","4"};
        spnNumeroMesa.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, valores));
        spnNumeroMesa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        btnAddPlace = (ImageButton) view.findViewById(R.id.btnAddLocal);
        btnDelPlace = (ImageButton) view.findViewById(R.id.btnUpdLocal);
        btnUpdPlace = (ImageButton) view.findViewById(R.id.btnDelLocal);

        btnAddPlace.setOnClickListener(view1 -> createLocal());

        txtNombreLocal = (TextView) view.findViewById(R.id.txtNombreLocal);
        txtPosicionLocal = (TextView) view.findViewById(R.id.txtPosicionLocal);
        txtPuntuacionLocal = (TextView) view.findViewById(R.id.txtPuntuacionLocal);
        txtPrecioLocal = (TextView) view.findViewById(R.id.txtPrecioLocal);

        btnLocation = (Button) view.findViewById(R.id.btnLocalUbicacion);
        btnLocation.setOnClickListener(view1 -> getCurrentLocation());
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            position = new LatLng(location.getLatitude(), location.getLongitude());
                            txtPosicionLocal.setText(
                                    "Lat: " + position.latitude + "\nLng: " + position.longitude
                            );
                        }
                    }
                });
    }

    private void createLocal(){
        Place newPlace = new Place();
        newPlace.setName(txtNombreLocal.getText().toString());
        newPlace.setPrice(Float.parseFloat(txtPrecioLocal.getText().toString()));
        newPlace.setRating(Float.parseFloat(txtPuntuacionLocal.getText().toString()));

        if(position != null){
            newPlace.setPosition(position);
        }

        //if(checkValidations()) {
            Task<DocumentReference> reference;
            reference = DBManager.create(newPlace, COLLECTION);

            if(reference!=null){
                //  Si tiene éxito
                Toast.makeText(getActivity(), "Local creado con éxito", Toast.LENGTH_LONG).show();
            }else{
                //  Si no tiene éxito
                Toast.makeText(getActivity(), "Error al crear el local", Toast.LENGTH_LONG).show();
            }
//        }else{
//            Toast.makeText(UserRegisterActivity.this, "Por favor revise e introduzca todos los datos de manera correcta", Toast.LENGTH_LONG).show();
//        }
    }

    private void readLocal(){

    }

    private void updateLocal(){

    }

    private void deleteLocal(){

    }
}