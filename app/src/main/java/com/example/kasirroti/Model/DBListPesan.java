package com.example.kasirroti.Model;

public class DBListPesan{
    String id;
    String kode;
    String id_produk;
    String name;
    String jumlah;
    String harga;
    String img;
    String total;

    // contrustor(empty)
    public DBListPesan() {
    }
    // constructor
    public DBListPesan(String kode, String id_produk, String name, String jumlah, String harga, String total) {
        this.kode   = kode;
        this.id_produk     = id_produk;
        this.name   = name;
        this.jumlah = jumlah;
        this.harga  = harga;
        this.total  = total;
    }

    /*Setter and Getter*/

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }
}