package com.example.quanlyquancaphe.models;

public class DatBan {
    public String getId_Ban() {
        return id_Ban;
    }

    public void setId_Ban(String id_Ban) {
        this.id_Ban = id_Ban;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getGio() {
        return gio;
    }

    public void setGio(String gio) {
        this.gio = gio;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public Integer getSoNguoi() {
        return soNguoi;
    }

    public void setSoNguoi(Integer soNguoi) {
        this.soNguoi = soNguoi;
    }

    public DatBan(String id_Ban, String ngay, String gio, String tenKH, String SDT, Integer soNguoi) {
        this.id_Ban = id_Ban;
        this.ngay = ngay;
        this.gio = gio;
        this.tenKH = tenKH;
        this.SDT = SDT;
        this.soNguoi = soNguoi;
    }
    public DatBan() {
    }

    @Override
    public String toString() {
        return "DatBan{" +
                "id_Ban='" + id_Ban + '\'' +
                ", ngay='" + ngay + '\'' +
                ", gio='" + gio + '\'' +
                ", tenKH='" + tenKH + '\'' +
                ", SDT='" + SDT + '\'' +
                ", soNguoi=" + soNguoi +
                '}';
    }

    private String id_Ban, ngay, gio, tenKH, SDT;
    private Integer soNguoi;
}
