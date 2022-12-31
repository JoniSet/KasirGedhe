package com.example.kasirroti;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kasirroti.DataSqlite.BahanOutletSqlite;
import com.example.kasirroti.DataSqlite.BahanProdukSqlite;
import com.example.kasirroti.DataSqlite.KategoriSqlite;
import com.example.kasirroti.DataSqlite.ProdukSqlite;
import com.example.kasirroti.Helper.SqliteHelper;
import com.example.kasirroti.Helper.Tanggal;
import com.example.kasirroti.Server.Server;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SinkronDataActivity extends AppCompatActivity {
    private TextView txt_title, txt_info;
    private Button btn_sinkron;

    private SqliteHelper sqliteHelper;
    private ArrayList<KategoriSqlite> listKategori;
    private ArrayList<ProdukSqlite> listProduk;
    private ArrayList<BahanProdukSqlite> listBahanProduk;
    private ArrayList<BahanOutletSqlite> listBahanOutlet;
    private String info;

    private Tanggal tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinkron_data);
        sqliteHelper    = new SqliteHelper(SinkronDataActivity.this);
        txt_title       = findViewById(R.id.txt_title);
        txt_info        = findViewById(R.id.txt_info);
        btn_sinkron     = findViewById(R.id.btn_sinkron);

        listKategori    = new ArrayList<>();
        listProduk      = new ArrayList<>();
        listBahanProduk = new ArrayList<>();
        listBahanOutlet = new ArrayList<>();

        tanggal         = new Tanggal();

        btn_sinkron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getKategori();
            }
        });

        cekData();

    }

    private void getKategori() {
        sqliteHelper.delete_kategori();
        sqliteHelper.delete_produk();
        sqliteHelper.delete_bahan_produk();
        sqliteHelper.delete_bahan_outlet();
        sqliteHelper.delete_info();
        Dialog dialog               = new Dialog(SinkronDataActivity.this);
        dialog.show();
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        TextView txt_title          = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Sinkronasi Data");

        AndroidNetworking.post(Server.URL + "kategori")
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
                                    sqliteHelper.add_kategori(jsonObject.getString("id"), jsonObject.getString("nama_kategori"), jsonObject.getString("urutan") );
                                }
                                getProduk(dialog);
                            } else {
                                FancyToast.makeText(SinkronDataActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(SinkronDataActivity.this, "Gagal mengambil data Kategori Produk, Kesalahan Request",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.d("Kategori", String.valueOf(anError.getErrorCode()));
//                        Toast.makeText(HomeActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        if (anError.getErrorCode() == 401){
                            HomeActivity.sm.logout();
                            HomeActivity.sessionManager.storeLogin(HomeActivity.id_outlet, HomeActivity.nama, HomeActivity.notelp, HomeActivity.alamat, HomeActivity.logo, HomeActivity.nama_rek, HomeActivity.no_rek, "0");
                            finish();
                        }
                        else{
                            FancyToast.makeText(SinkronDataActivity.this, "Gagal mengambil data Kategori Produk, Jaringan Bermasalah",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                });
    }

    private void getProduk(Dialog dialog) {
        AndroidNetworking.get(Server.URL + "all-produk")
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
                                JSONArray jsonArray         = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject   = jsonArray.getJSONObject(i);
                                    sqliteHelper.add_produk(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("id_kategori"),
                                            jsonObject.getString("nama_produk"),
                                            jsonObject.getString("harga"),
                                            jsonObject.getString("img"));
                                }
                                getBahanProduk(dialog);

                            } else {
                                FancyToast.makeText(SinkronDataActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(SinkronDataActivity.this, "Gagal mengambil data Produk, Kesalahan Request",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.d("Produk", String.valueOf(anError.getErrorCode()));
                        if (anError.getErrorCode() == 401){
                            HomeActivity.sm.logout();
                            HomeActivity.sessionManager.storeLogin(HomeActivity.id_outlet, HomeActivity.nama, HomeActivity.notelp, HomeActivity.alamat, HomeActivity.logo, HomeActivity.nama_rek, HomeActivity.no_rek, "0");
                            finish();
                        }
                        else{
                            FancyToast.makeText(SinkronDataActivity.this, "Gagal mengambil data Produk, Jaringan Bermasalah",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                });
    }

    private void getBahanProduk(Dialog dialog) {
        AndroidNetworking.get(Server.URL + "all-komposisi_produk")
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
                                JSONArray jsonArray         = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject   = jsonArray.getJSONObject(i);
                                    sqliteHelper.add_bahan_produk(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("id_produk"),
                                            jsonObject.getString("id_bahan"),
                                            jsonObject.getString("terpakai"),
                                            jsonObject.getString("nama_bahan"));
                                }
                                getBahanOutlet(dialog);

                            } else {
                                FancyToast.makeText(SinkronDataActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(SinkronDataActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.d("Bahan Produk", String.valueOf(anError.getErrorCode()));
                        if (anError.getErrorCode() == 401){
                            HomeActivity.sm.logout();
                            HomeActivity.sessionManager.storeLogin(HomeActivity.id_outlet, HomeActivity.nama, HomeActivity.notelp, HomeActivity.alamat, HomeActivity.logo, HomeActivity.nama_rek, HomeActivity.no_rek, "0");
                            finish();
                        }
                        else{
                            FancyToast.makeText(SinkronDataActivity.this, "Gagal mengambil data Bahan Produk, Jaringan Bermasalah",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                });
    }

    private void getBahanOutlet(Dialog dialog) {
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
                                    if (!jsonObject.getString("stok").equals("0")) {
                                        sqliteHelper.add_bahan_outlet(
                                                jsonObject.getString("id_stok"),
                                                jsonObject.getString("id_bahan"),
                                                jsonObject.getString("stok"),
                                                jsonObject.getString("nama_bahan"));
                                    }

                                    Log.d("Salah", jsonArray.toString());
                                }
                                sqliteHelper.add_update_info("1", tanggal.getTanggal() + " " + tanggal.getTime());
                                FancyToast.makeText(SinkronDataActivity.this,  "Sinkronasi data berhasil!" ,
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                dialog.dismiss();
                                txt_title.setText("Sinkronasi data berhasil!\nSinkron Ulang?");
                                txt_info.setText("Terakhir sinkron pada " + sqliteHelper.readAllInfoSqlite("1"));

                                Intent intent       = new Intent(SinkronDataActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                FancyToast.makeText(SinkronDataActivity.this,  message,
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FancyToast.makeText(SinkronDataActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        Log.d("Bahan Outlet", String.valueOf(anError.getErrorCode()));
                        if (anError.getErrorCode() == 401){
                            HomeActivity.sm.logout();
                            HomeActivity.sessionManager.storeLogin(HomeActivity.id_outlet, HomeActivity.nama, HomeActivity.notelp, HomeActivity.alamat, HomeActivity.logo, HomeActivity.nama_rek, HomeActivity.no_rek, "0");
                            finish();
                        }
                        else{
                            FancyToast.makeText(SinkronDataActivity.this, "Gagal mengambil data Bahan Outlet, Jaringan Bermasalah",
                                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                });
    }

    private void cekData() {
        listKategori    = sqliteHelper.readKategoriSqlite();
        listProduk      = sqliteHelper.readAllProdukSqlite();
        listBahanProduk = sqliteHelper.readAllBahanProdukSqlite();
        listBahanOutlet = sqliteHelper.readAllBahanOutletSqlite();
        info            = sqliteHelper.readAllInfoSqlite("1");

        if (listKategori.isEmpty()){
            txt_title.setText("Anda belum melakukan Sinkronasi data!\nSinkron Data Sekarang!");
        }
        else if (listProduk.isEmpty()){
            txt_title.setText("Data Produk kosong\nSinkron Data Sekarang!");
        }
        else if (listBahanProduk.isEmpty()){
            txt_title.setText("Data bahan produk kosong\nSinkron Data Sekarang!");
        }
        else if (listBahanOutlet.isEmpty()){
            txt_title.setText("Data bahan outlet kosong\nSinkron Data Sekarang!");
        }
        else if (info.isEmpty()){
            txt_info.setText("");
        }
        else{
            txt_title.setText("Anda sudah melakukan Sinkronasi data!\nSinkron Ulang?");
            txt_info.setText("Terakhir sinkron pada " + sqliteHelper.readAllInfoSqlite("1"));
        }
    }
}