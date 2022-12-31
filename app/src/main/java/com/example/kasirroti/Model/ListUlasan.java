package com.example.kasirroti.Model;

public class ListUlasan {
    String nama_user, tgl_ulasan, bintang, saran;

    public ListUlasan() {
    }

    public ListUlasan(String nama_user, String tgl_ulasan, String bintang, String saran) {
        this.nama_user  = nama_user;
        this.tgl_ulasan = tgl_ulasan;
        this.bintang    = bintang;
        this.saran      = saran;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getTgl_ulasan() {
        return tgl_ulasan;
    }

    public void setTgl_ulasan(String tgl_ulasan) {
        this.tgl_ulasan = tgl_ulasan;
    }

    public String getBintang() {
        return bintang;
    }

    public void setBintang(String bintang) {
        this.bintang = bintang;
    }

    public String getSaran() {
        return saran;
    }

    public void setSaran(String saran) {
        this.saran = saran;
    }
}
