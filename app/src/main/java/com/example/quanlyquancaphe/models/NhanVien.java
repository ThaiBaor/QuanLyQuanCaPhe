package com.example.quanlyquancaphe.models;

public class NhanVien {
    String maNhanVien, tenNhanVien, diaChi, soDienThoai, matKhau, avatar, imageCCCDT, imageCCCDS;
    String ViTri;

    public NhanVien(String tenNhanVien, String maNhanVien, String diaChi, String soDienThoai, String matKhau, String avatar, String imageCCCDT, String imageCCCDS, String viTri) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.matKhau = matKhau;
        this.avatar = avatar;
        this.imageCCCDT = imageCCCDT;
        this.imageCCCDS = imageCCCDS;
        ViTri = viTri;
    }

    public NhanVien() {
    }


    public String getViTri() {
        return ViTri;
    }

    public void setViTri(String viTri) {
        ViTri = viTri;
    }


    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImageCCCDT() {
        return imageCCCDT;
    }

    public void setImageCCCDT(String imageCCCDT) {
        this.imageCCCDT = imageCCCDT;
    }

    public String getImageCCCDS() {
        return imageCCCDS;
    }

    public void setImageCCCDS(String imageCCCDS) {
        this.imageCCCDS = imageCCCDS;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                ", maNhanVien='" + maNhanVien + '\'' +
                ", tenNhan='" + tenNhanVien + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", avatar='" + avatar + '\'' +
                ", imageCCCDT='" + imageCCCDT + '\'' +
                ", getImageCCCDS='" + imageCCCDS + '\'' +
                ", ViTri='" + ViTri + '\'' +
                '}';
    }
}
