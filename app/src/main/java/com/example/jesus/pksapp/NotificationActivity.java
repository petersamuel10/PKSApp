package com.example.jesus.pksapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

// this class is to show handle Notification

public class NotificationActivity extends AppCompatActivity {

    private TextView mNotifyData;
    private TextView mNotifySender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mNotifyData=(TextView)findViewById(R.id.notify_text);
        mNotifySender = (TextView)findViewById(R.id.notify_From);

        // get sender name and message body from intent comes from onMessageReceived(FirebaseMessagingService class)
        String dataMessage = getIntent().getStringExtra("message");
        String dataFrom = getIntent().getStringExtra("dataFrom");

        // set sender name and message body to the Activity to show to user
        mNotifySender.setText(dataFrom);
        mNotifyData.setText(" TXT : " + dataMessage);

    }
}
