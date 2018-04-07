package com.khoaluan.mxhgiaothong.drawer.dto;

public class HeaderDto {

    public int key;
    public boolean isSelected;

    public HeaderDto(int key, int navId) {
        this.key = key;
        this.isSelected = key == navId;
    }
}
