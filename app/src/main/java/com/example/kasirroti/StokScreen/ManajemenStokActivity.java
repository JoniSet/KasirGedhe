package com.example.kasirroti.StokScreen;

import static com.github.ybq.android.spinkit.animation.AnimationUtils.start;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.DataSqlite.BahanOutletSqlite;
import com.example.kasirroti.AuthScreen.FotoTutupActivity;
import com.example.kasirroti.AuthScreen.GantiPasswordActivity;
import com.example.kasirroti.Helper.SingOut;
import com.example.kasirroti.Helper.SqliteHelper;
import com.example.kasirroti.HomeActivity;
import com.example.kasirroti.LaporanScreen.LaporanActivity;
import com.example.kasirroti.RedeemScreen.PoinActivity;
import com.example.kasirroti.AuthScreen.ProfilOutletActivity;
import com.example.kasirroti.R;
import com.example.kasirroti.SOPActivity;
import com.example.kasirroti.Server.Server;
import com.example.kasirroti.SettingPrinterActivity;
import com.example.kasirroti.SinkronDataActivity;
import com.example.kasirroti.Helper.Tanggal;
import com.example.kasirroti.bantuan.AdapterStok;
import com.example.kasirroti.bantuan.ListStok;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class ManajemenStokActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView txt_outlet, txt_posisi, txt_nama, hotlist_hot, txt_title;
    public static RecyclerView list_stok;
    private ArrayList<ListStok> listStok;
    private AdapterStok adapter;

    String tampil_nama;

    private ImageView hotlist_bell, img_kiri, tutorial;

    ArrayList<BahanOutletSqlite> arrayList  = new ArrayList<>();
    ArrayList<String> sinkron_cek           = new ArrayList<>();

    String tipe;

    public static Dialog dial;
    SqliteHelper sqliteHelper;

    View view2;
    LinearLayout linear_a;
    Tanggal tanggal     = new Tanggal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manajemen_stok);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_stok);
        txt_title       = findViewById(R.id.txt_title);

        hotlist_hot     = findViewById(R.id.hotlist_hot);
        hotlist_bell    = findViewById(R.id.hotlist_bell);
        tutorial        = findViewById(R.id.tutorial);
        img_kiri        = findViewById(R.id.img_kiri);
//        shimmer_view_container        = findViewById(R.id.shimmer_view_container);

        Intent intent   = getIntent();
        tipe            = intent.getStringExtra("tipe");

        sqliteHelper    = new SqliteHelper(this);

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
                Intent intent       = new Intent(ManajemenStokActivity.this, ProfilOutletActivity.class);
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
                startActivity(new Intent(ManajemenStokActivity.this, HistoryPerubahanStokActivity.class));
            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_stok.scrollToPosition(0);
                tutorial.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        show_tutorial();
                    }
                }, 200);

            }
        });

        listStok                    = new ArrayList<>();
        list_stok                   = findViewById(R.id.list_stok);
        list_stok.setLayoutManager(new LinearLayoutManager(this));
        list_stok.setItemAnimator(new DefaultItemAnimator());
        list_stok.setHasFixedSize(true);

        set_stok(0);


    }

    private void set_stok(int pos) {
        listStok.clear();
        list_stok.setAdapter(null);
        sinkron_cek.clear();
        arrayList.clear();

        dial                    = new Dialog(ManajemenStokActivity.this);
        dial.show();
        dial.setContentView(R.layout.loading);
        dial.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dial.setCanceledOnTouchOutside(false);
        dial.setCancelable(true);

        arrayList               = sqliteHelper.readAllBahanOutletSqlite();

        AndroidNetworking.post(Server.URL + "stok_manajemen")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
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
                                    if (!arrayList.isEmpty()) {
                                        String cek_1    = sqliteHelper.readStokSqlite(object.getString("id_bahan"));
                                        if (!object.getString("stok").equals(cek_1)){
                                            sinkron_cek.add(object.getString("id_bahan"));
                                            Log.d("oke", String.valueOf(sinkron_cek.size()));
                                        }
                                        else {
                                            Log.d("oke", "oke");
                                        }
                                    }
                                    else {
                                        Log.d("oke", "oke");
                                    }

                                    ListStok data       = new ListStok(
                                            object.getString("id_stok"),
                                            object.getString("id_bahan"),
                                            object.getString("stok"),
                                            object.getString("nama_bahan")
                                    );

                                    listStok.add(data);

                                    adapter             = new AdapterStok(ManajemenStokActivity.this, listStok);

                                    adapter.setOnItemClickListener(new com.example.kasirroti.bantuan.AdapterStok.recyclerViewClickListener() {
                                        @Override
                                        public void onClick(View v, int position) {
//                                            Toast.makeText(ManajemenStokActivity.this, listStok.get(position).getJml_stok(), Toast.LENGTH_SHORT).show();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ManajemenStokActivity.this);
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
                                                    if (edt_nama_pembeli.getText().toString().isEmpty() || edt_nama_pembeli.getText().toString().equals("0")) {
                                                        FancyToast.makeText(ManajemenStokActivity.this, "Stok belum di isi!",
                                                                FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                                                    }
//                                                            else if(Integer.parseInt(edt_nama_pembeli.getText().toString()) < Integer.parseInt(listStok.get(position).getJml_stok())){
//                                                                FancyToast.makeText(ManajemenStokActivity.this, "Anda Tidak Bisa Mengurangi Stok Bahan!",
//                                                                        FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
//                                                            }
                                                    else {
                                                        txt_ok.setEnabled(false);
                                                        dialog.dismiss();
                                                        Dialog dialog1              = new Dialog(ManajemenStokActivity.this);
                                                        dialog1.show();
                                                        dialog1.setContentView(R.layout.loading);
                                                        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                                        dialog1.setCancelable(false);

                                                        AndroidNetworking.post(Server.URL + "stok_masuk")
                                                                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                                                                .addHeaders("Accept", "application/json")
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
                                                                                set_stok(position);
                                                                                txt_ok.setEnabled(true);
                                                                                FancyToast.makeText(ManajemenStokActivity.this, "Stok Bahan Berhasil Dirubah!",
                                                                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                                                            }
                                                                            else
                                                                            {
                                                                                dialog1.dismiss();
                                                                                txt_ok.setEnabled(true);
                                                                                FancyToast.makeText(ManajemenStokActivity.this, "Gagal Merubah Stok Bahan!",
                                                                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                            dialog1.dismiss();
                                                                            txt_ok.setEnabled(true);
                                                                            FancyToast.makeText(ManajemenStokActivity.this, e.getMessage(),
                                                                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onError(ANError anError) {
                                                                        if (anError.getErrorCode() == 401){
                                                                            SingOut signOut = new SingOut();
                                                                            signOut.Logout(ManajemenStokActivity.this);
                                                                        }
                                                                        else {
                                                                            dialog1.dismiss();
                                                                            txt_ok.setEnabled(true);
                                                                            FancyToast.makeText(ManajemenStokActivity.this, anError.getErrorDetail(),
                                                                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                                        }
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
                                    list_stok.scrollToPosition(pos);
                                }

                                dial.dismiss();
                                list_stok.setVisibility(View.VISIBLE);

                                if (sinkron_cek.size() > 0 ){
                                    cek_sinkron();
                                }
                            }
                            else
                            {
                                FancyToast.makeText(ManajemenStokActivity.this,  "Data Kosong",
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dial.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(ManajemenStokActivity.this,  "Masalah Request!",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dial.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                         if (anError.getErrorCode() == 401){
                            SingOut signOut = new SingOut();
                            signOut.Logout(ManajemenStokActivity.this);
                        }
                        else {
                             FancyToast.makeText(ManajemenStokActivity.this, "Masalah Jaringan!",
                                     FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                             dial.dismiss();
                         }
                    }
                });
    }

    public void cek_sinkron(){
        Dialog dialog       = new Dialog(ManajemenStokActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sinkron);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        Button btn_tutup            = dialog.findViewById(R.id.btn_tutup_dialog);
        Button btn_lanjut           = dialog.findViewById(R.id.btn_sinkron_dialog);

        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getBahanOutlet();
            }
        });

        btn_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void getBahanOutlet() {
        dial                    = new Dialog(ManajemenStokActivity.this);
        dial.show();
        dial.setContentView(R.layout.loading);
        dial.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dial.setCanceledOnTouchOutside(false);
        dial.setCancelable(false);

        sqliteHelper.delete_bahan_outlet();
        sqliteHelper.delete_info();

        AndroidNetworking.post(Server.URL + "stok_manajemen")
                .addHeaders("Authorization", "Bearer " + HomeActivity.token)
                .addHeaders("Accept", "application/json")
                .addBodyParameter("id_outlet", HomeActivity.id_outlet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            String responseCode = response.getString("response");

                            if (responseCode.equals("200")) {
                                JSONArray jsonArray         = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject   = jsonArray.getJSONObject(i);

                                    if (!jsonObject.getString("stok").equals("0")){
                                        sqliteHelper.add_bahan_outlet(
                                                jsonObject.getString("id_stok"),
                                                jsonObject.getString("id_bahan"),
                                                jsonObject.getString("stok"),
                                                jsonObject.getString("nama_bahan"));
                                    }

                                }
                                sqliteHelper.add_update_info("1", tanggal.getTanggal() + " " + tanggal.getTime());
                                FancyToast.makeText(ManajemenStokActivity.this,  "Sinkronasi bahan berhasil!" ,
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                dial.dismiss();
                            } else {
                                FancyToast.makeText(ManajemenStokActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dial.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(ManajemenStokActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dial.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dial.dismiss();
                        Log.d("Bahan Outlet", String.valueOf(anError.getErrorCode()));
                        if (anError.getErrorCode() == 401){
                            HomeActivity.sm.logout();
                            HomeActivity.sessionManager.storeLogin(HomeActivity.id_outlet, HomeActivity.nama, HomeActivity.notelp, HomeActivity.alamat, HomeActivity.logo, HomeActivity.nama_rek, HomeActivity.no_rek, "0");
                            finish();
                        }
                        else{
                            FancyToast.makeText(ManajemenStokActivity.this, "Gagal mengambil data Bahan Outlet, Jaringan Bermasalah",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
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
            if (tipe.equals("home")){
                finish();
            }
            else {
                Intent intent = new Intent(ManajemenStokActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        } else if (id == R.id.nav_poin) {
            startActivity(new Intent(ManajemenStokActivity.this, PoinActivity.class));
        } else if (id == R.id.nav_report) {
            startActivity(new Intent(ManajemenStokActivity.this, LaporanActivity.class));
            finish();
        } else if (id == R.id.nav_setting) {
            setting_printer();
        } else if (id == R.id.nav_sync_data) {
            startActivity(new Intent(ManajemenStokActivity.this, SinkronDataActivity.class));
        } else if (id == R.id.nav_ubah_password) {
            startActivity(new Intent(ManajemenStokActivity.this, GantiPasswordActivity.class));
        } else if (id == R.id.nav_logout) {
            Logout();
        }else if (id == R.id.nav_tutup_toko) {
            showDialog();
        }else if (id == R.id.nav_foto) {
            reqPermission();
        }else if (id == R.id.nav_sop) {
            startActivity(new Intent(ManajemenStokActivity.this, SOPActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setting_printer() {
        startActivity(new Intent(ManajemenStokActivity.this, SettingPrinterActivity.class));
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
                                FancyToast.makeText(ManajemenStokActivity.this,  message,
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

    private void tutup_toko(){
        Dialog dialog       = new Dialog(ManajemenStokActivity.this);
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
                                        ManajemenStokActivity.this,
                                        "Toko Berhasil Ditutup",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        false
                                ).show();
                            }
                            else{
                                FancyToast.makeText(
                                        ManajemenStokActivity.this,
                                        "Gagal Menutup Toko",
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.ERROR,
                                        false
                                ).show();
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(
                                    ManajemenStokActivity.this,
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
                            signOut.Logout(ManajemenStokActivity.this);
                        }
                        else {
                            FancyToast.makeText(
                                    ManajemenStokActivity.this,
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
        Dialog dialog       = new Dialog(ManajemenStokActivity.this);
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

    private void show_tutorial(){
        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(hotlist_bell, "Riwayat Penambahan Stok", "Berisi riwayat serta rincian penambahan stok bahan")
                        // All options below are optional
                        .outerCircleColor(android.R.color.holo_orange_dark)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.putih)   // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.putih)      // Specify the color of the title text
                        .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.merah)  // Specify the color of the description text
                        .textColor(R.color.putih)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.hitam)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)                   // Specify a custom drawable to draw as the target
                        .targetRadius(40),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        new GuideView.Builder(ManajemenStokActivity.this)
                                .setTitle("Nama Stok Bahan")
                                .setContentText("Untuk melakukan Pembelian produk, pilih dan tekan produk untuk menambah jumlah dengan kelipatan 1 atau tekan dan tahan untuk mengisi jumlah lebih dari 1")
                                .setTargetView(list_stok.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_nama_stok))
                                .setContentTextSize(16)//optional
                                .setTitleTextSize(20)//optional
                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                .setGuideListener(new GuideListener() {
                                    @Override
                                    public void onDismiss(View view) {

                                        new GuideView.Builder(ManajemenStokActivity.this)
                                                .setTitle("Jumlah Stok Bahan!")
                                                .setContentText("")
                                                .setTargetView(list_stok.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.txt_jml_stok))
                                                .setContentTextSize(12)//optional
                                                .setTitleTextSize(20)//optional
                                                .setDismissType(DismissType.targetView) //optional - default dismissible by TargetView
                                                .setGuideListener(new GuideListener() {
                                                    @Override
                                                    public void onDismiss(View view) {

                                                        final Dialog dialog     = new Dialog(ManajemenStokActivity.this);
                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                        dialog.setContentView(R.layout.dialog_tutorial_tambah_stok);

                                                        FloatingActionButton fab    = dialog.findViewById(R.id.fab_tutup);
                                                        fab.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialog.dismiss();

                                                                tutorial.setEnabled(true);
                                                                hotlist_bell.setEnabled(true);
                                                            }
                                                        });

                                                        dialog.show();
                                                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnitmation;
                                                        dialog.getWindow().setGravity(Gravity.BOTTOM);

                                                    }
                                                })
                                                .build()
                                                .show();

                                    }
                                })
                                .build()
                                .show();
                    }
                });

    }

    private void reqPermission() {

        if (hasCameraPermissions() || hasLocationPermissions()) {
            Intent intent   = new Intent(ManajemenStokActivity.this, FotoTutupActivity.class);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_stok);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(ManajemenStokActivity.this, HomeActivity.class));
            finish();
            super.onBackPressed();
        }
    }
}