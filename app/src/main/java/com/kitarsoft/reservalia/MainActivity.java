package com.kitarsoft.reservalia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.kitarsoft.reservalia.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private TextView loggedUserTxt;
    private Button disconnectBtn;

    private String loggedUser="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        loggedUser = i.getStringExtra("loggedUser");

        loggedUserTxt = (TextView)findViewById(R.id.loggedUser);
        disconnectBtn = (Button) findViewById(R.id.disconnect);

        loggedUserTxt.setText(loggedUser);
        disconnectBtn.setOnClickListener(view -> disconnect());

    }

    private void disconnect(){
        loggedUser = "Guest";
        loggedUserTxt.setText(loggedUser);

        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}