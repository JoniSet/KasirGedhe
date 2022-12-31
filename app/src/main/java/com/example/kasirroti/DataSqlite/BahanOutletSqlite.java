package com.example.kasirroti.DataSqlite;

public class BahanOutletSqlite {
    private String id, id_bahan, stok, nama_bahan;

    public BahanOutletSqlite() {
    }

    public BahanOutletSqlite(String id, String id_bahan, String stok, String nama_bahan) {
        this.id = id;
        this.id_bahan = id_bahan;
        this.stok = stok;
        this.nama_bahan = nama_bahan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
