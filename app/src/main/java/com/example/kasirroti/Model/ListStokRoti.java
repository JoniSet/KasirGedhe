package com.example.kasirroti.Model;

import java.io.Serializable;

public class ListStokRoti implements Serializable {
    String id_bahan, nama_bahan, dipakai;

    public ListStokRoti(String id_bahan, String nama_bahan, String dipakai) {
        this.id_bahan   = id_bahan;
        this.nama_bahan = nama_bahan;
        this.dipakai    = dipakai;
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

    public String getDipakai() {
        return dipakai;
    }

    public void setDipakai(String dipakai) {
        this.dipakai = dipakai;
    }
}
