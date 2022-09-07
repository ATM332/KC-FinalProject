package com.example.review;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText email, password;
    TextView signUp, login_bttn;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        email = findViewById(R.id.emaillog);
        password= findViewById(R.id.passwordlog);
        signUp= findViewById(R.id.signupbttn);
        login_bttn= findViewById(R.id.login_bttn);

        auth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });


        login_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog pd = new ProgressDialog(Login.this);
                pd.setMessage("Please wait....");
                pd.show();


                String str_email= email.getText().toString();
                String str_password= password.getText().toString();

                if(str_email.isEmpty()){
                    email.setError("Email is required");
                    email.requestFocus();
                    return;

                }
                if(!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
                    email.setError("Please enter valied email");
                    email.requestFocus();
                    return;
                }
                if (str_password.isEmpty()){
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                }
                if(str_password.length() < 6){
                    password.setError("Password must be 6 characters or more!");
                    password.requestFocus();
                    return;
                }else{
                    auth.signInWithEmailAndPassword(str_email, str_password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child(auth.getCurrentUser().getUid());

                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                pd.dismiss();
                                                Intent intent = new Intent(Login.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                pd.dismiss();

                                            }
                                        });
                                    }else{
                                        pd.dismiss();
                                        Toast.makeText(Login.this, "Authentication Failed..!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }

            }
        });

    }
}