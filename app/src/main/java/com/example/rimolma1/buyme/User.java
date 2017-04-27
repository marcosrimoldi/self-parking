package com.example.rimolma1.buyme;

/**
 * Created by RIMOLMA1 on 10/31/2014.
 */
public class User {

    private String id;
    private String name;

    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getId().equals(Constants.userID) ? "Para mi!" : getName();
    }
}
