package com.kitarsoft.reservalia.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.dao.UserDao;
import com.kitarsoft.reservalia.models.User;
import com.kitarsoft.reservalia.utils.Checker;

public class UserRegisterActivity extends AppCompatActivity {

    private TextView emailTxt, passwordTxt, nameTxt, surnameTxt, phoneTxt;
    private Switch ownerSwitch;
    private Button registerBtn;

    private UserDao userDao = new UserDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        emailTxt = (TextView)findViewById(R.id.usernameRegister);
        passwordTxt = (TextView)findViewById(R.id.passwordRegister);
        nameTxt = (TextView)findViewById(R.id.nameRegister);
        surnameTxt = (TextView)findViewById(R.id.surnameRegister);
        phoneTxt = (TextView)findViewById(R.id.phoneRegister);
        registerBtn = (Button)findViewById(R.id.createUser);
        ownerSwitch = (Switch)findViewById(R.id.ownerRegister);

        registerBtn.setOnClickListener(v -> createNewUser());
    }

    private void createNewUser(){
        if(checkValidations()) {
            userDao.create(new User(
                    emailTxt.getText().toString(),
                    passwordTxt.getText().toString(),
                    nameTxt.getText().toString(),
                    surnameTxt.getText().toString(),
                    phoneTxt.getText().toString(),
                    ownerSwitch.isChecked()),
                        emailTxt.getText().toString());

            Toast.makeText(UserRegisterActivity.this, "Usuario creado con éxito", Toast.LENGTH_LONG).show();
            emailTxt.setText("");
            passwordTxt.setText("");
            nameTxt.setText("");
            surnameTxt.setText("");
            phoneTxt.setText("");
            ownerSwitch.setChecked(false);
            finish();
        }else{
            Toast.makeText(UserRegisterActivity.this, "Por favor revise e introduzca todos los datos de manera correcta", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkValidations(){

        boolean validation = true;

        GradientDrawable incorrect =  new GradientDrawable();
        incorrect.setColor(Color.parseColor("#75FF0000"));

        GradientDrawable correct =  new GradientDrawable();
        correct.setColor(Color.parseColor("#30ffffff"));

        if(!Checker.email(emailTxt.getText().toString())){
            emailTxt.setBackground(incorrect);
            validation = false;
        }else{
            emailTxt.setBackground(correct);
            validation = true;
        }

        if(!Checker.password(passwordTxt.getText().toString())){
            passwordTxt.setBackground(incorrect);
            validation = false;
        }else{
            passwordTxt.setBackground(correct);
            validation = true;
        }

        if(!Checker.phone(phoneTxt.getText().toString())){
            phoneTxt.setBackground(incorrect);
            validation = false;
        }else{
            phoneTxt.setBackground(correct);
            validation = true;
        }

        return validation;
    }
}