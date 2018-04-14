package com.khoaluan.mxhgiaothong.activities.post.items;

/**
 * Created by Hong Hanh on 4/13/2018.
 */

public class LevelItem {
    private int level;
    private String levelName;
    private boolean isSelected;

    public LevelItem(int level, String levelName) {
        this.level = level;
        this.levelName = levelName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
