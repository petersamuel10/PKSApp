package com.example.jesus.pksapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn;
    private Button mRegPageBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mfirestore;
    private ProgressBar mRegisterProgressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        references();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null)
        {sendToMain();}

        // event of the login btn
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();

                // check the email and password text to not be empty
                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password))
                {
                    // show progressbar
                    mRegisterProgressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                // get current user token to save in the firebase to retrive it again and send notification with it
                                String token_id = FirebaseInstanceId.getInstance().getToken();
                                        String current_id= mAuth.getCurrentUser().getUid();

                                        Map<String , Object> tokenMap = new HashMap<>();
                                        tokenMap.put("token_id",token_id);

                                        // (update) method used to create anew field to save the token in the current user document in the users collection
                                        mfirestore.collection("users").document(current_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // if success go to profile fragment
                                                sendToMain();

                                                // disappear progressbar
                                                 mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                            }
                                        });
                            }else {
                                Toast.makeText(LoginActivity.this,"Error "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                mRegisterProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                }
            }
        });

        // sign up Btn
        mRegPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regPage=new Intent (getApplication(),RegisterActivity.class);
                startActivity(regPage);
                finish();
            }
        });
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void references() {
        mEmailField=(EditText)findViewById(R.id.email);
        mPasswordField=(EditText)findViewById(R.id.password);
        mLoginBtn=(Button)findViewById(R.id.login);
        mRegPageBtn=(Button)findViewById(R.id.signup);
        mRegisterProgressBar=(ProgressBar)findViewById(R.id.registerProgressBar);
        mAuth=FirebaseAuth.getInstance();
        mfirestore = FirebaseFirestore.getInstance();

    }
}
