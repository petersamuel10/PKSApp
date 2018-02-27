package com.example.jesus.pksapp;

import android.content.Intent;
import android.content.IntentSender;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE =1 ;
    private CircleImageView mImageBtn;
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mRegBtn;
    private Button mLoginPageBtn;
    private  static Uri imageURI;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private ProgressBar mRegisterProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        references();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // go to login Activity
        mLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),LoginActivity.class));
                finish();
            }
        });

        // create anew account btn event
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 // chick if the user select profile image
                  if(imageURI!=null)
                  {
                    // show progressbar
                    mRegisterProgressBar.setVisibility(View.VISIBLE);

                    // get name ,email and password
                    final String name=mNameField.getText().toString();
                    String email=mEmailField.getText().toString();
                    String password=mPasswordField.getText().toString();
                    try {
                        // check name password email to not empty
                        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                            // create anew account
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull final Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                       // Toast.makeText(getApplication(),"donn",Toast.LENGTH_LONG).show();

                                        final String user_id = mAuth.getCurrentUser().getUid();

                                        // upload profile image with the user_id name
                                        StorageReference user_profile = mStorageRef.child(user_id + ".jpg");
                                        user_profile.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadtask) {
                                                if (uploadtask.isSuccessful()) {
                                                  //  Toast.makeText(getApplication(),"upload",Toast.LENGTH_LONG).show();
                                                   final String download_URI = uploadtask.getResult().getDownloadUrl().toString();
                                                           String token_id = FirebaseInstanceId.getInstance().getToken();
                                                           Map<String, Object> userMap = new HashMap<>();
                                                           userMap.put("name", name);
                                                           userMap.put("image", download_URI);
                                                           userMap.put("token_id",token_id);

                                                           // make anew document named with the user id and save name , image file and current user token
                                                           mFireStore.collection("users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                               @Override
                                                               public void onSuccess(Void aVoid) {
                                                                //disappear progressbar and go to profile fragment
                                                               mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                                               sendToMain();
                                                       }
                                                   });

                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Error " + uploadtask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        });

                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        }
                    }catch (Exception e)
                    {
                        Toast.makeText(getApplication(),"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
                        mRegisterProgressBar.setVisibility(View.INVISIBLE);
                    }

                }else
                  {
                      Toast.makeText(getApplication(),"Please choose profile Picture first",Toast.LENGTH_LONG).show();
                  }

            }
        });

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();

                // open gallery to select profile image
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Choose Profile Pic"),PICK_IMAGE);
            }
        });

    }

    private void sendToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE)
        {
            // handle image url
            imageURI = data.getData();
            mImageBtn.setImageURI(imageURI);
            //Toast.makeText(getApplication(),"Error "+imageURI.toString(),Toast.LENGTH_LONG).show();
        }

    }

    private void references() {
        mEmailField=(EditText)findViewById(R.id.email);
        mPasswordField=(EditText)findViewById(R.id.password);
        mNameField=(EditText)findViewById(R.id.name);
        mImageBtn= (CircleImageView) findViewById(R.id.imageButton);
        mRegBtn=(Button)findViewById(R.id.Reg);
        mLoginPageBtn=(Button)findViewById(R.id.backToLogin);
        mRegisterProgressBar=(ProgressBar)findViewById(R.id.registerProgressBar);
        mAuth=FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference().child("images");

    }
}
