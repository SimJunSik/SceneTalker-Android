package com.scenetalker.yapp.scenetalker;

import android.widget.ImageButton;

public class SearchInfo {
    String word;
    ImageButton cancel;

    public SearchInfo(String word, ImageButton cancel) {
        this.word = word;
        this.cancel = cancel;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ImageButton getCancel() {
        return cancel;
    }

    public void setCancel(ImageButton cancel) {
        this.cancel = cancel;
    }
}