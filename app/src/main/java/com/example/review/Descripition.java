package com.example.review;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Descripition extends AppCompatActivity {

    ImageButton privatenextbttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripition);

        TextView revType = findViewById(R.id.reviewtype);
        TextView revDescription = findViewById(R.id.revdescripiton);
        TextView HowTo = findViewById(R.id.howtorev);

        privatenextbttn= findViewById(R.id.privatenextbtn);

        privatenextbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Descripition.this, PostActivity.class);
                startActivity(intent);
            }
        });





    }
}