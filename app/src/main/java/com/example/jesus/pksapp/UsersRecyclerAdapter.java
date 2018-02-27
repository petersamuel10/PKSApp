package com.example.jesus.pksapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jesus on 2/18/2018.
 */

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {
   private List<Users> usersList;
   private Context context;
   public UsersRecyclerAdapter (Context context,List<Users> usersList)
   {
       this.usersList=usersList;
       this.context=context;

   }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // inflate the template user for every record in the list
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       // get user name and profile image from the list
       final String user_name = usersList.get(position).getName();
       holder.userNameView.setText(user_name);
       CircleImageView user_image_view = holder.userImageView;
        RequestOptions placeHolder = new RequestOptions();
        placeHolder.placeholder(R.mipmap.btnface);
        Glide.with(context).setDefaultRequestOptions(placeHolder).load(usersList.get(position).getImage()).into(user_image_view);

        final String user_id=usersList.get(position).user_Id;

        // est an event of clicking in any user template
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sentActivity = new Intent(context,SendActivity.class);
                sentActivity.putExtra("user_id",user_id);
                sentActivity.putExtra("userName", user_name);
                context.startActivity(sentActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

       private View mview;
       private CircleImageView userImageView;
       private TextView userNameView;


        public ViewHolder(View itemView) {
            super(itemView);

            mview = itemView;
            userImageView =(CircleImageView) mview.findViewById(R.id.users_Image_list);
            userNameView=(TextView)mview.findViewById(R.id.user_name_list);

        }
    }
}
