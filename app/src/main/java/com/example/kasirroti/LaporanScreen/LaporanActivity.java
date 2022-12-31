package com.example.kasirroti.LaporanScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.AuthScreen.FotoTutupActivity;
import com.example.kasirroti.AuthScreen.GantiPasswordActivity;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.StokScreen.ManajemenStokActivity;
import com.example.kasirroti.Model.ListGambar;
import com.example.kasirroti.RedeemScreen.PoinActivity;
import com.example.kasirroti.AuthScreen.ProfilOutletActivity;
import com.example.kasirroti.R;
import com.example.kasirroti.SOPActivity;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.SettingPrinterActivity;
import com.example.kasirroti.SinkronDataActivity;
import com.google.android.material.navigation.NavigationView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LaporanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private LinearLayout L_1, L_2, L_12;
    ArrayList<ListGambar> listGambar;

    String tampil_nama;

    TextView txt_outlet, txt_posisi, txt_nama;

    private Handler slideHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_laporan);
        toolbar.setTitle("Laporan Penjualan");
        setSupportActionBar(toolbar);


        L_1         = findViewById(R.id.L_1);
        L_2         = findViewById(R.id.L_2);
        L_12        = findViewById(R.id.L_12);

        L_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LaporanActivity.this, SemuaLaporanTransActivity.class));
            }
        });

        L_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LaporanActivity.this, LaporanPenjualanPerProdukActivity.class));
            }
        });

        L_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LaporanActivity.this, GeneralReportActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_laporan);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view                   = navigationView.getHeaderView(0);
        txt_nama                    = view.findViewById(R.id.txt_nama);
        txt_outlet                  = view.findViewById(R.id.txt_outlet);
        txt_posisi                  = view.findViewById(R.id.txt_posisi);
        TextView txt_posisi         = view.findViewById(R.id.txt_posisi);

        HashMap<String, String> map = HomeActivity.sm.getDetailLogin();

        txt_outlet.setText(HomeActivity.nama);
        txt_posisi.setText(" - " + map.get(HomeActivity.sm.KEY_JABATAN));

        String[] dua_kata   = map.get(HomeActivity.sm.KEY_NAME).trim().split(" ");
        if (dua_kata.length >= 2){
            tampil_nama  = dua_kata[0] + " " + dua_kata[1];
        }
        else{
            tampil_nama  = map.get(HomeActivity.sm.KEY_NAME);
        }
        txt_nama.setText(tampil_nama);

        txt_outlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent       = new Intent(LaporanActivity.this, ProfilOutletActivity.class);
                intent.putExtra("id_outlet", HomeActivity.id_outlet);
                intent.putExtra("nama_outlet", HomeActivity.nama);
                intent.putExtra("alamat", HomeActivity.alamat);
                intent.putExtra("no_telp", HomeActivity.notelp);
                intent.putExtra("logo", HomeActivity.logo);
                intent.putExtra("nama_rek", HomeActivity.nama_rek);
                intent.putExtra("no_rek", HomeActivity.no_rek);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_laporan);

        if (id == R.id.nav_stock) {
            Intent intent       = new Intent(LaporanActivity.this, ManajemenStokActivity.class);
            intent.putExtra("tipe", "nonhome");
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_transaksi) {
            Intent intent       = new Intent(LaporanActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_poin) {
            startActivity(new Intent(LaporanActivity.this, PoinActivity.class));
        } else if (id == R.id.nav_report) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_setting) {
            setting_printer();
        } else if (id == R.id.nav_ubah_password) {
            startActivity(new Intent(LaporanActivity.this, GantiPasswordActivity.class));
        } else if (id == R.id.nav_sync_data) {
            startActivity(new Intent(LaporanActivity.this, SinkronDataActivity.class));
        } else if (id == R.id.nav_logout) {
            Logout();
        } else if (id == R.id.nav_tutup_toko) {
            showDialog();
        } else if (id == R.id.nav_foto) {
            reqPermission();
        }else if (id == R.id.nav_sop) {
            startActivity(new Intent(LaporanActivity.this, SOPActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setting_printer() {
        startActivity(new Intent(LaporanActivity.this, SettingPrinterActivity.class));
    }

    private void Logout() {
        AndroidNetworking.get(Server.URL + "logout")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String message = response.getString("message");
                            String responseCode = response.getString("response");

                            if (responseCode.equals("200")) {
                                HomeActivity.sm.logout();
                                HomeActivity.sessionManager.storeLogin(HomeActivity.id_outlet, HomeActivity.nama, HomeActivity.notelp, HomeActivity.alamat, HomeActivity.logo, HomeActivity.nama_rek, HomeActivity.no_rek, "0");
                                finish();

                            } else {
                                Toast.makeText(LaporanActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(LaporanActivity.this);
                        }
                        else {
                            Log.d("Kategori", anError.getMessage());
                        }
                    }
                });
    }

    private void tutup_toko(){
        Dialog dialog       = new Dialog(LaporanActivity.this);
        dialog.setContentView(R.layout.loading);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        TextView txt_title  = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Menutup Toko");

        AndroidNetworking.post(Server.URL + "tutup")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String res_cde      = response.getString("response");
                            if (res_cde.equals("200")){
                                Logout();
                                dialog.dismiss();
                                FancyToast.makeText(
                                        LaporanActivity.this,
                                        "Toko Berhasil Ditutup",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        false
                                ).show();
                            }
                            else{
                                FancyToast.makeText(
                                        LaporanActivity.this,
                                        "Gagal Menutup Toko",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(
                                    LaporanActivity.this,
                                    "Kesalahan Request!",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.ERROR,
                                    false
                            ).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(LaporanActivity.this);
                        }
                        else {
                            FancyToast.makeText(
                                    LaporanActivity.this,
                                    "Jaringan Bermasalah!",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.ERROR,
                                    false
                            ).show();
                            dialog.dismiss();
                        }
                    }
                });
    }

    private void showDialog(){
        Dialog dialog       = new Dialog(LaporanActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_tutup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        Button btn_batal_tutup  = dialog.findViewById(R.id.btn_batal_tutup);
        Button btn_lanjut_tutup = dialog.findViewById(R.id.btn_lanjut_tutup);

        btn_batal_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_lanjut_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutup_toko();
            }
        });
    }

    private void reqPermission() {

        if (hasCameraPermissions() || hasLocationPermissions()) {
            Intent intent   = new Intent(LaporanActivity.this, FotoTutupActivity.class);
            startActivity(intent);
        }
        else{
            requestPermission();
        }
    }

    private boolean hasCameraPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasLocationPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                10);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_laporan);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(LaporanActivity.this, HomeActivity.class));
            finish();
            super.onBackPressed();
        }
    }
}