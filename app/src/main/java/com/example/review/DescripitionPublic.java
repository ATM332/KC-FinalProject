package com.example.review;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class DescripitionPublic extends AppCompatActivity {

    ImageButton publicnextbttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripition_public);



        publicnextbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentA = new Intent(DescripitionPublic.this, UploadActivity.class);
                startActivity(intentA);
            }
        });


        
        
        


    }
}