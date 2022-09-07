package com.example.review;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdpater;
    private ImageButton exitbttn, addbttn;

    private DatabaseReference mDatabaseref;
    private List<Upload> mUploads;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mRecyclerView= findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        exitbttn= findViewById(R.id.exit_bttn);
        addbttn= findViewById(R.id.add_bttn);

        exitbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImagesActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        addbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ImagesActivity.this, UploadActivity.class);
                startActivity(intent1);
            }
        });





        mUploads= new ArrayList<>();

        mDatabaseref= FirebaseDatabase.getInstance().getReference("uploads");

        mDatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()){
                    Upload upload = postSnapShot.getValue(Upload.class);
                    mUploads.add(upload);
                }

                mAdpater= new ImageAdapter(ImagesActivity.this, mUploads);

                mRecyclerView.setAdapter(mAdpater);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImagesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}