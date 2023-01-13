package com.example.arwashamaly_networkproject.Utility;

public class User {
    String name;
    String city;
    String gender;
    String photo;

    public User() {
    }

    public User(String name, String city, String gender, String photo) {
        this.name = name;
        this.city = city;
        this.gender = gender;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
