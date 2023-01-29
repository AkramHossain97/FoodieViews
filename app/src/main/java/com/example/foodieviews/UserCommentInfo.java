package com.example.foodieviews;

public class UserCommentInfo {

    private String profile_picture , username , CommentMsg ;


    public UserCommentInfo() {

    }

    public UserCommentInfo(String profile_picture, String username, String commentMsg) {
        this.profile_picture = profile_picture;
        this.username = username;
        CommentMsg = commentMsg;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentMsg() {
        return CommentMsg;
    }

    public void setCommentMsg(String commentMsg) {
        CommentMsg = commentMsg;
    }
}
