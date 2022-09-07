package com.example.review;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class Register extends AppCompatActivity {
    EditText username, fullname, email, password;
    TextView signin, register_bttn;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        username= findViewById(R.id.usernamereg);
        fullname= findViewById(R.id.fullnamereg);
        email= findViewById(R.id.emailreg);
        password= findViewById(R.id.passwordreg);
        register_bttn= findViewById(R.id.register_bttn);
        signin= findViewById(R.id.signin_bttn);

        auth = FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        register_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd= new ProgressDialog(Register.this);
                pd.setMessage("Please wait....");
                pd.show();

                String str_username =username.getText().toString();
                String str_fullname =fullname.getText().toString();
                String str_email =email.getText().toString();
                String str_password =password.getText().toString();


                if(str_username.isEmpty()){
                    username.setError("Please enter Username");
                    username.requestFocus();
                    return;

                }
                if(str_fullname.isEmpty()){
                    fullname.setError("Please enter Fullname");
                    fullname.requestFocus();
                    return;

                }
                if(str_email.isEmpty()){
                    email.setError("Please Enter Email");
                    email.requestFocus();
                    return;


                }
                if(!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
                    email.setError("Please provide valid Email");
                    email.requestFocus();
                    return;
                }
                if(str_password.isEmpty()){
                    password.setError("Please Enter Password");
                    password.requestFocus();
                    return;
                }
                if(str_password.length() < 6){
                    password.setError("Your Password should be 6 characters or more!");
                    password.requestFocus();
                    return;
                }else{
                    register(str_username, str_fullname, str_email, str_password);
                }
            }
        });


    }

    private void register(String username, String fullname, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid= firebaseUser.getUid();

                            reference= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username.toLowerCase());
                            hashMap.put("fullname",fullname);
                            hashMap.put("bio","");
                            hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/review-6c20f.appspot.com/o/profile.png?alt=media&token=0e9fafa3-224e-4d7b-a7bd-b69275969496");


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        pd.dismiss();
                                        Intent intent = new Intent(Register.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });

                        }else{
                            pd.dismiss();
                            Toast.makeText(Register.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}