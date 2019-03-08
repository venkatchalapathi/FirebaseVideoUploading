package com.example.firebasevideouploading;

public class Model {
    private String about;
    private String video_url;

    public Model() {

    }

    public Model(String about, String video_url) {
        this.about = about;
        this.video_url = video_url;
    }

    public String getAbout() {
        return about;
    }

    public String getVideo_url() {
        return video_url;
    }
}
