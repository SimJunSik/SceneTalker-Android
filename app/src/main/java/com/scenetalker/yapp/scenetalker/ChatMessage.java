package com.scenetalker.yapp.scenetalker;

public class ChatMessage {
    String token;
    String message;
    String drama_id;
    String episode;

    public ChatMessage(String token, String message, String drama_id, String episode) {
        this.token = token;
        this.message = message;
        this.drama_id = drama_id;
        this.episode = episode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDrama_id() {
        return drama_id;
    }

    public void setDrama_id(String drama_id) {
        this.drama_id = drama_id;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }
}
