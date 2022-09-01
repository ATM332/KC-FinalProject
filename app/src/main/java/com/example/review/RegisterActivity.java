package com.example.review;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView signIn, registeruser;
    private EditText inputEmail, inputPassword, inputName;
    private ProgressBar progressBar;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signIn = findViewById(R.id.signin_TV);

        inputEmail = findViewById(R.id.input_email1);
        inputPassword = findViewById(R.id.input_pass1);
        inputName = findViewById(R.id.input_username1);

        registeruser= findViewById(R.id.registeruser);

        progressBar= findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(this);
        registeruser.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signin_TV:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
            case R.id.registeruser:
                Registerbttn();
                break;
        }


    }

    private void Registerbttn(){

        String email= inputEmail.getText().toString();
        String username= inputName.getText().toString();
        String password= inputPassword.getText().toString();

        if(username.isEmpty()){
            inputName.setError("Please enter Username");
            inputName.requestFocus();
            return;

        }
        if(email.isEmpty()){
            inputEmail.setError("Please Enter Email");
            inputEmail.requestFocus();
            return;


        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Please provide valid Email");
            inputEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            inputPassword.setError("Please Enter Password");
            inputPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            inputPassword.setError("Your Password should be 6 characters or more!");
            inputPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(username, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()) {

                                                Toast.makeText(RegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);


                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Failed to register user. Please Try again", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register user. Please Try again", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });



    }
}