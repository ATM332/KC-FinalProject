package com.example.review;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView signup,login;
    private EditText edittextemail, edittextpassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.signup_TV);
        login = findViewById(R.id.loginbttn);

        edittextemail= findViewById(R.id.input_email2);
        edittextpassword= findViewById(R.id.input_pass2);

        progressBar= findViewById(R.id.progressbar);

        mAuth= FirebaseAuth.getInstance();




        signup.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_TV:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.loginbttn:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email= edittextemail.getText().toString();
        String password= edittextpassword.getText().toString();

        if(email.isEmpty()){
            edittextemail.setError("Email is required");
            edittextemail.requestFocus();
            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edittextemail.setError("Please enter valied email");
            edittextemail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            edittextpassword.setError("Password is required");
            edittextpassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            edittextpassword.setError("Password must be 6 characters or more!");
            edittextpassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()) {
                        //redirect to user profile
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(LoginActivity.this, "Failed to Login! Please check your credentails", Toast.LENGTH_LONG).show();

                }

            }
        });

    }
}