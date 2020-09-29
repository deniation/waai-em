package com.asisst.waaiem.user.main.pages.contacts.pojo;

public class Contacts {
    private String username;
    private String firstname;
    private String lastname;
    private String location;
    private String profile_picture;
    private String user_id;

    public Contacts() {
    }

    public Contacts(String username, String firstname, String lastname, String location, String profile_picture, String user_id) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.location = location;
        this.profile_picture = profile_picture;
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
