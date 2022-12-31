package com.example.kasirroti.bantuan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.AuthScreen.GantiPasswordActivity;
import com.example.kasirroti.StokScreen.HistoryPerubahanStokActivity;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.LaporanScreen.LaporanActivity;
import com.example.kasirroti.AuthScreen.ProfilOutletActivity;
import com.example.kasirroti.R;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.SettingPrinterActivity;
import com.google.android.material.navigation.NavigationView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ManajemenStok extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView txt_outlet, txt_posisi, txt_nama, hotlist_hot, txt_title;
    public static RecyclerView list_stok;
    private ArrayList<ListStok> listStok;
    private AdapterStok adapter;

    private ImageView hotlist_bell, img_kiri;

    String tipe;

    public static Dialog dial;
//    public static ShimmerFrameLayout shimmer_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manajemen_stok);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_stok);
        txt_title       = findViewById(R.id.txt_title);

        hotlist_hot     = findViewById(R.id.hotlist_hot);
        hotlist_bell    = findViewById(R.id.hotlist_bell);
        img_kiri        = findViewById(R.id.img_kiri);

        Intent intent   = getIntent();
        tipe            = intent.getStringExtra("tipe");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_stok);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_stok);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toggle.setDrawerIndicatorEnabled(true);
        txt_title.setText("Manajemen Stok");
        img_kiri.setVisibility(View.GONE);


        img_kiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        View view                   = navigationView.getHeaderView(0);
        txt_nama                    = view.findViewById(R.id.txt_nama);
        txt_outlet                  = view.findViewById(R.id.txt_outlet);
        txt_posisi                  = view.findViewById(R.id.txt_posisi);

        HashMap<String, String> map = HomeActivity.sm.getDetailLogin();

        txt_outlet.setText(HomeActivity.nama);
        txt_nama.setText(map.get(HomeActivity.sm.KEY_NAME));
        txt_posisi.setText(" - " + map.get(HomeActivity.sm.KEY_JABATAN));

        txt_outlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent       = new Intent(ManajemenStok.this, ProfilOutletActivity.class);
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

        hotlist_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManajemenStok.this, HistoryPerubahanStokActivity.class));
            }
        });

        listStok                    = new ArrayList<>();
        list_stok                   = findViewById(R.id.list_stok);
        list_stok.setLayoutManager(new LinearLayoutManager(this));
        list_stok.setItemAnimator(new DefaultItemAnimator());
        list_stok.setHasFixedSize(true);

        set_stok();

    }

    private void set_stok() {
        listStok.clear();
        list_stok.setAdapter(null);

        dial                    = new Dialog(ManajemenStok.this);
        dial.show();
        dial.setContentView(R.layout.loading);
        dial.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dial.setCanceledOnTouchOutside(false);
        dial.setCancelable(true);

        AndroidNetworking.post(Server.URL + "stok_manajemen")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("200"))
                            {
                                JSONArray array         = response.getJSONArray("data");

                                for(int i = 0; i < array.length(); i++)
                                {
                                    JSONObject object   = array.getJSONObject(i);

                                    ListStok data       = new ListStok(
                                            object.getString("id_stok"),
                                            object.getString("id_bahan"),
                                            object.getString("stok"),
                                            object.getString("nama_bahan")
                                    );

                                    listStok.add(data);

                                    adapter             = new AdapterStok(ManajemenStok.this, listStok);

                                    adapter.setOnItemClickListener(new AdapterStok.recyclerViewClickListener() {
                                        @Override
                                        public void onClick(View v, int position) {
//                                            Toast.makeText(ManajemenStokActivity.this, listStok.get(position).getJml_stok(), Toast.LENGTH_SHORT).show();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ManajemenStok.this);
                                            LayoutInflater inflater = getLayoutInflater();
                                            View view = inflater.inflate(R.layout.dialog_update_stok, null);
                                            builder.setCancelable(true);
                                            builder.setView(view);

                                            Dialog dialog = builder.create();
                                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                                            TextView txt_kode_stok_dialog   = view.findViewById(R.id.txt_kode_stok_dialog);
                                            TextView txt_nama_bahan         = view.findViewById(R.id.txt_nama_bahan);
                                            TextView txt_jml_skrg           = view.findViewById(R.id.txt_jml_skrg);
                                            TextView txt_ok                 = view.findViewById(R.id.txt_ok);
                                            EditText edt_nama_pembeli       = view.findViewById(R.id.edt_cari_nomor_wa);

                                            ImageView img_stok_min          = view.findViewById(R.id.img_stok_min);
                                            ImageView img_stok_plus         = view.findViewById(R.id.img_stok_plus);

                                            txt_kode_stok_dialog.setText(listStok.get(position).getNama_bahan().substring(0, 2));
                                            txt_nama_bahan.setText(listStok.get(position).getNama_bahan());
                                            txt_jml_skrg.setText(listStok.get(position).getStok());
                                            edt_nama_pembeli.setText("1");


                                            img_stok_plus.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int jml                 = Integer.parseInt(edt_nama_pembeli.getText().toString());
                                                    int jml_final           = jml + 1;

                                                    edt_nama_pembeli.setText(String.valueOf(jml_final));
                                                    img_stok_min.setVisibility(View.VISIBLE);
                                                }
                                            });

                                            img_stok_min.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int jml                 = Integer.parseInt(edt_nama_pembeli.getText().toString());
                                                    int jml_final           = jml - 1;

                                                    edt_nama_pembeli.setText(String.valueOf(jml_final));

                                                    if (Integer.parseInt(edt_nama_pembeli.getText().toString()) < 2)
                                                    {
                                                        img_stok_min.setVisibility(View.GONE);
                                                    }
                                                    else
                                                    {
                                                        img_stok_min.setVisibility(View.VISIBLE);
                                                    }

                                                }
                                            });

                                            txt_ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (edt_nama_pembeli.getText().toString().isEmpty()) {
                                                        FancyToast.makeText(ManajemenStok.this, "Stok belum di isi!",
                                                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                                                    }
//                                                            else if(Integer.parseInt(edt_nama_pembeli.getText().toString()) < Integer.parseInt(listStok.get(position).getJml_stok())){
//                                                                FancyToast.makeText(ManajemenStokActivity.this, "Anda Tidak Bisa Mengurangi Stok Bahan!",
//                                                                        FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
//                                                            }
                                                    else {
                                                        dialog.dismiss();
                                                        Dialog dialog1              = new Dialog(ManajemenStok.this);
                                                        dialog1.show();
                                                        dialog1.setContentView(R.layout.loading);
                                                        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                                                        AndroidNetworking.post(Server.URL + "stok_masuk")
                                                                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                                                                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                                                                .addBodyParameter("id_bahan", listStok.get(position).getId_bahan())
                                                                .addBodyParameter("stok_masuk", edt_nama_pembeli.getText().toString())
                                                                .setPriority(Priority.MEDIUM)
                                                                .build()
                                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                                    @Override
                                                                    public void onResponse(JSONObject response) {
                                                                        try {
                                                                            if (response.getString("response").equals("200"))
                                                                            {
                                                                                dialog1.dismiss();
                                                                                set_stok();
                                                                                FancyToast.makeText(ManajemenStok.this, "Stok Bahan Berhasil Dirubah!",
                                                                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                                                            }
                                                                            else
                                                                            {
                                                                                dialog1.dismiss();
                                                                                FancyToast.makeText(ManajemenStok.this, "Gagal Merubah Stok Bahan!",
                                                                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                            dialog1.dismiss();
                                                                            FancyToast.makeText(ManajemenStok.this, e.getMessage(),
                                                                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onError(ANError anError) {
                                                                        dialog1.dismiss();
                                                                        FancyToast.makeText(ManajemenStok.this, anError.getErrorDetail(),
                                                                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                                    }
                                                                });

                                                        dialog.dismiss();
                                                    }
                                                }
                                            });

                                            dialog.show();
                                        }
                                    });
                                    adapter.notifyDataSetChanged();
                                    list_stok.setAdapter(adapter);
                                }

                                dial.dismiss();
                                list_stok.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                FancyToast.makeText(ManajemenStok.this,  "Data Kosong",
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dial.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(ManajemenStok.this,  "Masalah Request!",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dial.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        FancyToast.makeText(ManajemenStok.this,  "Masalah Jaringan!",
                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        dial.dismiss();
                    }
                });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_stok);

        if (id == R.id.nav_stock) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_transaksi) {
            Intent intent       = new Intent(ManajemenStok.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_report) {
            startActivity(new Intent(ManajemenStok.this, LaporanActivity.class));
            finish();
        } else if (id == R.id.nav_setting) {
            setting_printer();
        } else if (id == R.id.nav_ubah_password) {
            startActivity(new Intent(ManajemenStok.this, GantiPasswordActivity.class));
        } else if (id == R.id.nav_logout) {
            Logout();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setting_printer() {
        startActivity(new Intent(ManajemenStok.this, SettingPrinterActivity.class));
    }

    private void Logout() {

        AndroidNetworking.get(Server.URL + "logout")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
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
                                FancyToast.makeText(ManajemenStok.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Kategori", anError.getMessage());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_stok);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }
}