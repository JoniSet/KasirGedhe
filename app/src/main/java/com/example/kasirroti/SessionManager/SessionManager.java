package com.example.kasirroti.SessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.kasirroti.AuthScreen.FirstLoginActivity;

import java.util.HashMap;


public class SessionManager {
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public static final String KEY_ID       = "id";
    public static final String NAMA         = "nama";
    public static final String TELP         = "telp";
    public static final String ALAMAT       = "alamat";
    public static final String LOGO         = "logo";
    public static final String NAMA_REK     = "nama_rek";
    public static final String NO_REK       = "no_rek";
    public static final String KLIK_KAEGORI = "klik";
    public static final String IS_LOGIN     = "loginstatus";
    public final String SHARE_NAME          = "loginsession";
    public final int MODE_PRIVATE           = 0;
    public Context context;

    public SessionManager(Context _context)
    {
        this.context                        = _context;
        sharedPreferences                   = _context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        edit                                = sharedPreferences.edit();
    }

    public void storeLogin(String id, String nama, String telp, String alamat, String logo, String nama_rek, String no_rek, String klik)
    {
        edit.putBoolean(IS_LOGIN, true);
        edit.putString(KEY_ID, id);
        edit.putString(NAMA, nama);
        edit.putString(TELP, telp);
        edit.putString(ALAMAT, alamat);
        edit.putString(LOGO, logo);
        edit.putString(NAMA_REK, nama_rek);
        edit.putString(NO_REK, no_rek);
        edit.putString(KLIK_KAEGORI, klik);
        edit.commit();
    }

    public HashMap getDetailLogin()
    {
        HashMap<String, String> map         = new HashMap<>();
        map.put(KEY_ID, sharedPreferences.getString(KEY_ID, null));
        map.put(NAMA, sharedPreferences.getString(NAMA, null));
        map.put(TELP, sharedPreferences.getString(TELP, null));
        map.put(ALAMAT, sharedPreferences.getString(ALAMAT, null));
        map.put(LOGO, sharedPreferences.getString(LOGO, null));
        map.put(NAMA_REK, sharedPreferences.getString(NAMA_REK, null));
        map.put(NO_REK, sharedPreferences.getString(NO_REK, null));
        map.put(KLIK_KAEGORI, sharedPreferences.getString(KLIK_KAEGORI, null));

        return map;
    }

    public void updatePoin(String klik_baru){
        edit.putString(KLIK_KAEGORI, klik_baru);
        edit.commit();
        edit.apply();
    }

    public void cekLogin(){
        if (!this.login()){
            Intent login                        = new Intent(context, FirstLoginActivity.class);
            login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(login);

        }
    }

    public boolean login(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void logout(){
        edit.clear();
        edit.commit();
        Intent intent                       = new Intent(context, FirstLoginActivity.class);
        context.startActivity(intent);

    }
}
