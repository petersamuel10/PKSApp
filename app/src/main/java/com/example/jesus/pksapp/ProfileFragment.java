package com.example.jesus.pksapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Button mLogoutBtn;
    private CircleImageView profileImage;
    private TextView profileUserName;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private String mUserId;
    View view;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        references();
        mUserId=mAuth.getCurrentUser().getUid();

        // get name and profile image belongs to the current login user
        mFireStore.collection("users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String user_name=documentSnapshot.getString("name");
                String user_image=documentSnapshot.getString("image");

                profileUserName.setText(user_name);

                // donload profile image and set it on the profile fragment
                RequestOptions placeHolder=new RequestOptions();
                placeHolder.placeholder(R.mipmap.btnface);
                Glide.with(container.getContext()).applyDefaultRequestOptions(placeHolder).load(user_image).into(profileImage);



            }
        });

        // log out button event
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // delete token field to avoid send notification to wrong device
                // the token id is a number belongs to the device which u are login in it
                // so if u logout of advice and another login to this device with another account
                // this token now delongs to the new user not u
                Map<String,Object> tokenMapRemove = new HashMap<>();
                tokenMapRemove.put("token_id", FieldValue.delete());
                mFireStore.collection("users").document(mUserId).update(tokenMapRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        mAuth.signOut();
                        Intent LoginIntent = new Intent(getContext(),LoginActivity.class);
                        startActivity(LoginIntent);
                    }
                });
            }
        });
        return view;
    }

    private void references() {

        mLogoutBtn=(Button)view.findViewById(R.id.logOutBtn);
        profileImage=(CircleImageView) view.findViewById(R.id.profileImage);
        profileUserName=(TextView)view.findViewById(R.id.userName);
        mAuth=FirebaseAuth.getInstance();
        mFireStore=FirebaseFirestore.getInstance();

    }

}
