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

import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.activities.EstablishmentManagementActivity;

public class ProfileFragment extends Fragment {

    private Button btnGestionLocal, btnGestionReservas;

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
        btnGestionLocal = (Button) view.findViewById(R.id.btnGestionLocal);
        btnGestionLocal.setOnClickListener(v -> goEstablishmentManagement());
    }

    private void goEstablishmentManagement(){
        Intent i = new Intent(getActivity(), EstablishmentManagementActivity.class);
        startActivity(i);
    }
}