package com.example.kasirroti.Model;

public class ListLaporanPerhari {
    private String jam;
    private String tanggal;
    private String bulan;
    private String tahun;
    private String no;
    private String pendapatan;
    private String id;
    private String status;

    public ListLaporanPerhari() {
    }

    public ListLaporanPerhari(String jam, String tanggal, String bulan, String tahun, String no, String pendapatan, String id, String status) {
        this.jam            = jam;
        this.tanggal        = tanggal;
        this.tahun          = tahun;
        this.bulan          = bulan;
        this.no             = no;
        this.pendapatan     = pendapatan;
        this.id             = id;
        this.status         = status;
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

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
