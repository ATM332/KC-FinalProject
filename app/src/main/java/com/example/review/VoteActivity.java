package com.example.review;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.review.Adapters.CommentAdapter;
import com.example.review.Adapters.MainAdapter;
import com.example.review.Adapters.PostAdapter;
import com.example.review.Model.Comment;
import com.example.review.Model.Users;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoteActivity extends AppCompatActivity {

    private RecyclerView recyclerView2;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;


    TextView name, question;
    RecyclerView recyclerView;
    FloatingActionButton fbAdd;

    JSONArray jsonArray =new JSONArray();
    MainAdapter mainAdapter;


    EditText addcomment;
    ImageView image_profile;
    TextView post;

    String postid;
    String publisherid;

    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        recyclerView= findViewById(R.id.recycler_view5);
        fbAdd= findViewById(R.id.fb_add);
        name= findViewById(R.id.name);
        question= findViewById(R.id.questions);


        Intent intent2 = getIntent();
        String postname = intent2.getStringExtra("name");
        String postquestion = intent2.getStringExtra("question");

        name.setText(postname);
        question.setText(postquestion);


        recyclerView.setLayoutManager(new GridLayoutManager(
                this, 3

        ));

        mainAdapter= new MainAdapter(this,jsonArray);
        recyclerView.setAdapter(mainAdapter);

        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(VoteActivity.this);
                //set background transparent
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT
                ));
                //set view
                dialog.setContentView(R.layout.dialog_main);
                dialog.show();


                RatingBar ratingBar= dialog.findViewById(R.id.rating_bar);
                TextView tvRating= dialog.findViewById(R.id.tv_rating);
                Button btnSubmit= dialog.findViewById(R.id.bt_submit);

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        //set selected rating on txtview
                        tvRating.setText(String.format("(%s)",v));
                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get rating from rating bar
                        String sRating = String.valueOf(ratingBar.getRating());
                        try {
                            //add value in json array
                            jsonArray.put(new JSONObject().put("rating",sRating));
                            //set adapter
                            recyclerView.setAdapter(mainAdapter);
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });


        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView2 = findViewById(R.id.recycler_view4);
        recyclerView2.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(linearLayoutManager);
        commentList= new ArrayList<>();
        commentAdapter= new CommentAdapter(this, commentList);
        recyclerView2.setAdapter(commentAdapter);

        addcomment= findViewById(R.id.add_reply);
        image_profile= findViewById(R.id.image_profile);
        post = findViewById(R.id.post);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        publisherid= intent.getStringExtra("publisherid");


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addcomment.getText().toString().equals("")){
                    Toast.makeText(VoteActivity.this, "Your replies cant be empty..!", Toast.LENGTH_SHORT).show();
                }else{
                    addComment();
                }
            }
        });

        getImage();
        readComments();



    }

    private void addComment(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", addcomment.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());

        reference.push().setValue(hashMap);
        addcomment.setText("");
    }

    private void getImage(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if (users != null) {
                    Glide.with(getApplicationContext()).load(users.getImageurl()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readComments() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments");


        if (firebaseUser != null) {
            postid = firebaseUser.getUid();


            reference.child(postid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    commentList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Comment comment = snapshot1.getValue(Comment.class);
                        commentList.add(comment);
                    }

                    commentAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }




}