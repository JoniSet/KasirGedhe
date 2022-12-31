package com.example.kasirroti.Model;

public class ListSisaBahan {
    String id_bahan, nama_bahan, jumlah, satuan;

    public ListSisaBahan() {
    }

    public ListSisaBahan(String id_bahan, String nama_bahan, String jumlah, String satuan) {
        this.id_bahan = id_bahan;
        this.nama_bahan = nama_bahan;
        this.jumlah = jumlah;
        this.satuan = satuan;
    }

    public String getId_bahan() {
        return id_bahan;
    }

    public void setId_bahan(String id_bahan) {
        this.id_bahan = id_bahan;
    }

    public String getNama_bahan() {
        return nama_bahan;
    }

    public void setNama_bahan(String nama_bahan) {
        this.nama_bahan = nama_bahan;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
