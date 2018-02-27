package com.example.jesus.pksapp;

import android.support.annotation.NonNull;

/**
 * Created by jesus on 2/22/2018.
 */

public class UserId {
    public String user_Id;
    public <T extends UserId> T withId(@NonNull final String id){
        this.user_Id=id;
        return (T)this;
    }
}
