package com.example.jesus.pksapp;

/**
 * Created by jesus on 2/26/2018.
 */

public class Notification  {
    String from,message;

    public Notification()
    {

    }
    public Notification(String from, String message) {
        this.from = from;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
