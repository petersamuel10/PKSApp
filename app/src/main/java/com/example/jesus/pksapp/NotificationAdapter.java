package com.example.jesus.pksapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jesus on 2/26/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> mNotifList;
    private Context context;
    private FirebaseFirestore mFireStore;


    public NotificationAdapter (Context context,List<Notification> mNotifList)
    {
        this.context = context;
        this.mNotifList = mNotifList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // for every call (new notification ) inflate new View of a single notification template
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_notidication,parent,false);
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        //get notification data and bind it in the Notification template
        mFireStore = FirebaseFirestore.getInstance();
        // get id of the sending user
        String from_id = mNotifList.get(position).getFrom();

        // get message body from the list and put it in the notification template
        holder.mNotifyMessage.setText(mNotifList.get(position).getMessage());
       // holder.mNotificationTime.setText(Calendar.HOUR+":"+Calendar.MINUTE+Calendar.AM_PM+" "+Calendar.DAY_OF_MONTH+"/"+Calendar.MONTH);
        // to get name and  and profile image of from firebase depending on the id of the sender saved in the list
        mFireStore.collection("users").document(from_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                //get the sender name and Profile image
                String name = documentSnapshot.getString("name");
                String image = documentSnapshot.getString("image");
                // set the name of the sender
                holder.mNotifName.setText(name);

                // set the default image first until it loaded from the server
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.mipmap.btnface);
                Glide.with(context).setDefaultRequestOptions(requestOptions).load(image).into(holder.mNotifImage);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotifList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        private CircleImageView mNotifImage;
        private TextView mNotifName;
        private TextView mNotifyMessage;



        public ViewHolder(View itemView) {
            super(itemView);
            //references all widget used in the notification template
            mNotifyMessage = (TextView)itemView.findViewById(R.id.mNotifyMessage);
            mNotifName = (TextView)itemView.findViewById(R.id.mNotifName);
            mNotifImage = (CircleImageView) itemView.findViewById(R.id.mNotifyImage);

        }
    }


}
