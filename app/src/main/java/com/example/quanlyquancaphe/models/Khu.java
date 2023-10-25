package com.example.quanlyquancaphe.models;

public class Khu {
    Integer id_Khu;
    String tenKhu;


    public Integer getId_Khu() {
        return id_Khu;
    }

    public void setId_Khu(Integer id_Khu) {
        this.id_Khu = id_Khu;
    }

    public String getTenKhu() {
        return tenKhu;
    }

    public void setTenKhu(String tenKhu) {
        this.tenKhu = tenKhu;
    }

    public Khu() {
    }

    public Khu(int ma, String tenkhu) {
        this.id_Khu = ma;
        this.tenKhu = tenkhu;
    }

}
