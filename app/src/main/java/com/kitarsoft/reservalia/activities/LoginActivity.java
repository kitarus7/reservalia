package com.kitarsoft.reservalia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
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
        userDao.getUser(userTxt.getText().toString().toLowerCase(), user -> {
            if (user != null) {
                String encriptedInputUser = Utils.md5(passwordTxt.getText().toString());
                String encriptedDBUser = user.getContrasenia();

                if(userTxt.getText().toString().toLowerCase().equals(user.getCorreo().toLowerCase()) &&
                        encriptedInputUser.equals(encriptedDBUser)){
                    Toast.makeText(LoginActivity.this, "Usuario correcto", Toast.LENGTH_SHORT).show();
                    Intent mainClassIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainClassIntent.putExtra("userId", user.getCorreo().toString());
                    startActivity(mainClassIntent);
                }else{
                    Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    passwordTxt.setText("");
                }
            }else{
                Toast.makeText(LoginActivity.this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();
            }

        });
    }
}