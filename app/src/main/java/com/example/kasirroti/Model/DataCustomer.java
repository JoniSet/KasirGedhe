package com.example.kasirroti.Model;

public class DataCustomer {
    String id, nama, notelp, tgl_lahir, poin;

    public DataCustomer(String id, String nama, String notelp, String tgl_lahir, String poin) {
        this.id = id;
        this.nama = nama;
        this.notelp = notelp;
        this.tgl_lahir = tgl_lahir;
        this.poin = poin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    public String getPoin() {
        return poin;
    }

    public void setPoin(String poin) {
        this.poin = poin;
    }
}
