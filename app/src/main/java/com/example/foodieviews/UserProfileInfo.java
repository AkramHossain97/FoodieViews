package com.example.foodieviews;

public class UserProfileInfo {

    String email,username,profile_picture;


    public UserProfileInfo(){

    }

    public UserProfileInfo(String email, String username, String profile_picture) {
        this.email = email;
        this.username = username;
        this.profile_picture = profile_picture;
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

}
