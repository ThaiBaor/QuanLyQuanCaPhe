package com.example.quanlyquancaphe.models;

public class PhieuGiamGia {
    private String id_Phieu, ngayHetHan, id_HoaDon=" ";
    private Integer giaTri;

    public PhieuGiamGia() {
    }

    public PhieuGiamGia(String id_Phieu, String ngayHetHan, String id_HoaDon, Integer giaTri) {
        this.id_Phieu = id_Phieu;
        this.ngayHetHan = ngayHetHan;
        this.id_HoaDon = id_HoaDon;
        this.giaTri = giaTri;
    }

    public String getId_Phieu() {
        return id_Phieu;
    }

    public void setId_Phieu(String id_Phieu) {
        this.id_Phieu = id_Phieu;
    }

    public String getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(String ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public String getId_HoaDon() {
        return id_HoaDon;
    }

    public void setId_HoaDon(String id_HoaDon) {
        this.id_HoaDon = id_HoaDon;
    }

    public Integer getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(Integer giaTri) {
        this.giaTri = giaTri;
    }
}
