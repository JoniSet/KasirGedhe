package com.example.kasirroti.bantuan;

public class ListStok {
    String id_stok, id_bahan, stok, nama_bahan;

    public ListStok() {
    }

    public ListStok(String id_stok, String id_bahan, String stok, String nama_bahan) {
        this.id_stok = id_stok;
        this.id_bahan = id_bahan;
        this.stok = stok;
        this.nama_bahan = nama_bahan;
    }

    public String getId_stok() {
        return id_stok;
    }

    public void setId_stok(String id_stok) {
        this.id_stok = id_stok;
    }

    public String getId_bahan() {
        return id_bahan;
    }

    public void setId_bahan(String id_bahan) {
        this.id_bahan = id_bahan;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getNama_bahan() {
        return nama_bahan;
    }

    public void setNama_bahan(String nama_bahan) {
        this.nama_bahan = nama_bahan;
    }
}
