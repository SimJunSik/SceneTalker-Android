package com.android.yapp.scenetalker;

import java.util.ArrayList;
import java.util.List;

public class DramaInfo {
    int id;
    String title;
    String summary;
    ArrayList<String> genre;
    String rating;
    String poster_url;
    ArrayList<String> broadcasting_day;
    String broadcasting_start_time;
    String broadcasting_end_time;
    String broadcasting_station;
    boolean is_broadcasting;
    boolean is_bookmarked_by_me;
    String episode;

    public int getId() {
        return id;
    }

    public String getBroadcasting_station() {
        return broadcasting_station;
    }

    public void setBroadcasting_station(String broadcasting_station) {
        this.broadcasting_station = broadcasting_station;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getBroadcasting_day() {
        return broadcasting_day;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public String getBroadcasting_end_time() {
        return broadcasting_end_time;
    }

    public String getBroadcasting_start_time() {
        return broadcasting_start_time;
    }

    public String getEpisode() {
        return episode;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public String getRating() {
        return rating;
    }

    public String getSummary() {
        return summary;
    }

    public boolean isIs_broadcasting() {
        return is_broadcasting;
    }

    public void setIs_broadcasting(boolean is_broadcasting) {
        this.is_broadcasting = is_broadcasting;
    }

    public void setBroadcasting_day(ArrayList<String> broadcasting_day) {
        this.broadcasting_day = broadcasting_day;
    }

    public void setBroadcasting_end_time(String broadcasting_end_time) {
        this.broadcasting_end_time = broadcasting_end_time;
    }

    public void setBroadcasting_start_time(String broadcasting_start_time) {
        this.broadcasting_start_time = broadcasting_start_time;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isIs_bookmarked_by_me() {
        return is_bookmarked_by_me;
    }

    public void setIs_bookmarked_by_me(boolean is_bookmarked_by_me) {
        this.is_bookmarked_by_me = is_bookmarked_by_me;
    }
}

