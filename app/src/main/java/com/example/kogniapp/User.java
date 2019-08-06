package com.example.kogniapp;

public class User {

    private long id;
    private String email;
    public User(){

    }
    public User(long id, String email){
        this.id=id;
        this.email=email;
    }
    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
