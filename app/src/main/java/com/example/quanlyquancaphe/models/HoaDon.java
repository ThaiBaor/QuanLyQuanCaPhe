package com.example.quanlyquancaphe.models;

public class HoaDon {
    private String id_HoaDon;
    private String thoiGian_ThanhToan;
    private String ngayThanhToan;
    private Double tongTien;
    private Boolean daThanhToan = false;

    public HoaDon() {

    }

    public HoaDon(String id_HoaDon, String thoiGian_ThanhToan, String ngayThanhToan, Double tongTien, Boolean daThanhToan) {
        this.id_HoaDon = id_HoaDon;
        this.thoiGian_ThanhToan = thoiGian_ThanhToan;
        this.ngayThanhToan = ngayThanhToan;
        this.tongTien = tongTien;
        this.daThanhToan = daThanhToan;
    }

    public String getId_HoaDon() {
        return id_HoaDon;
    }

    public void setId_HoaDon(String id_HoaDon) {
        this.id_HoaDon = id_HoaDon;
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

    @Override
    public String toString() {
        return "HoaDon{" +
                "id_HoaDon='" + id_HoaDon + '\'' +
                ", thoiGian_ThanhToan='" + thoiGian_ThanhToan + '\'' +
                ", ngayThanhToan='" + ngayThanhToan + '\'' +
                '}';
    }
}
