package com.example.mabar_v1.main.model;

public class ListGameModel {
    String id;
    String judulGame;
    String urlImage;

    public ListGameModel(String id, String judulGame, String urlImage) {
        this.id = id;
        this.judulGame = judulGame;
        this.urlImage = urlImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudulGame() {
        return judulGame;
    }

    public void setJudulGame(String judulGame) {
        this.judulGame = judulGame;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
