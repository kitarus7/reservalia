package com.kitarsoft.reservalia.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.activities.EstablishmentManagementActivity;
import com.kitarsoft.reservalia.dao.UserDao;
import com.kitarsoft.reservalia.models.User;

public class ProfileFragment extends Fragment {

    private static final String USER_ID = "userId";
    private String userId;

    private UserDao userDao = new UserDao();

    private Button btnGestionLocal, btnGestionReservas, btnModificarPerfil;
    private TextView txtNombre, txtApellidos, txtTelefono;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getUserData();

        btnGestionLocal = (Button) view.findViewById(R.id.btnGestionLocal);
        btnGestionLocal.setOnClickListener(v -> goEstablishmentManagement());

        btnModificarPerfil = (Button) view.findViewById(R.id.btnModificarPerfil);
        btnModificarPerfil.setOnClickListener(v -> updateProfile());

        txtNombre = (TextView) view.findViewById(R.id.txtNombrePerfil);
        txtApellidos = (TextView) view.findViewById(R.id.txtApellidosPerfil);
        txtTelefono = (TextView) view.findViewById(R.id.txtTelefonoPerfil);

    }

    @Override
    public void onStop() {
        super.onStop();
        clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserData();
    }

    private void goEstablishmentManagement(){
        Intent i = new Intent(getActivity(), EstablishmentManagementActivity.class);
        i.putExtra("userId", userId);
        startActivity(i);
    }

    private void getUserData(){
        userDao.getUsers(userId, "correo", userId, users -> {
            txtNombre.setText(users.get(0).getNombre());
            txtApellidos.setText(users.get(0).getApellidos());
            txtTelefono.setText(users.get(0).getTelefono());

            //  Escondemos el botón getión local si no es propietario
            if(!users.get(0).isEsPropietario()){
                btnGestionLocal.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void updateProfile(){
        userDao.getUsers(userId, "correo", userId, users -> {
            User userMod = users.get(0);
            userMod.setTelefono(txtTelefono.getText().toString());
            userDao.update(userMod, userId);
        });
    }

    private void clear(){
        txtNombre.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");

    }
}