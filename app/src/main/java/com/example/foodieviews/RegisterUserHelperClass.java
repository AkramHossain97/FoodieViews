package com.example.foodieviews;

public class RegisterUserHelperClass {

    String email , username , profile_picture , uid ;


    public RegisterUserHelperClass() {

    }

    public RegisterUserHelperClass(String email, String username, String profile_picture, String uid) {
        this.email = email;
        this.username = username;
        this.profile_picture = profile_picture;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
