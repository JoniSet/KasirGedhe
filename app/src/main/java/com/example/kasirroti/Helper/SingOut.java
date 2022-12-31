package com.example.kasirroti.Helper;

import android.app.Activity;

import com.example.kasirroti.HomeActivity;
import com.shashank.sony.fancytoastlib.FancyToast;


public class SingOut {
    public void Logout(Activity context) {
        HomeActivity.sm.logout();
        HomeActivity.sessionManager.storeLogin(
                HomeActivity.id_outlet,
                HomeActivity.nama,
                HomeActivity.notelp,
                HomeActivity.alamat,
                HomeActivity.logo,
                HomeActivity.nama_rek,
                HomeActivity.no_rek,
                "0");
        context.finish();

        FancyToast.makeText(
                context,
                "Token anda kadaluarsa, Silahkan Login Kembali!",
                FancyToast.LENGTH_SHORT,
                FancyToast.ERROR,
                false
        ).show();
    }
}
