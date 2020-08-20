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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText et1,et2,et3,et4;
    Button register;
    FirebaseAuth auth;
    DatabaseReference root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1=findViewById(R.id.etname);
        et2=findViewById(R.id.etmail);
        et3=findViewById(R.id.etpwd);
        et4=findViewById(R.id.etrep) ;
        register=findViewById(R.id.regbutton);
        auth=FirebaseAuth.getInstance();
        root= FirebaseDatabase.getInstance().getReference();
    }

    public void loginb(View view) {
        Intent i= new Intent(getApplicationContext(),login.class);
        startActivity(i);
    }

    public void doregister(View view) {
        final String name=et1.getText().toString();
        String email=et2.getText().toString();
        String  password=et3.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            et2.setError("Enter the email");
            et2.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            et2.setError("Email is not valid");
            et2.requestFocus();
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
            et3.setError("Password must be of 6 characters");
            et3.requestFocus();
            return;
        }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    String uid= auth.getCurrentUser().getUid();
                    root.child(uid).setValue(name);
                    Toast.makeText(MainActivity.this, "User account created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,login.class));
                    finish();

                }
                else
                {
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}
