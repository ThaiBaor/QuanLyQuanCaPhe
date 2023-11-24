package com.example.quanlyquancaphe.models;

public class HoaDonMangVe extends HoaDon {
    private String tenKH;

    public HoaDonMangVe() {
        super();
    }

    public HoaDonMangVe(String id_HoaDon, String thoiGian_ThanhToan, String ngayThanhToan, Double tongTien, Boolean daThanhToan, String tenKH) {
        super(id_HoaDon, thoiGian_ThanhToan, ngayThanhToan, tongTien, daThanhToan);
        this.tenKH = tenKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    @Override
    public String toString() {
        return "HoaDonMangVe{" +
                "tenKH='" + tenKH + '\'' +
                '}';
    }
}
