package com.example.quanlyquancaphe.models;

public class HoaDon {
    private String id_HoaDon, id_Ban;
    private String thoiGian_ThanhToan;
    private String ngayThanhToan;
    private Double tongTien;
    private Boolean daThanhToan;

    public String getId_HoaDon() {
        return id_HoaDon;
    }

    public void setId_HoaDon(String id_HoaDon) {
        this.id_HoaDon = id_HoaDon;
    }

    public String getId_Ban() {
        return id_Ban;
    }

    public void setId_Ban(String id_Ban) {
        this.id_Ban = id_Ban;
    }

    public String getThoiGian_ThanhToan() {
        return thoiGian_ThanhToan;
    }

    public void setThoiGian_ThanhToan(String thoiGian_ThanhToan) {
        this.thoiGian_ThanhToan = thoiGian_ThanhToan;
    }

    public String getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(String ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }

    public Boolean getDaThanhToan() {
        return daThanhToan;
    }

    public void setDaThanhToan(Boolean daThanhToan) {
        this.daThanhToan = daThanhToan;
    }

    public HoaDon(String id_HoaDon, String id_Ban, String thoiGian_ThanhToan, String ngayThanhToan, Double tongTien, Boolean daThanhToan) {
        this.id_HoaDon = id_HoaDon;
        this.id_Ban = id_Ban;
        this.thoiGian_ThanhToan = thoiGian_ThanhToan;
        this.ngayThanhToan = ngayThanhToan;
        this.tongTien = tongTien;
        this.daThanhToan = daThanhToan;
    }

    public HoaDon() {
    }
}
