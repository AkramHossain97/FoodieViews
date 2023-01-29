package com.example.foodieviews;

public class GetDataPar {

    private String title;
    private String artists;
    private String coverImage;

    public GetDataPar() {
    }

    public GetDataPar(String title, String artists, String coverImage) {
        this.title = title;
        this.artists = artists;
        this.coverImage = coverImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}
