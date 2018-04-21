package com.apanteleeva.weatherapp;

public class City {
    private long id;
    private String name;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String str) {
        this.name = str;
    }
    // для ArrayAdapter, чтобы правильно отображался текст
    public String toString() {
        return name;
    }

}
