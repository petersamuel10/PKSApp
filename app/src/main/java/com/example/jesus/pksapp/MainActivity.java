package com.example.jesus.pksapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    private TextView mProfileLabel;
    private TextView mUsersLabel;
    private TextView mNotificationLabel;

    private ViewPager mMainPager;
    private PagerViewAdapter mPagerViewAdapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //references of all used widgets
        references();

        // apply mMainPager to fragments
        mPagerViewAdapter=new PagerViewAdapter(getSupportFragmentManager());
        mMainPager.setAdapter(mPagerViewAdapter);

        mAuth=FirebaseAuth.getInstance();


        mMainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                // change the tabs focus when you move over pageFragments
                changeTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // check if the user is logging
        FirebaseUser currentUser=mAuth.getCurrentUser();
        //if not login got to login Activity
        if(currentUser==null)
            sendToLogin();

        // events of the tabs to go to the right fragment
        //
        //go to profile fragment
        mProfileLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainPager.setCurrentItem(0);
            }
        });

        //go to users fragment
        mUsersLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    mMainPager.setCurrentItem(1);
                }
        });

        // go to Notification fragment
        mNotificationLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainPager.setCurrentItem(2);
            }
        });

    }

    private void sendToLogin() {
        Intent LogPage=new Intent(this,LoginActivity.class);
        startActivity(LogPage);
        finish();
    }


    private void changeTabs(int position) {
        // if first fragment (profile) is selected
        if(position==0)
        {
           // mProfileLabel.setTextColor(Integer.parseInt("#ffffff"));
            mProfileLabel.setTextSize(22);

           // mUsersLabel.setTextColor(Integer.parseInt("#7EC6FF"));
            mUsersLabel.setTextSize(16);

           // mNotificationLabel.setTextColor(Integer.parseInt("#7EC6FF"));
            mNotificationLabel.setTextSize(16);
        }

        // if second fragment (allUsers) is selected
        if (position==1)
        {
             //   mProfileLabel.setTextColor(Integer.parseInt("#7EC6FF"));
                mProfileLabel.setTextSize(16);

               // mUsersLabel.setTextColor(Integer.parseInt("#ffffff"));
                mUsersLabel.setTextSize(22);

               // mNotificationLabel.setTextColor(Integer.parseInt("#7EC6FF"));
                mNotificationLabel.setTextSize(16);
        }

        // if third fragment (Notification) is selected
        if(position==2)
        {
           // mProfileLabel.setTextColor(Integer.parseInt("#7EC6FF"));
            mProfileLabel.setTextSize(16);

           // mUsersLabel.setTextColor(Integer.parseInt("#7EC6FF"));
            mUsersLabel.setTextSize(16);

            //mNotificationLabel.setTextColor(Integer.parseInt("#ffffff"));
            mNotificationLabel.setTextSize(22);
        }

    }

    private void references() {

        mProfileLabel =(TextView)findViewById(R.id.profileLabel);
        mUsersLabel =(TextView)findViewById(R.id.usersLabel);
        mNotificationLabel =(TextView)findViewById(R.id.notificationLabel);
        mMainPager =(ViewPager)findViewById(R.id.mainPager);
        mMainPager.setOffscreenPageLimit(2);

    }
}
