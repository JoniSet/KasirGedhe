package com.example.kasirroti.DataSqlite;

public class KategoriSqlite {
    private String id, nama_kategori, urutan;

    public KategoriSqlite() {
    }

    public KategoriSqlite(String id, String nama_kategori, String urutan) {
        this.id = id;
        this.nama_kategori = nama_kategori;
        this.urutan = urutan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getUrutan() {
        return urutan;
    }

    public void setUrutan(String urutan) {
        this.urutan = urutan;
    }
}
