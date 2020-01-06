package com.scenetalker.yapp.scenetalker;

public class ChattingInfo {
    String name;
    String talk;

    public ChattingInfo(String name, String talk) {
        this.name = name;
        this.talk = talk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTalk() {
        return talk;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }
}
