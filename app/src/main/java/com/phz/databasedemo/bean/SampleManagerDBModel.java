package com.phz.databasedemo.bean;

import android.content.ContentValues;

public class SampleManagerDBModel{
    public static final String ID = "_id";
    public static final String MESSAGE = "message";
    public static final String TIME = "time";

    long id;
    String message;
    long time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ContentValues toContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(MESSAGE, message);
        cv.put(TIME, time);
        return cv;
    }
}
