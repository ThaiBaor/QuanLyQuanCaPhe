package com.example.quanlyquancaphe.models;

public class HoaDonTaiBan1 extends HoaDon1{
    private String id_Ban;

    public HoaDonTaiBan1() {
        super();
    }

    public HoaDonTaiBan1(String id_HoaDon, String thoiGian_ThanhToan, String ngayThanhToan, Double tongTien, Boolean daThanhToan, String id_Ban) {
        super(id_HoaDon, thoiGian_ThanhToan, ngayThanhToan, tongTien, daThanhToan);
        this.id_Ban = id_Ban;
    }

    public String getId_Ban() {
        return id_Ban;
    }

    public void setId_Ban(String id_Ban) {
        this.id_Ban = id_Ban;
    }
}
