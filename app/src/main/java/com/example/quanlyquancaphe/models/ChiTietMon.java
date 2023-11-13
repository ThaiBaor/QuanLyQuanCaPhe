package com.example.quanlyquancaphe.models;

public class ChiTietMon_TaiBan {
    private String id_DSMon_TaiBan, id_Mon, hinh, ghiChu, tenMon;
    private Integer id_TrangThai, sl, gia;

    public String getId_DSMon_TaiBan() {
        return id_DSMon_TaiBan;
    }

    public void setId_DSMon_TaiBan(String id_DSMon_TaiBan) {
        this.id_DSMon_TaiBan = id_DSMon_TaiBan;
    }

    public String getId_Mon() {
        return id_Mon;
    }

    public void setId_Mon(String id_Mon) {
        this.id_Mon = id_Mon;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public Integer getId_TrangThai() {
        return id_TrangThai;
    }

    public void setId_TrangThai(Integer id_TrangThai) {
        this.id_TrangThai = id_TrangThai;
    }

    public Integer getSl() {
        return sl;
    }

    public void setSl(Integer sl) {
        this.sl = sl;
    }

    public Integer getGia() {
        return gia;
    }

    public void setGia(Integer gia) {
        this.gia = gia;
    }

    public ChiTietMon_TaiBan(String id_DSMon_TaiBan, String id_Mon, String hinh, String ghiChu, String tenMon, Integer id_TrangThai, Integer sl, Integer gia) {
        this.id_DSMon_TaiBan = id_DSMon_TaiBan;
        this.id_Mon = id_Mon;
        this.hinh = hinh;
        this.ghiChu = ghiChu;
        this.tenMon = tenMon;
        this.id_TrangThai = id_TrangThai;
        this.sl = sl;
        this.gia = gia;
    }

    public ChiTietMon_TaiBan() {
    }
}
