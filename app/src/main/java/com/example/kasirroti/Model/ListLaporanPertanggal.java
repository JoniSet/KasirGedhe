package com.example.kasirroti.Model;

public class ListLaporanPertanggal {
    private String tanggal;
    private String bulan;
    private String tahun;
    private String jml_transaksi;
    private String pendapatan;

    public ListLaporanPertanggal() {
    }

    public ListLaporanPertanggal(String tanggal, String bulan, String tahun, String jml_transaksi, String pendapatan) {
        this.tanggal        = tanggal;
        this.tahun          = tahun;
        this.bulan          = bulan;
        this.jml_transaksi  = jml_transaksi;
        this.pendapatan     = pendapatan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
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
