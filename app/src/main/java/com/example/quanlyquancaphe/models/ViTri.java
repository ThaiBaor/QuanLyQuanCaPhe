package com.example.quanlyquancaphe.models;

public class ViTri {
    private Integer id;
    private String vitri;

    public ViTri() {
    }

    public ViTri(Integer id, String vitri) {
        this.id = id;
        this.vitri = vitri;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVitri() {
        return vitri;
    }

    public void setVitri(String vitri) {
        this.vitri = vitri;
    }
}
