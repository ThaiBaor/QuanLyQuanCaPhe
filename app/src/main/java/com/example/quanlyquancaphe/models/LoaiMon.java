package com.example.quanlyquancaphe.models;

public class LoaiMon {
    private Integer id_loai;
    private String ten_loai;

    public LoaiMon() {
    }

    public LoaiMon(Integer id_loai, String ten_loai) {
        this.id_loai = id_loai;
        this.ten_loai = ten_loai;
    }

    public Integer getId_loai() {
        return id_loai;
    }

    public void setId_loai(Integer id_loai) {
        this.id_loai = id_loai;
    }

    public String getTen_loai() {
        return ten_loai;
    }

    public void setTen_loai(String ten_loai) {
        this.ten_loai = ten_loai;
    }


}
