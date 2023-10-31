package com.example.quanlyquancaphe.models;

public class OrderPhaChe {
    String maHoaDon ,tenBan ,thoiGian ;
    String images ,tenMon ,ghiChu,soLuong;

    public OrderPhaChe(String tenBan, String thoiGian, String images, String tenMon, String ghiChu, String soLuong) {
        this.tenBan = tenBan;
        this.thoiGian = thoiGian;
        this.images = images;
        this.tenMon = tenMon;
        this.ghiChu = ghiChu;
        this.soLuong = soLuong;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }
}
