package com.kitarsoft.reservalia.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.database.DBManager;
import com.kitarsoft.reservalia.models.User;
import com.kitarsoft.reservalia.utils.Checker;

public class UserRegisterActivity extends AppCompatActivity {

    private final String COLLECTION = "Users";

    private TextView emailTxt;
    private TextView passwordTxt;
    private TextView phoneTxt;
    private Switch ownerSwitch;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        emailTxt = (TextView)findViewById(R.id.usernameRegister);
        passwordTxt = (TextView)findViewById(R.id.passwordRegister);
        phoneTxt = (TextView)findViewById(R.id.phoneRegister);
        registerBtn = (Button)findViewById(R.id.createUser);
        ownerSwitch = (Switch)findViewById(R.id.ownerRegister);

        registerBtn.setOnClickListener(v -> create());
    }

    private void create(){

        if(checkValidations()) {

            Task<DocumentReference> reference;

            reference = DBManager.create(new User(
                    null,
                    emailTxt.getText().toString(),
                    passwordTxt.getText().toString(),
                    phoneTxt.getText().toString(),
                    ownerSwitch.isChecked()), COLLECTION);

            if(reference!=null){
                //  Si tiene éxito
                Toast.makeText(UserRegisterActivity.this, "Usuario creado con éxito", Toast.LENGTH_LONG).show();
                emailTxt.setText("");
                passwordTxt.setText("");
                phoneTxt.setText("");
                ownerSwitch.setChecked(false);
                finish();
            }else{
                //  Si no tiene éxito
                Toast.makeText(UserRegisterActivity.this, "Error al crear el usuario", Toast.LENGTH_LONG).show();
            }
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