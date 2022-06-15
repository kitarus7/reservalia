package com.kitarsoft.reservalia.fragments;

import static com.kitarsoft.reservalia.utils.Utils.ocultarTeclado;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;
import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.dao.EstablishmentDao;
import com.kitarsoft.reservalia.dao.TableDao;
import com.kitarsoft.reservalia.models.Establishment;
import com.kitarsoft.reservalia.models.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EstablishmentManagementFragment extends Fragment {

    private String userId;

    private static final int MODE_ESTABLISHMENT = 0;
    private static final int MODE_TABLE = 1;

    private static final String USER_ID = "userId";
    private EstablishmentDao establishmentDao = new EstablishmentDao();
    private TableDao tableDao = new TableDao();

    private Spinner spnNumeroMesa;
    private Button btnLocation;
    private ImageButton btnAddPlace, btnDelPlace, btnUpdPlace, btnAddTable, btnDelTable, btnUpdTable;
    private TextView txtNombreLocal, txtTelefonoLocal, txtPosicionLocal, txtPuntuacionLocal, txtPrecioLocal;
    private EditText txtNumMesa, txtTamanioMesa;
    private CheckBox chbModificarPosicion;
    private ConstraintLayout formMesas;
    private CheckBox chbTerraza;

    private FusedLocationProviderClient fusedLocationClient;
    private LatLng position;

    private List<Table> listaMesas = new ArrayList<Table>();

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
        if (getArguments() != null) {
            userId = getArguments().getString(USER_ID);
        }
        getCurrentLocation();
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
        spnNumeroMesa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long position) {
                if(position > 0){
                    txtNumMesa.setVisibility(View.INVISIBLE);
                    txtTamanioMesa.setText(String.valueOf(listaMesas.get((int)position-1).getTamaño()));
                    chbTerraza.setChecked(listaMesas.get((int)position-1).isTerraza());
                    chargedDataMode(MODE_TABLE);
                }else{
                    txtNumMesa.setVisibility(View.VISIBLE);
                    clearMesa();
                    readyToAddMode(MODE_TABLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        btnLocation = (Button) view.findViewById(R.id.btnLocalUbicacion);
        btnLocation.setOnClickListener(view1 -> getCurrentLocation());
        chbModificarPosicion = (CheckBox) view.findViewById(R.id.chbCambiarUbicacion);
        btnAddPlace = (ImageButton) view.findViewById(R.id.btnAddLocal);
        btnAddPlace.setOnClickListener(view1 -> createLocal());
        btnDelPlace = (ImageButton) view.findViewById(R.id.btnDelLocal);
        btnDelPlace.setOnClickListener(view1 -> deleteLocal());
        btnUpdPlace = (ImageButton) view.findViewById(R.id.btnUpdLocal);
        btnUpdPlace.setOnClickListener(view1 -> updateLocal());

        txtNombreLocal = (TextView) view.findViewById(R.id.txtNombreLocal);
        txtTelefonoLocal = (TextView) view.findViewById(R.id.txtTelefonoLocal);
        txtPosicionLocal = (TextView) view.findViewById(R.id.txtPosicionLocal);

        formMesas = (ConstraintLayout) view.findViewById(R.id.formMesasManagement);
        txtTamanioMesa = (EditText) view.findViewById(R.id.txtTamanioMesa);
        txtNumMesa = (EditText) view.findViewById(R.id.txtNumeroMesa);
        chbTerraza = (CheckBox) view.findViewById(R.id.chbMesaExterior);

        btnAddTable = (ImageButton) view.findViewById(R.id.btnAddMesa);
        btnAddTable.setOnClickListener(view1 -> createMesa());
        btnDelTable = (ImageButton) view.findViewById(R.id.btnDelMesa);
        btnDelTable.setOnClickListener(view1 -> deleteMesa());
        btnUpdTable = (ImageButton) view.findViewById(R.id.btnUpdMesa);
        btnUpdTable.setOnClickListener(view1 -> updateMesa());

        readLocal();
    }

    @Override
    public void onStop() {
        super.onStop();
        clearLocal();
        clearMesa();
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
//                            txtPosicionLocal.setText(
//                                    "Lat: " + position.latitude + " / Lng: " + position.longitude
//                            );
                        }
                    }
                });
    }

    private void createLocal(){
        Establishment newEstablishment = new Establishment();
        newEstablishment.setUserId(userId);
        newEstablishment.setNombre(txtNombreLocal.getText().toString());
        newEstablishment.setTelefono(txtTelefonoLocal.getText().toString());
//        newEstablishment.setPrecio(Float.parseFloat(txtPrecioLocal.getText().toString()));
//        newEstablishment.setPuntuacion(Float.parseFloat(txtPuntuacionLocal.getText().toString()));

        //  Modificamos la posición actual si se requiere
        if(chbModificarPosicion.isChecked() && position != null){
            newEstablishment.setPosicion(new GeoPoint(position.latitude, position.longitude));
        }
        if(!chbModificarPosicion.isChecked() && txtPosicionLocal.getText().toString().equals("SIN UBICACIÓN")){
            newEstablishment.setPosicion(new GeoPoint(0, 0));
        }

        //if(checkValidations()) {
            establishmentDao.create(newEstablishment, userId);
            Toast.makeText(getActivity(), "Local creado con éxito", Toast.LENGTH_LONG).show();
            chargedDataMode(MODE_ESTABLISHMENT);
            readLocal();
//        }else{
//            Toast.makeText(UserRegisterActivity.this, "Por favor revise e introduzca todos los datos de manera correcta", Toast.LENGTH_LONG).show();
//        }
    }

    @SuppressLint("ResourceAsColor")
    private void readLocal(){
        establishmentDao.getEstablishment(userId, result->{
            if(result!=null){
                txtNombreLocal.setText(result.getNombre());
                txtTelefonoLocal.setText(result.getTelefono());
                if(result.getPosicion() != null){
                    txtPosicionLocal.setText(
                            "Lat: " + result.getPosicion().getLatitude() +
                                    "\nLng: " + result.getPosicion().getLongitude());
                }else{
                    txtPosicionLocal.setText("SIN UBICACIÓN");
                }
                readMesas();
                chargedDataMode(MODE_ESTABLISHMENT);
            }else{
                readyToAddMode(MODE_ESTABLISHMENT);
            }
        });
    }

    private void updateLocal(){
        createLocal();
    }

    private void deleteLocal(){
        establishmentDao.delete(userId);
        clearLocal();
        readyToAddMode(MODE_ESTABLISHMENT);
        Toast.makeText(getActivity(), "Local eliminado con éxito", Toast.LENGTH_LONG).show();
        readLocal();
    }

    private void clearLocal(){
        txtNombreLocal.setText("");
        txtTelefonoLocal.setText("");
        txtPosicionLocal.setText("");
//        txtPrecioLocal.setText("0.0");
//        txtPuntuacionLocal.setText("0.0");
    }

    /**
     * Show the UI in add establishment mode
     */
    private void readyToAddMode(int mode){
        if(mode == MODE_ESTABLISHMENT){
            btnAddPlace.setVisibility(View.VISIBLE);
            btnUpdPlace.setVisibility(View.INVISIBLE);
            btnDelPlace.setVisibility(View.INVISIBLE);
            formMesas.setVisibility(View.INVISIBLE);
        }else{
            btnAddTable.setVisibility(View.VISIBLE);
            btnUpdTable.setVisibility(View.INVISIBLE);
            btnDelTable.setVisibility(View.INVISIBLE);
        }
        ocultarTeclado(getActivity(), txtNombreLocal);
    }

    /**
     * Show the UI charged data establishment mode
     */
    private void chargedDataMode(int mode){
        if(mode == MODE_ESTABLISHMENT){
            btnAddPlace.setVisibility(View.INVISIBLE);
            btnUpdPlace.setVisibility(View.VISIBLE);
            btnDelPlace.setVisibility(View.VISIBLE);
            formMesas.setVisibility(View.VISIBLE);
        }else{
            btnAddTable.setVisibility(View.INVISIBLE);
            btnUpdTable.setVisibility(View.VISIBLE);
            btnDelTable.setVisibility(View.VISIBLE);
        }
        ocultarTeclado(getActivity(), txtNombreLocal);
    }

    private void createMesa(){
        Table newTable = new Table();
        String numeroMesa;

        newTable.setTamaño(Integer.parseInt(txtTamanioMesa.getText().toString()));
        newTable.setTerraza(chbTerraza.isChecked());


        if(!txtNumMesa.getText().toString().equals("")){
            numeroMesa = txtNumMesa.getText().toString();
            newTable.setId(numeroMesa);
            listaMesas.add(newTable);
            Collections.sort(listaMesas, (o1, o2) -> o1.getId().compareTo(o2.getId()));
        }else{
            numeroMesa = String.valueOf(listaMesas.get(
                    spnNumeroMesa.getSelectedItemPosition()-1).getId());
            newTable.setId(numeroMesa);
        }

        //if(checkValidations()) {
        tableDao.create(newTable, userId, newTable.getId());
        clearMesa();
        readMesas();
        Toast.makeText(getActivity(), "Mesa creada con éxito", Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(UserRegisterActivity.this, "Por favor revise e introduzca todos los datos de manera correcta", Toast.LENGTH_LONG).show();
//        }
    }

    private void readMesas(){
        tableDao.getTables(userId, null, null, new TableDao.ReadTables() {
            @Override
            public void onCallback(List<Table> results) {
                listaMesas = results;
                rellenarSpinnerMesas();
            }
        });
    }

    private void updateMesa(){
        createMesa();
    }

    private void deleteMesa(){
        String numeroMesa;

        if(!txtNumMesa.getText().toString().equals("")){
            numeroMesa = txtNumMesa.getText().toString();
        }else{
            numeroMesa = String.valueOf(listaMesas.get(
                    spnNumeroMesa.getSelectedItemPosition()-1).getId());
        }

        tableDao.delete(userId, numeroMesa);
        clearMesa();
        Toast.makeText(getActivity(), "Mesa eliminada con éxito", Toast.LENGTH_LONG).show();
        readLocal();
    }

    private void clearMesa(){
        txtNumMesa.setText("");
        txtTamanioMesa.setText("");
        chbTerraza.setChecked(false);
    }

    private void rellenarSpinnerMesas(){
        List<String> numsMesas = new ArrayList<String>();
        numsMesas.add("---");
        for(Table mesa : listaMesas){
            numsMesas.add(String.valueOf(mesa.getId()));
        }

        spnNumeroMesa.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, numsMesas.toArray(new String[numsMesas.size()])));
    }
}