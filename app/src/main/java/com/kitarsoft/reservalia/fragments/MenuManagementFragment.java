package com.kitarsoft.reservalia.fragments;

import static com.kitarsoft.reservalia.utils.Utils.ocultarTeclado;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.dao.EstablishmentDao;
import com.kitarsoft.reservalia.dao.MenuItemDao;
import com.kitarsoft.reservalia.models.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuManagementFragment extends Fragment {

    private String userId;
    private static final String USER_ID = "userId";

    private EstablishmentDao establishmentDao = new EstablishmentDao();
    private MenuItemDao menuItemDao = new MenuItemDao();

    private Spinner spnCategoria, spnProductos;
    private ImageButton btnAddMenuItem, btnDelMenuItem, btnUpdMenuItem;
    private EditText txtNombreMenuItem, txtDescripcionMenuItem, txtPrecioMenuItem;

    private List<MenuItem> listaItems = new ArrayList<MenuItem>();

    public MenuManagementFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_menu_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtNombreMenuItem = (EditText) view.findViewById(R.id.txtNombreItemMenu);
        txtDescripcionMenuItem = (EditText) view.findViewById(R.id.txtDescripcionItemMenu);
        txtPrecioMenuItem = (EditText) view.findViewById(R.id.txtPrecioItemMenu);

        btnAddMenuItem = (ImageButton) view.findViewById(R.id.btnAddItemMenu);
        btnAddMenuItem.setOnClickListener(view1 -> createMenuItem());
        btnDelMenuItem = (ImageButton) view.findViewById(R.id.btnDelItemMenu);
        btnDelMenuItem.setOnClickListener(view1 -> deleteMenuItem());
        btnUpdMenuItem = (ImageButton) view.findViewById(R.id.btnUpdItemMenu);
        btnUpdMenuItem.setOnClickListener(view1 -> updateMenuItem());

        spnCategoria = (Spinner) view.findViewById(R.id.spnCategoriaItemMenu);
        spnProductos = (Spinner) view.findViewById(R.id.spnProductosCreados);
        spnProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long position) {
                if(position > 0){
                    txtNombreMenuItem.setVisibility(View.INVISIBLE);
                    txtDescripcionMenuItem.setText(String.valueOf(listaItems.get((int)position-1).getDescripcion()));
                    txtPrecioMenuItem.setText(String.valueOf(listaItems.get((int)position-1).getPrecio()));

                    String[] categorias = getResources().getStringArray(R.array.categorias_item_menu);
                    int j = 0;
                    for(String categoria : categorias){
                        if(categoria.equals(listaItems.get((int)position-1).getCategoria())){
                            spnCategoria.setSelection(j);
                        }
                        j++;
                    }
                    chargedDataMode();
                }else{
                    txtNombreMenuItem.setVisibility(View.VISIBLE);
                    clearMenuItem();
                    readyToAddMode();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        readMenuItems();
    }

    @Override
    public void onStop() {
        super.onStop();
        clearMenuItem();
    }

    /**
     * Show the UI in add mode
     */
    private void readyToAddMode(){
        btnAddMenuItem.setVisibility(View.VISIBLE);
        btnUpdMenuItem.setVisibility(View.INVISIBLE);
        btnDelMenuItem.setVisibility(View.INVISIBLE);
        ocultarTeclado(getActivity(), txtNombreMenuItem);
    }

    /**
     * Show the UI charged data mode
     */
    private void chargedDataMode(){
        btnAddMenuItem.setVisibility(View.INVISIBLE);
        btnUpdMenuItem.setVisibility(View.VISIBLE);
        btnDelMenuItem.setVisibility(View.VISIBLE);
        ocultarTeclado(getActivity(), txtNombreMenuItem);
    }

    private void createMenuItem(){
        MenuItem newMenuItem = new MenuItem();

        //  Si el producto es nuevo se genera un id nuevo sino se coge el del item escogido
        if(spnProductos.getSelectedItemPosition()<1){
            //  Autoincremental id
            if(listaItems.size()>0){
                listaItems.get(listaItems.size()-1).getMenuId();
                List<MenuItem> listaItemsOrdenadaId = listaItems;
                Collections.sort(listaItemsOrdenadaId, (o1, o2) -> o1.getMenuId().compareTo(o2.getMenuId()));
                Integer nextId = Integer.parseInt(listaItemsOrdenadaId.get(listaItemsOrdenadaId.size()-1).getMenuId())+1;
                newMenuItem.setMenuId(String.valueOf(nextId));
            }else{
                newMenuItem.setMenuId(String.valueOf(1));
            }
            newMenuItem.setNombre(txtNombreMenuItem.getText().toString());
        }else{
            newMenuItem.setMenuId(listaItems.get(spnProductos.getSelectedItemPosition()-1).getMenuId());
            newMenuItem.setNombre(listaItems.get(spnProductos.getSelectedItemPosition()-1).getNombre());
        }


        newMenuItem.setDescripcion(txtDescripcionMenuItem.getText().toString());
        String precio = txtPrecioMenuItem.getText().toString().replace(",", ".");
        newMenuItem.setPrecio(Float.parseFloat(precio));
        newMenuItem.setMenu(false);
        newMenuItem.setCategoria(spnCategoria.getSelectedItem().toString());

        menuItemDao.create(newMenuItem, userId, newMenuItem.getMenuId());

        //if(checkValidations()) {

        Toast.makeText(getActivity(), "Producto creado con éxito", Toast.LENGTH_LONG).show();
        clearMenuItem();
        readMenuItems();
//        }else{
//            Toast.makeText(UserRegisterActivity.this, "Por favor revise e introduzca todos los datos de manera correcta", Toast.LENGTH_LONG).show();
//        }
    }

    private void readMenuItems(){
        menuItemDao.getMenuItems(userId, null, null, new MenuItemDao.ReadMenuItems() {
            @Override
            public void onCallback(List<MenuItem> results) {
                listaItems = results;
                rellenarSpinnerProductos();
            }
        });
    }

    private void updateMenuItem(){
        createMenuItem();
        clearMenuItem();
    }

    private void deleteMenuItem(){
        menuItemDao.delete(userId, listaItems.get(spnProductos.getSelectedItemPosition()-1).getMenuId());
        clearMenuItem();
        readMenuItems();
        Toast.makeText(getActivity(), "Producto eliminado con éxito", Toast.LENGTH_LONG).show();
        //readMenuItems();
    }

    private void clearMenuItem(){
        txtNombreMenuItem.setText("");
        txtDescripcionMenuItem.setText("");
        txtPrecioMenuItem.setText("");
        spnCategoria.setSelection(0);
        spnProductos.setSelection(0);
    }

    private void rellenarSpinnerProductos(){
        List<String> productos = new ArrayList<String>();
        productos.add("---");

        Collections.sort(listaItems, (o1, o2) -> o1.getNombre().compareTo(o2.getNombre()));

        for(MenuItem item : listaItems){
            productos.add(item.getNombre());
        }

        spnProductos.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, productos.toArray(new String[productos.size()])));
    }
}