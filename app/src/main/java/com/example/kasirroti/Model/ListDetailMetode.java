package com.example.kasirroti.Model;

public class ListDetailMetode {

    private String tipe, jml_transaksi, total_pendapatan;

    public ListDetailMetode() {
    }

    public ListDetailMetode(String tipe, String jml_transaksi, String total_pendapatan) {
        this.tipe = tipe;
        this.jml_transaksi = jml_transaksi;
        this.total_pendapatan = total_pendapatan;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getJml_transaksi() {
        return jml_transaksi;
    }

    public void setJml_transaksi(String jml_transaksi) {
        this.jml_transaksi = jml_transaksi;
    }

    public String getTotal_pendapatan() {
        return total_pendapatan;
    }

    public void setTotal_pendapatan(String total_pendapatan) {
        this.total_pendapatan = total_pendapatan;
    }
}
