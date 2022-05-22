package com.kitarsoft.reservalia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
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
import com.kitarsoft.reservalia.models.Place;
import com.kitarsoft.reservalia.utils.Scale;

import java.util.ArrayList;
import java.util.List;


public class MapsFragment extends Fragment {

    private final float MAX_ZOOM = 10.5f;
    private final float MIN_ZOOM = 7.5f;

    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private Location currentLocation;
    private Circle circle;

    private Context context;
    private Button btnBuscar;
    private SeekBar selectorRadio;
    private TextView valorRadio;

    /**
     * Función que se llama al tener el mapa listo
     */
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap gmap) {
            googleMap = gmap;
            googleMap.setMyLocationEnabled(true);
            getCurrentLocation();
            init();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = getActivity();
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
    }

    /**
     * Inicialización de elementos del front
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
                float zoom = Scale.scaleValues(MAX_ZOOM,MIN_ZOOM, 10, 1, selectorRadio.getProgress());
                LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,zoom));
                if(circle != null){
                    circle.remove();
                }
                showRadiusInMap(currentLatLng);

                valorRadio.setText("Buscar a " + getSeekBarRoundedValue(progress) + " km de la posición");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        valorRadio = (TextView) getActivity().findViewById(R.id.txtradioSeleccionado);
        valorRadio.setText("Buscar a " + getSeekBarRoundedValue(selectorRadio.getProgress()) + " km de la posición");
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
     * Refresca la ubicación actual y mueve la cámara hacia ella
     */
    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,MAX_ZOOM));
                            getNearbyMarkers(dummyMarkerData(), location,
                                    Float.parseFloat(String.valueOf(
                                            getSeekBarRoundedValue(selectorRadio.getProgress()))));
                        }
                    }
                });
    }

    /**
     * Muestra en el mapa los marcadores cercanos según un rádio
     * @param places    Lista de lugares a verificar si están en rango
     * @param searchLocation    Localización desde la que buscar
     * @param radius    Radio máximo de búsqueda
     */
    private void getNearbyMarkers(List<Place> places, Location searchLocation, float radius ){
        for (Place place : places) {
            float[] result = new float[1];

            Location.distanceBetween(
                    searchLocation.getLatitude(), searchLocation.getLongitude(),
                    place.getPosition().latitude, place.getPosition().longitude, result);
            //  El radio viene dado en KMs el result es en m, por lo que se convierte a KMs
            if(result[0]<=radius*1000){
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getPosition().latitude, place.getPosition().longitude))
                        .title(place.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        .snippet("Precio medio: " + place.getPrice() + "€ / Valoración: " + place.getRating()));
            }
        }
    }

    /**
     * Datos de prueba en local
     *
     * @return
     */
    private List<Place> dummyMarkerData(){
        LatLng[] dummyLatLangs = {
                new LatLng(43.43468212355435, -4.011860418191267),//  <10km
                new LatLng(43.44178428605581, -4.022742262928622),//  <10km
                new LatLng(43.47146733109299, -3.820208111560402),// <20km
                new LatLng(43.46199869267057, -3.719614556439797), //   <30km
                new LatLng(43.46374302578148, -3.4600624512940326),//   <50km
                new LatLng(43.41064289102547, -3.3350929834991514),//  <60km
                new LatLng(43.371473523809215, -3.2111534659233523),//  <70km
                new LatLng(43.253063395460835, -3.1019768259618408),//  <80km
                new LatLng(43.23505686466661, -2.867487412489577),//   <100km
                new LatLng(41.67548651039684, 0.9108348399817848)// >100km
        };

        List<Place> dummyPlaces = new ArrayList<Place>();

        for(int i=0; i<10;i++){
            Place place = new Place();
            place.setId(i);
            place.setName("Restaurante "+i);
            place.setPrice(20.0f);
            place.setRating(4.5f);
            place.setPosition(dummyLatLangs[i]);

            dummyPlaces.add(place);
        }
        return dummyPlaces;
    }

    /**
     * Refresca los marcadores según el rádio que le indicamos
     * @param location
     */
    private void searchHere(Location location){
        googleMap.clear();
        Float newRadius = 10f;
        newRadius = Float.parseFloat(
                String.valueOf(
                        getSeekBarRoundedValue(selectorRadio.getProgress())));
        getNearbyMarkers(dummyMarkerData(), location, newRadius);
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