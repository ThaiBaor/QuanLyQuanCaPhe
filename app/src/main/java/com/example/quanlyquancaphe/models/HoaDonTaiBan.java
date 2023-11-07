package com.example.quanlyquancaphe.models;

public class HoaDonTaiBan {
    private String id_HoaDon;
    private String id_DSMon_TaiBan;
    private String thoiGian_thanhtoan;
    private Double tongTien;
    private Boolean daThanhToan;

    public HoaDonTaiBan(String id_HoaDon, String id_DSMon_TaiBan, String thoiGian_thanhtoan, Double tongTien, Boolean daThanhToan) {
        this.id_HoaDon = id_HoaDon;
        this.id_DSMon_TaiBan = id_DSMon_TaiBan;
        this.thoiGian_thanhtoan = thoiGian_thanhtoan;
        this.tongTien = tongTien;
        this.daThanhToan = daThanhToan;
    }

    public HoaDonTaiBan() {
    }

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

    public String getThoiGian_thanhtoan() {
        return thoiGian_thanhtoan;
    }

    public void setThoiGian_thanhtoan(String thoiGian_thanhtoan) {
        this.thoiGian_thanhtoan = thoiGian_thanhtoan;
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
        return "HoaDonTaiBan{" +
                "maHoaDon='" + id_HoaDon + '\'' +
                ", maDSMon_TaiBan='" + id_DSMon_TaiBan + '\'' +
                ", thoiGian_thanhtoan='" + thoiGian_thanhtoan + '\'' +
                ", tongTien=" + tongTien +
                ", daThanhToan=" + daThanhToan +
                '}';
    }
}
