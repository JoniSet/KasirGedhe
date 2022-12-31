package com.example.kasirroti.Model;

public class ListLaporanPerproduk {
    private String nama_produk;
    private String harga_satuan;
    private String terjual;
    private String pendapatan;
    private String image;

    public ListLaporanPerproduk() {
    }

    public ListLaporanPerproduk(String nama_produk, String harga_satuan, String terjual, String pendapatan, String image) {
        this.nama_produk = nama_produk;
        this.harga_satuan = harga_satuan;
        this.terjual = terjual;
        this.pendapatan = pendapatan;
        this.image = image;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getHarga_satuan() {
        return harga_satuan;
    }

    public void setHarga_satuan(String harga_satuan) {
        this.harga_satuan = harga_satuan;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
