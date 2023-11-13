package com.example.quanlyquancaphe.models;

public class TrangThai_Mon {
    Integer id_TrangThai ;
    String tenTrangThai;

    public TrangThai_Mon() {
    }

    public TrangThai_Mon(Integer id_TrangThai, String tenTrangThai) {
        this.id_TrangThai = id_TrangThai;
        this.tenTrangThai = tenTrangThai;
    }

    public Integer getId_TrangThai() {
        return id_TrangThai;
    }

    public void setId_TrangThai(Integer id_TrangThai) {
        this.id_TrangThai = id_TrangThai;
    }

    public String getTenTrangThai() {
        return tenTrangThai;
    }

    public void setTenTrangThai(String tenTrangThai) {
        this.tenTrangThai = tenTrangThai;
    }
}
