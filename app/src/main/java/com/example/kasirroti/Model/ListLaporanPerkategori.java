package com.example.kasirroti.Model;

public class ListLaporanPerkategori {
    private String nama_kategori, terjual, pendapatan;

    public ListLaporanPerkategori() {
    }

    public ListLaporanPerkategori(String nama_kategori, String terjual, String pendapatan) {
        this.nama_kategori = nama_kategori;
        this.terjual = terjual;
        this.pendapatan = pendapatan;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getTerjual() {
        return terjual;
    }

    public void setTerjual(String terjual) {
        this.terjual = terjual;
    }

    public String getPendapatan() {
        return pendapatan;
    }

    public void setPendapatan(String pendapatan) {
        this.pendapatan = pendapatan;
    }
}
