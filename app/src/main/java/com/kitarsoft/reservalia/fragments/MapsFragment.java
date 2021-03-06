package com.kitarsoft.reservalia.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;
import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.activities.EstablishmentActivity;
import com.kitarsoft.reservalia.dao.EstablishmentDao;
import com.kitarsoft.reservalia.models.Establishment;
import com.kitarsoft.reservalia.utils.Scale;

import java.util.ArrayList;
import java.util.List;


public class MapsFragment extends Fragment {

    private final float MAX_ZOOM = 10.5f;
    private final float MIN_ZOOM = 7.5f;

    private float zoom = MAX_ZOOM;

    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private Location currentLocation;
    private Circle circle;

    private Context context;
    private Button btnBuscar;
    private SeekBar selectorRadio;
    private TextView valorRadio;

    private String userId;
    private static final String USER_ID = "userId";

    private EstablishmentDao establishmentDao = new EstablishmentDao();
    private List<Establishment> listaLocales;

    /**
     * Funci??n que se llama al tener el mapa listo
     */
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap gmap) {
            googleMap = gmap;
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(@NonNull Marker marker) {
                    Intent EstablishmentActivityIntent = new Intent(getActivity(), EstablishmentActivity.class);
                    EstablishmentActivityIntent.putExtra("userId", userId);
                    startActivity(EstablishmentActivityIntent);
                }
            });
            getCurrentLocation(zoom);
            init();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = getActivity();
        if (getArguments() != null) {
            userId = getArguments().getString(USER_ID);
        }
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        getEstablishmentsData();
    }

    /**
     * Inicializaci??n de elementos del front
     */
    private void init(){
        btnBuscar = (Button) getActivity().findViewById(R.id.btnBuscarAqui);
        btnBuscar.setOnClickListener(v -> searchHere(currentLocation));

        selectorRadio = (SeekBar) getActivity().findViewById(R.id.selectorRadio);
        selectorRadio.setMax(10);
        selectorRadio.setProgress(1);
        selectorRadio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean userAction) {
                zoom = Scale.scaleValues(MAX_ZOOM,MIN_ZOOM, 10, 1, selectorRadio.getProgress());
                LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,zoom));
                if(circle != null){
                    circle.remove();
                }
                showRadiusInMap(currentLatLng);
                valorRadio.setText("Buscar a " + getSeekBarRoundedValue(progress) + " km de la posici??n");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        valorRadio = (TextView) getActivity().findViewById(R.id.txtradioSeleccionado);
        valorRadio.setText("Buscar a " + getSeekBarRoundedValue(selectorRadio.getProgress()) + " km de la posici??n");
    }

    /**
     *  Escala el valor de la seekbar (1...10) a KMs (10...100)
     * @param value
     * @return
     */
    private int getSeekBarRoundedValue(int value){
        if (value < 1){
            value = 1;
            selectorRadio.setProgress(1);
        }
        return value * 10;
    }

    /**
     * Refresca la ubicaci??n actual y mueve la c??mara hacia ella
     */
    @SuppressLint("MissingPermission")
    private void getCurrentLocation(float zoom){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,zoom));
                            getNearbyMarkers(/*dummyMarkerData()*/listaLocales, location,
                                    Float.parseFloat(String.valueOf(
                                            getSeekBarRoundedValue(selectorRadio.getProgress()))));
                        }
                    }
                });
    }

    /**
     * Muestra en el mapa los marcadores cercanos seg??n un r??dio
     * @param establishments    Lista de lugares a verificar si est??n en rango
     * @param searchLocation    Localizaci??n desde la que buscar
     * @param radius    Radio m??ximo de b??squeda
     */
    private void getNearbyMarkers(List<Establishment> establishments, Location searchLocation, float radius ){
        for (Establishment establishment : establishments) {
            float[] result = new float[1];

            Location.distanceBetween(
                    searchLocation.getLatitude(), searchLocation.getLongitude(),
                    establishment.getPosicion().getLatitude(), establishment.getPosicion().getLongitude(), result);
            //  El radio viene dado en KMs el result es en m, por lo que se convierte a KMs
            if(result[0]<=radius*1000){
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(establishment.getPosicion().getLatitude(), establishment.getPosicion().getLongitude()))
                        .title(establishment.getNombre())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                        //.snippet("Precio medio: " + establishment.getPrecio() + "??? / Valoraci??n: " + establishment.getPuntuacion()));
                marker.setTag(establishment.getUserId());
            }
        }
    }

    private void getEstablishmentsData(){
        establishmentDao.getEstablishments(null, null, new EstablishmentDao.ReadEstablishments() {
            @Override
            public void onCallback(List<Establishment> results) {
                listaLocales = results;
            }
        });
    }

//    /**
//     * Datos de prueba en local
//     *
//     * @return
//     */
//    private List<Establishment> dummyMarkerData(){
//        GeoPoint[] dummyLatLangs = {
//                new GeoPoint(43.43468212355435, -4.011860418191267),//  <10km
//                new GeoPoint(43.44178428605581, -4.022742262928622),//  <10km
//                new GeoPoint(43.47146733109299, -3.820208111560402),// <20km
//                new GeoPoint(43.46199869267057, -3.719614556439797), //   <30km
//                new GeoPoint(43.46374302578148, -3.4600624512940326),//   <50km
//                new GeoPoint(43.41064289102547, -3.3350929834991514),//  <60km
//                new GeoPoint(43.371473523809215, -3.2111534659233523),//  <70km
//                new GeoPoint(43.253063395460835, -3.1019768259618408),//  <80km
//                new GeoPoint(43.23505686466661, -2.867487412489577),//   <100km
//                new GeoPoint(41.67548651039684, 0.9108348399817848)// >100km
//        };
//
//        List<Establishment> dummyEstablishments = new ArrayList<Establishment>();
//
//        for(int i=0; i<10;i++){
//            Establishment establishment = new Establishment();
//            establishment.setUserId("Local" + i);
//            establishment.setNombre("Restaurante "+i);
//            establishment.setPrecio(20.0f);
//            establishment.setPuntuacion(4.5f);
//            establishment.setPosicion(dummyLatLangs[i]);
//
//            dummyEstablishments.add(establishment);
//        }
//        return dummyEstablishments;
//    }

    /**
     * Refresca los marcadores seg??n el r??dio que le indicamos
     * @param location
     */
    private void searchHere(Location location){
        googleMap.clear();
        getCurrentLocation(zoom);
        Float newRadius = 10f;
        newRadius = Float.parseFloat(
                String.valueOf(
                        getSeekBarRoundedValue(selectorRadio.getProgress())));
        //getNearbyMarkers(dummyMarkerData(), location, newRadius);
        getNearbyMarkers(listaLocales, location, newRadius);
        showRadiusInMap(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
    }

    private void showRadiusInMap(LatLng currentLatLng){
        CircleOptions circleOptions = new CircleOptions()
                .center(currentLatLng)
                .radius(selectorRadio.getProgress()*10000)
                .strokeColor(Color.argb(100,255,255,255))
                .fillColor(Color.argb(100,74,255,181));

        circle = googleMap.addCircle(circleOptions);
    }
}