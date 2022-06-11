package com.kitarsoft.reservalia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.dao.UserDao;
import com.kitarsoft.reservalia.models.User;
import com.kitarsoft.reservalia.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private TextView userTxt;
    private TextView passwordTxt;
    private Button loginBtn;
    private Button createBtn;

    private ConstraintLayout layout;

    private UserDao userDao = new UserDao();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userTxt = (TextView)findViewById(R.id.usernameRegister);
        passwordTxt = (TextView)findViewById(R.id.passwordRegister);
        loginBtn = (Button)findViewById(R.id.login);
        createBtn = (Button)findViewById(R.id.createUser);

        loginBtn.setOnClickListener(v -> login());
        createBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, UserRegisterActivity.class));
        });

        layout = findViewById(R.id.layoutLoginActivity);
        layout.setOnClickListener(view -> Utils.ocultarTeclado(this, userTxt));
    }

    private void login(){
//        //      SALTARSE EL LOGING
//        Toast.makeText(LoginActivity.this, "Usuario correcto", Toast.LENGTH_SHORT).show();
//        Intent mainClassIntent = new Intent(LoginActivity.this, MainActivity.class);
//        mainClassIntent.putExtra("loggedUser", userTxt.getText().toString());
//        startActivity(mainClassIntent);
//        ///////////////////////////////////////////////////////////////////////////////////////////

        userDao.getUser(userTxt.getText().toString(), user -> {
            if(userTxt.getText().toString().equals(user.getCorreo().toString()) && passwordTxt.getText().toString().equals(user.getContrasenia().toString())){
                Toast.makeText(LoginActivity.this, "Usuario correcto", Toast.LENGTH_SHORT).show();
                Intent mainClassIntent = new Intent(LoginActivity.this, MainActivity.class);
                mainClassIntent.putExtra("userId", user.getCorreo().toString());
                startActivity(mainClassIntent);
            }else{
                Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}