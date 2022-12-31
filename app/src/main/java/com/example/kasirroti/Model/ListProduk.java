package com.example.kasirroti.Model;

public class ListProduk {
    String id, id_kategori, nama_produk, harga, img, jml;

    public ListProduk() {
    }

    public ListProduk(String id, String id_kategori, String nama_produk, String harga, String img, String jml) {
        this.id = id;
        this.id_kategori = id_kategori;
        this.nama_produk = nama_produk;
        this.harga = harga;
        this.img = img;
        this.jml = jml;
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

    public String getJml() {
        return jml;
    }

    public void setJml(String jml) {
        this.jml = jml;
    }
}
