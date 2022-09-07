package com.example.review.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.review.Fragments.ProfileFragment;
import com.example.review.Model.Users;
import com.example.review.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    private Context aContext;
    private List<Users> aUsers;

    private FirebaseUser firebaseUser;

    public UsersAdapter(Context aContext, List<Users> aUsers){
        this.aContext= aContext;
        this.aUsers= aUsers;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(aContext).inflate(R.layout.user_item, parent ,false);
        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Users users = aUsers.get(position);

        holder.btn_follow.setVisibility(View.VISIBLE);

        holder.username.setText(users.getUsername());
        holder.fullname.setText(users.getFullname());
        Glide.with(aContext).load(users.getImageurl()).into(holder.image_profile);
        isFollowing(users.getId(), holder.btn_follow);


        if (users.getId().equals(firebaseUser.getUid())){
            holder.btn_follow.setVisibility(View.GONE);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor= aContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileId", users.getId());
                editor.apply();

                ((FragmentActivity)aContext).getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, new ProfileFragment()).commit();
            }
        });

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn_follow.getText().toString().equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(users.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(users.getId())
                            .child("followers").child(firebaseUser.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(users.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(users.getId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return aUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView fullname;
        public CircleImageView image_profile;
        public Button btn_follow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            fullname= itemView.findViewById(R.id.fullname);
            image_profile= itemView.findViewById(R.id.image_profile);
            btn_follow= itemView.findViewById(R.id.btn_follow);



        }
    }

    private void isFollowing(String userid, Button button){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userid).exists()){
                    button.setText("following");
                }else{
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
