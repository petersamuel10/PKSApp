package com.example.jesus.pksapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
public class UsersFragment extends Fragment {
private RecyclerView mUsersListViews;
private List<Users> userLists;
private UsersRecyclerAdapter usersRecyclerAdapter;
private FirebaseFirestore mFireStorage;


public UsersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_users,container,false);
       mUsersListViews = (RecyclerView)view.findViewById(R.id.usersListView);
       userLists = new ArrayList<>();
       mFireStorage =FirebaseFirestore.getInstance();
       usersRecyclerAdapter = new UsersRecyclerAdapter(container.getContext(),userLists);
       mUsersListViews.setHasFixedSize(true);
       mUsersListViews.setLayoutManager(new LinearLayoutManager(container.getContext()));
       mUsersListViews.setAdapter(usersRecyclerAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // clear the list to prevent repeated users
        // and get all users for first time
        userLists.clear();

            mFireStorage.collection("users").addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    // check if new account is saved in the firebase collection
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED)
                        {
                            String user_id = doc.getDocument().getId();
                            Users users = doc.getDocument().toObject(Users.class).withId(user_id);
                            // add this new account to the list
                            userLists.add(users);

                            // notify the adapter about this changes
                            usersRecyclerAdapter.notifyDataSetChanged();
                         //   Toast.makeText(getActivity(),"donnnnnddddddd",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            //Log.e("erererer","donnnnnnn");

    }
}
