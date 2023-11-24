package com.example.quanlyquancaphe.models;

public class PDF {
    private String name,url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PDF() {
    }

    public PDF(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
