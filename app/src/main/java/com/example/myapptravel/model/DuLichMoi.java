package com.example.myapptravel.model;

import java.io.Serializable;

public class DuLichMoi implements Serializable {
    int id;
    String tendulich;
    String hinhanh;
    String batdau;
    String ketthuc;
    String mota;
    String giatien;
    int diadiem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTendulich() {
        return tendulich;
    }

    public void setTendulich(String tendulich) {
        this.tendulich = tendulich;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getBatdau() {
        return batdau;
    }

    public void setBatdau(String batdau) {
        this.batdau = batdau;
    }

    public String getKetthuc() {
        return ketthuc;
    }

    public void setKetthuc(String ketthuc) {
        this.ketthuc = ketthuc;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getGiatien() {
        return giatien;
    }

    public void setGiatien(String giatien) {
        this.giatien = giatien;
    }

    public int getDiadiem() {
        return diadiem;
    }

    public void setDiadiem(int diadiem) {
        this.diadiem = diadiem;
    }
}
