package com.kitarsoft.reservalia.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kitarsoft.reservalia.activities.MainActivity;
import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.activities.UserRegisterActivity;

public class LoginActivity extends AppCompatActivity {

    /**
     * Credenciales de pruebas
     * TODO: remuévelas cuando vayas a implementar una autenticación real.
     */
    private static final String DUMMY_USER = "user@email.com";
    private static final String DUMMY_PASSWORD = "user";

    private TextView userTxt;
    private TextView passwordTxt;
    private Button loginBtn;
    private Button createBtn;

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
    }

    private void login(){

        String userLogged=null;

        //      SALTARSE EL LOGING

        Toast.makeText(LoginActivity.this, "Usuario correcto", Toast.LENGTH_SHORT).show();
        Intent mainClassIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainClassIntent.putExtra("loggedUser", userTxt.getText().toString());
        startActivity(mainClassIntent);

        ///////////////////////////////////////////////////////////////////////////////////////////

        /*DBManager.readData(new User(), "Users", "email", userTxt.getText().toString(), new DBManager.ReadUsers() {
            @Override
            public void onCallback(List<User> users) {
                for(User user : users){
                    if(userTxt.getText().toString().equals(user.getEmail().toString()) && passwordTxt.getText().toString().equals(user.getPassword().toString())){
                        Toast.makeText(LoginActivity.this, "Usuario correcto", Toast.LENGTH_SHORT).show();
                        Intent mainClassIntent = new Intent(LoginActivity.this, MainActivity.class);
                        mainClassIntent.putExtra("loggedUser", userTxt.getText().toString());
                        startActivity(mainClassIntent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/


    }

}