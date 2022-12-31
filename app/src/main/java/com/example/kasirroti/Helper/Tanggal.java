package com.example.kasirroti.Helper;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tanggal {
    public String getTanggal(){
        DateFormat dateFormat       = new SimpleDateFormat("dd MMM yyyy");
        Date date                   = new Date();

        return dateFormat.format(date);
    }

    public String getTanggal1(){
        DateFormat dateFormat       = new SimpleDateFormat("dd MMM yyyy");
        Date date                   = new Date();

        return dateFormat.format(date);
    }

    public String getTanggal2(){
        DateFormat dateFormat       = new SimpleDateFormat("yyyy-MM-dd");
        Date date                   = new Date();

        return dateFormat.format(date);
    }

    public String getTime()
    {
        DateFormat dateFormat       = new SimpleDateFormat("HH:mm:ss");
        Date date                   = new Date();

        return dateFormat.format(date);
    }

    public String getJam()
    {
        DateFormat dateFormat       = new SimpleDateFormat("HH");
        Date date                   = new Date();

        return dateFormat.format(date);
    }

    public String getBulan(){
        DateFormat dateFormat       = new SimpleDateFormat("yyyy-MM-");
        Date date                   = new Date();

        return dateFormat.format(date);
    }

    public String getTahun(){
        DateFormat dateFormat       = new SimpleDateFormat("yyyy");
        Date date                   = new Date();

        return dateFormat.format(date);
    }

    public String getMinggu(){
        DateFormat dateFormat       = new SimpleDateFormat("yyyy-");
        Date date                   = new Date();

        String minggu               = String.valueOf(date.getDate() - 1);

        return minggu;
    }

    public String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

}
