package com.example.kasirroti.SessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.AuthScreen.SecondLoginActivity;

import java.util.HashMap;


public class SessionManagerUser {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    public static final String KEY_TOKEN    = "token";
    public static final String KEY_NAME     = "name";
    public static final String KEY_JABATAN  = "jabatan";
    private static final String IS_LOGIN    = "loginstatus";
    private final String SHARE_NAME         = "loginsession";
    public final int MODE_PRIVATE           = 0;
    public Context context;

    public SessionManagerUser(Context _context)
    {
        this.context                        = _context;
        sharedPreferences                   = _context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        edit                                = sharedPreferences.edit();
    }

    public void storeLogin(String token, String nama, String jabatan)
    {
        edit.putBoolean(IS_LOGIN, true);
        edit.putString(KEY_TOKEN, token);
        edit.putString(KEY_NAME, nama);
        edit.putString(KEY_JABATAN, jabatan);
        edit.commit();
    }

    public HashMap getDetailLogin()
    {
        HashMap<String, String> map         = new HashMap<>();
        map.put(KEY_TOKEN, sharedPreferences.getString(KEY_TOKEN, null));
        map.put(KEY_NAME, sharedPreferences.getString(KEY_NAME, null));
        map.put(KEY_JABATAN, sharedPreferences.getString(KEY_JABATAN, null));

        return map;
    }

    public void cekLogin(){
        if (!this.login()){
            Intent login                        = new Intent(context, SecondLoginActivity.class);
            context.startActivity(login);

        }
        else
        {
            Intent login                        = new Intent(context, HomeActivity.class);
            context.startActivity(login);
        }
    }

    public boolean login(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void logout(){
        edit.clear();
        edit.commit();
        Intent intent                       = new Intent(context, SecondLoginActivity.class);
        context.startActivity(intent);

    }
}
