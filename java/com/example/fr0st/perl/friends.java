package com.example.fr0st.perl;

import android.support.v7.widget.RecyclerView;

public class friends {

    public  String status;

    public friends (){


    }


    public friends(String status) {
        this.status = status;
    }


    public  String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
