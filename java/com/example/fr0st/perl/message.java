package com.example.fr0st.perl;

public class message {


    public String textmessage;
    public String from;

    public message() {

    }

    public message( String textmessage, String from) {

        this.textmessage = textmessage;
        this.from = from;
    }



    public String getTextmessage() {
        return textmessage;
    }

    public void setTextmessage(String textmessage) {
        this.textmessage = textmessage;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
