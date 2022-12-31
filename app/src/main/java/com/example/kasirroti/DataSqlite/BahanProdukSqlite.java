package com.example.kasirroti.DataSqlite;

public class BahanProdukSqlite {
    private String id, id_produk, id_bahan, terpakai, nama_bahan;

    public BahanProdukSqlite() {
    }

    public BahanProdukSqlite(String id, String id_produk, String id_bahan, String terpakai, String nama_bahan) {
        this.id = id;
        this.id_produk = id_produk;
        this.id_bahan = id_bahan;
        this.terpakai = terpakai;
        this.nama_bahan = nama_bahan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getId_bahan() {
        return id_bahan;
    }

    public void setId_bahan(String id_bahan) {
        this.id_bahan = id_bahan;
    }

    public String getTerpakai() {
        return terpakai;
    }

    public void setTerpakai(String terpakai) {
        this.terpakai = terpakai;
    }

    public String getNama_bahan() {
        return nama_bahan;
    }

    public void setNama_bahan(String nama_bahan) {
        this.nama_bahan = nama_bahan;
    }
}
