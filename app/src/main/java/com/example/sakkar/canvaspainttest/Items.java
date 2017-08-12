package com.example.sakkar.canvaspainttest;

public class Items {
    String title;
    int id;

    public Items(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public Items() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
