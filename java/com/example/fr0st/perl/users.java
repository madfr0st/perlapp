package com.example.fr0st.perl;

import android.media.Image;

public class users {

    public  String name;
    public  String status;
    public  String image;

    public users(){


    }


    public users(String name, String status, String image) {
        this.name = name;
        this.status = status;
        this.image = image;
    }



    public  String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() { return image; }

    public void setImage() { this.image = image; }
}
