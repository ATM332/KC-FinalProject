package com.example.review;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity3 extends AppCompatActivity {

    Button loginbttn, registerbttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        loginbttn = findViewById(R.id.loginbtn);
        registerbttn= findViewById(R.id.registerbtn);

        loginbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent3 = new Intent(MainActivity3.this ,LoginActivity.class);
                startActivity(intent3);


            }
        });

        registerbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent4 = new Intent(MainActivity3.this, RegisterActivity.class);
                startActivity(intent4);
            }
        });


    }
}