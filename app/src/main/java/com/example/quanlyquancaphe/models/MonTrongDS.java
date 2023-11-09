package com.example.quanlyquancaphe.models;

public class MonTrongDS extends Mon{
    private Integer sl = 0;

    public MonTrongDS() {
    }

    public MonTrongDS(String id_Mon, String moTa, String tenMon, String hinh, Double donGia, Integer giamGia, Integer id_Loai, Integer slDaBan, Boolean hetMon, Integer sl) {
        super(id_Mon, moTa, tenMon, hinh, donGia, giamGia, id_Loai, slDaBan , hetMon);
        this.sl = sl;
    }

    public Integer getSl() {
        return sl;
    }

    public void setSl(Integer sl) {
        this.sl = sl;
    }
    public void tang(){
        if (this.sl < 99){
            ++this.sl;
        }
    }
    public void giam(){
        if (this.sl > 0){
           --this.sl;
        }
    }
}
