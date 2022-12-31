package com.example.kasirroti.Model;

public class ListOutlet {
    String id, nama_outlet;

    public ListOutlet() {
    }

    public ListOutlet(String id, String nama_outlet) {
        this.id = id;
        this.nama_outlet = nama_outlet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_outlet() {
        return nama_outlet;
    }

    public void setNama_outlet(String nama_outlet) {
        this.nama_outlet = nama_outlet;
    }
}
