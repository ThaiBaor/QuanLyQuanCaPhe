package com.example.quanlyquancaphe.models;

public class Mon {
    private String id_Mon, moTa, tenMon, hinh;
    private Double donGia;
    private Integer giamGia, id_Loai, slDaBan = 0;
    private Boolean hetMon = false;
    public Mon() {
    }


    public Mon(String id_Mon, String moTa, String tenMon, String hinh, Double donGia, Integer giamGia, Integer id_Loai, Integer slDaBan, Boolean hetMon) {
        this.id_Mon = id_Mon;
        this.moTa = moTa;
        this.tenMon = tenMon;
        this.hinh = hinh;
        this.donGia = donGia;
        this.giamGia = giamGia;
        this.id_Loai = id_Loai;
        this.slDaBan = slDaBan;
        this.hetMon = hetMon;
    }

    public String getId_Mon() {
        return id_Mon;
    }

    public void setId_Mon(String id_Mon) {
        this.id_Mon = id_Mon;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public Double getDonGia() {
        return donGia;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }

    public Integer getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(Integer giamGia) {
        this.giamGia = giamGia;
    }

    public Integer getId_Loai() {
        return id_Loai;
    }

    public void setId_Loai(Integer id_Loai) {
        this.id_Loai = id_Loai;
    }

    public Integer getSlDaBan() {
        return slDaBan;
    }

    public void setSlDaBan(Integer slDaBan) {
        this.slDaBan = slDaBan;
    }

    public Boolean getHetMon() {
        return hetMon;
    }

    public void setHetMon(Boolean hetMon) {
        this.hetMon = hetMon;
    }

    @Override
    public String toString() {
        return "Mon{" +
                "id_Mon='" + id_Mon + '\'' +
                ", moTa='" + moTa + '\'' +
                ", tenMon='" + tenMon + '\'' +
                ", hinh='" + hinh + '\'' +
                ", donGia=" + donGia +
                ", giamGia=" + giamGia +
                ", id_Loai=" + id_Loai +
                '}';
    }
}
