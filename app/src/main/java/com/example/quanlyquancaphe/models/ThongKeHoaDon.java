package com.example.quanlyquancaphe.models;

public class ThongKeHoaDon {
    private String id_MaHoaDon;
    private String ID_DSMon;
    private String thoiGian_thanhtoan;

    private String gio;
    private Double tongTien;
    private Boolean daThanhToan;
    private String tenKhachHang;

    public ThongKeHoaDon() {
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }



    public String getId_MaHoaDon() {
        return id_MaHoaDon;
    }

    public void setId_MaHoaDon(String id_MaHoaDon) {
        this.id_MaHoaDon = id_MaHoaDon;
    }

    public String getID_DSMon() {
        return ID_DSMon;
    }

    public void setID_DSMon(String ID_DSMon) {
        this.ID_DSMon = ID_DSMon;
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
        return "ThongKeHoaDon{" +
                "id_MaHoaDon='" + id_MaHoaDon + '\'' +
                ", ID_DSMon='" + ID_DSMon + '\'' +
                ", thoiGian_thanhtoan='" + thoiGian_thanhtoan + '\'' +
                ", gio='" + gio + '\'' +
                ", tongTien=" + tongTien +
                ", daThanhToan=" + daThanhToan +
                ", tenKhachHang='" + tenKhachHang + '\'' +
                '}';
    }

    public String getGio() {
        return gio;
    }

    public void setGio(String gio) {
        this.gio = gio;
    }

    public ThongKeHoaDon(String id_MaHoaDon, String ID_DSMon, String thoiGian_thanhtoan, String gio, Double tongTien, Boolean daThanhToan, String tenKhachHang) {
        this.id_MaHoaDon = id_MaHoaDon;
        this.ID_DSMon = ID_DSMon;
        this.thoiGian_thanhtoan = thoiGian_thanhtoan;
        this.gio = gio;
        this.tongTien = tongTien;
        this.daThanhToan = daThanhToan;
        this.tenKhachHang = tenKhachHang;
    }

    public ThongKeHoaDon(String id_MaHoaDon, String ID_DSMon, String thoiGian_thanhtoan, Double tongTien, Boolean daThanhToan, String tenKhachHang) {
        this.id_MaHoaDon = id_MaHoaDon;
        this.ID_DSMon = ID_DSMon;
        this.thoiGian_thanhtoan = thoiGian_thanhtoan;
        this.tongTien = tongTien;
        this.daThanhToan = daThanhToan;
        this.tenKhachHang = tenKhachHang;
    }


}
