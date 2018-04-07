package com.khoaluan.mxhgiaothong.drawer.dto;


public class FooterDto {

    public int key;
    public String name;
    public String email;
    public boolean isSelected;

    public FooterDto(int key, String name, String email, int navId) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.isSelected = key == navId;
    }

    public FooterDto() {
    }

    public FooterDto(int key) {
        this.key = key;
    }
}
