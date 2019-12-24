package com.example.hospitalityapp.model;

public class Messages {

    public  String date,time,type,message,from,sendername;

    public  Messages ()
    {

    }

    public Messages(String date, String time, String type, String message, String from, String sendername) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.message = message;
        this.from = from;
        this.sendername = sendername;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }
}
