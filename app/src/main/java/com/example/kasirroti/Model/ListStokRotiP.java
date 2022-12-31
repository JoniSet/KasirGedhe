package com.example.kasirroti.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ListStokRotiP implements Serializable {
    String id_produk;
    ArrayList<ListStokRoti> listStokRoti;

    public ListStokRotiP() {
    }

    public ListStokRotiP(String id_produk, ArrayList<ListStokRoti> listStokRoti) {
        this.id_produk = id_produk;
        this.listStokRoti = listStokRoti;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public ArrayList<ListStokRoti> getListStokRoti() {
        return listStokRoti;
    }

    public void setListStokRoti(ArrayList<ListStokRoti> listStokRoti) {
        this.listStokRoti = listStokRoti;
    }
}
