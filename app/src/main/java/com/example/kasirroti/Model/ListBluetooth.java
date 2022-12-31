package com.example.kasirroti.Model;

public class ListBluetooth {
    String id, nama_bt;

    public ListBluetooth() {
    }

    public ListBluetooth(String id, String nama_bt) {
        this.id = id;
        this.nama_bt = nama_bt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_outlet() {
        return nama_bt;
    }

    public void setNama_outlet(String nama_outlet) {
        this.nama_bt = nama_outlet;
    }
}
