package com.handheld.upsizeuhf.model;

public class Actor {
    public String uid;
    public String name;
    public boolean selected = false;

    public Actor(String uid, String name) {
        this.name = name;
        this.uid = uid;
    }

    public Actor() {
        this.name = "";
        this.uid = "";
        this.selected = false;
    }
}
