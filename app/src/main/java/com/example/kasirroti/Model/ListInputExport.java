package com.example.kasirroti.Model;

import java.io.Serializable;

public class ListInputExport implements Serializable {
    String jenis, jumlah;

    public ListInputExport() {
    }

    public ListInputExport(String jenis, String jumlah) {
        this.jenis = jenis;
        this.jumlah = jumlah;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
