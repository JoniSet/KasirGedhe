package com.example.kasirroti.DataSqlite;

public class ProdukSqlite {
    private String id, id_kategori, nama_produk, harga, img;

    public ProdukSqlite() {
    }

    public ProdukSqlite(String id, String id_kategori, String nama_produk, String harga, String img) {
        this.id = id;
        this.id_kategori = id_kategori;
        this.nama_produk = nama_produk;
        this.harga = harga;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(String id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
