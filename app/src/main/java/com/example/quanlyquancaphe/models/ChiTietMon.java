package com.example.quanlyquancaphe.models;

public class ChiTietMon {
    private String id_Mon, id_Ban = " ", tenKH = " ", ngayGoiMon, gioGoiMon, ghiChu = " ", hinh, tenMon;
    private Integer id_TrangThai = -1, sl = 0;
    private Double gia;

    public ChiTietMon() {
    }
    public ChiTietMon(String id_Mon, String id_Ban, String tenKH, String ngayGoiMon, String gioGoiMon, String ghiChu, String hinh, String tenMon, Integer id_TrangThai, Integer sl, Double gia) {
        this.id_Mon = id_Mon;
        this.id_Ban = id_Ban;
        this.ngayGoiMon = ngayGoiMon;
        this.gioGoiMon = gioGoiMon;
        this.ghiChu = ghiChu;
        this.hinh = hinh;
        this.tenMon = tenMon;
        this.id_TrangThai = id_TrangThai;
        this.sl = sl;
        this.gia = gia;
        this.tenKH = tenKH;
    }

    public String getId_Mon() {
        return id_Mon;
    }

    public void setId_Mon(String id_Mon) {
        this.id_Mon = id_Mon;
    }


    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
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

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }

    public String getId_Ban() {
        return id_Ban;
    }

    public void setId_Ban(String id_Ban) {
        this.id_Ban = id_Ban;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getNgayGoiMon() {
        return ngayGoiMon;
    }

    public void setNgayGoiMon(String ngayGoiMon) {
        this.ngayGoiMon = ngayGoiMon;
    }

    public String getGioGoiMon() {
        return gioGoiMon;
    }

    public void setGioGoiMon(String gioGoiMon) {
        this.gioGoiMon = gioGoiMon;
    }

    public void tang() {
        if (this.sl < 99) {
            ++this.sl;
        }
    }

    public void giam() {
        if (this.sl > 1) {
            --this.sl;
        }
    }
    public double tinhTongTien(){
        return sl*gia;
    }

    @Override
    public String toString() {
        return "ChiTietMon_TaiBan{" +
                "id_Mon='" + id_Mon + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                ", hinh='" + hinh + '\'' +
                ", tenMon='" + tenMon + '\'' +
                ", id_TrangThai=" + id_TrangThai +
                ", sl=" + sl +
                ", gia=" + gia +
                '}';
    }
}
