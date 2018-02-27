package com.example.jesus.pksapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {
    private RecyclerView mNotificationList;
    private NotificationAdapter notificationsAdapter;
    private List<Notification> mNotifList;
    private FirebaseFirestore mFireStore;


    public NotificationsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // references all used widgets

        mNotifList = new ArrayList<>();
        mNotificationList = (RecyclerView)view.findViewById(R.id.notificationList);
        notificationsAdapter = new NotificationAdapter(container.getContext(),mNotifList);
        mNotificationList.setHasFixedSize(true);
        mNotificationList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mNotificationList.setAdapter(notificationsAdapter);

        mFireStore = FirebaseFirestore.getInstance();


        ////////////
        String currentUserId = FirebaseAuth.getInstance().getUid();
       // Toast.makeText(getContext(),"UserID"+currentUserId,Toast.LENGTH_LONG).show();


        // get new added notification from firebase and added it to a list
        mFireStore.collection("users").document(currentUserId).collection("notification").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for(DocumentChange doc :documentSnapshots.getDocumentChanges())
                {
                    // get the notification ass message Body and who send this notification
                    Notification notification = doc.getDocument().toObject(Notification.class);
                    mNotifList.add(notification);
                    // send the new with list to notication Adapter class
                    notificationsAdapter.notifyDataSetChanged();
                }
            }
        });



     return view;
    }

}
