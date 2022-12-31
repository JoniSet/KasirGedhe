package com.example.kasirroti.Model;

public class ListKategori {
    String id_kategori, nama_kategori, di_klik;

    public ListKategori() {
    }

    public ListKategori(String id_kategori, String nama_kategori, String di_klik) {
        this.id_kategori = id_kategori;
        this.nama_kategori = nama_kategori;
        this.di_klik = di_klik;
    }

    public String getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(String id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getDi_klik() {
        return di_klik;
    }

    public void setDi_klik(String di_klik) {
        this.di_klik = di_klik;
    }
}
