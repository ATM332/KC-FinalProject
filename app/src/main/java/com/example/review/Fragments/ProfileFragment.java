package com.example.review.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.review.Adapters.MyReviewAdapter;
import com.example.review.Model.Post;
import com.example.review.Model.Users;
import com.example.review.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProfileFragment extends Fragment {

    ImageView image_profile, options;
    TextView posts, followers, following, fullname, bio, username;
    Button edit_profile;
    RecyclerView recyclerView;
    MyReviewAdapter myReviewsAdapter;
    List<Post> postList;

    FirebaseUser firebaseUser;
    String profileid;

    ImageButton my_photos, saved_photos;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid= prefs.getString("profileid", "none");

        image_profile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.options);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        fullname= view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username);
        my_photos = view.findViewById(R.id.my_photos);
        saved_photos = view.findViewById(R.id.saved_photos);
        edit_profile= view.findViewById(R.id.edit_profile);

        recyclerView = view.findViewById(R.id.view_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(linearLayoutManager);

        postList = new ArrayList<>();

        myReviewsAdapter = new MyReviewAdapter(getContext(), postList);
        recyclerView.setAdapter(myReviewsAdapter);


        userInfo();
        getFollowers();
        getNmbrPosts();
        myPhotos();

        if(profileid.equals(firebaseUser.getUid())){
            edit_profile.setText("Edit Profile");
        }else{
            checkFollow();
            saved_photos.setVisibility(View.VISIBLE);
        }


        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = edit_profile.getText().toString();

                if(btn.equals("Edit Profile")){
                    //go to editprofile
                }else if(btn.equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);
                }else if (btn.equals("following")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });


        return view;
    }

    private void userInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(profileid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getContext() == null){
                    return;

                }

                Users users = snapshot.getValue(Users.class);


                if (users != null) {
                    Glide.with(getContext()).load(users.getImageurl()).into(image_profile);

                    username.setText(users.getUsername());

                    fullname.setText(users.getFullname());

                    bio.setText(users.getBio());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkFollow(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(profileid).exists()){
                    edit_profile.setText("following");
                }else{
                    edit_profile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFollowers(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("followers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("following");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNmbrPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i= 0;
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Post post = snapshot1.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)) {
                        i++;
                    }
                }
                posts.setText(""+i);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void myPhotos(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Post post= snapshot1.getValue(Post.class);
                    if(post.getPublisher().equals(profileid)){
                        postList.add(post);

                    }
                }
                Collections.reverse(postList);
                myReviewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }}