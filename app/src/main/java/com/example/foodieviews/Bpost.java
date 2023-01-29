package com.example.foodieviews;

public class Bpost {

    private String postname , posttext , post_image , username , Time , profile_picture ;

    public Bpost() {

    }


    public Bpost(String postname, String posttext, String post_image, String username, String time, String profile_picture) {
        this.postname = postname;
        this.posttext = posttext;
        this.post_image = post_image;
        this.username = username;
        Time = time;
        this.profile_picture = profile_picture;
    }

    public String getPostname() {
        return postname;
    }

    public void setPostname(String postname) {
        this.postname = postname;
    }

    public String getPosttext() {
        return posttext;
    }

    public void setPosttext(String posttext) {
        this.posttext = posttext;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
