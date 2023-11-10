package com.example.quanlyquancaphe.models;

public class ThongKeHoaDon {
    private String id_MaHoaDon;
    private String id_Ban;
    private String thoiGian_thanhtoan;

    private String ngayThanhToan;
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

    public String getId_Ban() {
        return id_Ban;
    }

    public void setId_Ban(String id_Ban) {
        this.id_Ban = id_Ban;
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
                ", ID_DSMon='" + id_Ban + '\'' +
                ", thoiGian_thanhtoan='" + thoiGian_thanhtoan + '\'' +
                ", gio='" + ngayThanhToan + '\'' +
                ", tongTien=" + tongTien +
                ", daThanhToan=" + daThanhToan +
                ", tenKhachHang='" + tenKhachHang + '\'' +
                '}';
    }

    public String getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(String ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public ThongKeHoaDon(String id_MaHoaDon, String id_Ban, String thoiGian_thanhtoan, String ngayThanhToan, Double tongTien, Boolean daThanhToan, String tenKhachHang) {
        this.id_MaHoaDon = id_MaHoaDon;
        this.id_Ban = id_Ban;
        this.thoiGian_thanhtoan = thoiGian_thanhtoan;
        this.ngayThanhToan = ngayThanhToan;
        this.tongTien = tongTien;
        this.daThanhToan = daThanhToan;
        this.tenKhachHang = tenKhachHang;
    }

    public ThongKeHoaDon(String id_MaHoaDon, String id_Ban, String thoiGian_thanhtoan, Double tongTien, Boolean daThanhToan, String tenKhachHang) {
        this.id_MaHoaDon = id_MaHoaDon;
        this.id_Ban = id_Ban;
        this.thoiGian_thanhtoan = thoiGian_thanhtoan;
        this.tongTien = tongTien;
        this.daThanhToan = daThanhToan;
        this.tenKhachHang = tenKhachHang;
    }


}
