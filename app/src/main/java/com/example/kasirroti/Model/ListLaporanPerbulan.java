package com.example.kasirroti.Model;

public class ListLaporanPerbulan {
    private String bulan;
    private String jml_transaksi;
    private String pendapatan;

    public ListLaporanPerbulan() {
    }

    public ListLaporanPerbulan(String bulan, String jml_transaksi, String pendapatan) {
        this.bulan          = bulan;
        this.jml_transaksi  = jml_transaksi;
        this.pendapatan     = pendapatan;
    }

    public String getJml_transaksi() {
        return jml_transaksi;
    }

    public void setJml_transaksi(String jml_transaksi) {
        this.jml_transaksi = jml_transaksi;
    }

    public String getPendapatan() {
        return pendapatan;
    }

    public void setPendapatan(String pendapatan) {
        this.pendapatan = pendapatan;
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
    }
}
