package com.example.review;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    TextView signIn;
    EditText inputEmail, inputPassword, inputName;
    Button registerbttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signIn = findViewById(R.id.signin_TV);

        inputEmail = findViewById(R.id.input_email1);
        inputPassword = findViewById(R.id.input_pass1);
        inputName = findViewById(R.id.input_username1);

        registerbttn = findViewById(R.id.registerbtn);



        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentlog = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intentlog);

            }
        });
        
        registerbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PerformAuth();
            }
        });
    }

    private void PerformAuth(){
        String email=inputEmail.getText().toString();

    }
}