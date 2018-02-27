package com.example.jesus.pksapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {

    private TextView user_id_view;
    private String mUserId;
    private String mUserName;
    private EditText mMessageView;
    private Button send;
    private String mCurrentId;
    private ProgressBar progressBar;


    // this class is to writ the message to send to another user
    private FirebaseFirestore mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        // references all used widget
        references();

        // get the name and user for userwho i want to notify him
        mUserId = getIntent().getStringExtra("user_id");
        mUserName = getIntent().getStringExtra("userName");

        user_id_view.setText("Send to "+mUserName);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mMessageView.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(message))
                {
                    // put the notification and current user id
                    // (to bring user name and image of this user from it's document as a sender when received in the other device)
                    // to the document of the purpose user
                    Map <String , Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("message",message);
                    notificationMessage.put("from",mCurrentId);

                    // put the notification to the document of the purpose user
                    mFireStore.collection("users/"+mUserId+"/notification").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressBar.setVisibility(View.INVISIBLE);
                            mMessageView.setText("");
                            Toast.makeText(SendActivity.this,"message send",Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SendActivity.this,"ERROR "+e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
    }

    private void references() {

        user_id_view = (TextView)findViewById(R.id.user_name_view);
        mFireStore = FirebaseFirestore.getInstance();
        mCurrentId = FirebaseAuth.getInstance().getUid();
        mMessageView = (EditText)findViewById(R.id.body);
        progressBar = (ProgressBar)findViewById(R.id.messageProgressBar);
        send = (Button)findViewById(R.id.sendBtn);

    }
}
