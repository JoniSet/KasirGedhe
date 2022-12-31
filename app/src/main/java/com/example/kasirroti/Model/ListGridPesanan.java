package com.example.kasirroti.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ListGridPesanan implements Serializable {

    private String id_produk, nama_produk, jumlah, harga_satuan, img, total;

    private ArrayList<ListStokRotiP> bahan;

    public ListGridPesanan(){

    }

    public ListGridPesanan(String id_produk, String nama_produk, String jumlah, String harga_satuan, String img, String total, ArrayList<ListStokRotiP> bahan) {
        this.id_produk = id_produk;
        this.nama_produk = nama_produk;
        this.jumlah = jumlah;
        this.harga_satuan = harga_satuan;
        this.img = img;
        this.total = total;
        this.bahan = bahan;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
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

    public String getHarga_satuan() {
        return harga_satuan;
    }

    public void setHarga_satuan(String harga_satuan) {
        this.harga_satuan = harga_satuan;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<ListStokRotiP> getBahan() {
        return bahan;
    }

    public void setBahan(ArrayList<ListStokRotiP> bahan) {
        this.bahan = bahan;
    }
}
