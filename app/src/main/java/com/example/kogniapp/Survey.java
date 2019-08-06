package com.example.kogniapp;

public class Survey {
    public String id;
    public String ans1;
    public String ans2;
    public Survey() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Survey(String id, String ans1,String ans2) {
        this.id = id;
        this.ans1 = ans1;
        this.ans2 = ans2;
    }


}
