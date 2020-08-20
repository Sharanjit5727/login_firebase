package com.example.login_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
EditText et1,et2;
Button login;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et1=findViewById(R.id.etmail_l);
        et2=findViewById(R.id.etpwd_l);
        login=findViewById(R.id.login_buttton);
        auth=FirebaseAuth.getInstance();
    }
    protected void onStart()
    {
        super.onStart();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    Toast.makeText(login.this, "User is not null", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(login.this,UserProfile.class));
                    finish();
                }
            }
        });
    }


    public void registerb(View view) {
        Intent i= new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    public void dologin(View view) {
        String email=et1.getText().toString();
        String  password=et2.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            et1.setError("Enter the email");
            et1.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            et1.setError("Email is not valid");
            et1.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            et2.setError("Enter the password");
            et2.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            et2.setError("Password must be of 6 characters");
            et2.requestFocus();
            return;
        }

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(login.this,UserProfile.class));
                    finish();
                }
                else
                {
                    Toast.makeText(login.this, "Login not Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
