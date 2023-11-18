package com.example.quanlyquancaphe.models;

public class DanhSachOder {
    String id_Ban, tenKH ;
    String gioGoiMon;

    public DanhSachOder(String id_Ban, String tenKH, String gioGoiMon) {
        this.id_Ban = id_Ban;
        this.tenKH = tenKH;
        this.gioGoiMon = gioGoiMon;
    }

    public DanhSachOder() {
    }

    public String getId_Ban() {
        return id_Ban;
    }

    public void setId_Ban(String id_Ban) {
        this.id_Ban = id_Ban;
    }

    public String getGioGoiMon() {
        return gioGoiMon;
    }

    public void setGioGoiMon(String gioGoiMon) {
        this.gioGoiMon = gioGoiMon;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    @Override
    public String toString() {
        return "DanhSachOder{" +
                "id_Ban='" + id_Ban + '\'' +
                ", tenKH='" + tenKH + '\'' +
                ", gioGoiMon='" + gioGoiMon + '\'' +
                '}';
    }
}
