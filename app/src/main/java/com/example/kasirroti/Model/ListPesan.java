package com.example.kasirroti.Model;

import java.io.Serializable;

public class ListPesan implements Serializable {
    String id_produk;
    String name;
    String jumlah;
    String harga_satuan;
    String img;
    String total;

    // contrustor(empty)
    public ListPesan() {
    }
    // constructor
    public ListPesan(String id_produk, String name, String jumlah, String harga_satuan, String img, String total) {
        this.id_produk = id_produk;
        this.name   = name;
        this.jumlah = jumlah;
        this.harga_satuan = harga_satuan;
        this.img    = img;
        this.total  = total;
    }

    /*Setter and Getter*/

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}

