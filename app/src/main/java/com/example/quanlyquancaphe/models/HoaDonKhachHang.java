package com.example.quanlyquancaphe.models;

public class HoaDonKhachHang {
    private String id_HoaDon;
    private String tenKH;

    public HoaDonKhachHang() {
    }

    public HoaDonKhachHang(String id_HoaDon, String tenKH) {
        this.id_HoaDon = id_HoaDon;
        this.tenKH = tenKH;
    }

    public String getId_HoaDon() {
        return id_HoaDon;
    }

    public void setId_HoaDon(String id_HoaDon) {
        this.id_HoaDon = id_HoaDon;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }
}
