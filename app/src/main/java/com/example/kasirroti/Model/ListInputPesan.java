package com.example.kasirroti.Model;

public class ListInputPesan {
    String nama_produk, jumlah, harga;

    public ListInputPesan() {
    }

    public ListInputPesan(String nama_produk, String jumlah, String harga) {
        this.nama_produk = nama_produk;
        this.jumlah = jumlah;
        this.harga = harga;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
