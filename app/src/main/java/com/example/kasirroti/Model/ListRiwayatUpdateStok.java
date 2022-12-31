package com.example.kasirroti.Model;

public class ListRiwayatUpdateStok {
    String nama_bahan, tgl, stok_masuk, name;

    public ListRiwayatUpdateStok() {
    }

    public ListRiwayatUpdateStok(String nama_bahan, String tgl, String stok_masuk, String name) {
        this.nama_bahan = nama_bahan;
        this.tgl = tgl;
        this.stok_masuk = stok_masuk;
        this.name = name;
    }

    public String getNama_bahan() {
        return nama_bahan;
    }

    public void setNama_bahan(String nama_bahan) {
        this.nama_bahan = nama_bahan;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getStok_masuk() {
        return stok_masuk;
    }

    public void setStok_masuk(String stok_masuk) {
        this.stok_masuk = stok_masuk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
