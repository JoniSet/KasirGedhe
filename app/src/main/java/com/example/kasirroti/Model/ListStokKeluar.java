package com.example.kasirroti.Model;

public class ListStokKeluar {
    private String id_stok, nama_stok, jml_stok;

    public ListStokKeluar() {
    }

    public ListStokKeluar(String id_stok, String nama_stok, String jml_stok) {
        this.id_stok    = id_stok;
        this.nama_stok  = nama_stok;
        this.jml_stok   = jml_stok;
    }

    public String getJml_stok() {
        return jml_stok;
    }

    public void setJml_stok(String jml_stok) {
        this.jml_stok = jml_stok;
    }

    public String getNama_stok() {
        return nama_stok;
    }

    public void setNama_stok(String nama_stok) {
        this.nama_stok = nama_stok;
    }

    public String getId_stok() {
        return id_stok;
    }

    public void setId_stok(String id_stok) {
        this.id_stok = id_stok;
    }
}
