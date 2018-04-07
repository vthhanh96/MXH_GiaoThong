package com.khoaluan.mxhgiaothong.drawer.dto;

public class BodyDto {

    public int key;
    public int icon;
    public String title;
    public boolean isSelected;

    public BodyDto(int key, int icon, String title, int navId) {
        this.key = key;
        this.icon = icon;
        this.title = title;
        this.isSelected = key == navId;
    }
}