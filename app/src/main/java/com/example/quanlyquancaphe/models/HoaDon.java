package com.example.quanlyquancaphe.models;

public class HoaDonTaiBan {
    private String id_HoaDon, id_DSMon_TaiBan;
    private String thoiGian_ThanhToan;
    private Double tongTien;
    private Boolean daThanhToan;

    public String getId_HoaDon() {
        return id_HoaDon;
    }

    public void setId_HoaDon(String id_HoaDon) {
        this.id_HoaDon = id_HoaDon;
    }

    public String getId_DSMon_TaiBan() {
        return id_DSMon_TaiBan;
    }

    public void setId_DSMon_TaiBan(String id_DSMon_TaiBan) {
        this.id_DSMon_TaiBan = id_DSMon_TaiBan;
    }

    public String getThoiGian_ThanhToan() {
        return thoiGian_ThanhToan;
    }

    public void setThoiGian_ThanhToan(String thoiGian_ThanhToan) {
        this.thoiGian_ThanhToan = thoiGian_ThanhToan;
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

    public HoaDonTaiBan() {
    }

    public HoaDonTaiBan(String maHD, String maDSMon_TaiBan, String thoiGian_ThanhToan, Double tongTien, Boolean daThanhToan) {
        this.id_HoaDon = maHD;
        this.id_DSMon_TaiBan = maDSMon_TaiBan;
        this.thoiGian_ThanhToan = thoiGian_ThanhToan;
        this.tongTien = tongTien;
        this.daThanhToan = daThanhToan;
    }
}
