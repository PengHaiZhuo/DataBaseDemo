package com.phz.databasedemo.bean;

public class Sample {
    Long id;
    String message;
    String name;
    long time;

    public Sample() {
    }

    public Sample(String message, long time) {
        this.message = message;
        this.time = time;
    }

    public Sample(Long id, String message, String name) {
        this.id = id;
        this.message = message;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
