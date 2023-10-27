package com.example.quanlyquancaphe.models;

public class Ban {
    public String getId_Ban() {
        return id_Ban;
    }

    public void setId_Ban(String id_Ban) {
        this.id_Ban = id_Ban;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public Integer getId_Khu() {
        return id_Khu;
    }

    public void setId_Khu(Integer id_Khu) {
        this.id_Khu = id_Khu;
    }

    public Integer getSoChoNgoi() {
        return soChoNgoi;
    }

    public void setSoChoNgoi(Integer soChoNgoi) {
        this.soChoNgoi = soChoNgoi;
    }
    public Integer getId_TrangThaiBan() {
        return id_TrangThaiBan;
    }

    public void setId_TrangThaiBan(Integer id_TrangThaiBan) {
        this.id_TrangThaiBan = id_TrangThaiBan;
    }

    public Ban(String id_Ban, String tenBan, Integer soChoNgoi, Integer id_Khu, Integer id_TrangThaiBan) {
        this.id_Ban = id_Ban;
        this.tenBan = tenBan;
        this.id_Khu = id_Khu;
        this.soChoNgoi = soChoNgoi;
        this.id_TrangThaiBan = id_TrangThaiBan;
    }
    public Ban(String id_Ban,String tenBan, Integer soChoNgoi, Integer id_Khu) {
        this.id_Ban = id_Ban;
        this.tenBan = tenBan;
        this.id_Khu = id_Khu;
        this.soChoNgoi = soChoNgoi;
    }
    public Ban(String tenBan, Integer soChoNgoi, Integer id_Khu) {
        this.tenBan = tenBan;
        this.id_Khu = id_Khu;
        this.soChoNgoi = soChoNgoi;
    }

    public Ban() {
    }

    private String id_Ban, tenBan;
    private Integer soChoNgoi, id_Khu, id_TrangThaiBan;

    @Override
    public String toString() {
        return "Ban{" +
                "maBan='" + id_Ban + '\'' +
                ", tenBan='" + tenBan + '\'' +
                ", soChoNgoi=" + soChoNgoi +
                ", maKhu=" + id_Khu +
                ", maTrangThai=" + id_TrangThaiBan +
                '}';
    }
}
