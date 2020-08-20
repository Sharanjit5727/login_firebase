package com.example.login_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

public class UserProfile extends AppCompatActivity {
    ImageView image;
    TextView text;
    FirebaseAuth auth;
    DatabaseReference root;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        image = findViewById(R.id.img1);
        text = findViewById(R.id.tv_user);
        auth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
    }

    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(UserProfile.this, "Image Clicked", Toast.LENGTH_SHORT).show();
                            openImages();
                        }
                    });
                    showDetails();
                }
            }



        });
    }
    private  void openImages(){

Intent i= new Intent();
i.setType("image/*");
i.setAction(Intent.ACTION_GET_CONTENT);
startActivityForResult(i,1);
    }

    protected  void onActivityResult(int requestcode,int resultcode,Intent data)
    {
        super.onActivityResult(requestcode,resultcode,data);
        if(requestcode==1 && resultcode==RESULT_OK)
        {
            if(data.getData()!=null);
            {
                Uri myuri=data.getData();
                image.setImageURI(myuri);
                uploadImageFirebase(myuri);
            }
        }
    }
    private void uploadImageFirebase(Uri uri)
    {
        StorageReference myreference= storageReference.child(auth.getCurrentUser().getUid());
        myreference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(UserProfile.this, "Image Uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });

    }


    private void showDetails() {
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ds=dataSnapshot.child(auth.getCurrentUser().getUid());
               String getName= ds.getValue(String.class);
               text.setText("\n"+getName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
public void onBackPressed()
{
    auth.signOut();
    startActivity(new Intent(UserProfile.this,login.class));
    finish();
}


    public void doadd (View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View myview=getLayoutInflater().inflate(R.layout.add_details,null);
        final EditText edit=myview.findViewById(R.id.et_type);
        builder.setView(myview);
       builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               String info=edit.getText().toString();
               String uid=auth.getCurrentUser().getUid();
               root.child(uid).setValue(text.getText().toString()+"\n"+info);
               dialog.cancel();

           }
       });
       builder.show();
        }

    }

