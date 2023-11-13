package com.example.quanlyquancaphe.models;

public class HoaDonMangVe {
    private String id_HoaDon;
    private String id_DSMon_MangVe;
    private String thoiGian_thanhtoan;
    private Double tongTien;

    private Boolean daThanhToan;

    public HoaDonMangVe() {
    }

    public HoaDonMangVe(String maHoaDon, String id_DSMon_MangVe, String thoiGian_thanhtoan, Double tongTien, Boolean daThanhToan) {
        this.id_HoaDon = maHoaDon;
        this.id_DSMon_MangVe = id_DSMon_MangVe;
        this.thoiGian_thanhtoan = thoiGian_thanhtoan;
        this.tongTien = tongTien;
        this.daThanhToan = daThanhToan;
    }

    public String getMaHoaDon() {
        return id_HoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.id_HoaDon = maHoaDon;
    }

    public String getId_DSMon_MangVe() {
        return id_DSMon_MangVe;
    }

    public void setId_DSMon_MangVe(String id_DSMon_MangVe) {
        this.id_DSMon_MangVe = id_DSMon_MangVe;
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
        return "HoaDonMangVe{" +
                "maHoaDon='" + id_HoaDon + '\'' +
                ", maDSMon_MangVe='" + id_DSMon_MangVe + '\'' +
                ", thoiGian_thanhtoan='" + thoiGian_thanhtoan + '\'' +
                ", tongTien=" + tongTien +
                ", daThanhToan=" + daThanhToan +
                '}';
    }
}
