package com.example.quanlyquancaphe.models;

import java.util.Date;

public class NguyenLieu {
    String maNguyenLieu;
    String tenNguyenLieu, donVi;
    String ngayNhap;
    Double soLuongNhap, tonKho;

    public String getMaNguyenLieu() {
        return maNguyenLieu;
    }

    public void setMaNguyenLieu(String maNguyenLieu) {
        this.maNguyenLieu = maNguyenLieu;
    }

    public String getTenNguyenLieu() {
        return tenNguyenLieu;
    }

    public void setTenNguyenLieu(String tenNguyenLieu) {
        this.tenNguyenLieu = tenNguyenLieu;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public String getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(String ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public Double getSoLuongNhap() {
        return soLuongNhap;
    }

    public void setSoLuongNhap(Double soLuongNhap) {
        this.soLuongNhap = soLuongNhap;
    }

    public Double getTonKho() {
        return tonKho;
    }

    public void setTonKho(Double tonKho) {
        this.tonKho = tonKho;
    }

    public NguyenLieu() {
    }

    public NguyenLieu(String maNguyenLieu, String tenNguyenLieu, String donVi, String ngayNhap, Double soLuongNhap, Double tonKho) {
        this.maNguyenLieu = maNguyenLieu;
        this.tenNguyenLieu = tenNguyenLieu;
        this.donVi = donVi;
        this.ngayNhap = ngayNhap;
        this.soLuongNhap = soLuongNhap;
        this.tonKho = tonKho;
    }
}
