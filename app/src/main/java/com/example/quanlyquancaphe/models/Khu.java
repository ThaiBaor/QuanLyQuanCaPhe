package com.example.quanlyquancaphe.models;

public class Khu {
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

    public Khu(Integer id_Khu, String tenKhu) {
        this.id_Khu = id_Khu;
        this.tenKhu = tenKhu;
    }
    public Khu() {
    }

    @Override
    public String toString() {
        return "Khu{" +
                "id_Khu=" + id_Khu +
                ", tenKhu='" + tenKhu + '\'' +
                '}';
    }

    private Integer id_Khu;
    private String tenKhu;
}
