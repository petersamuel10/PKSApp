package com.example.jesus.pksapp;

/**
 * Created by jesus on 2/18/2018.
 */

public class Users extends UserId {

   private String image;
   private String name;
    public Users()
    {

    }
    public Users(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
